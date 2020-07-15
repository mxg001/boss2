package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentFunctionManager;
import cn.eeepay.framework.model.AgentInfo;

/**
 * 代理商功能管理
 * 
 * @author Administrator
 *
 */
public interface AgentFunctionManagerDao {

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(AgentFunctionManager.class)
	List<AgentFunctionManager> selectByParam(@Param("agentFunctionManager") AgentFunctionManager agentFunctionManager,
			@Param("page")Page<AgentFunctionManager> page);

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(AgentFunctionManager.class)
	List<AgentFunctionManager> exportConfig(@Param("agentFunctionManager") AgentFunctionManager agentFunctionManager);

	@Insert("Insert into agent_function_manage(agent_no,agent_name,create_time,create_user,function_number,team_id) "
			+ "values(#{agentFunctionManager.agentNo},#{agentFunctionManager.agentName},"
			+ "#{agentFunctionManager.createTime},#{agentFunctionManager.createUser},"
			+ "#{agentFunctionManager.functionNumber},#{agentFunctionManager.teamId})")
	int addAgentFunctionManager(@Param("agentFunctionManager") AgentFunctionManager agentFunctionManager);

	/**
	 * 从agent_info表中检查输入的代理商是否存在
	 * @return
	 */
	@Select("select * from agent_info where agent_no=#{agentInfo.agentNo} ")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findAgentInfo(@Param("agentInfo") AgentInfo agentInfo);

	@Select("select * from agent_info where agent_no=#{agentNo}")
	@ResultType(AgentInfo.class)
	AgentInfo findAgentInfoByAgentNo(String agentNo);

	/**
	 * 删除
	 * 
	 * @param agentNo
	 * @return
	 */
	@Delete("delete from agent_function_manage_blacklist where agent_no=#{agentNo} and function_number=#{functionNumber} and blacklist=#{blacklist}")
	int deleteInfoBlacklist(@Param("agentNo") String agentNo,@Param("functionNumber") String functionNumber,@Param("blacklist")Integer blacklist);

	/**
	 * 删除
	 *
	 * @param agentNo
	 * @return
	 */
	@Delete("delete from agent_function_manage where agent_no=#{agentNo} ")
	int deleteInfobyAgentNo(@Param("agentNo") String agentNo);

	@InsertProvider(type = SqlProvider.class, method = "addButchAgentFunctionManager")
	int addButchAgentFunctionManager(@Param("list") List<AgentFunctionManager> agentFunctionManager);

	@Select("select count(1) from agent_function_manage where agent_no = #{agentNo} and function_number=#{functionNumber}")
	@ResultType(Integer.class)
    int selectExists(AgentFunctionManager agentFunctionManager);

    public class SqlProvider {

		public String addButchAgentFunctionManager(Map<String, Object> param) {
			@SuppressWarnings("unchecked")
			final List<AgentFunctionManager> list = (List<AgentFunctionManager>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append(
					"insert into agent_function_manage(agent_no,agent_name,create_time,create_user,function_number,team_id) values ");
			MessageFormat message = new MessageFormat(
					"(#'{'list[{0}].agentNo},#'{'list[{0}].agentName},#'{'list[{0}].createTime},"
							+ "#'{'list[{0}].createUser},#'{'list[{0}].functionNumber},#'{'list[{0}].teamId}),");
			for (int i = 0; i < list.size(); i++) {
				sb.append(message.format(new Integer[] { i }));
			}
			sb.setLength(sb.length() - 1);
			System.out.println(sb.toString());
			return sb.toString();
		}
		
		public String addButchAgentFunctionManagerBlacklist(Map<String, Object> param) {
			@SuppressWarnings("unchecked")
			final List<AgentFunctionManager> list = (List<AgentFunctionManager>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append(
					"insert into agent_function_manage_blacklist(agent_no,agent_name,create_time,create_user,function_number,team_id,contains_lower,blacklist) values ");
			MessageFormat message = new MessageFormat(
					"(#'{'list[{0}].agentNo},#'{'list[{0}].agentName},#'{'list[{0}].createTime},"
							+ "#'{'list[{0}].createUser},#'{'list[{0}].functionNumber},#'{'list[{0}].teamId},#'{'list[{0}].containsLower},#'{'list[{0}].blacklist}),");
			for (int i = 0; i < list.size(); i++) {
				sb.append(message.format(new Integer[] { i }));
			}
			sb.setLength(sb.length() - 1);
			System.out.println(sb.toString());
			return sb.toString();
		}


		public String selectByParam(final Map<String, Object> param) {
			final AgentFunctionManager agentFunctionManager = (AgentFunctionManager) param.get("agentFunctionManager");
			return new SQL() {
				{
					SELECT(" id,team_id, agent_no, agent_name,function_number");
					FROM(" agent_function_manage ");
					//添加查询条件 tgh
					WHERE(" function_number=#{agentFunctionManager.functionNumber} ");
					if (agentFunctionManager.getAgentNo() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getAgentNo())) {
						WHERE(" agent_no=#{agentFunctionManager.agentNo} ");
					}
					if (agentFunctionManager.getAgentName() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getAgentName())) {
						agentFunctionManager.setAgentName("%" + agentFunctionManager.getAgentName() + "%");
						WHERE(" agent_name like #{agentFunctionManager.agentName} ");
					}
					if (agentFunctionManager.getTeamId() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getTeamId())) {
						WHERE(" team_id=#{agentFunctionManager.teamId} ");
					}
					/*
					Integer containsLower = agentFunctionManager.getContainsLower();
					if (containsLower != -1) {
						WHERE(" contains_lower=#{agentFunctionManager.containsLower} ");
					}
					Integer blacklist = agentFunctionManager.getBlacklist();
					if (blacklist != null) {
						WHERE(" blacklist=#{agentFunctionManager.blacklist} ");
					}else {
						WHERE(" blacklist=0 ");
					}
					*/
				}
			}.toString();
		}
		public String selectByParamBlacklist(final Map<String, Object> param) {
			final AgentFunctionManager agentFunctionManager = (AgentFunctionManager) param.get("agentFunctionManager");
			return new SQL() {
				{
					SELECT(" id,team_id, agent_no, agent_name,function_number,contains_lower,blacklist ");
					FROM(" agent_function_manage_blacklist ");
					//添加查询条件 tgh
					WHERE(" function_number=#{agentFunctionManager.functionNumber} ");
					if (agentFunctionManager.getAgentNo() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getAgentNo())) {
						WHERE(" agent_no=#{agentFunctionManager.agentNo} ");
					}
					if (agentFunctionManager.getAgentName() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getAgentName())) {
						agentFunctionManager.setAgentName("%" + agentFunctionManager.getAgentName() + "%");
						WHERE(" agent_name like #{agentFunctionManager.agentName} ");
					}
					if (agentFunctionManager.getTeamId() != null
							&& StringUtils.isNotBlank(agentFunctionManager.getTeamId())) {
						WHERE(" team_id=#{agentFunctionManager.teamId} ");
					}
					
					Integer containsLower = agentFunctionManager.getContainsLower();
					if (containsLower != -1) {
						WHERE(" contains_lower=#{agentFunctionManager.containsLower} ");
					}
					Integer blacklist = agentFunctionManager.getBlacklist();
					if (blacklist != null) {
						WHERE(" blacklist=#{agentFunctionManager.blacklist} ");
					}else {
						WHERE(" blacklist=0 ");
					}
					 
				}
			}.toString();
		}
	}

    @Select("select * from agent_function_manage where id = #{id}")
	AgentFunctionManager selectById(Integer id);

    @Delete(" delete from agent_function_manage where id = #{id} ")
	int delete(Integer id);

	@Select("select * from agent_info where agent_no=#{agentNo} and agent_level=1 ")
	@ResultType(AgentInfo.class)
	AgentInfo findAgentInfoByAgentNoOneLevel(String agentNo);

	@Select("SELECT " + 
			"	count( * )  " + 
			"FROM " + 
			"	function_manage f_mana " + 
			"	JOIN agent_function_manage_blacklist a_f_mana ON f_mana.function_number = a_f_mana.function_number " + 
			"	JOIN agent_info a_info ON a_info.agent_no = a_f_mana.agent_no " + 
			"	JOIN agent_info a_info2 ON a_info2.agent_node LIKE CONCAT( a_info.agent_node, '%' )  " + 
			"	AND f_mana.function_switch = 1  " + 
			"	AND f_mana.function_number = #{functionNumber}  " + 
			"	AND a_info2.agent_no = #{agentNo}  " + 
			"	AND a_f_mana.blacklist = 1  " + 
			"	AND a_f_mana.contains_lower = 1")
	long countAgentControlContains(@Param("agentNo")String agentNo,@Param("functionNumber")String functionNumber);

	@Delete(" delete from agent_function_manage where function_number = #{functionNumber} and agent_no = #{agentNo} ")
	void deleteByAgentNoFunNum(@Param("agentNo")String agentNo,@Param("functionNumber")String functionNumber);
	
	@Select("SELECT" + 
			"	count( * ) " + 
			"FROM" + 
			"	function_manage f_mana" + 
			"	JOIN agent_function_manage_blacklist a_f_mana ON f_mana.function_number = a_f_mana.function_number " + 
			"	AND f_mana.function_switch = 1 " + 
			"	AND f_mana.function_number = #{functionNumber} " + 
			"	AND a_f_mana.agent_no = #{agentNo} " + 
			"	AND a_f_mana.blacklist = 1 " + 
			"	AND a_f_mana.contains_lower = 0")
	long countBlacklistNotContains(@Param("agentNo")String agentNo,@Param("functionNumber")String functionNumber);

	@Insert("Insert into agent_function_manage_blacklist(agent_no,agent_name,create_time,create_user,function_number,team_id,contains_lower,blacklist) "
			+ "values(#{agentFunctionManager.agentNo},#{agentFunctionManager.agentName},"
			+ "#{agentFunctionManager.createTime},#{agentFunctionManager.createUser},"
			+ "#{agentFunctionManager.functionNumber},#{agentFunctionManager.teamId},#{agentFunctionManager.containsLower},#{agentFunctionManager.blacklist})")
	int addAgentFunctionManagerBlacklist(@Param("agentFunctionManager")AgentFunctionManager agentFunctionManager);

	
	@InsertProvider(type = SqlProvider.class, method = "addButchAgentFunctionManagerBlacklist")
	void addButchAgentFunctionManagerBlacklist(@Param("list")List<AgentFunctionManager> list);
	
	@Select("select count(1) from agent_function_manage_blacklist where agent_no = #{agentNo} and function_number=#{functionNumber} and blacklist=#{blacklist}")
	@ResultType(Integer.class)
	int selectExistsBlacklist(AgentFunctionManager agentFunctionManager);
	
	@Delete("delete from agent_function_manage where agent_no=#{agentNo} and function_number=#{functionNumber} ")
	int deleteInfo(@Param("agentNo")String agentNo, @Param("functionNumber")String functionNumber);

	@SelectProvider(type = SqlProvider.class, method = "selectByParamBlacklist")
	@ResultType(AgentFunctionManager.class)
	List<AgentFunctionManager> selectByParamBlacklist(@Param("agentFunctionManager")AgentFunctionManager agentFunctionManager,@Param("page") Page<AgentFunctionManager> page);
	
	@SelectProvider(type = SqlProvider.class, method = "selectByParamBlacklist")
	@ResultType(AgentFunctionManager.class)
	List<AgentFunctionManager> exportConfigBlacklist(@Param("agentFunctionManager")AgentFunctionManager agentFunctionManager);

	 @Delete(" delete from agent_function_manage_blacklist where id = #{id} ")
	int deleteBlacklist(Integer id);

	 @Select("select * from agent_function_manage_blacklist where id = #{id}")
	AgentFunctionManager selectBlacklistById(Integer id);

}