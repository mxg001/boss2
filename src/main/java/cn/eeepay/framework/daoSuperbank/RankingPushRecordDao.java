package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RankingPushRecordInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface RankingPushRecordDao {

    @SelectProvider(type = SqlProvider.class, method = "selectRankingPushRecordPage")
    @ResultType(RankingPushRecordInfo.class)
    List<RankingPushRecordInfo> selectRankingPushRecordPage(@Param("params") Map<String, Object> params, @Param("page") Page<RankingPushRecordInfo> page);

    @SelectProvider(type = SqlProvider.class, method = "selectRankingPushRecordTotalMoneySum")
    @ResultType(String.class)
    String selectRankingPushRecordTotalMoneySum(@Param("params") Map<String,Object> params);

    @SelectProvider(type = SqlProvider.class, method = "selectRankingPushRecordPushTotalMoneySum")
    @ResultType(String.class)
    String selectRankingPushRecordPushTotalMoneySum(@Param("params") Map<String,Object> params);

    @InsertProvider(type = SqlProvider.class, method = "insertList")
    @ResultType(Integer.class)
    int insertRankingPushRecords(@Param("list") List<RankingPushRecordInfo> pushRecords);
    
    @Delete("delete from ranking_push_record where ranking_no=#{rankingNo}")
    int delRankingPushRecords(@Param("rankingNo") String rankingNo);
    
    class SqlProvider {
        public String selectRankingPushRecordPage(Map<String, Object> map) {
            final Map<String, Object> param = (Map<String, Object>) map.get("params");
            String sql = new SQL(){{
            SELECT("record.*,oi.org_name");
            FROM("ranking_push_record record");
            LEFT_OUTER_JOIN("org_info oi ON oi.org_id=record.org_id");
            //排行榜编号
            if (StringUtils.isNotBlank(param.get("rankingNo") == null ? "" : param.get("rankingNo").toString())) {
                WHERE("record.ranking_no = #{params.rankingNo}");
            }
            //统计周期
            if (StringUtils.isNotBlank(param.get("rankingType") == null ? "" : param.get("rankingType").toString())) {
                WHERE("record.ranking_type = #{params.rankingType}");
            }
            //用户ID
            if (StringUtils.isNotBlank(param.get("userCode") == null ? "" : param.get("userCode").toString())) {
                WHERE("record.user_code = #{params.userCode}");
            }
            //手机号
            if (StringUtils.isNotBlank(param.get("phone") == null ? "" : param.get("phone").toString())) {
                WHERE("record.phone = #{params.phone}");
            }
            //发放状态
            if (StringUtils.isNotBlank(param.get("pushStatus") == null ? "" : param.get("pushStatus").toString())) {
                WHERE("record.push_status = #{params.pushStatus}");
            }
            //所属组织
            if (StringUtils.isNotBlank(param.get("orgId") == null ? "" : param.get("orgId").toString())) {
                WHERE("record.org_id = #{params.orgId}");
            }
            //记账状态
            if (StringUtils.isNotBlank(param.get("accountStatus") == null ? "" : param.get("accountStatus").toString())) {
                WHERE("record.account_status = #{params.accountStatus}");
            }
            //统计数据
            if (StringUtils.isNotBlank(param.get("dataType") == null ? "" : param.get("dataType").toString())) {
                WHERE("record.data_type = #{params.dataType}");
            }
            //期号
            if (StringUtils.isNotBlank(param.get("batchNo") == null ? "" : param.get("batchNo").toString())) {
                WHERE("record.batch_no = #{params.batchNo}");
            }
            ORDER_BY("record.push_time desc");
            }}.toString();
            System.out.println(sql);
            return sql;
        }

        public String selectRankingPushRecordTotalMoneySum(Map<String, Object> map) {
            final Map<String, Object> param = (Map<String, Object>) map.get("params");
            String sql = new SQL(){{
                SELECT("sum(record.ranking_money) as rankingMoney");
                FROM("ranking_push_record record");
                //排行榜编号
                if (StringUtils.isNotBlank(param.get("rankingNo") == null ? "" : param.get("rankingNo").toString())) {
                    WHERE("record.ranking_no = #{params.rankingNo}");
                }
                //统计周期
                if (StringUtils.isNotBlank(param.get("rankingType") == null ? "" : param.get("rankingType").toString())) {
                    WHERE("record.ranking_type = #{params.rankingType}");
                }
                //用户ID
                if (StringUtils.isNotBlank(param.get("userCode") == null ? "" : param.get("userCode").toString())) {
                    WHERE("record.user_code = #{params.userCode}");
                }
                //手机号
                if (StringUtils.isNotBlank(param.get("phone") == null ? "" : param.get("phone").toString())) {
                    WHERE("record.phone = #{params.phone}");
                }
                //发放状态
                if (StringUtils.isNotBlank(param.get("pushStatus") == null ? "" : param.get("pushStatus").toString())) {
                    WHERE("record.push_status = #{params.pushStatus}");
                }
                //所属组织
                if (StringUtils.isNotBlank(param.get("orgId") == null ? "" : param.get("orgId").toString())) {
                    WHERE("record.org_id = #{params.orgId}");
                }
                //记账状态
                if (StringUtils.isNotBlank(param.get("accountStatus") == null ? "" : param.get("accountStatus").toString())) {
                    WHERE("record.account_status = #{params.accountStatus}");
                }
                //统计数据
                if (StringUtils.isNotBlank(param.get("dataType") == null ? "" : param.get("dataType").toString())) {
                    WHERE("record.data_type = #{params.dataType}");
                }
                //期号
                if (StringUtils.isNotBlank(param.get("batchNo") == null ? "" : param.get("batchNo").toString())) {
                    WHERE("record.batch_no = #{params.batchNo}");
                }
            }}.toString();
            System.out.println(sql);
            return sql;
        }
        public String selectRankingPushRecordPushTotalMoneySum(Map<String, Object> map) {
            final Map<String, Object> param = (Map<String, Object>) map.get("params");
            String sql = new SQL(){{
                SELECT("sum(record.ranking_money) as rankingMoney");
                FROM("ranking_push_record record");
                //排行榜编号
                if (StringUtils.isNotBlank(param.get("rankingNo") == null ? "" : param.get("rankingNo").toString())) {
                    WHERE("record.ranking_no = #{params.rankingNo}");
                }
                //统计周期
                if (StringUtils.isNotBlank(param.get("rankingType") == null ? "" : param.get("rankingType").toString())) {
                    WHERE("record.ranking_type = #{params.rankingType}");
                }
                //用户ID
                if (StringUtils.isNotBlank(param.get("userCode") == null ? "" : param.get("userCode").toString())) {
                    WHERE("record.user_code = #{params.userCode}");
                }
                //手机号
                if (StringUtils.isNotBlank(param.get("phone") == null ? "" : param.get("phone").toString())) {
                    WHERE("record.phone = #{params.phone}");
                }
                //发放状态
                param.put("pushStatus",1);
                WHERE("record.push_status = #{params.pushStatus}");
                //所属组织
                if (StringUtils.isNotBlank(param.get("orgId") == null ? "" : param.get("orgId").toString())) {
                    WHERE("record.org_id = #{params.orgId}");
                }
                //记账状态
                if (StringUtils.isNotBlank(param.get("accountStatus") == null ? "" : param.get("accountStatus").toString())) {
                    WHERE("record.account_status = #{params.accountStatus}");
                }//统计数据
                if (StringUtils.isNotBlank(param.get("dataType") == null ? "" : param.get("dataType").toString())) {
                    WHERE("record.data_type = #{params.dataType}");
                }
                //期号
                if (StringUtils.isNotBlank(param.get("batchNo") == null ? "" : param.get("batchNo").toString())) {
                    WHERE("record.batch_no = #{params.batchNo}");
                }
            }}.toString();
            System.out.println(sql);
            return sql;
        }

        public String insertList(Map<String,Object> map){
        	List<RankingPushRecordInfo> list = (List<RankingPushRecordInfo>)map.get("list");
        	StringBuilder sb = new StringBuilder();
        	String columns = "order_no,ranking_no,batch_no,rule_no,ranking_name,ranking_type,org_id,show_no,user_name,nick_name,user_code,phone,ranking_data,ranking_level,ranking_money,push_status,account_status,remove_remark,push_time,remove_time,data_type,start_time,end_time";
        	sb.append("INSERT INTO ranking_push_record ");
        	sb.append(" ("+columns+") ");
        	sb.append(" values ");
        	MessageFormat mf = new MessageFormat("(#'{'list[{0}].orderNo},#'{'list[{0}].rankingNo},#'{'list[{0}].batchNo},#'{'list[{0}].ruleNo},#'{'list[{0}].rankingName},#'{'list[{0}].rankingType},#'{'list[{0}].orgId},#'{'list[{0}].showNo},#'{'list[{0}].userName},#'{'list[{0}].nickName},#'{'list[{0}].userCode},#'{'list[{0}].phone},#'{'list[{0}].rankingData},#'{'list[{0}].rankingLevel},#'{'list[{0}].rankingMoney},#'{'list[{0}].pushStatus},#'{'list[{0}].accountStatus},#'{'list[{0}].removeRemark},#'{'list[{0}].pushTime},#'{'list[{0}].removeTime},#'{'list[{0}].dataType},#'{'list[{0}].startTime},#'{'list[{0}].endTime} )");
        	
        	for (int i = 0; i < list.size(); i++) {
				sb.append(mf.format(new Object[]{i}));
				if(i < list.size()-1){
					sb.append(",");
				}
			}
        	return sb.toString();
        }
    }
}
