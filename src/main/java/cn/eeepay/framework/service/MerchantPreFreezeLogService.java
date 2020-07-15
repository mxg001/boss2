package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.TransInfoPreFreezeLog;

public interface MerchantPreFreezeLogService {

	int insertPreFreezeLog(TransInfoPreFreezeLog record);

	void insertLogAndUpdateMerchantInfoAmount(TransInfoPreFreezeLog record);

	List<TransInfoPreFreezeLog> selectByMerchantNo(String merchantNo);
}
