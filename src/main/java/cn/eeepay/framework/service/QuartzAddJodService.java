package cn.eeepay.framework.service;

import org.quartz.SchedulerException;

/**
 * Created by Administrator on 2018/3/8/008.
 */
public interface QuartzAddJodService {

    //标准添加JOB方法，支持扩展
    void addPublicJob(String jobName, String jobGroup,Class<?> jobClass,int state) throws SchedulerException;

    void addAcqorgJob() throws SchedulerException;

}
