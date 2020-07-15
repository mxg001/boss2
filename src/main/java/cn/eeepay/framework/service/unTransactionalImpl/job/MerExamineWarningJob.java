package cn.eeepay.framework.service.unTransactionalImpl.job;


import cn.eeepay.framework.service.SysWarningService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class MerExamineWarningJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(MerExamineWarningJob.class);

    @Resource
    private SysWarningService sysWarningService;

    @Override
    protected void runTask(String runNo) {
        log.info("商户审核预警...,runningNo:{}",runNo);
        sysWarningService.merExamineWarningTask();
        log.info("商户审核预警...,runningNo:{}",runNo);
    }
}
