package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentFunctionManager;
import cn.eeepay.framework.model.AgentInfo;

public interface AgentFunctionManagerService {

	List<AgentFunctionManager> selectByParam( AgentFunctionManager agentFunctionManager, Page<AgentFunctionManager> page);

	List<AgentFunctionManager> exportConfig(AgentFunctionManager agentFunctionManager);

	int addAgentFunctionManager(AgentFunctionManager agentFunctionManager);
	
	List<AgentInfo> findAgentInfo(AgentInfo agentInfo);

	 AgentInfo findAgentInfoByAgentNo(String agentNo);

	int deleteInfo(String agentNo,String functionNumber,Integer blacklist);
	
	int addButchAgentFunctionManager(List<AgentFunctionManager> agentFunctionManager);

    int selectExists(AgentFunctionManager agentFunctionManager);

	AgentFunctionManager get(Integer id);

	int delete(Integer id);

	AgentInfo findAgentInfoByAgentNoOneLevel(String agentNo);

	boolean isAgentControlContains(String agentNo,String functionNumber);

	void deleteByAgentNoFunNum(String agentNo, String functionNumber2);

	boolean isBlacklistNotContains(String agentNo, String functionNumber2);

	int addAgentFunctionManagerBlacklist(AgentFunctionManager agentFunctionManager);

	void addButchAgentFunctionManagerBlacklist(List<AgentFunctionManager> list);

	int selectExistsBlacklist(AgentFunctionManager agentFunctionManager);

	void deleteInfo(String agentNo, String functionNumber);

	List<AgentFunctionManager> selectByParamBlacklist(AgentFunctionManager agentFunctionManager, Page<AgentFunctionManager> page);

	int deleteBlacklist(Integer id);

	AgentFunctionManager getBlacklist(Integer id);

	List<AgentFunctionManager> exportConfigBlacklist(AgentFunctionManager agentFunctionManager);

}
