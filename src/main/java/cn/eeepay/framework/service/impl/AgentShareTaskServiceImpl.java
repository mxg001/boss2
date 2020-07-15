package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentShareDao;
import cn.eeepay.framework.dao.ServiceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AgentShareTaskService;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("agentShareTaskService")
@Transactional
public class AgentShareTaskServiceImpl implements AgentShareTaskService{
	private static final Logger log = LoggerFactory.getLogger(AgentShareTaskServiceImpl.class);
	@Resource
	private AgentShareDao agentShareDao;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private ServiceDao serviceDao;
	@Override
	public Map<String, Object> insertAgentShareList(AgentShareRuleTask share){
		log.info("新增历史分润时，带入的参数：{}",JSONObject.toJSONString(share));
		Map<String,Object> msg=new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败");
		//不是提现服务的，代理商成本需要加上%
		if(share.getServiceType()!=null && share.getServiceType()!=10000 && share.getServiceType()!=10001 ){
			if(share.getCost()!=null && !share.getCost().contains("%")){
				share.setCost(share.getCost()+"%");
			}
		}
		AgentShareRule agentShareRule=new AgentShareRule();
		Long shareId = share.getShareId();
		agentShareRule.setProfitType(share.getProfitType());
		agentShareRule.setIncome(share.getIncome());
		agentShareRule.setCost(share.getCost());
		agentShareRule.setCostCapping(share.getCostCapping());
		agentShareRule.setLadderRate(share.getLadderRate());
		agentShareRule.setEfficientDate(share.getEfficientDate());
		agentShareRule.setShareProfitPercent(share.getShareProfitPercent());
		agentShareRule.setRateType(share.getRateType());
		//检查分润格式
		agentInfoService.setShareRule(agentShareRule);
		BeanUtils.copyProperties(agentShareRule,share);
		share.setShareId(shareId);
		//保证新的生效时间和share_id是分润历史记录表唯一
		List<Integer> idList = agentShareDao.getByEfficientDate(share);
		if(idList!=null && idList.size()>0){
			msg.put("msg", "生效日期已存在");
			return msg;
		}
		//查到当前分润的主分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
		Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(share.getShareId().intValue());
		//分润需要小于商户费率
		//根据服务ID、服务类型、代理商编号、卡类型、节假日标志，获取对应的费率
		ServiceRate minRate = queryMinServiceRate(resultMap);

		//比较rule和rate，rule不能大于rate
		agentInfoService.compareRuleRate(agentShareRule, minRate);
		//插入组长记录
		agentShareDao.insertAgentShareListTask(share);
		Map<String, Object> updateMap=new HashMap<String, Object>();
		updateMap.put("share_task_id",share.getId());
		updateMap.put("share_id",share.getShareId());
		if(resultMap.get("cost_rate_type").toString().equals("1")){
			if(StringUtil.isNotBlank(resultMap.get("per_fix_cost"))){
				updateMap.put("cost_history",resultMap.get("per_fix_cost")+"元");
			}
		}else{
			if(StringUtil.isNotBlank(resultMap.get("cost_rate"))){
				updateMap.put("cost_history",resultMap.get("cost_rate")+"%");
			}
		}
		if(share.getCostRateType().equals("1")){
			updateMap.put("cost",share.getPerFixCost()+"元");
		}else {
			updateMap.put("cost",share.getCost());
		}
		if(StringUtil.isNotBlank(resultMap.get("share_profit_percent"))){
			updateMap.put("share_profit_percent_history",resultMap.get("share_profit_percent"));
		}
		updateMap.put("share_profit_percent",share.getShareProfitPercent());
		updateMap.put("efficient_date",share.getEfficientDate());
		updateMap.put("effective_status",0);
		updateMap.put("auther","sys");
		agentShareDao.insertProfitUpdateRecord(updateMap);
		//获取同组的其他分润
		List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
		if(otherShareList!=null && otherShareList.size()>0){
			for(AgentShareRule otherShare: otherShareList){
				log.info("新增同组子表分润，otherShare=【{}】",JSONObject.toJSONString(otherShare));
				share.setShareId(otherShare.getId());
				agentShareDao.insertAgentShareListTask(share);
			}
		}
		msg.put("status", true);
		msg.put("msg", "新增成功");
		return msg;
	}

	/**
	 * 查询最低同组费率
	 * @author tans
	 * @date 2017年7月24日 上午10:41:37
	 * @param resultMap
	 * @return
	 */
	public ServiceRate queryMinServiceRate(Map<String, Object> resultMap) {
		AgentShareRule rule = new AgentShareRule();
		rule.setAgentNo(resultMap.get("agent_no").toString());
		rule.setServiceId(resultMap.get("service_id").toString());
		rule.setCardType(Integer.valueOf(resultMap.get("card_type").toString()));
		rule.setHolidaysMark(Integer.valueOf(resultMap.get("holidays_mark").toString()));
		rule.setServiceType(Integer.valueOf(resultMap.get("service_type").toString()));
		//找到同组的最低费率
		ServiceRate minRate = agentInfoService.queryMinServiceRate(rule);
		return minRate;
	}
	
	/**
	 * 查询对应费率
	 * @author tans
	 * @date 2017年7月24日 上午10:41:48
	 * @param resultMap
	 * @return
	 */
	public ServiceRate queryRateByShare(Map<String, Object> resultMap) {
		AgentShareRule itemRule = new AgentShareRule();
		itemRule.setAgentNo(resultMap.get("agent_no").toString());
		itemRule.setServiceId(resultMap.get("service_id").toString());
		itemRule.setCardType(Integer.valueOf(resultMap.get("card_type").toString()));
		itemRule.setHolidaysMark(Integer.valueOf(resultMap.get("holidays_mark").toString()));
		//找到对应的费率
		ServiceRate minRate = agentShareDao.queryRateByShare(itemRule);
		if(minRate==null){
			throw new RuntimeException("找不到对应的费率");
		}
		if(minRate.getServiceType()==10000 || minRate.getServiceType()==10001){
			minRate.setIsTx(1);
		} else {
			minRate.setIsTx(0);
		}
		return minRate;
	}
	
	@Override
	public Map<String, Object> updateAgentShareList(AgentShareRuleTask share){
		log.info("修改分润时，带入的参数：{}",JSONObject.toJSONString(share));
		Map<String,Object> msg=new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "修改失败");
		//不是提现服务的，代理商成本需要加上%
		if(share.getServiceType()!=null && share.getServiceType()!=10000 && share.getServiceType()!=10001 ){
			if(share.getCost()!=null && !share.getCost().contains("%")){
				share.setCost(share.getCost()+"%");
			}
		}
		AgentShareRule agentShareRule=new AgentShareRule();
		Integer id = share.getId();
		Long shareId = share.getShareId();
		agentShareRule.setProfitType(share.getProfitType());
		agentShareRule.setServiceType(share.getServiceType());
		agentShareRule.setIncome(share.getIncome());
		agentShareRule.setCost(share.getCost());
		agentShareRule.setLadderRate(share.getLadderRate());
		agentShareRule.setEfficientDate(share.getEfficientDate());
		agentShareRule.setShareProfitPercent(share.getShareProfitPercent());
		agentShareRule.setCostCapping(share.getCostCapping());
		//检查分润填写格式
		agentInfoService.setShareRule(agentShareRule);
		BeanUtils.copyProperties(agentShareRule,share);
		share.setShareId(shareId);
		share.setId(id);
		share.setCheckStatus(0);
		//查下这个分润是主表，还是子表
		AgentShareRule ruleTask = agentShareDao.getById(id.longValue());
		//保证新的生效时间和share_id是分润历史记录表唯一
		AgentShareRuleTask oldRuleTask = agentShareDao.getTaskById(share.getId());
		if(ruleTask!=null){
			log.info("修改分润主表，share=【{}】",JSONObject.toJSONString(share));
			//查到当前分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
			Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(share.getId());
			ServiceRate minRate = queryMinServiceRate(resultMap);
			//比较rule和rate，rule不能大于rate
			agentInfoService.compareRuleRate(agentShareRule, minRate);
			agentShareDao.updateAgentShare(share);
			updateInsertProfitUpdateRecord(share,oldRuleTask);
			//获取同组的其他分润
			List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
			if(otherShareList!=null && otherShareList.size()>0){
				for(AgentShareRule otherShare: otherShareList){
					log.info("修改同组主表分润，otherShare=【{}】",JSONObject.toJSONString(otherShare));
					share.setId(otherShare.getId().intValue());
					agentShareDao.updateAgentShare(share);
				}
			}
		} else {
			log.info("修改分润子表，share=【{}】",JSONObject.toJSONString(share));
			Date oldEfficentDate = oldRuleTask.getEfficientDate();
			List<Integer> idList = agentShareDao.getByEfficientDate(share);
			if(idList!=null && idList.size()>0){
				msg.put("msg", "生效日期已存在");
				return msg;
			}
			//查到当前分润的主分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
			Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(share.getShareId().intValue());
			ServiceRate minRate = new ServiceRate();
			if(resultMap.get("group_no")!=null){
				minRate = queryMinServiceRate(resultMap);
			} else {
				minRate = queryRateByShare(resultMap);
			}
			//比较rule和rate，rule不能大于rate
			agentInfoService.compareRuleRate(agentShareRule, minRate);
			agentShareDao.updateAgentShareTaskByShare(share,oldEfficentDate);
			updateInsertProfitUpdateRecord(share,oldRuleTask);
			//获取同组的其他分润
			List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
			//根据shareId和生效时间，确定同组的其他分润，再去修改
			if(otherShareList!=null && otherShareList.size()>0){
				for(AgentShareRule otherShare: otherShareList){
					log.info("修改同组子表分润，otherShare=【{}】",JSONObject.toJSONString(otherShare));
					share.setShareId(otherShare.getId());
					agentShareDao.updateAgentShareTaskByShare(share,oldEfficentDate);
				}
			}
		}
		msg.put("status", true);
		msg.put("msg", "修改成功");
		return msg;
		
	}
    
	/**
	 * 已经用过的分润不能删除
	 */
	@Override
	public int deleteAgentShareTask(Integer taskId){
		log.info("删除子表分润，ID：" + taskId);
		AgentShareRuleTask oldRuleTask = agentShareDao.getTaskById(taskId);
		//查到当前分润的主分润的所属代理商、业务产品ID、服务类型、卡类型、节假日标志
		Map<String, Object> resultMap = agentShareDao.getAgentBpServiceType(oldRuleTask.getShareId().intValue());
		//获取同组的其他分润
		List<AgentShareRule> otherShareList = agentShareDao.getOtherGroupShareId(resultMap);
		//根据分润主表ID和生效时间，确定同组的其他分润，再去删除
		if(otherShareList!=null && otherShareList.size()>0){
			for(AgentShareRule otherShare: otherShareList){
				log.info("删除同组子表分润，otherShare：[{}],生效日期:[{}]" ,JSONObject.toJSONString(otherShare),oldRuleTask.getEfficientDate());
				agentShareDao.deleteAgentShareTask(otherShare.getId(),oldRuleTask.getEfficientDate());
			}
		}
    	return agentShareDao.deleteAgentShareTask(oldRuleTask.getShareId(),oldRuleTask.getEfficientDate());
    }
	@Override
	public List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId){
		AgentShareRule rule=agentShareDao.getById(Long.valueOf(shareId));
		AgentShareRuleTask taskItem = new AgentShareRuleTask();
		BeanUtils.copyProperties(rule,taskItem);
		taskItem.setId(shareId);
		taskItem.setMainStatus(1);//代表主表
		taskItem.setEffectiveStatus(taskItem.getCheckStatus());//主表里面没有生效状态，但是主表里面的审核状态为已审核，即为生效
		List<AgentShareRuleTask> list=agentShareDao.getAgentShareRuleTask(shareId);
		list.add(0,taskItem);
		for(AgentShareRuleTask task:list){
			AgentShareRule agentShareRule=new AgentShareRule();
			BeanUtils.copyProperties(task,agentShareRule);
			agentInfoService.profitExpression(agentShareRule);
			task.setIncome(agentShareRule.getIncome());
			task.setCost(agentShareRule.getCost());
			task.setLadderRate(agentShareRule.getLadderRate());
		}
    	return list;
    }

	@Override
	public List<AgentShareRuleTask> findByEffective() {
		return agentShareDao.findByEffective();
	}

	@Override
	public int updateByTask(AgentShareRuleTask task) {
		return agentShareDao.updateByTask(task);
	}

	@Override
	public AgentShareRule getById(Long id) {
		return agentShareDao.getById(id);
	}

	@Override
	public int updateByRule(AgentShareRule rule, Integer taskId) {
		return agentShareDao.updateByRule(rule, taskId);
	}

	@Override
	public int updateByTaskBatch(List<AgentShareRuleTask> taskList) {
		return agentShareDao.updateByTaskBatch(taskList);
	}

	@Override
	public int updateByRuleBatch(List<AgentShareRule> ruleList,
			List<Integer> taskIdList) {
		return agentShareDao.updateByRuleBatch(ruleList, taskIdList);
	}

	@Override
	public int updateProfitUpdateRecordStatus(){
		return agentShareDao.updateProfitUpdateRecordStatus();
	}

	@Override
	public List<AgentShareRuleTask> queryAllShareTask(AgentShareRuleTask params, Page<AgentShareRuleTask> page) {
		return agentShareDao.queryAllShareTask(params, page);
	}


	public void updateInsertProfitUpdateRecord(AgentShareRuleTask share,AgentShareRuleTask oldRuleTask){
		Map<String, Object> updateMap=new HashMap<String, Object>();
		updateMap.put("share_task_id",share.getId());
		updateMap.put("share_id",share.getShareId());
		if(oldRuleTask!=null){
			if(oldRuleTask.getCostRateType().equals("1")){
				if(StringUtil.isNotBlank(oldRuleTask.getPerFixCost())){
					updateMap.put("cost_history",oldRuleTask.getPerFixCost()+"元");
				}
			}else{
				if(StringUtil.isNotBlank(oldRuleTask.getCostRate())){
					updateMap.put("cost_history",oldRuleTask.getCostRate()+"%");
				}
			}
			if(StringUtil.isNotBlank(oldRuleTask.getShareProfitPercent())){
				updateMap.put("share_profit_percent_history",oldRuleTask.getShareProfitPercent());
			}
		}
		if(share.getCostRateType().equals("1")){
			updateMap.put("cost",share.getPerFixCost()+"元");
		}else {
			updateMap.put("cost",share.getCost());
		}
		updateMap.put("share_profit_percent",share.getShareProfitPercent());
		updateMap.put("efficient_date",share.getEfficientDate());
		updateMap.put("effective_status",0);
		updateMap.put("auther","sys");
		agentShareDao.insertProfitUpdateRecord(updateMap);
	}
}
