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

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WithdrawHisInfo;
import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.service.RepaySettleOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 超级还款结算管理,交易查询
 * @author mays
 * @date 2017年10月31日
 */
@Controller
@RequestMapping(value = "/repaySettleOrder")
public class RepaySettleOrderAction {

	private Logger log = LoggerFactory.getLogger(RepaySettleOrderAction.class);

	@Resource
	private RepaySettleOrderService repaySettleOrderService;

	/**
	 * 结算订单查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectSettleOrderByParam")
	@ResponseBody
	public Map<String, Object> selectSettleOrderByParam(@RequestBody WithdrawHisInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<WithdrawHisInfo> page = new Page<>(pageNo, pageSize);
			repaySettleOrderService.selectSettleOrderByParam(page, info);
			String sumAmount = repaySettleOrderService.countSettleOrderByParam(info);
			msg.put("status", true);
			msg.put("page", page);
			msg.put("sumAmount", sumAmount);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡结算订单查询失败", e);
		}
		return msg;
	}

	/**
	 * 再次出款
	 * @author mays
	 * @date 2017年10月31日
	 */
	@RequestMapping(value = "/againPayment")
	@ResponseBody
	@SystemLog(description = "再次出款", operCode = "repaySettleOrder.againPayment")
	public Map<String, Object> againPayment(@RequestBody String orderNo) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "出款失败");
		try {
			if (orderNo == null || orderNo.isEmpty()) {
				msg.put("status", false);
				msg.put("msg", "订单号为空");
				return msg;
			}
			WithdrawHisInfo whi = repaySettleOrderService.selectYfbWithdrawHis(orderNo);
			if (!"withdraw".equals(whi.getService())) {
				msg.put("status", false);
				msg.put("msg", "该订单的订单类型不为'用户提现'");
				return msg;
			}
			if (!"3".equals(whi.getStatus())) {
				msg.put("status", false);
				msg.put("msg", "该订单的结算状态不为'未结算'");
				return msg;
			}
			String returnMsg = ClientInterface.repayAgainPayment(whi.getServiceOrderNo());
			Map<String, Object> result = JSON.parseObject(returnMsg);
			if (result != null && "200".equals(result.get("status"))) {
				msg.put("status", true);
				msg.put("msg", "出款成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "出款失败");
			log.error("信用卡还款再次出款失败", e);
		}
		return msg;
	}

	/**
	 * 结算订单导出
	 * @author mays
	 * @date 2017年10月31日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportInfo")
	@ResponseBody
	public void exportAllInfo(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response)
			throws Exception {
		WithdrawHisInfo bean = JSON.parseObject(baseInfo, WithdrawHisInfo.class);
		List<WithdrawHisInfo> list = repaySettleOrderService.exportSettleOrderByParam(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "结算订单记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (WithdrawHisInfo info : list) {
			if ("0".equals(info.getStatus())) {
				info.setStatus("未结算");
			} else if ("1".equals(info.getStatus())) {
				info.setStatus("结算中");
			} else if ("2".equals(info.getStatus())) {
				info.setStatus("已结算");
			} else if ("3".equals(info.getStatus())) {
				info.setStatus("结算失败");
			}
			if ("repayPlan".equals(info.getService())) {
				info.setService("计划还款");
			} else if ("withdraw".equals(info.getService())) {
				info.setService("用户提现");
			}
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("orderNo", null == info.getOrderNo() ? "" : info.getOrderNo());
			maps.put("serviceOrderNo", null == info.getServiceOrderNo() ? "" : info.getServiceOrderNo());
			maps.put("merNo", null == info.getMerNo() ? "" : info.getMerNo());
			maps.put("channelName", null == info.getChannelName() ? "" : info.getChannelName());
			maps.put("outAccNo", null == info.getOutAccNo() ? "" : info.getOutAccNo());
			maps.put("nickname", null == info.getNickname() ? "" : info.getNickname());
			maps.put("accName", null == info.getAccName() ? "" : info.getAccName());
			maps.put("mobileNo", null == info.getMobileNo() ? "" : info.getMobileNo());
			maps.put("accNo", null == info.getAccNo() ? "" : info.getAccNo());
			maps.put("status", null == info.getStatus() ? "" : info.getStatus());
			maps.put("service", null == info.getService() ? "" : info.getService());
			maps.put("amount", null == info.getAmount() ? "" : info.getAmount().toString());
			maps.put("fee", null == info.getFee() ? "" : info.getFee().toString());
			maps.put("bankOrderNo", null == info.getBankOrderNo() ? "" : info.getBankOrderNo());
			maps.put("createTime", null == info.getCreateTime() ? "" : sdf1.format(info.getCreateTime()));
			data.add(maps);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {"orderNo", "serviceOrderNo", "merNo", "channelName", "outAccNo", "nickname", "accName", "mobileNo", "accNo", "status", "service", "amount", "fee", "bankOrderNo", "createTime"};
		String[] colsName = new String[] {"订单ID", "业务订单ID", "用户编号", "出款渠道", "上游商户号", "昵称", "姓名", "手机号", "银行卡号", "结算状态", "订单类型", "出款金额", "手续费", "上游订单号", "时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

	/**
	 * 获取交易通道，yfb_pay_channel
	 * @author	mays
	 * @date	2017年11月28日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/listAcqCode")
	@ResponseBody
	public Map<String, Object> listAcqCode() {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<Map<String, String>> acqCodes = repaySettleOrderService.listAcqCode();
			msg.put("status", true);
			msg.put("acqCodes", acqCodes);
		} catch (Exception e) {
			msg.put("status", false);
			log.error("获取交易通道失败", e);
		}
		return msg;
	}

	/**
	 * 超级还-交易查询
	 * @author	mays
	 * @date	2017年11月17日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectRepayTradeOrder")
	@ResponseBody
	public Map<String, Object> selectRepayTradeOrder(@RequestBody YfbPayOrder info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<YfbPayOrder> page = new Page<>(pageNo, pageSize);
			repaySettleOrderService.selectRepayTradeOrder(page, info);
			Map<String, String> count = repaySettleOrderService.countRepayTradeOrder(info);
			msg.put("status", true);
			msg.put("page", page);
			msg.put("count", count);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("超级还-交易查询失败", e);
		}
		return msg;
	}

	/**
	 * 交易详情
	 * @author	mays
	 * @date	2017年11月21日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectTradeOrderDetail")
	@ResponseBody
	public Map<String, Object> selectTradeOrderDetail(String orderNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (orderNo == null || orderNo.isEmpty()) {
				msg.put("status", false);
				msg.put("msg", "订单号为空");
				return msg;
			}
			YfbPayOrder info = repaySettleOrderService.selectTradeOrderDetail(orderNo);
			msg.put("status", true);
			msg.put("info", info);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("超级还-交易详情查询失败", e);
		}
		return msg;
	}

	/**
	 * 超级还-交易查询-导出
	 * @author	mays
	 * @date	2017年11月20日
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/exportRepayTradeOrder")
	@ResponseBody
	public void exportRepayTradeOrder(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response) throws Exception {
		YfbPayOrder bean = JSON.parseObject(baseInfo, YfbPayOrder.class);
		List<YfbPayOrder> list = repaySettleOrderService.exportRepayTradeOrder(bean);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "交易记录" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("orderNo", null);
			maps.put("merchantNo", null);
			maps.put("mobileNo", null);
			maps.put("service", null);
			maps.put("transType", null);
			maps.put("transStatus", null);
			maps.put("transAmount", null);
			maps.put("transFee", null);
			maps.put("transFeeRate", null);
			maps.put("serviceOrderNo", null);
			maps.put("recordStatus", null);
			maps.put("acqCode", null);
			maps.put("acqMerchantNo", null);
			maps.put("accountNo", null);
			maps.put("resMsg", null);
			maps.put("createTime", null);
			maps.put("transTime", null);
			data.add(maps);
		} else {
			for (YfbPayOrder info : list) {
				if ("repayPlan".equals(info.getService())) {
					info.setService("还款计划消费");
				} else if ("ensure".equals(info.getService())) {
					info.setService("保证金");
				} else if ("fullRepayFee".equals(info.getService())) {
					info.setService("全额还款手续费");
				} else if ("perfectPlan".equals(info.getService())) {
					info.setService("完美还款消费");
				}
				if ("quickPay".equals(info.getTransType())) {
					info.setTransType("快捷");
				}
				if ("0".equals(info.getTransStatus())) {
					info.setTransStatus("初始化");
				} else if ("1".equals(info.getTransStatus())) {
					info.setTransStatus("交易中");
				} else if ("2".equals(info.getTransStatus())) {
					info.setTransStatus("交易成功");
				} else if ("3".equals(info.getTransStatus())) {
					info.setTransStatus("交易失败");
				} else if ("4".equals(info.getTransStatus())) {
					info.setTransStatus("未知");
				}
				if ("0".equals(info.getRecordStatus())) {
					info.setRecordStatus("未记账");
				} else if ("1".equals(info.getRecordStatus())) {
					info.setRecordStatus("记账中");
				} else if ("2".equals(info.getRecordStatus())) {
					info.setRecordStatus("记账成功");
				} else if ("3".equals(info.getRecordStatus())) {
					info.setRecordStatus("记账失败");
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("orderNo", null == info.getOrderNo() ? "" : info.getOrderNo());
				maps.put("merchantNo", null == info.getMerchantNo() ? "" : info.getMerchantNo());
				maps.put("mobileNo", null == info.getMobileNo() ? "" : info.getMobileNo());
				maps.put("service", null == info.getService() ? "" : info.getService());
				maps.put("transType", null == info.getTransType() ? "" : info.getTransType());
				maps.put("transStatus", null == info.getTransStatus() ? "" : info.getTransStatus());
				maps.put("transAmount", null == info.getTransAmount() ? "" : info.getTransAmount().toString());
				maps.put("transFee", null == info.getTransFee() ? "" : info.getTransFee().toString());
				maps.put("transFeeRate", null == info.getTransFeeRate() ? "" : info.getTransFeeRate());
				maps.put("serviceOrderNo", null == info.getServiceOrderNo() ? "" : info.getServiceOrderNo());
				maps.put("recordStatus", null == info.getRecordStatus() ? "" : info.getRecordStatus());
				maps.put("acqCode", null == info.getAcqCode() ? "" : info.getAcqCode());
				maps.put("acqMerchantNo", null == info.getAcqMerchantNo() ? "" : info.getAcqMerchantNo());
				maps.put("accountNo", null == info.getAccountNo() ? "" : info.getAccountNo());
				maps.put("resMsg", null == info.getResMsg() ? "" : info.getResMsg());
				maps.put("createTime", null == info.getCreateTime()? "" : sdf1.format(info.getCreateTime()));
				maps.put("transTime", null == info.getTransTime() ? "" : sdf1.format(info.getTransTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = {"orderNo", "merchantNo", "mobileNo", "service", "transType", "transStatus", "transAmount", "transFee",
				"transFeeRate", "serviceOrderNo", "recordStatus", "acqCode", "acqMerchantNo", "accountNo", "resMsg","createTime", "transTime"};
		String[] colsName = {"订单ID", "用户编号", "手机号", "订单类型", "交易方式", "交易状态", "交易金额",
				"交易手续费", "交易费率", "关联业务订单", "记账状态", "交易通道","上游商户号", "银行卡号", "错误详情","创建时间", "交易时间"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

}
