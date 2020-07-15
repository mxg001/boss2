package cn.eeepay.boss.action;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.service.CouponImportService;
import cn.eeepay.framework.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardAndReward;
import cn.eeepay.framework.model.CmBillInfo;
import cn.eeepay.framework.model.CouponActivityInfo;
import cn.eeepay.framework.service.CardAndRewardManageService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/cardAndReward")
public class CardAndRewardAction {

	private static final Logger log = LoggerFactory.getLogger(CardAndRewardAction.class);

	@Resource
	public CardAndRewardManageService cardAndRewardManageService;

	@Resource
	public CouponImportService couponImportService;

	/**
	 * 查询用户信息
	 */
	@RequestMapping(value = "/selectUserInfo")
	@ResponseBody
	public Map<String, Object> selectUserInfo(@RequestBody CardAndReward info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CardAndReward> page = new Page<>(pageNo, pageSize);
			cardAndRewardManageService.selectUserInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("办卡送奖金查询失败", e);
		}
		return msg;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "getSendTypeList")
	@ResponseBody
	public Object getListByKey(@Param("type") String type) {
		Map<String, Object> maps = new HashMap();
		try {
			cardAndRewardManageService.selectByKey(type,maps);
		} catch (Exception e) {
			log.error("查询失败!", e);
		}
		return maps;
	}

	// 批量赠送
	@RequestMapping(value = "/allSend")
	@ResponseBody
	@SystemLog(description = "批量赠送", operCode = "cardAndReward.allSend")
	public Object allSend(@RequestParam("baseInfo") String param, @RequestParam("info") String info,
			@RequestParam("type") String type) throws Exception {
		Map<String, Object> maps = null;
		try {
			List<String> ids = JSONObject.parseArray(param, String.class);

			CardAndReward cardAndReward = JSON.parseObject(info, CardAndReward.class);

			maps = cardAndRewardManageService.allSend(ids, cardAndReward, type);

		} catch (Exception e) {
			log.error("赠送商户异常", e);
			maps.put("bols", false);
			maps.put("msg", "赠送商户异常");
		}
		return maps;
	}

	@RequestMapping(value = "/selectCardLoanHeartenLog")
	@ResponseBody
	public Map<String, Object> selectUserDetailByUserNo(@RequestParam("id") String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<CardAndReward> list = cardAndRewardManageService.selectCardLoanHeartenLogById(id);
			msg.put("status", true);
			msg.put("cardList", list);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("查询赠送记录失败", e);
		}
		return msg;
	}

	@RequestMapping(value = "/exportUserList")
	@ResponseBody
	public void exportUserList(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CardAndReward bean = JSON.parseObject(info, CardAndReward.class);

		List<CardAndReward> list = cardAndRewardManageService.exportUserList(bean);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		String fileName = "";
		// `order_type` int(255) NOT NULL COMMENT '订单类型 2信用卡办理 6 贷款',
		if (bean.getOrderType() == 2) {
			fileName = "办卡送鼓励金用户列表" + sdf.format(new Date()) + ".xlsx";
		} else {
			fileName = "贷款送鼓励金用户列表" + sdf.format(new Date()) + ".xlsx";
		}
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("username", null);
			maps.put("phone", null);
			maps.put("orgName", null);
			maps.put("mechName", null);
			maps.put("orderTypeName", null);
			maps.put("orderNo", null);
			maps.put("status", null);
			maps.put("transAmount", null);
			maps.put("transTime", null);
			maps.put("givenChannelName", null);
			maps.put("givenType", null);
			maps.put("couponAmount", null);
			maps.put("effectiveDays", null);
			maps.put("givenStatus", null);
			maps.put("operUsername", null);
			maps.put("updateTime", null);
			maps.put("successTime", null);
			maps.put("successTime", null);
			maps.put("operTime", null);
			data.add(maps);
		} else {
			for (CardAndReward i : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", i.getId()+"");
				maps.put("username", null==i.getUsername()?"":i.getUsername().toString());
				maps.put("phone", null==i.getPhone()?"":i.getPhone().toString());
				maps.put("orgName", null==i.getOrgName()?"":i.getOrgName().toString());
				maps.put("mechName", null==i.getMechName()?"":i.getMechName().toString());
				
				maps.put("orderType", null==i.getOrderTypeName()?"":i.getOrderTypeName().toString());
				maps.put("orderNo", null==i.getOrderNo()?"":i.getOrderNo().toString());
				maps.put("statusName", null==i.getStatusName()?"":i.getStatusName().toString());
				maps.put("transAmount", null==i.getTransAmount()?"":i.getTransAmount().toString());
				maps.put("givenChannelName", null==i.getGivenChannelName()?"":i.getGivenChannelName().toString());
				
				maps.put("givenType", null==i.getGivenType()?"":i.getGivenType().toString());
				maps.put("couponAmount", null==i.getCouponAmount()?"":i.getCouponAmount().toString());
				maps.put("effectiveDays", i.getEffectiveDays()+"");
				maps.put("givenStatus", null==i.getGivenStatusName()?"":i.getGivenStatusName().toString());
				maps.put("operUsername", null==i.getOperUsername()?"":i.getOperUsername().toString());
				
				maps.put("transTime", null==i.getTransTime()?"":DateUtil.getLongFormatDate(i.getTransTime()));
				maps.put("updateTime",  null==i.getUpdateTime()?"":DateUtil.getLongFormatDate(i.getUpdateTime()));
				maps.put("successTime",null==i.getSuccessTime()?"":DateUtil.getLongFormatDate(i.getSuccessTime()));
				maps.put("successTime2", null==i.getSuccessTime()?"":DateUtil.getLongFormatDate(i.getSuccessTime()));
				maps.put("operTime", null==i.getOperTime()?"":DateUtil.getLongFormatDate(i.getOperTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { 
				"id", "username", "phone", "orgName", "mechName", 
				"orderType", "orderNo","statusName", "transAmount", "transTime", 
				"givenChannelName", "givenType", "couponAmount", "effectiveDays", "givenStatus", 
				"operUsername", "updateTime", "successTime", "successTime2", "operTime", 
				};
		String[] colsName = {
				"编号", "商户名称", "手机号码", "机构名称", "组织名称", 
				"订单类型", "订单号", "订单状态", "交易金额", "交易时间",
				"赠送渠道","赠送类型", "赠送面值", "有效期", "赠送状态", "操作人", 
				"操作时间", "赠送成功时间", "鼓励金生效时间", "鼓励金到期时间"
				};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}
	
	@RequestMapping(value = "/exportSendList")
	@ResponseBody
	public void exportSendList(@RequestParam("id") String id, HttpServletResponse response) throws Exception {

		List<CardAndReward> list = cardAndRewardManageService.exportCardLoanHeartenLogById(id);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		String fileName = "";
		// `order_type` int(255) NOT NULL COMMENT '订单类型 2信用卡办理 6 贷款',
		fileName = "赠送记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("username", null);
			maps.put("phone", null);
			maps.put("orgName", null);
			maps.put("mechName", null);
			maps.put("orderTypeName", null);
			maps.put("orderNo", null);
			maps.put("status", null);
			maps.put("transAmount", null);
			maps.put("transTime", null);
			maps.put("givenChannelName", null);
			maps.put("givenType", null);
			maps.put("couponAmount", null);
			maps.put("effectiveDays", null);
			maps.put("givenStatus", null);
			maps.put("operUsername", null);
			maps.put("updateTime", null);
			maps.put("successTime", null);
			maps.put("successTime", null);
			maps.put("operTime", null);
			data.add(maps);
		} else {
			for (CardAndReward i : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", i.getId()+"");
				maps.put("username", null==i.getUsername()?"":i.getUsername().toString());
				maps.put("phone", null==i.getPhone()?"":i.getPhone().toString());
				maps.put("orgName", null==i.getOrgName()?"":i.getOrgName().toString());
				maps.put("mechName", null==i.getMechName()?"":i.getMechName().toString());
				
				maps.put("orderType", null==i.getOrderTypeName()?"":i.getOrderTypeName().toString());
				maps.put("orderNo", null==i.getOrderNo()?"":i.getOrderNo().toString());
				maps.put("statusName", null==i.getStatusName()?"":i.getStatusName().toString());
				maps.put("transAmount", null==i.getTransAmount()?"":i.getTransAmount().toString());
				maps.put("givenChannelName", null==i.getGivenChannelName()?"":i.getGivenChannelName().toString());
				
				maps.put("givenType", null==i.getGivenType()?"":i.getGivenType().toString());
				maps.put("couponAmount", null==i.getCouponAmount()?"":i.getCouponAmount().toString());
				maps.put("effectiveDays", i.getEffectiveDays()+"");
				maps.put("givenStatus", null==i.getGivenStatusName()?"":i.getGivenStatusName().toString());
				maps.put("operUsername", null==i.getOperUsername()?"":i.getOperUsername().toString());
				
				maps.put("transTime", null==i.getTransTime()?"":DateUtil.getLongFormatDate(i.getTransTime()));
				maps.put("updateTime",  null==i.getUpdateTime()?"":DateUtil.getLongFormatDate(i.getUpdateTime()));
				maps.put("successTime",null==i.getSuccessTime()?"":DateUtil.getLongFormatDate(i.getSuccessTime()));
				maps.put("successTime2", null==i.getSuccessTime()?"":DateUtil.getLongFormatDate(i.getSuccessTime()));
				maps.put("operTime", null==i.getOperTime()?"":DateUtil.getLongFormatDate(i.getOperTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { 
				"id", "username", "phone", "orgName", "mechName", 
				"orderType", "orderNo","statusName", "transAmount", "transTime", 
				"givenChannelName", "givenType", "couponAmount", "effectiveDays", "givenStatus", 
				"operUsername", "updateTime", "successTime", "successTime2", "operTime", 
				};
		String[] colsName = {
				"编号", "商户名称", "手机号码", "机构名称", "组织名称", 
				"订单类型", "订单号", "订单状态", "交易金额", "交易时间",
				"赠送渠道","赠送类型", "赠送面值", "有效期", "赠送状态", "操作人", 
				"操作时间", "赠送成功时间", "鼓励金生效时间", "鼓励金到期时间"
				};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}


	/**
	 * 办卡送鼓励金导入赠送模板下载
	 */
	@RequestMapping("/couponImportCardTemplate")
	public String couponImportCardTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"couponImportTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"办卡送鼓励金导入赠送模板.xlsx");
		return null;
	}
	/**
	 * 贷款送鼓励金导入赠送模板下载
	 */
	@RequestMapping("/couponImportLoanTemplate")
	public String couponImportLoanTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"couponImportTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"贷款送鼓励金导入赠送模板.xlsx");
		return null;
	}

	/**
	 * 批量导入新增券(权限隔离)
	 */
	@RequestMapping(value="/couponImportCard")
	@ResponseBody
	@SystemLog(description = "批量导入办卡送鼓励金新增券", operCode = "cardAndReward.couponImportCard")
	public Map<String, Object> couponImportCard(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}
			msg = couponImportService.couponImportCard(request.getParameter("info"),file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "导入失败!");
			log.error("导入失败!",e);
		}
		return msg;
	}
	/**
	 * 批量导入新增券(权限隔离)
	 */
	@RequestMapping(value="/couponImportLoan")
	@ResponseBody
	@SystemLog(description = "批量导入贷款送鼓励金新增券", operCode = "cardAndReward.couponImportLoan")
	public Map<String, Object> couponImportLoan(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "导入文件格式错误!");
					return msg;
				}
			}
			msg = couponImportService.couponImportCard(request.getParameter("info"),file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "导入失败!");
			log.error("导入失败!",e);
		}
		return msg;
	}
}
