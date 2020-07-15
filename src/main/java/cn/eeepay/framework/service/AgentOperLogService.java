package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLog;

import java.util.List;

/**
 * AgentOperLogService
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:33:02
 */
public interface AgentOperLogService {

	int insert(AgentOperLog bossOperLog);

	AgentOperLog queryDetail(Integer id);

	List<AgentOperLog> queryByCondition(Page page, AgentOperLog logInfo);
}
