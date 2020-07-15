package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.model.SysConfig;

public interface SysConfigService {
		/**
		 * 获取科目类别文本
		 * @param sysKey 类别Key
		 * @param sysValue 数值
		 * @return
		 * @throws Exception
		 */
		SysConfig findSysConfig(String sysKey,String sysValue) throws Exception;
		
		/**
		 * 获取字典类别集合
		 * @param sysKey 类别Key
		 * @return
		 * @throws Exception
		 */
		List<SysConfig> findSysConfigGroup(String sysKey) throws Exception;
		
		int insertSysConfig(SysConfig sysConfig) throws Exception;
		
		int updateSysConfig(SysConfig sysConfig) throws Exception;
		
		int deleteSysConfig(String id) throws Exception;
		
		int deleteSysConfigByParams(String sysKey,String sysValue) throws Exception;
		
		void init()  throws Exception;

	String getStringValueByKey(String function_manage_003);
}
