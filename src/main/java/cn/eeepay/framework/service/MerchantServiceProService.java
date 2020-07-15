package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.MerchantService;

public interface MerchantServiceProService {

	public int updateByPrimaryKey(MerchantService record);

	public List<MerchantService> selectByMerId(String merId);

	List<String> selectServiceTypeByMerId(String merId);

	int updateTradeTypeByPrimaryKey(Long primaryKey, String tradeType, String channelCode);

	List<MerchantService> selectByMerAndMbpId(String merchantNo, String bpId);

	MerchantService  selectPosSerByMbpId(String mbpId);

	MerchantService selectQuickOrNoCardSerByMbpId(String mbpId);

	public MerchantService getMerchantServiceByID(Long id);
}
