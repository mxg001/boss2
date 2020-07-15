package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AgentAppInfoDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.TeamInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAppInfo;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.AgentAppInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Service("agentAppInfoService")
@Transactional
public class AgentAppInfoServiceImpl implements AgentAppInfoService {

	@Resource
	private AgentAppInfoDao agentAppInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;
	
	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Override
	public List<AgentAppInfo> selectByCondition(Page<AgentAppInfo> page, Map<String, Object> params) {
		List<AgentAppInfo> list = agentAppInfoDao.selectByCondition(page,params);
		if(list != null){
			for(AgentAppInfo appInfo :list){
				if("0".equals(appInfo.getTeamId())){
					appInfo.setTeamName("全部");
				}
				if("0".equals(appInfo.getAgentNo())){
					appInfo.setAgentName("全部");
				}
			}
		}
		return list;
	}

	@Override
	public AgentAppInfo selectDetailById(Long id) {
		AgentAppInfo appInfo = agentAppInfoDao.selectDetailById(id);
		if(appInfo != null){
			if(appInfo.getTeamId()==null){
				appInfo.setTeamName("全部");
			}
			if(appInfo.getAgentNo()==null){
				appInfo.setAgentName("全部");
			}
			if(appInfo.getPhoto() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, appInfo.getPhoto(), new Date(64063065600000L));
				appInfo.setPhotoUrl(url);
			}
		} 
		return appInfo;
	}

	@Override
	public Map<String, Object> modifyAppInfo(Long id) {
		Map<String, Object> msg = new HashMap<String, Object>();
		List<TeamInfo> allTeam = teamInfoDao.selectTeamName();
		List<AgentInfo> allAgent = agentInfoDao.selectOneAgent();
		AgentAppInfo appInfo = selectDetailById(id);
		msg.put("allTeam", allTeam);
		msg.put("allAgent", allAgent);
		msg.put("appInfo", appInfo);
		return msg;
	}

	@Override
	public int insertOrUpdate(AgentAppInfo appInfo) {
		int num = 0;
		if(appInfo != null){
			appInfo.setVersion("1.0");
			if(appInfo.getId() != null){
				num = agentAppInfoDao.update(appInfo);
			}else{
				num = agentAppInfoDao.insert(appInfo);
			}
		}
		return num;
	}

}
