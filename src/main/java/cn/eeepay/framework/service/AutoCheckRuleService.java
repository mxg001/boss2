package cn.eeepay.framework.service;

import cn.eeepay.framework.model.AutoCheckRoute;
import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AutoCheckRuleService {
	
	SysConfigAutoCheck selectByParamKey(@Param("paramKey")String paramKey);

	AutoCheckRoute selectByChannelCode(String channelCode);
	
	int updateValue(Map<String, Object> sysConfigAutoCheck);

	List<AutoCheckRule> selectAll();

	int updateState(AutoCheckRule autoCheckRule);

	int updateIsOpen(AutoCheckRule info);
	
	int updateIsPass(AutoCheckRule info);

	List<AutoCheckRoute> listByRouteType(int i);

}
