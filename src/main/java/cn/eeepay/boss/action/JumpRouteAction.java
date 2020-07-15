package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.JumpRouteService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.SysWarningService;
import cn.eeepay.framework.service.impl.PosCardBinServiceImpl;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author tans
 * @date 2017年3月14日 下午3:23:10
 */
@Controller
@RequestMapping(value="/jumpRoute")
public class JumpRouteAction {

	public static final String ACQ="ACQ_MERCHANT_TYPE";//收单商户类别 数据字典key值

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private JumpRouteService jumpRouteSevice;
	
	@Resource
	private MerchantInfoService merchantInfoService;
	
	@Resource
	private PosCardBinServiceImpl posCardBinService;
	
   	@Resource
	private SysDictService sysDictService;

	@Resource
	private SysWarningService sysWarningService;
	
	/**
	 * 
	 * @author tans
	 * @date 2017年3月14日 下午3:27:14
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/page.do")
	@ResponseBody
	public Page<JumpRouteConfig> list(@RequestParam("baseInfo") String param, 
			@ModelAttribute("page") Page<JumpRouteConfig> page){
		logger.info("入参：" + JSONObject.toJSONString(param));
		try {
			JumpRouteConfig baseInfo = JSONObject.parseObject(param, JumpRouteConfig.class);
			jumpRouteSevice.list(baseInfo, page);
		} catch (Exception e) {
			logger.error("条件查询跳转集群配置列表失败",e);
		}
		return page;
	}
	
	/**
	 * 保存交易跳转集群配置
	 * @author tans
	 * @date 2017年3月15日 上午11:52:24
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/save.do")
	@ResponseBody
	@SystemLog(description = "保存",operCode="jumpRoute.save")
	public Result save(@RequestBody String params){
		Result result = Result.fail();
		try {
			int num = jumpRouteSevice.save(params);
			if(num == 1){
				result = Result.success();
			}
		} catch (Exception e) {
			logger.error("保存交易跳转集群配置失败", e);
			result = ResponseUtil.buildResult(e);
		}
		return result;
	}
	
	/**
	 * 修改交易跳转集群的配置
	 * @author tans
	 * @date 2017年3月15日 下午4:49:28
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update/{id}")
	@ResponseBody
	public Map<String, Object> update(@PathVariable("id") Integer id){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			JumpRouteConfig baseInfo = jumpRouteSevice.getById(id);
			msg.put("status", true);
			msg.put("baseInfo", baseInfo);
		} catch (Exception e) {
			logger.error("保存交易跳转集群配置失败", e);
		}
		return msg;
	}
	
	/**
	 * 删除交易跳转集群的配置
	 * @author tans
	 * @date 2017年3月16日 下午3:05:36
	 * @param
	 * @return
	 */
	@RequestMapping(value="/delete.do")
	@ResponseBody
	@SystemLog(description = "删除交易跳转集群配置",operCode="jumpRoute.delete")
	public Map<String, Object> delete(@RequestParam("id") String id){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "删除失败");
		try {
			int num = jumpRouteSevice.delete(id);
			if(num>0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			}
		} catch (Exception e) {
			logger.error("删除交易跳转集群的配置失败", e);
		}
		return msg;
	}
	
	/**** 交易跳转集群白名单管理  ******/
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllWlInfo")
	@ResponseBody
	public Map<String, Object> selectAllWlInfo(){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		try {
			List<AcpWhitelist> result = jumpRouteSevice.selectAllWlInfo();
			msg.put("status", true);
			msg.put("result", result);
		} catch (Exception e) {
			logger.error("查询交易跳转集群配置商户白名单失败", e);
		}
		return msg;
	}
	
	@RequestMapping(value = "/deleteWlInfo")
	@ResponseBody
	@SystemLog(description = "交易跳转集群白名单删除",operCode="jumpRoute.deleteWhite")
	public Object deleteWlInfo(@RequestParam("ids") String param) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(param, Integer.class);
			int i = jumpRouteSevice.deleteByWlid(id);
			if (i > 0) {
				jsonMap.put("msg", "删除成功");
				jsonMap.put("status", true);
			} else {
				jsonMap.put("msg", "删除失败");
				jsonMap.put("status", false);
			}
		} catch (Exception e) {
			logger.error("交易跳转集群白名单删除报错", e);
			jsonMap.put("msg", "交易跳转集群白名单删除报错");
			jsonMap.put("status", false);
		}
		return jsonMap;
	}
	
	/**
	 * 收单机构白名单新增
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/addWlInfo")
	@ResponseBody
	@SystemLog(description = "交易跳转集群白名单新增",operCode="jumpRoute.insertWhite")
	public Object addWlInfo(@RequestParam("info") String merchantNo) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			AcpWhitelist aw = new AcpWhitelist();
			aw.setMerchantNo(merchantNo);
			if (merchantInfoService.selectByMerNo(merchantNo) == null) {
				jsonMap.put("msg", "商户编号不正确");
				jsonMap.put("status", false);
				return jsonMap;
			}
			AcpWhitelist dataAl = jumpRouteSevice.getWlInfoByMerchantNo(merchantNo);
			if(dataAl != null){
				jsonMap.put("msg", "商户编号已存在");
				jsonMap.put("status", false);
				return jsonMap;
			}
			
			aw.setCreatePerson(principal.getRealName());
			int i = jumpRouteSevice.insertWl(aw);
			if (i > 0) {
				jsonMap.put("msg", "新增成功");
				jsonMap.put("status", true);
			} else {
				jsonMap.put("msg", "新增失败");
				jsonMap.put("status", false);
			}
		} catch (Exception e) {
			logger.error("交易跳转集群白名单新增报错~~~~~", e);
			jsonMap.put("msg", "交易跳转集群白名单新增报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getCarBinList")
	@ResponseBody
	public List<Map<String,Object>> getCarBinList(@RequestParam("cardType") int cardType) {
		return posCardBinService.getCarBinList(cardType);
	}
	
	/**** 交易跳转集群白名单管理  ******/


	/**
	 * 获取收单商户类别
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getSysDictList")
	@ResponseBody
	public List<SysDict> getSysDictList(){
		return sysDictService.getAcqMerchantList(JumpRouteAction.ACQ);
	}
	
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@ResponseBody
	@RequestMapping("/getServiceTypeSelect")
	public List<SysDict> getServiceTypeSelect(String bqIds){
		Integer[] ids = JSONObject.parseObject(bqIds,Integer[].class);
		return jumpRouteSevice.getServiceTypeSelectByBqIds(ids);
	}

	/**
	 * 目标金额预警
	 * @return
	 */
	@RequestMapping(value = "/selectTargetAmountWarning")
	@ResponseBody
	public Object selectTargetAmountWarning() {
		Map<String, Object> maps = new HashMap<>();
		try {
			maps.put("yesAmountWarning", sysWarningService.getByType("3"));
			maps.put("noAmountWarning", sysWarningService.getByType("4"));
			maps.put("status", true);
		} catch (Exception e) {
			logger.error("目标金额预警查询报错", e);
			maps.put("msg", "目标金额预警查询报错");
			maps.put("status", false);
		}
		return maps;
	}


	/**
	 * 目标金额预警修改
	 * @return
	 */
	@RequestMapping(value = "/updateTargetAmountWarning")
	@ResponseBody
	@SystemLog(description = "目标金额预警修改", operCode = "jumpRoute.updateTargetAmountWarning")
	public Object updateTargetAmountWarning(@RequestBody String param) {
		Map<String, Object> maps = new HashMap<>();
		JSONObject json = JSON.parseObject(param);
		Map<String, Object> yesAmountWarning = json.getObject("yesAmountWarning", Map.class);
		Map<String, Object> noAmountWarning = json.getObject("noAmountWarning", Map.class);
		try {
			sysWarningService.updateSysWarning(yesAmountWarning);
			sysWarningService.updateSysWarning(noAmountWarning);
			maps.put("status", true);
		} catch (Exception e) {
			logger.error("目标金额预警修改报错", e);
			maps.put("msg", "目标金额预警修改报错");
			maps.put("status", false);
		}
		return maps;
	}

	/**
	 * 跳转路由集群导出
	 * @param baseInfo
	 * @param response
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/export")
	@SystemLog(operCode = "jumpRoute.export", description = "导出")
	public void export(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotEmpty(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}
			JumpRouteConfig config = JSONObject.parseObject(baseInfo, JumpRouteConfig.class);
			jumpRouteSevice.export(response, config);
		} catch (Exception e){
			logger.error("跳转路由集群导出异常", e);
		}
	}

}
