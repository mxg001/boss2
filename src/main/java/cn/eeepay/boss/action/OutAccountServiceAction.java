package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import cn.eeepay.framework.service.AcqOrgService;
import cn.eeepay.framework.service.OutAccountServiceService;

/**
 * 出款服务action
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping("/outAccountService")
public class OutAccountServiceAction {

	private static final Logger log = LoggerFactory.getLogger(OutAccountServiceAction.class);

	@Resource
	private OutAccountServiceService outAccountServiceService;
	@Resource
	private AcqOrgService acqOrgService;

	@RequestMapping(value = "/saveFunction.do")
	@SystemLog(description = "出款服务设置预警信息",operCode="managerService.setInfo")
	public @ResponseBody Map<String, Object> saveFunction(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			OutAccountServiceFunction function = JSON.parseObject(param, OutAccountServiceFunction.class);
			Integer num = outAccountServiceService.saveOutAccountServiceFunction(function);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "保存成功！");
				msg.put("data", num);
			}
		} catch (Exception e) {
			log.error("保存失败！", e);
			msg.put("status", false);
			msg.put("msg", "保存失败！");
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryFunction.do")
	public @ResponseBody OutAccountServiceFunction queryFunction() {
		OutAccountServiceFunction function = new OutAccountServiceFunction();
		try {
			function = outAccountServiceService.queryOutAccountServiceFunction();
		} catch (Exception e) {
			log.error("查询失败！", e);
		}
		return function;
	}

	@RequestMapping(value = "/addService.do")
	@SystemLog(description = "出款服务增加",operCode="managerService.insert")
	public @ResponseBody Map<String, Object> addService(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			OutAccountService service = jsonObject.getObject("serviceBaseInfo", OutAccountService.class);
			final Integer acqOrgId = service.getAcqOrgId();
			if (acqOrgId == null) {
				msg.put("msg", "收单机构没有填写");
				return msg;
			}
			AcqOrg acqOrg = acqOrgService.selectByPrimaryKey(acqOrgId);
			if(acqOrg == null){
				msg.put("msg", "收单机构填写错误");
				return msg;
			}
			service.setAcqOrgName(acqOrg.getAcqName());
			service.setAcqEnname(acqOrg.getAcqEnname());
			if(StringUtils.isBlank(service.getServiceName())){
				msg.put("msg", "服务名称没有填写");
				return msg;
			}
			if(StringUtils.isBlank(service.getServiceName())){
				msg.put("msg", "服务名称没有填写");
				return msg;
			}
			if(service.getOutAccountMinAmount() == null){
				msg.put("msg", "每笔出款最小限额没有填写");
				return msg;
			}
			if(service.getOutAccountMaxAmount() == null){
				msg.put("msg", "每笔出款最大限额没有填写");
				return msg;
			}
			if(service.getDayOutAccountAmount() == null){
				msg.put("msg", "每日出款限额没有填写");
				return msg;
			}
			if(service.getOutAmountWarning() == null){
				msg.put("msg", "打款预警额度没有填写");
				return msg;
			}
			if(service.getTransformationAmount() == null){
				msg.put("msg", "调整服务预警额度没有填写");
				return msg;
			}
			if(service.getAntoCloseMsg() == null){
				msg.put("msg", "提现自动关闭提示没有填写");
				return msg;
			}
			List<OutAccountServiceRate> serviceRates = JSON.parseArray(jsonObject.getJSONArray("serviceRateInfos").toJSONString(), OutAccountServiceRate.class);
			for(OutAccountServiceRate item : serviceRates){
				if(item == null){
					msg.put("msg", "服务管控费率没有填写");
					return msg;
				}
				if(StringUtils.isBlank(item.getServiceRate())){
					msg.put("msg", "代付服务费率没有填写");
					return msg;
				}
			}
			Integer num = outAccountServiceService.insertOutAccountService(service, serviceRates);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			}
		} catch (Exception e) {
			log.error("添加失败！", e);
			System.out.println(e);
			System.out.println(e.getMessage());
			msg.put("status", false);
			if (e.getMessage() != null) {
				msg.put("msg", e.getMessage());
			} else {
				msg.put("msg", "添加失败！");
			}

		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryService.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<OutAccountService> queryService(@RequestParam("info") String param, @ModelAttribute("page") Page<OutAccountService> page) {
		try {
			Map<String, Object> jsonMap = JSON.parseObject(param);
			outAccountServiceService.queryOutAccountService(jsonMap, page);
		} catch (Exception e) {
			log.error("查询出款服务失败！", e);
		}
		return page;
	}
	
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryServiceNoPage.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryService() {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			List<OutAccountService> list = outAccountServiceService.queryOutAccountServiceNoPage();
			msg.put("result", list);
		} catch (Exception e) {
			log.error("查询出款服务失败！", e);
		}
		return msg;
	}

	@RequestMapping(value = "/updateServiceStatus.do")
	@SystemLog(description = "出款服务状态开关",operCode="managerService.switch")
	public @ResponseBody Map<String, Object> updateServiceStatus(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			Integer num = outAccountServiceService.updateOutAccountServiceStatus(jsonObject.getInteger("id"), jsonObject.getInteger("outAccountStatus"));
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
	@RequestMapping(value = "/getServiceDetail.do")
	public @ResponseBody Map<String, Object> getServiceDetail(@RequestBody String param) {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			res = outAccountServiceService.getOutAccountServiceDetail(jsonObject.getInteger("serviceId"));
		} catch (Exception e) {
			log.error("获取出款服务失败！", e);
		}
		return res;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/queryServiceRateLog.do")
	public @ResponseBody List<OutAccountServiceRateTask> queryServiceRateLog(@RequestBody String param) {
		List<OutAccountServiceRateTask> data = new ArrayList<OutAccountServiceRateTask>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			data = outAccountServiceService.queryOutAccountServiceRateLog(jsonObject.getInteger("serviceRateId"));
		} catch (Exception e) {
			log.error("获取历史服务费率失败！", e);
		}
		return data;
	}

	@RequestMapping(value = "/updateService.do")
	@SystemLog(description = "出款服务修改",operCode="managerService.update")
	public @ResponseBody Map<String, Object> updateService(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			OutAccountService service = JSON.parseObject(param, OutAccountService.class);
			Integer num = outAccountServiceService.updateOutAccountService(service);
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

	@RequestMapping(value = "/addServiceRateTask.do")
	@SystemLog(description = "出款服务修改费率",operCode="managerService.updateRate")
	public @ResponseBody Map<String, Object> addServiceRateTask(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			OutAccountServiceRateTask serviceRateTask = JSON.parseObject(param, OutAccountServiceRateTask.class);
			Integer num = outAccountServiceService.insertOutAccountServiceRateTask(serviceRateTask);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			System.out.println(e);
			System.out.println(e.getMessage());
			msg.put("status", false);
			msg.put("msg", e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "/deleteServiceRateTask.do")
	@SystemLog(description = "出款服务删除费率",operCode="managerService.delete")
	public @ResponseBody Map<String, Object> deleteServiceRateTask(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(param);
			Integer num = outAccountServiceService.deleteOutAccountServiceRateTask(jsonObject.getInteger("id"));
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

	/**
	 * 下拉框
	 * 
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectBoxAllInfo")
	@ResponseBody
	public Object selectBoxAllInfo() {
		List<OutAccountService> list = null;
		try {
			list =outAccountServiceService.queryBoxAllInfo();
		} catch (Exception e) {
			log.error("下拉框报错！！", e);
		}
		return list;
	}

	/**
	 * create by: tans 2018/8/27 11:14
	 * description: 查询代理商提现配置
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/agentWithdraw")
	@ResponseBody
	public Result agentWithdraw(){
		Result result;
		try {
			result = outAccountServiceService.selectAgentWithdraw();
		} catch (Exception e){
			result = ResponseUtil.buildResult(e);
			log.error("查询代理商提现配置异常", e);
		}
		return result;
	}

    /**
     * create by: tans 2018/8/27 11:14
     * description:保存代理商提现配置
     * @return
     */
	@RequestMapping("saveAgentWithdraw")
    @ResponseBody
	@SystemLog(operCode = "money.agentWithdraw", description = "保存代理商提现配置")
    public Result saveAgentWithdraw(@RequestBody Map<String, Object> map){
        Result result;
        try {
            result = outAccountServiceService.saveAgentWithdraw(map);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询代理商提现配置异常", e);
        }
        return result;
    }
}
