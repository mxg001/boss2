package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.BusinessRequireItemDao;
import cn.eeepay.framework.service.BusinessRequireItemService;

@Service("businessRequireItemService")
@Transactional
public class BusinessRequireItemServiceImpl implements BusinessRequireItemService {

	@Resource
	private BusinessRequireItemDao businessRequireItemDao;
	@Override
	public List<String> findByProduct(String bpId) {
		return businessRequireItemDao.findByProduct(bpId);
	}

}
