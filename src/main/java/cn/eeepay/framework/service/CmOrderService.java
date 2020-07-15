package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmPayment;

public interface CmOrderService {

	/**
	 * 订单查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	List<CmPayment> selectOrderInfo(Page<CmPayment> page, CmPayment info);

	List<CmPayment> exportOrderInfo(CmPayment info);

	String sumOrderInfo(CmPayment info);

	/**
	 * 根据tradeNo查询订单详情
	 * @author	mays
	 * @date	2018年5月24日
	 */
	CmPayment queryOrderInfoById(String tradeNo);

}
