package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransRouteGroup;

public interface TransRouteGroupService {

	int deleteByPrimaryKey(Integer id);

    int insert(TransRouteGroup record);

    int insertSelective(TransRouteGroup record);

    TransRouteGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TransRouteGroup record);

    int updateByPrimaryKey(TransRouteGroup record);

	List<TransRouteGroup> selectByParam(Page<TransRouteGroup> page, Map<String, Object> info);

	int transferMer(String groupCode, List<TransRouteGroup> list);
	
	List<TransRouteGroup> selectGroupByAcqMerchantNo(String acqMerchantNo);
	
	List<TransRouteGroup> selectMerNoByGroupCode(String GroupCode);
	
	List<TransRouteGroup> selectAcqMerNoByGroupCode(String GroupCode);
	
}
