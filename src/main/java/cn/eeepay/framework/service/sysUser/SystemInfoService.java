package cn.eeepay.framework.service.sysUser;

import cn.eeepay.framework.model.SystemInfo;

public interface SystemInfoService {
	SystemInfo findSystemInfo(SystemInfo systemInfo) throws Exception;
	int updateSystemInfo(SystemInfo systemInfo) throws Exception;
}
