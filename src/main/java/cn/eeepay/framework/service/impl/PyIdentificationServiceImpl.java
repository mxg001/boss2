package cn.eeepay.framework.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.PyIdentificationDao;
import cn.eeepay.framework.model.PyIdentification;
import cn.eeepay.framework.service.PyIdentificationService;

@Service("pyIdentificationService")
@Transactional
public class PyIdentificationServiceImpl implements PyIdentificationService{

	@Resource
	private PyIdentificationDao pyIdentificationDao;

	/**
	 *
	 * @param name 开户名
	 * @param idCard 开户身份证号
	 * @param accountNo 卡号
	 * @return
	 */
	@Override
	public PyIdentification queryByCheckInfo(String name, String idCard, String accountNo) {
		return pyIdentificationDao.queryByCheckInfo(name, idCard, accountNo);
	}

	@Override
	public int insert(PyIdentification pyIdentification) {
		return pyIdentificationDao.insert(pyIdentification);
	}

	
}
