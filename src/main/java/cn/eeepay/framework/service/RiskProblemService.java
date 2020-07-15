package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskProblem;
import cn.eeepay.framework.model.RiskProblemAuditRecord;
import cn.eeepay.framework.model.ShiroUser;

public interface RiskProblemService {

	List<RiskProblem> selectAllInfo(Page<RiskProblem>page,RiskProblem record);
	
	List<RiskProblem> selectAuditAllInfo(Page<RiskProblem>page,RiskProblem record);
	
	List<RiskProblemAuditRecord> selectRecordAllInfo(int pid);
	
	RiskProblem selectInfo(int id);
	
	int updateFeedback(RiskProblem record);
	
	int updateInfo(RiskProblem record);
	
	int insertAuditRecord(RiskProblemAuditRecord record,RiskProblem rp);
	
	List<ShiroUser> selectBossAllInfo();
	
	int insertInfo(RiskProblem record);
	
	RiskProblem selectInfoByRuleNo(int ruleNo);
}
