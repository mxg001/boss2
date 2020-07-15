package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RiskProblemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskProblem;
import cn.eeepay.framework.model.RiskProblemAuditRecord;
import cn.eeepay.framework.model.ShiroUser;
import cn.eeepay.framework.service.RiskProblemService;

@Service("riskProblemService")
@Transactional
public class RiskProblemServiceImpl implements RiskProblemService {

	@Resource
	private RiskProblemDao riskProblemDao;

	@Override
	public List<RiskProblem> selectAllInfo(Page<RiskProblem> page, RiskProblem record) {
		return riskProblemDao.selectAllInfo(page, record);
	}

	@Override
	public RiskProblem selectInfo(int id) {
		return riskProblemDao.selectInfo(id);
	}

	@Override
	public List<RiskProblemAuditRecord> selectRecordAllInfo(int pid) {
		return riskProblemDao.selectRecordAllInfo(pid);
	}

	@Override
	public int updateFeedback(RiskProblem record) {
		return riskProblemDao.updateFeedback(record);
	}

	@Override
	public int updateInfo(RiskProblem record) {
		return riskProblemDao.updateInfo(record);
	}

	@Override
	public List<RiskProblem> selectAuditAllInfo(Page<RiskProblem> page, RiskProblem record) {
		return riskProblemDao.selectAuditAllInfo(page, record);
	}

	@Override
	public int insertAuditRecord(RiskProblemAuditRecord record,RiskProblem rp) {
		int i =0;
		i+=riskProblemDao.insertAuditRecord(record);
		i+=riskProblemDao.updateStatus(rp);
		return i;
	}

	@Override
	public List<ShiroUser> selectBossAllInfo() {
		return riskProblemDao.selectBossAllInfo();
	}

	@Override
	public int insertInfo(RiskProblem record) {
		return riskProblemDao.insertInfo(record);
	}

	@Override
	public RiskProblem selectInfoByRuleNo(int ruleNo) {
		return riskProblemDao.selectInfoByRuleNo(ruleNo);
	}

}
