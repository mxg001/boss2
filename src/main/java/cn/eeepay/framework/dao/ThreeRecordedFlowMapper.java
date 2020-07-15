package cn.eeepay.framework.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.ThreeRecordedFlow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface ThreeRecordedFlowMapper {
    @Delete({ "delete from three_recorded_flow", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into three_recorded_flow (id, agent_no, ", "create_time, activte_amount, ",
			"recorded_status, recorded_sum, ", "from_serial_no, trans_order_no)",
			"values (#{id,jdbcType=BIGINT}, #{agentNo,jdbcType=VARCHAR}, ",
			"#{createTime,jdbcType=TIMESTAMP}, #{activteAmount,jdbcType=DECIMAL}, ",
			"#{recordedStatus,jdbcType=INTEGER}, #{recordedSum,jdbcType=DECIMAL}, ",
			"#{fromSerialNo,jdbcType=VARCHAR}, #{transOrderNo,jdbcType=VARCHAR})" })
	int insert(ThreeRecordedFlow record);

	@InsertProvider(type = ThreeRecordedFlowSqlProvider.class, method = "insertSelective")
	int insertSelective(ThreeRecordedFlow record);

	@Select({ "select", "id, agent_no, create_time, activte_amount, recorded_status, recorded_sum, from_serial_no, ",
			"trans_order_no", "from three_recorded_flow", "where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "agent_no", property = "agentNo", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "activte_amount", property = "activteAmount", jdbcType = JdbcType.DECIMAL),
			@Result(column = "recorded_status", property = "recordedStatus", jdbcType = JdbcType.INTEGER),
			@Result(column = "recorded_sum", property = "recordedSum", jdbcType = JdbcType.DECIMAL),
			@Result(column = "from_serial_no", property = "fromSerialNo", jdbcType = JdbcType.VARCHAR),
			@Result(column = "trans_order_no", property = "transOrderNo", jdbcType = JdbcType.VARCHAR) })
	ThreeRecordedFlow selectByPrimaryKey(Long id);

	@UpdateProvider(type = ThreeRecordedFlowSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(ThreeRecordedFlow record);

	@Update({ "update three_recorded_flow", "set agent_no = #{agentNo,jdbcType=VARCHAR},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "activte_amount = #{activteAmount,jdbcType=DECIMAL},",
			"recorded_status = #{recordedStatus,jdbcType=INTEGER},", "recorded_sum = #{recordedSum,jdbcType=DECIMAL},",
			"from_serial_no = #{fromSerialNo,jdbcType=VARCHAR},", "trans_order_no = #{transOrderNo,jdbcType=VARCHAR}",
			"where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(ThreeRecordedFlow record);

    @Select("select sum(activte_amount) from three_recorded_flow where agent_no=#{agentNo} and recorded_status=1")
	BigDecimal sumRecordedAmountByAgentNo(String agentNo);
}