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
import cn.eeepay.framework.model.CmPayment;
import cn.eeepay.framework.service.CmOrderService;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
@RequestMapping(value = "/cmOrder")
public class CmOrderAction {
	private static final Logger log = LoggerFactory.getLogger(CmOrderAction.class);

	@Resource
	public CmOrderService cmOrderService;

	/**
	 * 订单查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/selectOrderInfo")
	@ResponseBody
	public Map<String, Object> selectOrderInfo(@RequestBody CmPayment info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<CmPayment> page = new Page<>(pageNo, pageSize);
			cmOrderService.selectOrderInfo(page, info);
			msg.put("page", page);
			msg.put("sumAmount", cmOrderService.sumOrderInfo(info));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-订单查询失败", e);
		}
		return msg;
	}

	/**
	 * 根据tradeNo查询订单详情
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/queryOrderInfoById")
	@ResponseBody
	public Map<String, Object> queryOrderInfoById(String tradeNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg.put("info", cmOrderService.queryOrderInfoById(tradeNo));
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡管家-查询订单详情失败", e);
		}
		return msg;
	}

	/**
	 * 信用卡管家-订单查询-导出
	 * @author	mays
	 * @date	2018年5月24日
	 */
	@RequestMapping(value = "/exportOrderInfo")
	@ResponseBody
	public void exportOrderInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
		CmPayment bean = JSON.parseObject(info, CmPayment.class);
		List<CmPayment> list = cmOrderService.exportOrderInfo(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "信用卡管家订单信息" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("tradeNo", null);
			maps.put("orgName", null);
			maps.put("orgId", null);
			maps.put("userNo", null);
			maps.put("transSubject", null);
			maps.put("transStatus", null);
			maps.put("transAmount", null);
			maps.put("transType", null);
			maps.put("thirdTradeNo", null);
			maps.put("createTime", null);
			maps.put("paymentTime", null);
			maps.put("expireTime", null);
			data.add(maps);
		} else {
			for (CmPayment i : list) {
				if ("alipay".equals(i.getTransType())) {
					i.setTransType("支付宝");
				} else if ("weixin".equals(i.getTransType())) {
					i.setTransType("微信");
				}
				if ("WAIT".equals(i.getTransStatus())) {
					i.setTransStatus("待付款");
				} else if ("SUCCESS".equals(i.getTransStatus())) {
					i.setTransStatus("已付款");
				} else if ("CLOSED".equals(i.getTransStatus())) {
					i.setTransStatus("已关闭");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("tradeNo", null == i.getTradeNo() ? "" : i.getTradeNo());
				maps.put("orgName", null == i.getOrgName() ? "" : i.getOrgName());
				maps.put("orgId", null == i.getOrgId() ? "" : i.getOrgId());
				maps.put("userNo", null == i.getUserNo() ? "" : i.getUserNo());
				maps.put("transSubject", null == i.getTransSubject() ? "" : i.getTransSubject());
				maps.put("transStatus", null == i.getTransStatus() ? "" : i.getTransStatus());
				maps.put("transAmount", null == i.getTransAmount() ? "" : i.getTransAmount().toString());
				maps.put("transType", null == i.getTransType() ? "" : i.getTransType());
				maps.put("thirdTradeNo", null == i.getThirdTradeNo() ? "" : i.getThirdTradeNo());
				maps.put("createTime", null == i.getCreateTime() ? "" : sdf1.format(i.getCreateTime()));
				maps.put("paymentTime", null == i.getPaymentTime() ? "" : sdf1.format(i.getPaymentTime()));
				maps.put("expireTime", null == i.getExpireTime() ? "" : sdf1.format(i.getExpireTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"tradeNo", "orgName", "orgId", "userNo", "transSubject", "transStatus",
				"transAmount", "transType", "thirdTradeNo", "createTime", "paymentTime", "expireTime"};
		String[] colsName = { "订单ID", "组织名称", "组织编号", "用户ID", "订单类型", "订单状态",
				"订单金额", "支付方式", "关联支付单号", "创建时间", "付款时间", "会员到期时间" };
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
