package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WithdrawHisInfo;
import cn.eeepay.framework.model.YfbPayOrder;

/**
 * 超级还款结算管理
 * @author mays
 * @date 2017年10月31日
 */
public interface RepaySettleOrderService {

	/**
	 * 结算订单查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<WithdrawHisInfo> selectSettleOrderByParam(Page<WithdrawHisInfo> page, WithdrawHisInfo info);

	/**
	 * 结算订单查询汇总
	 * @author mays
	 * @date 2017年10月31日
	 */
	String countSettleOrderByParam(WithdrawHisInfo info);

	/**
	 * 根据orderNo查询yfb_withdraw_his
	 * @author mays
	 * @date 2017年12月18日
	 */
	WithdrawHisInfo selectYfbWithdrawHis(String orderNo);

	/**
	 * 结算订单导出
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<WithdrawHisInfo> exportSettleOrderByParam(WithdrawHisInfo info);

	/**
	 * 交易查询
	 * @author	mays
	 * @date	2017年11月17日
	 */
	List<YfbPayOrder> selectRepayTradeOrder(Page<YfbPayOrder> page, YfbPayOrder info);

	/**
	 * 交易查询汇总
	 * @author	mays
	 * @date	2017年11月20日
	 */
	Map<String, String> countRepayTradeOrder(YfbPayOrder info);

	/**
	 * 交易查询-导出
	 * @author	mays
	 * @date	2017年11月20日
	 */
	List<YfbPayOrder> exportRepayTradeOrder(YfbPayOrder info);

	/**
	 * 交易详情
	 * @author	mays
	 * @date	2017年11月21日
	 */
	YfbPayOrder selectTradeOrderDetail(String orderNo);

	/**
	 * 获取交易通道，yfb_pay_channel
	 * @author	mays
	 * @date	2017年11月28日
	 */
	List<Map<String, String>> listAcqCode();

	YfbPayOrder getYfbPayOrder(String orderNo);

}
