package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRoll;
import cn.eeepay.framework.model.RiskRollExport;
import cn.eeepay.framework.model.RiskRollList;
import cn.eeepay.framework.model.BlackOperLog;

import java.util.List;
import java.util.Map;

public interface RiskRollService {

	List<RiskRoll> selectRollAllInfo(Page<RiskRoll> page,RiskRoll record);

	List<RiskRollExport> selectRollAllInfoExport(RiskRoll record);

	List<RiskRollList> selectRollList(Page<RiskRollList> page,RiskRollList record);

	int updateRollStatus(RiskRoll record);

	int updateRollInfo(RiskRoll record);

	int insertRoll(RiskRoll record);

	Map<String, Object> insertRollReLoad(RiskRoll record);

	RiskRollList selectInfoByrollNoAndMerNo(RiskRollList record);

	RiskRoll selectRollByRollName(String rollName);

	List<RiskRoll> selectRollByRollNo(String rollNo);

	RiskRoll selectRollByRollNoAndType(String rollNo, Integer rollType, Integer rollBelong);

	int insertRollList(RiskRollList record);

	RiskRoll selectRollDetail(int id);

	int deleteRollListInfo(int id,Map<String, Object> jsonMap);

	int deleteBatch(String idStr,Map<String, Object> jsonMap);

	int updateOpenBatch(String idStr);

	String findBlacklist(String rollNo,String rollType,String rollBelong);

	RiskRoll findRiskRollByRollNoAndRollType(String rollNo, Integer rulesInstruction);

	List<RiskRoll> selectRollAll();

	public List<BlackOperLog> selectBlackLogs(Page<BlackOperLog> page,String rollId);

	public void insertBlackLog(BlackOperLog b);

	public List<BlackOperLog> selectBlackLogsByMerNo(String merchantNo);

	public List<BlackOperLog> selectMoreTime(Page<BlackOperLog> page,String rId);

	public List<BlackOperLog> selectMoreTime2(String rId);

	RiskRoll checkRollByRollNo(String rollNo, Integer rollType, Integer rollBelong);
}
