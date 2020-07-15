package cn.eeepay.framework.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SuperPushConfig;
import cn.eeepay.framework.model.SuperPushShareRule;

/** 
 * @author tans 
 * @version ：2017年5月5日 上午10:28:09 
 * 
 */
public interface SuperPushConfigService {

	/**
	 * 根据业务产品ID获取微创业的配置信息
	 * @author tans
	 * @version 创建时间：2017年5月5日 上午10:42:10
	 */
	SuperPushConfig getByBpId(String bpId);

	/**
	 * 根据微创业里面的代理商编号获取代理商集合
	 * @author tans
	 * @version 创建时间：2017年5月5日 上午11:43:46
	 */
	List<AgentInfo> getAgentList(String[] strArr);

	/**
	 * 根据微创业里面的服务ID获取分润信息集合
	 * @author tans
	 * @version 创建时间：2017年5月5日 下午1:34:14
	 */
	List<SuperPushShareRule> getShareRuleList(String bpId);

	/**
	 * 修改分润配置信息
	 * @author tans
	 * @version 创建时间：2017年5月8日 上午10:03:25
	 */
	Map<String, Object> updateShareRule(SuperPushShareRule rule);

	void setsuperPushShareRule(SuperPushShareRule rule);

	Map<String, Object> saveConfig(JSONObject json);

	List<SuperPushShareRule> getServiceListByBp(String bpId);

	void checkRule(Integer profitType, BigDecimal income, BigDecimal inrate);


}
