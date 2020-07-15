package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BossOperLog;
import cn.eeepay.framework.service.BossOperLogService;

/**
 * 系统日志管理
 * 
 * @author tans
 */
@Controller
@RequestMapping(value = "/sysLog")
public class SysLogController {

	private Logger log = LoggerFactory.getLogger(SysLogController.class);
	
	@Resource
	private BossOperLogService bossOperLogService;

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
			BossOperLog logInfo = JSONObject.parseObject(param, BossOperLog.class);
			if(logInfo == null || logInfo.getOper_start_time() == null){
				msg.put("msg", "参数非法");
				return msg;
			}
			bossOperLogService.queryByCondition(page, logInfo);
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
	public BossOperLog queryDetail(@PathVariable("id") Integer id) throws Exception {
		BossOperLog logInfo = null;
		try {
			logInfo = bossOperLogService.queryDetail(id);
		} catch (Exception e) {
			log.error("日志详情查询失败");
			e.printStackTrace();
		}
		return logInfo;
	}

}
