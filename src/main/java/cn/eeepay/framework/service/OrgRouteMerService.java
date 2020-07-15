package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;

/**
 * 集群中普通商户service
 * 
 * @author junhu
 *
 */
@SuppressWarnings("all")
public interface OrgRouteMerService {
	List<Map> listOrgRouteMerByCon(Map<String, Object> param, Page<Map> page);

	int deleteOrgRouteMerById(Long id);
}
