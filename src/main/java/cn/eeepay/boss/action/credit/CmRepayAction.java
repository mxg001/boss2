package cn.eeepay.boss.action.credit;

import java.io.OutputStream;
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

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillInfo;
import cn.eeepay.framework.service.CmRepayService;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
@RequestMapping(value = "/cmRepay")
public class CmRepayAction {
	private static final Logger log = LoggerFactory.getLogger(CmRepayAction.class);

	@Resource
	public CmRepayService cmRepayService;

	/**
	 * 还款查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/selectRepayInfo")
	@ResponseBody
	public Map<String, Object> selectRepayInfo(@RequestBody CmBillInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmBillInfo> page = new Page<>(pageNo, pageSize);
			cmRepayService.selectRepayInfo(page, info);
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-还款查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据id查询还款信息
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/selectRepayInfoById")
	@ResponseBody
	public Map<String, Object> selectRepayInfoById(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg.put("info", cmRepayService.selectRepayInfoById(id,1));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-根据id查询还款信息失败", e);
		}
		return msg;
	}

	/**
	 * 查询敏感信息
	 */
	@RequestMapping(value = "/getDataProcessing")
	@ResponseBody
	public Map<String, Object> getDataProcessing(String id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg.put("info", cmRepayService.selectRepayInfoById(id,0));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询敏感信息失败");
			log.error("查询敏感信息失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家-还款查询-导出
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/exportRepayInfo")
	@ResponseBody
	public void exportRepayInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmBillInfo bean = JSON.parseObject(info, CmBillInfo.class);
		List<CmBillInfo> list = cmRepayService.exportRepayInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家还款信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id", null);
			maps.put("orderId", null);
			maps.put("userNo", null);
			maps.put("cardNo", null);
			maps.put("mobileNo", null);
			maps.put("userName", null);
			maps.put("orderMoney", null);
			maps.put("billStatus", null);
			maps.put("payWay", null);
			maps.put("payDate", null);
			data.add(maps);
		} else {
			for (CmBillInfo i : list) {
				String billStatus = "";
				if (i.getBillStatus() == null) {
				} else if (i.getBillStatus() == 0) {
					billStatus = "未还款";
				} else if (i.getBillStatus() == 1) {
					billStatus = "已还款";
				}
				if ("1".equals(i.getPayWay())) {
					i.setPayWay("手工标记");
				} else if ("2".equals(i.getPayWay())) {
					i.setPayWay("超级还-分期");
				} else if ("3".equals(i.getPayWay())) {
					i.setPayWay("超级还-全额");
				} else if ("4".equals(i.getPayWay())) {
					i.setPayWay("超级还-完美");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", null == i.getId() ? "" : i.getId().toString());
				maps.put("orderId", null == i.getOrderId() ? "" : i.getOrderId());
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("cardNo", null == i.getCardNo() ? "" : i.getCardNo());
				maps.put("mobileNo", null == i.getMobileNo() ? "" : i.getMobileNo());
				maps.put("userName", null == i.getUserName() ? "" : i.getUserName());
				maps.put("orderMoney", null == i.getOrderMoney() ? "" : i.getOrderMoney());
				maps.put("billStatus", billStatus);
				maps.put("payWay", null == i.getPayWay() ? "" : i.getPayWay());
				maps.put("payDate", null == i.getPayDate() ? "" : sdf1.format(i.getPayDate()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"id", "orderId", "userNo", "cardNo", "mobileNo",
				"userName", "orderMoney", "billStatus", "payWay", "payDate"};
		String[] colsName = {"还款ID", "关联订单号", "用户ID", "银行卡号",
				"手机号", "姓名", "还款金额", "还款状态", "还款方式", "还款时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
