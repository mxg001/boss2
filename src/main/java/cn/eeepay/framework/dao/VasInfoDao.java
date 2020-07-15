package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface VasInfoDao {

    @Insert(
            " INSERT INTO vas_rate " +
                    " (vas_service_no,team_id,team_entry_id,rate_type," +
                    "  single_num_amount,rate,merchant_type,order_type,service_type," +
                    "  create_time,operator) " +
                    " VALUES " +
                    " (#{info.vasServiceNo},#{info.teamId},#{info.teamEntryId},#{info.rateType}," +
                    " #{info.singleNumAmount},#{info.rate},#{info.merchantType},#{info.orderType},#{info.serviceType}," +
                    "  now(),#{info.operator}) "
    )
    int insertVasRate(@Param("info") VasRate info);

    @Insert(
            " INSERT INTO vas_share_rule " +
                    " (vas_service_no,team_id,team_entry_id,agent_no,rate_type," +
                    "per_fix_cost,cost_rate,share_profit_percent," +
                    "profit_switch," +
                    "create_time,operator) " +
                    " VALUES " +
                    " (#{info.vasServiceNo},#{info.teamId},#{info.teamEntryId},#{info.agentNo},#{info.rateType}," +
                    "#{info.perFixCost},#{info.costRate},#{info.shareProfitPercent}," +
                    "#{info.profitSwitch}," +
                    "now(),#{info.operator}) "
    )
    int insertVasShareRule(@Param("info") VasShareRule info);

    @Insert(
            " INSERT INTO vas_share_rule " +
                    " (vas_service_no,team_id,team_entry_id,agent_no,rate_type," +
                    " per_fix_cost,cost_rate,share_profit_percent," +
                    " profit_switch," +
                    "create_time,operator) " +
                    " VALUES " +
                    " (#{info.vasServiceNo},#{info.teamId},#{info.teamEntryId},#{info.agentNo},#{info.rateType}," +
                    " #{info.perFixCost},#{info.costRate},#{info.shareProfitPercent}," +
                    " #{info.profitSwitch}," +
                    "now(),#{info.operator}) "
    )
    int insertVasShareRuleByAgent(@Param("info") VasShareRule info);

    @Select(
            "select * from vas_service_info where source_service_no=#{functionNumber} and service_source=#{serviceSource} "
    )
    VasServiceInfo getVasServiceInfo(@Param("functionNumber") String functionNumber, @Param("serviceSource") String serviceSource);

    @Select(
            "select * from vas_rate where vas_service_no=#{vasServiceNo} and team_id=#{teamId} and team_entry_id is null"
    )
    VasRate getVasRateByTeamIdOnly(@Param("vasServiceNo") String vasServiceNo, @Param("teamId") String teamId);


    @Select(
            "select * from vas_rate where vas_service_no=#{vasServiceNo} and team_id=#{teamId} and team_entry_id=#{teamEntryId} "
    )
    VasRate getVasRate(@Param("vasServiceNo") String vasServiceNo, @Param("teamId") String teamId, @Param("teamEntryId") String teamEntryId);


    @Select(
            "select * from vas_share_rule where id=#{id} "
    )
    VasShareRule getVasShareRuleByVarId(@Param("id") String id);

//    @Select(
//            "select * from vas_share_rule where vas_service_no=#{vasServiceNo} and team_id=#{teamId} and team_entry_id=#{teamEntryId} and agent_no=#{agentNo} "
//    )
    @SelectProvider(type = SqlProvider.class, method = "getVasShareRuleByTeamId")
    @ResultType(VasShareRule.class)
    VasShareRule getVasShareRuleByTeamId(@Param("info")VasShareRule info);



    @Update(
            " update vas_rate set " +
                    "single_num_amount=#{info.singleNumAmount},rate=#{info.rate}," +
                    "merchant_type=#{info.merchantType},order_type=#{info.orderType},service_type=#{info.serviceType},operator=#{info.operator} " +
                    "where id=#{info.id}"
    )
    int updateVasRate(@Param("info") VasRate info);


    @SelectProvider(type = SqlProvider.class, method = "vasShareRuleQuery")
    @ResultType(VasShareRule.class)
    List<VasShareRule> vasShareRuleQuery(@Param("params") Map<String, String> params, Page<VasShareRule> page);

    @SelectProvider(type = SqlProvider.class, method = "vasShareRuleTaskQuery")
    @ResultType(VasShareRuleTask.class)
    List<VasShareRuleTask> vasShareRuleTaskQuery(@Param("params") Map<String, String> params, Page<VasShareRuleTask> page);

    @Select(" select * from vas_share_rule_task where effective_status=0")
    @ResultType(VasShareRuleTask.class)
    List<VasShareRuleTask> getShareRuleTaskNeedUpdate();

    @Select(" select * from vas_share_rule_task where vas_rule_id=#{vasRuleId} and effective_status=#{effectiveStatus} limit 0,1")
    @ResultType(VasShareRuleTask.class)
    VasShareRuleTask getShareRuleTaskByRuleId(@Param("vasRuleId") int vasRuleId,@Param("effectiveStatus") int effectiveStatus);

    @Select(" select ai.agent_no from agent_info ai " +
            " inner JOIN agent_business_product abp on abp.agent_no = ai.agent_no" +
            " inner JOIN business_product_define bpd on bpd.bp_id = abp.bp_id" +
            " where ai.agent_level = '1' and bpd.team_id = #{teamId}" +
            " and not exists (" +
            " select 1 from vas_share_rule var where var.agent_no = ai.agent_no" +
            " and var.vas_service_no =#{vasServiceNo} " +
            //" and var.team_entry_id = #{teamEntryId}" +
            " and IFNULL(var.team_entry_id,1) = IFNULL(#{teamEntryId},1)" +
            " and var.team_id = bpd.team_id" +
            ") GROUP BY ai.agent_no"
    )
    @ResultType(AgentInfo.class)
    List<AgentInfo> getAgentNoNeedUpdate(@Param("teamId") String teamId, @Param("teamEntryId") String teamEntryId, @Param("vasServiceNo") String vasServiceNo);


    @Update("update vas_share_rule set profit_switch=#{profitSwitch} where id=#{id}")
    int updateVasShareRuleSwitch(VasShareRule info);

    @Update("update vas_share_rule vsr " +
            "left join agent_info ai on vsr.agent_no=ai.agent_no " +
            "set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id=#{teamEntryId} and ai.agent_node like #{agentNo}")
    int updateVasShareRuleSwitchByAgentNode(VasShareRule info);

    @Update("update vas_share_rule vsr " +
            "left join agent_info ai on vsr.agent_no=ai.agent_no " +
            "set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id is null and ai.agent_node like #{agentNo}")
    int updateVasShareRuleSwitchByAgentNode2(VasShareRule info);

    @Update("update vas_share_rule vsr set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id=#{teamEntryId}")
    int updateVasShareRuleSwitchByTeamId(VasShareRule info);

    @Update("update vas_share_rule vsr set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id is null")
    int updateVasShareRuleSwitchByTeamId2(VasShareRule info);

    @Update("update vas_share_rule vsr " +
            "left join agent_info ai on vsr.agent_no=ai.agent_no " +
            "set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id=#{teamEntryId} and ai.agent_level=1")
    int updateVasShareRuleSwitchByAgentLevel(VasShareRule info);


    @Update("update vas_share_rule vsr " +
            "left join agent_info ai on vsr.agent_no=ai.agent_no " +
            "set vsr.profit_switch=#{profitSwitch} where vsr.vas_service_no=#{vasServiceNo} and vsr.team_id=#{teamId} and vsr.team_entry_id is null and ai.agent_level=1")
    int updateVasShareRuleSwitchByAgentLevel2(VasShareRule info);



    @Update("update vas_share_rule_task set effective_status=#{effectiveStatus},effective_date=now() where id=#{id}")
    int updateVasShareRuleTaskStatus(VasShareRuleTask info);

    //生效改成已失效
    @Update("update vas_share_rule_task set effective_status=2 where vas_rule_id=#{vasRuleId} and effective_status=1")
    int updateVasShareRuleTaskStatusToEffect(@Param("vasRuleId") int vasRuleId);

    @Select(" select vsr.*,vr.rate_type from vas_share_rule vsr " +
            "LEFT JOIN vas_rate vr on vsr.vas_service_no=vr.vas_service_no and vsr.team_id=vr.team_id and ((vsr.team_entry_id is null and vr.team_entry_id is null) or (vsr.team_entry_id=vr.team_entry_id))  " +
            "where vsr.id=#{id}")
    @ResultType(VasShareRule.class)
    VasShareRule getVasShareRuleById(@Param("id") int id);


    @SelectProvider(type = SqlProvider.class, method = "getVasShareRuleMaxByRate")
    @ResultType(VasShareRule.class)
    VasShareRule getVasShareRuleMaxByRate(@Param("info")VasRate info);

    @SelectProvider(type = SqlProvider.class, method = "getVasShareRuleMaxByRule")
    @ResultType(VasShareRule.class)
    VasShareRule getVasShareRuleMaxByRule(@Param("info")VasShareRule info);



    @Select(" select abp.agent_no,vsr.vas_service_no,vsr.team_id,vsr.team_entry_id," +
            " vsr.profit_switch," +
            " vsr.rate_type,vsr.default_per_fix_cost,vsr.default_cost_rate,vsr.default_share_profit_percent " +
            " from agent_business_product abp " +
            " INNER JOIN business_product_define bpd on bpd.bp_id = abp.bp_id" +
            " INNER JOIN vas_share_rule vsr on vsr.team_id = bpd.team_id"+
            " where abp.agent_no = #{agentNo} and vsr.agent_no = '0'" +
            " and not exists ("+
            " select 1 from vas_share_rule var where var.agent_no = abp.agent_no" +
            " and var.team_id = bpd.team_id"+
            " ) GROUP BY abp.agent_no"
    )
    @ResultType(VasShareRule.class)
    List<VasShareRule> getVasShareRuleByAgentNo(@Param("agentNo") String agentNo);

    @Update(
            " update vas_share_rule set " +
                    "agent_per_fix_cost=#{info.agentPerFixCost},agent_cost_rate=#{info.agentCostRate}," +
                    "default_per_fix_cost=#{info.defaultPerFixCost},default_cost_rate=#{info.defaultCostRate}," +
                    "default_share_profit_percent=#{info.defaultShareProfitPercent}," +
                    "remark=#{info.remark},operator=#{info.operator} " +
                    "where id=#{info.id}"
    )
    int updateVasShareRule(@Param("info") VasShareRule info);

    @Update(
            " update vas_share_rule set " +
                    "per_fix_cost=#{info.perFixCost},cost_rate=#{info.costRate}," +
                    "share_profit_percent=#{info.shareProfitPercent}," +
                    "effective_date=#{info.effectiveDate}," +
                    "operator=#{info.operator} " +
                    "where id=#{info.id}"
    )
    int updateAgentVasShareRule(@Param("info") VasShareRule info);

    @Insert(
            " INSERT INTO vas_share_rule_task " +
                    " (" +
                    "vas_rule_id,rate_type," +
                    "per_fix_cost,cost_rate,share_profit_percent," +
                    "agent_per_fix_cost,agent_cost_rate," +
                    "default_per_fix_cost,default_cost_rate,default_share_profit_percent," +
                    "effective_date,effective_status,remark,create_time,operator" +
                    ") " +
                    " VALUES " +
                    " (" +
                    "#{info.vasRuleId},#{info.rateType}," +
                    "#{info.perFixCost},#{info.costRate},#{info.shareProfitPercent}," +
                    "#{info.agentPerFixCost},#{info.agentCostRate}," +
                    "#{info.defaultPerFixCost},#{info.defaultCostRate},#{info.defaultShareProfitPercent}," +
                    "#{info.effectiveDate},#{info.effectiveStatus},#{info.remark},now(),#{info.operator}" +
                    ") "
    )
    int insertVasShareRuleTask(@Param("info") VasShareRuleTask info);


    public class SqlProvider {

        public String getVasShareRuleByTeamId(final Map<String, Object> params) {

            final VasShareRule info = (VasShareRule) params.get("info");

            final SQL sql = new SQL() {
                {
                    SELECT("vsr.*");
                    FROM(
                            "vas_share_rule vsr"
                    );

                }
            };
            sql.WHERE("vsr.vas_service_no=#{info.vasServiceNo}");
            sql.WHERE("vsr.team_id=#{info.teamId}");
            sql.WHERE("vsr.agent_no=#{info.agentNo}");
            if(info.getTeamEntryId()!=null){
                sql.WHERE("vsr.team_entry_id=#{info.teamEntryId}");
            }else{
                sql.WHERE("vsr.team_entry_id is null");
            }
            return sql.toString();
        }

        public String getVasShareRuleMaxByRate(final Map<String, Object> params) {

            final VasRate info = (VasRate) params.get("info");

            final SQL sql = new SQL() {
                {
                    SELECT("vsr.*");
                    FROM(
                            "vas_share_rule vsr" +
                            " left join agent_info ai on ai.agent_no=vsr.agent_no"
                    );

                }
            };
            sql.WHERE("ai.agent_level = '1'");
            sql.WHERE("vsr.vas_service_no =#{info.vasServiceNo}");
            sql.WHERE("vsr.team_id =#{info.teamId}");
            if(info.getTeamEntryId()!=null){
                sql.WHERE("vsr.team_entry_id =#{info.teamEntryId}");
            }
            if("1".equals(info.getRateType())){
                sql.ORDER_BY(" vsr.per_fix_cost desc ");
            }else{
                sql.ORDER_BY(" vsr.cost_rate desc ");
            }
            System.out.println("sql.toString()=" + sql.toString());
            System.out.println("info=" + info.toString());
            return sql.toString()+" limit 0,1";
        }

        public String getVasShareRuleMaxByRule(final Map<String, Object> params) {

            final VasShareRule info = (VasShareRule) params.get("info");

            final SQL sql = new SQL() {
                {
                    SELECT("vsr.*");
                    FROM(
                            "vas_share_rule vsr" +
                                    " left join agent_info ai on ai.agent_no=vsr.agent_no"
                    );

                }
            };
            sql.WHERE("ai.agent_level = '1'");
            sql.WHERE("vsr.vas_service_no =#{info.vasServiceNo}");
            sql.WHERE("vsr.team_id =#{info.teamId}");
            if(info.getTeamEntryId()!=null){
                sql.WHERE("vsr.team_entry_id =#{info.teamEntryId}");
            }
            if("1".equals(info.getRateType())){
                sql.ORDER_BY(" vsr.per_fix_cost desc ");
            }else{
                sql.ORDER_BY(" vsr.cost_rate desc ");
            }
            System.out.println("sql.toString()=" + sql.toString());
            System.out.println("info=" + info.toString());
            return sql.toString()+" limit 0,1";
        }


        public String vasShareRuleQuery(final Map<String, Object> params) {

            final Map<String, String> param = (Map<String, String>) params.get("params");

            final SQL sql = new SQL() {
                {
                    SELECT(" vsr.*," +
                            /* "vr.rate_type," +*/
                            "vr.single_num_amount,vr.rate," +
                            "ai.agent_name," +
                            "ai.one_level_id," +
                            "ai.parent_id," +
                            "ai.agent_level," +
                            "parentAi.agent_name as parent_agent_name," +
                            "ti.team_name,tie.team_entry_name,vsi.vas_service_name");
                    FROM(fromSql());

                }
            };
            whereSql(param, sql);
            //sql.GROUP_BY(" v.order_no");
            //sql.GROUP_BY(" v.type ");
            //sql.ORDER_BY(" v.id desc ");
            sql.ORDER_BY(" vsr.id desc ");
            System.out.println("sql.toString()=" + sql.toString());
            return sql.toString();
        }

        public String vasShareRuleTaskQuery(final Map<String, Object> params) {

            final Map<String, String> param = (Map<String, String>) params.get("params");

            final SQL sql = new SQL() {
                {
                    SELECT(" vsrt.*," +
                            "vr.single_num_amount,vr.rate," +
                            "ti.team_name,tie.team_entry_name,vsi.vas_service_name ");
                    FROM(fromSqlTask());

                }
            };
            whereSqlTask(param, sql);
            sql.ORDER_BY(" vsrt.id desc ");
            return sql.toString();
        }


        public String fromSql() {
            StringBuilder sb = new StringBuilder(" vas_share_rule vsr ");
            sb.append(" LEFT JOIN vas_rate vr on vsr.vas_service_no=vr.vas_service_no and vsr.team_id=vr.team_id and ((vsr.team_entry_id is null and vr.team_entry_id is null) or (vsr.team_entry_id=vr.team_entry_id)) ");
            sb.append(" LEFT JOIN vas_service_info vsi on vsr.vas_service_no=vsi.vas_service_no ");
            sb.append(" LEFT JOIN team_info ti on vsr.team_id=ti.team_id ");
            sb.append(" LEFT JOIN team_info_entry tie on vsr.team_entry_id=tie.team_entry_id ");
            sb.append(" LEFT JOIN agent_info ai on vsr.agent_no=ai.agent_no ");
            sb.append(" LEFT JOIN agent_info parentAi on parentAi.agent_no=ai.parent_id ");
            return sb.toString();
        }

        public String fromSqlTask() {
            StringBuilder sb = new StringBuilder(" vas_share_rule_task vsrt ");
            sb.append(" LEFT JOIN vas_share_rule vsr on vsr.id=vsrt.vas_rule_id ");
            sb.append(" LEFT JOIN vas_rate vr on vsr.vas_service_no=vr.vas_service_no and vsr.team_id=vr.team_id and ((vsr.team_entry_id is null and vr.team_entry_id is null) or (vsr.team_entry_id=vr.team_entry_id)) ");
            sb.append(" LEFT JOIN vas_service_info vsi on vsr.vas_service_no=vsi.vas_service_no ");
            sb.append(" LEFT JOIN team_info ti on vsr.team_id=ti.team_id ");
            sb.append(" LEFT JOIN team_info_entry tie on vsr.team_entry_id=tie.team_entry_id ");
            return sb.toString();
        }

        public void whereSql(Map<String, String> param, SQL sql) {
            if (param == null) {
                return;
            }
            String fromType = param.get("fromType");
            if ("0".equals(fromType)) {//agentNo=0
                sql.WHERE("vsr.agent_no = 0");
            } else {
                sql.WHERE("vsr.agent_no > 0");
            }
           /* if (!StringUtil.isEmpty(param.get("agentNo"))) {
                sql.WHERE("vsr.agent_no = #{params.agentNo}");
            }*/
            if (!StringUtil.isEmpty(param.get("vasServiceNo"))) {
                sql.WHERE("vsr.vas_service_no = #{params.vasServiceNo}");
            }
            if (!StringUtil.isEmpty(param.get("teamId"))) {
                sql.WHERE("vsr.team_id = #{params.teamId}");
            }
            if (!StringUtil.isEmpty(param.get("teamEntryId"))) {
                sql.WHERE("vsr.team_entry_id = #{params.teamEntryId}");
            }
            if (!StringUtil.isEmpty(param.get("profitSwitch"))) {
                sql.WHERE("vsr.profit_switch = #{params.profitSwitch}");
            }
            if (!StringUtil.isEmpty(param.get("mobilephone"))) {
                sql.WHERE("ai.mobilephone = #{params.mobilephone}");
            }
            if (!StringUtil.isEmpty(param.get("agentLevel"))) {
                if (!"-1".equals(param.get("agentLevel") + "")) {
                    sql.WHERE("ai.agent_level = #{params.agentLevel}");
                }
            }
            //是否包含下级;
            if(StringUtils.isNotBlank((String)param.get("agentNo"))){
                if(StringUtils.isNotBlank((String)param.get("subAgentNo"))){
                    sql.WHERE("ai.agent_node like concat(#{params.subAgentNo},'%')");
                }else{
                    sql.WHERE("ai.agent_no = #{params.agentNo}");
                }
            }
           /* if (!StringUtil.isEmpty(param.get("agentNo"))&&!"0".equals(param.get("agentNo"))) {
                if("0".equals(param.get("hasSub"))){//不包含
                    sql.WHERE("vsr.agent_no = #{params.agentNo}");
                }else if("1".equals(param.get("hasSub"))){//包含
                    sql.WHERE(
                            "(vsr.agent_no = #{params.agentNo} or ai.parent_id=#{params.agentNo})");
                }
            }*/

        }

        public void whereSqlTask(Map<String, String> param, SQL sql) {
            if (param == null) {
                return;
            }
            /*if(!StringUtil.isEmpty(param.get("orderNo"))){
                sql.WHERE(" v.order_no = #{params.orderNo}");
            }*/
            sql.WHERE(" vsrt.vas_rule_id = #{params.id}");
        }

    }
}
