package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.MerchantServiceDao;
import cn.eeepay.framework.model.MerchantService;
import cn.eeepay.framework.service.MerchantServiceProService;

@Service("merchantServiceProService")
@Transactional
public class MerchantServiceProServiceImpl implements MerchantServiceProService{

	@Resource
	private MerchantServiceDao merchantServiceDao;

	@Override
	public int updateByPrimaryKey(MerchantService record) {
		return merchantServiceDao.updateByPrimaryKey(record);
	}

	@Override
	public List<MerchantService> selectByMerId(String merId) {
		return merchantServiceDao.selectByMerId(merId);
	}

	@Override
	public List<String> selectServiceTypeByMerId(String merId) {
		return merchantServiceDao.selectServiceTypeByMerId(merId);
	}

	@Override
	public int updateTradeTypeByPrimaryKey(Long primaryKey, String tradeType,String channelCode) {
		return merchantServiceDao.updateTradeTypeByPrimaryKey(primaryKey, tradeType,channelCode);
	}

	@Override
	public List<MerchantService> selectByMerAndMbpId(String merchantNo, String bpId) {
		return merchantServiceDao.selectByMerAndMbpId(merchantNo, bpId);
	}

	@Override
	public MerchantService selectPosSerByMbpId(String mbpId) {
		return merchantServiceDao.selectPosSerByMbpId(mbpId);
	}

	@Override
	public MerchantService selectQuickOrNoCardSerByMbpId(String mbpId) {
		return merchantServiceDao.selectQuickOrNoCardSerByMbpId(mbpId);
	}

	public MerchantService getMerchantServiceByID(Long id){
		return merchantServiceDao.getMerchantServiceByID(id);
	}
}
