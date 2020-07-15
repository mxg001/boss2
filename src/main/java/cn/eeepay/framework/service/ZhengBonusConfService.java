package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.rpc.entity.ZhengBonusConfEntity;

import java.util.List;

public interface ZhengBonusConfService {
	
	int saveZhengBonusConf(ZhengBonusConfEntity entity);
	
	List<ZhengBonusConfEntity> selectByOrgId(ZhengBonusConfEntity zhengBonusConfEntity,Page<ZhengBonusConfEntity> page);

	Result updateBonusConfById(ZhengBonusConfEntity entity);
}
