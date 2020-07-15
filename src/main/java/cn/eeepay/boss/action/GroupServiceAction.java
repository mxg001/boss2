package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AcqServiceRate;
import cn.eeepay.framework.model.AcqServiceRateTask;
import cn.eeepay.framework.model.AcqServiceTransRules;
import cn.eeepay.framework.model.WarningSet;
import cn.eeepay.framework.service.GroupServiceService;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.WarningSetService;

/**
 * 收单服务action
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping("/groupService")
public class GroupServiceAction {

	private static final Logger log = LoggerFactory.getLogger(GroupServiceAction.class);

	@Resource
	private GroupServiceService groupServiceService;
	@Autowired
	private WarningSetService warningSetService;
	@Autowired
	private RedisService redisService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/acqOrgSelectBox.do", method = RequestMethod.POST)
	public @ResponseBody List<AcqOrg> acqOrgSelectBox() {
		return groupServiceService.acqOrgSelectBox();
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAcqService.do", method = RequestMethod.POST)
	public @ResponseBody Page<AcqService> queryAcqService(@RequestParam("info") String param,
			@ModelAttribute("page") Page<AcqService> page) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			groupServiceService.listAcqServiceByCon(jsonMap, page);
		} catch (Exception e) {
			log.error("查询收单服务列表失败！", e);
		}
		return page;
	}

	@RequestMapping(value = "/addAcqService.do", method = RequestMethod.POST)
	@SystemLog(description = "新增收单服务",operCode="groupService.insert")
	public @ResponseBody Map<String, Object> addAcqService(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			AcqService acqService = jsonObject.getObject("serviceBaseInfo", AcqService.class);
			List<AcqServiceRate> acqServiceRates = JSON.parseArray(jsonObject.getJSONArray("serviceRateInfos").toJSONString(), AcqServiceRate.class);
			AcqServiceTransRules acqServiceTransRules = jsonObject.getObject("serviceQuotaInfo", AcqServiceTransRules.class);
			Integer num = groupServiceService.insertService(acqService, acqServiceRates, acqServiceTransRules);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			}
		} catch (Exception e) {
			log.error("添加失败！", e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "添加服务失败");
			else
				msg.put("msg", str);
		}
		return msg;
	}

	@RequestMapping(value = "/updateAcqServiceStatus.do", method = RequestMethod.POST)
	@SystemLog(description = "收单服务状态开关",operCode="groupService.switch")
	public @ResponseBody Map<String, Object> updateAcqServiceStatus(@RequestParam("info") String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			AcqService acq = JSON.parseObject(param, AcqService.class);
			int num = groupServiceService.updateAcqServiceStatus(acq);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改失败！");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/serviceDetail.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> serviceDetail(@RequestBody String param) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			data = groupServiceService.serviceDetail(jsonObject.getLong("id"));
		} catch (Exception e) {
			log.error("查询收单服务详情失败！", e);
		}
		return data;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/listEffectiveServiceRate.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> listEffectiveServiceRate(@RequestBody String param) {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			res.put("data", groupServiceService.listEffectiveServiceRateByServiceId(jsonObject.getLong("id")));
		} catch (Exception e) {
			log.error("查询收单服务费率失败！", e);
		}
		return res;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/listServiceRateLog.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> listServiceRateLog(@RequestBody String param) {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			res.put("data", groupServiceService.listServiceRateLogByRateIdAndCardType(jsonObject.getLong("rateId"),jsonObject.getInteger("cardType")));
		} catch (Exception e) {
			log.error("查询收单服务费率失败！", e);
		}
		return res;
	}

	@RequestMapping(value = "/deleteServiceRate.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteServiceRate(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			int num = groupServiceService.deleteServiceRateTask(jsonObject.getLong("id"));
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "删除成功！");
			}
		} catch (Exception e) {
			log.error("删除失败！", e);
			msg.put("status", false);
			msg.put("msg", "删除失败！");
		}
		return msg;
	}

	@RequestMapping(value = "/updateServiceRate.do", method = RequestMethod.POST)
	@SystemLog(description = "收单服务修改费率",operCode="groupService.updateRate")
	public @ResponseBody Map<String, Object> updateServiceRate(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			AcqServiceRateTask task = JSON.parseObject(param, AcqServiceRateTask.class);
			int num = groupServiceService.insertAcqServiceRateTask(task);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			if (e.getMessage() != null) {
				msg.put("msg", e.getMessage());
			} else {
				msg.put("msg", "修改失败！");
			}
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAcqServiceTransRule.do", method = RequestMethod.POST)
	public @ResponseBody AcqServiceTransRules getAcqServiceTransRule(@RequestBody String param) {
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			return groupServiceService.getAcqServiceTransRule(jsonObject.getLong("acqServiceId"));
		} catch (Exception e) {
			log.error("获取收单服务限额失败！", e);
		}
		return null;
	}

	@RequestMapping(value = "/updateAcqServiceTransRule.do", method = RequestMethod.POST)
	@SystemLog(description = "修改限额",operCode="groupService.updateQuota")
	public @ResponseBody Map<String, Object> updateAcqServiceTransRule(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			AcqServiceTransRules rule = JSON.parseObject(param, AcqServiceTransRules.class);
			/*AcqServiceTransRules acqServiceTransRule = groupServiceService
					.getAcqServiceTransRule(Long.valueOf(rule.getAcqServiceId() + ""));*/
			int num = groupServiceService.updateAcqServiceTransRules(rule);
			String acqServiceQuota = "acqServiceQuota:" + rule.getAcqServiceId();
			// 手机号码有修改
			/*if (redisService.exists(acqServiceQuota)
					&& !rule.getWarningPhone().equals(acqServiceTransRule.getWarningPhone())) {
				redisService.insertHash(acqServiceQuota, "phone", rule.getWarningPhone());
			} else if (!((Integer) rule.getSavingsSingleMinAmount().intValue())
					.equals((Integer) acqServiceTransRule.getSavingsSingleMinAmount().intValue())
					|| !((Integer) rule.getSavingsSingleMaxAmount().intValue())
							.equals((Integer) acqServiceTransRule.getSavingsSingleMaxAmount().intValue())
					|| !((Integer) rule.getSavingsDayTotalAmount().intValue())
							.equals((Integer) acqServiceTransRule.getSavingsDayTotalAmount().intValue())
					|| !((Integer) rule.getCreditSingleMinAmount().intValue())
							.equals((Integer) acqServiceTransRule.getCreditSingleMinAmount().intValue())
					|| !((Integer) rule.getCreditSingleMaxAmount().intValue())
							.equals((Integer) acqServiceTransRule.getCreditSingleMaxAmount().intValue())
					|| !((Integer) rule.getCreditDayTotalAmount().intValue())
							.equals((Integer) acqServiceTransRule.getCreditDayTotalAmount().intValue())
					|| !((Integer) rule.getDayTotalAmount().intValue())
							.equals((Integer) acqServiceTransRule.getDayTotalAmount().intValue())) {
				List<String> keyList = new ArrayList<>();
				keyList.add(acqServiceQuota);
				redisService.delete(keyList);
			}*/
			List<String> keyList = new ArrayList<>();
			keyList.add(acqServiceQuota);
			redisService.delete(keyList);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改失败！");
		}
		return msg;
	}

	@RequestMapping(value = "/updateTimeSwitch", method = RequestMethod.POST)
	@SystemLog(description = "定时开关修改",operCode="groupService.updateTimeSwitch")
	@ResponseBody
	public  Map<String, Object> updateTimeSwitch(@RequestParam("info") String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			AcqService acq = JSON.parseObject(param, AcqService.class);
			int num = groupServiceService.updateTimeSwitch(acq);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "定时设置成功！");
			}
		} catch (Exception e) {
			log.error("定时设置失败！", e);
			msg.put("status", false);
			msg.put("msg", "定时设置失败！");
		}
		return msg;
	}
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/queryWaringInfo")
	@ResponseBody
	public WarningSet queryWaringInfo(Integer serviceId) {
		return warningSetService.getWaringInfoByService(serviceId);
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/serviceList")
	@ResponseBody
	public Page<Map<String, Object>> serviceList(String info) {
		Page<Map<String, Object>> page = groupServiceService.queryServiceInfoList();
		return page;
	}
}
