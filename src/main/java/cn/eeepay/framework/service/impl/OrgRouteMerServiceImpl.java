package cn.eeepay.framework.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.OrgRouteMerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.OrgRouteMerService;

/**
 * 集群中普通商户service实现
 * 
 * @author junhu
 *
 */
@Service
@Transactional
public class OrgRouteMerServiceImpl implements OrgRouteMerService {

	@Resource
	private OrgRouteMerDao orgRouteMerDao;
	
	@Override
	public List<Map> listOrgRouteMerByCon(Map<String, Object> param, Page<Map> page) {

		return orgRouteMerDao.listOrgRouteMerByCon(param, page);
	}

	@Override
	public int deleteOrgRouteMerById(Long id) {

		return orgRouteMerDao.deleteOrgRouteMerById(id);
	}

}
