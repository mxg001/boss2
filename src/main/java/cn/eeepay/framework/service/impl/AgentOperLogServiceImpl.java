package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentOperLogDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLog;
import cn.eeepay.framework.service.AgentOperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("agentOperLogService")
@Transactional
public class AgentOperLogServiceImpl implements AgentOperLogService {
	
	private static final Logger log = LoggerFactory.getLogger(AgentOperLogServiceImpl.class);
	
	@Resource
	private AgentOperLogDao agentOperLogDao;

	@Override
	public int insert(AgentOperLog bossOperLog) {
		// TODO Auto-generated method stub
		return agentOperLogDao.insert(bossOperLog);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<AgentOperLog> queryByCondition(Page page, AgentOperLog logInfo) {
		return agentOperLogDao.queryByCondition(page, logInfo);
	}
	
	@Override
	public AgentOperLog queryDetail(Integer id) {
		return agentOperLogDao.queryDetail(new AgentOperLog().setId(id));
	}
}
