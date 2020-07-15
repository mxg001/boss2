package cn.eeepay.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.ServiceManageRate;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.TradeSumInfoQo;
import cn.eeepay.framework.model.three.AgentNameInfo;
import cn.eeepay.framework.model.three.SumDo;
import cn.eeepay.framework.model.three.TeamInfoEntry;
import cn.eeepay.framework.model.three.ThreeIncomeVo;
import cn.eeepay.framework.model.three.ThreeSumVo;
import cn.eeepay.framework.model.three.TransServiceIdDo;
import cn.eeepay.framework.util.StringUtil;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface TradeSumInfoMapper {

	@Delete({ "delete from trade_sum_info", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into trade_sum_info (id, create_time, ", "update_time, agent_no, ", "branch, one_level, ",
			"two_level, three_level, ", "four_level, five_level, ", "trade_sum, mer_sum, ",
			"activate_sum, machines_stock, ", "unused_machines, expired_not_activated, ",
			"three_income, recorded_date, ", "recorded_status, team_id, ", "income_calc)",
			"values (#{id,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP}, ",
			"#{updateTime,jdbcType=TIMESTAMP}, #{agentNo,jdbcType=VARCHAR}, ",
			"#{branch,jdbcType=VARCHAR}, #{oneLevel,jdbcType=VARCHAR}, ",
			"#{twoLevel,jdbcType=VARCHAR}, #{threeLevel,jdbcType=VARCHAR}, ",
			"#{fourLevel,jdbcType=VARCHAR}, #{fiveLevel,jdbcType=VARCHAR}, ",
			"#{tradeSum,jdbcType=DECIMAL}, #{merSum,jdbcType=INTEGER}, ",
			"#{activateSum,jdbcType=INTEGER}, #{machinesStock,jdbcType=INTEGER}, ",
			"#{unusedMachines,jdbcType=INTEGER}, #{expiredNotActivated,jdbcType=INTEGER}, ",
			"#{threeIncome,jdbcType=DECIMAL}, #{recordedDate,jdbcType=TIMESTAMP}, ",
			"#{recordedStatus,jdbcType=INTEGER}, #{teamId,jdbcType=VARCHAR}, ", "#{incomeCalc,jdbcType=INTEGER})" })
	int insert(TradeSumInfo record);

	@InsertProvider(type = TradeSumInfoSqlProvider.class, method = "insertSelective")
	int insertSelective(TradeSumInfo record);

	@Select({ "select", "id, create_time, update_time, agent_no, branch, one_level, two_level, three_level, ",
			"four_level, five_level, trade_sum, mer_sum, activate_sum, machines_stock, unused_machines, ",
			"expired_not_activated, three_income, recorded_date, recorded_status, team_id, ", "income_calc",
			"from trade_sum_info", "where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "agent_no", property = "agentNo", jdbcType = JdbcType.VARCHAR),
			@Result(column = "branch", property = "branch", jdbcType = JdbcType.VARCHAR),
			@Result(column = "one_level", property = "oneLevel", jdbcType = JdbcType.VARCHAR),
			@Result(column = "two_level", property = "twoLevel", jdbcType = JdbcType.VARCHAR),
			@Result(column = "three_level", property = "threeLevel", jdbcType = JdbcType.VARCHAR),
			@Result(column = "four_level", property = "fourLevel", jdbcType = JdbcType.VARCHAR),
			@Result(column = "five_level", property = "fiveLevel", jdbcType = JdbcType.VARCHAR),
			@Result(column = "trade_sum", property = "tradeSum", jdbcType = JdbcType.DECIMAL),
			@Result(column = "mer_sum", property = "merSum", jdbcType = JdbcType.INTEGER),
			@Result(column = "activate_sum", property = "activateSum", jdbcType = JdbcType.INTEGER),
			@Result(column = "machines_stock", property = "machinesStock", jdbcType = JdbcType.INTEGER),
			@Result(column = "unused_machines", property = "unusedMachines", jdbcType = JdbcType.INTEGER),
			@Result(column = "expired_not_activated", property = "expiredNotActivated", jdbcType = JdbcType.INTEGER),
			@Result(column = "three_income", property = "threeIncome", jdbcType = JdbcType.DECIMAL),
			@Result(column = "recorded_date", property = "recordedDate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "recorded_status", property = "recordedStatus", jdbcType = JdbcType.INTEGER),
			@Result(column = "team_id", property = "teamId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "income_calc", property = "incomeCalc", jdbcType = JdbcType.INTEGER) })
	TradeSumInfo selectByPrimaryKey(Long id);

	@UpdateProvider(type = TradeSumInfoSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(TradeSumInfo record);

	@Update({ "update trade_sum_info", "set create_time = #{createTime,jdbcType=TIMESTAMP},",
			"update_time = #{updateTime,jdbcType=TIMESTAMP},", "agent_no = #{agentNo,jdbcType=VARCHAR},",
			"branch = #{branch,jdbcType=VARCHAR},", "one_level = #{oneLevel,jdbcType=VARCHAR},",
			"two_level = #{twoLevel,jdbcType=VARCHAR},", "three_level = #{threeLevel,jdbcType=VARCHAR},",
			"four_level = #{fourLevel,jdbcType=VARCHAR},", "five_level = #{fiveLevel,jdbcType=VARCHAR},",
			"trade_sum = #{tradeSum,jdbcType=DECIMAL},", "mer_sum = #{merSum,jdbcType=INTEGER},",
			"activate_sum = #{activateSum,jdbcType=INTEGER},", "machines_stock = #{machinesStock,jdbcType=INTEGER},",
			"unused_machines = #{unusedMachines,jdbcType=INTEGER},",
			"expired_not_activated = #{expiredNotActivated,jdbcType=INTEGER},",
			"three_income = #{threeIncome,jdbcType=DECIMAL},", "recorded_date = #{recordedDate,jdbcType=TIMESTAMP},",
			"recorded_status = #{recordedStatus,jdbcType=INTEGER},", "team_id = #{teamId,jdbcType=VARCHAR},",
			"income_calc = #{incomeCalc,jdbcType=INTEGER}", "where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(TradeSumInfo record);

	@SuppressWarnings("unchecked")
	public class SqlProvider {
		public String tradeSumByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" sum( c_t_order.trans_amount ) AS trans_amount,a_i.agent_no AS agent_no,m_info.team_id AS team_id ");
					FROM(" agent_info a_i ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.agent_node LIKE CONCAT( a_i.agent_node, '%' ) ");
					JOIN(" merchant_info m_info ON m_info.merchant_no = c_t_order.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.agent_no", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_info.team_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					GROUP_BY(" a_i.agent_no,m_info.team_id ");
				}
			}.toString();
			return sql;
		}

		public String tradeSumByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamEntryIdList = (List<String>) params.get("teamEntryIdList");
			String sql = new SQL() {
				{
					SELECT(" sum( c_t_order.trans_amount ) AS trans_amount,a_i.agent_no AS agent_no,m_info.team_entry_id AS team_id ");
					FROM(" agent_info a_i ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.agent_node LIKE CONCAT( a_i.agent_node, '%' ) ");
					JOIN(" merchant_info m_info ON m_info.merchant_no = c_t_order.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.agent_no", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamEntryIdList != null && teamEntryIdList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_info.team_entry_id", "teamEntryIdList",
								teamEntryIdList.size());
						WHERE(inJoint);
					}
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					GROUP_BY(" agent_no,team_id ");
				}
			}.toString();
			return sql;
		}

		public String merSumByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" COUNT( DISTINCT m_i.merchant_no ) AS mer_sum,m_i.team_id AS team_id,a_info.one_level_id AS agent_no  ");
					FROM(" merchant_info m_i left join merchant_business_product m_b_p ON m_i.merchant_no = m_b_p.merchant_no ");
					JOIN(" agent_info a_info ON a_info.agent_no = m_i.agent_no ");
					JOIN(" terminal_info t_info ON t_info.merchant_no = m_b_p.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_info.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_i.team_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" m_i.create_time >= #{startTime} ");
					WHERE(" m_i.create_time <= #{endTime} ");
					GROUP_BY(" a_info.one_level_id,m_i.team_id ");
				}
			}.toString();
			return sql;
		}

		public String merSumByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamEntryIdList = (List<String>) params.get("teamEntryIdList");
			String sql = new SQL() {
				{
					SELECT(" COUNT( DISTINCT m_i.merchant_no ) AS mer_sum,m_i.team_entry_id AS team_id,a_info.one_level_id AS agent_no  ");
					FROM(" merchant_info m_i left join merchant_business_product m_b_p ON m_i.merchant_no = m_b_p.merchant_no ");
					JOIN(" agent_info a_info ON a_info.agent_no = m_i.agent_no ");
					JOIN(" terminal_info t_info ON t_info.merchant_no = m_b_p.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_info.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamEntryIdList != null && teamEntryIdList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_i.team_entry_id", "teamEntryIdList",
								teamEntryIdList.size());
						WHERE(inJoint);
					}
					WHERE(" m_i.create_time >= #{startTime} ");
					WHERE(" m_i.create_time <= #{endTime} ");
					GROUP_BY(" agent_no,team_id ");
				}
			}.toString();
			return sql;
		}

		public String activateSumByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" COUNT( DISTINCT m_i.merchant_no ) AS activate_sum,m_i.team_id AS team_id,a_info.one_level_id AS agent_no  ");
					FROM(" merchant_info m_i ");
					JOIN(" agent_info a_info ON a_info.agent_no = m_i.agent_no ");
					JOIN(" activity_detail a_d ON a_d.merchant_no = m_i.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_info.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_i.team_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" a_d.active_time >= #{startTime} ");
					WHERE(" a_d.active_time <= #{endTime} ");
					WHERE(" a_d.`status` != 1 ");
					GROUP_BY(" a_info.one_level_id,m_i.team_id ");
				}
			}.toString();
			return sql;
		}

		public String activateSumByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamEntryIdList = (List<String>) params.get("teamEntryIdList");
			String sql = new SQL() {
				{
					SELECT(" COUNT( DISTINCT m_i.merchant_no ) AS activate_sum,m_i.team_entry_id AS team_id,a_info.one_level_id AS agent_no  ");
					FROM(" merchant_info m_i ");
					JOIN(" agent_info a_info ON a_info.agent_no = m_i.agent_no ");
					JOIN(" activity_detail a_d ON a_d.merchant_no = m_i.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_info.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamEntryIdList != null && teamEntryIdList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_i.team_entry_id", "teamEntryIdList",
								teamEntryIdList.size());
						WHERE(inJoint);
					}
					WHERE(" a_d.active_time >= #{startTime} ");
					WHERE(" a_d.active_time <= #{endTime} ");
					WHERE(" a_d.`status` != 1 ");
					GROUP_BY(" agent_no,team_id ");
				}
			}.toString();
			return sql;
		}

		public String machinesStockByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" count(DISTINCT ti.sn) AS machines_stock,hp.org_id AS team_id,a_i.one_level_id AS agent_no  ");
					FROM(" terminal_info ti ");
					JOIN(" hardware_product hp ON ti.type = hp.hp_id ");
					JOIN(" agent_info a_i ON ti.agent_no = a_i.agent_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("hp.org_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
					GROUP_BY(" hp.org_id,a_i.one_level_id ");
				}
			}.toString();
			return sql;
		}

		public String machinesStockByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamEntryIdList = (List<String>) params.get("teamEntryIdList");
			String sql = new SQL() {
				{
					SELECT(" count(DISTINCT ti.sn) AS machines_stock,hp.team_entry_id AS team_id,a_i.one_level_id AS agent_no  ");
					FROM(" terminal_info ti ");
					JOIN(" hardware_product hp ON ti.type = hp.hp_id ");
					JOIN(" agent_info a_i ON ti.agent_no = a_i.agent_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamEntryIdList != null && teamEntryIdList.size() > 0) {
						String inJoint = StringUtil.inJoint("hp.team_entry_id", "teamEntryIdList",
								teamEntryIdList.size());
						WHERE(inJoint);
					}
					WHERE(" ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
					GROUP_BY(" hp.team_entry_id,a_i.one_level_id ");
				}
			}.toString();
			return sql;
		}

		public String unusedMachinesByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" count(DISTINCT ti.sn) AS unused_machines,hp.org_id AS team_id,a_i.one_level_id AS agent_no  ");
					FROM(" terminal_info ti ");
					JOIN(" hardware_product hp ON ti.type = hp.hp_id ");
					JOIN(" agent_info a_i ON ti.agent_no = a_i.agent_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("hp.org_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" ti.merchant_no IS NULL ");
					WHERE(" ti.open_status = 1 ");
					WHERE(" ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
					GROUP_BY(" hp.org_id,a_i.one_level_id ");
				}
			}.toString();
			return sql;
		}

		public String unusedMachinesByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamEntryIdList = (List<String>) params.get("teamEntryIdList");
			String sql = new SQL() {
				{
					SELECT(" count(DISTINCT ti.sn) AS unused_machines,hp.team_entry_id AS team_id,a_i.one_level_id AS agent_no  ");
					FROM(" terminal_info ti ");
					JOIN(" hardware_product hp ON ti.type = hp.hp_id ");
					JOIN(" agent_info a_i ON ti.agent_no = a_i.agent_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamEntryIdList != null && teamEntryIdList.size() > 0) {
						String inJoint = StringUtil.inJoint("hp.team_entry_id", "teamEntryIdList",
								teamEntryIdList.size());
						WHERE(inJoint);
					}
					WHERE(" ti.merchant_no IS NULL ");
					WHERE(" ti.open_status = 1 ");
					WHERE(" ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
					GROUP_BY(" hp.team_entry_id,a_i.one_level_id ");
				}
			}.toString();
			return sql;
		}

		public String expiredNotActivatedByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" SELECT ");
			sqlSb.append("   sum( t.count ) AS expired_not_activated, ");
			sqlSb.append("   t.team_id AS team_id, ");
			sqlSb.append("   t.agent_no AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   ( ");
			sqlSb.append(" SELECT ");
			sqlSb.append("   count( ti.sn ) AS count, ");
			sqlSb.append("   hp.org_id AS team_id, ");
			sqlSb.append("   a_i.one_level_id AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   terminal_info ti ");
			sqlSb.append("   JOIN hardware_product hp ON ti.type = hp.hp_id ");
			sqlSb.append("   JOIN agent_info a_i ON ti.agent_no = a_i.agent_no ");
			sqlSb.append("   JOIN activity_detail a_detail ON a_detail.merchant_no = ti.merchant_no  ");
			sqlSb.append("   AND a_detail.`status` = 1 ");
			sqlSb.append("   AND ti.CREATE_TIME <#{endTime} ");
			if (agentList != null && agentList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(" ( ");
				String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
				sb.append(inJoint);
				sb.append(" ) ");
				sqlSb.append("   AND ");
				sqlSb.append(sb.toString());
			}
			sqlSb.append("   AND hp.org_id='100070' ");
			sqlSb.append("   AND ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   team_id, ");
			sqlSb.append("   agent_no UNION ");
			sqlSb.append(" SELECT ");
			sqlSb.append("   count( ti.sn ) AS count, ");
			sqlSb.append("   hp.org_id AS team_id, ");
			sqlSb.append("   a_i.one_level_id AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   terminal_info ti ");
			sqlSb.append("   JOIN hardware_product hp ON ti.type = hp.hp_id ");
			sqlSb.append("   JOIN agent_info a_i ON ti.agent_no = a_i.agent_no ");
			sqlSb.append("   AND ti.open_status = 1 ");
			if (agentList != null && agentList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(" ( ");
				String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
				sb.append(inJoint);
				sb.append(" ) ");
				sqlSb.append("   AND ");
				sqlSb.append(sb.toString());
			}
			sqlSb.append("   AND hp.org_id='100070' ");
			sqlSb.append("   AND ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
			sqlSb.append("   AND ti.CREATE_TIME < #{endTime} ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   team_id, ");
			sqlSb.append("   agent_no ");
			sqlSb.append("   ) AS t  ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   team_id, ");
			sqlSb.append("   agent_no ");
			return sqlSb.toString();
		}

		public String expiredNotActivatedByListTeamEntry(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" SELECT ");
			sqlSb.append("   sum( t.count ) AS expired_not_activated, ");
			sqlSb.append("   t.team_id AS team_id, ");
			sqlSb.append("   t.agent_no AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   ( ");
			sqlSb.append(" SELECT ");
			sqlSb.append("   count( ti.sn ) AS count, ");
			sqlSb.append("    hp.team_entry_id AS team_id, ");
			sqlSb.append("   a_i.one_level_id AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   terminal_info ti ");
			sqlSb.append("   JOIN hardware_product hp ON ti.type = hp.hp_id ");
			sqlSb.append("   JOIN agent_info a_i ON ti.agent_no = a_i.agent_no ");
			sqlSb.append("   JOIN activity_detail a_detail ON a_detail.merchant_no = ti.merchant_no  ");
			sqlSb.append("   AND a_detail.`status` = 1 ");
			sqlSb.append("   AND ti.CREATE_TIME <#{endTime} ");
			if (agentList != null && agentList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(" ( ");
				String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
				sb.append(inJoint);
				sb.append(" ) ");
				sqlSb.append("   AND ");
				sqlSb.append(sb.toString());
			}
			sqlSb.append("   AND hp.org_id='100070' ");
			sqlSb.append("   AND ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   hp.team_entry_id, ");
			sqlSb.append("   a_i.one_level_id UNION ");
			sqlSb.append(" SELECT ");
			sqlSb.append("   count( ti.sn ) AS count, ");
			sqlSb.append("    hp.team_entry_id AS team_id, ");
			sqlSb.append("   a_i.one_level_id AS agent_no ");
			sqlSb.append(" FROM ");
			sqlSb.append("   terminal_info ti ");
			sqlSb.append("   JOIN hardware_product hp ON ti.type = hp.hp_id ");
			sqlSb.append("   JOIN agent_info a_i ON ti.agent_no = a_i.agent_no ");
			sqlSb.append("   AND ti.open_status = 1 ");
			if (agentList != null && agentList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(" ( ");
				String inJoint = StringUtil.inJoint("a_i.one_level_id", "agentList", agentList.size());
				sb.append(inJoint);
				sb.append(" ) ");
				sqlSb.append("   AND ");
				sqlSb.append(sb.toString());
			}
			sqlSb.append("   AND hp.org_id='100070' ");
			sqlSb.append("   AND ti.PSAM_NO NOT LIKE concat( 'jh', '%' ) ");
			sqlSb.append("   AND ti.CREATE_TIME < #{endTime} ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   hp.team_entry_id, ");
			sqlSb.append("   a_i.one_level_id ");
			sqlSb.append("   ) AS t  ");
			sqlSb.append(" GROUP BY ");
			sqlSb.append("   team_id, ");
			sqlSb.append("   agent_no ");
			return sqlSb.toString();
		}

		public String findAgentInfoByNoList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			return new SQL() {
				{
					SELECT(" a_i.agent_name AS agent_name,a_a_l.link_level AS link_level,a_i.agent_no AS agent_no  ");
					FROM(" agent_info a_i ");
					JOIN(" agent_authorized_link a_a_l ON a_i.agent_no = a_a_l.agent_link ");
					if (agentList != null && agentList.size() > 0) {

						String inJoint = StringUtil.inJoint("a_i.agent_no", "agentList", agentList.size());
						WHERE(inJoint);

					}
					WHERE(" a_a_l.record_check = 1 ");
					WHERE(" a_a_l.record_status = 1 ");
					WHERE(" a_a_l.link_level <= 5 ");
				}
			}.toString();
		}

		public String findTopAgentInfoByNoList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			return new SQL() {
				{
					SELECT(" a_i.agent_name AS agent_name,a_i.agent_no AS agent_no  ");
					FROM(" agent_info a_i ");
					JOIN(" agent_authorized_link a_a_l ON a_i.agent_no = a_a_l.agent_authorized ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.agent_no", "agentList", agentList.size());
						WHERE(inJoint);
					}
					WHERE(" a_a_l.record_check = 1 ");
					WHERE(" a_a_l.record_status = 1 ");
					WHERE(" a_a_l.is_top = 1 ");
				}
			}.toString();
		}

		public String findSumAgentNo(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" agent_no  ");
					FROM(" trade_sum_info ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("agent_no", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("team_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" create_time >= #{startTime} ");
					WHERE(" create_time <= #{endTime} ");
					GROUP_BY(" agent_no ");
					HAVING(" count( * ) > 0 ");
				}
			}.toString();
			return sql;
		}

		public String page(Map<String, Object> params) {
			final TradeSumInfoQo qo = (TradeSumInfoQo) params.get("qo");

			String sql = new SQL() {
				{
					StringBuilder builder = new StringBuilder();
					builder.append(" create_time,branch,one_level,two_level,three_level,four_level,five_level, ");
					builder.append(" sum( trade_sum ) as trade_sum, ");
					builder.append(" sum( mer_sum ) as mer_sum, ");
					builder.append(" sum( activate_sum ) as activate_sum, ");
					builder.append(" sum( machines_stock ) as machines_stock, ");
					builder.append(" sum( unused_machines ) as unused_machines, ");
					builder.append(" sum( expired_not_activated ) as expired_not_activated, ");
					builder.append(" sum( three_income ) as three_income, ");
					builder.append(" recorded_status, ");
					builder.append(" recorded_date ");
					SELECT(builder.toString());
					FROM(" trade_sum_info ");
					Integer incomeStatus = qo.getIncomeStatus();
					if (incomeStatus != null && incomeStatus != -1) {
						WHERE(" recorded_status = #{qo.incomeStatus} ");
						if (incomeStatus == 1) {
							String recordedStartTime = qo.getRecordedStartTime();
							if (StringUtils.hasLength(recordedStartTime)) {
								qo.setRecordedStartTime(recordedStartTime + " 00:00:00");
								WHERE(" recorded_date >= #{qo.recordedStartTime}  ");
							}
							String recordedEndTime = qo.getRecordedEndTime();
							if (StringUtils.hasLength(recordedEndTime)) {
								qo.setRecordedEndTime(recordedEndTime + " 23:59:59");
								WHERE(" recorded_date <= #{qo.recordedEndTime} ");
							}
						}
					}

					String agentOem = qo.getAgentOem();
					if (StringUtils.hasLength(agentOem)) {
						if (StringUtils.hasLength(qo.getTeamEntryId())) {
							WHERE("team_id=#{qo.teamEntryId}");
						} else {
							WHERE("team_id like concat(#{qo.agentOem},'%')");
						}
					}

					if (StringUtils.hasLength(qo.getStartTime())) {
						qo.setStartTime(qo.getStartTime() + " 00:00:00");
						WHERE(" create_time >= #{qo.startTime} ");
					}
					if (StringUtils.hasLength(qo.getEndTime())) {
						qo.setEndTime(qo.getEndTime() + " 23:59:59");
						WHERE(" create_time <= #{qo.endTime} ");
					}
					if (qo.getAgentNoList() != null && qo.getAgentNoList().size() > 0) {
						String inJoint = StringUtil.inJoint("agent_no", "qo.agentNoList", qo.getAgentNoList().size());
						WHERE(inJoint);
					}
					GROUP_BY(" agent_no,create_time ");
					ORDER_BY(" create_time DESC,id ");
				}
			}.toString();
			return sql;
		}

		public String sum(Map<String, Object> params) {
			final TradeSumInfoQo qo = (TradeSumInfoQo) params.get("qo");

			return new SQL() {
				{
					SELECT(" sum(trade_sum) as trade_sum,sum(mer_sum) as mer_sum,sum(activate_sum) as active_sum,sum( IF ( three_income > 0, three_income, 0 )) as three_income_sum ");
					FROM(" trade_sum_info t_s_info ");
					Integer incomeStatus = qo.getIncomeStatus();
					if (incomeStatus != null && incomeStatus != -1) {
						WHERE(" t_s_info.recorded_status = #{qo.incomeStatus} ");
						if (incomeStatus == 1) {
							String recordedStartTime = qo.getRecordedStartTime();
							if (StringUtils.hasLength(recordedStartTime)) {
								qo.setRecordedStartTime(recordedStartTime + " 00:00:00");
								WHERE(" t_s_info.recorded_date >= #{qo.recordedStartTime}  ");
							}
							String recordedEndTime = qo.getRecordedEndTime();
							if (StringUtils.hasLength(recordedEndTime)) {
								qo.setRecordedEndTime(recordedEndTime + " 23:59:59");
								WHERE(" t_s_info.recorded_date <= #{qo.recordedEndTime} ");
							}
						}
					}

					String agentOem = qo.getAgentOem();
					if (StringUtils.hasLength(agentOem)) {
						if (StringUtils.hasLength(qo.getTeamEntryId())) {
							WHERE("team_id=#{qo.teamEntryId}");
						} else {
							WHERE("team_id like concat(#{qo.agentOem},'%')");
						}
					}
					if (StringUtils.hasLength(qo.getStartTime())) {
						qo.setStartTime(qo.getStartTime() + " 00:00:00");
						WHERE(" t_s_info.create_time >= #{qo.startTime} ");
					}
					if (StringUtils.hasLength(qo.getEndTime())) {
						qo.setEndTime(qo.getEndTime() + " 23:59:59");
						WHERE(" t_s_info.create_time <= #{qo.endTime} ");
					}
					if (qo.getAgentNoList() != null && qo.getAgentNoList().size() > 0) {
						String inJoint = StringUtil.inJoint("t_s_info.agent_no", "qo.agentNoList",
								qo.getAgentNoList().size());
						WHERE(inJoint);
					}

				}
			}.toString();
		}

		public String sumRecorded(Map<String, Object> params) {
			final TradeSumInfoQo qo = (TradeSumInfoQo) params.get("qo");

			return new SQL() {
				{
					SELECT(" sum(three_income) as recorded_sum ");
					FROM(" trade_sum_info t_s_info ");
					Integer incomeStatus = qo.getIncomeStatus();
					if (incomeStatus != null && incomeStatus != -1) {
						WHERE(" t_s_info.recorded_status = #{qo.incomeStatus} ");
						if (incomeStatus == 1) {
							String recordedStartTime = qo.getRecordedStartTime();
							if (StringUtils.hasLength(recordedStartTime)) {
								qo.setRecordedStartTime(recordedStartTime + " 00:00:00");
								WHERE(" t_s_info.recorded_date >= #{qo.recordedStartTime}  ");
							}
							String recordedEndTime = qo.getRecordedEndTime();
							if (StringUtils.hasLength(recordedEndTime)) {
								qo.setRecordedEndTime(recordedEndTime + " 23:59:59");
								WHERE(" t_s_info.recorded_date <= #{qo.recordedEndTime} ");
							}
						}
					}
					WHERE(" t_s_info.recorded_status = 1");
					WHERE(" t_s_info.three_income >0");
					String agentOem = qo.getAgentOem();
					if (StringUtils.hasLength(agentOem)) {
						if (StringUtils.hasLength(qo.getTeamEntryId())) {
							WHERE("team_id=#{qo.teamEntryId}");
						} else {
							WHERE("team_id like concat(#{qo.agentOem},'%')");
						}
					}
					if (StringUtils.hasLength(qo.getStartTime())) {
						qo.setStartTime(qo.getStartTime() + " 00:00:00");
						WHERE(" t_s_info.create_time >= #{qo.startTime} ");
					}
					if (StringUtils.hasLength(qo.getEndTime())) {
						qo.setEndTime(qo.getEndTime() + " 23:59:59");
						WHERE(" t_s_info.create_time <= #{qo.endTime} ");
					}
					if (qo.getAgentNoList() != null && qo.getAgentNoList().size() > 0) {
						String inJoint = StringUtil.inJoint("t_s_info.agent_no", "qo.agentNoList",
								qo.getAgentNoList().size());
						WHERE(inJoint);
					}

				}
			}.toString();
		}

		public String findAllTransServiceIdByTeamId(Map<String, Object> params) {
			String teamId = params.get("teamId").toString();
			final boolean isTeamEntryId = teamId.contains("-");
			String sql = new SQL() {
				{
					SELECT(" sum( c_t_order.trans_amount ) AS trans_amount_sum,count( c_t_order.id ) AS num,c_t_order.service_id as service_id , c_t_order.card_type AS card_type,a_a_link.agent_node as agent_node  ");
					FROM(" agent_business_product a_b_pro ");
					JOIN(" business_product_define b_p_de ON a_b_pro.bp_id = b_p_de.bp_id ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.business_product_id = b_p_de.bp_id ");
					if (isTeamEntryId) {
						JOIN(" merchant_info mer_info on mer_info.merchant_no=c_t_order.merchant_no ");
					}
					JOIN(" agent_info a_info ON c_t_order.agent_node = a_info.agent_node ");
					JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link = a_info.one_level_id  ");
					//JOIN(" agent_info a_info ON c_t_order.agent_node LIKE CONCAT( a_info.agent_node, '%' ) ");
					//JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link =a_info.agent_no ");
					WHERE(" a_a_link.agent_node LIKE CONCAT( #{agentNode}, '%' ) ");
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					WHERE(" c_t_order.order_type != 3 ");
					WHERE(" a_b_pro.agent_no = #{childAgentNo} ");
					if (isTeamEntryId) {
						WHERE(" b_p_de.team_id= left(#{teamId},6) ");
						WHERE(" mer_info.team_entry_id= #{teamId} ");
					} else {
						WHERE(" b_p_de.team_id=#{teamId} ");
					}
					GROUP_BY("service_id,card_type,agent_node");
				}
			}.toString();
			return sql;
		}

		public String findAllSettleServiceIdByTeamId(Map<String, Object> params) {
			String teamId = params.get("teamId").toString();
			final boolean isTeamEntryId = teamId.contains("-");
			String sql = new SQL() {
				{
					SELECT(" sum( s_trans.amount ) AS trans_amount_sum,c_t_order.service_id AS service_id,c_t_order.card_type AS card_type,count( * ) AS num,a_a_link.agent_node as agent_node  ");
					FROM(" agent_business_product a_b_pro ");
					JOIN(" business_product_define b_p_de ON a_b_pro.bp_id = b_p_de.bp_id ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.business_product_id = b_p_de.bp_id ");
					if (isTeamEntryId) {
						JOIN(" merchant_info mer_info on mer_info.merchant_no=c_t_order.merchant_no ");
					}
					JOIN(" agent_info a_info ON c_t_order.agent_node = a_info.agent_node ");
					JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link = a_info.one_level_id  ");
					//JOIN(" agent_info a_info ON c_t_order.agent_node LIKE CONCAT( a_info.agent_node, '%' ) ");
					//JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link =a_info.agent_no ");
					JOIN(" settle_transfer s_trans ON c_t_order.order_no = s_trans.order_no ");
					WHERE(" a_a_link.agent_node LIKE CONCAT( #{agentNode}, '%' ) ");
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					WHERE(" a_b_pro.agent_no = #{childAgentNo} ");
					if (isTeamEntryId) {
						WHERE(" b_p_de.team_id= left(#{teamId},6) ");
						WHERE(" mer_info.team_entry_id= #{teamId} ");
					} else {
						WHERE(" b_p_de.team_id=#{teamId} ");
					}
					GROUP_BY("service_id,card_type,agent_node");
				}
			}.toString();
			return sql;
		}

		public String findManageRateByLinkService(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" s_m_rate.* ");
					FROM(" service_info s_info1 ");
					JOIN(" service_manage_rate s_m_rate ON s_m_rate.service_id = s_info1.link_service ");
					WHERE(" s_info1.service_id = #{serviceId} ");
					WHERE(" s_m_rate.agent_no = #{agentNo} ");
					WHERE(" (s_m_rate.card_type=#{cardType} or s_m_rate.card_type=0  ) ");
					WHERE(" s_m_rate.check_status=0 ");
					WHERE(" s_m_rate.lock_status=0 ");
				}
			}.toString();
			return sql;
		}

		public String findThreeIncomeVoList(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" sum( three_income ) AS three_income_sum,agent_no ");
					FROM(" trade_sum_info ");
					WHERE(" recorded_status = 0 ");
					WHERE(" income_calc = 1 ");
					WHERE(" create_time >= #{start} ");
					WHERE(" create_time <= #{end} ");
					GROUP_BY(" agent_no ");
				}
			}.toString();
			return sql;
		}

		public String findConfigInfo(Map<String, Object> params) {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" SELECT DISTINCT info.agent_no,info.agent_name ");
			sqlSb.append(" FROM ");
			sqlSb.append("   agent_authorized_link agent_link ");
			sqlSb.append(" JOIN agent_info info ON agent_link.agent_link = info.agent_no ");
			sqlSb.append(" AND agent_link.record_check = 1 ");
			sqlSb.append(" AND agent_link.record_status = 1 ");
			sqlSb.append(" AND agent_link.link_level <= 5 ");
			sqlSb.append(" UNION  ");
			sqlSb.append("   SELECT DISTINCT info.agent_no,info.agent_name ");
			sqlSb.append("   FROM ");
			sqlSb.append("     agent_authorized_link agent_link ");
			sqlSb.append("   JOIN agent_info info ON agent_link.agent_authorized = info.agent_no ");
			sqlSb.append("   AND agent_link.record_check = 1 ");
			sqlSb.append("   AND agent_link.link_level <= 5 ");
			sqlSb.append("   AND agent_link.record_status = 1 ");
			return sqlSb.toString();
		}

		public String findRuleByLinkService(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" a_s_rule.* ");
					FROM(" service_info s_info1 ");
					JOIN(" agent_share_rule a_s_rule ON a_s_rule.service_id = s_info1.link_service ");
					WHERE(" s_info1.service_id = #{serviceId} ");
					WHERE(" a_s_rule.agent_no = #{agentNo} ");
					WHERE(" (a_s_rule.card_type=#{cardType} or a_s_rule.card_type=0 ) ");
					WHERE(" a_s_rule.check_status=1 ");
					WHERE(" a_s_rule.lock_status=0 ");
					GROUP_BY(" agent_no ");
				}
			}.toString();
			return sql;
		}

		public String findConfigAgentNo(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" agent_link ");
					FROM(" agent_authorized_link t ");
					WHERE(" t.agent_authorized = #{agentNo} ");
					WHERE(" t.record_status=1 ");
					WHERE(" t.record_check=1 ");
					WHERE(" link_level <=5 ");
				}
			}.toString();
			return sql;
		}

		public String findAllTopAgentNo(Map<String, Object> params) {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("SELECT DISTINCT ");
			sqlSb.append("IF ( ");
			sqlSb.append("	( ");
			sqlSb.append("		SELECT ");
			sqlSb.append("			count(*) ");
			sqlSb.append("		FROM ");
			sqlSb.append("			agent_authorized_link ");
			sqlSb.append("		WHERE ");
			sqlSb.append("			agent_link = a_a_l_w.agent_authorized ");
			sqlSb.append("		AND record_status = 1 ");
			sqlSb.append("		AND record_check = 1 ");
			sqlSb.append("		AND link_level <= 5 ");
			sqlSb.append("	) > 0, ");
			sqlSb.append("	NULL, ");
			sqlSb.append("	a_a_l_w.agent_authorized ");
			sqlSb.append(") AS agent_no ");
			sqlSb.append("FROM ");
			sqlSb.append("	agent_authorized_link a_a_l_w ");
			sqlSb.append("WHERE ");
			sqlSb.append("	a_a_l_w.record_status = 1 ");
			sqlSb.append("AND a_a_l_w.record_check = 1 ");
			sqlSb.append("AND a_a_l_w.link_level <= 5");
			return sqlSb.toString();
		}

		public String findLookAgentNo(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" agent_link ");
					FROM(" agent_authorized_link t ");
					WHERE(" t.agent_authorized = #{agentNo} ");
					WHERE(" t.record_status=1 ");
					WHERE(" t.record_check=1 ");
					WHERE(" t.is_look=1 ");
					WHERE(" link_level <=5 ");
				}
			}.toString();
			return sql;
		}

		public String findAuthAgentNode(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" IF( agent_authorized = #{agentNoStr} AND is_top = 1, concat( agent_authorized, '-' ), concat( agent_node, '-' ) )  ");
					FROM(" agent_authorized_link ");
					WHERE(" agent_link= #{agentNoStr} OR ( agent_authorized = #{agentNoStr} AND is_top = 1 )  ");

				}
			}.toString();
			return sql + " LIMIT 1 ";
		}

		public String findRuleByAgentNoAndServiceId(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" * ");
					FROM(" agent_share_rule ");
					WHERE(" agent_no=#{agentNo} ");
					WHERE(" service_id=#{serviceId} ");
					WHERE(" (card_type=#{cardType} or card_type=0  ) ");
					WHERE(" check_status=1 ");
				}
			}.toString();
			return sql;
		}

		public String findByCreateTime(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" * ");
					FROM(" trade_sum_info ");
					WHERE(" create_time>=#{startTime} ");
					WHERE(" create_time<=#{endTime} ");
					WHERE(" income_calc=0 ");
				}
			}.toString();
			return sql;
		}

		public String findManageRateByAgentNoAndServiceId(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" * ");
					FROM(" service_manage_rate ");
					WHERE(" agent_no=#{agentNo} ");
					WHERE(" service_id=#{serviceId} ");
					WHERE(" (card_type=#{cardType} or card_type=0  ) ");
					WHERE(" check_status=0 ");
					WHERE(" lock_status=0 ");
				}
			}.toString();
			return sql;
		}

		public String updateRecorded(Map<String, Object> params) {
			String sql = new SQL() {
				{
					UPDATE(" trade_sum_info ");
					SET(" recorded_status=1,recorded_date=#{date} ");
					WHERE(" agent_no=#{agentNo} ");
					WHERE(" income_calc=1 ");
					WHERE(" create_time>= #{start} ");
					WHERE(" create_time<=#{end} ");
				}
			}.toString();
			return sql;
		}

		public String updateThreeIncome(TradeSumInfo tradeSumInfo) {
			String sql = new SQL() {
				{
					UPDATE(" trade_sum_info ");
					SET(" three_income=#{threeIncome},update_time=now(),income_calc=#{incomeCalc} ");
					WHERE(" id=#{id} ");
				}
			}.toString();
			return sql;
		}

	}

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "updateRecorded")
	Integer updateRecorded(@Param("date") Date date, @Param("agentNo") String agentNo, @Param("start") Date start,
			@Param("end") Date end);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "updateThreeIncome")
	Integer updateThreeIncome(TradeSumInfo tradeSumInfo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findManageRateByAgentNoAndServiceId")
	ServiceManageRate findManageRateByAgentNoAndServiceId(@Param("agentNo") String agentNo,
			@Param("serviceId") String serivceId, @Param("cardType") String cardType);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findByCreateTime")
	@ResultType(TradeSumInfo.class)
	List<TradeSumInfo> findByCreateTime(@Param("startTime") String startTimeStr, @Param("endTime") String endTimeStr);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findRuleByAgentNoAndServiceId")
	AgentShareRule findRuleByAgentNoAndServiceId(@Param("agentNo") String agentNoStr,
			@Param("serviceId") String serivceId, @Param("cardType") String cardType);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findAuthAgentNode")
	String findAuthAgentNode(@Param("agentNoStr") String agentNoStr);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findLookAgentNo")
	@ResultType(String.class)
	List<String> findLookAgentNo(@Param("agentNo") String agentNo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findAllTopAgentNo")
	@ResultType(String.class)
	List<String> findAllTopAgentNo();

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findConfigAgentNo")
	@ResultType(String.class)
	List<String> findConfigAgentNo(@Param("agentNo") String agentNo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findRuleByLinkService")
	AgentShareRule findRuleByLinkService(@Param("agentNo") String agentNoStr, @Param("serviceId") String serviceId,
			@Param("cardType") String cardType);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findConfigInfo")
	@ResultType(Map.class)
	List<Map<String, String>> findConfigInfo();

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findThreeIncomeVoList")
	@ResultType(ThreeIncomeVo.class)
	List<ThreeIncomeVo> findThreeIncomeVoList(@Param("start") Date parseDate, @Param("end") Date addMonths);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findManageRateByLinkService")
	ServiceManageRate findManageRateByLinkService(@Param("agentNo") String agentNo,
			@Param("serviceId") String serviceId, @Param("cardType") String cardType);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findAllTransServiceIdByTeamId")
	@ResultType(TransServiceIdDo.class)
	List<TransServiceIdDo> findAllTransServiceIdByTeamId(@Param("teamId") String teamId,
			@Param("childAgentNo") String childAgentNo, @Param("agentNode") String agentNode,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findAllSettleServiceIdByTeamId")
	@ResultType(TransServiceIdDo.class)
	List<TransServiceIdDo> findAllSettleServiceIdByTeamId(@Param("teamId") String teamId,
			@Param("childAgentNo") String childAgentNo, @Param("agentNode") String agentNode,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "sum")
	@ResultType(ThreeSumVo.class)
	ThreeSumVo sum(@Param("qo") TradeSumInfoQo qo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "sumRecorded")
	@ResultType(ThreeSumVo.class)
	ThreeSumVo sumRecorded(@Param("qo") TradeSumInfoQo qo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "page")
	@ResultType(TradeSumInfo.class)
	List<TradeSumInfo> list(@Param("qo") TradeSumInfoQo qo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "tradeSumByList")
	@ResultType(SumDo.class)
	List<SumDo> tradeSumByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "merSumByList")
	@ResultType(SumDo.class)
	List<SumDo> merSumByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "activateSumByList")
	@ResultType(SumDo.class)
	List<SumDo> activateSumByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "machinesStockByList")
	@ResultType(SumDo.class)
	List<SumDo> machinesStockByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "unusedMachinesByList")
	@ResultType(SumDo.class)
	List<SumDo> unusedMachinesByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "expiredNotActivatedByList")
	@ResultType(SumDo.class)
	List<SumDo> expiredNotActivatedByList(@Param("agentList") List<String> agentList, @Param("endTime") Date endTime);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findAgentInfoByNoList")
	@ResultType(AgentNameInfo.class)
	List<AgentNameInfo> findAgentInfoByNoList(@Param("agentList") List<String> agentList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findSumAgentNo")
	@ResultType(String.class)
	List<String> findSumAgentNo(@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("agentList") List<String> agentList, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "findTopAgentInfoByNoList")
	@ResultType(AgentNameInfo.class)
	List<AgentNameInfo> findTopAgentInfoByNoList(@Param("agentList") List<String> agentLis);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "page")
	@ResultType(TradeSumInfo.class)
	List<TradeSumInfo> page(Page<TradeSumInfo> page, @Param("qo") TradeSumInfoQo qo);

	@Select({ "SELECT ", "	*  ", "FROM ", "	team_info_entry  ", "WHERE ", "	team_id = #{teamId}" })
	List<TeamInfoEntry> findTeamEntryByTeamId(@Param("teamId") String teamId);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "tradeSumByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> tradeSumByListTeamEntry(@Param("agentList") List<String> agentList,
			@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("teamEntryIdList") List<String> teamEntryIdList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "merSumByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> merSumByListTeamEntry(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamEntryIdList") List<String> teamEntryIdList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "activateSumByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> activateSumByListTeamEntry(@Param("agentList") List<String> agentList,
			@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("teamEntryIdList") List<String> teamEntryIdList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "machinesStockByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> machinesStockByListTeamEntry(@Param("agentList") List<String> agentList,
			@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("teamEntryIdList") List<String> teamEntryIdList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "unusedMachinesByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> unusedMachinesByListTeamEntry(@Param("agentList") List<String> agentList,
			@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("teamEntryIdList") List<String> teamEntryIdList);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "expiredNotActivatedByListTeamEntry")
	@ResultType(SumDo.class)
	List<SumDo> expiredNotActivatedByListTeamEntry(@Param("agentList") List<String> agentList,
			@Param("endTime") Date endTime);

	@Select({ "SELECT DISTINCT ", //
			"	agent_s_r_0.*  ", //
			"FROM ", //
			"	agent_share_rule agent_s_r_0 ", //
			"	JOIN agent_authorized_link agent_a_l_0 ON ( agent_s_r_0.agent_no = agent_a_l_0.agent_authorized OR agent_s_r_0.agent_no = agent_a_l_0.agent_link )  ", //
			"	AND agent_a_l_0.agent_node LIKE concat( #{agentNode}, '%' )  ", //
			"	AND agent_s_r_0.check_status = 1" })
	List<AgentShareRule> findRuleByAgentNode(String agentNode);

	@Select({ "SELECT DISTINCT ", //
			"	service_m_r_0.*  ", //
			"FROM ", //
			"	service_manage_rate service_m_r_0 ", //
			"	JOIN agent_authorized_link agent_a_l_0 ON service_m_r_0.agent_no = agent_a_l_0.agent_link  ", //
			"	AND agent_a_l_0.agent_node LIKE concat(  #{agentNode}, '%' )  ", //
			"	AND service_m_r_0.check_status = 0  ", //
			"	AND service_m_r_0.lock_status =0" })
	List<ServiceManageRate> findManageRateByAgentNode(String agentNode);

	@Select({ "SELECT DISTINCT ", //
			"	s_info1.service_id as service_id,a_s_rule.*  ", //
			"FROM ", //
			"	service_info s_info1 ", //
			"	JOIN agent_share_rule a_s_rule ON a_s_rule.service_id = s_info1.link_service ", //
			"	JOIN agent_authorized_link agent_a_l_0 ON ( a_s_rule.agent_no = agent_a_l_0.agent_authorized OR a_s_rule.agent_no = agent_a_l_0.agent_link )  ", //
			"	AND agent_a_l_0.agent_node LIKE concat( #{agentNode}, '%' )  ", //
			"	AND a_s_rule.check_status = 1  ", //
			"	AND a_s_rule.lock_status = 0  " })
	List<AgentShareRule> findRuleByLinkServiceAndAgentNode(String agentNode);

	@Select({ "SELECT DISTINCT ", //
			"	s_info1.service_id as service_id,s_m_rate.*  ", //
			"FROM ", //
			"	service_info s_info1 ", //
			"	JOIN service_manage_rate s_m_rate ON s_m_rate.service_id = s_info1.link_service ", //
			"	JOIN agent_authorized_link agent_a_l_0 ON s_m_rate.agent_no = agent_a_l_0.agent_link  ", //
			"	AND agent_a_l_0.agent_node LIKE concat( #{agentNode}, '%' )  ", //
			"	AND s_m_rate.check_status = 0  ", //
			"	AND s_m_rate.lock_status = 0  " })
	List<ServiceManageRate> findManageRateByLinkServiceAndAgentNode(String agentNode);
}