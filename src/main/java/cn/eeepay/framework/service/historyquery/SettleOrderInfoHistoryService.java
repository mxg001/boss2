package cn.eeepay.framework.service.historyquery;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;

public interface SettleOrderInfoHistoryService {

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
}
