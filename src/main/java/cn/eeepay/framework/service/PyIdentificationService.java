package cn.eeepay.framework.service;

import cn.eeepay.framework.model.PyIdentification;

public interface PyIdentificationService {

	PyIdentification queryByCheckInfo(String name,String card,String accountNo);
	
	int insert(PyIdentification pyIdentification);
}
