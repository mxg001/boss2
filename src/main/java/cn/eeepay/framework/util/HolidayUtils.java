package cn.eeepay.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/29 14:51
 */
public class HolidayUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static void main(String[] args) throws ParseException {
        //查询数据库中holiday,遍历存入list（表中每条记录存放的是假期的起止日期,遍历每条结果,并将其中的每一天都存入holiday的list中），以下为模拟假期
        List holidayList = new ArrayList();
        holidayList.add("2020-05-14");
        holidayList.add("2020-05-15");
        holidayList.add("2020-05-16");
        holidayList.add("2020-05-17");

        //获取计划激活日期
        Date scheduleActiveDate = getScheduleActiveDate(new Date(),holidayList,3);
        System.out.println("3个工作日后,即计划激活日期为::" + sdf.format(scheduleActiveDate));
    }


    /**
     * 获取计划激活日期
     * @param today opening date
     * @param list holidayList
     * @param num num个工作日后
     * @return
     * @throws ParseException
     */
    public static Date getScheduleActiveDate(Date today, List<String> list, int num) throws ParseException {
        Date tomorrow = null;
        int delay = 1;
        while(delay <= num){
            tomorrow = getTomorrow(today);
            if(!isHoliday(sdf.format(tomorrow),list)){
                delay++;
                today = tomorrow;
            } else {
                today = tomorrow;
            }
        }

        return today;
    }

    /**
     * 获取tomorrow的日期
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 判断是否是weekend
     *
     * @param sdate
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(String sdate) throws ParseException {
        Date date = sdf.parse(sdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }

    /**
     * 判断是否是holiday
     *
     * @param sdate
     * @param list
     * @return
     * @throws ParseException
     */
    public static boolean isHoliday(String sdate, List<String> list) throws ParseException {
        if(list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                if(sdate.equals(list.get(i))){
                    return true;
                }
            }
        }
        return false;
    }
}