package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.SuperPushConfig;
import cn.eeepay.framework.model.SuperPushShareRule;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.BusinessProductInfoService;
import cn.eeepay.framework.service.SuperPushConfigService;
import cn.eeepay.framework.service.SysDictService;

/**
 * 微创业配置
 * @author tans
 * @version 创建时间：2017年5月5日 上午10:15:31
 */
@Controller
@RequestMapping(value="/superPushConfig")
public class SuperPushConfigAction {

	private static final Logger log = LoggerFactory.getLogger(SuperPushConfigAction.class);
	
	@Resource
	private SysDictService sysDictService;
	
	@Resource
	private SuperPushConfigService superPushConfigService;
	
	@Resource
	private BusinessProductInfoService businessProductInfoService;
	
	/**
	 * 获取微创业的相关配置信息
	 * @author tans
	 * @version 创建时间：2017年5月5日 上午10:20:08
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getInfo")
	@ResponseBody
	public Map<String, Object> getInfo(){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			SysDict sysDict = sysDictService.getByKey("SUPER_PUSH_BP_ID");
			if(sysDict==null){
				msg.put("msg", "微创业的业务产品为空");
				return msg;
			}
			String bpId = sysDict.getSysValue();
			
			
			SysDict sysDictList = sysDictService.getByKey("SUPER_PUSH_BP_ID_LIST");
			if(sysDictList==null){
				msg.put("msg", "微创业的业务产品为空");
				return msg;
			}
			String bpIdList = sysDictList.getSysValue();
//			String bpId1 = bpId.split(",")[0];
//			String bpId2 = bpId.split(",")[1];
			//1.获取微创业的配置信息
			SuperPushConfig info  = superPushConfigService.getByBpId(bpId);//微创业配置信息
			//1.1如果数据库里面的配置信息为空，设置初始化信息
			if(info==null){
				info = new SuperPushConfig();
				info.setBpId(bpId);
				info.setIncentiveFundSwitch(0);
			}
			//2.获取业务产品的分润信息
			List<SuperPushShareRule> shareRuleList = superPushConfigService.getServiceListByBp(bpIdList);
//			List<SuperPushShareRule> shareRuleList2 = getServiceListByBp(bpId2);
//			shareRuleList.addAll(shareRuleList2);
//			//3.获取微创业的代理商集合
//			List<AgentInfo> agentList = new ArrayList<>();
//			if(info!=null&&StringUtils.isNotBlank(info.getAppAgentNoList())){
//				String[] strArr = info.getAppAgentNoList().split(",");
//				agentList = superPushConfigService.getAgentList(strArr);
//			}
			msg.put("status", true);
			msg.put("info", info);
			msg.put("shareRuleList", shareRuleList);
//			msg.put("agentList", agentList);
		} catch (Exception e) {
			msg.put("msg", "微创业配置初始化失败");
			log.error("获取微创业的相关配置信息失败,",e);
		}
		return msg;
	}
	
	@RequestMapping(value="/saveShareRule")
	@ResponseBody
	@SystemLog(description = "保存微创业分润配置",operCode="superPushConfig.saveShareRule")
	public Map<String, Object> saveShareRule(@RequestBody SuperPushShareRule rule){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			msg = superPushConfigService.updateShareRule(rule);
		} catch (Exception e) {
			log.error("保存分润配置失败,分润配置:[{}]",JSONObject.toJSONString(rule),e);
			msg.put("status", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				msg.put("msg", "分润配置信息不完整");
				return msg;
			}
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "保存分润配置信息异常");
			else
				msg.put("msg", str);	
			log.error("保存分润配置信息异常",e);
		}
		return msg;
	}
	
	/**
	 * 保存微创业配置信息
	 * @author tans
	 * @version 创建时间：2017年5月8日 下午2:55:27
	 */
	@RequestMapping(value = "/saveConfig")
	@ResponseBody
	@SystemLog(description = "保存微创业配置",operCode="superPushConfig.saveConfig")
	public Map<String, Object> saveConfig(@RequestBody String data){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		JSONObject json=JSONObject.parseObject(data);
		try {
			msg = superPushConfigService.saveConfig(json);
		} catch (Exception e) {
			log.error("保存微创业配置失败,配置:[{}]",JSONObject.toJSONString(json),e);
		}
		return msg;
	}
	
}
