package cn.eeepay.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.ActivityGroupDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.service.ActivityGroupService;

@Service("activityGroupService")
@Transactional
public class ActivityGroupServiceImpl implements ActivityGroupService {

	@Resource
	private SysDictDao sysDictDao;
	
	@Resource
	private ActivityGroupDao activityGroupDao;
	
	/**
	 * 获取所有的活动包含群组关系
	 */
	@Override
	public Map<String, Object> getAllActivityGroup() {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg","获取活动群组失败");
		List<Map<String, String>> activityTypeList = sysDictDao.getListByKey("ACTIVITY_TYPE");
		msg.put("status", true);
		msg.put("activityTypeList", activityTypeList);
		return msg;
	}

	@Override
	public int checkMutex(List<String> activityTypeList) {
		return activityGroupDao.checkMutex(activityTypeList);
	}

}
