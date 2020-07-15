package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.impl.AgentInfoServiceImpl;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface AgentInfoDao {


	@Select("select agent_node from agent_info where sale_name in (${saleNames})")
	@ResultType(List.class)
	List<String> getAgentInfoBySaleName(@Param("saleNames")String saleNames);


	@Select("select * from xhlf_smart_config where id=#{xhlfSmartConfigId}")
	@ResultType(XhlfSmartConfig.class)
	XhlfSmartConfig selectXhlfSmart(@Param("xhlfSmartConfigId")Integer xhlfSmartConfigId);

	@Select("select ai.* from agent_info ai where ai.agent_no=#{agentNo}")
	@ResultType(AgentInfo.class)
	AgentInfo select(@Param("agentNo")String agentNo);
	
    @Select("select agent_no,agent_node from agent_info where status='1' and agent_name=#{name}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByName(@Param("name")String name);
    
    @SelectProvider(type=SqlProvider.class,method="agentAutoComplete")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAllAgentInfo(@Param("item")String item, @Param("agentLevel")int agentLevel);

    @SelectProvider(type=SqlProvider.class,method="agentAutoComplete")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAllAgentInfo2(@Param("item")String item,@Param("saleName")String saleName, @Param("agentLevel")int agentLevel);

    @SelectProvider(type=SqlProvider.class,method="agentAutoComplete")
	@ResultType(AgentInfo.class)
	List<AgentInfo> getAllAgentListByParent(@Param("item")String item,@Param("agentNode") String agentNode,@Param("agentLevel") int agentLevel);

    @Select("select agent_no,agent_name from agent_info where status='1' and agent_level='1'")
	@ResultType(AgentInfo.class)
	List<AgentInfo> selectByLevelOne();

	@Select("select agent_no,agent_name from agent_info where status='1' and agent_level='1' and has_account<>1")
	@ResultType(AgentInfo.class)
	List<AgentInfo> selectLevelOneAgentWithNoAccount();

	@Select("select agent_no,agent_node from agent_info where status='1' and agent_level != '1' and agent_node LIKE #{agentNode}")
	@ResultType(AgentInfo.class)
	List<AgentInfo> selectByLevelAgent(@Param("agentNode")String agentNode);
    
    @Select("select info.* from agent_info info where agent_no=#{agentNo}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByAgentNo(@Param("agentNo")String agentNo);

	@Select("select info.* from agent_info info where agent_node=#{agentNode}")
	@ResultType(AgentInfo.class)
	AgentInfo selectByAgentNode(@Param("agentNode")String agentNode);
    /**
     * 查找所有的一级代理商：名称 + 编号（关联表中需要用到）
     */
    @Select("select agent_no,agent_name from agent_info where status='1' and agent_level='1'")
    @ResultType(AgentInfo.class)
	List<AgentInfo> selectOneAgentName();
    
    /**
     * 查找所有的一级代理商：名称 + 编号（关联表中需要用到）
     */
    @Select("select agent_no,agent_name from agent_info where agent_name like #{agentName} and status='1' and agent_level='1'")
    @ResultType(AgentInfo.class)
	List<AgentInfo> selectOneAgentByName(@Param("agentName")String agentName);
    
    /**
     * 查询非直营的一级代理商
     * @return
     */
    @Select("select agent_no,agent_name from agent_info a left join team_info t on a.team_id=t.team_id "
    		+ " where a.status='1' and a.agent_level='1' and t.team_type='2'")
    @ResultType(AgentInfo.class)
	List<AgentInfo> selectOneAgent();

	@Select("select a.agent_no,a.agent_name,a.agent_level,t.team_id,t.team_name from agent_info a left join team_info t on a.team_id=t.team_id " +
			" where a.status='1' and a.agent_no=#{agentNo}")
	Map<String,Object> selectAgentTeamByAgentNo(@Param("agentNo")String agentNo);

    /**
     * 根据代理商ID查询名称
     * @param oemId
     * by tans
     * @return
     */
    @Select("select agent_name from agent_info where agent_no=#{id}")
    @ResultType(java.lang.String.class)
	String selectNameById(@Param("id")String oemId);

    /**
     * 获取代理商的主分润
     * @author tans
     * @date 2017年5月23日 上午11:19:50
     * @param agentNo
     * @return
     */
    @Select("SELECT CONCAT((SELECT count(1) FROM agent_share_rule_task rt"
		+ " WHERE rt.share_id = a.id AND rt.check_status = '0' ),',',("
		+ " SELECT count(1) FROM agent_share_rule_task rt WHERE rt.share_id = a.id "
		+ " AND rt.check_status = '2' ))  uncheck_status, a.*,"
		+ " s.service_name,s.service_type,smr.rate_type,s2.service_type as service_type2,bpd.bp_name,bpd.team_id"
		+ " FROM"
		+ " agent_share_rule a "
		+ " LEFT JOIN service_info s ON a.service_id = s.service_id"
		+ " LEFT JOIN service_info s2 ON s2.link_service = s.service_id"
		+ " LEFT JOIN service_manage_rate smr ON s.service_id = smr.service_id and a.card_type=smr.card_type and a.holidays_mark=smr.holidays_mark " +
			"and smr.agent_no=(SELECT ai.one_level_id FROM agent_info ai WHERE ai.agent_no = a.agent_no) "
		+ " LEFT JOIN business_product_info bpi on bpi.service_id=s.service_id "
		+ " LEFT JOIN business_product_group bpg on bpg.bp_id=bpi.bp_id "
		+ " INNER JOIN business_product_define bpd on "
		+ "		CASE WHEN bpd.bp_id=bpg.bp_id THEN bpd.allow_individual_apply=1 else bpd.bp_id=bpi.bp_id end"
    	+ " WHERE a.agent_no=#{agentNo} and s.effective_status='1' "
    	+ " ORDER BY bpd.bp_id,"
		+ " case when s.service_type in ('10000','10001') then '1' else '0' end")
    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getAgentShareInfos(@Param("agentNo")String agentNo);
    
    @SelectProvider(type=SqlProvider.class,method="getServiceRate")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getServiceRate(@Param("bpIds")List<Integer> bpIds,@Param("agentNo") String agentId);
    
    @SelectProvider(type=SqlProvider.class,method="getNewServiceRate")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getNewServiceRate(@Param("bpIds")List<Integer> bpIds,@Param("agentNo") String agentId);
    
    @SelectProvider(type=SqlProvider.class,method="getNewServiceQuota")
    @ResultType(ServiceQuota.class)
	List<ServiceQuota> getNewServiceQuota(@Param("bpIds")List<Integer> bpIds, @Param("agentNo")String agentId);
    
    @SelectProvider(type=SqlProvider.class,method="getServiceQuota")
    @ResultType(ServiceQuota.class)
	List<ServiceQuota> getServiceQuota(@Param("bpIds")List<Integer> bpIds, @Param("agentNo")String agentId);
    
    @Insert("insert into agent_info(agent_no,agent_node,agent_name,agent_level,parent_id,one_level_id,is_oem,team_id,email,phone,cluster,invest,"
    		+ "agent_area,mobilephone,link_name,invest_amount,address," +
			"account_name,account_province,account_city,account_type,account_no,bank_name,cnaps_no,sale_name,creator,"
    		+ "mender,status,create_date,public_qrcode,manager_logo,logo_remark,client_logo,custom_tel,is_approve,count_level,province,city,area,profit_switch,cash_back_switch,"
			+ "agent_oem,agent_type,agent_share_level,id_card_no,full_prize_switch,not_full_deduct_switch) values("
    		+ "#{agent.agentNo},#{agent.agentNode},#{agent.agentName},#{agent.agentLevel},#{agent.parentId},#{agent.oneLevelId},#{agent.isOem},#{agent.teamId},"
    		+ "#{agent.email},#{agent.phone},#{agent.cluster},#{agent.invest},#{agent.agentArea},#{agent.mobilephone},#{agent.linkName},#{agent.investAmount},"
    		+ "#{agent.address}," +
			"#{agent.accountName},#{agent.accountProvince},#{agent.accountCity},#{agent.accountType},#{agent.accountNo},#{agent.bankName},#{agent.cnapsNo},#{agent.saleName},#{agent.creator},"
    		+ "#{agent.mender},#{agent.status},now(),#{agent.publicQrcode},#{agent.managerLogo},#{agent.logoRemark},"
    		+ "#{agent.clientLogo},#{agent.customTel},#{agent.isApprove},#{agent.countLevel},#{agent.province},#{agent.city},#{agent.area},#{agent.profitSwitch},#{agent.cashBackSwitch},"
			+ "#{agent.agentOem},#{agent.agentType},#{agent.agentShareLevel},#{agent.idCardNo},#{agent.fullPrizeSwitch},#{agent.notFullDeductSwitch})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="agent.id", before=false, resultType=Long.class)  
    public int insertAgentInfo(@Param("agent")AgentInfo agent);
    
    //新增代理商的用户
    @Insert("insert into user_info(user_id,user_name,mobilephone,status,password,team_id,create_time,email) values(#{agent.userId},#{agent.userName},"
    		+ "#{agent.mobilephone},1,#{agent.password},#{agent.teamId},now(),#{agent.email})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="agent.id", before=false, resultType=Long.class)  
    public int insertAgentUser(@Param("agent")AgentUserInfo agent);
    //新增代理商的结构组织
    @Insert("insert into user_entity_info(user_id,user_type,entity_id,apply,manage,status,last_notice_time,is_agent) values(#{agent.userId},1,#{agent.entityId},1,1,1,now(),#{agent.isAgent})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="agent.id", before=false, resultType=Long.class)  
    public int insertAgentEntity(@Param("agent")AgentUserEntity agent);
    //给一级代理商授权管理员授
    @Insert("insert into agent_user_role(user_id,role_id) values(#{userId},#{roleId})")
    public int insertAgentRole(@Param("userId")Long userId,@Param("roleId")Long roleId);
    //修改代理商是否已开账户
    @Update("update agent_info set has_account=#{status} where agent_no=#{agentNo}")
    public int updateAgentAccount(@Param("agentNo")String agentNo,@Param("status")int status);
    
    //修改非一级代理商是否已开账户
    @Update("update agent_info set has_account=#{status} where agent_level>1 and agent_no=#{agentNo}")
    public int updateNotOneAgentAccount(@Param("agentNo")String agentNo,@Param("status")int status);
    
    @Select("select uei.* from user_entity_info uei,user_info ui"
    		+ " where uei.entity_id=#{agentNo} and uei.user_type=1 and uei.apply=1 and uei.is_agent=1"
    		+ " and uei.user_id=ui.user_id and ui.team_id=#{teamId}")
    public Map<String,Object> getAgentEntity(@Param("agentNo")String agentNo, @Param("teamId")Integer teamId);
    
    @Select("select * from user_info where user_id=#{userId}")
    Map<String, Object> getUserInfoById(String userId);
    
    @Delete("delete from agent_user_role where user_id=#{userId} and role_id=5")
    public int delAgentRole(String userId);
    @Delete("delete from user_info where user_id=#{userId}")
    public int delAgentUser(String userId);
    @Delete("delete from user_entity_info where user_id=#{userId} and user_type=1")
    public int delAgentEntity(String userId);
    
    @InsertProvider(type=SqlProvider.class,method="insertAgentProductList")
    int insertAgentProductList(@Param("list")List<JoinTable> bp);

	@InsertProvider(type=SqlProvider.class,method="insertHappyBackType")
	int insertHappyBackType(@Param("list")List<ActivityHardwareType> bp);

	@Insert("insert into agent_activity (activity_type_no,agent_no,agent_node,cash_back_amount,tax_rate,repeat_register_amount,repeat_register_ratio," +
			"full_prize_amount,repeat_full_prize_amount,not_full_deduct_amount,repeat_not_full_deduct_amount," +
			"sub_type,one_reward_amount,two_reward_amount,three_reward_amount,four_reward_amount," +
			"one_repeat_reward_amount,two_repeat_reward_amount,three_repeat_reward_amount,four_repeat_reward_amount," +
			"scan_reward_amount,scan_repeat_reward_amount,all_reward_amount,all_repeat_reward_amount" +
			") " +
			"SELECT activity_type_no,agent_no,agent_node,'0','0','0','0','0','0','0','0'," +
			"sub_type,'0','0','0','0'," +
			"'0','0','0','0', "  +
			"'0','0','0','0' "  +
			" from activity_hardware_type,agent_info " +
			"where status='1' and agent_level!='1' and agent_no = #{agentNo} and activity_type_no in (${types})")
	int insertHappyBackTypeByAgentLevel(@Param("agentNo")String agentNo,@Param("types")String types);

	@UpdateProvider(type=SqlProvider.class,method="updateHappyBackTypeByAgentLevel")
	int updateHappyBackTypeByAgentLevel(@Param("agentNoes")String agentNoes,@Param("types")String types);

	@Insert("insert into agent_activity (activity_type_no,agent_no,agent_node,cash_back_amount,tax_rate,repeat_register_amount,repeat_register_ratio," +
			"full_prize_amount,repeat_full_prize_amount,not_full_deduct_amount,repeat_not_full_deduct_amount," +
			"sub_type,one_reward_amount,two_reward_amount,three_reward_amount,four_reward_amount," +
			"one_repeat_reward_amount,two_repeat_reward_amount,three_repeat_reward_amount,four_repeat_reward_amount," +
			"scan_reward_amount,scan_repeat_reward_amount,all_reward_amount,all_repeat_reward_amount" +
			") "+
			"SELECT aht.activity_type_no,ai.agent_no,ai.agent_node,'0','0','0','0','0','0','0','0', " +
			"aht.sub_type,'0','0','0','0','0','0','0','0','0','0','0','0' " +
			"from activity_hardware_type aht,agent_info ai " +
			"LEFT JOIN agent_activity aa on ai.agent_no=aa.agent_no and aa.activity_type_no =#{activityTypeNo} " +
			"where ai.status='1' and ai.agent_level!='1' and ai.agent_no like #{agentNo} and aht.activity_type_no =#{activityTypeNo} and aa.agent_no is null ")
	int insertHlfTerByAgentLevel(@Param("agentNo")String agentNo,@Param("activityTypeNo")String activityTypeNo);

	@UpdateProvider(type=SqlProvider.class,method="updateHlfTerByAgentLevel")
	int updateHlfTerByAgentLevel(@Param("agentNoes")String agentNoes,@Param("activityTypeNo")String activityTypeNo);

	@Insert("insert into agent_activity (activity_type_no,agent_no,agent_node,cash_back_amount,tax_rate,repeat_register_amount,repeat_register_ratio," +
			"full_prize_amount,repeat_full_prize_amount,not_full_deduct_amount,repeat_not_full_deduct_amount," +
			"sub_type," +
			"one_reward_amount,two_reward_amount,three_reward_amount,four_reward_amount," +
			"one_repeat_reward_amount,two_repeat_reward_amount,three_repeat_reward_amount,four_repeat_reward_amount," +
			"scan_reward_amount,scan_repeat_reward_amount,all_reward_amount,all_repeat_reward_amount" +
			",one_sub_reward_amount,one_sub_repeat_reward" +
			")"+
			"SELECT aht.activity_type_no,ai.agent_no,ai.agent_node,aht.cash_back_amount,'1',aht.repeat_register_amount,'1'," +
			"aht.full_amount,aht.repeat_full_amount,aht.empty_amount,aht.repeat_empty_amount," +
			"aht.sub_type," +
			"ifnull(xsc.one_reward_agent_amount,aht.one_reward_amount)," +
			"ifnull(xsc.two_reward_agent_amount,aht.two_reward_amount)," +
			"ifnull(xsc.three_reward_agent_amount,aht.three_reward_amount)," +
			"aht.four_reward_amount," +
			"ifnull(xsc.one_repeat_reward_agent_amount,aht.one_repeat_reward_amount)," +
			"ifnull(xsc.two_repeat_reward_agent_amount,aht.two_repeat_reward_amount)," +
			"ifnull(xsc.three_repeat_reward_agent_amount,aht.three_repeat_reward_amount)," +
			"aht.four_repeat_reward_amount," +
			"harc.scan_reward_amount,harc.scan_repeat_reward_amount,harc.all_reward_amount,harc.all_repeat_reward_amount" +
			",aht.one_sub_reward_amount,aht.one_sub_repeat_reward" +
			" from activity_hardware_type aht " +
			"LEFT JOIN hlf_agent_reward_config harc on harc.id=aht.hlf_agent_reward_config_id " +
			"LEFT JOIN xhlf_smart_config xsc on xsc.id=aht.xhlf_smart_config_id " +
			",agent_info ai " +
			"LEFT JOIN agent_activity aa on ai.agent_no=aa.agent_no and aa.activity_type_no =#{activityTypeNo} " +
			"where ai.status='1' and ai.agent_no = #{agentNo} and aht.activity_type_no =#{activityTypeNo} and aa.agent_no is null ")
	int insertHlfTerByOneAgentLevel(@Param("agentNo")String agentNo,@Param("activityTypeNo")String activityTypeNo);

	@InsertProvider(type=SqlProvider.class,method="insertAgentShareList")
    int insertAgentShareList(@Param("list")List<AgentShareRule> shareList);
    
    @SelectProvider(type=SqlProvider.class,method="queryAgentInfoList")
    @ResultType(AgentInfo.class)
    List<AgentInfo> queryAgentInfoList(@Param("params")Map<String, Object> params, @Param("page")Page<AgentInfo> page);
    
    @Select("select id,bp_id key1,`status` key2,agent_no key3 from agent_business_product where agent_no=#{agentNo} and bp_id=#{bpId}")
    @ResultType(JoinTable.class)
    JoinTable getAgentPro(@Param("agentNo")String agentNo,@Param("bpId")Integer bpId);
    //更新状态    
    @UpdateProvider(type=SqlProvider.class,method="updateAgentProStatus")
    int updateAgentProStatus(@Param("params")Map<String,Object> map);
    
    @Select("SELECT SUM(counts) FROM (SELECT COUNT(1) counts FROM merchant_info m WHERE m.agent_no=#{agentNo}"
    		+ " UNION ALL SELECT COUNT(1) counts FROM agent_info a WHERE"
    		+ " EXISTS (SELECT 1 FROM agent_info b WHERE a.agent_node LIKE CONCAT(b.agent_node,'%') AND b.agent_no=#{agentNo}) ) t")
    @ResultType(Integer.class)
    Integer getAgentAndMerchantCount(String agentNo);
    
    @Delete("delete from service_manage_quota where agent_no=#{agentNo}")
	int deleteAgentQuotas(String agentNo);
    @Delete("delete from service_manage_rate where agent_no=#{agentNo}")
	int deleteAgentRates(String agentNo);
    @Delete("delete FROM agent_share_rule_task USING agent_share_rule_task,agent_share_rule r WHERE r.agent_no=#{agentNo} AND r.id =agent_share_rule_task.share_id")
	int deleteAgentShareTasks(String agentNo);
    @Delete("delete from agent_share_rule where agent_no=#{agentNo}")
	int deleteAgentShares(String agentNo);
    @Delete("delete from agent_business_product where agent_no=#{agentNo}")
	int deleteAgentProducts(String agentNo);
    @Delete("delete from agent_info where agent_no=#{agentNo}")
	int deleteAgent(String agentNo);
    
    @Update("update agent_info set status=#{status} where agent_no=#{agentNo}")
   	int updateStatus(AgentInfo agent);

    @UpdateProvider(type=SqlProvider.class, method="updateProfitStatus")
    int updateProfitStatus(@Param("agentNode")String agentNode, @Param("profitSwitch")Integer profitSwitch);

    @UpdateProvider(type=SqlProvider.class, method="updatePromotionStatus")
    int updatePromotionStatus(@Param("agentNode")String agentNode, @Param("promotionSwitch")Integer promotionSwitch);

	@UpdateProvider(type=SqlProvider.class, method="updateCashBackStatus")
	int updateCashBackStatus(@Param("agentNode")String agentNode, @Param("cashBackSwitch")Integer cashBackSwitch);


	@UpdateProvider(type=SqlProvider.class, method="updateAgentUser")
    int updateAgentUser(@Param("agentUser")AgentUserInfo agentUser);
    
    @Update("update agent_info set agent_name=#{agent.agentName},is_oem=#{agent.isOem},email=#{agent.email},phone=#{agent.phone},invest=#{agent.invest},"
    		+ "agent_area=#{agent.agentArea},mobilephone=#{agent.mobilephone},link_name=#{agent.linkName},invest_amount=#{agent.investAmount},address=#{agent.address},account_name=#{agent.accountName},"
    		+ "account_type=#{agent.accountType},account_no=#{agent.accountNo}," +
			"bank_name=#{agent.bankName},account_province=#{agent.accountProvince},account_city=#{agent.accountCity},cnaps_no=#{agent.cnapsNo},sale_name=#{agent.saleName},mender=#{agent.mender},"
    		+ "public_qrcode=#{agent.publicQrcode},manager_logo=#{agent.managerLogo},logo_remark=#{agent.logoRemark},id_card_no=#{agent.idCardNo},agent_oem=#{agent.agentOem},agent_share_level=#{agent.agentShareLevel},"
    		+ "client_logo=#{agent.clientLogo},custom_tel=#{agent.customTel},is_approve=#{agent.isApprove},count_level=#{agent.countLevel},province=#{agent.province},city=#{agent.city},area=#{agent.area} "
    		+ "where id=#{agent.id}")
    int updateAgent(@Param("agent")AgentInfo agent);
    
    @Select("select * from  user_info where mobilephone=#{mobilephone} and team_id=#{teamId}")
    @ResultType(AgentUserInfo.class)
	AgentUserInfo selectAgentUser(@Param("mobilephone")String mobilephone,@Param("email")String email,@Param("teamId") String teamId);

    @Select("select * from  user_entity_info where user_id=#{userId} and entity_id=#{agentNo}")
    @ResultType(AgentUserEntity.class)
	AgentUserEntity selectAgentUserEntity(@Param("userId")String userId,@Param("agentNo") String agentNo);
    
    @SelectProvider(type=SqlProvider.class, method="existAgentByMobilephoneAndTeamId")
	int existAgentByMobilephoneAndTeamId(@Param("agent")AgentInfo agent);
    
    @Select("select CONCAT(agent_node,'%') as agentNode from agent_info where sale_name=#{name} and agent_level='1'")
    @ResultType(String.class)
    List<String> selectAllNodeSale(@Param("name")String name);
    
    @Select("select agent_no from agent_info where sale_name=#{name} and agent_level='1'")
    @ResultType(String.class)
    List<String> queryAllOneAgentBySale(@Param("name")String name);
    
    @Select("select * from agent_info where agent_node like #{node}")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAllInfoSale(@Param("node")String node);
    
    @SelectProvider(type=SqlProvider.class,method="queryAgentInfoListSale")
    @ResultType(AgentInfo.class)
    List<AgentInfo> queryAgentInfoListSale(@Param("str")String str,@Param("params")Map<String, Object> params, @Param("page")Page<AgentInfo> page);
    
    @Select("select at.*,s.service_type from agent_share_rule_task at " +
			"LEFT JOIN agent_share_rule a ON a.id = at.share_id " +
			"LEFT JOIN service_info s ON a.service_id = s.service_id " +
			"where at.check_status=0 and at.share_id=#{id} order by at.efficient_date desc")
    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getAgentShareRuleTaskByRule(AgentShareRule rule);
    
    @Select("select at.*,s.service_type from agent_share_rule_task at " +
			"LEFT JOIN agent_share_rule a ON a.id = at.share_id " +
			"LEFT JOIN service_info s ON a.service_id = s.service_id " +
    		"where at.share_id=#{id} order by at.efficient_date desc")
    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getAllAgentShareRuleTaskByRule(AgentShareRule rule);

    /**
     * 修改分润 tgh
     * @param list
     * @return
     */
    @UpdateProvider(type=SqlProvider.class, method="updateAgentShareList")
	int updateAgentShareList(@Param("list")List<AgentShareRule> list);
    
    @Select("select a.agent_no,a.agent_node from agent_info a where a.sale_name=#{realName} and a.agent_no=#{agentNo}")
    @ResultType(AgentInfo.class)
	AgentInfo findOneAgentByAgentNoAndRealName(@Param("realName")String realName, @Param("agentNo")String agentNo);

	 /**
	 * 新增代理商时回显分润信息
	 * @param bpIds
	 * @param agentId
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="getProfits")
	@ResultType(AgentShareRule.class)
	List<AgentShareRule> getProfits(@Param("bpIds")List<Integer> bpIds, @Param("agentId")String agentId);

	/**
	 * 根据业务产品，查询同组业务产品的所有除提现外的服务
	 * @author tans
	 * @date 2017年4月15日 下午4:39:32
	 * @param rule
	 * @return
	 */
	@Select("SELECT smr.service_id FROM service_info si,business_product_info bpi,service_manage_rate smr,agent_business_product abp,"
			+ "(SELECT bpg1.bp_id FROM business_product_group bpg1,business_product_group bpg2 "
			+ " WHERE bpg1.group_no = bpg2.group_no AND bpg1.bp_id != #{bpId} AND bpg2.bp_id = #{bpId}"
			+ " ) bpids "
			+ " WHERE bpi.bp_id = bpids.bp_id AND bpi.service_id = si.service_id "
			+ " AND abp.bp_id=bpi.bp_id"
			+ " AND abp.agent_no=#{agentNo}"
			+ " AND smr.service_id=si.service_id"
			+ " AND smr.agent_no=0"
			+ " AND si.service_type=#{serviceType}"
			+ " AND si.effective_status='1'"
			+ " AND smr.card_type=#{cardType}"
			+ " AND smr.holidays_mark=#{holidaysMark};")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getOtherServiceRate(AgentShareRule rule);
	
	/**
	 * 根据业务产品，查询同组业务产品的所有提现服务
	 * @author tans
	 * @date 2017年4月25日 上午9:28:31
	 * @param rule
	 * @return
	 */
	@Select("SELECT si3.service_id FROM"
			+ " service_info si1,"
	+ " business_product_info bi,"
	+ " business_product_group bg1,"
	+ " business_product_group bg2,"
	+ " business_product_info bi2,"
	+ " service_info si2,"
	+ " service_manage_rate smr1,"
	+ " service_info si3,"
	+ " agent_business_product abp"
	+ " WHERE"
	+ " si1.link_service = #{serviceId}"
	+ " AND si1.service_id = bi.service_id"
	+ " AND bi.bp_id = bg1.bp_id"
	+ " AND bg1.group_no = bg2.group_no"
	+ " AND bg1.bp_id <> bg2.bp_id"
	+ " AND abp.bp_id = bg2.bp_id"
	+ " AND abp.agent_no = #{agentNo}"
	+ " AND bg2.bp_id = bi2.bp_id"
	+ " AND bi2.service_id = si2.service_id"
	+ " AND si2.service_type = si1.service_type"
	+ " AND si2.link_service = smr1.service_id"
	+ " AND smr1.card_type = #{cardType}"
	+ " AND smr1.holidays_mark = #{holidaysMark}"
	+ " AND smr1.agent_no = 0"
	+ " AND si3.service_id = si2.link_service"
	+ " AND si3.effective_status = '1'"
	+ " AND si3.service_type = #{serviceType}")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getOtherTXServiceRate(AgentShareRule rule);
    
	/**
	 * 检查user_info表是否已存在手机号与组织唯一，或者邮箱与组织唯一
	 * @author tans
	 * @date 2017年4月13日 下午6:52:01
	 * @param agentUser
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="existUserInfo")
	int existUserInfo(@Param("agentUser")AgentUserInfo agentUser);
	
	/**
	 * 根据手机号和teamId找到唯一的user_info
	 * @author tans
	 * @date 2017年4月14日 上午11:35:13
	 * @param mobilephone
	 * @param teamId
	 * @return
	 */
	@Select("select * from user_info where mobilephone=#{mobilephone} and team_id=#{teamId}")
    @ResultType(AgentUserInfo.class)
	AgentUserInfo getUserInfo(@Param("mobilephone")String mobilephone, @Param("teamId")Integer teamId);

	@Select("select pai.agent_no,pai.profit_switch from agent_info ai INNER JOIN agent_info pai on ai.parent_id=pai.agent_no"
			+" where ai.agent_node=#{agentNode};")
	@ResultType(AgentInfo.class)
	AgentInfo getParentProfitsSwitch(String agentNode);
	
	@Select("select agent_no, agent_name, agent_level from agent_info where agent_no = #{agentNo} and agent_level = #{agentLevel}")
	@ResultType(AgentInfo.class)
	AgentInfo selectAgent(@Param("agentNo")String agentNo, @Param("agentLevel")int agentLevel);

	@Select("select aht.*,fm.function_name," +
			"harc.scan_reward_amount,harc.scan_repeat_reward_amount,harc.all_reward_amount,harc.all_repeat_reward_amount "+
			"from activity_hardware_type aht " +
			"LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code "+
			"LEFT JOIN hlf_agent_reward_config harc ON harc.id = aht.hlf_agent_reward_config_id"
	)
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackType();

	@Select("select aht.*,fm.function_name," +
			"harc.scan_reward_amount,harc.scan_repeat_reward_amount,harc.all_reward_amount,harc.all_repeat_reward_amount "+
			"from activity_hardware_type aht " +
			"LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code "+
			"LEFT JOIN hlf_agent_reward_config harc ON harc.id = aht.hlf_agent_reward_config_id " +
			" where aht.activity_code=#{activityCode} and aht.sub_type=#{subType} "
	)
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackTypeBySubType(@Param("activityCode")String activityCode,@Param("subType")String subType);


	@SelectProvider(type=SqlProvider.class,method="selectHappyBackTypeWithTeamId")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackTypeWithTeamId(@Param("teamIds") List<String> teamIds);

	@SelectProvider(type=SqlProvider.class,method="selectHappyBackWithTeamIdAndAgentNo")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackWithTeamIdAndAgentNo(@Param("teamIds") List<String> teamIds, @Param("agentNo") String agentNo);

	@Select("SELECT aa.activity_type_no,aht.activity_type_name,fm.function_name,aht.trans_amount," +
			" aa.full_prize_amount full_amount,aa.repeat_full_prize_amount repeat_full_amount," +
			" aa.not_full_deduct_amount empty_amount,aa.repeat_not_full_deduct_amount repeat_empty_amount," +
			" aa.cash_back_amount,aa.tax_rate,aa.repeat_register_amount,aa.repeat_register_ratio," +
			" aa.sub_type,aa.one_reward_amount,aa.two_reward_amount,aa.three_reward_amount,aa.four_reward_amount," +
			" aa.one_repeat_reward_amount,aa.two_repeat_reward_amount,aa.three_repeat_reward_amount,aa.four_repeat_reward_amount" +
//			",aa.one_sub_reward_amount,aa.one_sub_repeat_reward" +
			" FROM agent_activity aa " +
			" LEFT JOIN activity_hardware_type aht ON aht.activity_type_no = aa.activity_type_no " +
			" LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code " +
			" WHERE aa.agent_no = #{agentNo}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackTypeByAgentNo(@Param("agentNo") String agentNo);

	/**
	 * 通过代理商，活动编号，subType类型 ,查询活动代理商数据
	 * @param agentNo
	 * @return
	 */
	@Select("SELECT aa.activity_type_no,aht.activity_type_name,fm.function_name,aht.trans_amount," +
			" aa.full_prize_amount full_amount,aa.repeat_full_prize_amount repeat_full_amount," +
			" aa.not_full_deduct_amount empty_amount,aa.repeat_not_full_deduct_amount repeat_empty_amount," +
			" aa.cash_back_amount,aa.tax_rate,aa.repeat_register_amount,aa.repeat_register_ratio," +
			" aa.sub_type,aa.one_reward_amount,aa.two_reward_amount,aa.three_reward_amount,aa.four_reward_amount," +
			" aa.one_repeat_reward_amount,aa.two_repeat_reward_amount,aa.three_repeat_reward_amount,aa.four_repeat_reward_amount " +
			",aht.one_sub_trans_amount,aa.one_sub_reward_amount,aa.one_sub_repeat_reward" +
			" FROM agent_activity aa " +
			" LEFT JOIN activity_hardware_type aht ON aht.activity_type_no = aa.activity_type_no " +
			" LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code " +
			" WHERE aa.agent_no = #{agentNo} and aht.activity_code=#{activityCode} and aht.sub_type=#{subType}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyBackTypeByAgentNoCodeSubType(@Param("agentNo") String agentNo,@Param("activityCode") String activityCode,@Param("subType") String subType);

	@Select("SELECT activity_type_no FROM agent_activity WHERE agent_no = #{agentNo}")
	List<String> selectHbTypeByAgentNo(@Param("agentNo") String agentNo);

	@Select("SELECT id,role_name FROM agent_shiro_role where create_operator='admin'")
	List<Map> selectAgentShareRule();

	@SelectProvider(type=SqlProvider.class,method="queryAgentRoleOemList")
	@ResultType(Map.class)
	List<Map> queryAgentRoleOemList(@Param("map") Map<String, Object> params, @Param("page")Page<Map> page);

	@Insert("INSERT INTO agent_shiro_role_oem ( role_id, agent_oem, agent_type, remark) VALUES ( #{map.role_id}, #{map.agent_oem}, #{map.agent_type}, #{map.remark})")
	int insertAgentRoleOem(@Param("map") Map<String, Object> params);

	@Update("UPDATE agent_shiro_role_oem SET role_id=#{map.role_id}, agent_oem=#{map.agent_oem}, agent_type=#{map.agent_type}, remark=#{map.remark} WHERE id=#{map.id}")
	int updateAgentRoleOem(@Param("map") Map<String, Object> params);

	@Delete("Delete FROM agent_shiro_role_oem where id=#{id}")
	int deleteAgentRoleOem(@Param("id") Integer id);

	@SelectProvider(type=SqlProvider.class,method="selectAgentRoleOem")
	@ResultType(Map.class)
	Map<String,Object> selectAgentRoleOem(@Param("map") Map<String, Object> params);

	@SelectProvider(type=SqlProvider.class,method="selectUserAgentRoleOem")
	@ResultType(Map.class)
	List<Map> selectUserAgentRoleOem(@Param("map") Map<String, Object> params);

	//修改非一级代理商组织
	@Update("update agent_info set agent_oem=#{agentOem} where agent_level>1 and agent_node LIKE CONCAT(#{agentNode},'%') ")
	int updateNotOneAgentOem(@Param("agentNode")String agentNode,@Param("agentOem")String agentOem);

	@SelectProvider(type = SqlProvider.class, method = "selectAgentShareCheckList")
	@ResultType(AgentInfo.class)
	List<AgentInfo> selectAgentShareCheckList(@Param("page") Page<AgentInfo> page,@Param("baseInfo") AgentInfo agentInfo);

	@Select(
			"select id,agent_no,agent_node,agent_name,agent_level,one_level_id from agent_info where agent_no=#{agentNo}"
	)
    AgentInfo selectLittleInfo(@Param("agentNo")String agentNo);

	@Select("select agent_name agentName from agent_info where agent_no = #{agentNo}")
	String queryAgentNameByNo(@Param("agentNo") String agentNo);

	@Select("select one_level_id from agent_info where agent_no=#{agentNo}")
	@ResultType(String.class)
	String getOneAgentNo(@Param("agentNo") String agentNo);


	public class SqlProvider{
		public String selectHappyBackTypeWithTeamId(Map<String, Object> map){
			final List<String> ids = (List<String>)map.get("teamIds");
			String sql = new SQL(){
				{
					SELECT("b.*,a.org_id AS teamId"

					);
					FROM("(SELECT DISTINCT aht.id,aht.activity_type_no,aht.activity_type_name,hp.org_id " +
							"FROM activity_hardware ah " +
							"JOIN activity_hardware_type aht ON ah.activity_code = aht.activity_code AND ah.activity_type_no = aht.activity_type_no " +
							"JOIN hardware_product hp ON hp.hp_id = ah.hard_id " +
							"WHERE " + createCondition(ids) +
							"ORDER BY aht.activity_type_no) a");
					LEFT_OUTER_JOIN("(SELECT aht.*,fm.function_name," +
							"harc.scan_reward_amount,harc.scan_repeat_reward_amount,harc.all_reward_amount,harc.all_repeat_reward_amount "+
							"FROM activity_hardware_type aht " +
							"LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code " +
							"LEFT JOIN hlf_agent_reward_config harc ON harc.id = aht.hlf_agent_reward_config_id " +
							") b ON a.id = b.id");
				}
			}.toString();
			return sql;
		}

		public String selectHappyBackWithTeamIdAndAgentNo(Map<String, Object> map){
			final List<String> ids = (List<String>)map.get("teamIds");
			String sql = new SQL(){
				{
					SELECT("c.activity_type_no,b.activity_type_name,b.function_name,b.trans_amount," +
							"c.scan_reward_amount,c.scan_repeat_reward_amount,c.all_reward_amount,c.all_repeat_reward_amount,"+
							"c.full_prize_amount full_amount,c.repeat_full_prize_amount repeat_full_amount," +
							"c.not_full_deduct_amount empty_amount,c.repeat_not_full_deduct_amount repeat_empty_amount," +
							"c.cash_back_amount,c.tax_rate,c.repeat_register_amount,c.repeat_register_ratio ,a.org_id AS teamId," +
							"c.sub_type,c.one_reward_amount,c.two_reward_amount,c.three_reward_amount,c.four_reward_amount," +
							"c.one_repeat_reward_amount,c.two_repeat_reward_amount,c.three_repeat_reward_amount,c.four_repeat_reward_amount,"+
							"c.scan_reward_amount,c.scan_repeat_reward_amount,c.all_reward_amount,c.all_repeat_reward_amount "

					);
					FROM("(SELECT DISTINCT aht.id,aht.activity_type_no,aht.activity_type_name,hp.org_id " +
							"FROM activity_hardware ah " +
							"JOIN activity_hardware_type aht ON ah.activity_code = aht.activity_code AND ah.activity_type_no = aht.activity_type_no " +
							"JOIN hardware_product hp ON hp.hp_id = ah.hard_id " +
							"WHERE " + createCondition(ids) +
							"ORDER BY aht.activity_type_no) a");
					JOIN("(SELECT * FROM agent_activity WHERE agent_no=#{agentNo}) c ON c.activity_type_no=a.activity_type_no");
					LEFT_OUTER_JOIN("(SELECT aht.*,fm.function_name " +
							"FROM activity_hardware_type aht " +
							"LEFT JOIN function_manage fm ON fm.function_number = aht.activity_code " +
							") b ON a.id = b.id");
				}
			}.toString();
			return sql;
		}

		private String createCondition(List<String> ids){
			StringBuilder condition = new StringBuilder("hp.org_id IN (");
			for (int i = 0; i < ids.size(); i++) {
				condition.append("'"+ids.get(i)+"'");
				if(i != ids.size() - 1){
					condition.append(",");
				}
			}
			condition.append(")");
			return condition.toString();
		}



    	public String existUserInfo(Map<String, Object> map){
    		final AgentUserInfo agentUser = (AgentUserInfo) map.get("agentUser");
    		return new SQL(){{
    			SELECT("count(1)");
    			FROM("user_info");
    			WHERE("team_id=#{agentUser.teamId} and (mobilephone=#{agentUser.mobilephone} OR email=#{agentUser.email})");
    			if(agentUser.getUserId()!=null){
    				WHERE("id<>#{agentUser.userId}");
    			}
    		}}.toString();
    	}

    	public String existAgentByMobilephoneAndTeamId(Map<String, Object> map){
    		final AgentInfo agent = (AgentInfo) map.get("agent");
    		String sql = new SQL(){{
    			SELECT("count(1)");
    			FROM("agent_info");
    			//WHERE("team_id=#{agent.teamId} and (mobilephone=#{agent.mobilephone} OR agent_name=#{agent.agentName} OR email=#{agent.email})");
    			WHERE("team_id=#{agent.teamId} and (mobilephone=#{agent.mobilephone} OR agent_name=#{agent.agentName})");
    			if(agent.getId()!=null){
    				WHERE("id<>#{agent.id}");
    			}
    		}}.toString();
    		return sql;
    	}

    	public String getServiceRate(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final List<Integer> ids=(List<Integer>)map.get("bpIds");
    		final String agentNo = (String) map.get("agentNo");
    		String sql = new SQL(){{
    			SELECT_DISTINCT("bpd.team_id,ser.*,info.service_name,info.service_type,info2.service_type as service_type2,bpd.bp_id,bpd.bp_name,bpd.allow_individual_apply");
    			FROM("business_product_info bp ");
    			INNER_JOIN("service_info info ON bp.service_id = info.service_id");
    			LEFT_OUTER_JOIN("service_info info2 ON info2.link_service = info.service_id");
    			LEFT_OUTER_JOIN("service_manage_rate ser ON bp.service_id = ser.service_id");
    			INNER_JOIN("business_product_define bpd on bp.bp_id=bpd.bp_id");
    			if(StringUtils.isNotBlank(agentNo) && !"0".equals(agentNo)){
    				LEFT_OUTER_JOIN("agent_info ai on ser.agent_no=ai.one_level_id");
        			WHERE("ai.agent_no=#{agentNo}");
    			} else {
    				WHERE("ser.agent_no=#{agentNo}");
    			}
    			WHERE("info.effective_status = '1'");
    			StringBuilder sb=new StringBuilder(" bp.bp_id in (-1,");
    			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
				for (int i = 0; i < ids.size(); i++) {
					sb.append(messageFormat.format(new Integer[] { i }));
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				sb.append(") ");
				WHERE(sb.toString());
				ORDER_BY("bp.bp_id,"
						+ "case when service_type2 is null THEN info.service_type ELSE service_type2 end,"
						+ "case when info.service_type in ('10000','10001') then '1' else '0' end"
						);
    		}}.toString();
    		return sql;
    	}
    	public String getNewServiceRate(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final List<Integer> ids=(List<Integer>)map.get("bpIds");
    		String sql =  new SQL(){{
    			SELECT_DISTINCT("ser.*,info.service_name serviceName");
    			FROM("business_product_info bp ");
    			LEFT_OUTER_JOIN("service_info info ON bp.service_id = info.service_id");
    			LEFT_OUTER_JOIN("service_manage_rate ser ON bp.service_id = ser.service_id");
    			WHERE("ser.agent_no='0'");
    			StringBuilder sb=new StringBuilder(" bp.bp_id in (-1,");
    			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
				for (int i = 0; i < ids.size(); i++) {
					sb.append(messageFormat.format(new Integer[] { i }));
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				sb.append(") ");
				WHERE(sb.toString());
				WHERE(" NOT EXISTS (SELECT 1 FROM agent_business_product abp LEFT JOIN "
						+ "business_product_info bpi ON  abp.bp_id =bpi.bp_id WHERE abp.agent_no=#{agentNo} "
						+ "AND ser.service_id=bpi.service_id)");
//				ORDER_BY("bp.bp_id");
    		}}.toString();
    		return sql;
    	}

    	public String getNewServiceQuota(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final List<Integer> ids=(List<Integer>)map.get("bpIds");
    		return new SQL(){{
    			SELECT_DISTINCT("quota.*,info.service_name");
    			FROM("business_product_info bp ");
    			LEFT_OUTER_JOIN("service_info info ON bp.service_id = info.service_id");
    			LEFT_OUTER_JOIN("service_manage_quota quota ON bp.service_id = quota.service_id");
    			WHERE("quota.agent_no='0'");
    			StringBuilder sb=new StringBuilder(" bp.bp_id in (-1,");
    			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
				for (int i = 0; i < ids.size(); i++) {
					sb.append(messageFormat.format(new Integer[] { i }));
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
				WHERE(sb.toString());
				WHERE(" NOT EXISTS (SELECT 1 FROM agent_business_product abp LEFT JOIN "
						+ "business_product_info bpi ON  abp.bp_id =bpi.bp_id WHERE abp.agent_no=#{agentNo} "
						+ "AND quota.service_id=bpi.service_id)");
//				ORDER_BY("bp.bp_id");
    		}}.toString();
    	}
    	public String getServiceQuota(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final List<Integer> ids=(List<Integer>)map.get("bpIds");
    		final String agentNo = (String) map.get("agentNo");
    		String sql = new SQL(){{
    			SELECT_DISTINCT("quota.*,info.service_name");
    			FROM("business_product_info bp ");
    			INNER_JOIN("service_info info ON bp.service_id = info.service_id");
    			LEFT_OUTER_JOIN("service_manage_quota quota ON bp.service_id = quota.service_id");
    			if(StringUtils.isNotBlank(agentNo) && !"0".equals(agentNo)){
    				LEFT_OUTER_JOIN("agent_info ai on quota.agent_no=ai.one_level_id");
        			WHERE("ai.agent_no=#{agentNo}");
    			} else{
    				WHERE("quota.agent_no=#{agentNo}");
    			}
    			WHERE("info.effective_status = '1'");
    			StringBuilder sb=new StringBuilder(" bp.bp_id in (-1,");
    			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
				for (int i = 0; i < ids.size(); i++) {
					sb.append(messageFormat.format(new Integer[] { i }));
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
				WHERE(sb.toString());
//				ORDER_BY("bp.bp_id");
    		}}.toString();
    		return sql;
    	}

    	public String insertAgentShareList(Map<String, List<AgentShareRule>> param){
			List<AgentShareRule> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into agent_share_rule(agent_no,service_id,card_type,holidays_mark,efficient_date,disabled_date,profit_type,per_fix_income,per_fix_inrate,safe_line,capping,"
            		+ "share_profit_percent,ladder,cost_rate_type,per_fix_cost,cost_rate,cost_capping,cost_safeline,check_status,lock_status,ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,"
            		+ "ladder3_max,ladder4_rate,ladder4_max) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].agentNo},#'{'list[{0}].serviceId},#'{'list[{0}].cardType},#'{'list[{0}].holidaysMark},now(),#'{'list[{0}].disabledDate},"
            		+ "#'{'list[{0}].profitType},#'{'list[{0}].perFixIncome},#'{'list[{0}].perFixInrate},#'{'list[{0}].safeLine},#'{'list[{0}].capping},#'{'list[{0}].shareProfitPercent},#'{'list[{0}].ladder},#'{'list[{0}].costRateType},"
            		+ "#'{'list[{0}].perFixCost},#'{'list[{0}].costRate},#'{'list[{0}].costCapping},#'{'list[{0}].costSafeline},#'{'list[{0}].checkStatus},#'{'list[{0}].lockStatus},#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},"
            		+ "#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}
    	
    	public String insertAgentProductList(Map<String, List<JoinTable>> param){
			List<JoinTable> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into agent_business_product(agent_no,bp_id,status) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].key3},#'{'list[{0}].key1},#'{'list[{0}].key2})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
		}

		public String insertHappyBackType(Map<String, List<ActivityHardwareType>> param){
			List<ActivityHardwareType> list = param.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("insert into agent_activity (activity_type_no,agent_no,agent_node,cash_back_amount,tax_rate,repeat_register_amount,repeat_register_ratio," +
					"full_prize_amount,repeat_full_prize_amount,not_full_deduct_amount,repeat_not_full_deduct_amount," +
					"sub_type,one_reward_amount,two_reward_amount,three_reward_amount,four_reward_amount," +
					"one_repeat_reward_amount,two_repeat_reward_amount,three_repeat_reward_amount,four_repeat_reward_amount," +
					"scan_reward_amount,scan_repeat_reward_amount,all_reward_amount,all_repeat_reward_amount" +
					",one_sub_reward_amount,one_sub_repeat_reward" +
					") values");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].activityTypeNo},#'{'list[{0}].agentNo},#'{'list[{0}].agentNode},#'{'list[{0}].cashBackAmount},#'{'list[{0}].taxRate},#'{'list[{0}].repeatRegisterAmount},#'{'list[{0}].repeatRegisterRatio}," +
					"#'{'list[{0}].fullAmount},#'{'list[{0}].repeatFullAmount},#'{'list[{0}].emptyAmount},#'{'list[{0}].repeatEmptyAmount}," +
					"#'{'list[{0}].subType},#'{'list[{0}].oneRewardAmount},#'{'list[{0}].twoRewardAmount},#'{'list[{0}].threeRewardAmount},#'{'list[{0}].fourRewardAmount}," +
					"#'{'list[{0}].oneRepeatRewardAmount},#'{'list[{0}].twoRepeatRewardAmount},#'{'list[{0}].threeRepeatRewardAmount},#'{'list[{0}].fourRepeatRewardAmount}," +
					"#'{'list[{0}].scanRewardAmount},#'{'list[{0}].scanRepeatRewardAmount},#'{'list[{0}].allRewardAmount},#'{'list[{0}].allRepeatRewardAmount}" +
					",#'{'list[{0}].oneSubRewardAmount},#'{'list[{0}].oneSubRepeatReward}" +
					")");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
    	
    	public String queryAgentInfoList(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final Map<String,Object> param=(Map<String,Object>)map.get("params");
    		String sql = new SQL(){{
    			SELECT("*,parentInfo.agent_name parentAgentName");
    			FROM("agent_info info");
    			LEFT_OUTER_JOIN("agent_info parentInfo on info.parent_id=parentInfo.agent_no");
    			INNER_JOIN("agent_info info1 on info.one_level_id = info1.agent_no");
    			String temp;
//    			if((param.get("shareCheck")!=null&&StringUtils.isNotBlank(param.get("shareCheck").toString()))){
//    				if("1".equals(param.get("shareCheck").toString())){
//    					temp="(not EXISTS (SELECT 1 FROM agent_share_rule_task asrt LEFT JOIN agent_share_rule r on r.id=asrt.share_id WHERE info.agent_no = r.agent_no AND asrt.check_status in(0,2) ";
//    					WHERE(temp+") and not EXISTS(SELECT 1 FROM agent_share_rule r WHERE info.agent_no = r.agent_no AND r.check_status in(0,2) )) ");
//    				} else if("0".equals(param.get("shareCheck").toString())){
//    					temp="(EXISTS (SELECT 1 FROM agent_share_rule_task asrt LEFT JOIN agent_share_rule r on r.id=asrt.share_id WHERE info.agent_no = r.agent_no AND asrt.check_status =0 ";
//    					WHERE(temp+") or  EXISTS(SELECT 1 FROM agent_share_rule r WHERE info.agent_no = r.agent_no AND r.check_status =0 )) ");
//    				} else if("2".equals(param.get("shareCheck").toString())){
//    					temp="(EXISTS (SELECT 1 FROM agent_share_rule_task asrt LEFT JOIN agent_share_rule r on r.id=asrt.share_id WHERE info.agent_no = r.agent_no AND asrt.check_status =2 ";
//    					WHERE(temp+")or  EXISTS(SELECT 1 FROM agent_share_rule r WHERE info.agent_no = r.agent_no AND r.check_status =2 )) ");
//    				}
//    			}
    			//315xytgh
//    			if((param.get("rateCheck")!=null&&StringUtils.isNotBlank(param.get("rateCheck").toString()))
//    					){
//    				if("1".equals(param.get("rateCheck").toString())){
//    					temp="not EXISTS (SELECT 1 FROM service_manage_rate r WHERE info.agent_no = r.agent_no and r.check_status='0' ";
//    					WHERE(temp+") and info.agent_level=1 ");
//    				} else if("0".equals(param.get("rateCheck").toString())){
//    					temp="EXISTS (SELECT 1 FROM service_manage_rate r WHERE info.agent_no = r.agent_no and r.check_status='0' ";
//    					WHERE(temp+") and info.agent_level=1 ");
//    				}
//    			}
//				if((param.get("quotaCheck")!=null&&StringUtils.isNotBlank(param.get("quotaCheck").toString()))
//    					){
//					if("1".equals(param.get("quotaCheck").toString())){
//    					temp="not EXISTS (SELECT 1 FROM service_manage_quota r WHERE info.agent_no = r.agent_no and r.check_status=0 ";
//    					WHERE(temp+") and info.agent_level=1 ");
//    				} else if("0".equals(param.get("quotaCheck").toString())){
//    					temp="EXISTS (SELECT 1 FROM service_manage_quota r WHERE info.agent_no = r.agent_no and r.check_status=0 ";
//    					WHERE(temp+") and info.agent_level=1 ");
//    				}
//    			}
    			String agentName=(String)param.get("agentName");
    			if(StringUtils.isNotBlank(agentName)){
    				param.put("agentName",agentName+"%");
    				WHERE(" info.agent_name like #{params.agentName}");
    			}
				if(param.get("accountNo")!=null&&StringUtils.isNotBlank(param.get("accountNo").toString())){
					WHERE(" info.account_no = #{params.accountNo}");
				}
				if(param.get("accountName")!=null&&StringUtils.isNotBlank(param.get("accountName").toString())){
					WHERE(" info.account_name = #{params.accountName}");
				}
				if(param.get("idCardNo")!=null&&StringUtils.isNotBlank(param.get("idCardNo").toString())){
					WHERE(" info.id_card_no = #{params.idCardNo}");
				}
    			if(param.get("teamId")!=null&&StringUtils.isNotBlank(param.get("teamId").toString())){
    				WHERE(" info.team_id = #{params.teamId}");
    			}
    			if(param.get("agentLevel")!=null&&!"-1".equals((param.get("agentLevel").toString()))){
    				WHERE(" info.agent_level = #{params.agentLevel}");
    			}
    			if(param.get("mobilephone")!=null&&StringUtils.isNotBlank(param.get("mobilephone").toString())){
    				WHERE(" info.mobilephone like concat(#{params.mobilephone},'%')");
    			}
    			if(param.get("hasAccount")!=null&&StringUtils.isNotBlank(param.get("hasAccount").toString())){
    				WHERE(" info.has_account = #{params.hasAccount}");
    			}
    			if(param.get("profitSwitch")!=null&&!"2".equals(param.get("profitSwitch").toString())){
    				WHERE(" info.profit_switch = #{params.profitSwitch}");
    			}
    			if(StringUtils.isNotBlank((String)param.get("agentNo"))){
    				if(StringUtils.isNotBlank((String)param.get("subAgentNo"))){
    					WHERE(" info.agent_node like concat(#{params.subAgentNo},'%')");
    				}else{	
    					WHERE(" info.agent_no = #{params.agentNo}");
    				}
    			}
        		if(param.get("saleName")!=null&&StringUtils.isNotBlank((String)param.get("saleName"))){
        			WHERE(" info1.sale_name=#{params.saleName} and info1.agent_level = '1' ");
        		}
        		if(param.get("promotionSwitch")!=null&&!"2".equals(param.get("promotionSwitch").toString())){
    				WHERE(" info.promotion_switch = #{params.promotionSwitch}");
    			}
    			if(param.get("cashBackSwitch")!=null&&!"2".equals(param.get("cashBackSwitch").toString())){
    				WHERE(" info.cash_back_switch = #{params.cashBackSwitch}");
    			}

    		}}.toString();
    		return sql;
    	}
    	
    	public String updateAgentProStatus(Map<String, Object> map){
    		@SuppressWarnings("unchecked")
			final Map<String,Object> agent=(Map<String,Object>)map.get("params");
    		String sql = new SQL(){{
    			UPDATE("agent_business_product bp");
    			SET("bp.status= #{params.status}");
    			//关闭的时候，需要联动
    			if((Integer)agent.get("status")==0){
    				agent.put("agentNode",(String)agent.get("agentNode")+"%");
    			}
				WHERE("EXISTS (SELECT 1 FROM agent_info a WHERE a.agent_node LIKE #{params.agentNode} AND a.agent_no=bp.agent_no)" );
    			WHERE("bp.bp_id=#{params.bpId}");
    		}}.toString();
    		return sql;
    	}

    	public String updateAgentUser(Map<String, Object> params){
    		final AgentUserInfo agentUser=(AgentUserInfo)params.get("agentUser");
    		return new SQL(){{
    			UPDATE("user_info");
    			SET("user_name=#{agentUser.userName},mobilephone=#{agentUser.mobilephone},email=#{agentUser.email}");
    			if(StringUtils.isNotBlank(agentUser.getPassword())){
    				SET("password=#{agentUser.password},update_pwd_time=now()");
    			}
    			WHERE("user_id=#{agentUser.userId}");
    		}}.toString();
    	}

    	public String queryAgentInfoListSale(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
			final Map<String,Object> param=(Map<String,Object>)map.get("params");
    		final String str=(String)map.get("str");
    		String sql= new SQL(){{
    			StringBuilder sb=new StringBuilder("(select info.*,parentInfo.agent_name parentAgentName from agent_info info "
    					+ "LEFT JOIN agent_info parentInfo on parentInfo.agent_no=info.parent_id ");
    			if(StringUtils.isNotBlank(str)){
					sb.append("where "+str+") as b ");
    	    	}else{
    	    		sb.append(") as b ");
    	    	}
    			SELECT("*");
    			FROM(sb.toString());
    			String temp;
//    			if((param.get("shareCheck")!=null&&StringUtils.isNotBlank(param.get("shareCheck").toString()))
//    					||(param.get("shareLock")!=null&&StringUtils.isNotBlank(param.get("shareLock").toString()))){
//    				temp="EXISTS (SELECT 1 FROM agent_share_rule r WHERE b.agent_no = r.agent_no ";
//    				if(param.get("shareCheck")!=null&&StringUtils.isNotBlank(param.get("shareCheck").toString()))
//    					temp+=" and r.check_status=#{params.shareCheck}";
//    				if(param.get("shareLock")!=null&&StringUtils.isNotBlank(param.get("shareLock").toString()))
//    					temp+=" and r.lock_status=#{params.shareLock}";
//    				WHERE(temp+")");
//    			}
//    			if((param.get("rateCheck")!=null&&StringUtils.isNotBlank(param.get("rateCheck").toString()))
//    					||(param.get("rateLock")!=null&&StringUtils.isNotBlank(param.get("rateLock").toString()))){
//    				temp="EXISTS (SELECT 1 FROM service_manage_rate r WHERE b.agent_no = r.agent_no ";
//    				if(param.get("rateCheck")!=null&&StringUtils.isNotBlank(param.get("rateCheck").toString()))
//    					temp+=" and r.check_status=#{params.rateCheck}";
//    				if(param.get("rateLock")!=null&&StringUtils.isNotBlank(param.get("rateLock").toString()))
//    					temp+=" and r.lock_status=#{params.rateLock}";
//    				WHERE(temp+")");
//    			}
//				if((param.get("quotaCheck")!=null&&StringUtils.isNotBlank(param.get("quotaCheck").toString()))
//    					||(param.get("quotaLock")!=null&&StringUtils.isNotBlank(param.get("quotaLock").toString()))){
//    				temp="EXISTS (SELECT 1 FROM service_manage_quota r WHERE b.agent_no = r.agent_no ";
//    				if(param.get("quotaCheck")!=null&&StringUtils.isNotBlank(param.get("quotaCheck").toString()))
//    					temp+=" and r.check_status=#{params.quotaCheck}";
//					if(param.get("quotaLock")!=null&&StringUtils.isNotBlank(param.get("quotaLock").toString()))
//    					temp+=" and r.lock_status=#{params.quotaLock}";
//    				WHERE(temp+")");
//    			}
    			temp=(String)param.get("agentName");
    			if(StringUtils.isNotBlank(temp)){
    				param.put("agentName","%"+temp+"%");
    				WHERE("b.agent_name like #{params.agentName}");
    			}
    			if(param.get("teamId")!=null&&StringUtils.isNotBlank(param.get("teamId").toString())){
    				WHERE("b.team_id = #{params.teamId}");
    			}
    			if(param.get("agentLevel")!=null&&!"-1".equals((param.get("agentLevel").toString()))){
    				WHERE("b.agent_level = #{params.agentLevel}");
    			}
    			if(param.get("mobilephone")!=null&&StringUtils.isNotBlank(param.get("mobilephone").toString())){
    				WHERE("b.mobilephone like concat(#{params.mobilephone},'%')");
    			}
    			if(param.get("hasAccount")!=null&&StringUtils.isNotBlank(param.get("hasAccount").toString())){
    				WHERE("b.has_account = #{params.hasAccount}");
    			}
    			if(StringUtils.isNotBlank((String)param.get("agentNo"))){
    				if(StringUtils.isNotBlank((String)param.get("subAgentNo"))){
    					WHERE("b.agent_node like #{params.subAgentNo}");
    				}else{	
    					WHERE("b.agent_no = #{params.agentNo}");
    				}
    			}
    		}}.toString();
    		return sql;
    	}
    	public String updateAgentShareList(Map<String, Object> param){
			List<AgentShareRule> list = (List<AgentShareRule>) param.get("list");
			StringBuilder sb = new StringBuilder("");
			MessageFormat message = new MessageFormat("update agent_share_rule set profit_type=#'{'list[{0}].profitType},per_fix_income=#'{'list[{0}].perFixIncome},"
					+ "per_fix_cost=#'{'list[{0}].perFixCost},share_profit_percent=#'{'list[{0}].shareProfitPercent},"
					+ "ladder1_rate=#'{'list[{0}].ladder1Rate},ladder1_max=#'{'list[{0}].ladder1Max},per_fix_inrate=#'{'list[{0}].perFixInrate},"
					+ "ladder2_rate=#'{'list[{0}].ladder2Rate},ladder2_max=#'{'list[{0}].ladder2Max},safe_line=#'{'list[{0}].safeLine},"
					+ "ladder3_rate=#'{'list[{0}].ladder3Rate},ladder3_max=#'{'list[{0}].ladder3Max},capping=#'{'list[{0}].capping},"
					+ "ladder4_rate=#'{'list[{0}].ladder4Rate},ladder4_max=#'{'list[{0}].ladder4Max},cost_rate_type=#'{'list[{0}].costRateType}"
					+ " where id =#'{'list[{0}].id};");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
//			sb.append(")");
			return sb.toString();
		}

    	public String getProfits(Map<String,Object> map){
    		@SuppressWarnings("unchecked")
    		final List<Integer> ids=(List<Integer>)map.get("bpIds");
    		final String agentId = (String) map.get("agentId");
    		String sql = new SQL(){{
    			SELECT_DISTINCT("asr.*,si.service_name,si.service_type");
    			FROM("business_product_info bp ");
    			INNER_JOIN("service_info si ON bp.service_id = si.service_id");
    			LEFT_OUTER_JOIN("agent_share_rule asr ON bp.service_id = asr.service_id");
    			if(StringUtils.isNotBlank(agentId) && !"0".equals(agentId)){
    				LEFT_OUTER_JOIN("agent_info ai on asr.agent_no=ai.one_level_id");
    				WHERE("ai.agent_no=#{agentId}");
    			} else{
    				WHERE("asr.agent_no=#{agentId}");
    			}
    			WHERE(" si.effective_status = '1'");
    			StringBuilder sb=new StringBuilder(" bp.bp_id in (-1,");
    			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
    			for (int i = 0; i < ids.size(); i++) {
    				sb.append(messageFormat.format(new Integer[] { i }));
    				sb.append(",");
    			}
    			sb.setLength(sb.length() - 1);
    			sb.append(")");
    			WHERE(sb.toString());
    		}}.toString();
    		return sql;
    	}

    	public String agentAutoComplete(final Map<String,Object> param){
			final String item = (String) param.get("item");
			final String saleName = param.containsKey("saleName") ? (String) param.get("saleName") : "";
			final String agentNode = param.containsKey("agentNode") ? (String) param.get("agentNode") : "";
    		final int agentLevel = (int) param.get("agentLevel");
    		SQL sql = new SQL(){{
    			SELECT("id,agent_no,agent_name,agent_node");
    			FROM("agent_info");
    			if(StringUtils.isNotBlank(item)){
    				if(StringUtils.isNumeric(item)){
    					WHERE("agent_no like concat(#{item},'%')");
    				}else{
    					WHERE("agent_name like concat(#{item},'%')");
    				}

    			}
				if(agentLevel!=0){
					WHERE("agent_level=#{agentLevel}");
				}
    			if(StringUtils.isNotBlank(saleName)){
					WHERE("sale_name like #{saleName}");
				}
				if(StringUtils.isNotBlank(agentNode)){
					WHERE("agent_node like  concat(#{agentNode},'%') ");
				}
    			WHERE("status='1'");
    			
    			ORDER_BY("id limit 50");
    		}};
    		return sql.toString();
    	}
    	
    	public String updateProfitStatus(Map<String, Object> param){
    		Integer profitSwitch = (Integer) param.get("profitSwitch");
    		StringBuilder sql = new StringBuilder();
    		sql.append("update agent_info ai set ai.profit_switch=#{profitSwitch} where ai.agent_node ");
    		if(0==profitSwitch){
    			sql.append(" like concat(#{agentNode},'%')");
    		} else if(1==profitSwitch){
    			sql.append(" = #{agentNode}");
    		}
    		return sql.toString();
    	}

		public String updatePromotionStatus(Map<String, Object> param) {
			Integer promotionSwitch = (Integer) param.get("promotionSwitch");
			StringBuilder sql = new StringBuilder();
			sql.append("update agent_info ai set ai.promotion_switch=#{promotionSwitch} where ai.agent_node ");
			if (0 == promotionSwitch) {
				sql.append(" like concat(#{agentNode},'%')");
			} else if (1 == promotionSwitch) {
				sql.append(" = #{agentNode}");
			}
			return sql.toString();
		}

		public String updateCashBackStatus(Map<String, Object> param) {
			Integer cashBackSwitch = (Integer) param.get("cashBackSwitch");
			StringBuilder sql = new StringBuilder();
			sql.append("update agent_info ai set ai.cash_back_switch=#{cashBackSwitch} where ai.agent_node ");
			if (0 == cashBackSwitch) {
				sql.append(" like concat(#{agentNode},'%')");
			} else if (1 == cashBackSwitch) {
				sql.append(" = #{agentNode}");
			}
			return sql.toString();
		}

		public String queryAgentRoleOemList(final Map<String,Object> param){
			final Map<String,Object> map = (Map<String,Object>) param.get("map");
			SQL sql = new SQL(){{
				SELECT("asro.*,asr.role_name ");
				FROM("agent_shiro_role_oem asro ");
				LEFT_OUTER_JOIN("agent_shiro_role asr on asr.id=asro.role_id ");
				if(StringUtil.isNotBlank(map.get("role_name"))){
					WHERE("asr.role_name=#{map.role_name} ");
				}
				if(StringUtil.isNotBlank(map.get("role_code"))){
					WHERE("asr.role_code=#{map.role_code} ");
				}
			}};
			return sql.toString();
		}

		public String selectAgentRoleOem(final Map<String,Object> param){
			final Map<String,Object> map = (Map<String,Object>) param.get("map");
			SQL sql = new SQL(){{
				SELECT("asro.*");
				FROM("agent_shiro_role_oem asro ");
				WHERE("asro.agent_type = #{map.agent_type} ");
				if(StringUtil.isNotBlank(map.get("agent_oem"))){
					WHERE("asro.agent_oem = #{map.agent_oem} ");
				}else{
					WHERE("asro.agent_oem is null ");
				}
				if(StringUtil.isNotBlank(map.get("id"))){
					WHERE("asro.id != #{map.id} ");
				}
			}};
			return sql.toString();
		}

		public String selectUserAgentRoleOem(final Map<String,Object> param){
			final Map<String,Object> map = (Map<String,Object>) param.get("map");
			SQL sql = new SQL(){{
				SELECT("ai.agent_name,ai.agent_no,u.mobilephone,ue.status ");
				FROM("agent_info ai ");
				LEFT_OUTER_JOIN("user_entity_info ue on ue.entity_id=ai.agent_no ");
				LEFT_OUTER_JOIN("user_info u on ue.user_id=u.user_id ");
				LEFT_OUTER_JOIN("agent_user_role aur on aur.user_id=ue.id ");
				WHERE("ue.user_type=1 and ue.is_agent=1 and ai.agent_level=1 and ai.agent_type = #{map.agent_type} ");
				if(StringUtil.isNotBlank(map.get("agent_oem"))){
					WHERE("ai.agent_oem = #{map.agent_oem} ");
				}else{
					WHERE("ai.agent_oem is null ");
				}
			}};
			return sql.toString();
		}

		public String selectAgentShareCheckList(Map<String, Object> param){
    		AgentInfo baseInfo = (AgentInfo) param.get("baseInfo");
    		SQL sql = new SQL();
//			select ai.agent_no,ai.agent_name,ai.status,ai.create_date,ai.team_id from v_agent_info ai
//					where
//			ai.agent_level = '1' and
//					(
//							EXISTS(SELECT 1 FROM agent_share_rule r
//									INNER JOIN service_info si on si.service_id = r.service_id
//									WHERE ai.agent_no = r.agent_no AND r.check_status =0 and si.effective_status = '1' )
//							or
//							EXISTS (SELECT 1 FROM agent_share_rule_task asrt
//									INNER JOIN agent_share_rule r on r.id=asrt.share_id
//									INNER JOIN service_info si on si.service_id = r.service_id
//									WHERE ai.agent_no = r.agent_no AND asrt.check_status =0 and si.effective_status = '1'));
    		sql.SELECT("ai.agent_no,ai.agent_name,ai.status,ai.create_date,ai.team_id,ti.team_name");
    		sql.FROM("agent_info ai");
			sql.LEFT_OUTER_JOIN("team_info ti on ti.team_id=ai.team_id");
    		sql.WHERE("ai.agent_level = '1'");
    		sql.WHERE("(" +
					" EXISTS(SELECT 1 FROM agent_share_rule r" +
					" INNER JOIN service_info si on si.service_id = r.service_id" +
					" WHERE ai.agent_no = r.agent_no AND r.check_status = '0' and si.effective_status = '1' )" +
					" or" +
					" EXISTS (SELECT 1 FROM agent_share_rule_task asrt" +
					" INNER JOIN agent_share_rule r on r.id=asrt.share_id" +
					" INNER JOIN service_info si on si.service_id = r.service_id" +
					" WHERE ai.agent_no = r.agent_no AND asrt.check_status = '0' and si.effective_status = '1')" +
					")");
    		if(baseInfo == null){
				return sql.toString();
			}
			if(StringUtils.isNotBlank(baseInfo.getAgentNo())){
    			sql.WHERE("ai.agent_no = #{baseInfo.agentNo}");
			}
			if(StringUtils.isNotBlank(baseInfo.getAgentName())){
    			baseInfo.setAgentName(baseInfo.getAgentName() + "%");
				sql.WHERE("ai.agent_name like #{baseInfo.agentName}");
			}
			if(StringUtils.isNotBlank(baseInfo.getMobilephone())){
				sql.WHERE("ai.mobilephone = #{baseInfo.mobilephone}");
			}
			if(baseInfo.getTeamId() != null){
				sql.WHERE("ai.team_id = #{baseInfo.teamId}");
			}
			if (StringUtils.isNotBlank(baseInfo.getStartTime())) {
				sql.WHERE("ai.create_date >= #{baseInfo.startTime} ");
			}
			if (StringUtils.isNotBlank(baseInfo.getEndTime())) {
				sql.WHERE("ai.create_date <= #{baseInfo.endTime} ");
			}
    		return sql.toString();
		}

		public String updateHappyBackTypeByAgentLevel(Map<String, Object> map) {
			String agentNoes = (String) map.get("agentNoes");
			String types = (String) map.get("types");
			String agentNo[]=agentNoes.split(",");
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE agent_activity SET status=1 where agent_no in (");
			for (int i = 0; i < agentNo.length; i++) {
				sb.append("'"+agentNo[i]+"'");
				if (i < agentNo.length - 1) {
					sb.append(",");
				}
			}
			sb.append(") and activity_type_no in (");
			sb.append(types);
			sb.append(")");
			System.out.println(sb.toString());
			return sb.toString();
		}

		public String updateHlfTerByAgentLevel(Map<String, Object> map) {
			String agentNoes = (String) map.get("agentNoes");
			String activityTypeNo = (String) map.get("activityTypeNo");
			String agentNo[]=agentNoes.split(",");
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE agent_activity SET status=1 where agent_no in (");
			for (int i = 0; i < agentNo.length; i++) {
				sb.append("'"+agentNo[i]+"'");
				if (i < agentNo.length - 1) {
					sb.append(",");
				}
			}
			sb.append(") and activity_type_no = #{activityTypeNo}");
			System.out.println(sb.toString());
			return sb.toString();
		}
    }
}