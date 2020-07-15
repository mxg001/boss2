package cn.eeepay.boss.job;

import cn.eeepay.framework.service.TimedTaskService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/3/8/008.
 */
@DisallowConcurrentExecution
public class TimedTaskJob implements Job {
    @Resource
    private TimedTaskService timedTaskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        timedTaskService.getSchedulerTimedTask();
    }
}
