package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysCalendar;

public interface SysCalendarService {

	void selectCalendarByCondition(Page<SysCalendar> page, SysCalendar sysCalendar);

	Map<String, Object> insertCalendar(SysCalendar parseObject);

	Map<String, Object> updateCalendar(SysCalendar parseObject);

	int deleteCalendar(Integer id);

	List<SysCalendar> getHolidayByYear(int year);

}
