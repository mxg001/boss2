package cn.eeepay.framework.service;



import cn.eeepay.framework.model.PosCardBin;

import java.util.List;

public interface PosCardBinService {
	PosCardBin queryInfo(String accountNo);
	
	String queryBankNo(String accountNo);

	String getPoscnapsNoByBankName(String bankName);

    List<PosCardBin> queryAllInfo(String accountNo);
}
