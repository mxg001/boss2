package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.job.OutAccountServiceJob;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AcqOrgService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.RedisService;

import static cn.eeepay.framework.util.ClientInterface.baseClient;

@Controller
@RequestMapping(value = "/acqOrgAction")
public class AcqOrgAction {
	private static final Logger log = LoggerFactory.getLogger(AcqOrgAction.class);

	@Resource
	private AcqOrgService acqOrgService;

	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private QuartzManager quartzManager;
	
	@Autowired
	private RedisService redisService;

	/**
	 * 收单机构初始化和模糊查询
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page") Page<AcqOrg> page, @RequestParam("info") String param)
			throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AcqOrg ao = JSON.parseObject(param, AcqOrg.class);
			acqOrgService.selectAllInfo(page, ao);
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("收单机构初始化和模糊查询报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 下拉框
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectBoxAllInfo")
	@ResponseBody
	public Object selectBoxAllInfo() {
		List<AcqOrg> list = null;
		try {
			list = acqOrgService.selectBoxAllInfo();
		} catch (Exception e) {
			log.error("下拉框报错！！", e);
		}
		return list;
	}

	/**
	 * 获取所有直清收单机构
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllZqOrg")
	@ResponseBody
	public Object selectAllZqOrg(){
		List<AcqOrg> list = null;
		try {
			list = acqOrgService.selectAllZqOrg();
		} catch (Exception e) {
			log.error("下拉框报错！！", e);
		}
		return list;
	}

	/**
	 * 修改收单机构状态
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAcqStatus")
	@ResponseBody
	@SystemLog(description = "T1开关状态",operCode="acqOrq.switch")
	public Object updateAcqStatus(@RequestParam("info") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			AcqOrg ao = new AcqOrg();
			ao.setId(json.getInteger("id"));
			ao.setAcqStatus(json.getInteger("acqStatus"));
			ao.setAcqCloseTips(json.getString("closeTips"));
			int i = acqOrgService.updateStatusByid(ao);
			if (ao.getAcqStatus() == 1) {
				jsonMap.put("msg", "开通成功~~~~");
			} else {
				jsonMap.put("msg", "关闭成功~~~~");
			}
			if (i > 0) {
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("修改收单机构状态报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	/**
	 * 交易转集群开关
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatechannelStatus")
	@ResponseBody
	public Object updatechannelStatus(@RequestParam("info") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			AcqOrg ao = new AcqOrg();
			ao.setId(json.getInteger("id"));
			ao.setChannelStatus(json.getInteger("channelStatus"));
			int i = acqOrgService.updateChannelStatusByid(ao);
			if (ao.getChannelStatus() == 1) {
				jsonMap.put("msg", "开通交易转集群成功~~~~");
			} else {
				jsonMap.put("msg", "关闭交易转集群成功~~~~");
			}
			if (i > 0) {
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("修改交易转集群状态报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 收单机构详情和修改详情
	 * 
	 * @param ids
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectByParam")
	@ResponseBody
	public Object selectByParam(@RequestParam("ids") String ids) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(ids, Integer.class);
			System.out.println(id+"========");
			AcqOrg aog = acqOrgService.selectByPrimaryKey(id);
			Object accountData = acqOrgService.selectAccountDate();
			jsonMap.put("accountData", accountData);
			if (aog == null) {
				jsonMap.put("msg", "查询失败~~~~~");
				jsonMap.put("bols", false);
			} else {
				jsonMap.put("result", aog);
				jsonMap.put("bols", true);
			}
		} catch (Exception e) {
			log.error("收单机构详情报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 调用账户接口，返回分润结算账户信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectAccountDate")
	@ResponseBody
	public Map<String, Object> selectAccountDate() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Object accountData = acqOrgService.selectAccountDate();
			jsonMap.put("accountData", accountData);
		} catch (Exception e) {
			log.error("收单机构详情报错!!!", e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 收单机构修改
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAcqOrgInfo")
	@ResponseBody
	@SystemLog(description = "收单机构修改",operCode="acqOrg.update")
	public Object updateAcqOrgInfo(@RequestParam("info") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AcqOrg ao = JSON.parseObject(param, AcqOrg.class);
			AcqOrg oldAcqOrg = acqOrgService.selectByPrimaryKey(ao.getId());
			int i = acqOrgService.updateByPrimaryKey(ao);
			List<String> keyList = new ArrayList<>();
			keyList.add("acqOrgId:"+oldAcqOrg.getId());
			redisService.delete(keyList);
			if (i > 0) {
				jsonMap.put("msg", "修改成功~~~~~");
				jsonMap.put("bols", true);
				//如果时间有改动，才会修改job的出发时间，但是不start
				AutoJobDTO job = new AutoJobDTO();
				job.setJob_id(ao.getId().toString());
				// 会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
				job.setJob_name(ao.getAcqEnname());
				job.setJob_group("ACQ_ORG");
				String time = ao.getDayAlteredTime(); // time eg: 00:05
				time = time+":00";//前端传来的是 00:05，只有时分，但是数据库保存的是时分秒
				String oldTime = oldAcqOrg.getDayAlteredTime();
				if (StringUtils.isNotBlank(time)&&!time.equals(oldTime)) {
					String[] timeStr = time.split(":");
					String conStr = "0 " + timeStr[1] + " " + timeStr[0] + " * * ?";
					job.setJob_time(conStr);
//					quartzManager.addJob(job, OutAccountServiceJob.class);
//					quartzManager.startJobs();
					quartzManager.addJob(quartzManager.bulidCronTirgger(job.getJob_name(),
							job.getJob_group(),conStr),OutAccountServiceJob.class);
					System.out.println("-----------定时任务：" + ao.getAcqEnname() + " 修改------------");
				}

			} else {
				jsonMap.put("msg", "修改失败~~~~~");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("报错!!!", e);
			jsonMap.put("msg", "收单机构修改报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 初始化收单机构白名单
	 * 
	 * @param page
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectAllWlInfo")
	@ResponseBody
	public Object selectAllWlInfo(@RequestParam("ids") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int acqId = JSON.parseObject(param, Integer.class);
			List<AcpWhitelist> alist = acqOrgService.selectAllWlInfo(acqId);
			if (alist.size() < 1) {
				jsonMap.put("msg", "没查到结果~~~~~");
				jsonMap.put("bols", false);
			}
			jsonMap.put("result", alist);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("初始化收单机构白名单报错!!!", e);
			jsonMap.put("msg", "初始化收单机构白名单报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 收单机构白名单删除
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/deleteWlInfo")
	@ResponseBody
	@SystemLog(description = "收单机构白名单删除",operCode="acqOrg.deleteWhite")
	public Object deleteWlInfo(@RequestParam("ids") String param) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int id = JSON.parseObject(param, Integer.class);
			int i = acqOrgService.deleteByWlid(id);
			if (i > 0) {
				jsonMap.put("msg", "删除成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("msg", "删除失败");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("收单机构白名单删除报错~~~~~", e);
			jsonMap.put("msg", "收单机构白名单删除报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 收单机构白名单新增
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/addWlInfo")
	@ResponseBody
	@SystemLog(description = "收单机构白名单新增",operCode="acqOrg.insertWhite")
	public Object addWlInfo(@RequestParam("info") String param) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			AcpWhitelist aw = JSON.parseObject(param, AcpWhitelist.class);
			if (merchantInfoService.selectByMerNo(aw.getMerchantNo()) == null) {
				jsonMap.put("msg", "请输入合法的商户编号信息");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			AcpWhitelist aw1 = acqOrgService.selectWlInfo(aw);
			if (aw1 != null) {
				jsonMap.put("msg", "请输入合法的商户编号信息");
				jsonMap.put("bols", false);
				return jsonMap;
			}
			aw.setCreatePerson(principal.getId().toString());
			int i = acqOrgService.insertWl(aw);
			if (i > 0) {
				jsonMap.put("msg", "新增成功");
				jsonMap.put("bols", true);
			} else {
				jsonMap.put("msg", "新增失败");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("收单机构白名单新增报错~~~~~", e);
			jsonMap.put("msg", "收单机构白名单新增报错~~~~~");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 收单机构新增 by sober
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addAcqOrg")
	@ResponseBody
	@SystemLog(description = "收单机构新增",operCode="acqOrg.insert")
	public Object addAgentFunctionManager(@RequestBody String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			AcqOrg acqOrg = JSON.parseObject(param, AcqOrg.class);
			System.out.println(param);
			acqOrg.setAcqStatus(1);//收单机构状态
			acqOrg.setCreatePerson("admin");
//			System.out.println(acqOrg+"========"+acqOrg.getAcqName());
			int i = 0;
			i = acqOrgService.addAcqOrg(acqOrg);
			if (i > 0) {
				jsonMap.put("msg", "添加成功");
				jsonMap.put("bols", true);
				AcqOrg acqOrgReasult=acqOrgService.selectInfoByName(acqOrg.getAcqName());
				invokeCreateAcc(acqOrgReasult.getId()+"");
			} else {
				jsonMap.put("msg", "添加失败");
				jsonMap.put("bols", false);
			}
		} catch (Exception e) {
			log.error("收单机构新增报错~~~~~", e);
			e.printStackTrace();
			jsonMap.put("msg", "系统异常");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	/**
	 * 存量代理商欢乐账户开设
	 * by ivan
	 * @param userId
	 * @return
	 */
	public  String invokeCreateAcc(String userId) {
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","Acq");
		claims.put("userId",userId);
		claims.put("subjectNo","122103");
		return baseClient(Constants.HAPPY_SEND_ACCOUT_HOST, claims);
	}


}
