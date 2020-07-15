package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TimedTask;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author  liuks
 * 定时任务监控service
 */
public interface TimedTaskService {

    List<TimedTask> selectAllList(TimedTask tim, Page<TimedTask> page);

    void getSchedulerTimedTask();

    void clearTimedTaskState();

    void closeTimedTask(String jobName,String jobGroup);

    void resetTimedTask(String jobName, String jobGroup);

    int updateEnabledState(String jobName, String jobGroup);

    TimedTask getdetailTimedTask(int id);

    int saveDetailTimedTask(TimedTask tim);

    List<TimedTask> getTimedTaskByWarningState(int warningState);
}
