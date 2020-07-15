package cn.eeepay.framework.service.sysUser.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.sysUser.SystemInfoDao;
import cn.eeepay.framework.model.SystemInfo;
import cn.eeepay.framework.service.sysUser.SystemInfoService;


//@Service("systemInfoService")
//@Transactional
public class SystemInfoServiceImpl implements SystemInfoService {

	@Resource
	public SystemInfoDao  systemInfoDao;
	
	@Override
	public SystemInfo findSystemInfo(SystemInfo systemInfo) throws Exception {
		return systemInfoDao.findSystemInfo(systemInfo);
	}

	@Override
	public int updateSystemInfo(SystemInfo systemInfo) throws Exception {
		return systemInfoDao.updateSystemInfo(systemInfo);
	}

}
