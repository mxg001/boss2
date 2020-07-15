package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.service.impl.RegularTasks;

@Controller
@RequestMapping(value="/refular")
public class RegularTasksAction {

	@Resource
	private RegularTasks regularTasks;
	
	/*@RequestMapping(value="/updateAgentShareTask")
	@ResponseBody
	@SystemLog(description = "代理商分润定时任务",operCode="task.agentShareTask")
	public Map<String, Object> updateAgentShareTask() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			regularTasks.updateAgentShareTask();
			msg.put("status",true);
			msg.put("msg","任务执行成功");
		} catch (Exception e) {
			msg.put("status",false);
			msg.put("msg","任务执行失败");
		}
		return msg;
	}*/
	
	/*@RequestMapping(value="/updateAcqServiceRate")
	@ResponseBody
	@SystemLog(description = "收单费率定时任务",operCode="task.acqRateTask")
	public Map<String, Object> updateAcqServiceRate() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			regularTasks.updateAcqServiceRate();
			msg.put("status",true);
			msg.put("msg","任务执行成功");
		} catch (Exception e) {
			msg.put("status",false);
			msg.put("msg","任务执行失败");
		}
		return msg;
	}*/
	
	/*@RequestMapping(value="/updateOutAccountServiceTask")
	@ResponseBody
	@SystemLog(description = "出款费率定时任务",operCode="task.outRateTask")
	public Map<String, Object> updateOutAccountServiceTask() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			regularTasks.updateOutAccountServiceTask();
			msg.put("status",true);
			msg.put("msg","任务执行成功");
		} catch (Exception e) {
			msg.put("status",false);
			msg.put("msg","任务执行失败");
		}
		return msg;
	}*/
	
	@RequestMapping(value="/updateSettleStatus")
	@ResponseBody
	@SystemLog(description = "交易结算状态更新",operCode="task.updateSettleStatus")
	public Object updateSettleStatus() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			regularTasks.updateSettleStatus(); 
			msg.put("status",true);
			msg.put("msg","任务执行成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg.put("status",false);
			msg.put("msg","任务执行失败");
		}
		return msg;
	}
}
