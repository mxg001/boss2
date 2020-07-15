package cn.eeepay.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysCalendar;

public interface SysCalendarDao {

	@SelectProvider(type=SqlProvider.class,method="selectCalendarByCondition")
	@ResultType(SysCalendar.class)
	List<SysCalendar> selectCalendarByCondition(Page<SysCalendar> page, @Param("sysCalendar")SysCalendar sysCalendar);

	@Insert("insert into sys_calendar(event_name,year,month,day,week,sys_date,type,status) values ("
			+ "#{parseObject.eventName},#{parseObject.year},#{parseObject.month},#{parseObject.day},#{parseObject.week},"
			+ "#{parseObject.sysDate},#{parseObject.type},#{parseObject.status})")
	int insertCalendar(@Param("parseObject")SysCalendar parseObject);

	@Update("update sys_calendar set event_name = #{parseObject.eventName},year=#{parseObject.year},"
			+ "month=#{parseObject.month},day=#{parseObject.day},week=#{parseObject.week},sys_date=#{parseObject.sysDate}"
			+ ",type=#{parseObject.type},status=#{parseObject.status} where id=#{parseObject.id}")
	int updateCalendar(@Param("parseObject")SysCalendar parseObject);

	@Delete("delete from sys_calendar where id=#{id}")
	int deleteCalendar(Integer id);
	
	@SelectProvider(type=SqlProvider.class, method="getBySysDate")
	@ResultType(SysCalendar.class)
	SysCalendar getBySysDate(@Param("calendar")SysCalendar calendar);

	@Select("select * from sys_calendar where year=#{year} and status=1")
	List<SysCalendar> getHolidayByYear(@Param("year") int year);

	public class SqlProvider{
		
		public String selectCalendarByCondition(Map<String, Object> param){
			final SysCalendar sysCalendar = (SysCalendar) param.get("sysCalendar");
			return new SQL(){{
				SELECT("*");
				FROM("sys_calendar");
				if(sysCalendar!=null){
					if(StringUtils.isNotBlank(sysCalendar.getEventName())){
						sysCalendar.setEventName(sysCalendar.getEventName()+"%");
						WHERE("event_name like #{sysCalendar.eventName}");
					}
					if(sysCalendar.getType()!=null && sysCalendar.getType()!=0){
						WHERE("type = #{sysCalendar.type}");
					}
					if(sysCalendar.getStartDate() != null){
						WHERE("sys_date >= #{sysCalendar.startDate}");
					}
					if(sysCalendar.getEndDate() != null){
						WHERE("sys_date < #{sysCalendar.endDate}");
					}
					ORDER_BY("sys_date");
				}
				
			}}.toString();
		}
		
		public String getBySysDate(Map<String, Object> param){
			SysCalendar calendar = (SysCalendar) param.get("calendar");
			StringBuilder sql = new StringBuilder();
			sql.append("select sys_date from sys_calendar where sys_date=#{calendar.sysDate}");
			if(calendar!=null && calendar.getId()!=null){
				sql.append(" and id<>#{calendar.id}");
			}
			sql.append("  limit 1");
			return sql.toString();
		}
	}

	
}
