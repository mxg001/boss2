package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PosCardBinDao;
import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.service.PosCardBinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("posCardBinService")
@Transactional
public class PosCardBinServiceImpl implements PosCardBinService {

	@Resource
	private PosCardBinDao posCardBinDao;
	
	@Override
	public PosCardBin queryInfo(String accountNo) {
		return posCardBinDao.queryInfo(accountNo);
	}

	@Override
	public String queryBankNo(String accountNo) {
		return posCardBinDao.queryBankNo(accountNo);
	}

	public List<Map<String, Object>> getCarBinList(int cardType) {

		return posCardBinDao.getCarBinList(cardType);
	}

	@Override
	public String getPoscnapsNoByBankName(String bankName) {
		return posCardBinDao.getPoscnapsNoByBankName(bankName);
	}

	@Override
	public List<PosCardBin> queryAllInfo(String accountNo) {
		return posCardBinDao.queryAllInfo(accountNo);
	}
}
