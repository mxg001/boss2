package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.FunctionMerchant;
import cn.eeepay.framework.model.MerchantInfo;

import java.util.List;

public interface MerFunctionManagerService {

	void selectByParam(FunctionMerchant functionMerchant, Page<FunctionMerchant> page);

	List<FunctionMerchant> exportConfig(FunctionMerchant functionMerchant);

	int addFunctionMerchant(FunctionMerchant functionMerchant);

	MerchantInfo findMerInfoByMerNo(String merNo);

    int selectExists(FunctionMerchant functionMerchant);

	FunctionMerchant get(Integer id);

	int delete(Integer id);

	void deleteInfo(String merchantNo, String functionNumber);



}
