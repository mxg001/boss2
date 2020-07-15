package cn.eeepay.boss.action.credit;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.service.CmCardService;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
@RequestMapping(value = "/cmCard")
public class CmCardAction {
	private static final Logger log = LoggerFactory.getLogger(CmCardAction.class);

	@Resource
	public CmCardService cmCardService;

	/**
	 * 卡片列表查询
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@RequestMapping(value = "/selectCardInfo")
	@ResponseBody
	public Map<String, Object> selectCardInfo(@RequestBody CmCardInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmCardInfo> page = new Page<>(pageNo, pageSize);
			cmCardService.selectCardInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-卡片列表查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据id查询卡片信息
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@RequestMapping(value = "/queryCardInfoById")
	@ResponseBody
	public Map<String, Object> queryCardInfoById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			CmCardInfo info = cmCardService.queryCardInfoById(id);
			if (info.getTotalAmount() != null) {
				info.setTotalAmount(info.getTotalAmount().divide(new BigDecimal(100)));
			}
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-卡片详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 修改卡片信息
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@RequestMapping(value = "/updateCardInfo")
	@ResponseBody
	@SystemLog(description = "修改卡片",operCode="creditMgr.updateCard")
	public Map<String, Object> updateCardInfo(@RequestBody CmCardInfo info) {
		Map<String, Object> msg = new HashMap<>();
		try {
			info.setTotalAmount(info.getTotalAmount().multiply(new BigDecimal(100)));//数据库单位是 分
			if (1 == cmCardService.updateCardInfo(info)) {
				msg.put("msg", "修改成功");
				msg.put("status", true);
			} else {
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改失败");
			log.error("信用卡管家-修改卡片信息失败", e);
		}
		return msg;
	}

	@RequestMapping(value = "/exportCmCard")
	@ResponseBody
	public void exportCmCard(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmCardInfo bean = JSON.parseObject(info, CmCardInfo.class);
		List<CmCardInfo> list = cmCardService.exportCmCard(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家卡片信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("userNo", null);
			maps.put("userMobile", null);
			maps.put("orgName", null);
			maps.put("cardNo", null);
			maps.put("bankName", null);
			maps.put("userName", null);
			maps.put("mobileNo", null);
			maps.put("mail", null);
			maps.put("totalAmount", null);
			maps.put("creditScore", null);
			maps.put("statementDate", null);
			maps.put("repaymentDate", null);
			maps.put("remindTime", null);
			maps.put("cardStatus", null);
			maps.put("createTime", null);
			data.add(maps);
		} else {
			for (CmCardInfo i : list) {
				String cardStatus = "";
				if (i.getCardStatus() == 0) {
					cardStatus = "已删除";
				} else if (i.getCardStatus() == 1) {
					cardStatus = "正常";
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == i.getId() ? "" : i.getId() + "");
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("userMobile", null == i.getUserMobile() ? "" : i.getUserMobile());
				maps.put("orgName", null == i.getOrgName() ? "" : i.getOrgName());
				maps.put("cardNo", null == i.getCardNo() ? "" : i.getCardNo());
				maps.put("bankName", null == i.getBankName() ? "" : i.getBankName());
				maps.put("userName", null == i.getUserName() ? "" : i.getUserName());
				maps.put("mobileNo", null == i.getMobileNo() ? "" : i.getMobileNo());
				maps.put("mail", null == i.getMail() ? "" : i.getMail());
				maps.put("totalAmount", null == i.getTotalAmount() ? "" : i.getTotalAmount().toString());
				maps.put("creditScore", null == i.getCreditScore() ? "" : i.getCreditScore());
				maps.put("statementDate", null == i.getStatementDate() ? "" : "每月" + i.getStatementDate() + "日");
				maps.put("repaymentDate", null == i.getRepaymentDate() ? "" : "每月" + i.getRepaymentDate() + "日");
				maps.put("remindTime", null == i.getRemindTime() ? "" : i.getRemindTime() + "天");
				maps.put("cardStatus", cardStatus);
				maps.put("createTime", null == i.getCreateTime() ? "" : sdf1.format(i.getCreateTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"id", "userNo", "userMobile", "orgName", "cardNo", "bankName", "userName", "mobileNo", "mail",
				"totalAmount", "creditScore", "statementDate", "repaymentDate", "remindTime", "cardStatus", "createTime"};
		String[] colsName = {"卡片ID", "用户ID", "登录手机号码", "所属组织", "卡号", "银行名称", "姓名",
				"预留手机号码", "邮箱", "总额度", "积分", "账单日", "还款日", "还款提醒（提前N天）", "状态", "创建时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
