package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityAgent;
import cn.eeepay.framework.model.HappyBackActivityAgentDetail;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.model.XhlfMerchantTransTotalDay;
import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.FilterPage;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author rpc
 * @description 欢乐返代理商奖励活动查询
 * @date 2019/11/7
 */
public interface HappyBackActivityAgentDao {

    /**
     * 欢乐返代理商奖励活动查询列表
     *
     * @param page
     */
    @SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityAgent")
    @ResultType(HappyBackActivityAgent.class)
    List<HappyBackActivityAgent> selectHappyBackActivityAgent(@Param("page") Page<HappyBackActivityAgent> page,
                                                              @Param("info") HappyBackActivityAgent happyBackActivityAgent);

    @SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityAgent")
    @ResultType(HappyBackActivityAgent.class)
    List<HappyBackActivityAgent> exportExcel(@Param("info") HappyBackActivityAgent happyBackActivityAgent);

    @Select({"SELECT a.*,ai.agent_name FROM hlf_agent_reward_account_detail a left join agent_info ai on ai.agent_no=a.agent_no where a.hlf_agent_reward_order_id=#{id} order by a.agent_level asc"})
    @ResultType(HappyBackActivityAgentDetail.class)
    List<HappyBackActivityAgentDetail> agentAwardDetail(@Param("id") String id);


    @SelectProvider(type = SqlProvider.class, method = "countRewardAmount")
    @ResultType(Map.class)
    Map<String, Object> countRewardAmount(@Param("info") HappyBackActivityAgent happyBackActivityAgent);

    @SelectProvider(type = HappyBackActivityAgentDao.SqlProvider.class, method = "selectAgentRewardAccountStatusByIds")
    @ResultType(Map.class)
    List<HappyBackActivityAgent> selectAgentRewardAccountStatusByIds(@Param("ids") String ids);

    @Update("update hlf_agent_reward_order set scan_target_status=#{info.scanTargetStatus},scan_target_time=#{info.scanTargetTime},scan_account_status=#{info.scanAccountStatus},scan_account_time=#{info.scanAccountTime} where id=#{info.id}")
    int updateHappyBackActivityAgentScan(@Param("info") HappyBackActivityAgent info);

    @Update("update hlf_agent_reward_order set all_target_status=#{info.allTargetStatus},all_target_time=#{info.allTargetTime},all_account_status=#{info.allAccountStatus},all_account_time=#{info.allAccountTime} where id=#{info.id}")
    int updateHappyBackActivityAgentAll(@Param("info") HappyBackActivityAgent info);

    @Update("update hlf_agent_reward_account_detail set scan_account_status=#{info.scanAccountStatus},scan_account_time=#{info.scanAccountTime},scan_remark=#{info.scanRemark} where id=#{info.id}")
    int updateHappyBackActivityAgentDetailScan(@Param("info") HappyBackActivityAgentDetail info);

    @Update("update hlf_agent_reward_account_detail set all_account_status=#{info.allAccountStatus},all_account_time=#{info.allAccountTime},all_remark=#{info.allRemark} where id=#{info.id}")
    int updateHappyBackActivityAgentDetailAll(@Param("info") HappyBackActivityAgentDetail info);

    @Update("update hlf_agent_reward_account_detail set scan_account_status = #{scanAccountStatus} where hlf_agent_reward_order_id = #{hlfAgentRewardOrderId}")
    int updateAgentScanDetailByOrderId(HappyBackActivityAgentDetail accountDetail);

    @Update("update hlf_agent_reward_account_detail set all_account_status = #{allAccountStatus} where hlf_agent_reward_order_id = #{hlfAgentRewardOrderId}")
    int updateAgentAllDetailByOrderId(HappyBackActivityAgentDetail accountDetail);


    class SqlProvider {
        public String selectHappyBackActivityAgent(Map<String, Object> param) {
            SQL sql = new SQL() {{
                SELECT(" a.*," +
                        "mi.team_id,mi.team_entry_id,team.team_name,teamEn.team_entry_name,ach.hard_id,"
                        + "ai.agent_name,"
                        + "ai2.agent_no as oneAgentNo,ai2.agent_name as oneAgentName"
                );
                FROM("hlf_agent_reward_order a");

                LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = a.merchant_no ");
                LEFT_OUTER_JOIN("team_info team ON team.team_id = mi.team_id");
                LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id");
                LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = a.merchant_no and ad.activity_code='009') ");
                LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");

                LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = a.agent_no ");
                LEFT_OUTER_JOIN("agent_info ai2 ON ai2.agent_no = ai.one_level_id ");
            }};
            whereSql(param, sql);
            sql.ORDER_BY("a.active_time desc");
            return sql.toString();
        }

        public String countRewardAmount(Map<String, Object> param) {
            final HappyBackActivityAgent info = (HappyBackActivityAgent) param.get("info");
            SQL sql = new SQL() {{
                SELECT(" SUM(IF(a.scan_account_status = 1 and a.scan_target_status = 2,IFNULL(a.scan_reward_amount,0),0)) scanRewardAmount "
                        + ",SUM(IF(a.scan_account_status = 0 and a.scan_target_status = 2,IFNULL(a.scan_reward_amount,0),0)) scanNoRewardAmount "
                        + ",SUM(IF(a.all_account_status = 1 and a.all_target_status = 2,IFNULL(a.all_reward_amount,0),0)) allRewardAmount "
                        + ",SUM(IF(a.all_account_status = 0 and a.all_target_status = 2,IFNULL(a.all_reward_amount,0),0)) allNoRewardAmount "
                );
                FROM("hlf_agent_reward_order a");

                if(info.getHardId()!=null){
                    LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = a.merchant_no and ad.activity_code='009') ");
                    LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
                }
                LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = a.agent_no ");
                LEFT_OUTER_JOIN("agent_info ai2 ON ai2.agent_no = ai.one_level_id ");
                //WHERE(" a.reward_account_status = #{status}");
            }};
            whereSql(param, sql);
            return sql.toString();
        }


        private void whereSql(Map<String, Object> param, SQL sql) {
            final HappyBackActivityAgent info = (HappyBackActivityAgent) param.get("info");
            if (StringUtils.isNotBlank(info.getActiveOrder())) {
                sql.WHERE(" FIND_IN_SET(a.active_order,#{info.activeOrder})");
            }
            if (StringUtils.isNotBlank(info.getScanTargetStatus())) {
                sql.WHERE(" a.scan_target_status = #{info.scanTargetStatus}");
            }
            if (StringUtils.isNotBlank(info.getScanAccountStatus())) {
                sql.WHERE(" a.scan_account_status = #{info.scanAccountStatus}");
            }
            if (StringUtils.isNotBlank(info.getAllTargetStatus())) {
                sql.WHERE(" a.all_target_status = #{info.allTargetStatus}");
            }
            if (StringUtils.isNotBlank(info.getAllAccountStatus())) {
                sql.WHERE(" a.all_account_status = #{info.allAccountStatus}");
            }
            if (StringUtils.isNotBlank(info.getMerchantNo())) {
                sql.WHERE(" a.merchant_no = #{info.merchantNo}");
            }
           // if (StringUtils.isNotBlank(info.getAgentNode())) {
           //     sql.WHERE(" ai.agent_node = #{info.agentNode}");
           // }
            if (StringUtils.isNotBlank(info.getOneAgentNo())) {
                sql.WHERE(" ai.one_level_id = #{info.oneAgentNo}");
            }
            if (StringUtils.isNotBlank(info.getActiveTimeStart())) {
                sql.WHERE(" a.active_time >= #{info.activeTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getActiveTimeEnd())) {
                sql.WHERE(" a.active_time <= #{info.activeTimeEnd}");
            }

            if (StringUtils.isNotBlank(info.getScanTargetTimeStart())) {
                sql.WHERE(" a.scan_target_time >= #{info.scanTargetTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getScanTargetTimeEnd())) {
                sql.WHERE(" a.scan_target_time <= #{info.scanTargetTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getScanAccountTimeStart())) {
                sql.WHERE(" a.scan_account_time >= #{info.scanAccountTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getScanAccountTimeEnd())) {
                sql.WHERE(" a.scan_account_time <= #{info.scanAccountTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getScanRewardEndTimeStart())) {
                sql.WHERE(" a.scan_reward_end_time >= #{info.scanRewardEndTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getScanRewardEndTimeEnd())) {
                sql.WHERE(" a.scan_reward_end_time <= #{info.scanRewardEndTimeEnd}");
            }

            if (StringUtils.isNotBlank(info.getAllTargetTimeStart())) {
                sql.WHERE(" a.all_target_time >= #{info.allTargetTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getAllTargetTimeEnd())) {
                sql.WHERE(" a.all_target_time <= #{info.allTargetTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getAllAccountTimeStart())) {
                sql.WHERE(" a.all_account_time >= #{info.allAccountTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getAllAccountTimeEnd())) {
                sql.WHERE(" a.all_account_time <= #{info.allAccountTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getAllRewardEndTimeStart())) {
                sql.WHERE(" a.all_reward_end_time >= #{info.allRewardEndTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getAllRewardEndTimeEnd())) {
                sql.WHERE(" a.all_reward_end_time <= #{info.allRewardEndTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getActivityTypeNo())) {
                sql.WHERE(" a.activity_type_no = #{info.activityTypeNo}");
            }
            if (info.getHardId()!=null) {
                sql.WHERE(" ach.hard_id = #{info.hardId}");
            }
            //是否包含下级;
            if (StringUtils.isNotBlank(info.getAgentNode())) {
                if ("1".equals(info.getContainsLower())) {//不包含
                    sql.WHERE(" ai.agent_node = #{info.agentNode} ");
                } else if ("2".equals(info.getContainsLower())) {//仅包含直属
                    String angentNo = info.getAgentNode();
                    List<String> list = StringUtil.strToList(angentNo, "-");
                    String agentN = list.get(list.size() - 1);
                    sql.WHERE(
                            " ai.parent_id=" + agentN + "");
                } else {//包含所有
                    sql.WHERE(" ai.agent_node like CONCAT(#{info.agentNode}, '%') ");
                }
            }


        }

        //批量奖励入账
        public String selectAgentRewardAccountStatusByIds(final Map<String, Object> param) {
            String ids = (String) param.get("ids");
            StringBuffer sb = new StringBuffer();
            sb.append("select");
            sb.append(" haro.* ");
            sb.append(" FROM hlf_agent_reward_order haro ");
            sb.append(" where ((haro.scan_target_status=2 and haro.scan_account_status!=1)");
            sb.append(" or (haro.all_target_status=2 and haro.all_account_status!=1))");
            sb.append(" and haro.id in (" + ids + ")");
            sb.append(" ORDER BY haro.id ");
            return sb.toString();
        }

    }

    @Select("select * from hlf_agent_reward_order " +
            " where scan_target_status = #{info.scanTargetStatus}" +
            " and #{info.scanRewardEndTime} between active_time and scan_reward_end_time")
    List<HappyBackActivityAgent> getScanNeedCountList(@Param("info") HappyBackActivityAgent queryOrder);

    @Select("select * from hlf_agent_reward_order " +
            " where all_target_status = #{info.allTargetStatus}" +
            " and #{info.allRewardEndTime} between active_time and all_reward_end_time")
    List<HappyBackActivityAgent> getAllNeedCountList(@Param("info") HappyBackActivityAgent queryOrder);


    @Select("<script>" +
            "select sum(cto.trans_amount) from collective_trans_order cto " +
            " where cto.merchant_no = #{info.merchantNo} and cto.trans_status = 'SUCCESS' " +
            " and cto.pay_method in ('2','3','6','7') " +
            " and cto.order_type = 0 " +
            "<if test=\"info.transStartTime==null\">"+
            " and cto.trans_time &lt;= #{info.transEndTime} " +
            " </if>" +
            "<if test=\"info.transStartTime!=null\">"+
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime} " +
            " </if>" +
            "</script>")
    @ResultType(BigDecimal.class)
    BigDecimal queryScanTransTotal(@Param("info") HappyBackActivityAgent info);



    @Select("<script>" +
            "select sum(cto.trans_amount) from collective_trans_order cto " +
            " where cto.merchant_no = #{info.merchantNo} and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in (0,5) " +
            "<if test=\"info.transStartTime==null\">"+
            " and cto.trans_time &lt;= #{info.transEndTime} " +
            " </if>" +
            "<if test=\"info.transStartTime!=null\">"+
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime} " +
            " </if>" +
            "</script>")
    @ResultType(BigDecimal.class)
    BigDecimal queryAllTransTotal(@Param("info") HappyBackActivityAgent info);


    @Select("select * from hlf_agent_reward_order where scan_target_status = '2' and scan_account_status = '0'" +
            " and DATE_FORMAT(#{nowDate},'%Y-%m-%d') = DATE_FORMAT(scan_target_time,'%Y-%m-%d')")
    @ResultType(HappyBackActivityAgent.class)
    List<HappyBackActivityAgent> queryScanNeedAccountList(@Param("nowDate") Date nowDate);

    @Select("select * from hlf_agent_reward_order where all_target_status = '2' and all_account_status = '0'" +
            " and DATE_FORMAT(#{nowDate},'%Y-%m-%d') = DATE_FORMAT(all_target_time,'%Y-%m-%d')")
    @ResultType(HappyBackActivityAgent.class)
    List<HappyBackActivityAgent> queryAllNeedAccountList(@Param("nowDate") Date nowDate);


}
