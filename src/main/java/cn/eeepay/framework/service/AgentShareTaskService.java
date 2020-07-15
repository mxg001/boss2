package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;

public interface AgentShareTaskService {

	/**
	 * 添加一条任务
	 * @param shareList
	 * @return
	 */
	Map<String, Object> insertAgentShareList(AgentShareRuleTask shareList);

	/**
	 * 删除一条任务
	 * @param id
	 * @return
	 */
	int deleteAgentShareTask(Integer id);

	/**
	 * 查询任务列表
	 * @param shareId
	 * @return
	 */
	List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId);
	
	/**
	 * 根据生效日期和生效状态查询
	 * @param dateStr
	 * @return
	 */
	List<AgentShareRuleTask> findByEffective();
	
	/**
	 * 将分润任务更新到分润表
	 * @return
	 */
	int updateByTask(AgentShareRuleTask task);
	
	/**
	 * 根据id获取分润信息
	 * @param id
	 * @return
	 */
	AgentShareRule getById(Long id);
	
	/**
	 * 将分润信息更新到分润任务
	 * @param rule
	 * @return
	 */
	int updateByRule(AgentShareRule rule, Integer taskId);
	
	
	int updateByTaskBatch(List<AgentShareRuleTask> taskList);
	
	
	int updateByRuleBatch(List<AgentShareRule> ruleList, List<Integer> taskIdList);

	int updateProfitUpdateRecordStatus();

	List<AgentShareRuleTask> queryAllShareTask(AgentShareRuleTask params, Page<AgentShareRuleTask> page);

	Map<String, Object> updateAgentShareList(AgentShareRuleTask parseObject);
}
