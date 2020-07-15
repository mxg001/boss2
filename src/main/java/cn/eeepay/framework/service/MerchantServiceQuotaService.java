package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.model.MerchantServiceQuota;

public interface MerchantServiceQuotaService {

	public int updateByPrimaryKey(MerchantServiceQuota record);
	
	public List<MerchantServiceQuota> selectByMertId(MerchantServiceQuota record);
	
	//用于商户限额新增查询
	public MerchantServiceQuota addSelectInfo(MerchantServiceQuota record);
	
	public List<MerchantServiceQuota> selectByMertIdAndServiceId(String merId,String serId);
}
