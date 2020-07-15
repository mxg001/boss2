package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.ReSettleService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 再次出款定时任务
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class ReSettleJob extends ScheduleJob {
//    @Resource
//    private TimeTaskCollectionService timeTaskCollectionService;

    @Resource
    private ReSettleService reSettleService;

    @Override
    protected void runTask(String runNo) {
//        timeTaskCollectionService.reSettle();
        reSettleService.reSettle();
    }
}
