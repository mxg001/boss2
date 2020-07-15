package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;
import cn.eeepay.framework.model.ServiceRate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AgentShareDao {

    
    //添加分润task
    @Insert("insert into agent_share_rule_task(share_id,efficient_date,effective_status,profit_type,per_fix_income,per_fix_inrate,safe_line,capping,share_profit_percent,"
    		+ "ladder,cost_rate_type,per_fix_cost,cost_rate,cost_capping,cost_safeline,ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,"
    		+ "ladder3_max,ladder4_rate,ladder4_max) values(#{agent.shareId},#{agent.efficientDate},0,#{agent.profitType},#{agent.perFixIncome},#{agent.perFixInrate},"
    		+ "#{agent.safeLine},#{agent.capping},#{agent.shareProfitPercent},#{agent.ladder},#{agent.costRateType},#{agent.perFixCost},#{agent.costRate},#{agent.costCapping},#{agent.costSafeline},"
    		+ "#{agent.ladder1Rate},#{agent.ladder1Max},#{agent.ladder2Rate},#{agent.ladder2Max},#{agent.ladder3Rate},#{agent.ladder3Max},#{agent.ladder4Rate},#{agent.ladder4Max})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="agent.id", before=false, resultType=Integer.class)
	int insertAgentShareListTask(@Param("agent")AgentShareRuleTask shareList);

	@Insert("insert into profit_update_record(share_id,cost_history,cost,share_profit_percent_history," +
			"share_profit_percent,efficient_date,effective_status,update_date,auther,share_task_id) " +
			"values(#{map.share_id},#{map.cost_history},#{map.cost},#{map.share_profit_percent_history}," +
			"#{map.share_profit_percent},#{map.efficient_date},#{map.effective_status},now(),#{map.auther},#{map.share_task_id})")
	int insertProfitUpdateRecord(@Param("map")Map<String, Object> updateMap);
    
    //修改单条历史分润
    @Update("update agent_share_rule_task set efficient_date=#{agent.efficientDate},check_status=#{agent.checkStatus},profit_type=#{agent.profitType},per_fix_income=#{agent.perFixIncome},per_fix_inrate=#{agent.perFixInrate},"
    		+ "safe_line=#{agent.safeLine},capping=#{agent.capping},share_profit_percent=#{agent.shareProfitPercent},ladder=#{agent.ladder},cost_rate_type=#{agent.costRateType},per_fix_cost=#{agent.perFixCost},"
    		+ "per_fix_cost=#{agent.perFixCost},cost_rate=#{agent.costRate},cost_capping=#{agent.costCapping},cost_safeline=#{agent.costSafeline},ladder1_rate=#{agent.ladder1Rate},ladder1_max=#{agent.ladder1Max},"
    		+ "ladder2_rate=#{agent.ladder2Rate},ladder2_max=#{agent.ladder2Max},ladder3_rate=#{agent.ladder3Rate},ladder3_max=#{agent.ladder3Max},ladder4_rate=#{agent.ladder4Rate},ladder4_max=#{agent.ladder4Max} "
    		+ " where id=#{agent.id}")
    int updateAgentShareTask(@Param("agent")AgentShareRuleTask share);
    
  //修改单条历史分润
    @Update("update agent_share_rule_task set efficient_date=#{agent.efficientDate},check_status=#{agent.checkStatus},profit_type=#{agent.profitType},per_fix_income=#{agent.perFixIncome},per_fix_inrate=#{agent.perFixInrate},"
    		+ "safe_line=#{agent.safeLine},capping=#{agent.capping},share_profit_percent=#{agent.shareProfitPercent},ladder=#{agent.ladder},cost_rate_type=#{agent.costRateType},per_fix_cost=#{agent.perFixCost},"
    		+ "per_fix_cost=#{agent.perFixCost},cost_rate=#{agent.costRate},cost_capping=#{agent.costCapping},cost_safeline=#{agent.costSafeline},ladder1_rate=#{agent.ladder1Rate},ladder1_max=#{agent.ladder1Max},"
    		+ "ladder2_rate=#{agent.ladder2Rate},ladder2_max=#{agent.ladder2Max},ladder3_rate=#{agent.ladder3Rate},ladder3_max=#{agent.ladder3Max},ladder4_rate=#{agent.ladder4Rate},ladder4_max=#{agent.ladder4Max} "
    		+ " where  efficient_date=#{efficientDate} and share_id=#{agent.shareId}")
    int updateAgentShareTaskByShare(@Param("agent")AgentShareRuleTask share, @Param("efficientDate")Date efficientDate);
    
  //修改单条主表分润
    @Update("update agent_share_rule set efficient_date=#{agent.efficientDate},check_status=#{agent.checkStatus},profit_type=#{agent.profitType},per_fix_income=#{agent.perFixIncome},per_fix_inrate=#{agent.perFixInrate},"
    		+ "safe_line=#{agent.safeLine},capping=#{agent.capping},share_profit_percent=#{agent.shareProfitPercent},ladder=#{agent.ladder},cost_rate_type=#{agent.costRateType},per_fix_cost=#{agent.perFixCost},"
    		+ "per_fix_cost=#{agent.perFixCost},cost_rate=#{agent.costRate},cost_capping=#{agent.costCapping},cost_safeline=#{agent.costSafeline},ladder1_rate=#{agent.ladder1Rate},ladder1_max=#{agent.ladder1Max},"
    		+ "ladder2_rate=#{agent.ladder2Rate},ladder2_max=#{agent.ladder2Max},ladder3_rate=#{agent.ladder3Rate},ladder3_max=#{agent.ladder3Max},ladder4_rate=#{agent.ladder4Rate},ladder4_max=#{agent.ladder4Max} "
    		+ " where id=#{agent.id}")
    int updateAgentShare(@Param("agent")AgentShareRuleTask share);
    
    //删除分润
    @Delete("delete from agent_share_rule_task where efficient_date=#{efficientDate}"
    		+ " and share_id=#{shareId}")
    int deleteAgentShareTask(@Param("shareId")Long shareId, @Param("efficientDate")Date efficientDate);
    
    //查询分润task
    @Select("select at.*,s.service_type,smr.rate_type from agent_share_rule_task at " +
			"LEFT JOIN agent_share_rule a ON a.id = at.share_id " +
			"LEFT JOIN service_info s ON a.service_id = s.service_id " +
			"LEFT JOIN service_manage_rate smr ON a.service_id = smr.service_id and a.card_type=smr.card_type and a.holidays_mark=smr.holidays_mark " +
			"and smr.agent_no=(SELECT ai.one_level_id FROM agent_info ai WHERE ai.agent_no = a.agent_no) " +
			"where at.share_id=#{shareId} order by at.efficient_date desc")
    @ResultType(AgentShareRuleTask.class)
    List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId);

    //计算代理商分润
    @Select("select * from agent_share_rule where card_type=#{param.cardType} and holidays_mark=#{param.holidaysMark} "
    		+ "and service_id=#{param.serviceId} and agent_no=#{param.agentNo}")
    @ResultType(AgentShareRule.class)
	AgentShareRule selectByparam(@Param("param")Map<String, Object> param);

    //查询分润task：根据生效日期和生效状态
    @Select("SELECT c.* FROM agent_share_rule b, agent_share_rule_task c,(SELECT share_id,MAX(t.efficient_date)AS effective_date_max "
    		+" FROM agent_share_rule_task t WHERE (NOW() >= t.efficient_date) and t.check_status=1  GROUP BY(t.share_id))a "
    		+" WHERE b.id = a.share_id AND b.efficient_date < a.effective_date_max "
    		+" and a.effective_date_max = c.efficient_date and a.share_id=c.share_id;")
    @ResultType(AgentShareRuleTask.class)
    List<AgentShareRuleTask> findByEffective();
    
    //查询所有的分润记录
    @SelectProvider(type=SqlProvider.class, method="queryAllShareTask")
    @ResultType(AgentShareRuleTask.class)
	List<AgentShareRuleTask> queryAllShareTask(@Param("info")AgentShareRuleTask info, Page<AgentShareRuleTask> page);
    
    @Update("update agent_share_rule set profit_type=#{task.profitType}, per_fix_income=#{task.perFixIncome}, per_fix_inrate=#{task.perFixInrate}, "
    		+ "safe_line=#{task.safeLine}, capping=#{task.capping}, share_profit_percent=#{task.shareProfitPercent}, ladder=#{task.ladder}, cost_rate_type=#{task.costRateType}, "
    		+ "per_fix_cost=#{task.perFixCost}, cost_rate=#{task.costRate}, cost_capping=#{task.costCapping}, cost_safeline=#{task.costSafeline}, ladder1_rate=#{task.ladder1Rate}, "
    		+ "ladder1_max=#{task.ladder1Max}, ladder2_rate=#{task.ladder2Rate}, ladder2_max=#{task.ladder2Max}, ladder3_rate=#{task.ladder3Rate}, ladder3_max=#{task.ladder3Max}, "
    		+ "ladder4_rate=#{task.ladder4Rate}, ladder4_max=#{task.ladder4Max} where id=#{task.shareId}")
    int updateByTask(@Param("task")AgentShareRuleTask task);
    
    @Select("select a.*,s.service_type,smr.rate_type from agent_share_rule a " +
			"LEFT JOIN service_info s ON a.service_id = s.service_id " +
			"LEFT JOIN service_manage_rate smr ON a.service_id = smr.service_id and a.card_type=smr.card_type and a.holidays_mark=smr.holidays_mark " +
			"and smr.agent_no=(SELECT ai.one_level_id FROM agent_info ai WHERE ai.agent_no = a.agent_no) " +
			"where a.id=#{id}")
    @ResultType(AgentShareRule.class)
    AgentShareRule getById(@Param("id")Long id);
    
    @Select("select * from agent_share_rule_task where id=#{id}")
    @ResultType(AgentShareRuleTask.class)
    AgentShareRuleTask getTaskById(Integer id);
    
    @Update("update agent_share_rule_task set profit_type=#{rule.profitType}, per_fix_income=#{rule.perFixIncome}, per_fix_inrate=#{rule.perFixInrate}, "
    		+ "safe_line=#{rule.safeLine}, capping=#{rule.capping}, share_profit_percent=#{rule.shareProfitPercent}, ladder=#{rule.ladder}, cost_rate_type=#{rule.costRateType}, "
    		+ "per_fix_cost=#{rule.perFixCost}, cost_rate=#{rule.costRate}, cost_capping=#{rule.costCapping}, cost_safeline=#{rule.costSafeline}, ladder1_rate=#{rule.ladder1Rate}, "
    		+ "ladder1_max=#{rule.ladder1Max}, ladder2_rate=#{rule.ladder2Rate}, ladder2_max=#{rule.ladder2Max}, ladder3_rate=#{rule.ladder3Rate}, ladder3_max=#{rule.ladder3Max}, "
    		+ "ladder4_rate=#{rule.ladder4Rate}, ladder4_max=#{rule.ladder4Max} where id=#{rule.taskId}")
    int updateByRule(@Param("rule")AgentShareRule rule, @Param("taskId")Integer taskId);
    
    @UpdateProvider(type = SqlProvider.class, method = "updateByTaskBatch")
    int updateByTaskBatch(@Param("list")List<AgentShareRuleTask> taskList);
    
    @UpdateProvider(type = SqlProvider.class, method = "updateByRuleBatch")
    int updateByRuleBatch(@Param("list")List<AgentShareRule> ruleList, @Param("taskIdList")List<Integer> taskIdList);

	@Update("UPDATE profit_update_record pur " +
			"INNER JOIN agent_share_rule_task asrt on asrt.id=pur.share_task_id and asrt.check_status=1 " +
			"INNER JOIN (SELECT share_task_id, MAX(update_date) update_date " +
			"FROM profit_update_record where NOW() >= efficient_date and effective_status=0 GROUP BY share_task_id) pur2 " +
			"on pur.share_task_id = pur2.share_task_id AND pur.update_date = pur2.update_date " +
			"set pur.effective_status=1")
	int updateProfitUpdateRecordStatus();

    @Update("update agent_share_rule_task set check_status=#{checkStatus} where efficient_date=#{efficientDate} and share_id=#{shareId}")
	int updateShareTaskStatus(AgentShareRule rule);

    @Update("update agent_share_rule set check_status=#{checkStatus} where efficient_date=#{efficientDate} and id=#{id}")
	int updateShareStatus(AgentShareRule rule);
    
    /**
     * 根据代理商主表分润ID，获取对应的代理商编号、业务产品ID、服务类型、卡类型、节假日标志
     * @author tans
     * @date 2017年5月25日 下午3:40:07
     * @param id
     * @return
     */
    @Select("select asr.card_type,asr.holidays_mark,asr.agent_no,si.service_id,"
    		+ "si.service_type,bpi.bp_id,asr.share_profit_percent,asr.cost_rate_type," +
			"asr.per_fix_cost,asr.cost_rate "
    		+" from agent_share_rule asr"
    		+" INNER JOIN service_info si on si.service_id=asr.service_id"
    		+" INNER JOIN business_product_info bpi on bpi.service_id=si.service_id"
    		+" where asr.id=#{id}")
    @ResultType(Map.class)
	Map<String, Object> getAgentBpServiceType(@Param("id")Integer id);
    
    @Select("SELECT asr.id"
    		+" FROM service_info si"
    		+" INNER JOIN business_product_info bpi ON bpi.service_id = si.service_id"
    		+" INNER JOIN agent_share_rule asr ON asr.service_id = si.service_id"
    		+" INNER JOIN agent_business_product abp ON abp.bp_id = bpi.bp_id"
    		+" INNER JOIN ( SELECT bpg1.bp_id FROM business_product_group bpg1, business_product_group bpg2"
    		+" WHERE bpg1.group_no = bpg2.group_no AND bpg1.bp_id <> #{bp_id} AND bpg2.bp_id = #{bp_id}"
    		+" ) bpids ON bpi.bp_id = bpids.bp_id"
    		+" WHERE abp.agent_no = #{agent_no} AND asr.agent_no = #{agent_no} AND si.service_type = #{service_type}"
    		+" AND asr.card_type = #{card_type} AND asr.holidays_mark = #{holidays_mark}" +
			" and si.effective_status = '1'")
    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getOtherGroupShareId(Map<String, Object> resultMap);
    
//    @Select("select id from agent_share_rule_task where share_id=#{shareId} and id<>#{id}  and efficient_date=#{efficientDate};")
    @SelectProvider(type = SqlProvider.class, method="getByEfficientDate")
    @ResultType(Integer.class)
    List<Integer> getByEfficientDate(@Param("share")AgentShareRuleTask share);
    
    public class SqlProvider {
    	public String updateByTaskBatch(Map<String, Object> map) {
    		List<AgentShareRuleTask> taskList = (List<AgentShareRuleTask>) map.get("list");
    		StringBuilder sb = new StringBuilder();  
            sb.append("INSERT INTO agent_share_rule ");  
            sb.append("(id,agent_no, service_id,card_type,holidays_mark,efficient_date,profit_type,per_fix_income,per_fix_inrate,safe_line,capping,share_profit_percent,ladder,cost_rate_type,per_fix_cost,cost_rate,cost_capping,cost_safeline,check_status,");
            sb.append("ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max) ");
            sb.append("VALUES ");  
            MessageFormat mf = new MessageFormat("(#'{'list[{0}].shareId},#'{'list[{0}].profitType},#'{'list[{0}].profitType},#'{'list[{0}].profitType},#'{'list[{0}].profitType},#'{'list[{0}].efficientDate}, #'{'list[{0}].profitType}, #'{'list[{0}].perFixIncome},#'{'list[{0}].perFixInrate},#'{'list[{0}].safeLine},#'{'list[{0}].capping},#'{'list[{0}].shareProfitPercent},#'{'list[{0}].ladder},#'{'list[{0}].costRateType},#'{'list[{0}].perFixCost},#'{'list[{0}].costRate},#'{'list[{0}].costCapping},#'{'list[{0}].costSafeline},#'{'list[{0}].checkStatus},#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");  
            for (int i = 0; i < taskList.size(); i++) {  
                sb.append(mf.format(new Object[]{i}));  
                if (i < taskList.size() - 1) {  
                    sb.append(",");  
                }  
            }  
            sb.append("on duplicate key update efficient_date=values(efficient_date), profit_type=values(profit_type),per_fix_income=values(per_fix_income),per_fix_inrate=values(per_fix_inrate),safe_line=values(safe_line),capping=values(capping),share_profit_percent=values(share_profit_percent),");
            sb.append("ladder=values(ladder),cost_rate_type=values(cost_rate_type),per_fix_cost=values(per_fix_cost),cost_rate=values(cost_rate),cost_capping=values(cost_capping),cost_safeline=values(cost_safeline),check_status=values(check_status),ladder1_rate=values(ladder1_rate),");
            sb.append("ladder1_max=values(ladder1_max),ladder2_rate=values(ladder2_rate),ladder2_max=values(ladder2_max),ladder3_rate=values(ladder3_rate),ladder3_max=values(ladder3_max),ladder4_rate=values(ladder4_rate),ladder4_max=values(ladder4_max)");
            System.out.println(sb.toString());
            return sb.toString(); 
    	}
    	
    	public String updateByRuleBatch(Map<String, Object> map) {
    		List<AgentShareRule> taskList = (List<AgentShareRule>) map.get("list");
    		List<Integer> taskIdList = (List<Integer>) map.get("taskIdList");
    		StringBuilder sb = new StringBuilder();  
            sb.append("INSERT INTO agent_share_rule_task ");  
            sb.append("(id,share_id,effective_status,efficient_date,profit_type,per_fix_income,per_fix_inrate,safe_line,capping,share_profit_percent,ladder,cost_rate_type,per_fix_cost,cost_rate,cost_capping,cost_safeline,check_status,");
            sb.append("ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max) ");
            sb.append("VALUES ");  
            MessageFormat mf = new MessageFormat("(#'{'taskIdList[{0}]},0,0,#'{'list[{0}].efficientDate}, #'{'list[{0}].profitType}, #'{'list[{0}].perFixIncome},#'{'list[{0}].perFixInrate},#'{'list[{0}].safeLine},#'{'list[{0}].capping},#'{'list[{0}].shareProfitPercent},#'{'list[{0}].ladder},#'{'list[{0}].costRateType},#'{'list[{0}].perFixCost},#'{'list[{0}].costRate},#'{'list[{0}].costCapping},#'{'list[{0}].costSafeline},#'{'list[{0}].checkStatus},#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");  
            for (int i = 0; i < taskList.size(); i++) {  
                sb.append(mf.format(new Object[]{i}));  
                if (i < taskList.size() - 1) {  
                    sb.append(",");  
                }  
            }  
            sb.append("on duplicate key update efficient_date=values(efficient_date), profit_type=values(profit_type),per_fix_income=values(per_fix_income),per_fix_inrate=values(per_fix_inrate),safe_line=values(safe_line),capping=values(capping),share_profit_percent=values(share_profit_percent),");
            sb.append("ladder=values(ladder),cost_rate_type=values(cost_rate_type),per_fix_cost=values(per_fix_cost),cost_rate=values(cost_rate),cost_capping=values(cost_capping),cost_safeline=values(cost_safeline),check_status=values(check_status),ladder1_rate=values(ladder1_rate),");
            sb.append("ladder1_max=values(ladder1_max),ladder2_rate=values(ladder2_rate),ladder2_max=values(ladder2_max),ladder3_rate=values(ladder3_rate),ladder3_max=values(ladder3_max),ladder4_rate=values(ladder4_rate),ladder4_max=values(ladder4_max)");
            System.out.println(sb.toString());
            return sb.toString(); 
    	}
    	
    	public String queryAllShareTask(final Map<String, Object> param) {
			final AgentShareRuleTask info = (AgentShareRuleTask) param.get("info");
			return new SQL() {
				{
					SELECT("*");
					FROM("agent_share_rule_task");
					if (info.getEffectiveStatus() != null && info.getEffectiveStatus()!=-1) {
						WHERE("effective_status = #{info.effectiveStatus}");
					}
					if(info.getProfitType() != null && info.getProfitType()!=-1){
							WHERE("profit_type=#{info.profitType}");
					}
					if(info.getEfficientStartDate() != null){
						WHERE("efficient_date >= #{queryaram.efficientStartDate}");
					}
					if(info.getEfficientEndDate() != null){
						WHERE("efficient_date < #{queryaram.efficientEndDate}");
					}
				}
			}.toString();
		}
    	
    	public String getByEfficientDate(Map<String, Object> param){
    		final AgentShareRuleTask share = (AgentShareRuleTask) param.get("share");
    		String sql = new SQL(){{
    			SELECT("id");
    			FROM("agent_share_rule_task");
    			WHERE("share_id=#{share.shareId} and efficient_date=#{share.efficientDate}");
    			if(share!=null && share.getId()!=null){
    				WHERE("id<>#{share.id}");
    			}
    		}}.toString();
    		return sql;
    	}
    	
    }

    /**
     * 根据代理商编号查询这个代理商的业务产品（所有的组长组员）
     * @author tans
     * @date 2017年7月13日 下午4:07:39
     * @param agentNo
     * @return
     */
    @Select("SELECT bpg1.bp_id AS leader,bpg2.bp_id AS member FROM business_product_group bpg1"
       +"  JOIN business_product_group bpg2 ON bpg2.group_no = bpg1.group_no AND bpg2.bp_id != bpg1.bp_id"
       +"  LEFT JOIN business_product_define bpd ON bpd.bp_id = bpg1.bp_id"
       +"  WHERE EXISTS("
       +"      SELECT 1 FROM agent_business_product abp"
       +"      WHERE abp.agent_no = #{agentNo}"
       +"      AND abp.bp_id = bpg1.bp_id"
       +"  )"
       +"  AND bpd.allow_individual_apply = 1 ;")
    @ResultType(List.class)
	List<Map<Long, Long>> selectLeaderAndOtherMember(@Param("agentNo")String agentNo);

    /**
     * 根据组长组员ID，查询得到组员的分润（组装得来的）
     * @author tans
     * @param agentNo 
     * @date 2017年7月13日 下午4:08:57
     * @param bpIdMap
     * @return
     */
    @Select("SELECT "
     +"        sub.agent_no, main.service_id,sub.card_type,sub.holidays_mark, "
     +"        sub.efficient_date,sub.disabled_date,sub.profit_type,sub.per_fix_income, "
     +"        sub.per_fix_inrate,sub.safe_line,sub.capping,sub.share_profit_percent, "
     +"        sub.ladder, sub.cost_rate_type,sub.per_fix_cost,sub.cost_rate,sub.cost_capping, "
     +"        sub.cost_safeline,sub.check_status,sub.lock_status,sub.ladder1_rate, "
     +"        sub.ladder1_max,sub.ladder2_rate,sub.ladder2_max,sub.ladder3_rate,sub.ladder3_max, "
     +"        sub.ladder4_rate,sub.ladder4_max,sub.service_type "
     +"    FROM "
     +"    ( "
//     +"        <!-- 查出队员还没有加入agent_share_rule的分润数据 --> "
     +"        SELECT si.service_name,si.service_type, "
     +"       CONCAT(si.service_type,IFNULL(si2.service_type,'')) AS service_type2, "
     +"        smr.* "
     +"        FROM service_manage_rate smr "
     +"        LEFT JOIN service_info si ON si.service_id = smr.service_id "
     +"        LEFT JOIN service_info si2 ON si2.link_service = si.service_id "
     +"        WHERE smr.agent_no = 0 and si.effective_status = '1'"
     +"        AND EXISTS( "
     +"            SELECT 1 FROM business_product_info bpi "
     +"            WHERE bpi.bp_id = #{bpIdMap.member} "
     +"            AND bpi.service_id = smr.service_id "
     +"        ) "
     +"    )main "
     +"    JOIN "
     +"    ( "
//     +"        <!-- 查出队长已经加入agent_share_rule的分润数据--> "
     +"        SELECT si.service_name,si.service_type, "
     +"        CONCAT(si.service_type,IFNULL(si2.service_type,'')) AS service_type2, "
     +"        smr.* "
     +"        FROM agent_share_rule smr "
     +"        LEFT JOIN service_info si ON si.service_id = smr.service_id "
     +"        LEFT JOIN service_info si2 ON si2.link_service = si.service_id "
     +"        WHERE smr.agent_no = #{agentNo}  and si.effective_status = '1'"
     +"        AND EXISTS( "
     +"            SELECT 1 FROM business_product_info bpi "
     +"            WHERE bpi.bp_id =  #{bpIdMap.leader} "
     +"             AND bpi.service_id = smr.service_id "
     +"        ) "
     +"   )sub ON  main.service_type2 = sub.service_type2 "
     +"    AND	    main.card_type = sub.card_type "
     +"   AND 	main.holidays_mark = sub.holidays_mark")
    @ResultType(List.class)
	List<AgentShareRule> getShareByLeaderAndMember(@Param("agentNo")String agentNo, @Param("bpIdMap")Map<Long, Long> bpIdMap);

    @Select("SELECT COUNT(1) "
      + "  FROM agent_share_rule_task asrt"
      + "   WHERE (asrt.check_status <> 1"
      + "   OR DATEDIFF(asrt.efficient_date, CURRENT_DATE) >= 0)"
      + "   AND EXISTS("
      + "       SELECT 1 FROM agent_share_rule asr"
      + "       LEFT JOIN business_product_info bpi ON bpi.service_id = asr.service_id"
      + "       WHERE asr.agent_no = #{agentNo}"
      + "       AND bpi.bp_id = #{bpId}"
      + "       AND asrt.share_id = asr.id"
      +"  )")
	int countHasNotCheck(@Param("bpId")Long bpId, @Param("agentNo")String agentNo);

    @Select("select asr.* FROM agent_share_rule asr "
    		+ " inner JOIN business_product_info bpi on bpi.service_id=asr.service_id"
    		+ " inner JOIN agent_business_product abp on abp.agent_no = asr.agent_no  and bpi.bp_id = abp.bp_id "
    		+ " where asr.agent_no=#{agentNo}"
    		+ " and abp.bp_id=#{bpId} ;")
    @ResultType(AgentShareRule.class)
	List<AgentShareRule> getByAgentNoBpId(@Param("agentNo")String agentNo, @Param("bpId")String bpId);

    @Select("select smr.*, si.service_type,si.service_name,bpd.bp_name from service_manage_rate smr "
    		+" left join service_info si on si.service_id=smr.service_id "
    		+" left join business_product_info bpi on bpi.service_id=smr.service_id"
    		+" left join business_product_define bpd on bpd.bp_id=bpi.bp_id "
    		+" where smr.agent_no=#{agentNo} and smr.card_type=#{cardType}  "
    		+" and smr.holidays_mark=#{holidaysMark}"
    		+" and smr.service_id=#{serviceId}")
    @ResultType(ServiceRate.class)
	ServiceRate queryRateByShare(AgentShareRule share);





}