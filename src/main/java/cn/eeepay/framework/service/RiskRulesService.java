package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRules;
import cn.eeepay.framework.model.TeamInfo;

public interface RiskRulesService {

	List<RiskRules> selectAllInfo(Page<RiskRules> page,RiskRules rr);
	
	RiskRules selectDetail(int id);
	
	int updateInfo(RiskRules rr);
	
	int updateStatus(RiskRules rr);
	
	RiskRules selectByRoll(String roll);
	
	List<RiskRules> selectAll();

	List<RiskRules> selectAllWithOutStatus();
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateBatch(List<RiskRules> list);

	int updateRulesInstruction(RiskRules rr);

	RiskRules selectByRulesNo(Integer rulesNo);

	List<TeamInfo> getAllScope();

    void updateFaceRecognition(String jskj, String xskj);
}
