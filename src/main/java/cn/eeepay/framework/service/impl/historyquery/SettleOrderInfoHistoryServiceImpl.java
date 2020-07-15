package cn.eeepay.framework.service.impl.historyquery;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoHistory.SettleOrderInfoHistoryDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;
import cn.eeepay.framework.service.historyquery.SettleOrderInfoHistoryService;
import cn.eeepay.framework.service.impl.TransferService;

@Service("settleOrderInfoHistoryService")
@Transactional
public class SettleOrderInfoHistoryServiceImpl implements SettleOrderInfoHistoryService {

	@Resource
	private SettleOrderInfoHistoryDao settleOrderInfoHistoryDao;

	@Override
	public List<SettleOrderInfo> selectAllInfo(Page<SettleOrderInfo> page, SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.selectAllInfo(page, soi);
	}

	@Override
	public SettleOrderInfo selectInfo(String tranId) {
		return settleOrderInfoHistoryDao.selectInfo(tranId);
	}

	@Override
	public List<SettleTransfer> selectSettleInfo(String tranId) {
		return settleOrderInfoHistoryDao.selectSettleInfo(tranId);
	}

	@Override
	public String getTotalNumAndTotalMoney(SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.getTotalNumAndTotalMoney(soi);
	}

	@Override
	public List<SettleOrderInfo> selectOutDetailAllInfo(Page<SettleOrderInfo> page, SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.selectOutDetailAllInfo(page,soi);
	}

	@Override
	public Map<String, String> getOutDetailTotalMoney(SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.getOutDetailTotalMoney(soi);
	}

	@Override
	public List<SettleOrderInfo> exportOutDetailAllInfo(SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.exportOutDetailAllInfo(soi);
	}

	@Override
	public SettleTransfer selectOutSettleInfo(String id) {
		return settleOrderInfoHistoryDao.selectOutSettleInfo(id);
	}

	@Override
	public SettleOrderInfo selectOrderNoInfo(String orderNo) {
		return settleOrderInfoHistoryDao.selectOrderNoInfo(orderNo);
	}

	@Override
	public List<SettleOrderInfo> importAllInfo(SettleOrderInfo soi) {
		return settleOrderInfoHistoryDao.importAllInfo(soi);
	}

	@Resource
	private TransferService transferService;

	@Override
	public SettleOrderInfo getBySettleOrder(String settleOrder) {
		return settleOrderInfoHistoryDao.getBySettleOrder(settleOrder);
	}

	@Override
	public int updateSettleOrderStatus(String settleOrder, int i) {
		return settleOrderInfoHistoryDao.updateSettleOrderStatus(settleOrder,1);
	}

}
