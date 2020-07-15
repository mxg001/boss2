package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAppInfo;

public interface AgentAppInfoService {

	List<AgentAppInfo> selectByCondition(Page<AgentAppInfo> page, Map<String, Object> params);

	AgentAppInfo selectDetailById(Long id);

	Map<String, Object> modifyAppInfo(Long id);

	int insertOrUpdate(AgentAppInfo appInfo);

}
