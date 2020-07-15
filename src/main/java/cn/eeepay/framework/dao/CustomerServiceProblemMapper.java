package cn.eeepay.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CustomerServiceProblem;
import cn.eeepay.framework.model.CustomerServiceQo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface CustomerServiceProblemMapper {
	@Delete({ "delete from customer_service_problem", "where problem_id = #{problemId,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer problemId);

	@Insert({ "insert into customer_service_problem (problem_id, problem_type, ", "problem_name, problem_content, ",
			"app_scope, clicks, ", "create_time, update_time, ", "create_user, update_user, ", "problem_status)",
			"values (#{problemId,jdbcType=INTEGER}, #{problemType,jdbcType=VARCHAR}, ",
			"#{problemName,jdbcType=VARCHAR}, #{problemContent,jdbcType=VARCHAR}, ",
			"#{appScope,jdbcType=VARCHAR}, #{clicks,jdbcType=INTEGER}, ",
			"#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ",
			"#{createUser,jdbcType=VARCHAR}, #{updateUser,jdbcType=VARCHAR}, ", "#{problemStatus,jdbcType=INTEGER})" })
	int insert(CustomerServiceProblem record);

	@InsertProvider(type = CustomerServiceProblemSqlProvider.class, method = "insertSelective")
	int insertSelective(CustomerServiceProblem record);

	@Select({ "select", "problem_id, problem_type, problem_name, problem_content, app_scope, clicks, ",
			"create_time, update_time, create_user, update_user, problem_status", "from customer_service_problem",
			"where problem_id = #{problemId,jdbcType=INTEGER}" })
	@Results({ @Result(column = "problem_id", property = "problemId", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "problem_type", property = "problemType", jdbcType = JdbcType.VARCHAR),
			@Result(column = "problem_name", property = "problemName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "problem_content", property = "problemContent", jdbcType = JdbcType.VARCHAR),
			@Result(column = "app_scope", property = "appScope", jdbcType = JdbcType.VARCHAR),
			@Result(column = "clicks", property = "clicks", jdbcType = JdbcType.INTEGER),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "create_user", property = "createUser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_user", property = "updateUser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "problem_status", property = "problemStatus", jdbcType = JdbcType.INTEGER) })
	CustomerServiceProblem selectByPrimaryKey(Integer problemId);

	@UpdateProvider(type = CustomerServiceProblemSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(CustomerServiceProblem record);

	@Update({ "update customer_service_problem", "set problem_type = #{problemType,jdbcType=VARCHAR},",
			"problem_name = #{problemName,jdbcType=VARCHAR},", "problem_content = #{problemContent,jdbcType=VARCHAR},",
			"app_scope = #{appScope,jdbcType=VARCHAR},", "clicks = #{clicks,jdbcType=INTEGER},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
			"create_user = #{createUser,jdbcType=VARCHAR},", "update_user = #{updateUser,jdbcType=VARCHAR},",
			"problem_status = #{problemStatus,jdbcType=INTEGER}", "where problem_id = #{problemId,jdbcType=INTEGER}" })
	int updateByPrimaryKey(CustomerServiceProblem record);


	@SelectProvider(type = CustomerServiceProblemMapper.SqlProvider.class, method = "selectByQo")
	List<CustomerServiceProblem> selectByQo(@Param("qo") CustomerServiceQo qo);

	class SqlProvider {
		public String selectByQo(Map<String, Object> param) {
			final CustomerServiceQo qo = (CustomerServiceQo) param.get("qo");
			return new SQL() {
				{
					SELECT(" * ");
					FROM("customer_service_problem");
					WHERE(" problem_status=0 ");
					WHERE(" app_scope like concat('%',#{qo.appScope},'%')");
					Integer problemType = qo.getCustomServiceProblemType();
					if (problemType != null && problemType > 0) {
						WHERE(" problem_type = #{qo.customServiceProblemType}");
					}
					String problemName = qo.getProblemName();
					if (StringUtils.hasLength(problemName)) {
						WHERE(" problem_name like concat('%',#{qo.problemName},'%')");
					}
				}
			}.toString();
		}

	}

	@SelectProvider(type = CustomerServiceProblemMapper.SqlProvider.class, method = "selectByQo")
	List<CustomerServiceProblem> queryByQo(@Param("page") Page<CustomerServiceProblem> page,
			@Param("qo") CustomerServiceQo qo);

	@Update(" update customer_service_problem set problem_status=1,update_user=#{username},update_time=#{date} where problem_id=#{id}")
	int updateStatusById(@Param("id") String id, @Param("date") Date date, @Param("username") String username);
}