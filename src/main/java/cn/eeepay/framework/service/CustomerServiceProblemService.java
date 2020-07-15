package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CustomerServiceProblem;
import cn.eeepay.framework.model.CustomerServiceQo;

public interface CustomerServiceProblemService {

	void queryByQo(Page<CustomerServiceProblem> page, CustomerServiceQo qo);

	List<CustomerServiceProblem> listByQo(CustomerServiceQo qo);

	void removeById(String id);

	void save(CustomerServiceProblem data);

}
