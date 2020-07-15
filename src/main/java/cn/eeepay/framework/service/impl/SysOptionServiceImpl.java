package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperbank.SysOptionDao;
import cn.eeepay.framework.model.SysOption;
import cn.eeepay.framework.service.SysOptionService;

@Service("sysOptionService")
public class SysOptionServiceImpl implements SysOptionService {

	private Logger log = LoggerFactory.getLogger(SysOptionServiceImpl.class);
	
	@Resource
    private SysOptionDao sysOptionDao;
	
	@Override
	public List<SysOption> selectSysOption(SysOption sysOption) {
		
		return sysOptionDao.selectSysOption(sysOption);
	}

}
