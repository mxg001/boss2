package cn.eeepay.framework.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.MerchantCardInfoDao;
import cn.eeepay.framework.model.MerchantCardInfo;
import cn.eeepay.framework.service.MerchantCardInfoService;

@Service("merchantCardInfoService")
@Transactional
public class MerchantCardInfoServiceImpl implements MerchantCardInfoService {

	
	@Resource
	private MerchantCardInfoDao merchantCardInfoDao;
	

	@Override
	public int insert(MerchantCardInfo record) {
		return merchantCardInfoDao.insert(record);
	}


	@Override
	public MerchantCardInfo selectByMertId(String mertId) {
		return merchantCardInfoDao.selectByMertId(mertId);
	}


	@Override
	public MerchantCardInfo selectByMertIdAndAccountNo(MerchantCardInfo merchantCardInfo) {
		return merchantCardInfoDao.selectByMertIdAndAccountNo(merchantCardInfo);
	}


	@Override
	public int updateById(MerchantCardInfo record) {
		return merchantCardInfoDao.updateById(record);
	}


	@Override
	public int updateByMerId(MerchantCardInfo record) {
		return merchantCardInfoDao.updateByMerId(record);
	}

}
