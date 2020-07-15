package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AgentInfoService {

	AgentInfo selectByName(String name);

	List<AgentInfo> selectAllAgentInfo(String item, int i);

	List<AgentInfo> selectAllAgentInfo(String item,String sale_name, int i);

	List<AgentInfo> getAllAgentListByParent(String item,String parentAgentNo);

	List<AgentInfo> selectByLevelOne();

	AgentInfo selectByagentNo(String agentNo);

	AgentInfo getAgentByNo(String agentNo);

	List<AgentInfo> selectOneAgentByName(String agentName);

	Map<String, Object> selectAgentTeamByAgentNo(String agentNo);

	List<BusinessProductDefine> selectProductById(String id);

	List<JoinTable> selectProductByTeamId(Integer id, String agentNo);

	List<JoinTable> selectProductByAgentNoBpId(String bpId, String agentNo);

	List<AgentShareRule> getAgentShareInfos(String agentNo);

	Map<String, Object> getAgentServices(Map<String, Object> json);

	List<ServiceRate> getServiceRate(List<Integer> bpIds, String agentId);

	List<ServiceQuota> getServiceQuota(List<Integer> bpIds, String agentId);

	AgentInfo saveAgentInfo(JSONObject json);

	void setShareRule(AgentShareRule share);

	Page<AgentInfo> queryAgentInfoList(Map<String, Object> params, Page<AgentInfo> page);

	Map<String, Object> queryAgentProducts(String agentNo, Integer teamId);

	Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId, boolean isAllProduct);

	Map<String, Object> getNewAgentServices(Map<String, Object> map);

	void profitExpression(AgentShareRule rule);

	Map<String, Object> updateAgentProStatus(Map<String, Object> map);

	String delAgent(String agentNo, Integer teamId);

	String updateAgent(String data);

	int updateAgentAccount(String agentNo);

	int updateStatus(AgentInfo agent);

	List<AgentInfo> selectAllInfoSale(String node);

	List<AgentInfo> selectAllInfoSale(String node,boolean onlyOneNode);

	List<AgentInfo> selectAllInfoSale1(String node);

	List<String> selectAllNodeSale(String node);

	List<String> queryAllOneAgentBySale(String saler);

	Page<AgentInfo> queryAgentInfoListSale(String str, Map<String, Object> params, Page<AgentInfo> page);

	Map<String, Object> queryAgentInfoAudit(String agentNo, Integer teamId, boolean b);

	List<AgentShareRule> getAllShare(String agentNo);

	int updateAgentShare(String data);

	int levelOneCreateAcc(String agentNo);

	Map<String, Object> updateProfitStatus(AgentInfo agent);

	int updateProgitSwitchBatch(List<String> agentNodeList, Integer switchStatus);

	Map<String, Object> openOldAccount(List<String> agentNoList, String subjectNo);

	void compareRuleRate(AgentShareRule rule, ServiceRate rate);

	/**
	 * 修改代理商推广功能开关
	 * @author mays
	 * @date 2017年8月11日 下午5:20:45
	 */
	Map<String, Object> updatePromotionStatus(AgentInfo agent);

	/**
	 * 修改代理商欢乐返返现功能开关
	 * @author rpc
	 * @date 2018年6月28日 下午4:41:00
	 */
	Map<String, Object> updateCashBackStatus(AgentInfo agent);

	/**
	 * 批量修改代理商推广功能开关
	 * @author	mays
	 * @date	2017年8月12日 上午9:33:52
	 */
	int updatePromotionSwitchBatch(List<String> agentNodeList, Integer switchStatus);

	/**
	 * 查询欢乐返子类型
	 * @author	mays
	 * @date	2018年5月15日
	 */
	List<ActivityHardwareType> selectHappyBackType();

	/**
	 * 查询欢乐返子类型，根据SubType划分数据
	 * @return
	 */
	List<ActivityHardwareType> selectHappyBackTypeBySubType(String activityCode,String subType);

	List<ActivityHardwareType> selectHappyBackTypeByAgentNo(String agentNo);

    ServiceRate queryMinServiceRate(AgentShareRule rule);

	/**
	 * 查询未开户的一级代理商
	 * @return
	 */
	List<AgentInfo> selectLevelOneAgentWithNoAccount();

	List<Map> selectAgentShareRule();

	List<Map> queryAgentRoleOemList(Map<String, Object> params, Page<Map> page);

	Map selectAgentRoleOem(Map<String, Object> params);

	int insertAgentRoleOem(Map<String, Object> params);

	int updateAgentRoleOem(Map<String, Object> params);

	int deleteAgentRoleOem(Integer id);

	List<Map> selectUserAgentRoleOem(Map<String, Object> params);

	List<AgentInfo> selectAgentShareCheckList(Page<AgentInfo> page, AgentInfo agentInfo);

	void exportAgentShareCheck(AgentInfo info, HttpServletResponse response)  throws Exception;

	int insertHappyBackTypeByAgentLevel(String agentNo, String types);

	int updateHappyBackTypeByAgentLevel(String agentNoes, String types);

	int insertHlfTerByAgentLevel(String agentNo, String activityTypeNo);

	int updateHlfTerByAgentLevel(String agentNoes, String activityTypeNo);

	int insertHlfTerByOneAgentLevel(String agentNo, String activityTypeNo);

	int insertHappyBackType(List<ActivityHardwareType> happyBackTypes);

	AgentInfo selectLittleInfo(String agentNo);

	List<JoinTable> getAgentProducts(String agentNo);

    List<ActivityHardwareType> selectHappyBackTypeWithTeamId(List<String> teamIds);

    String getOneAgentNo(String agentNo);

}
