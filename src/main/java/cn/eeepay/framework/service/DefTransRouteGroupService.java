package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.DefTransRouteGroup;

public interface DefTransRouteGroupService {
	    int insert(DefTransRouteGroup record);

	    DefTransRouteGroup selectByPrimaryKey(Integer id);

	    int updateByPrimaryKey(DefTransRouteGroup record);
	    
	    List<DefTransRouteGroup> selectAllInfo(Page<DefTransRouteGroup> page,DefTransRouteGroup drg);

		int selectExistByParam(DefTransRouteGroup dtrg);
		
		DefTransRouteGroup selectInfo(String bpId,String serviceId);
}
