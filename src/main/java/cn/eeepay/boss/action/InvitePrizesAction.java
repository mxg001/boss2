package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.InvitePrizesConfig;
import cn.eeepay.framework.model.InvitePrizesMerchantInfo;
import cn.eeepay.framework.service.InvitePrizesService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 邀请有奖
 * @author tans
 * @date 2017年8月19日 上午10:55:06
 */
@Controller
@RequestMapping(value = "/invitePrizes")
public class InvitePrizesAction {

	private Logger log = LoggerFactory.getLogger(InvitePrizesAction.class);

	@Resource
	private InvitePrizesService invitePrizesService;

	/**
	 * 获取邀请有奖的基本配置
	 * @author tans
	 * @date 2017年8月19日 上午10:59:13
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getInfo")
	@ResponseBody
	public Map<String, Object> getInfo() {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "邀请有奖配置初始化失败");
		try {
			msg = invitePrizesService.getInfo();
		} catch (Exception e) {
			log.error("获取邀请有奖的基本配置失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 保存邀请有奖的基本配置
	 * @author tans
	 * @date 2017年8月19日 下午3:26:02
	 * @return
	 */
	@RequestMapping(value = "/saveInfo")
	@ResponseBody
	@SystemLog(description = "保存邀请有奖", operCode = "invitePrizes.saveInfo")
	public Map<String, Object> saveInfo(@RequestBody Map<String, String> baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		try {
			String invitePrizesAmount = baseInfo.get("invitePrizesAmount");
			Integer amount = Integer.valueOf(invitePrizesAmount);
			if (amount < 0) {
				msg.put("status", false);
				msg.put("msg", "奖励金额不能小于0");
			}
			msg = invitePrizesService.updateInfo(baseInfo);
		} catch (Exception e) {
			log.error("保存邀请有奖的基本配置失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 邀请有奖，代理商配置
	 * @author tans
	 * @date 2017年8月19日 上午11:18:54
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAgentListByParam")
	@ResponseBody
	public Map<String, Object> getAgentListByParam(@RequestBody InvitePrizesConfig baseInfo,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "邀请有奖，代理商配置初始化失败");
		try {
			Page<InvitePrizesConfig> page = new Page<>(pageNo, pageSize);
			baseInfo.setCurrentDate(DateUtil.getNowDateShort());
			invitePrizesService.getAgentListByParam(baseInfo, page);
			msg.put("page", page);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("邀请有奖，代理商配置初始化失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 修改代理商的活动日期
	 * @author tans
	 * @date 2017年8月19日 下午4:38:44
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/updateAgentActivityDate")
	@ResponseBody
	@SystemLog(description = "修改代理商的活动日期", operCode = "invitePrizes.updateAgentActivityDate")
	public Map<String, Object> updateAgentActivityDate(@RequestBody InvitePrizesConfig baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "修改代理商的活动日期失败");
		try {
			msg = invitePrizesService.updateAgentActivityDate(baseInfo);
		} catch (Exception e) {
			log.error("修改代理商的活动日期失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 根据代理商编号和代理商等级查找代理商
	 * @author tans
	 * @date 2017年8月20日 上午11:32:38
	 * @param agentNo
	 * @param agentLevel
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/getAgent")
	@ResponseBody
	public AgentInfo getAgent(String agentNo, Integer agentLevel) {
		AgentInfo agentInfo = new AgentInfo();
		try {
			if (agentLevel == null || agentLevel != 1) {
				return null;
			}
			agentInfo = invitePrizesService.getAgent(agentNo, agentLevel);
		} catch (Exception e) {
			log.error("根据代理商编号和等级获取信息失败");
			log.error(e.toString());
		}
		return agentInfo;
	}

	/**
	 * 新增代理商
	 * @author tans
	 * @date 2017年8月20日 上午9:38:46
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/insertAgent")
	@ResponseBody
	@SystemLog(description = "新增代理商", operCode = "invitePrizes.insertAgent")
	public Map<String, Object> insertAgent(@RequestBody InvitePrizesConfig baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "新增代理商失败");
		try {
			msg = invitePrizesService.insertAgent(baseInfo);
		} catch (Exception e) {
			log.error("新增代理商失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 批量插入代理商
	 * @author tans
	 * @date 2017年8月21日 下午1:54:48
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/insertAgentBatch")
	@ResponseBody
	@SystemLog(description = "批量新增代理商", operCode = "invitePrizes.insertAgentBatch")
	public Map<String, Object> importTerminal(@RequestParam("file") MultipartFile file) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("status", false);
		msg.put("msg", "批量新增代理商失败");
		try {
			if (file.isEmpty()) {
				msg.put("msg", "文件不能为空");
			}
			String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!format.equals(".xls") && !format.equals(".xlsx")) {
				msg.put("msg", "文件格式错误");
				return msg;
			}
			long fileSize = file.getSize();
			if (((fileSize / 1024) / 1024) > 4) {
				msg.put("msg", "上传文件过大,请上传4MB以内的文件");
				return msg;
			}
			msg = invitePrizesService.insertBatchAgent(file, msg);
		} catch (Exception e) {
			log.error("批量新增代理商");
			log.error(e.toString());
			msg.put("status", false);
			msg.put("msg", "批量新增代理商失败");
		}
		return msg;
	}

	@RequestMapping(value = "/deleteBatch")
	@ResponseBody
	@SystemLog(description = "批量删除代理商", operCode = "invitePrizes.deleteBatch")
	public Map<String, Object> deleteBatch(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "批量删除代理商失败");
		try {
			List<String> agentNoList = JSONObject.parseArray(param, String.class);
			if (agentNoList == null || agentNoList.size() < 1) {
				msg.put("msg", "参数非法");
				return msg;
			}
			msg = invitePrizesService.deleteBatch(agentNoList);
		} catch (Exception e) {
			log.error("批量删除代理商失败");
			log.error(e.toString());
		}
		return msg;
	}

	/**
	 * 邀请有奖查询
	 * @author mays
	 * @date 2017年8月19日 上午11:24:34
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectInvitePrizesByParam")
	@ResponseBody
	public Map<String, Object> selectInvitePrizesByParam(@RequestBody InvitePrizesMerchantInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<InvitePrizesMerchantInfo> page = new Page<>(pageNo, pageSize);
			invitePrizesService.selectInvitePrizesByParam(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("邀请有奖查询失败", e);
		}
		return msg;
	}

	/**
	 * 邀请有奖查询统计
	 * @author mays
	 * @date 2017年8月21日 下午2:15:38
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/countInvitePrizesMerchant")
	@ResponseBody
	public Map<String, Object> countInvitePrizesMerchant(@RequestBody InvitePrizesMerchantInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Map<String, String> result = invitePrizesService.countInvitePrizesMerchant(info);
			msg.put("status", true);
			msg.put("result", result);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "统计失败");
			log.error("邀请有奖查询统计失败", e);
		}
		return msg;
	}

	/**
	 * 邀请奖励入账
	 * @author mays
	 * @date 2017年8月21日 下午2:23:58
	 */
	@RequestMapping(value = "/recordAccount")
	@ResponseBody
	@SystemLog(description = "邀请奖励入账", operCode = "invitePrizes.recordAccount")
	public Map<String, Object> recordAccount(@RequestBody Integer id) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "入账失败");
		try {
			if (id == null) {
				msg.put("msg", "参数非法");
				return msg;
			}
			msg = invitePrizesService.updateRecordAccount(id, msg);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "入账失败");
			log.error("邀请奖励入账失败", e);
		}

		return msg;
	}

	/**
	 * 邀请奖励批量入账
	 * @author mays
	 * @date 2017年8月21日 下午2:24:11
	 */
	@RequestMapping(value = "/batchRecordAccount")
	@ResponseBody
	@SystemLog(description = "邀请奖励批量入账", operCode = "invitePrizes.batchRecordAccount")
	public Map<String, Object> batchRecordAccount(@RequestBody List<Integer> ids) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "批量入账失败");
		if (ids == null || ids.size() < 1) {
			msg.put("msg", "参数非法");
			return msg;
		}
		int successTimes = 0;
		int failTimes = 0;
		for (int i = 0; i < ids.size(); i++) {
			try {
				Map<String, Object> itemMsg = invitePrizesService.updateRecordAccount(ids.get(i), msg);
				if (itemMsg != null && (boolean) itemMsg.get("status")) {
					successTimes++;
				} else {
					failTimes++;
				}
			} catch (Exception e) {
				log.error("入账失败", e);
				failTimes++;
			}
		}
		msg.put("status", true);
		msg.put("msg", "批量入账成功,其中成功条数：" + successTimes + "，失败条数：" + failTimes);
		return msg;
	}

	/**
	 * 邀请有奖导出
	 * @author mays
	 * @date 2017年10月17日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportInfo")
	@ResponseBody
	public void exportAllInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception{
		InvitePrizesMerchantInfo bean = JSON.parseObject(info,InvitePrizesMerchantInfo.class);
		List<InvitePrizesMerchantInfo> list = invitePrizesService.exportInvitePrizesByParam(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "邀请有奖记录" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("merchantName", null);
			maps.put("merchantNo", null);
			maps.put("agentName", null);
			maps.put("agentNo", null);
			maps.put("prizesType", null);
			maps.put("prizesObject", null);
			maps.put("prizesAmount", null);
			maps.put("createTime", null);
			maps.put("orderNo", null);
			maps.put("accountStatus", null);
			maps.put("accountTime", null);
			maps.put("realName", null);
			data.add(maps);
		} else {
			for (InvitePrizesMerchantInfo ipmi : list) {
				if ("1".equals(ipmi.getPrizesType())) {
					ipmi.setPrizesType("商户");
				} else if ("2".equals(ipmi.getPrizesType())) {
					ipmi.setPrizesType("代理商");
				}
				if ("0".equals(ipmi.getAccountStatus())) {
					ipmi.setAccountStatus("未入账");
				} else if ("1".equals(ipmi.getAccountStatus())) {
					ipmi.setAccountStatus("已入账");
				} else if ("2".equals(ipmi.getAccountStatus())) {
					ipmi.setAccountStatus("入账失败");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == ipmi.getId() ? "" : ipmi.getId() + "");
				maps.put("merchantName", null == ipmi.getMerchantName() ? "" : ipmi.getMerchantName());
				maps.put("merchantNo", null == ipmi.getMerchantNo() ? "" : ipmi.getMerchantNo());
				maps.put("agentName", null == ipmi.getAgentName() ? "" : ipmi.getAgentName());
				maps.put("agentNo", null == ipmi.getAgentNo() ? "" : ipmi.getAgentNo());
				maps.put("prizesType", null == ipmi.getPrizesType() ? "" : ipmi.getPrizesType());
				maps.put("prizesObject", null == ipmi.getPrizesObject() ? "" : ipmi.getPrizesObject());
				maps.put("prizesAmount", null == ipmi.getPrizesAmount() ? "" : ipmi.getPrizesAmount().toString());
				maps.put("createTime", null == ipmi.getCreateTime() ? "" : sdf1.format(ipmi.getCreateTime()));
				maps.put("orderNo", null == ipmi.getOrderNo() ? "" : ipmi.getOrderNo());
				maps.put("accountStatus", null == ipmi.getAccountStatus() ? "" : ipmi.getAccountStatus());
				maps.put("accountTime", null == ipmi.getAccountTime() ? "" : sdf1.format(ipmi.getAccountTime()));
				maps.put("realName", null == ipmi.getRealName() ? "" : ipmi.getRealName());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","merchantName","merchantNo","agentName","agentNo","prizesType",
				"prizesObject","prizesAmount","createTime","orderNo","accountStatus","accountTime","realName"};
		String[] colsName = new String[]{"编号","邀请商户名称","邀请商户编号","所属代理商名称","所属代理商编号",
				"奖励用户类型","商户/一级代理商编号","奖励金额(元)","创建时间","交易订单","入账状态","入账时间","操作人"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

}
