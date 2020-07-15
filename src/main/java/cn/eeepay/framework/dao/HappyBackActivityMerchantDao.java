package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.FilterPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
public interface HappyBackActivityMerchantDao {

	/**
	 * 欢乐返活跃商户活动查询列表
	 * @param page
	 * @param happyBackActivityMerchant
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityMerchant")
	@ResultType(HappyBackActivityMerchant.class)
	List<HappyBackActivityMerchant> selectHappyBackActivityMerchant(@Param("page") Page<HappyBackActivityMerchant> page,
																	@Param("info")HappyBackActivityMerchant happyBackActivityMerchant);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityMerchant")
	@ResultType(HappyBackActivityMerchant.class)
	List<HappyBackActivityMerchant> exportExcel(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant);

	@SelectProvider(type = SqlProvider.class, method = "countRewardAmount")
	@ResultType(BigDecimal.class)
	BigDecimal countRewardAmount(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant, @Param("status")String status);

	@SelectProvider(type = SqlProvider.class, method = "countDeductAmount")
	@ResultType(BigDecimal.class)
	BigDecimal countDeductAmount(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant,@Param("status")String status);

	/**
	 *分页查询活跃商户活动奖励商户
	 */
	@SelectProvider(type=SqlProvider.class,method="happyBackActivityMerchantRewardPage")
	@ResultType(HappyBackActivityMerchant.class)
	List<HappyBackActivityMerchant> happyBackActivityMerchantRewardPage(@Param("adCon") FilterDate adCon, @Param("filterPage") FilterPage filterPage);

	/**
	 *分页查询活跃商户活动扣款商户
	 */
	@SelectProvider(type=SqlProvider.class,method="happyBackActivityMerchantDeductPage")
	@ResultType(HappyBackActivityMerchant.class)
	List<HappyBackActivityMerchant> happyBackActivityMerchantDeductPage(@Param("adCon") FilterDate adCon);


	/**
	 *活跃商户活动指定时间内交易总量
	 */
	@SelectProvider(type=SqlProvider.class,method="happyBackMerTransMonthSum")
	@ResultType(Map.class)
	Map happyBackMerTransMonthSum(@Param("map") Map<String,String> map);


	@Update("update hlf_activity_merchant_order set target_status=#{info.targetStatus},target_time=#{info.targetTime}," +
			"total_amount=#{info.totalAmount},reward_amount=#{info.rewardAmount},reward_account_status=#{info.rewardAccountStatus}," +
			"reward_account_time=#{info.rewardAccountTime} " +
			"where id=#{info.id}")
	int updateHappyBackActivityMerchantOrderReward(@Param("info")HappyBackActivityMerchant info);

	@Update("update hlf_activity_merchant_order set deduct_amount=#{info.deductAmount},deduct_status=#{info.deductStatus}," +
			"deduct_time=#{info.deductTime} where id=#{info.id}")
	int updateHappyBackActivityMerchantOrderDeduct(@Param("info")HappyBackActivityMerchant info);

	@SelectProvider(type=SqlProvider.class,method="selectMerRewardAccountStatusByIds")
	@ResultType(Map.class)
	List<HappyBackActivityMerchant> selectMerRewardAccountStatusByIds(@Param("ids") String ids);

	class SqlProvider{
		public String selectHappyBackActivityMerchant(Map<String, Object> param) {
			final HappyBackActivityMerchant info = (HappyBackActivityMerchant) param.get("info");
			SQL sql = new SQL() {{
				SELECT(" a.*,amr.repeat_status,amr.activity_type_no," +
						"mi.team_id,mi.team_entry_id,team.team_name,teamEn.team_entry_name," +
						"ach.hard_id ");
				FROM("hlf_activity_merchant_order a");
				LEFT_OUTER_JOIN("hlf_activity_merchant_record amr on amr.merchant_no = a.merchant_no");
				LEFT_OUTER_JOIN("agent_info ai ON ai.agent_node = a.agent_node ");

				LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = a.merchant_no ");
				LEFT_OUTER_JOIN("team_info team ON team.team_id = mi.team_id");
				LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id");

				LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = a.merchant_no and ad.activity_code='009') ");
				LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");

			}};
			whereSql(param,sql,info);
			sql.ORDER_BY("a.active_time desc");
			return sql.toString();
		}
		public String countRewardAmount(Map<String, Object> param) {
			final HappyBackActivityMerchant info = (HappyBackActivityMerchant) param.get("info");
			SQL sql = new SQL() {{
				SELECT(" sum(a.reward_amount) ");
				FROM(" hlf_activity_merchant_order a");
				LEFT_OUTER_JOIN("hlf_activity_merchant_record amr on amr.merchant_no = a.merchant_no");
				LEFT_OUTER_JOIN("agent_info ai ON ai.agent_node = a.agent_node ");
				if (info.getHardId()!=null) {
					LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = a.merchant_no and ad.activity_code='009') ");
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
				}
				WHERE(" a.reward_account_status = #{status}");
			}};
			whereSql(param,sql,info);
			return sql.toString();
		}
		public String countDeductAmount(Map<String, Object> param) {
			final HappyBackActivityMerchant info = (HappyBackActivityMerchant) param.get("info");
			SQL sql = new SQL() {{
				SELECT(" sum(a.deduct_amount) ");
				FROM(" hlf_activity_merchant_order a");
				LEFT_OUTER_JOIN("hlf_activity_merchant_record amr on amr.merchant_no = a.merchant_no");
				LEFT_OUTER_JOIN("agent_info ai ON ai.agent_node = a.agent_node ");
				if (info.getHardId()!=null) {
					LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = a.merchant_no and ad.activity_code='009') ");
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
				}
				WHERE(" a.deduct_status = #{status}");
			}};
			whereSql(param,sql,info);
			return sql.toString();
		}

		private void whereSql(Map<String, Object> param,SQL sql,final HappyBackActivityMerchant info) {
			if (StringUtils.isNotBlank(info.getActiveOrder())) {
				sql.WHERE(" FIND_IN_SET(a.active_order,#{info.activeOrder})");
			}
			if (StringUtils.isNotBlank(info.getTargetStatus())) {
				sql.WHERE(" a.target_status = #{info.targetStatus}");
			}
			if (StringUtils.isNotBlank(info.getRewardAccountStatus())) {
				sql.WHERE(" a.reward_account_status = #{info.rewardAccountStatus}");
			}
			if (StringUtils.isNotBlank(info.getDeductStatus())) {
				sql.WHERE(" a.deduct_status = #{info.deductStatus}");
			}
			if (StringUtils.isNotBlank(info.getRepeatStatus())) {
				sql.WHERE(" amr.repeat_status = #{info.repeatStatus}");
			}
			if (info.getRewardAmountMin() != null) {
				sql.WHERE(" a.reward_amount >= #{info.rewardAmountMin}");
			}
			if (info.getRewardAmountMax() != null) {
				sql.WHERE(" a.reward_amount <= #{info.rewardAmountMax}");
			}
			if (info.getDeductAmountMin() != null) {
				sql.WHERE(" a.deduct_amount >= #{info.deductAmountMin}");
			}
			if (info.getDeductAmountMax() != null) {
				sql.WHERE(" a.deduct_amount <= #{info.deductAmountMax}");
			}
			if (StringUtils.isNotBlank(info.getMerchantNo())) {
				sql.WHERE(" a.merchant_no = #{info.merchantNo}");
			}
			if (StringUtils.isNotBlank(info.getAgentNo())) {
				sql.WHERE(" ai.agent_no = #{info.agentNo}");
			}
			if (StringUtils.isNotBlank(info.getOneAgentNo())) {
				sql.WHERE(" ai.one_level_id = #{info.oneAgentNo}");
			}
			if (StringUtils.isNotBlank(info.getActiveTimeStart())) {
				sql.WHERE(" a.active_time >= #{info.activeTimeStart}");
			}
			if (StringUtils.isNotBlank(info.getActiveTimeEnd())) {
				sql.WHERE(" a.active_time <= #{info.activeTimeEnd}");
			}
			if (StringUtils.isNotBlank(info.getTargetTimeStart())) {
				sql.WHERE(" a.target_time >= #{info.targetTimeStart}");
			}
			if (StringUtils.isNotBlank(info.getTargetTimeEnd())) {
				sql.WHERE(" a.target_time <= #{info.targetTimeEnd}");
			}
			if (StringUtils.isNotBlank(info.getRewardAccountTimeStart())) {
				sql.WHERE(" a.reward_account_time >= #{info.rewardAccountTimeStart}");
			}
			if (StringUtils.isNotBlank(info.getRewardAccountTimeEnd())) {
				sql.WHERE(" a.reward_account_time <= #{info.rewardAccountTimeEnd}");
			}
			if (StringUtils.isNotBlank(info.getDeductTimeStart())) {
				sql.WHERE(" a.deduct_time >= #{info.deductTimeStart}");
			}
			if (StringUtils.isNotBlank(info.getDeductTimeEnd())) {
				sql.WHERE(" a.deduct_time <= #{info.deductTimeEnd}");
			}
			if (info.getHardId()!=null) {
				sql.WHERE(" ach.hard_id = #{info.hardId}");
			}
			if (StringUtils.isNotBlank(info.getActivityTypeNo())) {
				sql.WHERE(" amr.activity_type_no = #{info.activityTypeNo}");
			}
		}

		//奖励
		public String happyBackActivityMerchantRewardPage(final Map<String, Object> param){
			FilterDate adCon=(FilterDate)param.get("adCon");
			FilterPage filterPage=(FilterPage)param.get("filterPage");
			StringBuffer sb=new StringBuffer();
			sb.append("select");
			sb.append(" hamo.*,hamr.repeat_status,hamr.reward_type,hamr.reward_month,hamr.reward_amount rewardAmountConfig,hamr.reward_total_amount," +
					"hamr.deduct_type,hamr.deduct_month,hamr.deduct_amount deductAmountConfig,hamr.deduct_total_amount,ai.one_level_id oneAgentNo ");
			sb.append(" FROM hlf_activity_merchant_order hamo ");
			sb.append(" LEFT JOIN  hlf_activity_merchant_record hamr ON hamr.merchant_no = hamo.merchant_no ");
			sb.append(" LEFT JOIN  agent_info ai ON ai.agent_node = hamo.agent_node ");
			sb.append(" where 1=1 ");
			sb.append("   and hamo.reward_end_time>=#{adCon.endDate} ");
			sb.append("  ORDER BY hamo.id ");
			sb.append("  LIMIT "+filterPage.getStartPage()+","+filterPage.getLength());
			return sb.toString();
		}

		//扣款
		public String happyBackActivityMerchantDeductPage(final Map<String, Object> param){
			FilterDate adCon=(FilterDate)param.get("adCon");
			StringBuffer sb=new StringBuffer();
			sb.append("select");
			sb.append(" hamo.*,hamr.repeat_status,hamr.reward_type,hamr.reward_month,hamr.reward_amount rewardAmountConfig,hamr.reward_total_amount," +
					"hamr.deduct_type,hamr.deduct_month,hamr.deduct_amount deductAmountConfig,hamr.deduct_total_amount,ai.one_level_id oneAgentNo,ai2.agent_name oneAgentName ");
			sb.append(" FROM hlf_activity_merchant_order hamo ");
			sb.append(" LEFT JOIN  hlf_activity_merchant_record hamr ON hamr.merchant_no = hamo.merchant_no ");
			sb.append(" LEFT JOIN  agent_info ai ON ai.agent_node = hamo.agent_node ");
			sb.append(" LEFT JOIN  agent_info ai2 ON ai2.agent_no = ai.one_level_id ");
			sb.append(" where 1=1 ");
			sb.append("   and hamo.deduct_status!=1 and deduct_status!=2 ");
			sb.append("   and ((hamo.deduct_end_time>=#{adCon.startDate} and hamo.deduct_end_time<=#{adCon.endDate} and hamr.deduct_type=0) ");
			sb.append("   or (hamo.deduct_start_time<#{adCon.endDate} and #{adCon.startDate}<=hamo.deduct_end_time and hamr.deduct_type=1)) ");
			sb.append("  ORDER BY hamo.id ");
			return sb.toString();
		}

		//统计每月交易汇总
		public String happyBackMerTransMonthSum(final Map<String, Object> param){
			Map<String,String> map=(Map<String,String>)param.get("map");
			StringBuffer sb=new StringBuffer();
			sb.append("select merchant_no,sum(amount_count) total ");
			sb.append(" FROM merchant_trans_total_day ");
			sb.append(" where ");
			sb.append("   merchant_no=#{map.merchant_no} ");
			sb.append("   and day >= #{map.startDate} ");
			sb.append("   and day <= #{map.endDate} ");
			sb.append(" GROUP BY merchant_no  ");
			return sb.toString();
		}

		//批量奖励入账
		public String selectMerRewardAccountStatusByIds(final Map<String, Object> param){
			String ids=(String)param.get("ids");
			StringBuffer sb=new StringBuffer();
			sb.append("select");
			sb.append(" hamo.*,hamr.repeat_status,hamr.reward_type,hamr.reward_month,hamr.reward_amount rewardAmountConfig,hamr.reward_total_amount," +
					"hamr.deduct_type,hamr.deduct_month,hamr.deduct_amount deductAmountConfig,hamr.deduct_total_amount,ai.one_level_id oneAgentNo ");
			sb.append(" FROM hlf_activity_merchant_order hamo ");
			sb.append(" LEFT JOIN  hlf_activity_merchant_record hamr ON hamr.merchant_no = hamo.merchant_no ");
			sb.append(" LEFT JOIN  agent_info ai ON ai.agent_node = hamo.agent_node ");
			sb.append(" where hamo.target_status=1 and hamo.reward_account_status!=1");
			sb.append("   and hamo.id in ("+ids+") ");
			sb.append("  ORDER BY hamo.id ");
			return sb.toString();
		}
	}
}
