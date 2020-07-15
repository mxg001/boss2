package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.agentAuth.AgentAuth;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface AgentAuthorizedDao {

	//新增代理商的用户
	@Insert("insert into agent_authorized_link(record_code,agent_authorized,agent_link,record_status,record_creator,create_time,last_updat_time,top_agent,link_level,is_top,agent_node,is_look)" +
			" values(#{record_code},#{agent_authorized},#{agent_link},0,#{record_creator},now(),now(),#{top_agent},#{link_level},#{is_top},#{agent_node},0)")
	int addRecord(Map params);

	@InsertProvider(type = AgentAuthorizedDao.SqlProvider.class,method = "upRecord")
	int upRecord(Map params);

	@Update("UPDATE agent_authorized_link SET record_status=#{map.record_status},is_look=#{map.is_look},last_updat_time=now() where agent_node like concat(#{map.agent_node},'%')")
	int upCloseStatus(@Param("map") Map params);

	@Update("UPDATE agent_authorized_link SET is_look=#{map.is_look},last_updat_time=now() where record_code=#{map.record_code}")
	int upIsLook(@Param("map") Map params);

	@Delete("delete from agent_authorized_link where record_code=#{record_code}")
	int deRecord(@Param("record_code")String record_code);

	@Select("select * from agent_authorized_link where record_code=#{record_code}")
	Map getData(@Param("record_code")String record_code);

	@SelectProvider(type= AgentAuthorizedDao.SqlProvider.class, method="getDatas")
	@ResultType(Map.class)
	List<Map> getDatas(@Param("info") Map<String, Object> params, Page<Map> page);

	@SelectProvider(type= AgentAuthorizedDao.SqlProvider.class, method="getDatas")
	@ResultType(Map.class)
	List<Map<String, Object>> importDetailSelect(@Param("info")Map<String, Object> info);

	@Select(
			"select * from agent_authorized_link " +
					" where agent_authorized=#{info.agentAuthorized} and agent_link=#{info.agentLink} "
	)
	List<AgentAuth> checkSelectAgentAuth(@Param("info")AgentAuth info);

	//新增代理商的用户
	@Insert("insert into agent_authorized_link " +
			" (record_code,agent_authorized,agent_link,record_status,record_creator,create_time,top_agent,link_level,is_top) " +
			" values " +
			" (#{info.recordCode},#{info.agentAuthorized},#{info.agentLink},0,#{info.recordCreator},now(),#{topAgent},#{linkLevel},#{isTop})"
	)
	int addAgentAuth(@Param("info")AgentAuth info);

	@Select(
			"select aal.agent_link agentNo,al.agent_name agentName " +
					" from agent_authorized_link aal " +
					" INNER JOIN agent_info al on aal.agent_link = al.agent_no " +
					" where aal.agent_authorized=#{agentNoParent} "
//					"       and aal.record_check=1 and aal.record_status=1"
	)
	List<Map<String,String>> getCurrentLevel(@Param("agentNoParent")String agentNoParent);

	@SelectProvider(type=SqlProvider.class,method="selectTopAgentManagement")
	@ResultType(Map.class)
	List<Map<String, Object>> selectTopAgentManagement(@Param("map") Map<String, Object> map,@Param("page") Page page);

	@Select("select * from agent_authorized_link aal where aal.agent_link=#{agent_link}")
	@ResultType(Map.class)
	Map<String, Object> selectAgentAuthorizedAgentLink(@Param("agent_link") String agent_link);

	@Select("select aal.* from agent_authorized_link aal where aal.agent_authorized=#{agent_authorized}")
	@ResultType(Map.class)
	List<Map<String,Object>> getAgentAuthorized(@Param("agent_authorized") String agent_authorized);

	@Update("UPDATE agent_authorized_link SET top_agent=#{agent_authorized},link_level=0,is_top=1,last_updat_time=now() where agent_authorized=#{agent_authorized}")
	int updateTopAgentManagement(@Param("agent_authorized") String agent_authorized);

	@Delete("delete from agent_authorized_link where agent_authorized=#{agent_authorized}")
	int deleteTopAgentManagement(@Param("agent_authorized") String agent_authorized);

	@Insert("insert into agent_authorized_link " +
			" (record_code,agent_authorized,record_status,record_creator,create_time,top_agent,link_level,is_top) " +
			" values " +
			" (#{recordCode},#{agent_authorized},0,#{recordCreator},now(),#{agent_authorized},1,1)"
	)
	int addTopAgentManagement(@Param("agent_authorized") String agent_authorized,@Param("recordCode") String recordCode,@Param("recordCreator") String recordCreator);

	class SqlProvider{
		public String getDatas(final Map<String, Object> params){
			final Map<String, Object> map = (Map<String, Object>)params.get("info");
			return new SQL(){{
				SELECT("aal.*,aa.agent_name agent_authorized_name,al.agent_name agent_link_name");
				FROM("agent_authorized_link aal");
				LEFT_OUTER_JOIN("agent_info aa on aal.agent_authorized = aa.agent_no");
				LEFT_OUTER_JOIN("agent_info al on aal.agent_link = al.agent_no");
				Object obj;
				obj = map.get("agent_authorized");
				if(isNotBlank(obj)){
					WHERE("aal.agent_authorized=#{info.agent_authorized}");
				}
				obj = map.get("agent_link");
				if(isNotBlank(obj)){
					WHERE("aal.agent_link=#{info.agent_link}");
				}
				obj = map.get("record_check");
				if(isNotBlank(obj)){
					WHERE("aal.record_check=#{info.record_check}");
				}
				obj = map.get("is_top");
				if(isNotBlank(obj)){
					WHERE("aal.is_top=#{info.is_top}");
				}
				obj = map.get("top_agent");
				if(isNotBlank(obj)){
					WHERE("aal.top_agent=#{info.top_agent}");
				}
				ORDER_BY("aal.id desc");
			}}.toString();
		}

		public String upRecord(final Map<String, Object> params){
			final Map<String, Object> map = (Map<String, Object>)params.get("params");
			return new SQL(){{
				UPDATE("agent_authorized_link");
				WHERE("record_code=#{record_code}");
				Object obj;
				obj = params.get("check_user");
				if(isNotBlank(obj)){
					SET("check_user=#{check_user}");
				}
				obj = params.get("agent_link");
				if(isNotBlank(obj)){ SET("agent_link=#{agent_link}");
				}
				obj = params.get("record_check");
				if(isNotBlank(obj)){
					SET("record_check=#{record_check}");
				}
				obj = params.get("record_status");
				if(isNotBlank(obj)){
					SET("record_status=#{record_status}");
				}
				obj = params.get("agent_authorized");
				if(isNotBlank(obj)){
					SET("agent_authorized=#{agent_authorized}");
				}
				obj = params.get("is_look");
				if(isNotBlank(obj)){
					SET("is_look=#{is_look}");
				}
				obj = params.get("agent_node");
				if(isNotBlank(obj)){
					SET("agent_node=#{agent_node}");
				}
				obj = params.get("link_level");
				if(isNotBlank(obj)){
					SET("link_level=#{link_level}");
				}
			}}.toString();
		}

		public boolean isNotBlank(Object obj){
			return obj!=null && StringUtils.isNotBlank(String.valueOf(obj));
		}

		public String selectTopAgentManagement(final Map<String, Object> param) {
			final Map<String, Object> map = (Map<String, Object>) param.get("map");
			SQL sql = new SQL(){{
				SELECT("aal.agent_authorized,MIN(aal.create_time) create_time,aa.agent_name agent_authorized_name");
				FROM("agent_authorized_link aal");
				LEFT_OUTER_JOIN("agent_info aa on aal.agent_authorized = aa.agent_no");
				WHERE("aal.is_top=1");
				if(StringUtil.isNotBlank(map.get("agent_authorized"))){
					WHERE("aal.agent_authorized=#{map.agent_authorized}");
				}
				GROUP_BY("aal.agent_authorized");
			}};
			sql.ORDER_BY("aal.create_time DESC");
			return sql.toString();
		}
	}

	@Select("SELECT " + 
			"	count( * )  " + 
			"FROM " + 
			"	agent_authorized_link a_a_link " + 
			"	JOIN agent_authorized_link a_a_link2 ON a_a_link2.agent_link = a_a_link.agent_authorized  " + 
			"	AND a_a_link.record_code = #{recordCode}  " + 
			"	AND a_a_link2.record_check = 1  " + 
			"	AND a_a_link2.record_status = 1")
	long countCorrectChain(String recordCode);

	@Select("select count(*) from agent_authorized_link where record_code =#{record_code} and is_top=1 ")
	long countTop(String recordCode);

}