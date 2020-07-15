package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.*;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;

import javax.servlet.http.HttpServletResponse;

public interface ServiceProService {
	List<ServiceInfo> selectServiceInfo();
	
	int existServiceName(ServiceInfo serviceInfo);

	int existAgentShowName(ServiceInfo serviceInfo);

	int insertServiceInfo(ServiceInfo serviceInfo);

	int insertServiceRateList(List<ServiceRate> list);

	int insertServiceQuotaList(List<ServiceQuota> list);

	int updateServiceInfo(ServiceInfo serviceInfo);

	int deleteServiceInfo(ServiceInfo serviceInfo);

	List<ServiceInfo> getServiceInfo(Map<String,Object> info, Page<ServiceInfo> page);

	List<ServiceInfo> getServiceInfoWithDetail(ServiceInfo info, Page<ServiceInfo> page);

	void setServiceRate(ServiceRate rate,boolean isChange);

	String profitExpression(ServiceRate rule);

	ServiceInfo queryServiceDetail(ServiceInfo info);

	ServiceRate queryServiceRate(ServiceRate sr);
	
	ServiceInfo queryServiceInfo(Long serviceId);
	
	ServiceQuota queryServiceQuota(ServiceQuota sq);

	int updateServiceStatus(String id, String status);
	
	List<ServiceRate> getServiceAllRate(Long serviceId,String agentId);
	
	List<ServiceQuota> getServiceAllQuota(Long serviceId,String agentId);

	List<ServiceInfo> getLinkServices();
	
	//=====sober==========
	List<AgentShareRule> queryAgentProfit(ServiceInfo info);

	int saveAgentProfit(JSONObject json);

    //=====sober==========

	Result updateEffectiveStatus(ServiceInfo baseInfo);

	List<ServiceInfo> selectServiceName();

	Map<Long,String> selectServiceNameMap();

    void getServiceInfoToExport(Map<String, Object> jsonMap, HttpServletResponse response,Map<String, Object> msgMap);

	ServiceRate getRate(String cardType,Long serviceId,String holidayMark);
    String getServiceTypeName(Integer type);
}
