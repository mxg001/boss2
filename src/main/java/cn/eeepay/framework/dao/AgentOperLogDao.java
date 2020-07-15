package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLog;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * BossOperLog-DAO
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:25:31
 */
public interface AgentOperLogDao {

	//添加
    @Insert("insert into boss_oper_log(user_id,user_name,oper_code,request_method,request_params,"
    		+ "return_result, method_desc,oper_ip,oper_status,oper_time) "
    		+ "values (#{bossOperLog.user_id}, #{bossOperLog.user_name}, #{bossOperLog.oper_code}, #{bossOperLog.request_method}, #{bossOperLog.request_params},"
    		+ "#{bossOperLog.return_result},#{bossOperLog.method_desc},#{bossOperLog.oper_ip},#{bossOperLog.oper_status}, now())")
	int insert(@Param("bossOperLog") AgentOperLog agentOperLog);

	@SelectProvider(type=SqlProvider.class, method="queryByCondition")
	@Results(value ={
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "agent_no", column = "agent_no", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "agent_name", column = "agent_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "request_method", column = "request_method", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "method_desc", column = "method_desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "request_params", column = "request_params", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "return_result", column = "return_result", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "oper_ip", column = "oper_ip", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "oper_time", column = "oper_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
			})
	List<AgentOperLog> queryByCondition(Page page, @Param("logInfo") AgentOperLog logInfo);

	@SelectProvider(type=SqlProvider.class, method="queryByCondition")
	@Results(value ={
			@Result(id = true, property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "agent_no", column = "agent_no", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "agent_name", column = "agent_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "request_method", column = "request_method", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "method_desc", column = "method_desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "request_params", column = "request_params", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "return_result", column = "return_result", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "oper_ip", column = "oper_ip", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "oper_time", column = "oper_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
			})
	AgentOperLog queryDetail(@Param("logInfo") AgentOperLog logInfo);

	class SqlProvider{
		public String queryByCondition(Map<String, Object> param){
			AgentOperLog logInfo = (AgentOperLog) param.get("logInfo");
			StringBuffer sb = new StringBuffer("select ai.agent_name,ai.agent_no,aol.id,aol.request_method,aol.method_desc,aol.request_params,aol.return_result,aol.oper_ip,aol.oper_time")
					.append(" from agent_oper_log aol")
					.append(" LEFT JOIN agent_info ai on aol.agent_no = ai.agent_no")
					.append(" where 1=1");
			if(logInfo!=null){
				if (logInfo.getId() != null) {
					sb.append(" and aol.id = #{logInfo.id}");
				}
				if (StringUtils.isNotBlank(logInfo.getAgent_no())) {
					sb.append(" and aol.agent_no = #{logInfo.agent_no}");
				}
				if (StringUtils.isNoneBlank(logInfo.getAgent_name())) {
					sb.append(" and aol.agent_name = #{logInfo.agent_name}");
				}
				if (StringUtils.isNoneBlank(logInfo.getMethod_desc())) {
					sb.append(" and aol.method_desc like #{logInfo.method_desc}");
				}
				if (StringUtils.isNoneBlank(logInfo.getRequest_method())) {
					sb.append(" and aol.request_method = #{logInfo.request_method}");
				}
				if (logInfo.getOper_start_time() != null) {
					sb.append(" and aol.oper_time >= #{logInfo.oper_start_time}");
				}
				if (logInfo.getOper_end_time() != null) {
					sb.append(" and aol.oper_time <= #{logInfo.oper_end_time}");
				}
			}else{

			}

			sb.append(" order by aol.id desc");
			return sb.toString();
		}

	}
}
