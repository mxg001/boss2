package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface ActivityDetailDao {

	@Update("update activity_detail set billing_status=#{billingStatus},billing_msg=#{billingMsg},"
			+ "billing_time=now() where id =#{id}")
	int updateBillStatus(@Param("id")Integer id, @Param("billingStatus")int billingStatus,
						   @Param("billingMsg")String billingMsg);

	@Update("update activity_detail set billing_status=#{billingStatus},billing_msg=#{billingMsg},"
			+ "billing_time=now(), status='6' where id =#{id}")
	int updateBillStatusOk(@Param("id")Integer id, @Param("billingStatus")int billingStatus,
						 @Param("billingMsg")String billingMsg);


	@Select("SELECT IFNULL(aht.cash_back_amount,0) FROM activity_detail ad LEFT JOIN activity_hardware_type aht ON ad.activity_type_no = aht.activity_type_no where ad.id =#{id}")
	BigDecimal getMaxAmount(@Param("id")Integer id);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfo")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectAllInfo(@Param("page") Page<ActivityDetail> page,@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfo")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectAllInfoAll(@Param("activityDetail")ActivityDetail activityDetail);

	@Update("update activity_detail set discount_status=#{discountStatus},discount_operator=#{discountOperator},"
			+ "discount_time=now() where merchant_no=#{merchantNo} and active_order=#{activeOrder}")
	int updateDiscount(ActivityDetail info);
	
	@Delete("delete from activity_detail where status=#{status} and activity_code=#{activityCode}"
			+ " and merchant_no=#{merchantNo}")
	int deleteByCodeAndMer(ActivityDetail activityDetail);
	

	@SelectProvider(type=SqlProvider.class, method="selectListByParam")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectListByParam(@Param("merchantNo")String merchantNo,@Param("activityCodeList") String[] activityCodeList);
	
	/**
	 * by Ivan
	 * @param id
	 * @return ActivityDetail
	 */
	@Select("SELECT ad.merchant_no,ad.id,ad.activity_code,ad.active_time,ad.enter_time,ad.trans_total,ad.frozen_amout,ad. STATUS,ad.cash_time,ad.active_order,"+
		"ad.cash_order,ad.check_status,ad.discount_status,ad.liquidation_status,ad.account_check_status,ad.cash_back_amount,"
		+ "ai.agent_no,ai.agent_name,"+
		"ai.one_level_id oneAgentNo,oneAgent.agent_name oneAgentName,mi.merchant_name,ah.target_amout,"+
		"cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee "+
		"FROM activity_detail ad "+
		"LEFT OUTER JOIN agent_info ai ON ai.agent_node = ad.agent_node "+
		"LEFT OUTER JOIN agent_info oneAgent ON (oneAgent.agent_no = ai.one_level_id "+
		"AND ai.agent_node = ad.agent_node) "+
		"LEFT OUTER JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no "+
		"LEFT OUTER JOIN activity_hardware ah ON ah.id = ad.activity_id "+
		"LEFT OUTER JOIN collective_trans_order cto ON cto.order_no = ad.active_order " +
		"WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectActivityDetailById(@Param("id")int id);


	//定时任务查询需要的参数
	@Select(
			"select ad.*,ai.agent_no,ai.agent_name,oneAgent.agent_no oneAgentNo,oneAgent.agent_name oneAgentName, " +
					" cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee,mi.recommended_source " +
					" from activity_detail ad " +
					"  LEFT JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no " +
					"  LEFT JOIN agent_info ai ON ai.agent_no = mi.agent_no " +
					"  LEFT JOIN agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no " +
					"  LEFT JOIN collective_trans_order cto ON cto.order_no = ad.active_order " +
					" where ad.id=#{id} "
	)
	@ResultType(ActivityDetail.class)
	ActivityDetail getActivityDetail(@Param("id")int id);

	@Select("select ai.agent_name ,ai.agent_level,ai.parent_id,ai.agent_node," +
			"ai.cash_back_switch agent_cash_back_switch,ai.full_prize_switch," +
			"ai.not_full_deduct_switch,cbd.* " +
			"from cash_back_detail cbd " +
			"LEFT JOIN agent_info ai ON ai.agent_no = cbd.agent_no " +
			"where cbd.ad_id=#{id} and cbd.amount_type=#{amountType} " +
			"ORDER BY ABS(ai.agent_level) ")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> getCashBackDetailById(@Param("id")Integer id ,@Param("amountType") int amountType);

	@Update("update cash_back_detail set cash_back_switch=#{a.agentCashBackSwitch},entry_status=#{a.entryStatus},"
			+ "entry_time=now(),remark=#{a.remark},pre_transfer_status=#{a.preTransferStatus},pre_transfer_time=#{a.preTransferTime} where id =#{a.id}")
	int updateCashBackDetail(@Param("a") CashBackDetail cashBackDetail);

	/**
	 * by Ivan
	 * @param id
	 * @param status
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set check_status=#{status},check_operator=#{userId},"
			+ "check_time=now() where id =#{id}")
	int updateAdjustStatus(@Param("id")Integer id, @Param("status")String status,
			@Param("userId")String userId);
	
	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetail(@Param("page") Page<ActivityDetail> page, @Param("activityDetail")ActivityDetail activityDetail);


	@Select("SELECT ad.is_exclusion,ad.id,ad.active_order,ad.activity_code,ad.trans_total,ad.status,mi.merchant_name,mi.merchant_no,ai.agent_name,ai.agent_no,ad.cumulate_trans_amount,ah.activiy_name, " +
			" aht.activity_type_no,aht.activity_type_name,ah.hard_id," +
			" mi.team_id,mi.team_entry_id," +
			" team.team_name,teamEn.team_entry_name "+
			"FROM activity_detail ad "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_node = ad.agent_node "+
			"LEFT OUTER JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no "+
			"LEFT OUTER JOIN activity_hardware ah ON ah.id = ad.activity_id "+
			"LEFT OUTER JOIN activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no "+
			"LEFT OUTER JOIN team_info team ON team.team_id = mi.team_id "+
			"LEFT OUTER JOIN team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id "+
			"WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectHappyBackDetailById(@Param("id") Integer id);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetailAll( @Param("activityDetail")ActivityDetail activityDetail);

	@Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark,cbd.amount_type,cbd.pre_transfer_status,cbd.pre_transfer_time" +
			",ai.agent_name,ai.agent_level "+
			"FROM cash_back_detail cbd "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
			"WHERE cbd.ad_id=#{id} and cbd.amount_type=#{amountType} order by ai.agent_level asc")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> selectAgentReturnCashDetailAll(@Param("id")Integer id,@Param("amountType") int amountType);


	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackTotalAmount")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackTotalAmount(@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackTotalTransTotal")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackTotalTransTotal(@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackCashBackAmount")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackCashBackAmount(@Param("activityDetail")ActivityDetail activityDetail);
	
	/**
	 * 修改清算核算同意,且已返代理商
	 * @author tans
	 * @date 2017年6月27日 上午10:41:50
	 * @param id
	 * @param liquidationStatus
	 * @param userId 
	 * @return
	 */
	@Update("update activity_detail set liquidation_status=#{liquidationStatus},liquidation_time=now(),liquidation_operator=#{operator} where status='2' and id=#{id}")
	int updateAgreeLiquidationStatus(@Param("id")Integer id, @Param("liquidationStatus")String liquidationStatus, @Param("operator")Integer userId);

	@Update("update activity_detail set status='6' where status='2' and id=#{id}")
	int updateAgreeLiquidationStatusById(@Param("id")Integer id);
	
	/**
	 * 清算核算
	 * @author tans
	 * @date 2017年7月4日 上午11:55:23
	 * @param id
	 * @param liquidationStatus
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set liquidation_status=#{liquidationStatus},liquidation_time=now(),liquidation_operator=#{operator} where status='2' and id=#{id}")
	int updatLiquidationStatus(@Param("id")Integer id, @Param("liquidationStatus")String liquidationStatus, @Param("operator")Integer userId);
	
	/**
	 * 财务核算同意，且已返代理商
	 * @author tans
	 * @date 2017年6月27日 上午10:41:58
	 * @param id
	 * @param accountCheckStatus
	 * @return
	 */
	@Update("update activity_detail set account_check_status=#{accountCheckStatus},account_check_time=now(),account_check_operator=#{operator} "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAgreeAccountCheckStatus(@Param("id")Integer id, @Param("accountCheckStatus")String accountCheckStatus, @Param("operator")Integer userId);

	@Update("update activity_detail set status='6' "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAgreeAccountCheckStatusById(@Param("id")Integer id);

	/**
	 * 财务核算
	 * @author tans
	 * @date 2017年7月4日 下午12:06:45
	 * @param id
	 * @param accountCheckStatus
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set account_check_status=#{accountCheckStatus},account_check_time=now(),account_check_operator=#{operator} "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAccountCheckStatus(@Param("id")Integer id, @Param("accountCheckStatus")String accountCheckStatus, @Param("operator")Integer userId);


	/**
	 * 奖励入账更新  liuks
	 */
	@Update("update activity_detail set status=#{h.status},add_amount_time=now()"
			+ " where id=#{h.id}")
	int updateRewardIsBooked(@Param("h")ActivityDetail activityDetail);

	/**
	 * 欢乐返扣减更新  liuks
	 */
	@Update("update activity_detail set status=#{h.status},minus_amount_time=now()"
			+ " where id=#{h.id}")
	int updateRewardIsBookedMinus(@Param("h")ActivityDetail activityDetail);

	/**
	 * 查询活动商户表
	 */
	@Select("SELECT ad.*,mi.one_agent_no one_agent_no,ai.agent_name one_agent_name,ai.agent_type "+
				" FROM activity_detail ad "+
				"  LEFT JOIN  merchant_info mi ON mi.merchant_no = ad.merchant_no"+
			    "  LEFT JOIN agent_info ai ON mi.one_agent_no = ai.agent_no "+
				"  WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail getActivityDetailById(@Param("id")int id);


	/**
	 * 更新欢乐返商户的累计值，并标注已达标  liuks
	 */
	@Update("update activity_detail set "+
			"   cumulate_trans_amount=#{info.cumulateTransAmount}," +
			"   end_cumulate_time=NOW()," +
			"   is_standard=#{info.isStandard},"+
			"   standard_time=#{info.standardTime}"+
			" where id=#{info.id}")
	int updateActivityDetailForStandard(@Param("info")ActivityDetail ad);

	/**
	 * 更新欢乐返商户的累计值  liuks
	 */
	@Update("update activity_detail set "+
			"  cumulate_trans_amount=#{info.cumulateTransAmount}," +
			"  end_cumulate_time=NOW() " +
			" where id=#{info.id}")
	int updateActivityDetailForSum(@Param("info")ActivityDetail ad);

	/**
	 * 更新扣减商户统计值
	 */
	@Update("update activity_detail set"+
			"  cumulate_trans_min_amount=#{info.cumulateTransMinAmount}," +
			"  end_cumulate_min_time=NOW() " +
			" where id=#{info.id}")
	int updateAdDeduction(@Param("info")ActivityDetail ad);

	@SelectProvider(type = SqlProvider.class, method = "selectHappySendOrderDetail")
	@ResultType(HappySendNewOrder.class)
	List<HappySendNewOrder> selectHappySendOrderDetail(@Param("page") Page<HappySendNewOrder> page,
													   @Param("happySendNewOrder") HappySendNewOrder happySendNewOrder);

	@SelectProvider(type = SqlProvider.class, method = "selectHappySendOrderDetail")
	@ResultType(HappySendNewOrder.class)
	List<HappySendNewOrder> selectHappySendOrderDetailAll(@Param("happySendNewOrder") HappySendNewOrder happySendNewOrder);

	@SelectProvider(type = SqlProvider.class, method = "selectHappySendOrderTotalAmount")
	@ResultType(Map.class)
	Map<String, Object> selectHappySendOrderTotalAmount(@Param("happySendNewOrder") HappySendNewOrder happySendNewOrder);

	@SelectProvider(method = "newHappyBackQuery",type =ActivityDetailDao.SqlProvider.class )
	List<NewHappyBackActivityVo> newHappyBackQuery(@Param("page")Page<NewHappyBackActivityVo> page, @Param("qo") NewHappyBackActivityQo qo);

	@SelectProvider(type = SqlProvider.class, method = "newHappyBackCount")
	@ResultType(Map.class)
	Map<String, Object> newHappyBackCount(@Param("qo") NewHappyBackActivityQo qo);

	@Select({"SELECT " ,
			"	agent_info0.agent_name AS agent_name, " ,
			"	xhlf_agent_a_d0.agent_no, " ,
			"	xhlf_agent_a_d0.agent_level, " ,
			"	xhlf_agent_a_d0.amount, " ,
			"	xhlf_agent_a_d0.account_status, " ,
			"	xhlf_agent_a_d0.remark, " ,
			"	xhlf_agent_a_d0.account_time  " ,
			"FROM " ,
			"	xhlf_agent_account_detail xhlf_agent_a_d0 " ,
			"	 JOIN agent_info agent_info0 ON agent_info0.agent_no = xhlf_agent_a_d0.agent_no  " ,
			"	AND xhlf_agent_a_d0.xhlf_activity_order_id =#{id}"})
	List<AgentAwardDetailVo> agentAwardDetail(@Param("id")Long id);

	@Select("<script>" +
			"SELECT * FROM xhlf_activity_merchant_order where id in " +
			"<foreach  collection=\"ids\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\" > " +
			" #{id} " +
			"</foreach >" +
			" and activity_target_status=1 and reward_account_status=0" +
			"</script>")
	List<XhlfActivityMerchantOrder> getXhlfMerOrderLists(@Param("ids")List<String> ids);

	@Select("<script>" +
			"SELECT * FROM xhlf_activity_order where id in " +
			"<foreach  collection=\"ids\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\" > " +
			" #{id} " +
			"</foreach >" +
			" and current_target_status=2 and reward_account_status=0" +
			"</script>")
	List<XhlfActivityOrder> getXhlfAgentOrderLists(@Param("ids")List<String> ids);

	public class SqlProvider {

		public String selectAllInfo(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT(" ad.id,ad.active_time,ad.enter_time,ad.trans_total,ad.frozen_amout,ad.status,ad.cash_time,"
							+ "ad.active_order,ad.cash_order,ad.check_status,ad.discount_status,ad.check_operator,"
							+ "ad.check_time,ad.discount_operator,ad.discount_time,"
							+ "sd1.sys_name checkStatusStr,"
							+ "sd2.sys_name statusStr,"
							+ "ai.agent_no,ai.agent_name,oneAgent.agent_no oneAgentNo,oneAgent.agent_name oneAgentName,"
							+ "mi.merchant_name,mi.merchant_no,"
							+ "ah.target_amout,ah.activiy_name,"
							+ "aht.activity_type_name,"
							+ "cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee,"
							+ "st.id settleTransferId,st.out_amount merchantOutAmount,st.fee_amount merchantFeeAmount,"
							+ "bsu1.real_name checkOperatorName,bsu2.real_name discountOperatorName");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
					LEFT_OUTER_JOIN("activity_hardware ah ON ah.id = ad.activity_id ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					LEFT_OUTER_JOIN("settle_transfer st on st.status=4 and st.correction = '0' and st.trans_id=ad.cash_order and st.settle_type = '2'");
					LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id=ad.check_operator");
					LEFT_OUTER_JOIN("boss_shiro_user bsu2 on bsu2.id=ad.discount_operator");
					//导出的时候，需要显示这两个状态的对应值，如果后面数据量大影响性能
					//那就在导出的时候再去查
					LEFT_OUTER_JOIN("sys_dict sd1 on sd1.sys_value=ad.check_status and sd1.sys_key='CHECK_STATUS'");
					LEFT_OUTER_JOIN("sys_dict sd2 on sd2.sys_value=ad.status and sd2.sys_key='ACTIVITY_STATUS'");
					WHERE("ad.activity_code='002'");
					
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" ad.active_order in ("+activityDetail.getActiveOrder()+")");
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if(activityDetail.getAcqOrgId()!=null){
						WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
					}
					if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
						WHERE(" oneAgent.agent_no=#{activityDetail.oneAgentNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						WHERE("(mi.merchant_name like concat(#{activityDetail.merchantN},'%') or mi.merchant_no = #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
						if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
							WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
						}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
						} else {    //所有商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
						}
					}
					if (activityDetail.getCheckStatus()!=null) {
						WHERE(" ad.check_status=#{activityDetail.checkStatus}");
					}
					if (activityDetail.getDiscountStatus()!=null && activityDetail.getDiscountStatus()!=-1) {
						WHERE(" ad.discount_status=#{activityDetail.discountStatus}");
					}
					if (activityDetail.getFrozenAmout()!=null) {
						WHERE(" ad.frozen_amout=#{activityDetail.frozenAmout}");
					}
					if (activityDetail.getActiveTimeStart()!=null) {
					WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
				}
					if (activityDetail.getActiveTimeEnd()!=null) {
						WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
					}

					if (activityDetail.getEnterTimeStart()!=null) {
						WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
					}
					if (activityDetail.getEnterTimeEnd()!=null) {
						WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
					}

					if (activityDetail.getCashTimeStart()!=null) {
						WHERE(" ad.cash_time>=#{activityDetail.cashTimeStart}");
					}
					if (activityDetail.getCashTimeEnd()!=null) {
						WHERE(" ad.cash_time<=#{activityDetail.cashTimeEnd}");
					}
					if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
						WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}
		
		public String selectListByParam(Map<String, Object> param){
			final String[] activityCodeList = (String[]) param.get("activityCodeList");
			String sql = new SQL(){{
				SELECT("id,activity_code");
				FROM("activity_detail");
				if(activityCodeList!=null && activityCodeList.length>0){
					MessageFormat message = new MessageFormat("#'{'activityCodeList[{0}]}");
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<activityCodeList.length; i++){
						sb.append(message.format(new Integer[]{i}));
						sb.append(",");
					}
					sb.setLength(sb.length()-1);
					WHERE("activity_code in (" + sb + ")");
				}
				WHERE("status <> 1");
				WHERE("merchant_no = #{merchantNo}");
			}}.toString();
			System.out.println(sql);
			return sql;
		}

		public String selectHappyBackTotalAmount(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IF(ad.status = 7,IFNULL(ad.empty_amount,0), 0)) totalEmptyAmount," +
							"SUM(IF(ad.status = 8,IFNULL(ad.empty_amount,0),0)) totalAdjustmentAmount," +
							"SUM(IF(ad.status = 9,IFNULL(ad.full_amount ,0),0)) totalFullAmount");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					//欢乐返活动硬件子类型关联表
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}

		public String selectHappyBackTotalTransTotal(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IFNULL(ad.trans_total,0)) totalTransTotal");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					//欢乐返活动硬件子类型关联表
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}
		
		public String selectHappyBackCashBackAmount(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IF(ad.billing_status = 0,IFNULL(cbd.cash_back_amount,0), 0)) cashBackAmountNotPay," +
							"SUM(IF(ad.billing_status = 1,IFNULL(cbd.cash_back_amount,0), 0)) cashBackAmountHavePay");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					//欢乐返活动硬件子类型关联表
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}
		
		public String selectHappyBackDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT(" ad.is_exclusion,ad.id,ad.active_order,ad.active_time,ad.activity_code,ad.enter_time,ad.overdue_time,"
							+ "ad.trans_total,ad.status,ad.liquidation_status,ad.liquidation_time,cbd.cash_back_amount,"
							+ "bsu1.real_name as liquidation_operator,bsu2.real_name as account_check_operator,"
							+ "ad.account_check_status,ad.account_check_time,ad.min_overdue_time,"
							+ "mi.merchant_name,mi.merchant_no,"
							+ "ach.hard_id,mi.team_id,mi.team_entry_id,"
							+ "aht.activity_type_name,aht.activity_type_no,"
							+" team.team_name,teamEn.team_entry_name, "
							+" cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee, "
							+ "ai.agent_no,ai.agent_name,oneAgent.agent_name oneAgentName,"
							+ "oneAgent.agent_no oneAgentNo,"
							+ "ad.cumulate_trans_amount, ad.end_cumulate_time, ad.cumulate_amount_minus, ad.cumulate_amount_add, ad.empty_amount, ad.full_amount, ad.is_standard,"
							+ "ad.standard_time, ad.minus_amount_time, ad.add_amount_time,mi.recommended_source,ad.repeat_register,ad.billing_time,ad.billing_msg,ad.billing_status"

							);
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("team_info team ON team.team_id = mi.team_id");
					LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id");
					//欢乐返活动硬件子类型关联表
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id=ad.liquidation_operator ");
					LEFT_OUTER_JOIN("boss_shiro_user bsu2 on bsu2.id=ad.account_check_operator ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}

		public String selectAgentReturnCashDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT("cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark"
									+ "ai.agent_name,ai.agent_level "
							);
					FROM("cash_back_detail cbd ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = cbd.agent_no ");
					WHERE(" aa.activity_type_no=? IN ('008','009','021') ");
					WHERE(" ad.cbd.active_order=#{activityDetail.activityCode}");
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" aa..active_order in ("+activityDetail.getActiveOrder()+")");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" ad.activity_code=#{activityDetail.activityCode}");
					}
					if(activityDetail.getAcqOrgId()!=null){
						WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
					}
					if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
						WHERE(" oneAgent.agent_no=#{activityDetail.oneAgentNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
						if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
							WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
						}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
						} else {    //所有商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
						}
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (activityDetail.getActiveTimeStart()!=null) {
						WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
					}
					if (activityDetail.getActiveTimeEnd()!=null) {
						WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
					}
					if (activityDetail.getEnterTimeStart()!=null) {
						WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
					}
					if (activityDetail.getEnterTimeEnd()!=null) {
						WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
					}
					if (activityDetail.getTransTotal()!=null) {
						WHERE(" ad.trans_total=#{activityDetail.transTotal}");
					}
					if (activityDetail.getCashBackAmount()!=null) {
						WHERE(" ad.cash_back_amount=#{activityDetail.cashBackAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
						WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
						WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
					}
					if (activityDetail.getLiquidationTimeStart()!=null) {
						WHERE(" ad.liquidation_time>=#{activityDetail.liquidationTimeStart}");
					}
					if (activityDetail.getLiquidationTimeEnd()!=null) {
						WHERE(" ad.liquidation_time<=#{activityDetail.liquidationTimeEnd}");
					}
					if(activityDetail.getMinCumulateTransAmount()!=null){
						WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
					}
					if (activityDetail.getMaxCumulateTransAmount()!=null){
						WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
					}
					if (activityDetail.getMinStandardTime()!=null){
						WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
					}
					if (activityDetail.getMaxStandardTime()!=null){
						WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
					}
					if (activityDetail.getMinMinusAmountTime()!=null){
						WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
					}
					if (activityDetail.getMaxMinusAmountTime()!=null){
						WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
					}
					if (activityDetail.getMinAddAmountTime()!=null){
						WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
					}
					if (activityDetail.getMaxAddAmountTime()!=null){
						WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
						WHERE(" ad.is_standard = #{activityDetail.isStandard}");
					}
					if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
						WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHappySendOrderTotalAmount(Map<String, Object> param) {
			final HappySendNewOrder happySendNewOrder = (HappySendNewOrder) param.get("happySendNewOrder");
			SQL sql = new SQL() {
				{
					SELECT(
							"SUM(IF(xamo.reward_account_status = 0,IFNULL(xamo.reward_amount,0), 0)) totalNotRewardAmount," +
									"SUM(IF(xamo.reward_account_status = 1,IFNULL(xamo.reward_amount,0), 0)) totalRewardAmount"
					);
					FROM("xhlf_activity_merchant_order xamo ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = xamo.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = xamo.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
					if(happySendNewOrder.getHardId()!=null){
						LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = xamo.merchant_no and ad.activity_code='009')  ");
						LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
					}
					if(StringUtils.isNotBlank(happySendNewOrder.getActivityTypeNo())){
						LEFT_OUTER_JOIN(" xhlf_activity_record rec ON rec.merchant_no=xamo.merchant_no ");
					}
					WHERE(" xamo.activity_target_status=1 ");
				}
			};
			whereMer(sql,happySendNewOrder);
			sql.ORDER_BY("xamo.id desc");
			return sql.toString();
		}

		public String selectHappySendOrderDetail(Map<String, Object> param) {
			final HappySendNewOrder happySendNewOrder = (HappySendNewOrder) param.get("happySendNewOrder");
			SQL sql = new SQL() {
				{
					SELECT(" xamo.* "
							+ ",ai.agent_no,ai.agent_name, " +
							" rec.activity_type_no, "+
							" mi.team_id,mi.team_entry_id,team.team_name,teamEn.team_entry_name,ach.hard_id," +
							"oneAgent.agent_name oneAgentName,oneAgent.agent_no oneAgentNo"
					);
					FROM("xhlf_activity_merchant_order xamo ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = xamo.merchant_no ");
					LEFT_OUTER_JOIN("team_info team ON team.team_id = mi.team_id");
					LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id");
					LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = xamo.merchant_no and ad.activity_code='009')  ");
					LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");

					LEFT_OUTER_JOIN(" xhlf_activity_record rec ON rec.merchant_no=xamo.merchant_no ");

					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = xamo.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");

					WHERE(" 1=1 ");
				}
			};
			whereMer(sql,happySendNewOrder);
			sql.ORDER_BY("xamo.active_time desc");
			return sql.toString();
		}

		public void whereMer(SQL sql, HappySendNewOrder happySendNewOrder) {
			if (StringUtils.isNotBlank(happySendNewOrder.getCheckIds())) {
				sql.WHERE(" xamo.id in (" + happySendNewOrder.getCheckIds() + ") ");
			}
			if(happySendNewOrder.getOrderNoList() != null && happySendNewOrder.getOrderNoList().size() > 0) {
				StringBuilder orderNolistSb = new StringBuilder();
				MessageFormat messageFormat = new MessageFormat("#'{'happySendNewOrder.orderNoList[{0}]},");
				for(int i = 0; i < happySendNewOrder.getOrderNoList().size(); i++) {
					orderNolistSb.append(messageFormat.format(new Integer[]{i}));
				}
				String orderNoListStr = orderNolistSb.substring(0, orderNolistSb.length() - 1);
				sql.WHERE("xamo.active_order in (" + orderNoListStr + ")");
			}
			if (StringUtils.isNotBlank(happySendNewOrder.getMerchantNo())) {
				sql.WHERE(" xamo.merchant_no=#{happySendNewOrder.merchantNo}");
			}
			if (StringUtils.isNotBlank(happySendNewOrder.getActivityTargetStatus())) {
				sql.WHERE(" xamo.activity_target_status=#{happySendNewOrder.activityTargetStatus}");
			}
			if (StringUtils.isNotBlank(happySendNewOrder.getRewardAccountStatus())) {
				sql.WHERE(" xamo.reward_account_status=#{happySendNewOrder.rewardAccountStatus}");
			}
			if (happySendNewOrder.getMinRewardAccountTime() != null) {
				sql.WHERE(" xamo.reward_account_time>=#{happySendNewOrder.minRewardAccountTime}");
			}
			if (happySendNewOrder.getMaxRewardAccountTime() != null) {
				sql.WHERE(" xamo.reward_account_time<=#{happySendNewOrder.maxRewardAccountTime}");
			}
			if (happySendNewOrder.getMinTargetTime() != null) {
				sql.WHERE(" xamo.activity_target_time>=#{happySendNewOrder.minTargetTime}");
			}
			if (happySendNewOrder.getMaxTargetTime() != null) {
				sql.WHERE(" xamo.activity_target_time<=#{happySendNewOrder.maxTargetTime}");
			}
			if (happySendNewOrder.getMinRewardEndTime() != null) {
				sql.WHERE(" xamo.reward_end_time>=#{happySendNewOrder.minRewardEndTime}");
			}
			if (happySendNewOrder.getMaxRewardEndTime() != null) {
				sql.WHERE(" xamo.reward_end_time<=#{happySendNewOrder.maxRewardEndTime}");
			}
			if (happySendNewOrder.getMinActiveTime() != null) {
				sql.WHERE(" xamo.active_time>=#{happySendNewOrder.minActiveTime}");
			}
			if (happySendNewOrder.getMaxActiveTime() != null) {
				sql.WHERE(" xamo.active_time<=#{happySendNewOrder.maxActiveTime}");
			}
			if (StringUtils.isNotBlank(happySendNewOrder.getActivityTypeNo())) {
				sql.WHERE(" rec.activity_type_no = #{happySendNewOrder.activityTypeNo}");
			}
			if (happySendNewOrder.getHardId()!=null) {
				sql.WHERE(" ach.hard_id = #{happySendNewOrder.hardId}");
			}


			//是否包含下级;
			if (StringUtils.isNotBlank(happySendNewOrder.getAgentN())) {
				if("1".equals(happySendNewOrder.getAgentGrade())){//不包含
					sql.WHERE(" ai.agent_node = #{happySendNewOrder.agentN} ");
				}else if("2".equals(happySendNewOrder.getAgentGrade())){//仅包含直属
					String angentNo=happySendNewOrder.getAgentN();
					List<String> list = StringUtil.strToList(angentNo,"-");
					String agentN=list.get(list.size()-1);
					//happySendNewOrder.setAgentN(agentN);
					sql.WHERE(
							" ai.parent_id="+agentN+"");
					//sql.WHERE(
					//		" (ai.agent_node like CONCAT(#{happySendNewOrder.agentN}, '%') and ai.agent_node <> #{happySendNewOrder.agentN})");
				}else{//包含所有
					sql.WHERE(" ai.agent_node like CONCAT(#{happySendNewOrder.agentN}, '%') ");
				}
			}
		}

		public String newHappyBackQuery(Map<String,Object> params) {
			NewHappyBackActivityQo qo = (NewHappyBackActivityQo) params.get("qo");

			StringBuilder selectSb = new StringBuilder();
			selectSb.append("	xhlf_order0.id AS id, " ) ;
			selectSb.append("	xhlf_order0.active_order AS active_order, " );
			selectSb.append("	xhlf_order0.active_time AS active_time, " ) ;
			selectSb.append("	xhlf_order0.target_amount as target_amount, " ) ;
			selectSb.append("	xhlf_order0.reward_amount as reward_amount, " ) ;
			selectSb.append("	xhlf_order0.current_cycle as current_cycle, " ) ;
			selectSb.append("	xhlf_order0.reward_start_time as reward_start_time, " ) ;
			selectSb.append("	xhlf_order0.reward_end_time as reward_end_time, " ) ;
			selectSb.append("	xhlf_order0.current_target_status as current_target_status, " ) ;
			selectSb.append("	xhlf_order0.current_target_time as current_target_time, " ) ;
			selectSb.append("	xhlf_order0.reward_account_status as reward_account_status, " ) ;
			selectSb.append("	xhlf_order0.reward_account_time as reward_account_time, " ) ;
			selectSb.append("	xhlf_order0.activity_target_status as activity_target_status, " ) ;
			selectSb.append("	xhlf_order0.activity_target_time as activity_target_time, " ) ;
			selectSb.append("	xhlf_order0.merchant_no as merchant_no, " ) ;
			selectSb.append("	xhlf_order0.agent_trans_total_type, " ) ;
			selectSb.append("	agent_in0.agent_no AS agent_no, " ) ;
			selectSb.append("	agent_in0.agent_name AS agent_name, ");
			selectSb.append("	rec.activity_type_no AS activity_type_no, " ) ;
			selectSb.append("	mi.team_id,mi.team_entry_id,team.team_name,teamEn.team_entry_name,ach.hard_id, ");
			selectSb.append("   oneAgent.agent_name AS oneAgentName,oneAgent.agent_no AS oneAgentNo");
			SQL sql = new SQL();
			sql.SELECT(selectSb.toString());
			sql.FROM("xhlf_activity_order xhlf_order0");
			sql.JOIN("agent_info agent_in0 ON xhlf_order0.agent_no = agent_in0.agent_no");
			sql.JOIN("merchant_info mi ON mi.merchant_no = xhlf_order0.merchant_no ");
			sql.LEFT_OUTER_JOIN("team_info team ON team.team_id = mi.team_id");
			sql.LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id = mi.team_entry_id");
			sql.LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = xhlf_order0.merchant_no and ad.activity_code='009') ");
			sql.LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
			sql.JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
			sql.LEFT_OUTER_JOIN(" xhlf_activity_record rec ON rec.merchant_no=xhlf_order0.merchant_no ");
			sql.LEFT_OUTER_JOIN(" activity_hardware_type har ON har.activity_type_no=rec.activity_type_no ");

			whereNew(sql,qo);
			sql.ORDER_BY("xhlf_order0.active_time desc,xhlf_order0.current_cycle asc");
			return sql.toString();
		}
		public String newHappyBackCount(Map<String,Object> params) {
			NewHappyBackActivityQo qo = (NewHappyBackActivityQo) params.get("qo");
			StringBuilder selectSb = new StringBuilder();
			selectSb.append("SUM(IF(xhlf_order0.reward_account_status = 0,IFNULL(xhlf_order0.reward_amount,0), 0)) totalNoReward," ) ;
			selectSb.append("SUM(IF(xhlf_order0.reward_account_status = 1,IFNULL(xhlf_order0.reward_amount,0), 0)) totalReward " ) ;
			SQL sql = new SQL();
			sql.SELECT(selectSb.toString());
			sql.FROM("xhlf_activity_order xhlf_order0");
			sql.JOIN("agent_info agent_in0 ON xhlf_order0.agent_no = agent_in0.agent_no");
			sql.LEFT_OUTER_JOIN(" xhlf_activity_record rec ON rec.merchant_no=xhlf_order0.merchant_no ");
			sql.LEFT_OUTER_JOIN(" activity_hardware_type har ON har.activity_type_no=rec.activity_type_no ");

			sql.LEFT_OUTER_JOIN("activity_detail ad ON (ad.merchant_no = xhlf_order0.merchant_no and ad.activity_code='009') ");
			if (qo.getHardId()!=null) {
				sql.LEFT_OUTER_JOIN("activity_hardware ach ON ach.id = ad.activity_id ");
			}

			sql.WHERE(" xhlf_order0.current_target_status in ('2','4')");
			whereNew(sql,qo);
			//sql.GROUP_BY("xhlf_order0.reward_account_status");
			return sql.toString();
		}

		public void whereNew(SQL sql, NewHappyBackActivityQo qo) {
			String activeOrder = qo.getActiveOrder();
			qo.setOrderNoList(StringUtil.strToList(activeOrder,","));

			if(qo.getOrderNoList() != null && qo.getOrderNoList().size() > 0) {
				StringBuilder orderNolistSb = new StringBuilder();
				MessageFormat messageFormat = new MessageFormat("#'{'qo.orderNoList[{0}]},");
				for(int i = 0; i < qo.getOrderNoList().size(); i++) {
					orderNolistSb.append(messageFormat.format(new Integer[]{i}));
				}
				String orderNoListStr = orderNolistSb.substring(0, orderNolistSb.length() - 1);
				sql.WHERE("xhlf_order0.active_order in (" + orderNoListStr + ")");
			}

			String currentCycle = qo.getCurrentCycle();
			if (org.springframework.util.StringUtils.hasLength(currentCycle)) {
				sql.WHERE("xhlf_order0.current_cycle=#{qo.currentCycle}");
			}

			if (qo.getActiveTimeStart() != null) {
				sql.WHERE("xhlf_order0.active_time >=#{qo.activeTimeStart}");
			}
			if (qo.getActiveTimeEnd() != null) {
				sql.WHERE("xhlf_order0.active_time <=#{qo.activeTimeEnd}");
			}

			if (qo.getRewardStartTimeStart() != null ) {
				sql.WHERE("xhlf_order0.reward_start_time >=#{qo.rewardStartTimeStart}");
			}
			if (qo.getRewardStartTimeEnd() != null) {
				sql.WHERE("xhlf_order0.reward_start_time <=#{qo.rewardStartTimeEnd}");
			}

			if (qo.getRewardEndTimeStart() != null ) {
				sql.WHERE("xhlf_order0.reward_end_time >=#{qo.rewardEndTimeStart}");
			}
			if (qo.getRewardEndTimeEnd() != null) {
				sql.WHERE("xhlf_order0.reward_end_time <=#{qo.rewardEndTimeEnd}");
			}

			if (org.springframework.util.StringUtils.hasLength(qo.getCurrentTargetStatus())) {
				sql.WHERE("xhlf_order0.current_target_status=#{qo.currentTargetStatus}");
			}

			if (qo.getCurrentTargetTimeStart() != null ) {
				sql.WHERE("xhlf_order0.current_target_time >=#{qo.currentTargetTimeStart}");
			}
			if (qo.getCurrentTargetTimeEnd() != null) {
				sql.WHERE("xhlf_order0.current_target_time <=#{qo.currentTargetTimeEnd}");
			}

			if (org.springframework.util.StringUtils.hasLength(qo.getRewardAccountStatus())) {
				sql.WHERE("xhlf_order0.reward_account_status=#{qo.rewardAccountStatus}");
			}

			if (qo.getRewardAccountTimeStart() != null ) {
				sql.WHERE("xhlf_order0.reward_account_time >=#{qo.rewardAccountTimeStart}");
			}
			if (qo.getRewardAccountTimeEnd() != null) {
				sql.WHERE("xhlf_order0.reward_account_time <=#{qo.rewardAccountTimeEnd}");
			}

			if (org.springframework.util.StringUtils.hasLength(qo.getActivityTargetStatus())) {
				sql.WHERE("xhlf_order0.activity_target_status=#{qo.activityTargetStatus}");
			}

			if (qo.getActivityTargetTimeStart() != null ) {
				sql.WHERE("xhlf_order0.activity_target_time >=#{qo.activityTargetTimeStart}");
			}
			if (qo.getActivityTargetTimeEnd() != null) {
				sql.WHERE("xhlf_order0.activity_target_time <=#{qo.activityTargetTimeEnd}");
			}

			if (org.springframework.util.StringUtils.hasLength(qo.getMerchantNo())) {
				sql.WHERE("xhlf_order0.merchant_no=#{qo.merchantNo}");
			}
			if (org.springframework.util.StringUtils.hasLength(qo.getSubType())) {
				sql.WHERE("har.sub_type=#{qo.subType}");
			}
			if (StringUtils.isNotBlank(qo.getActivityTypeNo())) {
				sql.WHERE(" rec.activity_type_no = #{qo.activityTypeNo}");
			}
			if (qo.getHardId()!=null) {
				sql.WHERE(" ach.hard_id = #{qo.hardId}");
			}

			//是否包含下级;
			if (StringUtils.isNotBlank(qo.getAgentNo())) {
				if("1".equals(qo.getContainsLower())){//不包含
					sql.WHERE(" agent_in0.agent_node = #{qo.agentNo} ");
				}else if("2".equals(qo.getContainsLower())){//仅包含直属
					String angentNo=qo.getAgentNo();
					List<String> list=StringUtil.strToList(angentNo,"-");
					String agentN=list.get(list.size()-1);
					//qo.setAgentNo(agentN);
					sql.WHERE(
							" agent_in0.parent_id="+agentN);
				}else{//包含所有
					sql.WHERE(" agent_in0.agent_node like CONCAT(#{qo.agentNo}, '%') ");
				}
			}
		}


		public void where(SQL sql, ActivityDetail activityDetail) {
			if(activityDetail.getIsExclusion()!=-1){
				sql.WHERE(" ad.is_exclusion=#{activityDetail.isExclusion}");
			}

			if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
				sql.WHERE(" ad.active_order in ("+activityDetail.getActiveOrder()+")");
			}
			if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
				sql.WHERE(" ad.activity_code=#{activityDetail.activityCode}");
			}
			/*if(activityDetail.getAcqOrgId()!=null){
				sql.WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
			}*/
			if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
				sql.WHERE(" mi.one_agent_no=#{activityDetail.oneAgentNo}");
			}
			if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
				activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
				sql.WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
			}
			if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
				if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
					sql.WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
				}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
					sql.WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
				} else {    //所有商户
					sql.WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
				}
			}
			if (activityDetail.getStatus()!=null) {
				sql.WHERE(" ad.status=#{activityDetail.status}");
			}
			if (activityDetail.getActiveTimeStart()!=null) {
				sql.WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
			}
			if (activityDetail.getActiveTimeEnd()!=null) {
				sql.WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
			}
			if (activityDetail.getEnterTimeStart()!=null) {
				sql.WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
			}
			if (activityDetail.getEnterTimeEnd()!=null) {
				sql.WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
			}
			if (activityDetail.getCashBackAmount()!=null) {
				sql.WHERE(" cbd.cash_back_amount=#{activityDetail.cashBackAmount}");
			}
			if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
				sql.WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
			}
			if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
				sql.WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
			}
			if (activityDetail.getLiquidationTimeStart()!=null) {
				sql.WHERE(" ad.liquidation_time>=#{activityDetail.liquidationTimeStart}");
			}
			if (activityDetail.getLiquidationTimeEnd()!=null) {
				sql.WHERE(" ad.liquidation_time<=#{activityDetail.liquidationTimeEnd}");
			}
			if(activityDetail.getMinCumulateTransAmount()!=null){
				sql.WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
			}
			if (activityDetail.getMaxCumulateTransAmount()!=null){
				sql.WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
			}
			if(activityDetail.getMinTransTotal()!=null){
				sql.WHERE(" ad.trans_total >= #{activityDetail.minTransTotal}");
			}
			if (activityDetail.getMaxTransTotal()!=null){
				sql.WHERE(" ad.trans_total <= #{activityDetail.maxTransTotal}");
			}
			if(activityDetail.getMinFullAmount()!=null){
				sql.WHERE(" ad.full_amount >= #{activityDetail.minFullAmount}");
			}
			if (activityDetail.getMaxFullAmount()!=null){
				sql.WHERE(" ad.full_amount <= #{activityDetail.maxFullAmount}");
			}
			if(activityDetail.getMinEmptyAmount()!=null){
				sql.WHERE(" ad.empty_amount >= #{activityDetail.minEmptyAmount}");
			}
			if (activityDetail.getMaxEmptyAmount()!=null){
				sql.WHERE(" ad.empty_amount <= #{activityDetail.maxEmptyAmount}");
			}
			if (activityDetail.getMinStandardTime()!=null){
				sql.WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
			}
			if (activityDetail.getMaxStandardTime()!=null){
				sql.WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
			}
			if (activityDetail.getMinMinusAmountTime()!=null){
				sql.WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
			}
			if (activityDetail.getMaxMinusAmountTime()!=null){
				sql.WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
			}
			if (activityDetail.getMinAddAmountTime()!=null){
				sql.WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
			}
			if (activityDetail.getMaxAddAmountTime()!=null){
				sql.WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
			}
			if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
				sql.WHERE(" ad.is_standard = #{activityDetail.isStandard}");
			}
			if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
				sql.WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
			}
			if(StringUtils.isNotBlank(activityDetail.getRecommendedSource())){
				sql.WHERE(" mi.recommended_source = #{activityDetail.recommendedSource}");
			}
			if (activityDetail.getRepeatRegister()!=null){
				sql.WHERE(" ad.repeat_register = #{activityDetail.repeatRegister}");
			}
			if (StringUtils.isNotBlank(activityDetail.getActivityTypeNo())){
				sql.WHERE(" ad.activity_type_no = #{activityDetail.activityTypeNo}");
			}
			if (activityDetail.getBillingTimeStart() != null) {
				sql.WHERE(" ad.billing_time>=#{activityDetail.billingTimeStart}");
			}
			if (activityDetail.getBillingTimeEnd() != null) {
				sql.WHERE(" ad.billing_time<=#{activityDetail.billingTimeEnd}");
			}
			if(activityDetail.getBillingStatus() != null ){
				sql.WHERE(" ad.billing_status = #{activityDetail.billingStatus}");
			}
			if(activityDetail.getHardId() != null ){
				sql.WHERE(" ach.hard_id = #{activityDetail.hardId}");
			}
		}
	}



}

