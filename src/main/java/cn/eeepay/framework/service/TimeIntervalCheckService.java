package cn.eeepay.framework.service;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/28/028.
 * @author liuks
 * 时间校验service
 */
public interface TimeIntervalCheckService {

    boolean timeIntervalCheck(Date startDate,Date endDate);
}
