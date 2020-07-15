package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

	@Select("SELECT *  FROM activity_config WHERE activity_code=#{activityCode}")
	@ResultType(ActivityConfig.class)
	ActivityConfig selectActivityCofig(@Param("activityCode")String activityCode);
	
	// @Select("SELECT * FROM activity_config where activity_code=#{activityCode}")
	@Select("SELECT ah.*,hp.type_name,hp.hp_id FROM activity_hardware ah ,hardware_product hp WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} ")
	@ResultType(ActivityHardware.class)
	List<ActivityHardware> selectActivityHardware(@Param("page")Page<ActivityHardware> page,@Param("activityCode")String activityCode);

	@Select("SELECT ah.*,hp.type_name,hp.hp_id FROM activity_hardware ah ,hardware_product hp WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} ")
	@ResultType(ActivityHardware.class)
	List<ActivityHardware> selectActivityConfig(@Param("activityCode")String activityCode);

	@Insert({
		"insert into activity_hardware (activity_code,activiy_name,hard_id,price,target_amout,create_time)"
			+ "values " +
			"(#{n.activityCode},#{n.activiyName},#{n.hardId},#{n.price},#{n.targetAmout},now());"
	})
	int insertActivityHardware(@Param("n")ActivityHardware n);
	  
	@Update("update activity_config set start_time=#{n.startTime},end_time=#{n.endTime},wait_day=#{n.waitDay},cash_service_id=#{n.cashServiceId},agent_service_id=#{n.agentServiceId},"
			+ "cumulate_trans_day=#{n.cumulateTransDay},cumulate_amount_minus=#{n.cumulateAmountMinus},cumulate_amount_add=#{n.cumulateAmountAdd},cumulate_trans_minus_day=#{n.cumulateTransMinusDay}"
			+ " where activity_code=#{n.activityCode}")
	int updateActivityConfig(@Param("n")ActivityConfig n);

	@Insert("insert into activity_config (activity_code,start_time,end_time,wait_day,cash_service_id,agent_service_id,"
			+ "cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,cumulate_trans_minus_day) values (#{n.activityCode},#{n.startTime},#{n.endTime},#{n.waitDay},"
			+ "#{n.cashServiceId},#{n.agentServiceId},#{n.cumulateTransDay},#{n.cumulateAmountMinus},#{n.cumulateAmountAdd},#{n.cumulateTransMinusDay})")
	int insetActivityConfig(@Param("n")ActivityConfig n);

	@Update("update activity_hardware set price=#{activityHardware.price},target_amout=#{activityHardware.targetAmout}"
				+ " where hard_id=#{activityHardware.hardId}")
	int updateActivityHardware(@Param("activityHardware")ActivityHardware activityHardware);

	// -------------------------- 以下是欢乐返活动 !!! ----------------------------

	// SELECT hh.*,hp.type_name FROM hlf_hardware hh, hardware_product hp WHERE hh.hard_id=hp.hp_id AND hh.activity_code='008'
	@Select("SELECT ah.activity_code,ah.hard_id,hp.type_name,ah.activity_type_no,aht.activity_type_name,aht.trans_amount," +
			"aht.cash_back_amount,aht.repeat_register_amount,aht.empty_amount,aht.full_amount,aht.repeat_empty_amount,aht.repeat_full_amount," +
			"aht.sub_type,"+
			"ah.cash_last_ally_amount,ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day " +
            "FROM activity_hardware ah " +
			"LEFT JOIN hardware_product hp ON ah.hard_id=hp.hp_id " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} " +
			" order by ah.create_time desc")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHlfHardware(String activityCode);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityHardware")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHlfActivityHardware(@Param("h")HlfHardware h);

	// SELECT hh.*,hp.type_name FROM hlf_hardware hh, hardware_product hp WHERE hh.hard_id=hp.hp_id AND hh.activity_code='008'
	@Select("SELECT ah.id,ah.activity_code,ah.hard_id,hp.type_name,ah.activity_type_no,aht.rule_id activity_merchant_id,aht.activity_type_name,aht.trans_amount," +
			"aht.cash_back_amount,aht.empty_amount,aht.full_amount,ah.cash_last_ally_amount," +
			"aht.sub_type,"+
			"ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day,hp.org_id team_id,hp.team_entry_id," +
			"aht.repeat_empty_amount,aht.repeat_full_amount,ah.repeat_cumulate_trans_day,ah.repeat_cumulate_amount_minus,ah.repeat_cumulate_amount_add," +
			"ah.repeat_cumulate_trans_minus_day,ah.activity_reward_config_id " +
			"FROM activity_hardware ah " +
			"LEFT JOIN hardware_product hp ON ah.hard_id=hp.hp_id " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{a.activityCode} and hard_id=#{a.hardId} and ah.activity_type_no=#{a.activityTypeNo}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareByHardId(@Param("a")HlfHardware hlfHardware);

	@Select("SELECT * FROM activity_hardware where id=#{id}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareById(@Param("id")Integer id);

	@Select("SELECT ah.*,aht.activity_type_name,aht.sub_type FROM activity_hardware ah " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"where ah.activity_code=#{a.activityCode} and ah.hard_id=#{a.hardId} and ah.activity_type_no=#{a.activityTypeNo}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareInfo(@Param("a")HlfHardware hlfHardware);

	@Update("update activity_hardware set trans_amount=#{h.transAmount},cash_back_amount=#{h.cashBackAmount}," +
			"activity_type_no=#{h.activityTypeNo}," +
			"operator=#{h.operator},cash_last_ally_amount=#{h.cashLastAllyAmount},default_status=#{h.defaultStatus}," +
			"cumulate_trans_day=#{h.cumulateTransDay},cumulate_amount_minus=#{h.cumulateAmountMinus},cumulate_amount_add=#{h.cumulateAmountAdd}," +
			"cumulate_trans_minus_day=#{h.cumulateTransMinusDay}," +
			"repeat_cumulate_trans_day=#{h.repeatCumulateTransDay},repeat_cumulate_amount_minus=#{h.repeatCumulateAmountMinus},repeat_cumulate_amount_add=#{h.repeatCumulateAmountAdd}," +
			"repeat_cumulate_trans_minus_day=#{h.repeatCumulateTransMinusDay},activity_reward_config_id=#{h.activityRewardConfigId}"
			+ " where id=#{h.id}")
	int updateHlfHardware(@Param("h")HlfHardware h);

	@Select("SELECT id FROM activity_hardware WHERE activity_type_no=#{h.activityTypeNo} and hard_id=#{h.hardId}")
	@ResultType(Integer.class)
	Integer isExistHardware(@Param("h")HlfHardware h);

	@Select("SELECT id FROM activity_hardware WHERE activity_type_no=#{h.activityTypeNo} and hard_id=#{h.hardId} and id!=#{h.id}")
	@ResultType(Integer.class)
	Integer isExistHardwareByNoId(@Param("h")HlfHardware h);

	@Insert({
		"insert into activity_hardware (activity_code,activiy_name,hard_id,trans_amount,cash_back_amount," +
				"create_time,operator,activity_type_no,cash_last_ally_amount," +
				"default_status,cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,cumulate_trans_minus_day," +
				"repeat_cumulate_trans_day,repeat_cumulate_amount_minus," +
				"repeat_cumulate_amount_add,repeat_cumulate_trans_minus_day,activity_reward_config_id)"
			+ "values " +
			"(#{h.activityCode},#{h.activiyName},#{h.hardId},#{h.transAmount},#{h.cashBackAmount}," +
				"now(),#{h.operator},#{h.activityTypeNo},#{h.cashLastAllyAmount}," +
				"#{h.defaultStatus},#{h.cumulateTransDay},#{h.cumulateAmountMinus},#{h.cumulateAmountAdd},#{h.cumulateTransMinusDay}," +
				"#{h.repeatCumulateTransDay},#{h.repeatCumulateAmountMinus}," +
				"#{h.repeatCumulateAmountAdd},#{h.repeatCumulateTransMinusDay},#{h.activityRewardConfigId})"
	})
	int insertHlfHardware(@Param("h")HlfHardware h);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnType")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyReturnType(@Param("a") ActivityHardwareType activityHardwareType, @Param("page") Page<ActivityHardwareType> page);

	@Select("SELECT count(activity_type_name) FROM activity_hardware_type WHERE activity_type_name=#{a.activityTypeName} and activity_type_no !=#{a.activityTypeNo}")
	@ResultType(Integer.class)
	int queryByActivityTypeName(@Param("a")ActivityHardwareType activityHardwareType);

	@Select("SELECT count(activity_type_name) FROM activity_hardware_type WHERE activity_type_name=#{a.activityTypeName}")
	@ResultType(Integer.class)
	int checkByActivityTypeName(@Param("a")ActivityHardwareType activityHardwareType);

	@Insert({"insert into activity_hardware_type (activity_type_no,activity_type_name,activity_code,trans_amount," +
			"cash_back_amount,remark,create_time,repeat_register_amount,empty_amount,full_amount," +
			"org_id,team_entry_id,activity_details,"+
			"repeat_empty_amount,repeat_full_amount,rule_id,hlf_agent_reward_config_id)"
			+ "values (#{a.activityTypeNo},#{a.activityTypeName},#{a.activityCode},#{a.transAmount}," +
			"#{a.cashBackAmount},#{a.remark},now(),#{a.repeatRegisterAmount},#{a.emptyAmount},#{a.fullAmount}," +
			"#{a.orgId},#{a.teamEntryId},#{a.activityDetails}," +
			"#{a.repeatEmptyAmount},#{a.repeatFullAmount},#{a.ruleId},#{a.hlfAgentRewardConfigId} )"})
	int insertHappyReturnType(@Param("a")ActivityHardwareType activityHardwareType);

	@Insert({"insert into activity_hardware_type (activity_type_no,activity_type_name,activity_code,trans_amount," +
			"cash_back_amount,remark,create_time,repeat_register_amount,empty_amount,full_amount,repeat_empty_amount,repeat_full_amount," +
			"sub_type,"+
			"org_id,team_entry_id,"+
			"xhlf_smart_config_id,"+
			"one_limit_days,one_trans_amount,one_reward_amount,one_repeat_reward_amount," +
			"two_limit_days,two_trans_amount,two_reward_amount,two_repeat_reward_amount," +
			"three_limit_days,three_trans_amount,three_reward_amount,three_repeat_reward_amount," +
			"four_limit_days,four_trans_amount,four_reward_amount,four_repeat_reward_amount," +
			"merchant_limit_days,merchant_trans_amount,merchant_reward_amount,merchant_repeat_reward_amount," +
			"agent_trans_total_type,activity_details"+
			",one_sub_trans_amount"+
			",one_sub_reward_amount"+
			",one_sub_repeat_reward"+
			")"
			+ "values (#{a.activityTypeNo},#{a.activityTypeName},#{a.activityCode},#{a.transAmount}," +
			"#{a.cashBackAmount},#{a.remark},now(),#{a.repeatRegisterAmount},#{a.emptyAmount},#{a.fullAmount},#{a.repeatEmptyAmount},#{a.repeatFullAmount}," +
			"#{a.subType}," +
			"#{a.orgId},#{a.teamEntryId}," +
			"#{a.xhlfSmartConfigId}," +
			"#{a.oneLimitDays},#{a.oneTransAmount},#{a.oneRewardAmount},#{a.oneRepeatRewardAmount}," +
			"#{a.twoLimitDays},#{a.twoTransAmount},#{a.twoRewardAmount},#{a.twoRepeatRewardAmount}," +
			"#{a.threeLimitDays},#{a.threeTransAmount},#{a.threeRewardAmount},#{a.threeRepeatRewardAmount}," +
			"#{a.fourLimitDays},#{a.fourTransAmount},#{a.fourRewardAmount},#{a.fourRepeatRewardAmount}," +
			"#{a.merchantLimitDays},#{a.merchantTransAmount},#{a.merchantRewardAmount},#{a.merchantRepeatRewardAmount}," +
			"#{a.agentTransTotalType},#{a.activityDetails}" +
			",#{a.oneSubTransAmount}" +
			",#{a.oneSubRewardAmount}" +
			",#{a.oneSubRepeatReward}" +
			" )"})
	int insertHappyReturnTypeNew(@Param("a")ActivityHardwareType activityHardwareType);

	@Insert({"insert into activity_hardware_type (activity_type_no,activity_type_name,activity_code,trans_amount," +
			"cash_back_amount,remark,create_time,repeat_register_amount,empty_amount,full_amount,repeat_empty_amount,repeat_full_amount," +
			"sub_type,"+
			"org_id,team_entry_id,"+
			"xhlf_smart_config_id,"+
			"agent_trans_total_type,activity_details"+
			")"
			+ "values (#{a.activityTypeNo},#{a.activityTypeName},#{a.activityCode},#{a.transAmount}," +
			"#{a.cashBackAmount},#{a.remark},now(),#{a.repeatRegisterAmount},#{a.emptyAmount},#{a.fullAmount},#{a.repeatEmptyAmount},#{a.repeatFullAmount}," +
			"#{a.subType}," +
			"#{a.orgId},#{a.teamEntryId}," +
			"#{a.xhlfSmartConfigId}," +
			"#{a.agentTransTotalType},#{a.activityDetails}" +
			" )"})
	int insertHappyReturnTypeSmart(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware_type set activity_type_name=#{a.activityTypeName},trans_amount=#{a.transAmount}," +
			"cash_back_amount=#{a.cashBackAmount},remark=#{a.remark},update_time=now(),repeat_register_amount=#{a.repeatRegisterAmount}," +
			"empty_amount=#{a.emptyAmount},full_amount=#{a.fullAmount},repeat_empty_amount=#{a.repeatEmptyAmount}," +
			"repeat_full_amount=#{a.repeatFullAmount},rule_id=#{a.ruleId},hlf_agent_reward_config_id=#{a.hlfAgentRewardConfigId},activity_details=#{a.activityDetails} " +
			"where activity_type_no=#{a.activityTypeNo}")
	int updateHappyReturnType(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware_type set activity_type_name=#{a.activityTypeName},trans_amount=#{a.transAmount}," +
			"cash_back_amount=#{a.cashBackAmount},repeat_register_amount=#{a.repeatRegisterAmount},remark=#{a.remark},update_time=now()," +
			"one_limit_days=#{a.oneLimitDays},one_trans_amount=#{a.oneTransAmount},one_reward_amount=#{a.oneRewardAmount},one_repeat_reward_amount=#{a.oneRepeatRewardAmount}," +
			"two_limit_days=#{a.twoLimitDays},two_trans_amount=#{a.twoTransAmount},two_reward_amount=#{a.twoRewardAmount},two_repeat_reward_amount=#{a.twoRepeatRewardAmount}," +
			"three_limit_days=#{a.threeLimitDays},three_trans_amount=#{a.threeTransAmount},three_reward_amount=#{a.threeRewardAmount},three_repeat_reward_amount=#{a.threeRepeatRewardAmount}," +
			"four_limit_days=#{a.fourLimitDays},four_trans_amount=#{a.fourTransAmount},four_reward_amount=#{a.fourRewardAmount},four_repeat_reward_amount=#{a.fourRepeatRewardAmount}," +
			"merchant_limit_days=#{a.merchantLimitDays},merchant_trans_amount=#{a.merchantTransAmount},merchant_reward_amount=#{a.merchantRewardAmount},merchant_repeat_reward_amount=#{a.merchantRepeatRewardAmount}," +
			"agent_trans_total_type=#{a.agentTransTotalType},"+
			"xhlf_smart_config_id=#{a.xhlfSmartConfigId},activity_details=#{a.activityDetails}" +
			",one_sub_trans_amount=#{a.oneSubTransAmount}" +
			",one_sub_reward_amount=#{a.oneSubRewardAmount}" +
			",one_sub_repeat_reward=#{a.oneSubRepeatReward}"+
			" where activity_type_no=#{a.activityTypeNo}")
	int updateHappyReturnTypeNew(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware_type set activity_type_name=#{a.activityTypeName},trans_amount=#{a.transAmount}," +
			"cash_back_amount=#{a.cashBackAmount},repeat_register_amount=#{a.repeatRegisterAmount},remark=#{a.remark},update_time=now()," +
			"agent_trans_total_type=#{a.agentTransTotalType},"+
			"xhlf_smart_config_id=#{a.xhlfSmartConfigId},activity_details=#{a.activityDetails} "+
			" where activity_type_no=#{a.activityTypeNo}")
	int updateHappyReturnTypeSmart(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware set trans_amount=#{a.transAmount},cash_back_amount=#{a.cashBackAmount}"
			+ " where activity_type_no=#{a.activityTypeNo} ")
	int updateActivityHardwareByActivityTypeNo(@Param("a")ActivityHardwareType activityHardwareType);


	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set a.cash_back_amount=#{a.cashBackAmount},a.repeat_register_amount=#{a.repeatRegisterAmount}," +
			"a.full_prize_amount=#{a.fullAmount},a.repeat_full_prize_amount=#{a.repeatFullAmount},a.not_full_deduct_amount=#{a.emptyAmount},a.repeat_not_full_deduct_amount=#{a.repeatEmptyAmount}" +
			" where a.activity_type_no=#{a.activityTypeNo} and b.agent_level=1")
	int updateAgentSctivityCashBackAmount(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set " +
			"a.scan_reward_amount=#{a.scanRewardAmount},a.scan_repeat_reward_amount=#{a.scanRepeatRewardAmount},a.all_reward_amount=#{a.allRewardAmount},a.all_repeat_reward_amount=#{a.allRepeatRewardAmount}" +
			" where a.activity_type_no=#{a.activityTypeNo}  and b.agent_level=1")
	int updateAgentActivityRewardAmount1(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set " +
			"a.scan_reward_amount=#{a.scanRewardAmount},a.scan_repeat_reward_amount=#{a.scanRepeatRewardAmount},a.all_reward_amount=#{a.allRewardAmount},a.all_repeat_reward_amount=#{a.allRepeatRewardAmount}" +
			" where a.activity_type_no=#{a.activityTypeNo}")
	int updateAgentActivityRewardAmount2(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set " +
			"a.scan_reward_amount=#{a.scanRewardAmount},a.scan_repeat_reward_amount=#{a.scanRepeatRewardAmount},a.all_reward_amount=#{a.allRewardAmount},a.all_repeat_reward_amount=#{a.allRepeatRewardAmount}" +
			" where a.activity_type_no=#{a.activityTypeNo} and b.agent_level=1")
	int updateAgentActivityReward(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set a.cash_back_amount=#{a.cashBackAmount},a.repeat_register_amount=#{a.repeatRegisterAmount}," +
			"a.full_prize_amount=#{a.fullAmount},a.repeat_full_prize_amount=#{a.repeatFullAmount},a.not_full_deduct_amount=#{a.emptyAmount},a.repeat_not_full_deduct_amount=#{a.repeatEmptyAmount}" +
			",a.one_reward_amount=#{a.oneRewardAmount},a.one_repeat_reward_amount=#{a.oneRepeatRewardAmount}"+
			",a.two_reward_amount=#{a.twoRewardAmount},a.two_repeat_reward_amount=#{a.twoRepeatRewardAmount}"+
			",a.three_reward_amount=#{a.threeRewardAmount},a.three_repeat_reward_amount=#{a.threeRepeatRewardAmount}"+
			",a.four_reward_amount=#{a.fourRewardAmount},a.four_repeat_reward_amount=#{a.fourRepeatRewardAmount}"+
			",one_sub_reward_amount=#{a.oneSubRewardAmount}" +
			",one_sub_repeat_reward=#{a.oneSubRepeatReward}"+
			" where a.activity_type_no=#{a.activityTypeNo} and b.agent_level=1")
	int updateAgentSctivityCashBackAmountNew(@Param("a")ActivityHardwareType activityHardwareType);

	@Delete("delete from activity_hardware_type where activity_type_no=#{activityTypeNo}")
	int delHappyReturnType(@Param("activityTypeNo")String activityTypeNo);

	@Update("update activity_hardware_type set update_agent_status=#{a.updateAgentStatus}" +
			" where activity_type_no=#{a.activityTypeNo}")
	int updateAgentStatusSwitch(@Param("a")ActivityHardwareType activityHardwareType);


	@Update("update activity_hardware_type set count_trade_scope=#{countTradeScope}" +
			" where id=#{id}")
	int updateCountTradeScope(@Param("id")String id,@Param("countTradeScope")String countTradeScope);

	@Select("SELECT count(activity_type_no) FROM activity_hardware WHERE activity_type_no=#{activityTypeNo}")
	@ResultType(Integer.class)
	int queryActivityCount(@Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT aht.*,ti.team_name as orgName,tie.team_entry_name FROM activity_hardware_type aht " +
            "left join team_info ti on aht.org_id=ti.team_id " +
            "left join team_info_entry tie on aht.team_entry_id=tie.team_entry_id " +
            "WHERE aht.activity_code=#{activityCode}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> queryByactivityTypeNoList(@Param("activityCode")String activityCode);

	@Select("SELECT * FROM activity_hardware_type WHERE hlf_agent_reward_config_id=#{hlfAgentRewardConfigId}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> queryByAgentConfigIdList(@Param("hlfAgentRewardConfigId")String hlfAgentRewardConfigId);

	@Select("SELECT aht.*,ti.team_name as orgName,tie.team_entry_name FROM activity_hardware_type aht " +
            "left join team_info ti on aht.org_id=ti.team_id " +
            "left join team_info_entry tie on aht.team_entry_id=tie.team_entry_id " +
            "WHERE aht.activity_code=#{activityCode} and aht.sub_type=#{subType}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> queryByactivityTypeNoList2(@Param("activityCode")String activityCode,@Param("subType")String subType);

	/**
	 * 动态模糊获取欢乐返子类型列表
	 */
	@SelectProvider(type =SqlProvider.class, method = "getHlfActivityTypeList")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> getHlfActivityTypeList(@Param("activityCode")String activityCode,@Param("subType")String subType,@Param("str")String str);

	@Select("SELECT aht.*,ah.activiy_name," +
			"ti.team_name as orgName,tie.team_entry_name,"+
			"xsc.activity_name as smartActivityName,"+
			"xsc.one_limit_days as sOneLimitDays,xsc.one_trans_amount as sOneTransAmount,xsc.one_reward_mer_amount,xsc.one_repeat_reward_mer_amount,xsc.one_reward_agent_amount,xsc.one_repeat_reward_agent_amount,"+
			"xsc.two_limit_days as sTwoLimitDays,xsc.two_trans_amount as sTwoTransAmount,xsc.two_reward_agent_amount,xsc.two_repeat_reward_agent_amount,"+
			"xsc.three_limit_days as sThreeLimitDays,xsc.three_trans_amount as sThreeTransAmount,xsc.three_reward_agent_amount,xsc.three_repeat_reward_agent_amount,"+
			"harc.scan_reward_amount,harc.scan_repeat_reward_amount,harc.all_reward_amount,harc.all_repeat_reward_amount " +
			"FROM activity_hardware_type aht " +
			"LEFT JOIN activity_hardware ah on aht.activity_code=ah.activity_code " +
			"LEFT JOIN hlf_agent_reward_config harc on harc.id=aht.hlf_agent_reward_config_id " +
			"LEFT JOIN xhlf_smart_config xsc on xsc.id=aht.xhlf_smart_config_id " +
			"LEFT JOIN team_info ti on ti.team_id=aht.org_id " +
			"LEFT JOIN team_info_entry tie on tie.team_entry_id=aht.team_entry_id " +
			"WHERE aht.activity_type_no=#{activityTypeNo} limit 0,1")
	@ResultType(ActivityHardwareType.class)
	ActivityHardwareType queryByActivityHardwareType(@Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT * FROM activity_hardware_type")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> getActivityTypeNoList();

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityHardwareList")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHlfActivityHardwareList(@Param("info")HlfHardware hlfHardware);

	// -------------------------- 以上是欢乐返活动 !!! ----------------------------

	@Insert("insert into activity_reward_config (activity_name,start_time,end_time,cumulate_trans_minus_day," +
			"cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,repeat_cumulate_trans_minus_day," +
			"repeat_cumulate_trans_day,repeat_cumulate_amount_minus,repeat_cumulate_amount_add) " +
			"values (#{n.activityName},#{n.startTime},#{n.endTime},#{n.cumulateTransMinusDay}," +
			"#{n.cumulateTransDay},#{n.cumulateAmountMinus},#{n.cumulateAmountAdd},#{n.repeatCumulateTransMinusDay}," +
			"#{n.repeatCumulateTransDay},#{n.repeatCumulateAmountMinus},#{n.repeatCumulateAmountAdd})")
	int insertHappyReturnRewardActivity(@Param("n") ActivityRewardConfig activityRewardConfig);

	@Update("update activity_reward_config set activity_name=#{n.activityName},start_time=#{n.startTime},end_time=#{n.endTime},cumulate_trans_minus_day=#{n.cumulateTransMinusDay}," +
			"cumulate_trans_day=#{n.cumulateTransDay},cumulate_amount_minus=#{n.cumulateAmountMinus},cumulate_amount_add=#{n.cumulateAmountAdd}," +
			"repeat_cumulate_trans_minus_day=#{n.repeatCumulateTransMinusDay},repeat_cumulate_trans_day=#{n.repeatCumulateTransDay}," +
			"repeat_cumulate_amount_minus=#{n.repeatCumulateAmountMinus},repeat_cumulate_amount_add=#{n.repeatCumulateAmountAdd}" +
			" where id=#{n.id}")
	int updateHappyReturnRewardActivity(@Param("n") ActivityRewardConfig activityRewardConfig);

	@Delete("delete from activity_reward_config where id=#{id}")
	int deleteHappyReturnRewardActivity(@Param("id") String id);

	@Delete("delete from xhlf_smart_config where id=#{id}")
	int deleteXhlfSmartConfig(@Param("id") String id);

	@Select("SELECT count(*) FROM activity_hardware WHERE activity_reward_config_id=#{id}")
	int queryHappyReturnAgentActivityCount(@Param("id") String id);

	@Select("SELECT * FROM agent_activity_reward_config WHERE agent_no=#{agentNo}")
	@ResultType(AgentActivityRewardConfig.class)
	AgentActivityRewardConfig queryHappyReturnAgentActivityByAgentNo(@Param("agentNo") String agentNo);

	@Insert("Insert into agent_activity_reward_config(agent_no,agent_name,create_time,create_user,activity_id) "
			+ "values(#{info.agentNo},#{info.agentName},"
			+ "#{info.createTime},#{info.createUser},#{info.activityId})")
	int insertHappyReturnAgentActivity(@Param("info") AgentActivityRewardConfig info);

	@Delete("delete from agent_activity_reward_config where id=#{id}")
	int deleteHappyReturnAgentActivity(@Param("id") String id);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnRewardActivity")
	@ResultType(ActivityRewardConfig.class)
	List<ActivityRewardConfig> selectHappyReturnRewardActivity(@Param("a") ActivityRewardConfig activityRewardConfig, @Param("page") Page<ActivityRewardConfig> page);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnRewardActivity")
	@ResultType(ActivityRewardConfig.class)
	List<ActivityRewardConfig> importHappyReturnRewardActivity(@Param("a") ActivityRewardConfig activityRewardConfig);


	@Select("SELECT * FROM activity_reward_config WHERE id=#{id}")
	@ResultType(ActivityRewardConfig.class)
	ActivityRewardConfig selectHappyReturnRewardActivityById(@Param("id") String id);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnAgentActivity")
	@ResultType(AgentActivityRewardConfig.class)
	List<AgentActivityRewardConfig> selectHappyReturnAgentActivity(@Param("a") AgentActivityRewardConfig agentActivityRewardConfig, @Param("page") Page<AgentActivityRewardConfig> page);

	@SelectProvider(type =SqlProvider.class, method = "queryActivityVipList")
	@ResultType(Map.class)
	List<Map> queryActivityVipList(@Param("map") Map map,@Param("page") Page<Map> page);

	@Select("SELECT * from merchant_subscribe_vip where to_days(date_add(now(), interval ${time} day)) = to_days(validity_end)")
	@ResultType(Map.class)
	List<Map> queryActivityVipPush(@Param("time") String time);

	@Select("SELECT * FROM activity_reward_config")
	@ResultType(ActivityRewardConfig.class)
	List<ActivityRewardConfig> queryByActivityRewardConfigList();

	@Select("SELECT ah.activity_type_no,aht.activity_type_name " +
			"FROM activity_hardware ah " +
			"LEFT OUTER JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=#{hardId} AND ah.activity_code=#{activityCode}")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHBActivityHardwareList(@Param("hardId") String hardId, @Param("activityCode")String activityCode);

	@Insert("insert into hlf_activity_merchant_rule (rule_name,start_time,end_time,first_reward_type," +
			"first_reward_month,first_reward_total_amount,first_reward_amount,first_deduct_type," +
			"first_deduct_month,first_deduct_total_amount,first_deduct_amount,first_repeat_status," +
			"repeat_reward_type,repeat_reward_month,repeat_reward_total_amount,repeat_reward_amount," +
			"repeat_deduct_type,repeat_deduct_month,repeat_deduct_total_amount,repeat_deduct_amount," +
			"create_time,operator) " +
			"values (#{n.ruleName},#{n.startTime},#{n.endTime},#{n.firstRewardType}," +
			"#{n.firstRewardMonth},#{n.firstRewardTotalAmount},#{n.firstRewardAmount},#{n.firstDeductType}," +
			"#{n.firstDeductMonth},#{n.firstDeductTotalAmount},#{n.firstDeductAmount},#{n.firstRepeatStatus}," +
			"#{n.repeatRewardType},#{n.repeatRewardMonth},#{n.repeatRewardTotalAmount},#{n.repeatRewardAmount}," +
			"#{n.repeatDeductType},#{n.repeatDeductMonth},#{n.repeatDeductTotalAmount},#{n.repeatDeductAmount}," +
			"now(),#{n.operator})")
	int insertHlfActivityMerchantRule(@Param("n") HlfActivityMerchantRule info);

	@Insert("insert into hlf_agent_reward_config (activity_name," +
			"scan_activity_days,scan_target_amount,scan_reward_amount,scan_repeat_reward_amount," +
			"all_activity_days,all_target_amount,all_reward_amount,all_repeat_reward_amount," +
			"create_time,operator) " +
			"values (#{n.activityName}," +
			"#{n.scanActivityDays},#{n.scanTargetAmount},#{n.scanRewardAmount},#{n.scanRepeatRewardAmount}," +
			"#{n.allActivityDays},#{n.allTargetAmount},#{n.allRewardAmount},#{n.allRepeatRewardAmount}," +
			"now(),#{n.operator})")
	int insertHlfActivityAgentRule(@Param("n") HlfActivityAgentRule info);

	@Insert("insert into hlf_group (group_name,create_time) values (#{info.groupName},now())")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Integer.class)
	int insertHlfGroup(@Param("info") HlfGroup info);

	@Insert("insert into hlf_group_detail (group_id,activity_type_no,create_time) values (#{info.groupId},#{info.activityTypeNo},now())")
	int insertHlfGroupDetail(@Param("info") HlfGroupDetail info);

	@Update("update hlf_group set group_name=#{info.groupName} where id=#{info.id}")
	int updateHlfGroup(@Param("info") HlfGroup info);

	@Update("update hlf_activity_merchant_rule set rule_name=#{n.ruleName},start_time=#{n.startTime},end_time=#{n.endTime},first_reward_type=#{n.firstRewardType}," +
			"first_reward_month=#{n.firstRewardMonth},first_reward_total_amount=#{n.firstRewardTotalAmount},first_reward_amount=#{n.firstRewardAmount}," +
			"first_deduct_type=#{n.firstDeductType},first_deduct_month=#{n.firstDeductMonth},first_deduct_total_amount=#{n.firstDeductTotalAmount}," +
			"first_deduct_amount=#{n.firstDeductAmount},first_repeat_status=#{n.firstRepeatStatus},repeat_reward_type=#{n.repeatRewardType}," +
			"repeat_reward_month=#{n.repeatRewardMonth},repeat_reward_total_amount=#{n.repeatRewardTotalAmount},repeat_reward_amount=#{n.repeatRewardAmount}," +
			"repeat_deduct_type=#{n.repeatDeductType},repeat_deduct_month=#{n.repeatDeductMonth},repeat_deduct_total_amount=#{n.repeatDeductTotalAmount}," +
			"repeat_deduct_amount=#{n.repeatDeductAmount} " +
			" where rule_id=#{n.ruleId}")
	int updateHlfActivityMerchantRule(@Param("n") HlfActivityMerchantRule info);

	@Update("update hlf_agent_reward_config set " +
			"activity_name=#{n.activityName}," +
			"scan_activity_days=#{n.scanActivityDays},scan_target_amount=#{n.scanTargetAmount},scan_reward_amount=#{n.scanRewardAmount},scan_repeat_reward_amount=#{n.scanRepeatRewardAmount}," +
			"all_activity_days=#{n.allActivityDays},all_target_amount=#{n.allTargetAmount},all_reward_amount=#{n.allRewardAmount},all_repeat_reward_amount=#{n.allRepeatRewardAmount}" +
			" where id=#{n.id}")
	int updateHlfActivityAgentRule(@Param("n") HlfActivityAgentRule info);

	@Insert("insert into xhlf_smart_config (activity_name," +
			"one_limit_days,one_trans_amount,one_reward_mer_amount,one_repeat_reward_mer_amount,one_reward_agent_amount,one_repeat_reward_agent_amount," +
			"two_limit_days,two_trans_amount,two_reward_agent_amount,two_repeat_reward_agent_amount," +
			"three_limit_days,three_trans_amount,three_reward_agent_amount,three_repeat_reward_agent_amount," +
			"create_time,operator) " +
			"values (#{n.activityName}," +
			"#{n.oneLimitDays},#{n.oneTransAmount},#{n.oneRewardMerAmount},#{n.oneRepeatRewardMerAmount},#{n.oneRewardAgentAmount},#{n.oneRepeatRewardAgentAmount}," +
			"#{n.twoLimitDays},#{n.twoTransAmount},#{n.twoRewardAgentAmount},#{n.twoRepeatRewardAgentAmount}," +
			"#{n.threeLimitDays},#{n.threeTransAmount},#{n.threeRewardAgentAmount},#{n.threeRepeatRewardAgentAmount}," +
			"now(),#{n.operator})")
	int insertXhlfSmartConfig(@Param("n") XhlfSmartConfig info);

	@Update("update xhlf_smart_config set " +
			"activity_name=#{n.activityName}," +
			"one_limit_days=#{n.oneLimitDays},one_trans_amount=#{n.oneTransAmount},one_reward_mer_amount=#{n.oneRewardMerAmount},one_repeat_reward_mer_amount=#{n.oneRepeatRewardMerAmount},one_reward_agent_amount=#{n.oneRewardAgentAmount},one_repeat_reward_agent_amount=#{n.oneRepeatRewardAgentAmount}," +
			"two_limit_days=#{n.twoLimitDays},two_trans_amount=#{n.twoTransAmount},two_reward_agent_amount=#{n.twoRewardAgentAmount},two_repeat_reward_agent_amount=#{n.twoRepeatRewardAgentAmount}," +
			"three_limit_days=#{n.threeLimitDays},three_trans_amount=#{n.threeTransAmount},three_reward_agent_amount=#{n.threeRewardAgentAmount},three_repeat_reward_agent_amount=#{n.threeRepeatRewardAgentAmount}" +
			" where id=#{n.id}")
	int updateXhlfSmartConfig(@Param("n") XhlfSmartConfig info);

	@Delete("delete from hlf_activity_merchant_rule where rule_id=#{ruleId}")
	int deleteHlfActivityMerchantRule(@Param("ruleId") String ruleId);

	@Delete("delete from hlf_agent_reward_config where id=#{id}")
	int deleteHlfActivityAgentRule(@Param("id") String id);

	@Delete("delete from hlf_group where id=#{id}")
	int deleteHlfGroup(@Param("id") String id);

	@Delete("delete from hlf_group_detail where group_id=#{groupId}")
	int deleteHlfGroupDetail(@Param("groupId") Integer groupId);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityMerchantRule")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRule(@Param("a") HlfActivityMerchantRule info, @Param("page") Page<HlfActivityMerchantRule> page);

	@SelectProvider(type =SqlProvider.class, method = "selectXhlfSmartConfig")
	@ResultType(XhlfSmartConfig.class)
	List<XhlfSmartConfig> selectXhlfSmartConfig(@Param("a") XhlfSmartConfig info, @Param("page") Page<XhlfSmartConfig> page);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityAgentRule")
	@ResultType(HlfActivityAgentRule.class)
	List<HlfActivityAgentRule> selectHlfActivityAgentRule(@Param("a") HlfActivityAgentRule info, @Param("page") Page<HlfActivityAgentRule> page);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfGroup")
	@ResultType(HlfGroup.class)
	List<HlfGroup> selectHlfGroup(@Param("a") HlfGroup info, @Param("page") Page<HlfGroup> page);



	@Select("SELECT * FROM hlf_activity_merchant_rule WHERE rule_id=#{ruleId}")
	@ResultType(HlfActivityMerchantRule.class)
	HlfActivityMerchantRule selectHlfActivityMerchantRuleById(@Param("ruleId") String ruleId);

	@Select("SELECT * FROM hlf_agent_reward_config WHERE id=#{id}")
	@ResultType(HlfActivityAgentRule.class)
	HlfActivityAgentRule selectHlfActivityAgentRuleById(@Param("id") String id);

	@Select("SELECT * FROM hlf_group WHERE id=#{id}")
	@ResultType(HlfGroup.class)
	HlfGroup selectHlfGroupById(@Param("id") String id);

	@Select("SELECT * FROM xhlf_smart_config WHERE id=#{id}")
	@ResultType(XhlfSmartConfig.class)
	XhlfSmartConfig selectXhlfSmartConfigById(@Param("id") String id);

	@Select("select hgd.*,aht.activity_type_name,ti.team_name as orgName,tie.team_entry_name from hlf_group_detail hgd " +
			"left join activity_hardware_type aht on aht.activity_type_no=hgd.activity_type_no " +
			"left join team_info ti on ti.team_id=aht.org_id " +
			"left join team_info_entry tie on tie.team_entry_id=aht.team_entry_id " +
			" where hgd.group_id=#{groupId} order by hgd.activity_type_no asc")
	@ResultType(HlfGroupDetail.class)
	List<HlfGroupDetail> selectHlfGroupDetails(@Param("groupId") String groupId);

	@Select("select * from hlf_activity_merchant_rule")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo();

	@Select("select * from hlf_agent_reward_config")
	@ResultType(HlfActivityAgentRule.class)
	List<HlfActivityAgentRule> selectHlfActivityAgentRuleAllInfo();

	@Select("select * from xhlf_smart_config")
	@ResultType(XhlfSmartConfig.class)
	List<XhlfSmartConfig> selectXhlfSmartConfigAllInfo();

	@Select("select * from hlf_activity_merchant_rule where rule_id like concat('%',#{item},'%') or rule_name like concat('%',#{item},'%')")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo2(@Param("item") String item);

	@Select("select * from hlf_agent_reward_config where id like concat('%',#{item},'%') or activity_name like concat('%',#{item},'%')")
	@ResultType(HlfActivityAgentRule.class)
	List<HlfActivityAgentRule> selectHlfActivityAgentRuleAllInfo2(@Param("item") String item);

	@Select("select * from xhlf_smart_config where id like concat('%',#{item},'%') or activity_name like concat('%',#{item},'%')")
	@ResultType(XhlfSmartConfig.class)
	List<XhlfSmartConfig> selectXhlfSmartConfigAllInfo2(@Param("item") String item);

	@Select("select count(*) from activity_hardware_type where rule_id=#{ruleId}")
	int findActivityHardwareTypeByRuleIdCount(@Param("ruleId") String ruleId);

	@Select("select count(*) from activity_hardware_type where hlf_agent_reward_config_id=#{id}")
	int findActivityHardwareTypeByConfigIdCount(@Param("id") String id);


	@Select("select count(*) from activity_hardware_type where xhlf_smart_config_id=#{id}")
	int findActivityHardwareTypeBySmartConfigIdCount(@Param("id") String id);

	public class SqlProvider {


		public String selectHappyReturnType(Map<String, Object> param) {
			final ActivityHardwareType a = (ActivityHardwareType) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" aht.*,hamr.rule_name,harc.activity_name," +
                            "ti.team_name as orgName,tie.team_entry_name as teamEntryName,"+
                            "xsc.activity_name as smartActivityName"
					);
					FROM("activity_hardware_type aht ");
					LEFT_OUTER_JOIN("hlf_activity_merchant_rule hamr on hamr.rule_id=aht.rule_id");
					LEFT_OUTER_JOIN("hlf_agent_reward_config harc on harc.id=aht.hlf_agent_reward_config_id");
					LEFT_OUTER_JOIN("xhlf_smart_config xsc on xsc.id=aht.xhlf_smart_config_id");
					LEFT_OUTER_JOIN("team_info ti ON aht.org_id=ti.team_id");
					LEFT_OUTER_JOIN("team_info_entry tie ON aht.team_entry_id=tie.team_entry_id");
					WHERE("1=1 ");
					if (StringUtils.isNotBlank(a.getActivityTypeNo())) {
						WHERE(" aht.activity_type_no=#{a.activityTypeNo}");
					}
					if (StringUtils.isNotBlank(a.getActivityTypeName())) {
						WHERE(" aht.activity_type_name=#{a.activityTypeName}");
					}
					if (StringUtils.isNotBlank(a.getActivityCode())) {
						WHERE(" aht.activity_code=#{a.activityCode}");
					}
					if (StringUtil.isNotBlank(a.getRuleId())) {
						WHERE(" aht.rule_id=#{a.ruleId}");
					}
					if (StringUtil.isNotBlank(a.getHlfAgentRewardConfigId())) {
						WHERE(" aht.hlf_agent_reward_config_id=#{a.hlfAgentRewardConfigId}");
					}
					if (StringUtils.isNotBlank(a.getSubType())) {
						WHERE(" aht.sub_type=#{a.subType}");
					}
                    if (a.getOrgId()!=null&&StringUtils.isNotBlank(a.getOrgId()+"")) {
                        WHERE(" aht.org_id=#{a.orgId}");
						if (a.getTeamEntryId()!=null&&StringUtils.isNotBlank(a.getTeamEntryId()+"")) {
							WHERE(" aht.team_entry_id=#{a.teamEntryId}");
						}
                    }

					ORDER_BY("aht.id");
				}
			}.toString();
			return sql;
		}

		public String selectHappyReturnRewardActivity(Map<String, Object> param) {
			final ActivityRewardConfig a = (ActivityRewardConfig) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" arc.*"
					);
					FROM("activity_reward_config arc ");
					WHERE("1=1 ");
					if (StringUtils.isNotBlank(a.getActivityName())) {
						WHERE(" arc.activity_name=#{a.activityName}");
					}
				}
			}.toString();
			return sql;
		}

		public String selectHappyReturnAgentActivity(Map<String, Object> param) {
			final AgentActivityRewardConfig a = (AgentActivityRewardConfig) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" aarc.*"
					);
					FROM("agent_activity_reward_config aarc ");
					WHERE("1=1 ");
					WHERE(" aarc.activity_id=#{a.activityId}");
					if (StringUtils.isNotBlank(a.getAgentNo())) {
						WHERE(" aarc.agent_no=#{a.agentNo}");
					}
					if (StringUtils.isNotBlank(a.getAgentName())) {
						WHERE(" aarc.agent_name=#{a.agentName}");
					}
				}
			}.toString();
			return sql;
		}

		public String queryActivityVipList(Map<String, Object> param) {
			final Map<String, Object> map = (Map<String, Object>) param.get("map");
			String sql = new SQL() {
				{
					SELECT("sv.*,mi.merchant_name,mi.merchant_no,mi.mobilephone,ai.agent_name,ai2.agent_name one_agent_name,av.name,av.team_id "
					);
					FROM("subscribe_vip sv ");
					LEFT_OUTER_JOIN("activity_vip av on av.id=sv.vip_type");
					LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no=sv.user_id");
					LEFT_OUTER_JOIN("agent_info ai on ai.agent_no=mi.agent_no");
					LEFT_OUTER_JOIN("agent_info ai2 on ai2.agent_no=mi.one_agent_no");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(map.get("team_id"))) {
						WHERE(" av.team_id=#{map.team_id}");
					}
					
					if (StringUtil.isNotBlank(map.get("order_no"))) {
						WHERE(" sv.order_no=#{map.order_no}");
					}
					if (StringUtil.isNotBlank(map.get("payment_order_no"))) {
						WHERE(" sv.payment_order_no=#{map.payment_order_no}");
					}
					if (StringUtil.isNotBlank(map.get("merchantN"))) {
						WHERE(" (mi.merchant_no=#{map.merchantN} or mi.merchant_name like concat('%',#{map.merchantN},'%'))");
					}
					if (StringUtil.isNotBlank(map.get("mobilephone"))) {
						WHERE(" mi.mobilephone=#{map.mobilephone}");
					}
					if (StringUtil.isNotBlank(map.get("oneAgentNo"))) {
						WHERE(" mi.one_agent_no=#{map.oneAgentNo}");
					}
					if (StringUtil.isNotBlank(map.get("agentN"))) {
						WHERE(" mi.agent_no=#{map.agentN}");
					}
					if (StringUtil.isNotBlank(map.get("subscribe_status"))) {
						WHERE(" sv.subscribe_status=#{map.subscribe_status}");
					}
					if (StringUtil.isNotBlank(map.get("payment_type"))) {
						WHERE(" sv.payment_type=#{map.payment_type}");
					}
					if (StringUtil.isNotBlank(map.get("startTime"))) {
						WHERE(" sv.create_time>=#{map.startTime}");
					}
					if (StringUtil.isNotBlank(map.get("endTime"))) {
						WHERE(" sv.create_time<=#{map.endTime}");
					}
					
					if (StringUtil.isNotBlank(map.get("startValidityEnd"))) {
						WHERE(" sv.validity_end>=#{map.startValidityEnd}");
					}
					if (StringUtil.isNotBlank(map.get("endValidityEnd"))) {
						WHERE(" sv.validity_end<=#{map.endValidityEnd}");
					}
					
					if (StringUtil.isNotBlank(map.get("startTransTime"))) {
						WHERE(" sv.trans_time>=#{map.startTransTime}");
					}
					if (StringUtil.isNotBlank(map.get("endTransTime"))) {
						WHERE(" sv.trans_time<=#{map.endTransTime}");
					}
					
					ORDER_BY("sv.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityHardware(Map<String, Object> param) {
			final HlfHardware h = (HlfHardware) param.get("h");
			String sql = new SQL() {
				{
					SELECT("ah.id,ah.activity_reward_config_id,ah.activity_code,ah.hard_id,ah.activity_type_no,aht.empty_amount,aht.full_amount,ah.cash_last_ally_amount," +
							"ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day," +
							"ah.cumulate_trans_day,ah.cumulate_trans_minus_day,aht.repeat_empty_amount,aht.repeat_full_amount,ah.repeat_cumulate_trans_day,ah.repeat_cumulate_trans_minus_day," +
							"hp.type_name,aht.activity_type_name,aht.trans_amount,aht.cash_back_amount,aht.repeat_register_amount,aht.rule_id activityMerchantId,aht.hlf_agent_reward_config_id activityAgentId,aht.sub_type," +
							"ti.team_id,ti.team_name,teamEn.team_entry_id,teamEn.team_entry_name,"+
							"aht.one_reward_amount,aht.one_repeat_reward_amount,"+
							"aht.two_reward_amount,aht.two_repeat_reward_amount,"+
							"aht.three_reward_amount,aht.three_repeat_reward_amount,"+
							"aht.four_reward_amount,aht.four_repeat_reward_amount"
					);
					FROM("activity_hardware ah ");
					LEFT_OUTER_JOIN("hardware_product hp ON ah.hard_id=hp.hp_id ");
                    if (StringUtil.isNotBlank(h.getSubType())) {
                        LEFT_OUTER_JOIN(" activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no AND aht.sub_type=#{h.subType}");
                    } else {
                        LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no");
                    }
					LEFT_OUTER_JOIN("team_info ti ON ti.team_id=hp.org_id ");
					LEFT_OUTER_JOIN("team_info_entry teamEn ON teamEn.team_entry_id=hp.team_entry_id ");

					WHERE("ah.hard_id=hp.hp_id AND ah.activity_code=#{h.activityCode} ");

					if (StringUtil.isNotBlank(h.getHardId())) {
						WHERE(" ah.hard_id=#{h.hardId}");
					}
					if (StringUtil.isNotBlank(h.getTeamId())) {
						WHERE(" ti.team_id=#{h.teamId}");
					}
					if (StringUtil.isNotBlank(h.getDefaultStatus())) {
						WHERE(" ah.default_status=#{h.defaultStatus}");
					}
					if (StringUtil.isNotBlank(h.getIsMerchant())) {
						if(h.getIsMerchant()==0){
							WHERE(" aht.rule_id is null");
						}else{
							WHERE(" aht.rule_id is not null");
						}
					}
					if (StringUtil.isNotBlank(h.getIsAgent())) {
						if(h.getIsAgent()==0){
							WHERE(" aht.hlf_agent_reward_config_id is null");
						}else{
							WHERE(" aht.hlf_agent_reward_config_id is not null");
						}
					}
					if (StringUtil.isNotBlank(h.getSubType())) {
						WHERE(" aht.sub_type=#{h.subType}");
					}
					if (StringUtil.isNotBlank(h.getActivityTypeNo())) {
						WHERE(" ah.activity_type_no=#{h.activityTypeNo}");
					}
					ORDER_BY("ah.create_time desc ");
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityHardwareList(Map<String, Object> param) {
			final HlfHardware info = (HlfHardware) param.get("info");
			String sql = new SQL() {
				{
					SELECT("ah.activity_code,ah.hard_id,ah.activity_type_no," +
							"hp.type_name,aht.activity_type_name,aht.trans_amount,aht.cash_back_amount,aht.repeat_register_amount,ti.team_name,aht.sub_type"
					);
					FROM("activity_hardware ah ");
					LEFT_OUTER_JOIN("hardware_product hp ON ah.hard_id=hp.hp_id ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no");
					LEFT_OUTER_JOIN("team_info ti ON ti.team_id=hp.org_id ");
					WHERE("ah.hard_id=hp.hp_id AND ah.activity_code=#{info.activityCode} ");
					if (StringUtil.isNotBlank(info.getHardId())) {
						WHERE(" ah.hard_id=#{info.hardId}");
					}
					ORDER_BY("ah.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityMerchantRule(Map<String, Object> param) {
			final HlfActivityMerchantRule a = (HlfActivityMerchantRule) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" ham.*");
					FROM("hlf_activity_merchant_rule ham ");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(a.getRuleId())) {
						WHERE(" ham.rule_id=#{a.ruleId}");
					}
					if (StringUtil.isNotBlank(a.getRuleName())) {
						WHERE(" ham.rule_name like concat('%',#{a.ruleName},'%')");
					}
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityAgentRule(Map<String, Object> param) {
			final HlfActivityAgentRule a = (HlfActivityAgentRule) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" har.*");
					FROM("hlf_agent_reward_config har ");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(a.getId())) {
						WHERE(" har.id=#{a.id}");
					}
					if (StringUtil.isNotBlank(a.getActivityName())) {
						WHERE(" har.activity_name like concat('%',#{a.activityName},'%')");
					}
					ORDER_BY("har.create_time desc");
				}
			}.toString();
			return sql;
		}
		public String selectXhlfSmartConfig(Map<String, Object> param) {
			final XhlfSmartConfig a = (XhlfSmartConfig) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" xsc.*");
					FROM("xhlf_smart_config xsc ");
					//WHERE("1=1 ");
					if (StringUtil.isNotBlank(a.getId())) {
						WHERE(" xsc.id=#{a.id}");
					}
					if (StringUtil.isNotBlank(a.getActivityName())) {
						WHERE(" xsc.activity_name like concat('%',#{a.activityName},'%')");
					}
					if (StringUtil.isNotBlank(a.getMinCreateTime())) {
						WHERE(" xsc.create_time>=#{a.minCreateTime}");
					}
					if (StringUtil.isNotBlank(a.getMaxCreateTime())) {
						WHERE(" xsc.create_time<=#{a.maxCreateTime}");
					}
					ORDER_BY("xsc.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHlfGroup(Map<String, Object> param) {
			final HlfGroup a = (HlfGroup) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" hg.*");
					FROM("hlf_group hg ");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(a.getId())) {
						WHERE(" hg.id=#{a.id}");
					}
					if (StringUtil.isNotBlank(a.getGroupName())) {
						WHERE(" hg.group_name like concat('%',#{a.groupName},'%')");
					}
					ORDER_BY("hg.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String getHlfActivityTypeList(Map<String, Object> param) {
			String activityCode = (String) param.get("activityCode");
			String subType = (String) param.get("subType");
			String str = (String) param.get("str");

			StringBuffer sb=new StringBuffer();
			sb.append(" select * from activity_hardware_type aht ");
			sb.append("  where 1=1");
			if(StringUtil.isNotBlank(activityCode)){
				sb.append(" and aht.activity_code=#{activityCode} ");
			}
			if(StringUtil.isNotBlank(subType)){
				sb.append(" and aht.sub_type=#{subType} ");
			}
			if(StringUtil.isNotBlank(str)){
				sb.append(" and (aht.activity_type_no =#{str} or aht.activity_type_name like concat(#{str},'%') )");
			}
			sb.append(" limit 100 ");
			return sb.toString();
		}

	}
}
