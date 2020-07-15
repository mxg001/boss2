package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AgentFunctionManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentFunctionManager;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.service.AgentFunctionManagerService;

@Service("agentFunctionManagerService")
@Transactional
public class AgentFunctionManagerServiceImpl implements AgentFunctionManagerService {

	@Resource
	private AgentFunctionManagerDao agentFunctionManagerDao;

	@Override
	public List<AgentFunctionManager> selectByParam(AgentFunctionManager agentFunctionManager,
			Page<AgentFunctionManager> page) {
		return agentFunctionManagerDao.selectByParam(agentFunctionManager, page);
	}

	@Override
	public List<AgentFunctionManager> exportConfig(AgentFunctionManager agentFunctionManager) {
		return agentFunctionManagerDao.exportConfig(agentFunctionManager);
	}

	@Override
	public int addAgentFunctionManager(AgentFunctionManager agentFunctionManager) {
		return agentFunctionManagerDao.addAgentFunctionManager(agentFunctionManager);
	}

	@Override
	public List<AgentInfo> findAgentInfo(AgentInfo agentInfo) {
		return agentFunctionManagerDao.findAgentInfo(agentInfo);
	}

	@Override
	public AgentInfo findAgentInfoByAgentNo(String agentNo) {
		return agentFunctionManagerDao.findAgentInfoByAgentNo(agentNo);
	}

	@Override
	public int deleteInfo(String agentNo,String functionNumber,Integer blacklist) {
		if(functionNumber!=null){
			return agentFunctionManagerDao.deleteInfoBlacklist(agentNo,functionNumber,blacklist);
		}else{
			return agentFunctionManagerDao.deleteInfobyAgentNo(agentNo);
		}
	}

	@Override
	public int addButchAgentFunctionManager(List<AgentFunctionManager> agentFunctionManager) {
		return agentFunctionManagerDao.addButchAgentFunctionManager(agentFunctionManager);
	}

    @Override
    public int selectExists(AgentFunctionManager agentFunctionManager) {
        return agentFunctionManagerDao.selectExists(agentFunctionManager);
    }

	@Override
	public AgentFunctionManager get(Integer id) {
		return agentFunctionManagerDao.selectById(id);
	}

	@Override
	public int delete(Integer id) {
		return agentFunctionManagerDao.delete(id);
	}

	@Override
	public AgentInfo findAgentInfoByAgentNoOneLevel(String agentNo) {
		return agentFunctionManagerDao.findAgentInfoByAgentNoOneLevel(agentNo);
	}

	@Override
	public boolean isAgentControlContains(String agentNo,String functionNumber) {
		return agentFunctionManagerDao.countAgentControlContains(agentNo,functionNumber) > 0;
	}

	@Override
	public void deleteByAgentNoFunNum(String agentNo, String functionNumber2) {
		agentFunctionManagerDao.deleteByAgentNoFunNum(agentNo,functionNumber2);
	}

	@Override
	public boolean isBlacklistNotContains(String agentNo, String functionNumber2) {
		return agentFunctionManagerDao.countBlacklistNotContains(agentNo,functionNumber2) > 0;
	}

	@Override
	public int addAgentFunctionManagerBlacklist(AgentFunctionManager agentFunctionManager) {
		
		return agentFunctionManagerDao.addAgentFunctionManagerBlacklist(agentFunctionManager);
	}

	@Override
	public void addButchAgentFunctionManagerBlacklist(List<AgentFunctionManager> list) {
		agentFunctionManagerDao.addButchAgentFunctionManagerBlacklist(list);
	}

	@Override
	public int selectExistsBlacklist(AgentFunctionManager agentFunctionManager) {
		return agentFunctionManagerDao.selectExistsBlacklist(agentFunctionManager);
	}

	@Override
	public void deleteInfo(String agentNo, String functionNumber) {
		agentFunctionManagerDao.deleteInfo(agentNo,functionNumber);
	}

	@Override
	public List<AgentFunctionManager> selectByParamBlacklist(AgentFunctionManager agentFunctionManager, Page<AgentFunctionManager> page) {
		return agentFunctionManagerDao.selectByParamBlacklist(agentFunctionManager, page);
	}

	@Override
	public int deleteBlacklist(Integer id) {
		return agentFunctionManagerDao.deleteBlacklist(id);
	}

	@Override
	public AgentFunctionManager getBlacklist(Integer id) {
		return agentFunctionManagerDao.selectBlacklistById(id);
	}

	@Override
	public List<AgentFunctionManager> exportConfigBlacklist(AgentFunctionManager agentFunctionManager) {
		return agentFunctionManagerDao.exportConfigBlacklist(agentFunctionManager);
	}
}
