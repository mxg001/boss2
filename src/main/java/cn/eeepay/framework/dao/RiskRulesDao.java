package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRules;
import cn.eeepay.framework.model.RiskRulesLog;
import cn.eeepay.framework.model.TeamInfo;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface RiskRulesDao {
	@Select("select * from risk_rules where status=1")
	@ResultType(RiskRules.class)
	List<RiskRules> selectAll();

	@Select("select * from risk_rules")
	@ResultType(RiskRules.class)
	List<RiskRules> selectAllWithOutStatus();
	
	@Select("select team_id,team_name from team_info where brand_type=1")
	@ResultType(TeamInfo.class)
	List<TeamInfo> getAllScope();
	
	@Select("select * from risk_rules where black_roll_no=#{roll} or white_roll_no=#{roll}")
	@ResultType(RiskRules.class)
	RiskRules selectByRoll(@Param("roll")String roll);
	
	@Select("SELECT * from risk_rules rr where rr.rules_no=#{id}")
	@ResultType(RiskRules.class)
	RiskRules selectDetail(@Param("id")int id);
	
	@Select("SELECT * from risk_rules rr where rr.rules_no=#{rulesNo}")
	@ResultType(RiskRules.class)
	RiskRules selectByRulesNo(@Param("rulesNo")Integer rulesNo);
	
	@Update("update risk_rules set rules_merchant_type=#{rr.rulesMerchantType},rules_team_ids=#{rr.rulesTeamIds},rules_values=#{rr.rulesValues}," +
			" treatment_measures=#{rr.treatmentMeasures},rules_provinces=#{rr.rulesProvinces},rules_city=#{rr.rulesCity},"
			+ "remark =#{rr.remark} where rules_no=#{rr.rulesNo}")
	int updateInfo(@Param("rr")RiskRules rr);
	
	@Insert("insert into risk_rules_log(rules_no,content,update_person) values(#{log.rulesNo},#{log.content},#{log.updatePerson})")
	int insertLogInfo(@Param("log")RiskRulesLog log);
	
	@Update("update risk_rules set `status`=#{rr.status} where rules_no=#{rr.rulesNo}")
	int updateStatus(@Param("rr")RiskRules rr);
	
	@Update("update risk_rules set rules_instruction=#{rr.rulesInstruction} where rules_no=#{rr.rulesNo}")
	int updateRulesInstruction(@Param("rr")RiskRules rr);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(RiskRules.class)
	List<RiskRules> selectAllInfo(@Param("page")Page<RiskRules> page,@Param("rr")RiskRules rr);
	
	@UpdateProvider(type = SqlProvider.class, method = "updateBatchForWhiteRoll")
	int updateBatchForWhiteRoll(@Param("list")List<RiskRules> list);


	public class SqlProvider {

		public String selectAllInfo(Map<String, Object> param) {
			final RiskRules rr = (RiskRules) param.get("rr");
			String sql = new SQL() {
				{
					SELECT("*");
					FROM("risk_rules rr");
					if (rr.getStatus() != null && rr.getStatus() != -1) {
						WHERE(" rr.status=#{rr.status}");
					}
					if (rr.getRulesInstruction() != null && rr.getRulesInstruction() != -1) {
						WHERE(" rr.rules_instruction=#{rr.rulesInstruction}");
					}
					if (rr.getRulesNo() != null) {
						WHERE(" rr.rules_no=#{rr.rulesNo}");
					}
				}
			}.toString();
			return sql;
		}

		public String updateBatchForWhiteRoll(Map<String, Object> param) {
			List<RiskRules> list = (List<RiskRules>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO risk_rules");
			sb.append("(rules_no,rules_engine,mer_white_roll,real_name_white_roll,wallet_white_roll)");
			sb.append("VALUES");
			//看到下面这行也许你会骂‘哪个傻子给rules_engine赋个错值’。因为不给它值sql会报错，就随便给给。前人所留，我加个注释提醒后人。
			MessageFormat mf = new MessageFormat("(#'{'list[{0}].rulesNo},#'{'list[{0}].rulesNo},#'{'list[{0}].merWhiteRoll},#'{'list[{0}].realNameWhiteRoll},#'{'list[{0}].walletWhiteRoll})");
			for (int i = 0; i < list.size(); i++) {
				sb.append(mf.format(new Object[] { i }));
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
			sb.append("on duplicate key update mer_white_roll=values(mer_white_roll),real_name_white_roll=values(real_name_white_roll),wallet_white_roll=values(wallet_white_roll)");
			System.out.println(sb.toString());
			return sb.toString();
		}

	}

}
