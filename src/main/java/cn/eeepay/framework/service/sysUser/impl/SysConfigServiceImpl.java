package cn.eeepay.framework.service.sysUser.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.sysUser.SysConfigDao;
import cn.eeepay.framework.model.SysConfig;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.sysUser.SysConfigService;
import cn.eeepay.framework.util.Constants;

//@Service("sysConfigService")
//@Transactional
public class SysConfigServiceImpl implements SysConfigService{
	private static final Logger log = LoggerFactory.getLogger(SysConfigServiceImpl.class);
	@Resource
	public SysConfigDao sysConfigDao;
//	@Resource
//	private RedisUtil redisUtil;
	@Resource
	private RedisService redisService;

	
	@Override
	public SysConfig findSysConfig(String sysKey,String sysValue) throws Exception {
		List<SysConfig> sysConfigs = findSysConfigGroup(sysKey);
		for (SysConfig sysConfig : sysConfigs) {
			if (sysConfig.getSysValue().equals(sysValue)) {
				return sysConfig;
			}
		}
		return null;
		//return sysConfigDao.findSysConfig(sysKey,sysValue);
	}



	@Override
	public List<SysConfig> findSysConfigGroup(String sysKey) throws Exception {
		List<SysConfig> sysConfigs = new ArrayList<SysConfig>();
		List<SysConfig> all = getAllConfigListByRedis();
		for (SysConfig sysConfig : all) {
			if (sysConfig.getSysKey().equals(sysKey)) {
				sysConfigs.add(sysConfig);
			}
		}
		return sysConfigs;
		//return sysConfigDao.findSysConfigGroup(sysKey);
	}


	@PostConstruct
	@Override
	public void init() throws Exception {
		List<SysConfig> sysConfigList= sysConfigDao.findAllSysConfig();
		redisService.insertList(Constants.sys_config_list_redis_key, sysConfigList);
		System.out.println("hello world");
		
	}
	
	public List<SysConfig> getAllConfigListByRedis() throws Exception {
		if (redisService.exists(Constants.sys_config_list_redis_key)) {
			Object  sysConfigListRedis  = redisService.select(Constants.sys_config_list_redis_key);
			if(sysConfigListRedis instanceof List){
				List sysConfigList = (List)sysConfigListRedis;
				for (Object object : sysConfigList) {
					ArrayList<SysConfig> sysConfigs = (ArrayList<SysConfig>)object;
					return sysConfigs;
				}
			}
		}
		else{
			//如果redis key 没有，或者redis没启动，用数据库获取数据
			List<SysConfig> sysConfigList= sysConfigDao.findAllSysConfig();
			return sysConfigList;
		}
		return null;
	}

	@Override
	public int insertSysConfig(SysConfig sysConfig) throws Exception {
		List<SysConfig> sysConfigList = getAllConfigListByRedis();
		sysConfigList.add(sysConfig);
		redisService.insertList(Constants.sys_config_list_redis_key, sysConfigList);
		return sysConfigDao.insertSysConfig(sysConfig);
	}

	@Override
	public int updateSysConfig(SysConfig sysConfig) throws Exception {
		List<SysConfig> sysConfigList = getAllConfigListByRedis();
		if(sysConfigList.contains(sysConfig)){
			sysConfigList.remove(sysConfig);
			sysConfigList.add(sysConfig);
		}
		redisService.insertList(Constants.sys_config_list_redis_key, sysConfigList);
		return sysConfigDao.updateSysConfig(sysConfig);
	}



	@Override
	public int deleteSysConfig(String id) throws Exception {
		List<SysConfig> sysConfigList = getAllConfigListByRedis();
		for (SysConfig sysConfig : sysConfigList) {
			if(sysConfig.getId().toString().equals(id)){
				sysConfigList.remove(sysConfig);
			}
		}
		redisService.insertList(Constants.sys_config_list_redis_key, sysConfigList);
		return sysConfigDao.deleteSysConfig(id);
	}

	@Override
	public int deleteSysConfigByParams(String sysKey, String sysValue) throws Exception {
		List<SysConfig> sysConfigList = getAllConfigListByRedis();
		for (SysConfig sysConfig : sysConfigList) {
			if(sysConfig.getSysKey().equals(sysKey) && sysConfig.getSysValue().equals(sysValue)){
				sysConfigList.remove(sysConfig);
			}
		}
		redisService.insertList(Constants.sys_config_list_redis_key, sysConfigList);
		return sysConfigDao.deleteSysConfigByParams(sysKey, sysValue);
	}

	@Override
	public String getStringValueByKey(String key) {
		return sysConfigDao.getStringValueByKey(key);
	}
}
