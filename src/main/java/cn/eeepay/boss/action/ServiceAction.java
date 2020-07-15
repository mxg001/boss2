package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@Controller
@RequestMapping(value = "/service")
public class ServiceAction {
	private static final Logger log = LoggerFactory.getLogger(ServiceAction.class);
	
	@Resource
	private ServiceProService serviceProService;
	
	@RequestMapping(value = "/addServiceType")
	@ResponseBody
	@SystemLog(description = "服务种类新增",operCode="service.insert")
	public Map<String,Object> addServiceType(@RequestBody String param) throws Exception{
		Map<String,Object> msg = new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ServiceInfo info=json.getObject("baseInfo", ServiceInfo.class);
			if(!(info.gettFlag()==1)){//非TO的时候T0TurnT1为0;
				info.setT0TurnT1("0");
			}
			if(!StringUtils.isNotBlank(info.getT0TurnT1())){
				info.setT0TurnT1("0");
			}
			
			List<ServiceRate> rates=JSON.parseArray(json.getJSONArray("rates").toJSONString(),ServiceRate.class);
			boolean isTop=false;
			for(ServiceRate rate:rates){
				serviceProService.setServiceRate(rate,true);
				rate.setServiceId(info.getServiceId());
				rate.setCheckStatus(0);
				rate.setLockStatus(0);
				rate.setAgentNo("0");
				if(rate.getRateType().equals("6")){
					isTop=true;
				}
			}
			List<ServiceQuota> quotas=JSON.parseArray(json.getJSONArray("quotas").toJSONString(), ServiceQuota.class);
			for(ServiceQuota quota:quotas){
				quota.setServiceId(info.getServiceId());
				quota.setCheckStatus(0);
				quota.setLockStatus(0);
				quota.setAgentNo("0");
			}
			info.setRates(rates);
			info.setQuotas(quotas);
			if(principal!=null){
				info.setCreatePerson(principal.getUsername());
			}
			info.setCreateTime(new Date());
			if(isTop&&info.getRateCard()==1&&"1".equals(info.getLoanMergeShow().toString())){
				msg.put("status", false);
				msg.put("msg", "封顶模式不支持借贷设置合并");
				return msg;
			}
			int num=serviceProService.insertServiceInfo(info);
			if(num>0)
				msg.put("status", true);
		}catch(Exception e){
			log.error("添加服务失败！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "添加服务失败");
			else
				msg.put("msg", str);	
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryServiceList")
	@ResponseBody
	public Page<ServiceInfo> queryServiceList(@RequestParam("baseInfo") String param,
			@ModelAttribute("page") Page<ServiceInfo> page) throws Exception{
		try{
			Map<String,Object> jsonMap = JSON.parseObject(param, HashMap.class);  
			serviceProService.getServiceInfo(jsonMap, page);
		}catch(Exception e){
			log.error("查询服务列表失败！",e);
		}
			return page;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryServiceDetail")
	@ResponseBody
	public ServiceInfo queryServiceDetail(@ModelAttribute("info")ServiceInfo info) throws Exception{
		ServiceInfo serviceInfo=null;
		try{
			serviceInfo=serviceProService.queryServiceDetail(info);
		}catch(Exception e){
			log.error("查询服务列表失败！",e);
		}
		return serviceInfo;
	}
	
	@RequestMapping(value = "/updateServiceDetail")
	@ResponseBody
	@SystemLog(description = "服务种类修改",operCode="service.edit")
	public Map<String,Object> updateServiceType(@RequestBody String param) throws Exception{
		Map<String,Object> msg = new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
			ServiceInfo info=json.getObject("baseInfo", ServiceInfo.class);
			boolean isChange = true;
			if(info!=null && info.getUsedStatus()==1){
				isChange = false;
			}
			if(info.gettFlag()!=null){
				if(!(info.gettFlag()==1)){//非TO的时候T0TurnT1为0;
					info.setT0TurnT1("0");
				}
			}else{
				info.setT0TurnT1("0");
			}
			
			if(!StringUtils.isNotBlank(info.getT0TurnT1())){
				info.setT0TurnT1("0");
			}
			
			List<ServiceRate> rates=JSON.parseArray(json.getJSONArray("rates").toJSONString(),ServiceRate.class);
			boolean isTop=false;
			for(ServiceRate rate:rates){
				serviceProService.setServiceRate(rate,isChange);
				rate.setCheckStatus(0);
				rate.setLockStatus(0);
				rate.setAgentNo("0");
				rate.setIsGlobal(1);
				rate.setServiceId(info.getServiceId());
				if(rate.getRateType().equals("6")){
					isTop=true;
				}
			}
			List<ServiceQuota> quotas=JSON.parseArray(json.getJSONArray("quotas").toJSONString(), ServiceQuota.class);
			for(ServiceQuota quota:quotas){
				quota.setCheckStatus(0);
				quota.setLockStatus(0);
				quota.setAgentNo("0");
				quota.setIsGlobal(1);
				quota.setServiceId(info.getServiceId());
			}
			info.setRates(rates);
			info.setQuotas(quotas);
			if(isTop&&info.getRateCard()==1&&"1".equals(info.getLoanMergeShow().toString())){
				msg.put("status", false);
				msg.put("msg", "封顶模式不支持借贷设置合并");
				return msg;
			}
			int num=serviceProService.updateServiceInfo(info);
			if(num>0)
				msg.put("status", true);
		}catch(Exception e){
			log.error("修改服务失败！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "修改服务失败");
			else
				msg.put("msg", str);	
		}
		return msg;
	}
	
	@RequestMapping(value = "/updateServiceStatus")
	@ResponseBody
	@SystemLog(description = "服务状态开关",operCode="service.switch")
	public Map<String,Object> updateServiceStatus(@RequestBody String param) throws Exception{
		Map<String,Object> msg = new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
			String id=json.getString("serviceId");
			String status=json.getString("status");
			if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(status)){
				serviceProService.updateServiceStatus(id,status);
				msg.put("status", true);
			}else{
				msg.put("status", false);
				msg.put("msg", "修改服务状态失败！参数错误！");
			}
		}catch(Exception e){
			log.error("修改服务状态失败！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "修改服务状态失败！");
			else
				msg.put("msg", str);
			
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getLinkServices")
	@ResponseBody
	public List<ServiceInfo> getLinkServices() throws Exception{
		List<ServiceInfo> list = new ArrayList<>();
		try{
			list = serviceProService.getLinkServices();
		}catch(Exception e){
			log.error("查询服务列表失败！",e);
		}
		return list;
	}
	
	@RequestMapping(value="/existServiceName")
	@ResponseBody
	public Map<String, Object> existServiceName(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSON.parseObject(param);
			ServiceInfo baseInfo=json.getObject("baseInfo", ServiceInfo.class);
//			ServiceInfo serviceInfo = JSONObject.parseObject("baseInfo", ServiceInfo.class);
			int num =  serviceProService.existServiceName(baseInfo);
			if (num>0){
				msg.put("status", true);
			} else {
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "验证服务名称失败");
			log.error("验证服务名称失败");
		}
		return msg;
	}

	@RequestMapping(value="/existAgentShowName")
	@ResponseBody
	public Map<String, Object> existAgentShowName(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSON.parseObject(param);
			ServiceInfo baseInfo=json.getObject("baseInfo", ServiceInfo.class);
			int num =  serviceProService.existAgentShowName(baseInfo);
			if (num>0){
				msg.put("status", true);
			} else {
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "验证代理商展示名称失败");
			log.error("验证代理商展示名称失败");
		}
		return msg;
	}

	//******************************************
	//=========代理商分润设置===========================================
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryAgentProfit")
	@ResponseBody
	public List<AgentShareRule> queryAgentProfit(@ModelAttribute("info")ServiceInfo info) throws Exception{
		List<AgentShareRule> list=null;
		try{
			list=serviceProService.queryAgentProfit(info);
		}catch(Exception e){
			e.printStackTrace();
			log.error("代理商分润设置失败！",e);
		}
		return list;
	}

	//=============新增或修改代理商分润=======================================
	@RequestMapping(value = "/saveAgentProfit")
	@ResponseBody
	public Map<String,Object> saveAgentProfit(@RequestBody String param) throws Exception{
		Map<String,Object> msg = new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
			int num=serviceProService.saveAgentProfit(json);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "添加||修改默认分润成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("添加||修改默认分润失败！",e);
			msg.put("status", false);
			String str=e.getMessage();
			if(str.contains("\r\n")||str.contains("\n")){
				msg.put("msg", "添加||修改默认分润失败");
			}else{
				msg.put("msg", str);
			}
		}
		return msg;
	}

	/**
	 *	获取提现服务类型
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getServiceInfo")
	@ResponseBody
	public Object selectBoxAllInfo() {
		List<ServiceInfo> list = null;
		try {
			list=serviceProService.selectServiceInfo();
		} catch (Exception e) {
			log.error("获取提现服务类型list异常！！", e);
		}
		return list;
	}

	/**
	 * create by: tans 2018/7/31 17:44
	 * description:修改服务生效状态
	 * @return
	 */
	@RequestMapping("/updateEffectiveStatus")
	@ResponseBody
	@SystemLog(operCode = "service.updateEffectiveStatus", description = "修改服务生效状态")
	public Result updateEffectiveStatus(@RequestBody ServiceInfo baseInfo){
		Result result;
		try {
			result = serviceProService.updateEffectiveStatus(baseInfo);
		} catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("添加||修改默认分润失败！",e);
		}
		return result;
	}

	/**
	 * 查询所有有效的服务
	 * 目前字段：服务ID，名称（有需要可在dao层加）
	 * @return
	 */
	@RequestMapping("/selectList")
	@ResponseBody
	public Result selectList(){
		Result result = new Result();
		try {
			List<ServiceInfo> list = serviceProService.selectServiceName();
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(list);
		} catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询所有有效的服务异常",e);
		}
		return result;
	}


	@RequestMapping("/exportSelectList")
	@ResponseBody
	@SystemLog(description = "服务种类查询-导出",operCode = "service.exportSelectList")
	public List<AppInfo> exportSelectList(@RequestParam("baseInfo") String baseInfo, @Param("page")Page page, HttpServletResponse response, HttpServletRequest request)throws Exception{
		Map<String, Object> msgMap = new HashMap<>();
		try {
			Map<String,Object> jsonMap = JSON.parseObject(baseInfo, HashMap.class);
			serviceProService.getServiceInfoToExport(jsonMap,response,msgMap);
		} catch (Exception e) {
			log.error("服务种类查询-导出出错",e);
			msgMap.put("status", false);
			msgMap.put("msg", "服务种类查询-导出出错！");
		}
		return null;
	}
}
