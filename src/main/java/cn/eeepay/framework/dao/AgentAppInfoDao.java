package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAppInfo;

public interface AgentAppInfoDao {

	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(AgentAppInfo.class)
	List<AgentAppInfo> selectByCondition(Page<AgentAppInfo> page,@Param("params") Map<String,Object> params);

	public class SqlProvider{
		public String selectByCondition(final Map<String, Object> params){
			final Map<String, Object> map = (Map<String, Object>)params.get("params");
			return new SQL(){{
				SELECT("app.id,app.team_id teamId, team.team_name teamName,app.agent_no agentNo, agent.agent_name agentName");
				FROM("agent_app_info app ");
				LEFT_OUTER_JOIN(" team_info team on app.team_id=team.team_id");
				LEFT_OUTER_JOIN(" agent_info agent on app.agent_no=agent.agent_no");
				if(map.get("teamId") !=null && !"0".equals(map.get("teamId"))){
					WHERE("app.team_id=#{params.teamId}");
					if(map.get("agentNo") !=null && !"0".equals(map.get("agentNo"))){
						WHERE("app.agent_no=#{params.agentNo}");
					}
				}
				ORDER_BY("app.id");
			}}.toString();
		}
	}

	@Select("select app.*, team.team_name teamName,agent.agent_name agentName from agent_app_info app "
			+ "left join team_info team on app.team_id=team.team_id left join agent_info agent on "
			+ "app.agent_no=agent.agent_no where app.id=#{id}")
	@ResultType(AgentAppInfo.class)
	AgentAppInfo selectDetailById(Long id);

	@Update("update agent_app_info set text=#{app.text},photo=#{app.photo},team_id=#{app.teamId},agent_no=#{app.agentNo},version=#{app.version},"
			+ "download_url=#{app.downloadUrl} where id=#{app.id}")
	int update(@Param("app")AgentAppInfo appInfo);

	@Insert("insert into agent_app_info(photo,text,agent_no,team_id,version,download_url) values(#{app.photo},#{app.text},#{app.agentNo},"
			+ "#{app.teamId},#{app.version},#{app.downloadUrl})")
	int insert(@Param("app")AgentAppInfo appInfo);
}
