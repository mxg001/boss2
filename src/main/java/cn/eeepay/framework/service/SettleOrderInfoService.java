package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;

public interface SettleOrderInfoService {

	List<SettleOrderInfo> selectAllInfo(Page<SettleOrderInfo> page,SettleOrderInfo soi);
	
	SettleOrderInfo selectInfo(String tranId);
	
	SettleOrderInfo selectOrderNoInfo(String orderNo);
	
	List<SettleTransfer> selectSettleInfo(String tranId);
	
	String getTotalNumAndTotalMoney(SettleOrderInfo soi);
	
	List<SettleOrderInfo> selectOutDetailAllInfo(Page<SettleOrderInfo> page,SettleOrderInfo soi);
	
	Map<String, String> getOutDetailTotalMoney(SettleOrderInfo soi);
	
	List<SettleOrderInfo> exportOutDetailAllInfo(SettleOrderInfo soi);
	
	SettleTransfer selectOutSettleInfo(String id);
	
	List<SettleOrderInfo> importAllInfo(SettleOrderInfo soi);


	SettleOrderInfo getBySettleOrder(String settleOrder);

	int updateSettleOrderStatus(String settleOrder, int i);

	//变更状态
    String changeSettleStatus(String currSettleStatus, String oldSettleStatus,String changeSettleStatusReason, String[] ids,String username,Integer orderOrigin);
	//判断订单状态是否已经修改过
	boolean checkCanChangeSettleStatus(String[] id,Integer orderOrigin);

	//获取settle_transfer订单状态
	Integer getSettleStatusByAccountSerialNo(String accountSerialNo);
	//collective_trans_order
	Integer getCTOStatusByOrderNo(String orderNo);

	List<SettleOrderInfo> selectByAccountNos(Page<SettleOrderInfo> page, String[] accountNos);

	Map<String, String> selectTotalMoneyByAccountNos(String[] accountNos);
}
