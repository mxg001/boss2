package cn.eeepay.framework.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;


@Service
public class AgentShareRuleJob {
	@Autowired
	private AgentShareTaskService agentShareTaskService;
	
	/**
	 * 分润任务：从备份分润任务表，将生效的分润任务更新到正在执行的分润表
	 */
	public void execute() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
//		String dateStr = sdf.format(date);
		
		List<AgentShareRuleTask> list =  agentShareTaskService.findByEffective();
		AgentShareRule temp = null;
		List<AgentShareRule> ruleList = new ArrayList<AgentShareRule>();
		List<Integer> taskIdList = new ArrayList<Integer>();
		//放到正在执行的分润表
		for (AgentShareRuleTask ruleTask : list) {
			//查询原rule
			temp = agentShareTaskService.getById(ruleTask.getShareId());
			ruleList.add(temp);
			taskIdList.add(ruleTask.getId());
			//一条条更新
			//agentShareTaskService.updateByTask(ruleTask);
			//更新任务表
			//agentShareTaskService.updateByRule(temp, ruleTask.getId());
		}
		agentShareTaskService.updateByTaskBatch(list);
		agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
	}
}
