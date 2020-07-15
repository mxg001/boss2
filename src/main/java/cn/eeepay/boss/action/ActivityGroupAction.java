package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.service.ActivityGroupService;

/**
 * 活动群组
 * @author tans
 * @date 2017年6月22日 下午4:56:01
 */
@Controller
@RequestMapping(value="/activityGroup")
public class ActivityGroupAction {

	private Logger log = LoggerFactory.getLogger(ActivityGroupAction.class);
	
	@Resource
	private ActivityGroupService activityGroupService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getAllActivityGroup")
	@ResponseBody
	public Map<String, Object> getAllActivityGroup(){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("获取活动群组失败",msg);
		try {
			msg = activityGroupService.getAllActivityGroup();
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("获取活动群组失败",msg);
			log.error("获取活动群组失败",e);
		}
		return msg;
	}
}
