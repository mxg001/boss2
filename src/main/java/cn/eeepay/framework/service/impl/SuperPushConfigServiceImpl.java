package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.BusinessProductDefineDao;
import cn.eeepay.framework.dao.BusinessProductInfoDao;
import cn.eeepay.framework.dao.SuperPushConfigDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.SuperPushConfig;
import cn.eeepay.framework.model.SuperPushShareRule;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SuperPushConfigService;

/** 
 * @author tans 
 * @version ：2017年5月5日 上午10:28:32 
 * 
 */
@Service("jtConfigService")
@Transactional
public class SuperPushConfigServiceImpl implements SuperPushConfigService {

	@Resource
	private SuperPushConfigDao superPushConfigDao;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	
	@Resource
	private BusinessProductDefineDao businessProductDefineDao;
	
	@Override
	public SuperPushConfig getByBpId(String bpId) {
		return superPushConfigDao.getByBpId(bpId);
	}
	
	@Override
	public List<AgentInfo> getAgentList(String[] appAgentNoList) {
		return superPushConfigDao.getAgentList(appAgentNoList);
	}
	
	@Override
	public List<SuperPushShareRule> getShareRuleList(String bpId) {
		return superPushConfigDao.getShareRuleList(bpId);
	}

	@Override
	public Map<String, Object> updateShareRule(SuperPushShareRule rule) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "保存失败");
		setsuperPushShareRule(rule);
		int num = 0;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(rule!=null){
			if(rule.getId()!=null){
				num = superPushConfigDao.updateShareRule(rule);
			}
			if(rule.getId()==null){
				num = superPushConfigDao.insertShareRule(rule);
			}
			rule.setOperator(principal.getId());
		}

		if(num>0){
			msg.put("status", true);
			msg.put("msg", "保存成功");
			msg.put("id", rule.getId());
		} else {
			msg.put("msg", "保存分润信息失败");
		}
		return msg;
	}
	
	@Override
	public void setsuperPushShareRule(SuperPushShareRule rule){
			checkRule(rule.getAgentProfitType(),rule.getAgentPerFixIncome(),rule.getAgentPerFixInrate());
			checkRule(rule.getOneAgentProfitType(),rule.getOneAgentPerFixIncome(),rule.getOneAgentPerFixInrate());
			checkRule(rule.getProfitType1(),rule.getPerFixIncome1(),rule.getPerFixInrate1());
			checkRule(rule.getProfitType2(),rule.getPerFixIncome2(),rule.getPerFixInrate2());
			checkRule(rule.getProfitType3(),rule.getPerFixIncome3(),rule.getPerFixInrate3());
	}
	
	@Override
	public void checkRule(Integer profitType, BigDecimal income, BigDecimal inrate){
		switch (profitType) {
			case 1:
				if(income.compareTo(new BigDecimal("0.00"))==-1)
					throw new RuntimeException("分润信息中类型为【每笔固定收益金额】：填写不合法！");
				break;
			case 2:
				if(inrate.compareTo(new BigDecimal("0.00"))==-1)
				throw new RuntimeException("分润信息中类型为【每笔收益率】：填写不合法！");
				break;
			default:
				throw new RuntimeException("分润信息中类型异常");
		}
	}

	@Override
	public Map<String, Object> saveConfig(JSONObject json) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		SuperPushConfig baseInfo=JSONObject.parseObject(json.getString("baseInfo"), SuperPushConfig.class);
//		List<AgentInfo> agentList=JSONArray.parseArray(json.getString("agentList"), AgentInfo.class);
//		if(agentList==null || agentList.size()==0){
//			baseInfo.setAppAgentNoList(null);
//		} else {
//			StringBuilder sb = new StringBuilder();
//			for(AgentInfo agent: agentList){
//				sb.append(agent.getAgentNo());
//				sb.append(",");
//			}
//			sb.setLength(sb.length()-1);
//			baseInfo.setAppAgentNoList(sb.toString());
//		}
		
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		baseInfo.setOperator(String.valueOf(principal.getId()));
	
		int num = 0;
		if(baseInfo.getId()==null){
			num = superPushConfigDao.insertConfig(baseInfo);
		} else {
			num = superPushConfigDao.updateConfig(baseInfo);
		}
		if(num>0){
			msg.put("status", true);
			msg.put("msg", "保存成功");
		} else {
			msg.put("status", true);
			msg.put("msg", "保存失败");
		}
		return msg;
	}
	
	/**
	 * 微创业的分润配置（根据业务产品）
	 */
	public List<SuperPushShareRule> getServiceListByBp(String bpId){
		List<SuperPushShareRule> shareRuleList = null;//分润规则集合
		//1.获取微创业的分润配置
//		//1.1如果微创业关联的服务集合不为空，则取该服务集合的分润数据
		shareRuleList = superPushConfigDao.getShareRuleList(bpId);
		if(shareRuleList==null || shareRuleList.size()==0){
			shareRuleList = new ArrayList<>();
			SuperPushShareRule superPushShareRule = new SuperPushShareRule();
			BusinessProductDefine bpd = businessProductDefineDao.selectBybpId(bpId);
			superPushShareRule.setBpId(bpId);
			superPushShareRule.setBpName(bpd.getBpName());
			superPushShareRule.setProfitType1(2);
			superPushShareRule.setProfitType2(2);
			superPushShareRule.setProfitType3(2);
			superPushShareRule.setAgentProfitType(2);
			superPushShareRule.setOneAgentProfitType(2);
			superPushShareRule.setPerFixInrate1(new BigDecimal("0.00"));
			superPushShareRule.setPerFixInrate2(new BigDecimal("0.00"));
			superPushShareRule.setPerFixInrate3(new BigDecimal("0.00"));
			superPushShareRule.setAgentPerFixInrate(new BigDecimal("0.00"));
			superPushShareRule.setOneAgentPerFixInrate(new BigDecimal("0.00"));
			shareRuleList.add(superPushShareRule);
		}
		return shareRuleList;
	}

	/**
	 * 微创业的分润配置（根据服务）
	 */
//	public List<SuperPushShareRule> getServiceListByBp(String bpId){
//		List<SuperPushShareRule> shareRuleList = new ArrayList<>();//分润规则集合
//		//1.获取微创业的分润配置
//		//1.1如果微创业关联的服务集合不为空，则取该服务集合的分润数据
//		shareRuleList = superPushConfigDao.getShareRuleList(bpId);
//		List<String> serviceList = new ArrayList<>();
//		if(shareRuleList!=null && !shareRuleList.isEmpty()){
//			for(SuperPushShareRule rule: shareRuleList){
//				serviceList.add(rule.getServiceId());
//			}
//		}
//		//1.2如果为空，且微创业的业务产品ID不为空，则取该业务产品关联的所有服务
//		List<BusinessProductInfo> list = businessProductInfoDao.getByBpId(bpId);
//		if(list!=null&&list.size()>serviceList.size()){
//			for(BusinessProductInfo bpi: list){
//				if(!serviceList.contains(bpi.getServiceId())){
//					SuperPushShareRule rule = new SuperPushShareRule();
//					rule.setServiceId(bpi.getServiceId());
//					rule.setServiceType(bpi.getServiceType());
//					rule.setServiceName(bpi.getServiceName());
//					//初始化分润数据
//					//提现服务的分润单位为数字元
//					//交易类服务的分润单位为%
//					if("10000".equals(bpi.getServiceType())||"10001".equals(bpi.getServiceType())){
//						rule.setProfitType(1);
//						rule.setProfitType1(1);
//						rule.setProfitType2(1);
//						rule.setProfitType3(1);
//						rule.setPerFixIncome(new BigDecimal("0.00"));
//						rule.setPerFixIncome1(new BigDecimal("0.00"));
//						rule.setPerFixIncome2(new BigDecimal("0.00"));
//						rule.setPerFixIncome3(new BigDecimal("0.00"));
//					} else {
//						rule.setProfitType(2);
//						rule.setProfitType1(2);
//						rule.setProfitType2(2);
//						rule.setProfitType3(2);
//						rule.setPerFixInrate(new BigDecimal("0.00"));
//						rule.setPerFixInrate1(new BigDecimal("0.00"));
//						rule.setPerFixInrate2(new BigDecimal("0.00"));
//						rule.setPerFixInrate3(new BigDecimal("0.00"));
//					}
//					shareRuleList.add(rule);
//				}
//			}
//		}
//		return shareRuleList;
//	}

}
