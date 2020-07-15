package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.ExaminationsLogDao;
import cn.eeepay.framework.model.ExaminationsLog;
import cn.eeepay.framework.service.ExaminationsLogService;

@Service("ExaminationsLogService")
@Transactional
public class ExaminationsLogServiceImpl implements ExaminationsLogService {

	@Resource
	private ExaminationsLogDao examinationsLogDao;

	@Override
	public int insert(ExaminationsLog record) {
		return examinationsLogDao.insert(record);
	}

	@Override
	public List<ExaminationsLog> selectByMerchantId(String merchantId) {
		return examinationsLogDao.selectByMerchantId(merchantId);
	}

	@Override
	public ExaminationsLog selectByitemNo(String itemNo) {
		return examinationsLogDao.selectByitemNo(itemNo);
	}

	@Override
	public int insertLogExt(ExaminationsLog record) {
		return examinationsLogDao.insertLogExt(record);
	}


}
