package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLog;
import cn.eeepay.framework.service.AgentOperLogService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统日志管理
 * 
 * @author tans
 */
@Controller
@RequestMapping(value = "/agentLog")
public class AgentLogController {

	private Logger log = LoggerFactory.getLogger(AgentLogController.class);
	
	@Resource
	private AgentOperLogService agentOperLogService;

	/**
	 * 条件查询日志 
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/queryByCondition")
	@ResponseBody
	public Map<String, Object> queryByCondition(@RequestParam("baseInfo") String param, @ModelAttribute Page page){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			AgentOperLog logInfo = JSONObject.parseObject(param, AgentOperLog.class);
			if(logInfo == null ){
				msg.put("msg", "参数非法");
				return msg;
			}
			if(StringUtils.isNotBlank(logInfo.getMethod_desc())){
				logInfo.setMethod_desc("%" + logInfo.getMethod_desc() + "%");
			}
			agentOperLogService.queryByCondition(page, logInfo);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			log.error("条件查询系统日志失败", e);
			msg.put("msg", "查询异常");
		}
		return msg;
	}

	/**
	 * 日志详情 
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryDetail/{id}")
	@ResponseBody
	public AgentOperLog queryDetail(@PathVariable("id") Integer id) throws Exception {
		AgentOperLog logInfo = null;
		try {
			logInfo = agentOperLogService.queryDetail(id);
		} catch (Exception e) {
			log.error("日志详情查询失败");
			e.printStackTrace();
		}
		return logInfo;
	}

}
