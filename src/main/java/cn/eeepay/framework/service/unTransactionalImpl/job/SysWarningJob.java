package cn.eeepay.framework.service.unTransactionalImpl.job;


import cn.eeepay.framework.service.SysWarningJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * @author  yyao
 */
@Component
@Scope("prototype")
public class SysWarningJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(SysWarningJob.class);

    @Resource

    private SysWarningJobService sysWarningJobService;

    @Override
    protected void runTask(String runNo) {
        log.info("系统预警开始...,runningNo:{}",runNo);
        sysWarningJobService.sysWarningJob();
        log.info("系统预警结束...,runningNo:{}",runNo);
    }
}
