package cn.eeepay.boss.action.credit;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillDetail;
import cn.eeepay.framework.model.CmBillInfo;
import cn.eeepay.framework.service.CmBillService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/cmBill")
public class CmBillAction {
	private static final Logger log = LoggerFactory.getLogger(CmBillAction.class);

	@Resource
	public CmBillService cmBillService;

	/**
	 * 账单列表查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@RequestMapping(value = "/selectBillInfo")
	@ResponseBody
	public Map<String, Object> selectBillInfo(@RequestBody CmBillInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmBillInfo> page = new Page<>(pageNo, pageSize);
			cmBillService.selectBillInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-账单列表查询失败", e);
		}
		return msg;
	}

	/**
	 * 账单明细查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@RequestMapping(value = "/selectBillDetail")
	@ResponseBody
	public Map<String, Object> selectBillDetail(@RequestBody CmBillDetail info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (info.getRefBillId() == null) {
				msg.put("status", false);
				msg.put("msg", "查询失败，账单id为空");
				return msg;
			}
			Page<CmBillDetail> page = new Page<>(pageNo, pageSize);
			cmBillService.selectBillDetail(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-账单明细查询失败", e);
		}
		return msg;
	}

	/**
	 * 查询评测报告
	 * @author	mays
	 * @date	2018年4月24日
	 */
	@RequestMapping(value = "/queryReviewsReport")
	@ResponseBody
	public Map<String, Object> queryReviewsReport(String billId) {
		Map<String, Object> msg = new HashMap<>();
		try {
			cmBillService.queryReviewsReport(msg, billId);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-评测报告查询失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家-账单清单-导出
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/exportBillInfo")
	@ResponseBody
	public void exportBillInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmBillInfo bean = JSON.parseObject(info, CmBillInfo.class);
		List<CmBillInfo> list = cmBillService.exportBillInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家账单信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("userNo", null);
			maps.put("cardNo", null);
			maps.put("bankName", null);
			maps.put("userName", null);
			maps.put("repayment", null);
			maps.put("statementDate", null);
			maps.put("repaymentDate", null);
			maps.put("createTime", null);
			maps.put("method", null);
			maps.put("reviewTime", null);
			maps.put("billHealth", null);
			maps.put("withrawScore", null);
			data.add(maps);
		} else {
			for (CmBillInfo i : list) {
				String method = "";
				if (i.getMethod() == null) {
				} else if (i.getMethod() == 1) {
					method = "邮箱导入";
				} else if (i.getMethod() == 2) {
					method = "手工导入";
				} else if (i.getMethod() == 3) {
					method = "网银导入";
				} else {
					method = i.getMethod().toString();
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == i.getId() ? "" : i.getId().toString());
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("cardNo", null == i.getCardNo() ? "" : i.getCardNo());
				maps.put("bankName", null == i.getBankName() ? "" : i.getBankName());
				maps.put("userName", null == i.getUserName() ? "" : i.getUserName());
				maps.put("repayment", null == i.getRepayment() ? "" : i.getRepayment().toString());
				maps.put("statementDate", null == i.getStatementDate() ? "" : "每月" + i.getStatementDate() + "日");
				maps.put("repaymentDate", null == i.getRepaymentDate() ? "" : "每月" + i.getRepaymentDate() + "日");
				maps.put("createTime", null == i.getCreateTime() ? "" : sdf1.format(i.getCreateTime()));
				maps.put("method", method);
				maps.put("reviewTime", null == i.getReviewTime() ? "" : sdf1.format(i.getReviewTime()));
				maps.put("billHealth", null == i.getBillHealth() || "-1".equals(i.getBillHealth()) ? "" : i.getBillHealth());
				maps.put("withrawScore", null == i.getWithrawScore() ? "" : i.getWithrawScore());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { "id", "userNo", "cardNo", "bankName", "userName", "repayment",
				"statementDate", "repaymentDate", "createTime", "method", "reviewTime", "billHealth", "withrawScore" };
		String[] colsName = { "账单ID", "用户ID", "银行卡号", "银行名称", "姓名", "账单金额",
				"账单日", "还款日", "账单导入时间", "账单类型", "评测时间", "用卡健康度", "提额指数" };
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}


	/**
	 * 信用卡管家-账单明细-导出
	 * @author	yyao
	 * @date	2018年6月13日
	 */
	@RequestMapping(value = "/exportBillDetailInfo")
	@ResponseBody
	public void exportBillDetailInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmBillDetail bean = JSON.parseObject(info, CmBillDetail.class);
		List<CmBillDetail> list = cmBillService.exportBillDetailInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家账单明细" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("refBillId", null);
			maps.put("userNo", null);
			maps.put("cardNo", null);
			maps.put("transDesc", null);
			maps.put("transDate", null);
			maps.put("transType", null);
			maps.put("transAmt", null);
			maps.put("transArea", null);
			data.add(maps);
		} else {
			for (CmBillDetail i : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == i.getId() ? "" : i.getId().toString());
				maps.put("refBillId", null == i.getRefBillId() ? "" : i.getRefBillId().toString());
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("cardNo", null == i.getCardNo() ? "" : i.getCardNo());
				maps.put("transDesc", null == i.getTransDesc() ? "" : i.getTransDesc());
				maps.put("transDate", null == i.getTransDate() ? "" : sdf1.format(i.getTransDate()));
				maps.put("transType", null == i.getTransType() ? "" : i.getTransType());
				maps.put("transAmt", null == i.getTransAmt() ? "" : i.getTransAmt().toString());
				maps.put("transArea", null == i.getTransArea() ? "" : i.getTransArea());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = { "id", "refBillId", "userNo", "cardNo", "transDesc", "transDate",
				"transType", "transAmt", "transArea" };
		String[] colsName = { "账单明细ID", "账单ID", "用户ID", "银行卡号", "交易说明", "交易时间",
				"交易类型", "交易金额", "交易地" };
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
