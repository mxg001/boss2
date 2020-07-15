package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskEventRecord;

public interface RiskEventRecordService {

	List<RiskEventRecord> queryEventRecordList(Page<RiskEventRecord> page, RiskEventRecord riskEventRecord);

	RiskEventRecord findRiskEventRecordById(int id);

	int updateHandleStatus(RiskEventRecord record);

	List<RiskEventRecord> riskEventRecordExport(RiskEventRecord rr);

}
