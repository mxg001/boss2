package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.SysOption;

public interface SysOptionService {
	
	/**
	 * 	查询系统字典选项
	 * @param sysOption
	 * @return
	 */
	List<SysOption> selectSysOption(SysOption sysOption);
}
