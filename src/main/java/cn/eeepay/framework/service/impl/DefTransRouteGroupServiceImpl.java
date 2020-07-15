package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.DefTransRouteGroupDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.DefTransRouteGroup;
import cn.eeepay.framework.service.DefTransRouteGroupService;

@Service("defTransRouteGroupService")
@Transactional
public class DefTransRouteGroupServiceImpl implements DefTransRouteGroupService {

	@Resource
	private DefTransRouteGroupDao defTransRouteGroupDao;
	
	@Override
	public int insert(DefTransRouteGroup record) {
		return defTransRouteGroupDao.insert(record);
	}

	@Override
	public DefTransRouteGroup selectByPrimaryKey(Integer id) {
		return defTransRouteGroupDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(DefTransRouteGroup record) {
		return defTransRouteGroupDao.updateByPrimaryKey(record);
	}

	@Override
	public List<DefTransRouteGroup> selectAllInfo(Page<DefTransRouteGroup> page, DefTransRouteGroup drg) {
		return defTransRouteGroupDao.selectAllInfo(page, drg);
	}

	@Override
	public int selectExistByParam(DefTransRouteGroup dtrg) {
		return defTransRouteGroupDao.selectExistByParam(dtrg);
	}

	@Override
	public DefTransRouteGroup selectInfo(String bpId,String serviceId) {
		return defTransRouteGroupDao.selectInfo(bpId,serviceId);
	}

}
