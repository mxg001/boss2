package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BossOperLog;

/**
 * BossOperLogService
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:33:02
 */
public interface BossOperLogService {

	public  int insert(BossOperLog bossOperLog);
	
	BossOperLog queryDetail(Integer id);

	List<BossOperLog> queryByCondition(Page page, BossOperLog logInfo);
}
