package cn.eeepay.framework.service;



import cn.eeepay.framework.model.MerchantCardInfo;

public interface MerchantCardInfoService {

	public int insert(MerchantCardInfo record);

	public MerchantCardInfo selectByMertId(String mertId);
	
	MerchantCardInfo selectByMertIdAndAccountNo(MerchantCardInfo merchantCardInfo);
	
	 int updateById(MerchantCardInfo record);
	 
	 int updateByMerId(MerchantCardInfo record);
}
