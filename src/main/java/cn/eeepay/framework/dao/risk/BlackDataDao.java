package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRules;
import cn.eeepay.framework.model.risk.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/12/21
 */
public interface BlackDataDao {

    @SelectProvider(type = SqlProvider.class, method = "selectByParam")
    @ResultType(BlackDataInfo.class)
    List<BlackDataInfo> selectByParamWithPage(@Param("page") Page<BlackDataInfo> page, @Param("info") BlackDataInfo blackDataInfo);

    @SelectProvider(type = SqlProvider.class, method = "selectByParam")
    @ResultType(BlackDataInfo.class)
    List<BlackDataInfo> selectByParam(@Param("info") BlackDataInfo blackDataInfo);

    @Select("select team_id as value,team_name as text from team_info")
    @ResultType(Map.class)
    List<Map> selectTeamList();

    @Select("select bdi.*," +
            "date_format(bdi.create_time,'%Y-%m-%d %H:%i:%s') as createTime," +
            "mi.merchant_name,mi.lawyer,mi.mobilephone as merchantPhone,mi.recommended_source,mi.id_card_no as merchantIdCard," +
            "cto.account_no,bsu.user_name as blackCreator,ti.team_name " +
            "from black_data_info bdi " +
            "left join merchant_info mi ON bdi.merchant_no=mi.merchant_no " +
            "left join collective_trans_order cto ON cto.order_no=bdi.trans_order_no " +
            "left join boss_shiro_user bsu ON bsu.id=bdi.black_creator " +
            "left join team_info ti ON ti.team_id=bdi.team_id " +
            "where bdi.id=#{id}")
    @ResultType(BlackDataInfo.class)
    BlackDataInfo selectById(@Param("id") String id);

    @Select("SELECT rules_no,rules_engine,rules_values FROM risk_rules WHERE LOCATE(rules_no, #{rulesNos})")
    @ResultType(RiskRules.class)
    List<RiskRules> selectRulesList(@Param("rulesNos") String rulesNos);

    @SelectProvider(type = SqlProvider.class, method = "selectReplyRecord")
    @ResultType(ReplyRecord.class)
    ReplyRecord selectReplyRecord(@Param("orderNo") String orderNo);

    @Select("select *,date_format(operate_time,'%Y-%m-%d %H:%i:%s') as operateTime " +
            "from black_data_deal_log where orig_order_no=#{orderNo} order by operateTime DESC limit 20 ")
    @ResultType(BlackDataDealLog.class)
    List<BlackDataDealLog> selectLogByOrderNo(@Param("orderNo") String orderNo);

    @Update("update black_data_info set " +
            "risk_last_deal_status=#{riskStatus}," +
            "mer_last_deal_status=#{merStatus}," +
            "risk_last_deal_time=now(),risk_last_deal_operator=#{riskLastDealOperator} " +
            "where order_no=#{orderNo}")
    int updateDealStatus(@Param("orderNo") String orderNo, @Param("riskStatus") String riskStatus, @Param("merStatus") String merStatus, @Param("riskLastDealOperator") String riskLastDealOperator);

    @Update("update black_data_info set risk_last_remark=#{remark} where id=#{id}")
    int addRemark(@Param("id") String id, @Param("remark") String remark);

    @SelectProvider(type = SqlProvider.class, method = "selectDealReplyRecord")
    @ResultType(DealReplyRecord.class)
    List<DealReplyRecord> selectDealReplyRecord(@Param("orderNo") String orderNo);

    @Insert("INSERT INTO black_data_deal_log(orig_order_no,operate_type,operator,operate_table,pre_value,after_value,operate_detail,operate_time) " +
            "VALUES(#{log.origOrderNo},#{log.operateType},#{log.operator},#{log.operateTable}," +
            "#{log.preValue},#{log.afterValue},#{log.operateDetail},now())")
    int addLog(@Param("log") BlackDataDealLog log);

    @Update("update black_data_info set " +
            "risk_last_deal_status=#{status}," +
            "risk_last_deal_time=now()," +
            "risk_last_deal_operator=#{riskLastDealOperator}" +
            "where order_no=#{orderNo}")
    int updateRiskDealStatus(@Param("orderNo") String orderNo, @Param("status") String status, @Param("riskLastDealOperator") String riskLastDealOperator);

    @Select("SELECT id FROM merchant_business_product WHERE merchant_no=#{merchantNo} ORDER BY id asc LIMIT 1")
    @ResultType(String.class)
    String queryMbpId(@Param("merchantNo") String merchantNo);

    @Select("select * from black_data_info where order_no=#{orderNo}")
    @ResultType(BlackDataInfo.class)
    BlackDataInfo selectByOrderNo(@Param("orderNo") String orderNo);

    class SqlProvider {
    	
    	public String selectDealReplyRecord(Map<String,Object> params) {
    		StringBuilder sqlSb = new StringBuilder();
    		sqlSb.append(" SELECT ");
    		sqlSb.append("   bdi.mer_risk_rules_no AS rulesNo, ");
    		sqlSb.append("   bddr.risk_deal_msg AS dealMsg, ");
    		sqlSb.append("   date_format( bddr.create_time, '%Y-%m-%d %H:%i:%s' ) AS dealTime,  ");
    		sqlSb.append("   bdrr.reply_remark AS replyMsg,  ");
    		sqlSb.append("   date_format( bdrr.create_time, '%Y-%m-%d %H:%i:%s' ) AS replyTime,  ");
    		sqlSb.append("   bdrr.reply_files_name AS filesName,  ");
    		sqlSb.append("   bdrr.replier_type,  ");
    		sqlSb.append("   concat( agent_info0.agent_name, '(', agent_info0.agent_no, ')' ) AS merchant_name  ");
    		sqlSb.append(" FROM ");
    		sqlSb.append("   black_data_deal_record bddr ");
    		sqlSb.append("   LEFT JOIN black_data_info bdi ON bdi.order_no = bddr.orig_order_no ");
    		sqlSb.append("   LEFT JOIN ( SELECT * FROM black_data_reply_record WHERE STATUS = '1' ) bdrr ON bdrr.deal_record_order_no = bddr.order_no ");
    		sqlSb.append("   LEFT JOIN agent_info agent_info0 ON agent_info0.agent_no = bdrr.merchant_no  ");
    		sqlSb.append(" WHERE ");
    		sqlSb.append("   bddr.orig_order_no = #{orderNo} ");
    		sqlSb.append("   AND bddr.STATUS = '2' ");
    		sqlSb.append(" ORDER BY ");
    		sqlSb.append("   dealTime ASC ");
    		return sqlSb.toString();
    	}
    	
    	public String selectReplyRecord(Map<String,Object> param) {
    		StringBuilder sqlSb = new StringBuilder();
    		sqlSb.append(" SELECT ");
    		sqlSb.append("   black_data_reply_record0.*, ");
    		sqlSb.append("   date_format( create_time, '%Y-%m-%d %H:%i:%s' ) AS createTime, ");
    		sqlSb.append("   concat( agent_info0.agent_name, '(', merchant_no, ')' ) AS merchant_name  ");
    		sqlSb.append(" FROM ");
    		sqlSb.append("   black_data_reply_record black_data_reply_record0");
    		sqlSb.append("   LEFT JOIN agent_info agent_info0 ON agent_info0.agent_no = black_data_reply_record0.merchant_no ");
    		sqlSb.append(" WHERE ");
    		sqlSb.append("   black_data_reply_record0.orig_order_no = #{orderNo} ");
    		sqlSb.append(" ORDER BY ");
    		sqlSb.append("   black_data_reply_record0.create_time DESC ");
    		sqlSb.append("   LIMIT 1 ");
    		return sqlSb.toString();
    	}

        private SQL baseSql(){
            return new SQL(){
                {
                    SELECT("bdi.*," +
                            "date_format(bdi.create_time,'%Y-%m-%d %H:%i:%s') as createTime," +
                            "date_format(bdi.risk_last_deal_time,'%Y-%m-%d %H:%i:%s') as riskLastDealTime," +
                            "date_format(bdi.mer_last_deal_time,'%Y-%m-%d %H:%i:%s') as merLastDealTime," +
                            "mi.merchant_name,mi.lawyer,mi.mobilephone as merchantPhone,mi.recommended_source," +
                            "ai.agent_no,ai.agent_name,team_name,bsu.user_name AS blackCreator");
                    FROM("black_data_info bdi");
                    LEFT_OUTER_JOIN("merchant_info mi ON bdi.merchant_no=mi.merchant_no");
                    LEFT_OUTER_JOIN("agent_info ai ON bdi.agent_node=ai.agent_node");
                    LEFT_OUTER_JOIN("team_info ti ON ti.team_id=bdi.team_id");
                    LEFT_OUTER_JOIN("boss_shiro_user bsu ON bsu.id=bdi.black_creator");
                }
            };
        }

        public String selectByParam(Map<String, Object> param){
            final BlackDataInfo info = (BlackDataInfo) param.get("info");
            SQL sql = baseSql();
            sqlWhere(sql, info);
            sql.ORDER_BY("bdi.create_time DESC");
            return sql.toString();
        }

        private void sqlWhere(SQL sql, BlackDataInfo info) {
            sql.WHERE("bdi.mer_last_deal_status <> '0' and !(mer_last_deal_status='1' and risk_last_deal_status ='0')");
            if(StringUtils.isNotBlank(info.getMerchantNo())){
                sql.WHERE("bdi.merchant_no=#{info.merchantNo}");
            }
            if(StringUtils.isNotBlank(info.getMerchantName())){
                sql.WHERE("mi.merchant_name like concat('%',#{info.merchantName},'%')");
            }
            if(StringUtils.isNotBlank(info.getLawyer())){
                sql.WHERE("mi.lawyer like concat('%',#{info.lawyer},'%')");
            }
            if(StringUtils.isNotBlank(info.getMerchantPhone())){
                sql.WHERE("mi.mobilephone=#{info.merchantPhone}");
            }
            if(StringUtils.isNotBlank(info.getRecommendedSource())){
                sql.WHERE("mi.recommended_source=#{info.recommendedSource}");
            }
            if(StringUtils.isNotBlank(info.getMerRiskRulesNo())){
                sql.WHERE("LOCATE(#{info.merRiskRulesNo}, bdi.mer_risk_rules_no)>0");
            }
            if(StringUtils.isNotBlank(info.getRiskLastDealOperator())){
                sql.WHERE("bdi.risk_last_deal_operator like concat('%',#{info.riskLastDealOperator},'%')");
            }
            if(StringUtils.isNotBlank(info.getRiskLastDealStatus())){
                sql.WHERE("bdi.risk_last_deal_status=#{info.riskLastDealStatus}");
            }
            if(StringUtils.isNotBlank(info.getAgentName())){
                sql.WHERE("ai.agent_name like concat('%',#{info.agentName},'%') or ai.agent_no=#{info.agentName}");
            }
            if(StringUtils.isNotBlank(info.getCreateTimeBegin())){
                sql.WHERE("bdi.create_time>=#{info.createTimeBegin}");
            }
            if(StringUtils.isNotBlank(info.getCreateTimeEnd())){
                sql.WHERE("bdi.create_time<=#{info.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getRiskLastDealTimeBegin())){
                sql.WHERE("bdi.risk_last_deal_time>=#{info.riskLastDealTimeBegin}");
            }
            if(StringUtils.isNotBlank(info.getRiskLastDealTimeEnd())){
                sql.WHERE("bdi.risk_last_deal_time<=#{info.riskLastDealTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getMerLastDealTimeBegin())){
                sql.WHERE("bdi.mer_last_deal_time>=#{info.merLastDealTimeBegin}");
            }
            if(StringUtils.isNotBlank(info.getMerLastDealTimeEnd())){
                sql.WHERE("bdi.mer_last_deal_time<=#{info.merLastDealTimeEnd}");
            }
            if(StringUtils.isNotBlank(info.getHaveTriggerHis())){
                sql.WHERE("bdi.have_trigger_his=#{info.haveTriggerHis}");
            }
            if(StringUtils.isNotBlank(info.getTeamId())){
                sql.WHERE("bdi.team_id=#{info.teamId}");
            }
        }
    }
}
