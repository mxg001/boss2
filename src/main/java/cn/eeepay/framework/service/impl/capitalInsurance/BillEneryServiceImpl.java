package cn.eeepay.framework.service.impl.capitalInsurance;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.capitalInsurance.BillEneryDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.service.capitalInsurance.BillEneryService;

@Service("billEneryService")
@Transactional
public class BillEneryServiceImpl implements BillEneryService {
	private static final Logger log = LoggerFactory.getLogger(BillEneryServiceImpl.class);
	@Resource
	private BillEneryDao billEneryDao;
	@Override
	public List<BillEntry> queryAllInfo(BillEntry tis, Page<BillEntry> page) {
		return billEneryDao.queryAllInfo(tis,page);
	}
	@Override
	public BillEntry getEneryDetail(String id) {
		return billEneryDao.getEneryDetail(id);
	}
	@Override
	public List<BillEntry> importAllInfo(BillEntry tis) {
		return billEneryDao.importAllInfo(tis);
	}
	@Override
	public List<ShareReport> getInitReport(String sDate,String eDate) {
		return billEneryDao.getInitReport( sDate, eDate);
	}
	@Override
	public int reportNumByBatchNo(String batchNo) {
		return billEneryDao.reportNumByBatchNo(batchNo);
	}
	@Override
	public int isUploadFile(String orderType, String insurer, String startDate, String endtDate) {
		return billEneryDao.isUploadFile( orderType,  insurer,  startDate,  endtDate);
	}
}
