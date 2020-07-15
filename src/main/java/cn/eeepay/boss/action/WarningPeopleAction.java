package cn.eeepay.boss.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OutAccountService;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.WarningPeople;
import cn.eeepay.framework.model.WarningSet;
import cn.eeepay.framework.service.OutAccountServiceService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserService;
import cn.eeepay.framework.service.WarningPeopleService;
import cn.eeepay.framework.service.impl.TimingProduceServiceImpl;
import cn.eeepay.framework.util.Constants;

/**
 * Created by Administrator on 2018/1/8/008.
 * 
 * @author liuks 预警人列表
 */
@Controller
@RequestMapping(value = "/warningPeople")
public class WarningPeopleAction {

	private static final Logger log = LoggerFactory.getLogger(WarningPeopleAction.class);

	public static final Integer COUNT = 20;
	@Resource
	private WarningPeopleService warningPeopleService;

	@Resource
	private UserService userService;

	@Resource
	private OutAccountServiceService outAccountServiceService;

	@Resource
	private SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/csWarningPeople")
	@ResponseBody
	public Map<String, Object> getCollectingServicesWarningPeople(@ModelAttribute("page") Page<WarningPeople> page,
			@RequestParam("info") String param) throws Exception {
		// 1-收单机构预警人员
		Map<String, Object> jsonMap = getWarningPeople(page, param, 1);
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/kingWarningPeople")
	@ResponseBody
	public Map<String, Object> getAKingOfServicesWarningPeople(@ModelAttribute("page") Page<WarningPeople> page,
			@RequestParam("info") String param) throws Exception {
		// 出款预警人员 2
		Map<String, Object> jsonMap = getWarningPeople(page, param, 2);
		return jsonMap;
	}
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/taskWarningPeople")
	@ResponseBody
	public Map<String, Object> getTaskWarningPeople(@ModelAttribute("page") Page<WarningPeople> page,
			@RequestParam("info") String param) throws Exception {
		// 定时任务预警人员 3
		Map<String, Object> jsonMap = getWarningPeople(page, param, 3);
		return jsonMap;
	}

	private Map<String, Object> getWarningPeople(Page<WarningPeople> page, String param, int status) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			WarningPeople wp = JSON.parseObject(param, WarningPeople.class);
			wp.setStatus(status);// 1-收单机构预警人员
			warningPeopleService.getWarningPeople(page, wp);
			int total = warningPeopleService.sumWarningPeople(wp);

			jsonMap.put("status", true);
			jsonMap.put("total", total);
			jsonMap.put("remaining", WarningPeopleAction.COUNT - total);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("status", false);
			jsonMap.put("msg", "预警人查询失败!");

		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/taskWarningPeopleEdit")
	@ResponseBody
	public Map<String, Object> getTaskWarningPeopleEdit(@RequestParam("id") int id) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			WarningPeople wp = warningPeopleService.getWarningPeopleById(id);
			List<String> list = null;
			if (wp.getAssignmentTask() != null && !"".equals(wp.getAssignmentTask())) {
				String[] strs = wp.getAssignmentTask().split(",");
				list = Arrays.asList(strs);
			}
			jsonMap.put("list", list);
			jsonMap.put("status", true);
			jsonMap.put("wp", wp);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("status", false);
			jsonMap.put("msg", "获取预警人失败!");
		}
		return jsonMap;
	}

	@RequestMapping(value = "/updateWarningPeopleByAssignmentTask")
	@ResponseBody
	public Map<String, Object> updateWarningPeopleByAssignmentTask(
			@RequestParam("assignmentTask") String assignmentTask, @RequestParam("id") int id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if ("".equals(assignmentTask)) {
				assignmentTask = null;
			}
			int num = warningPeopleService.updateWarningPeopleByAssignmentTask(assignmentTask, id);
			if (num > 0) {
				map.put("status", true);
				map.put("msg", "设置任务成功!");
			} else {
				map.put("status", false);
				map.put("msg", "设置任务失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", false);
			map.put("msg", "系统异常,设置任务失败!");
		}
		return map;
	}

	@RequestMapping(value = "/remove/{id}")
	@ResponseBody
	public Map<String, Object> deleteWarningPeople(@PathVariable("id") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int num = warningPeopleService.deleteWarningPeople(id);
			if (num > 0) {
				map.put("status", true);
				map.put("msg", "删除成功!");
			} else {
				map.put("status", false);
				map.put("msg", "删除失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "系统异常,删除失败!");
		}
		return map;
	}

	@RequestMapping(value = "/synchronous/{status}")
	@ResponseBody
	public Map<String, Object> synchronous(@PathVariable("status") int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<WarningPeople> list = warningPeopleService.getWarningPeopleAll(status);
			map = warningPeopleService.synchronous(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "系统异常,删除失败!");
		}
		return map;
	}

	@RequestMapping(value = "/add/{sta}/{id}")
	@ResponseBody
	public Map<String, Object> addWarningPeople(@PathVariable("id") int id, @PathVariable("sta") int sta) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserInfo us = userService.getUserInfoById(id);
			if (us == null) {
				map.put("status", false);
				map.put("msg", "添加的用户不存在,添加失败!");
				return map;
			}
			WarningPeople oldwp = warningPeopleService.findWarningPeoplebyUserId(us.getId(), sta);
			if (oldwp != null) {
				map.put("status", false);
				map.put("msg", "预警人已存在,添加失败!");
				return map;
			}
			WarningPeople wp = new WarningPeople();
			wp.setStatus(sta);
			int total = warningPeopleService.sumWarningPeople(wp);

			if (total == WarningPeopleAction.COUNT) {
				map.put("status", false);
				map.put("msg", "预警人已满" + WarningPeopleAction.COUNT + "人,添加失败!");
				return map;
			}

			wp.setUserId(us.getId());
			wp.setUserName(us.getUserName());
			wp.setName(us.getRealName());
			wp.setPhone(us.getTelNo());
			int num = warningPeopleService.addWarningPeople(wp);
			if (num > 0) {
				map.put("status", true);
				map.put("msg", "添加成功!");
			} else {
				map.put("status", false);
				map.put("msg", "添加失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "系统异常,添加失败!");
		}
		return map;
	}

	@RequestMapping(value = "/setWarning")
	@SystemLog(description = "修改出款预警阈值",operCode="warningPeople.setWarning")
	@ResponseBody
	public Map<String, Object> setJob(@ModelAttribute("page") Page<WarningPeople> page,
			@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WarningSet ws = JSON.parseObject(param, WarningSet.class);
			ws.setStatus(WarningSet.warningSetTypeOut);
			if (ws.getServiceId() != null) {
				OutAccountService byId = outAccountServiceService.getOutAccountServiceById(ws.getServiceId());
				if (byId != null) {
					int num = warningPeopleService.saveOrUpdateWarningSet(ws);
					if (num > 0) {
						map.put("status", true);
						map.put("msg", "保存或修改成功!");
						return map;
					} else {
						throw new RuntimeException();
					}
				} else {
					map.put("status", false);
					map.put("msg", "查无此服务,请重新确认后查询");
					return map;
				}
			} else {
				map.put("status", false);
				map.put("msg", "查询id不能为空");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "保存或者更新失败!");
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/warningQuery")
	@ResponseBody
	public Map<String, Object> queryByServiceId(@RequestParam("info") String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WarningSet ws = JSON.parseObject(param, WarningSet.class);
			if (ws.getServiceId() == null) {
				map.put("status", false);
				map.put("msg", "查询id不能为空");
				return map;
			}
			ws.setStatus(WarningSet.warningSetTypeOut);
			
			WarningSet wsAll = new WarningSet();
			// 查服务表的
			OutAccountService byId = outAccountServiceService.getOutAccountServiceById(ws.getServiceId());
			if (byId != null) {
				try {
					Integer warningCycle = Integer.valueOf(
							sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING)
									.getSysValue());// 出款结算中预警周期
					Integer warningNumber = Integer.valueOf(sysDictService
							.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENTING)
							.getSysValue());// 出款结算中预警异常笔数
					Integer failuerWarningCycle = Integer.valueOf(sysDictService
							.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE)
							.getSysValue());// 出款结算失败预警周期
					Integer failuerWarningNumber = Integer.valueOf(sysDictService
							.getByKey(TimingProduceServiceImpl.TIMING_PEN_NUMBER_PAYMENT_SERVICE_SETTLEMENT_FAILURE)
							.getSysValue());// 出款结算失败预警异常笔数
					wsAll.setServiceId(byId.getId());
					wsAll.setServiceName(byId.getServiceName());
					wsAll.setWaringCycle(warningCycle);
					wsAll.setFailurWaringCycle(failuerWarningCycle);
					wsAll.setExceptionNumber(warningNumber);
					wsAll.setFailurExceptionNumber(failuerWarningNumber);
					// 新设置变量
					WarningSet warningSet = warningPeopleService.getWarningSet(ws.getServiceId());
					if (warningSet != null) {
						wsAll.setExceptionNumber(warningSet.getExceptionNumber() == null ? warningNumber
								: warningSet.getExceptionNumber());
						wsAll.setFailurExceptionNumber(warningSet.getFailurExceptionNumber() == null
								? failuerWarningNumber : warningSet.getFailurExceptionNumber());
					}
					map.put("status", true);
					map.put("info", wsAll);
					return map;
				} catch (Exception e) {
					e.printStackTrace();
					log.info("服务id查询数据字典配置数据异常!");
					throw e;
				}
			} else {
				map.put("status", false);
				map.put("msg", "查无此服务,请重新确认后查询");
				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "查询服务id出现异常!");
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/kingWarningPeopleById")
	@ResponseBody
	public Map<String, Object> getAKingOfServicesWarningPeopleById(@RequestParam("id") Integer id) throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		try {
			WarningPeople wp = warningPeopleService.getWarningPeopleByIdAndStatus(id,2);
			if (wp!=null) {
				map.put("wp", wp);
				map.put("status", true);
			}else{
				map.put("status", false);
				map.put("msg", "查无此预警人");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "查询预警人信息出现异常!");
		}
		return map;
	}
	
	@RequestMapping(value = "/kingWarningPeopleSelectSids")
	@SystemLog(description = "分配出款预警人员任务配置",operCode="warningPeople.kingWarningPeopleSelectSids")
	@ResponseBody
	public Map<String, Object> selectSids(@RequestParam("arr")String param,@RequestParam("id")Integer id) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			String sidstr = param.substring(1,param.length()-1);
			int sids = warningPeopleService.updateWarningSids(sidstr,id,2);
			if (sids>0) {
				map.put("status", true);
				map.put("msg", "保存成功");
			}else{
				map.put("status", false);
				map.put("msg", "更新出错");
			}
			
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "更新出现异常了");
			e.printStackTrace();
			log.error("报错!!!", e);
		}
		return map;
	}
	
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getCsWarningPeople")
	@ResponseBody
	public WarningPeople getCsWarningPeople(Integer id) {
		return warningPeopleService.getCsWarningPeople(id);
	}

	@RequestMapping(value = "/setSids")
	@SystemLog(description = "设置收单服务任务", operCode = "warningPeople.setSids")
	@ResponseBody
	public Map<String, Object> setSids(String arr, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sids = arr.substring(1, arr.length() - 1);
			int num = warningPeopleService.updateSidsById(sids, id);
			if (num > 0) {
				map.put("status", true);
				map.put("msg", "设置成功!");
			} else {
				map.put("status", false);
				map.put("msg", "设置失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报错!!!", e);
			map.put("status", false);
			map.put("msg", "系统异常,设置失败!");
		}
		return map;
	}
	
}
