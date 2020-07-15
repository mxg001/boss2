package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TaskService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 将所有未开户的商户上送到账户系统开户
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class CreateMerAccJob extends ScheduleJob {
    private static final Logger log = LoggerFactory.getLogger(CreateMerAccJob.class);
//    @Resource
//    private TimeTaskCollectionService timeTaskCollectionService;

    @Resource
    private TaskService taskService;


    @Override
    protected void runTask(String runNo) {
        log.info("=============开户定时任务开始=========");
//        timeTaskCollectionService.createMerAcc();
        taskService.createMerAcc();
        log.info("=============开户结束============");
    }
}
