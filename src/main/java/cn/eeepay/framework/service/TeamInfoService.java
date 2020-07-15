package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.SubTeam;
import cn.eeepay.framework.model.TeamInfo;

public interface TeamInfoService {

	List<TeamInfo> selectTeamName();

    Map<String, List<SubTeam>> querySubTeams();

}
