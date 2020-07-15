package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.InvitePrizesDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.InvitePrizesConfig;
import cn.eeepay.framework.model.InvitePrizesMerchantInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.InvitePrizesService;
import cn.eeepay.framework.util.ClientInterface;

@Service("invitePrizesService")
@Transactional
public class InvitePrizesServiceImpl implements InvitePrizesService {

	private Logger log = LoggerFactory.getLogger(InvitePrizesServiceImpl.class);

	@Resource
	private SysDictDao sysDictDao;

	@Resource
	private InvitePrizesDao invitePrizesDao;

	@Resource
	private AgentInfoDao agentInfoDao;

	@Override
	public Map<String, Object> getInfo() {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "初始化配置失败");
		Map<String, Object> baseInfo = new HashMap<>();
		String invitePrizesSwitch = sysDictDao.getValueByKey("INVITE_PRIZES_SWITCH");
		String invitePrizesAmount = sysDictDao.getValueByKey("INVITE_PRIZES_AMOUNT");
		String invitePrizesPushModel = sysDictDao.getValueByKey("INVITE_PRIZES_PUSH_MODEL");
		baseInfo.put("invitePrizesSwitch", invitePrizesSwitch);
		baseInfo.put("invitePrizesAmount", invitePrizesAmount);
		baseInfo.put("invitePrizesPushModel", invitePrizesPushModel);
		msg.put("baseInfo", baseInfo);
		msg.put("status", true);
		return msg;
	}

	@Override
	public List<InvitePrizesConfig> getAgentListByParam(InvitePrizesConfig baseInfo, Page<InvitePrizesConfig> page) {
		return invitePrizesDao.getAgentListByParam(baseInfo, page);
	}

	@Override
	public Map<String, Object> updateInfo(Map<String, String> baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		String invitePrizesSwitch = baseInfo.get("invitePrizesSwitch");
		if ("true".equals(invitePrizesSwitch) || "1".equals(invitePrizesSwitch)) {
			invitePrizesSwitch = "1";
		} else {
			invitePrizesSwitch = "0";
		}
		sysDictDao.updateValueByKey(invitePrizesSwitch, "INVITE_PRIZES_SWITCH");
		sysDictDao.updateValueByKey(baseInfo.get("invitePrizesAmount"), "INVITE_PRIZES_AMOUNT");
		sysDictDao.updateValueByKey(baseInfo.get("invitePrizesPushModel"), "INVITE_PRIZES_PUSH_MODEL");
		msg.put("status", true);
		msg.put("msg", "操作成功");
		return msg;
	}

	@Override
	public Map<String, Object> updateAgentActivityDate(InvitePrizesConfig baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		// 判断活动日期是否符合条件
		String message = checkActivityDate(baseInfo);
		if (!"".equals(message)) {
			msg.put("msg", message);
			return msg;
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		baseInfo.setOperator(String.valueOf(principal.getId()));
		int num = invitePrizesDao.updateAgentActivityDate(baseInfo);
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}


	@Override
	public Map<String, Object> insertAgent(InvitePrizesConfig baseInfo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		// 判断活动日期是否符合条件
		String message = checkActivityDate(baseInfo);
		if (!"".equals(message)) {
			msg.put("msg", message);
			return msg;
		}
		// 检查代理商是否是一级代理商
		AgentInfo agentInfo = agentInfoDao.selectAgent(baseInfo.getAgentNo(), 1);
		if (agentInfo == null) {
			msg.put("msg", "请输入正确的一级代理商");
			return msg;
		}
		baseInfo.setActivityAction("4");// 活动动作编号，4 邀请有奖
		// 判断代理商是否已配置邀请有奖
		int checkNum = invitePrizesDao.existsAgentNo(baseInfo);
		if (checkNum > 0) {
			msg.put("msg", "该代理商已配置邀请有奖");
			return msg;
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		baseInfo.setOperator(String.valueOf(principal.getId()));
		int num = invitePrizesDao.insertAgent(baseInfo);
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	@Override
	public Map<String, Object> deleteBatch(List<String> agentNoList) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		int num = invitePrizesDao.deleteBatch(agentNoList);
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	/**
	 * 判断活动日期是否符合条件
	 * 1.起始日期不能大于结束日期
	 * 2.活动日期要大于当前日期
	 * 
	 * @author tans
	 * @date 2017年8月20日 上午9:44:12
	 * @param baseInfo
	 * @return
	 */
	public String checkActivityDate(InvitePrizesConfig baseInfo) {
		String message = "";
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			currentDate = sdf.parse(sdf.format(currentDate));
		} catch (Exception e){
			log.error(e.toString());
		}

		if (baseInfo.getStartDate().before(currentDate)) {
			message = "活动起始日期要大于等于当前日期";
		} else if (baseInfo.getEndDate().before(baseInfo.getStartDate())) {
			message = "活动结束日期要大于等于活动起始日期";
		}
		return message;
	}

	@Override
	public AgentInfo getAgent(String agentNo, Integer agentLevel) {
		return agentInfoDao.selectAgent(agentNo, agentLevel);
	}

	/**
	 * 邀请有奖查询
	 */
	public List<InvitePrizesMerchantInfo> selectInvitePrizesByParam(Page<InvitePrizesMerchantInfo> page,
			InvitePrizesMerchantInfo info) {
		return invitePrizesDao.selectInvitePrizesByParam(page, info);
	}

	/**
	 * 邀请有奖导出
	 * mys,20171017
	 */
	public List<InvitePrizesMerchantInfo> exportInvitePrizesByParam(InvitePrizesMerchantInfo info) {
		return invitePrizesDao.exportInvitePrizesByParam(info);
	}

	@Override
	public Map<String, Object> insertBatchAgent(MultipartFile file, Map<String, Object> msg)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String operator = String.valueOf(principal.getId());
		String activityAction = "4";// 活动编号, 4 邀请有奖, 来源coupon_activity_info.activetiy_code
		List<InvitePrizesConfig> invitePrizesConfigList = new ArrayList<>();
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();
		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String agentNo = getCellValue(row.getCell(0));
			String startDate = getCellValue(row.getCell(1));
			String endDate = getCellValue(row.getCell(2));
			if (StringUtils.isBlank(agentNo) || StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
				msg.put("msg", "第" + i + "行的参数不能为空");
				return msg;
			}
			// 校验活动日期格式
			// /^(\d{4})\/(0\d{1}|1[0-2])\/(0\d{1}|[12]\d{1}|3[01])$/
			String patternStr = "^(\\d{4})/(0?\\d{1}|1[0-2])/(0?\\d{1}|[12]\\d{1}|3[01])$";
			Pattern p = Pattern.compile(patternStr);
			if (!p.matcher(startDate).matches()) {
				msg.put("msg", "第" + i + "行,请输入正确的活动起始日期，例如：2017/08/21");
				return msg;
			}
			if (!p.matcher(endDate).matches()) {
				msg.put("msg", "第" + i + "行,请输入正确的活动结束日期，例如：2017/08/21");
				return msg;
			}

			InvitePrizesConfig invitePrizesConfig = new InvitePrizesConfig();
			invitePrizesConfig.setStartDate(sdf.parse(startDate));
			invitePrizesConfig.setEndDate(sdf.parse(endDate));
			// 校验活动日期是否符合条件
			String message = checkActivityDate(invitePrizesConfig);
			if (!"".equals(message)) {
				msg.put("msg","第" + i + "行," + message);
				return msg;
			}
			invitePrizesConfig.setAgentNo(agentNo);
			invitePrizesConfig.setOperator(operator);
			invitePrizesConfig.setActivityAction(activityAction);
			// 检查代理商是否是一级代理商
			AgentInfo agentInfo = agentInfoDao.selectAgent(agentNo, 1);
			if (agentInfo == null) {
				msg.put("msg", "第" + i + "行,请输入正确的一级代理商");
				return msg;
			}
			// 判断代理商是否已配置邀请有奖
			int checkNum = invitePrizesDao.existsAgentNo(invitePrizesConfig);
			if (checkNum > 0) {
				msg.put("msg", "第" + i + "行,该代理商已配置邀请有奖");
				return msg;
			}
			invitePrizesConfigList.add(invitePrizesConfig);
		}
		List<InvitePrizesConfig> threeHundredList = new ArrayList<>();
		for (int i = 0; i < invitePrizesConfigList.size(); i++) {
			threeHundredList.add(invitePrizesConfigList.get(i));
			if (i % 300 == 0) {
				invitePrizesDao.insertAgentBatch(threeHundredList);
				threeHundredList.clear();
			}
		}
		if(threeHundredList.size() > 0){
			invitePrizesDao.insertAgentBatch(threeHundredList);
		}
		msg.put("status", true);
		msg.put("msg", "操作成功");
		return msg;
	}

	/**
	 * 邀请有奖入账
	 */
	@Override
	public Map<String, Object> updateRecordAccount(Integer id, Map<String, Object> msg) {
		InvitePrizesMerchantInfo info = invitePrizesDao.queryInvitePrizesMerchantInfo(id);
		if (info == null) {
			msg.put("msg", "找不到对应的记录");
			return msg;
		}
		if ("1".equals(info.getAccountStatus())) {
			msg.put("msg", "记录编号" + id + "已入账，不能重复入账");
			return msg;
		}
		if (info.getPrizesType() == null) {
			msg.put("msg", "记录编号" + id + "没有类型，数据异常");
			return msg;
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String returnMsg = ClientInterface.invitePrizseRecord(info);
		log.info("邀请入账，返回结果，returnMsg:{}", returnMsg);
		Map<String, Object> result = JSON.parseObject(returnMsg);
		if (result != null && (Boolean) result.get("status")) {
			info.setAccountStatus("1");
		} else {
			info.setAccountStatus("2");
		}
		info.setAccountTime(new Date());
		info.setOperator(String.valueOf(principal.getId()));
		int num = invitePrizesDao.updateAccountStatus(info);
		if (num > 0) {
			msg.put("status", true);
			msg.put("msg", "操作成功");
		}
		return msg;
	}

	public Map<String, String> countInvitePrizesMerchant(InvitePrizesMerchantInfo info) {
		return invitePrizesDao.countInvitePrizesMerchant(info);
	}

	@Override
	public InvitePrizesMerchantInfo queryInvitePrizesMerchantInfo(int id) {
		return invitePrizesDao.queryInvitePrizesMerchantInfo(id);
	}

	public String getCellValue(Cell cell) {
		System.out.println(cell.toString());
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return cell.getStringCellValue();
		}
		return null;
	}
}
