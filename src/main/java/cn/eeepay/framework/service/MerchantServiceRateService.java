package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.model.MerchantServiceRate;

public interface MerchantServiceRateService {

	public int deleteByPrimaryKey(Long id);

	public int insert(MerchantServiceRate record);


	public int updateByPrimaryKey(MerchantServiceRate record);

	//用户商户费率修改查询
	public List<MerchantServiceRate> selectByMertId(MerchantServiceRate rate);
	
	//用户商户费率新增查询
	public MerchantServiceRate addSelectInfo(MerchantServiceRate record);
	
	//组合商户费率
	public String profitExpression(MerchantServiceRate rate);
	
	//拆分商户费率
	public MerchantServiceRate setMerchantServiceRate(MerchantServiceRate rate);
	
	//用户商户费率修改查询
	public List<MerchantServiceRate> selectByMertIdAndSerivceId(String merId,String serId);
	
}
