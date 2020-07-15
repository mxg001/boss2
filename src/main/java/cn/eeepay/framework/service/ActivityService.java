package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ActivityService {
	/**
	 * 根据活动编号查询相应的活动配置
	 */
	List<ActivityHardware> selectActivityHardware(Page<ActivityHardware> page, String activityCode);

	List<ActivityHardware> selectActivityConfig(String activityCode);

	ActivityConfig selectActivityCofig(String activityCode);

	int updateActivityConfig(ActivityConfig activityInfo);

	int insertActivityHardware(ActivityHardware activityInfo);

	int insetActivityConfig(ActivityConfig activityInfo);

	int updateActivityHardware(ActivityHardware activityHardware);

	// -------------------------- 以下是欢乐返活动 !!! ----------------------------

	List<HlfHardware> selectHlfHardware(String activityCode);

	List<HlfHardware> selectHlfActivityHardware(HlfHardware hlfHardware);

	HlfHardware selectHlfHardwareByHardId(HlfHardware hlfHardware);

	HlfHardware selectHlfHardwareById(Integer id);

	HlfHardware selectHlfHardwareInfo(HlfHardware hlfHardware);

	boolean insertHlfConfig(ActivityConfig activityConfig);

	boolean updateHlfConfig(ActivityConfig activityConfig);

	int updateHlfHardware(HlfHardware hlfHardware);

	boolean isExistHardware(HlfHardware hlfHardware);

	boolean isExistHardwareByNoId(HlfHardware hlfHardware);

	int insertHlfHardware(HlfHardware hlfHardware,List<HardwareProduct> hards);

	List<ActivityHardwareType> selectHappyReturnType(ActivityHardwareType activityHardwareType,Page<ActivityHardwareType> page);

	void exportActivityHardwareType(ActivityHardwareType info, HttpServletResponse response)  throws Exception;

	boolean queryByActivityTypeName(ActivityHardwareType activityHardwareType);

	boolean insertHappyReturnType(ActivityHardwareType activityHardwareType);

	boolean updateHappyReturnType(ActivityHardwareType activityHardwareType);

	boolean delHappyReturnType(String activityTypeNo);

	boolean updateAgentStatusSwitch(ActivityHardwareType activityHardwareType);

	boolean updateCountTradeScope(String id,String countTradeScope);

	boolean queryActivityCount(String activityTypeNo);

	List<ActivityHardwareType> queryByactivityTypeNoList(String activityCode);

	ActivityHardwareType queryByActivityHardwareType(String activityTypeNo);

	boolean insertHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig);

	boolean updateHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig);

	boolean deleteHappyReturnRewardActivity(String id);

	boolean queryHappyReturnAgentActivityCount(String id);

	AgentActivityRewardConfig queryHappyReturnAgentActivityByAgentNo(String agentNo);

	boolean insertHappyReturnAgentActivity(AgentActivityRewardConfig info);

	boolean deleteHappyReturnAgentActivity(String id);

	List<ActivityRewardConfig> selectHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig,Page<ActivityRewardConfig> page);

	ActivityRewardConfig selectHappyReturnRewardActivityById(String id);

	List<AgentActivityRewardConfig> selectHappyReturnAgentActivity(AgentActivityRewardConfig agentActivityRewardConfig,Page<AgentActivityRewardConfig> page);
	// -------------------------- 以上是欢乐返活动 !!! ----------------------------
	List<Map> queryActivityVipList(Map map,Page<Map> page);

	void exportActivityVip(Map info, HttpServletResponse response)  throws Exception;

	List<ActivityRewardConfig> queryByActivityRewardConfigList();


	List<ActivityHardwareType> getActivityTypeNoList();

	List<HlfHardware> selectHBActivityHardwareList(String hardId, String activityCode);

	void exportHlfActivityHardware(HlfHardware info, HttpServletResponse response)  throws Exception;

	boolean insertHlfActivityMerchantRule(HlfActivityMerchantRule info);

	boolean insertHlfActivityAgentRule(HlfActivityAgentRule info);

	boolean insertHlfGroup(HlfGroup info,List<HlfGroupDetail> list);

	boolean updateHlfGroup(HlfGroup info,List<HlfGroupDetail> list);

	boolean updateHlfActivityMerchantRule(HlfActivityMerchantRule info);

	boolean updateHlfActivityAgentRule(HlfActivityAgentRule info);

	boolean deleteHlfActivityMerchantRule(String ruleId);

	boolean deleteHlfActivityAgentRule(String id);

	List<HlfActivityMerchantRule> selectHlfActivityMerchantRule(HlfActivityMerchantRule info,Page<HlfActivityMerchantRule> page);

	List<HlfActivityAgentRule> selectHlfActivityAgentRule(HlfActivityAgentRule info,Page<HlfActivityAgentRule> page);

	HlfActivityMerchantRule selectHlfActivityMerchantRuleById(String ruleId);

	HlfActivityAgentRule selectHlfActivityAgentRuleById(String id);

	List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo(String item);

	List<HlfActivityAgentRule> selectHlfActivityAgentRuleAllInfo(String item);

	int findActivityHardwareTypeByRuleIdCount(String ruleId);

	int findActivityHardwareTypeByConfigIdCount(String id);

	boolean checkByActivityTypeName(ActivityHardwareType activityHardwareType);

	boolean insertHappyReturnTypeNew(ActivityHardwareType activityHardwareType);

	boolean updateHappyReturnTypeNew(ActivityHardwareType activityHardwareType);

	List<ActivityHardwareType> queryByactivityTypeNoList2(String activityCode,String subType);

	List<HlfHardware> selectHlfActivityHardwareList(HlfHardware hlfHardware);

	List<HlfGroup> selectHlfGroup(HlfGroup info,Page<HlfGroup> page);

	HlfGroup selectHlfGroupById(String id);

	List<HlfGroupDetail> selectHlfGroupDetails(String groupId);

	boolean deleteHlfGroup(String id);

	List<XhlfSmartConfig> selectXhlfSmartConfig(XhlfSmartConfig info,Page<XhlfSmartConfig> page);

	XhlfSmartConfig selectXhlfSmartConfigById(String id);

	boolean updateXhlfSmartConfig(XhlfSmartConfig info);

	boolean deleteXhlfSmartConfig(String id);

	boolean insertXhlfSmartConfig(XhlfSmartConfig info);

	int findActivityHardwareTypeBySmartConfigIdCount(String id);

	List<XhlfSmartConfig> selectXhlfSmartConfigAllInfo(String item);

	List<ActivityHardwareType> getHlfActivityTypeList(String activityCode,String subType,String str);

	void importHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig, HttpServletResponse response) throws Exception;
}
