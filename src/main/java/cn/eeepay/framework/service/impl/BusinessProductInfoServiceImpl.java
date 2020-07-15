package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.BusinessProductInfoDao;
import cn.eeepay.framework.model.BusinessProductInfo;
import cn.eeepay.framework.service.BusinessProductInfoService;

@Service("businessProductInfoService")
@Transactional
public class BusinessProductInfoServiceImpl implements BusinessProductInfoService {

	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	@Override
	public List<BusinessProductInfo> selectInfoByBpId(String bpId) {
		return businessProductInfoDao.selectInfoByBpId(bpId);
	}
	@Override
	public List<BusinessProductInfo> getByBpId(String bpId) {
		return businessProductInfoDao.getByBpId(bpId);
	}

}
