package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.UserFreezeOperLogDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserFreezeOperLog;
import cn.eeepay.framework.service.UserFreezeOperLogService;

@Service("userFreezeOperLogService")
public class UserFreezeOperLogServiceImpl implements UserFreezeOperLogService {

	@Resource
	private UserFreezeOperLogDao userFreezeOperLogDao;

	@Override
	public List<UserFreezeOperLog> getUserFreezeOperLog(String userCode,
			Page<UserFreezeOperLog> page) {
		userFreezeOperLogDao.getUserFreezeOperLog(userCode, page);
		List<UserFreezeOperLog> list = page.getResult();
		return list;
	}

	@Override
	public int insertUserFreezeOperLog(UserFreezeOperLog record) {
		return userFreezeOperLogDao.insertUserFreezeOperLog(record);
	}

}
