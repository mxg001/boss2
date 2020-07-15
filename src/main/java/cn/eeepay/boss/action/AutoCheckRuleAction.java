package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AutoCheckRoute;
import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;
import cn.eeepay.framework.service.AutoCheckRuleService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/autoCheckRule")
public class AutoCheckRuleAction {
	
	@Resource
	private AutoCheckRuleService autoCheckRuleService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/autoCheckRule.do")
	@SuppressWarnings("all")
	public @ResponseBody Object selectByParamKey(String paramKey) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			SysConfigAutoCheck  singleMerchTimes = autoCheckRuleService.selectByParamKey("single_merch_times");
			SysConfigAutoCheck bankCardOcr = autoCheckRuleService.selectByParamKey("bank_card_ocr");
			SysConfigAutoCheck age = autoCheckRuleService.selectByParamKey("age_limit");
			SysConfigAutoCheck minAge = age;

			map.put("singleMerchTimes",singleMerchTimes);
			map.put("bankCardOcr",bankCardOcr);
			map.put("minAge",minAge);
			List<AutoCheckRoute> livingList  = autoCheckRuleService.listByRouteType(1);
			map.put("livingJskj",livingList.get(0));
			map.put("livingXskj",livingList.get(1));
			List<AutoCheckRoute> ocrList  = autoCheckRuleService.listByRouteType(2);
			map.put("ocrYlsw",ocrList.get(0));
			map.put("ocrJskj",ocrList.get(1));
			List<AutoCheckRoute> routingList  = autoCheckRuleService.listByRouteType(3);
			map.put("routingYlsw",routingList.get(0));
			map.put("routingJskj",routingList.get(1));
			map.put("routingXskj",routingList.get(2));
			map.put("realYlsw",autoCheckRuleService.selectByChannelCode("YLSW"));
			map.put("realJskj",autoCheckRuleService.selectByChannelCode("JSKJ"));
			map.put("realXskj",autoCheckRuleService.selectByChannelCode("XSKJ"));

			map.put("livingType", autoCheckRuleService.selectByParamKey("living_type").getParamValue());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 规则查询
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/autoCheckRuleAll.do")
	@ResponseBody
	public List<AutoCheckRule> selectAll() throws Exception {
		List<AutoCheckRule> list=new ArrayList<>();
		try {
			
			 list = autoCheckRuleService.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	@RequestMapping(value="/updateIsOpen.do")
	@SystemLog(description = "自动审件是否打开",operCode="func.switch")
	public @ResponseBody Object updateIsOpen(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		AutoCheckRule info = JSON.parseObject(param, AutoCheckRule.class);
		try {
			int i = autoCheckRuleService.updateIsOpen(info);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	@RequestMapping(value="/updateIsPass.do")
	@SystemLog(description = "自动审件是否必过",operCode="func.must")
	public @ResponseBody Object updateIsPass(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		AutoCheckRule info = JSON.parseObject(param, AutoCheckRule.class);
		try {
			int i = autoCheckRuleService.updateIsPass(info);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	
	/**
	 * 保存修改自动审件控制
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateValues.do")
	@SystemLog(description = "保存修改自动审件控制",operCode="func.save")
	public @ResponseBody Object updateValue(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> sysConfigAutoCheck = JSON.parseObject(param, Map.class);
		try {
			int i = autoCheckRuleService.updateValue(sysConfigAutoCheck);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "修改成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "修改失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	
}
