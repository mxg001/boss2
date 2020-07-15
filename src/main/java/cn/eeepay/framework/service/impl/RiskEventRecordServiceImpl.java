package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RiskEventRecordDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskEventRecord;
import cn.eeepay.framework.service.RiskEventRecordService;

@Service("riskEventRecordService")
@Transactional
public class RiskEventRecordServiceImpl implements RiskEventRecordService{
	@Resource
	private RiskEventRecordDao riskEventRecordDao;

	@Override
	public List<RiskEventRecord> queryEventRecordList(Page<RiskEventRecord> page, RiskEventRecord riskEventRecord) {
		return riskEventRecordDao.queryEventRecordList(page, riskEventRecord);
	}

	@Override
	public RiskEventRecord findRiskEventRecordById(int id) {
		return riskEventRecordDao.findRiskEventRecordById(id);
	}

	@Override
	public int updateHandleStatus(RiskEventRecord riskEventRecord) {
		return riskEventRecordDao.updateHandleStatus(riskEventRecord);
	}

	@Override
	public List<RiskEventRecord> riskEventRecordExport(RiskEventRecord rr) {
		return riskEventRecordDao.riskEventRecordExport(rr);
	}

}
