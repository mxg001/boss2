package cn.eeepay.framework.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AuthCardDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuthCard;
import cn.eeepay.framework.service.AuthCardService;

@Service("authCardService")
@Transactional
public class AuthCardServiceImpl implements AuthCardService {

	@Resource
	private AuthCardDao authCardDao;

	
	@Override
	public List<AuthCard> selectAllInfo(Page<AuthCard> page, AuthCard rr) {
		return authCardDao.selectAllInfo(page, rr);
	}


	@Override
	public List<AuthCard> authCardExport(AuthCard rr) {
		return authCardDao.authExport(rr);
	}


	@Override
	public Map<String, Object> authCardTotal(AuthCard rr) {
		return authCardDao.authCardTotal(rr);
	}
	
	
	
}
