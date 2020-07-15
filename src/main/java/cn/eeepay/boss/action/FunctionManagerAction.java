package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchange.PropertyConfig;
import cn.eeepay.framework.model.function.AppShowConfig;
import cn.eeepay.framework.model.function.FunctionTeam;
import cn.eeepay.framework.service.BossSysConfigService;
import cn.eeepay.framework.service.FunctionManagerService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能控制总开关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/functionManager")
public class FunctionManagerAction {

	private static final Logger log = LoggerFactory.getLogger(FunctionManagerAction.class);

	public static final String FLUSHFUNCTIONNUMBER="015";//充值返功能编码
	public static final String FLUSHFUNCTIONNUMBER_BUYREWARD="023";//购买鼓励金功能编码
	public static final String FUNCTION_MANAGE = "FUNCTION_MANAGE_";//费率调控，sys_config的key前缀
	public static final String FORCE_T1_CONTROL_CHANNEL = "FORCE_T1_CONTROL_CHANNEL";//强制T1控制通道
	@Resource
	private FunctionManagerService functionManagerService;

	@Resource
	private BossSysConfigService bossSysConfigService;
	@Resource
	private SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectFunctionManagers.do")
	@ResponseBody
	public List<FunctionManager> selectFunctionManagers() {
		List<FunctionManager> list = new ArrayList<>();
		try {
			list = functionManagerService.selectFunctionManagers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@RequestMapping(value="/updateVasRateStatus.do")
	@ResponseBody
	@SystemLog(description = "增值服务费状态开关",operCode="func.updateVasRateStatus")
	public  Object updateVasRateStatus(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		VasRate info = JSON.parseObject(param, VasRate.class);
		try {
			int i = functionManagerService.updateVasRateStatus(info);
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

	@RequestMapping(value="/updateFunctionManageConfigStatus.do")
	@ResponseBody
	@SystemLog(description = "功能开关设置表状态开关",operCode="func.updateFunctionManageConfigStatus")
	public  Object updateFunctionManageConfigStatus(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		FunctionTeam info = JSON.parseObject(param, FunctionTeam.class);
		try {
			int i = functionManagerService.updateFunctionManageConfigStatus(info);
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
	
	@RequestMapping(value="/updateFunctionSwitch.do")
	@ResponseBody
	@SystemLog(description = "功能控制功能开关",operCode="func.switchManager")
	public  Object updateFunctionSwitch(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		FunctionManager info = JSON.parseObject(param, FunctionManager.class);
		try {
			int i = functionManagerService.updateFunctionSwitch(info);
			if(i>0){
				FunctionManager fm=functionManagerService.getFunctionManager(info.getId());
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(1);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(1);
				}
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
	@RequestMapping(value="/updateAgentControl.do")
	@SystemLog(description = "是否开启代理商控制",operCode="func.agentManager")
	public @ResponseBody Object updateAgentControl(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		FunctionManager info = JSON.parseObject(param, FunctionManager.class);
		try {
			int i = functionManagerService.updateAgentControl(info);
			if(i>0){
				FunctionManager fm=functionManagerService.getFunctionManager(info.getId());
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(2);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(2);
				}
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
	
	@RequestMapping(value="/industrySwitch")
	@ResponseBody
	public Result industrySwitch() {
		Result result = new Result();
		try {
			IndustrySwitchInfo info = functionManagerService.getIndustrySwitchInfo();
			result.setStatus(true);
			result.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}
	@RequestMapping(value="/industrySwitchSave")
	@ResponseBody
	@SystemLog(description = "保存行业切换配置",operCode="func.industrySwitchActivitySave")
	public Result industrySwitchSave(@RequestBody IndustrySwitch data) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchSave(data);
			result.setStatus(true);
		} catch (Exception e) {
			if (!( e instanceof BossBaseException)) {
				e.printStackTrace();
			}
			result.setStatus(false);
		}
		return result;
	}
	
	@RequestMapping(value="/industrySwitchDelete")
	@ResponseBody
	@SystemLog(description = "删除行业切换配置",operCode="func.industrySwitchActivityDelete")
	public Result industrySwitchDelete(@RequestBody IndustrySwitch data) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchDelete(data.getId());
			result.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}
	
	@RequestMapping(value="/industrySwitchUpdate")
	@ResponseBody
	@SystemLog(description = "修改行业切换开关",operCode="func.industrySwitchActivityUpdate")
	public Result industrySwitchUpdate(boolean industrySwitch) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchUpdate(industrySwitch?1:0);
			result.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
		
	}

	/**
	 * 修改功能开关基本信息
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value="/updateBaseInfo")
	@ResponseBody
	@SystemLog(description = "修改功能开关基本信息",operCode="func.updateBaseInfo")
	public Result updateBaseInfo(@RequestBody FunctionManager baseInfo) {
		Result result;
		try {
			String functionName = baseInfo.getFunctionName();
			String remark = baseInfo.getRemark();
			if(StringUtil.isEmpty(functionName)) {
				return Result.fail("开关名称不能为空");
			}
			if(functionName.length() > 100) {
				return Result.fail("开关名称不能超过100个字");
			}
			if(!StringUtil.isEmpty(remark) && remark.length() > 200) {
				return Result.fail("开关说明不能超过200个字");
			}
			//更新功能开关基本信息
			functionManagerService.updateBaseInfo(baseInfo);
			//强制T1功能开关更新显示状态字段
			if("062".equals(baseInfo.getFunctionNumber())){
				bossSysConfigService.updateValueByKey("FORCE_T1_SHOW_STATUS", baseInfo.getForceT1ShowStatus());
			}
			result = Result.success();
		} catch (Exception e) {
			log.error("修改功能开关基本信息异常", e);
			result = Result.fail("操作异常");
		}
		return result;
	}

	/**
	 * 保存组织管控信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/saveSettingList")
	@ResponseBody
	@SystemLog(description = "保存组织管控信息",operCode="func.saveSettingList")
	public Result saveSettingList(@RequestBody Map<String, Object> param) {
		Result result;
		try {
			String functionNumber = String.valueOf(param.get("functionNumber"));
			if(StringUtil.isEmpty(functionNumber)) {
				return Result.fail("参数不能为空");
			}

			int num = 0;
			if("062".equals(functionNumber)){
				String channels = (String) param.get("channels");
				JSONObject channelsJson = JSONObject.parseObject(channels);
				JSONArray channelList = JSONObject.parseArray(channelsJson.getString("channels"));
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < channelList.size(); i++) {
					Map<String, String> map =  (Map<String, String>)channelList.get(i);
					map.get("channel");
					sb.append( map.get("channel"));
					if(i != channelList.size() - 1){
						sb.append(",");
					}
				}
				num = bossSysConfigService.updateValueByKey(FORCE_T1_CONTROL_CHANNEL, sb.toString());
			}else {
				String valueInfo = (String) param.get("valueInfo");
				num = updateFunctionManagerSysConfig(valueInfo, functionNumber);
			}

			if(num == 1) {
				result = Result.success();
			} else {
				result = Result.fail();
			}
		} catch (Exception e) {
			log.error("保存组织管控信息异常", e);
			result = Result.fail("操作异常");
		}
		return result;
	}

	private int updateFunctionManagerSysConfig(String valueInfo, String functionNumber) {
		String sysKey = FUNCTION_MANAGE + functionNumber;
		return bossSysConfigService.updateValueByKey(sysKey, valueInfo);
	}

	/**
	 * 查询功能开关基本信息和业务配置
	 * @param functionNumber
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectSettingList")
	@ResponseBody
	public Result selectSettingList(String functionNumber) {
		Result result;
		try {
			if(StringUtil.isEmpty(functionNumber)) {
				return Result.fail("参数不能为空");
			}

			Map<String, Object> map = new HashMap<>();
			FunctionManager baseInfo = functionManagerService.getFunctionManagerByNum(functionNumber);
			map.put("baseInfo", baseInfo);

			if("062".equals(functionNumber)){
				String forceT1ShowStatus = bossSysConfigService.selectValueByKey("FORCE_T1_SHOW_STATUS");
				baseInfo.setForceT1ShowStatus(forceT1ShowStatus);
				String channel = bossSysConfigService.selectValueByKey(FORCE_T1_CONTROL_CHANNEL);
				if(StringUtils.isNotBlank(channel)){
					String[] channels = channel.split(",");
					StringBuilder sb = new StringBuilder("{\"channels\":[");
					for (int i = 0; i < channels.length; i++) {
						sb.append("{\"channel\":\"" + channels[i] + "\"}");
						if(i != channels.length - 1){
							sb.append(",");
						}
					}
					sb.append("]}");
					JSONObject channelsJson = JSONObject.parseObject(sb.toString());
					JSONArray channelList = JSONObject.parseArray(channelsJson.getString("channels"));
					Map<String, Object> channelValue = new HashMap<>();
					channelValue.put("channels", channelList);
					map.put("channelValue", channelValue);
				}
			}else {
				String sysKey = FUNCTION_MANAGE + functionNumber;
				String sysValue = bossSysConfigService.selectValueByKey(sysKey);
				Map<String, Object> valueInfo = new HashMap<>();
				if(!StringUtil.isEmpty(sysValue)) {
					JSONObject sysValueJson = JSONObject.parseObject(sysValue);
					JSONArray teams = JSONObject.parseArray(sysValueJson.getString("teams"));
					valueInfo.put("teams", teams);
				}
				map.put("valueInfo", valueInfo);
			}
			result = Result.success("查询成功");
			result.setData(map);
		} catch (Exception e) {
			log.error("查询功能开关基本信息和业务配置异常", e);
			result = Result.fail("查询异常");
		}
		return result;
	}


    /**
     * 获取开关详细信息
     * @return
     */
    @RequestMapping(value="/getFunctionManagerInfo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public  Map<String,Object>  getFunctionManagerInfo(@RequestParam("functionNumber") String functionNumber) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            FunctionManager info=functionManagerService.getFunctionManagerByNum(functionNumber);
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e) {
            log.error("获取开关详细信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取开关详细信息异常!");
        }
        return msg;
    }

	/**
	 * 获取组织管控实体信息
	 * @return
	 */
	@RequestMapping(value="/getTeamModelInfo")
	@ResponseBody
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	public  Map<String,Object>  getTeamModelInfo(@RequestParam("functionNumber") String functionNumber,@RequestParam("id") int id) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {

			if("073".equals(functionNumber)){
				VasRate info=functionManagerService.getVasRateById(id);
				msg.put("status", true);
				msg.put("info", info);
			}else{
				FunctionTeam info=functionManagerService.getFunctionTeamById(functionNumber,id);
				msg.put("status", true);
				msg.put("info", info);
			}

		} catch (Exception e) {
			log.error("获取组织管控实体信息异常!",e);
			msg.put("status", false);
			msg.put("msg", "获取组织管控实体信息异常!");
		}
		return msg;
	}

	/**
	 * 查询收费订单、收费交易方式
	 */
	@RequestMapping(value = "/get073NeedTypes")
	@ResponseBody
	public Map<String,Object> get073NeedTypes() throws Exception{
		Map<String, Object> msg=new HashMap<String,Object>();
		try{
			List<SysDict> orderTypes = sysDictService.selectByKey("ORDER_TYPE");
			List<SysDict> serviceTypes = sysDictService.selectByKey("SERVICE_TYPE");
			List<SysDict> merchantTypes = sysDictService.selectByKey("MERCHANT_TYPE_LIST");
			msg.put("status", true);
			msg.put("orderTypes", orderTypes);
			msg.put("serviceTypes", serviceTypes);
			msg.put("merchantTypes", merchantTypes);
		} catch (Exception e){
			log.error("查询收费订单、收费交易方式失败!",e);
			msg.put("status", false);
			msg.put("msg", "查询收费订单、收费交易方式失败!");
		}
		return msg;
	}


    /**
     * 获取开关组织列表
     * @return
     */
    @RequestMapping(value="/getFunctionManagerTeamList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public  Map<String,Object>  getFunctionManagerTeamList(@RequestParam("functionNumber") String functionNumber) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
        	if("073".equals(functionNumber)){
				List<VasRate> list=functionManagerService.getVasRateList(functionNumber);
				msg.put("status", true);
				msg.put("list", list);
			}else{
				List<FunctionTeam> list=functionManagerService.getFunctionTeamList(functionNumber);
				msg.put("status", true);
				msg.put("list", list);
			}

        } catch (Exception e) {
            log.error("获取开关组织列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取开关组织列表异常!");
        }
        return msg;
    }

    /**
     * 保存开关功能-组织
     * @return
     */
    @RequestMapping(value="/saveFunctionTeam")
    @ResponseBody
    @SystemLog(description = "保存组织管控信息",operCode="func.saveSettingList")
    public  Map<String,Object>  saveFunctionTeam(@RequestParam("info") String param) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            FunctionTeam info=JSONObject.parseObject(param, FunctionTeam.class);
            //互斥激活
			if(null==info.getId()){
				functionManagerService.saveFunctionTeam(info,msg);
			}else{
				functionManagerService.updateFunctionTeam(info,msg);
			}
        } catch (Exception e) {
            log.error("保存开关功能-组织异常!",e);
            msg.put("status", false);
            msg.put("msg", "保存开关功能-组织异常!");
        }
        return msg;
    }

	/**
	 * 修改开关功能-组织
	 * @return
	 */
	@RequestMapping(value="/updateFunctionTeam")
	@ResponseBody
	@SystemLog(description = "修改组织管控信息",operCode="func.saveSettingList")
	public  Map<String,Object>  updateFunctionTeam(@RequestParam("info") String param) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			FunctionTeam info=JSONObject.parseObject(param, FunctionTeam.class);
			functionManagerService.updateFunctionTeamVas(info,msg);
		} catch (Exception e) {
			log.error("修改开关功能-组织异常!",e);
			msg.put("status", false);
			msg.put("msg", "修改开关功能-组织异常!");
		}
		return msg;
	}
    /**
     * 删除开关功能-组织
     * @return
     */
    @RequestMapping(value="/deleteFunctionTeam")
    @ResponseBody
    @SystemLog(description = "删除组织管控信息",operCode="func.saveSettingList")
    public  Map<String,Object>  deleteFunctionTeam(@RequestParam("id") int id) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            int num=functionManagerService.deleteFunctionTeam(id);
            if(num>0){
                //调用CORE更新数据
				if(num==2){
					clearCache(id);
				}

                msg.put("status", true);
                msg.put("msg", "删除成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除失败!");
            }
        } catch (Exception e) {
            log.error("删除开关功能-组织异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除开关功能-组织异常!");
        }
        return msg;
    }

	/**
	 * 删除开关功能-组织/特殊功能编码使用
	 * @return
	 */
	@RequestMapping(value="/deleteSpecialFunctionTeam")
	@ResponseBody
	@SystemLog(description = "删除组织管控信息",operCode="func.saveSettingList")
	public  Map<String,Object>  deleteSpecialFunctionTeam(@RequestParam("functionNumber") String functionNumber,@RequestParam("id") int id) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			int num=functionManagerService.deleteSpecialFunctionTeam(functionNumber,id);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "删除成功!");
			}else{
				msg.put("status", false);
				msg.put("msg", "删除失败!");
			}
		} catch (Exception e) {
			log.error("删除开关功能-组织异常!",e);
			msg.put("status", false);
			msg.put("msg", "删除开关功能-组织异常!");
		}
		return msg;
	}


	/**
	 * 保存组织业务配置
	 * @return
	 */
	@RequestMapping(value="/saveFunctionConfigure")
	@ResponseBody
	@SystemLog(description = "保存组织业务配置",operCode="func.saveSettingList")
	public  Map<String,Object>  saveFunctionConfigure(@RequestParam("info") String param) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			FunctionTeam info=JSONObject.parseObject(param, FunctionTeam.class);
			functionManagerService.saveFunctionConfigure(info,msg);
		} catch (Exception e) {
			log.error("保存开关功能-业务配置异常!",e);
			msg.put("status", false);
			msg.put("msg", "保存开关功能-业务配置异常!");
		}
		return msg;
	}

	/**
     * 获取APP首页显示配置
	 * @return
	 */
	@RequestMapping(value="/getAppShowList")
	@ResponseBody
	public  Map<String,Object>  saveFunctionTeam(@RequestParam("fmcId") int fmcId) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			List<AppShowConfig> list=functionManagerService.getAppShowList(fmcId);
			msg.put("status", true);
			msg.put("list", list);
		} catch (Exception e) {
			log.error("获取APP首页显示配置异常!",e);
			msg.put("status", false);
			msg.put("msg", "获取APP首页显示配置异常!");
		}
		return msg;
	}

	/**
	 * 保存APP首页显示配置
	 * @return
	 */
	@RequestMapping(value="/saveAppShowList")
	@ResponseBody
    @SystemLog(description = "保存APP首页显示配置",operCode="func.saveSettingList")
    public  Map<String,Object>  saveAppShowList(@RequestParam("addList") String addListStr,@RequestParam("fmcId") int fmcId) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			List<AppShowConfig> addList=JSONObject.parseArray(addListStr,AppShowConfig.class);
			int num=functionManagerService.saveAppShowList(fmcId,addList);
			if(num>0){
			   	//修改成功,调用core更新数据
				clearCache(fmcId);
				msg.put("status", true);
				msg.put("msg", "保存成功!");
			}else{
				msg.put("status", false);
				msg.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			log.error("保存APP首页显示配置异常!",e);
			msg.put("status", false);
			msg.put("msg", "保存APP首页显示配置异常!");
		}
		return msg;
	}

	/**
	 * 调用core清楚缓存
	 */
	private void clearCache(int fmcId){
		String params = "fmcId=" + fmcId;
		SysDict sysDict = sysDictService.getByKey("CORE_SERVICE_URL");
		String accessUrl = sysDict.getSysValue() + "/cjf/delAppFunction";
		HttpUtils.sendPost(accessUrl, params, "UTF-8");
	}

	@RequestMapping(value="/getFunctionTeamById")
	@ResponseBody
	public  Map<String,Object>  getFunctionTeamById(@RequestParam("id") int id) {
		Map<String, Object> msg=new HashMap<String,Object>();
		try {
			FunctionTeam ft = functionManagerService.getFunctionTeam(id);
			String bakJson = ft.getBakJson();
			JSONObject json = JSONObject.parseObject(bakJson);
			ft.setActivity_days(json.getInteger("day_number"));
			ft.setBeginTime(json.getString("begin_time"));
			ft.setEndTime(json.getString("end_time"));
			msg.put("status", true);
			msg.put("info", ft);
		} catch (Exception e) {
			log.error("获取组织异常!",e);
			msg.put("status", false);
			msg.put("msg", "获取组织异常!");
		}
		return msg;
	}
}
