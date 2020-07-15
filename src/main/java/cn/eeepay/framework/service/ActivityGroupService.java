package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

public interface ActivityGroupService {

	Map<String, Object> getAllActivityGroup();

	/**
	 * 检查活动类型是否存在互斥
	 * @author tans
	 * @date 2017年6月24日 下午3:24:48
	 * @param activityTypeSet
	 * @return
	 */
	int checkMutex(List<String> activityTypeList);

}
