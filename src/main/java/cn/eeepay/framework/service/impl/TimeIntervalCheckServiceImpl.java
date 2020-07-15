package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.exception.DateException;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TimeIntervalCheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/12/28/028.
 * @author liuks
 * 时间校验service
 */
@Service("timeIntervalCheckService")
public class TimeIntervalCheckServiceImpl implements TimeIntervalCheckService {

    public static final  String  TIMEINTERVAL ="TIMEINTERVAL";

    @Resource
    private SysDictService sysDictService;

    /**
     * @param startDate 开始时间
     * @param endDate  结束时间
     * @return 返回 true 符合
     */
    @Override
    public boolean timeIntervalCheck(Date startDate, Date endDate) throws DateException {
        List<SysDict> list=sysDictService.selectByKey(TimeIntervalCheckServiceImpl.TIMEINTERVAL);
        if(list!=null&&list.size()>0){
            if(list.size()==1){
                SysDict sd=list.get(0);
                String str=sd.getSysValue();
                int interval=Integer.valueOf(str);
                try {
                    int daysBetween=daysBetween(startDate,endDate);
                    if(daysBetween<=interval){
                        return true;
                    }else{
                        throw new DateException("0","时间跨度超过"+interval+"天,不允许查询");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new DateException("-1","时间解析异常!");
                }
            }else{
                throw new DateException("-1","时间校验-时间间隔-数据配置了多条记录!");
            }
        }else{
            throw new DateException("-1","时间校验-时间间隔-数据字典配置有问题!");
        }
    }
    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public  int daysBetween(Date smdate,Date bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
