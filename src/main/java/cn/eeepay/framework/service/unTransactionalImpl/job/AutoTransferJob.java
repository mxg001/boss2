package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.AutoTransferService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class AutoTransferJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(AutoTransferJob.class);

    @Resource
    private AutoTransferService autoTransferService;

    @Override
    protected void runTask(String runNo) {
        log.info("T1自动出款定时任务开始。。。runningNo:{}", runNo);
        autoTransferService.t1AutoTransfer();
        log.info("T1自动出款定时任务结束。。。runningNo:{}", runNo);
    }
}
