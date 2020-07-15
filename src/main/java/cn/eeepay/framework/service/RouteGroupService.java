package cn.eeepay.framework.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.model.TransRouteGroupAcqMerchant;
import cn.eeepay.framework.model.TransRouteGroupMerchant;
import org.springframework.web.multipart.MultipartFile;

public interface RouteGroupService {

	List<AcqOrg> acqOrgSelectBox();

	List<AcqService> acqServiceSelectBox(int acqId);

	int insertRouteGroup(TransRouteGroup routeGroup);

	List<AgentInfo> oneLevelAgentSelectBox();

	List<TransRouteGroup> queryRouteGroupByCon(Map<String, Object> param, Page<TransRouteGroup> page);

	TransRouteGroup queryRouteGroupById(Map<String, Object> param);
	
	int updateRouteGroup(TransRouteGroup routeGroup);

	Map<String, Object> insertRouteGroupMerchant(TransRouteGroupMerchant routeGroupMerchant);

	Map<String, Object> insertRouteGroupAcqMerchant(TransRouteGroupAcqMerchant routeGroupAcqMerchant);

	int updateGroupStatus(TransRouteGroup group);

	TransRouteGroup getGroupByCode(String groupCode);
	
	int deleteRouteGroup(int id);

	List<TransRouteGroup> getGroupByServiceType(String[] serviceTypes,String group);

    Map<String,Object> importDiscount(MultipartFile file) throws Exception;

	List<Map<String, String>> getMapGroupSelect(Integer groupCode);
}
