package cn.eeepay.framework.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmShareDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmShare;
import cn.eeepay.framework.service.CmShareService;

@Service("cmShareService")
@Transactional
public class CmShareServiceImpl implements CmShareService{

	@Resource
	private CmShareDao cmShareDao;

	public List<CmShare> selectShareInfo(Page<CmShare> page, CmShare info) {
		return cmShareDao.selectShareInfo(page, info);
	}

	public List<CmShare> exportShareInfo(CmShare info) {
		return cmShareDao.exportShareInfo(info);
	}
	
	public Map<String, String> sumShareInfo(CmShare info) {
		return cmShareDao.sumShareInfo(info);
	}

}
