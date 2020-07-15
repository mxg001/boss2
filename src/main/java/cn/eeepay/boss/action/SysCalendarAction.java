
package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysCalendar;
import cn.eeepay.framework.service.SysCalendarService;
//import cn.eeepay.framework.service.sysUser.UserRigthService;
@Controller
@RequestMapping(value = "/sysCalendar")
public class SysCalendarAction {
	private static final Logger log = LoggerFactory.getLogger(SysCalendarAction.class);
	@Resource
	public SysCalendarService sysCalendarService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/selectCalendarByCondition.do")
	@ResponseBody
	public Page<SysCalendar> selectCalendarByCondition(@RequestParam("baseInfo") String baseInfo,
			@Param("page") Page<SysCalendar> page) throws Exception{
		SysCalendar sysCalendar = JSON.parseObject(baseInfo, SysCalendar.class);
		try{
			sysCalendarService.selectCalendarByCondition(page, sysCalendar);
		} catch(Exception e) {
			log.error("查询所有日历失败",e);
		}
		return page;
	}
	
	// 新增日历
	@RequestMapping(value = "/saveCalendar.do")
	@ResponseBody
	@SystemLog(description = "新增日历",operCode="sys.calendar")
	public Map<String, Object> saveCalendar(@RequestBody String Calendar) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败！");
		try {
			msg = sysCalendarService.insertCalendar(JSONObject.parseObject(Calendar, SysCalendar.class));
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "添加失败！");
			log.error("新增日历失败",e);
		}
		return msg;
	}

	// 修改用户
	@RequestMapping(value = "/updateCalendar.do")
	@ResponseBody
	@SystemLog(description = "修改日历",operCode="calendar.update")
	public Map<String, Object> updateCalendar(@RequestBody String Calendar) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败！");
		try {
			msg = sysCalendarService.updateCalendar(JSONObject.parseObject(Calendar, SysCalendar.class));
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "修改失败！");
			log.error("修改日历失败",e);
		}
		return msg;
	}
	
	@RequestMapping(value="/deleteCalendar.do")
	@ResponseBody
	@SystemLog(description = "删除日历",operCode="calendar.delete")
	public Map<String, Object> deleteCalendar(@RequestParam("id")Integer id) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = sysCalendarService.deleteCalendar(id);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("删除日历失败",e);
		}
		return msg;
	}

}
