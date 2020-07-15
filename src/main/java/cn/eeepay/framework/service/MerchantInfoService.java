package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.*;

public interface MerchantInfoService {

	MerchantInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKey(MerchantInfo record);
	    
	List<MerchantInfo> selectAllInfo();
	
	List<MerchantInfo> selectByMertId(String mertId);
	
	int updateByMerId(MerchantInfo record);
	
	MerchantInfo selectByMerNo(String merchantNo);
	
	List<MerchantInfo> selectByNameInfoByTermianl(String oneAgentNo);
	
	SysDict selectSysDictByKey(String key,String Pkey);
	
	SysDict selectSysDict(String key);
	
	List<SysDict> selectTwoInfoByParentId(String ParentId);
	
	List<SysDict> selectOneInfo();
	
	MerchantInfo selectByMerIC(String card);

	MerchantInfo selectMerExistByMerNo(String merchantNo);
	
	int updateAddressByMerId(MerchantInfo record);
	 
	int updateMerAcoount(String merNo);
	 
	List<MerchantInfo> selectByMerAccount();
	 
	int updateRiskStatus(String merId,String riskStatus);

	int updateRiskStatusbyBlack(String merId,String riskStatus);

	MerchantInfo selectByMobilephone(String mobilephone);
	 
	boolean linkProduct(Map<String,String> params, UserLoginInfo principal) ;

	int updateMerAccountBatch(List<String> merchantNoList);

	MerchantInfo selectByMobilephoneAndTeam(String mobilephone, String teamId);

	MerchantInfo selectStatusByMerNo(String merchantNo);

	List<MerchantInfo> getMerchantFew(String item);


    List<ServiceRate> getServiceRateByServiceId(String oneAgentNo, String serviceId);

	List<ServiceQuota> getServiceQuotaByServiceId(String oneAgentNo, String serviceId);
}
