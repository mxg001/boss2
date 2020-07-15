package cn.eeepay.framework.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RepaySettleOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WithdrawHisInfo;
import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.service.RepaySettleOrderService;

/**
 * 超级还款结算管理
 * @author mays
 * @date 2017年10月31日
 */
@Service("repaySettleOrderService")
@Transactional
public class RepaySettleOrderServiceImpl implements RepaySettleOrderService {

	@Resource
	private RepaySettleOrderDao repaySettleOrderDao;

	/**
	 * 结算订单查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<WithdrawHisInfo> selectSettleOrderByParam(Page<WithdrawHisInfo> page, WithdrawHisInfo info) {
		List<WithdrawHisInfo> list=repaySettleOrderDao.selectSettleOrderByParam(page, info);
		dataProcessingList(page.getResult());
		return list;
	}

	/**
	 * 数据处理List
	 */
	private void dataProcessingList(List<WithdrawHisInfo> list){
		if(list!=null&&list.size()>0){
			for(WithdrawHisInfo item:list){
				if(item!=null){
					item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
				}
			}
		}
	}

	/**
	 * 结算订单查询汇总
	 * @author mays
	 * @date 2017年10月31日
	 */
	public String countSettleOrderByParam(WithdrawHisInfo info) {
		return repaySettleOrderDao.countSettleOrderByParam(info);
	}

	/**
	 * 根据orderNo查询yfb_withdraw_his
	 * @author mays
	 * @date 2017年12月18日
	 */
	public WithdrawHisInfo selectYfbWithdrawHis(String orderNo) {
		return repaySettleOrderDao.selectYfbWithdrawHis(orderNo);
	}

	/**
	 * 结算订单导出
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<WithdrawHisInfo> exportSettleOrderByParam(WithdrawHisInfo info) {
		List<WithdrawHisInfo> list=repaySettleOrderDao.exportSettleOrderByParam(info);
		dataProcessingList(list);
		return list;
	}

	/**
	 * 交易查询
	 */
	public List<YfbPayOrder> selectRepayTradeOrder(Page<YfbPayOrder> page, YfbPayOrder info) {
		List<YfbPayOrder> list=repaySettleOrderDao.selectRepayTradeOrder(page, info);
		dataProcessingList1(page.getResult());
		return list;
	}

	/**
	 * 数据处理List
	 */
	private void dataProcessingList1(List<YfbPayOrder> list){
		if(list!=null&&list.size()>0){
			for(YfbPayOrder item:list){
				if(item!=null){
					item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
				}
			}
		}
	}
	/**
	 * 交易查询汇总
	 */
	public Map<String, String> countRepayTradeOrder(YfbPayOrder info) {
		return repaySettleOrderDao.countRepayTradeOrder(info);
	}

	/**
	 * 交易查询-导出
	 */
	public List<YfbPayOrder> exportRepayTradeOrder(YfbPayOrder info) {
		List<YfbPayOrder> list=repaySettleOrderDao.exportRepayTradeOrder(info);
		dataProcessingList1(list);
		return list;
	}

	/**
	 * 交易详情
	 */
	public YfbPayOrder selectTradeOrderDetail(String orderNo) {
		return repaySettleOrderDao.selectTradeOrderDetail(orderNo);
	}

	/**
	 * 获取交易通道，yfb_pay_channel
	 */
	public List<Map<String, String>> listAcqCode() {
		return repaySettleOrderDao.listAcqCode();
	}

	public YfbPayOrder getYfbPayOrder(String orderNo) {
		return repaySettleOrderDao.getYfbPayOrder(orderNo);
	}

}
