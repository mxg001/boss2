package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.model.ExaminationsLog;

public interface ExaminationsLogService {

	 public int insert(ExaminationsLog record);

	 public List<ExaminationsLog> selectByMerchantId(String merchantId);
	 
	 public ExaminationsLog selectByitemNo(String itemNo);

	int insertLogExt(ExaminationsLog record);
}
