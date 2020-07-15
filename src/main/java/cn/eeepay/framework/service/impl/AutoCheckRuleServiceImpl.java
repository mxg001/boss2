package cn.eeepay.framework.service.impl;


import cn.eeepay.framework.dao.AutoCheckRuleDao;
import cn.eeepay.framework.model.AutoCheckRoute;
import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AutoCheckRuleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("autoCheckRuleService")
@Transactional
public class AutoCheckRuleServiceImpl implements AutoCheckRuleService {

	private static final String Map = null;
	@Resource
	private AutoCheckRuleDao autoCheckRuleDao;

	
	@Override
	public AutoCheckRoute selectByChannelCode(String channelCode) {
		return autoCheckRuleDao.selectByChannelCode(channelCode);
		
	}

	@Override
	public SysConfigAutoCheck selectByParamKey(String paramKey) {
		return autoCheckRuleDao.selectByParamKey(paramKey);

	}
	
	@Override
	public List<AutoCheckRule> selectAll() {
		
		return autoCheckRuleDao.selectAll();
	}
	
	@Override
	public int updateState(AutoCheckRule autoCheckRule) {
		return autoCheckRuleDao.updateState(autoCheckRule);
	}

	@Override
	public int updateValue(Map<String, Object> param) {
		int i=0;
		i = autoCheckRuleDao.updateValue("bank_card_ocr",param.get("bankCardOcr").toString());
		i++;
		i = autoCheckRuleDao.updateValue("single_merch_times",param.get("singleMerchTimes").toString());
		i++;
		i = autoCheckRuleDao.updateValue("age_limit",param.get("minAge").toString()+"_"+param.get("maxAge").toString());
		i++;
		i = autoCheckRuleDao.updateValue("age_limit",param.get("minAge").toString()+"_"+param.get("maxAge").toString());
		i++;
		i = autoCheckRuleDao.updateValue("living_type",param.get("livingType").toString());

		// 更新通道配置
		UserLoginInfo info = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = info.getUsername();
		Map  livingJskj = (Map)param.get("livingJskj");
		livingJskj.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(livingJskj);
		i++;
		
		Map  livingXskj = (Map)param.get("livingXskj");
		livingXskj.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(livingXskj);
		i++;
		
		Map  ocrJskj = (Map)param.get("ocrJskj");
		ocrJskj.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(ocrJskj);
		i++;
		
		Map  ocrYlsw = (Map)param.get("ocrYlsw");
		ocrYlsw.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(ocrYlsw);
		i++;

		Map  routingYlsw = (Map)param.get("routingYlsw");
		routingYlsw.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(routingYlsw);
		i++;

		Map  routingJskj = (Map)param.get("routingJskj");
		routingJskj.put("routingJskj", username);
		i = autoCheckRuleDao.updateRoutePercent(routingJskj);
		i++;

		Map  routingXskj = (Map)param.get("routingXskj");
		routingXskj.put("username", username);
		i = autoCheckRuleDao.updateRoutePercent(routingXskj);
		i++;

		Map  realJskj = (Map)param.get("realJskj");
		i = autoCheckRuleDao.updateRoute(realJskj);
		i++;
		
		Map  realXskj = (Map)param.get("realXskj");
		i = autoCheckRuleDao.updateRoute(realXskj);
		i++;
		
		Map  realYlsw = (Map)param.get("realYlsw");
		i = autoCheckRuleDao.updateRoute(realYlsw);
		i++;
		return i;
	}

	@Override
	public int updateIsOpen(AutoCheckRule info) {
		return autoCheckRuleDao.updateIsOpen(info);
	}
	@Override
	public int updateIsPass(AutoCheckRule info) {
		return autoCheckRuleDao.updateIsPass(info);
	}

	@Override
	public List<AutoCheckRoute> listByRouteType(int i) {
		return autoCheckRuleDao.findByRouteType(i);
	}

}
