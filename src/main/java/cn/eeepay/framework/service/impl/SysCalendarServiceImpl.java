package cn.eeepay.framework.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.SysCalendarDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysCalendar;
import cn.eeepay.framework.service.SysCalendarService;

@Service("sysCalendarService")
@Transactional
public class SysCalendarServiceImpl implements SysCalendarService {
	
	@Resource
	private SysCalendarDao sysCalendarDao;

	@Override
	public void selectCalendarByCondition(Page<SysCalendar> page, SysCalendar sysCalendar) {
		sysCalendarDao.selectCalendarByCondition(page,sysCalendar);
	}

	@Override
	public Map<String, Object> insertCalendar(SysCalendar parseObject) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败！");
		SysCalendar oldDate = sysCalendarDao.getBySysDate(parseObject);
		if(oldDate!=null){
			msg.put("msg", "系统日期已存在");
			return msg;
		}
		Date sysDate = parseObject.getSysDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sysDate);
		parseObject.setYear(calendar.get(Calendar.YEAR));  
		parseObject.setMonth(calendar.get(Calendar.MONTH)+1);  
		parseObject.setDay(calendar.get(Calendar.DAY_OF_MONTH));  
		parseObject.setWeek(calendar.get(Calendar.DAY_OF_WEEK));
		sysCalendarDao.insertCalendar(parseObject);
		msg.put("status", true);
		msg.put("msg", "添加成功");
		return msg;
	}

	@Override
	public Map<String, Object> updateCalendar(SysCalendar parseObject) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "添加失败！");
		SysCalendar oldDate = sysCalendarDao.getBySysDate(parseObject);
		if(oldDate!=null){
			msg.put("msg", "系统日期已存在");
			return msg;
		}
		Date sysDate = parseObject.getSysDate();
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(sysDate); 
		parseObject.setYear(calendar.get(Calendar.YEAR));  
		parseObject.setMonth(calendar.get(Calendar.MONTH)+1);  
		parseObject.setDay(calendar.get(Calendar.DAY_OF_MONTH));  
		parseObject.setWeek(calendar.get(Calendar.DAY_OF_WEEK));
		sysCalendarDao.updateCalendar(parseObject);
		msg.put("status", true);
		msg.put("msg", "添加成功");
		return msg;
	}

	@Override
	public int deleteCalendar(Integer id) {
		return sysCalendarDao.deleteCalendar(id);
	}


	@Override
	public List<SysCalendar> getHolidayByYear(int year) {
		return sysCalendarDao.getHolidayByYear(year);
	}
}
