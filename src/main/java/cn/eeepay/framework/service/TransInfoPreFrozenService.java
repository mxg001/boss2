package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransInfoPreFrozenService {

	List<TransInfoFreezeQueryCollection> queryAllInfo(TransInfoFreezeQueryCollection transInfo, Page<TransInfoFreezeQueryCollection> page);

	List<TransInfoFreezeQueryCollection> importAllInfo(TransInfoFreezeQueryCollection transInfo);
}
