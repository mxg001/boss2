package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.SubTeam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.TeamInfoDao;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.TeamInfoService;

@Service("teamInfoService")
@Transactional
public class TeamInfoServiceImpl implements TeamInfoService {

	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Override
	public List<TeamInfo> selectTeamName() {
		return teamInfoDao.selectTeamName();
	}

	@Override
	public Map<String, List<SubTeam>> querySubTeams() {
		Map<String, List<SubTeam>> subTeamMap = new HashMap<>();
		List<SubTeam> subTeams = teamInfoDao.querySubTeams();
		if(null != subTeams && subTeams.size() > 0){
			for (SubTeam subTeam : subTeams) {
				String teamId = subTeam.getTeamId();
				if(subTeamMap.containsKey(teamId)){
					List<SubTeam> temp = subTeamMap.get(teamId);
					temp.add(subTeam);
					subTeamMap.put(teamId, temp);
				}else {
					List<SubTeam> temp = new ArrayList<>();
					temp.add(subTeam);
					subTeamMap.put(teamId, temp);
				}
			}
		}
		return subTeamMap;
	}
}
