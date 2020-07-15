package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.pushManager.PushManagerService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import cn.eeepay.framework.serviceImpl.couponImport.PushManagerJobServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class PushManagerJob extends ScheduleJob {
    private static final Logger log = LoggerFactory.getLogger(PushManagerJob.class);

    @Resource
    private PushManagerJobServiceImpl pushManagerJobService;


    @Override
    protected void runTask(String runNo) {
        log.debug("推送内容推送开始...,runningNo:{}",runNo);
        pushManagerJobService.PushManagerJob();
        log.debug("推送内容推送开始...,runningNo:{}",runNo);
    }
}
