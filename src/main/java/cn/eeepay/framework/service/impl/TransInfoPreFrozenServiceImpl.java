package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.TransInfoPreFrozenDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TransInfoFreezeQueryCollection;
import cn.eeepay.framework.service.TransInfoPreFrozenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("transInfoPreFrozenService")
@Transactional
public class TransInfoPreFrozenServiceImpl implements TransInfoPreFrozenService{
	
	private static final Logger log = LoggerFactory.getLogger(TransInfoPreFrozenServiceImpl.class);

	@Resource
	private TransInfoPreFrozenDao transInfoPreFrozenDao;

	@Override
	public List<TransInfoFreezeQueryCollection> queryAllInfo(TransInfoFreezeQueryCollection transInfo, Page<TransInfoFreezeQueryCollection> page) {
		return transInfoPreFrozenDao.queryAllInfo(transInfo, page);
	}

	@Override
	public List<TransInfoFreezeQueryCollection> importAllInfo(TransInfoFreezeQueryCollection transInfo) {
		return transInfoPreFrozenDao.importAllInfo(transInfo);
	}
}
