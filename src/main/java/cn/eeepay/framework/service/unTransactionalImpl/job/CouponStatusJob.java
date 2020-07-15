package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TaskService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 已过期的优惠劵更新状态
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class CouponStatusJob extends ScheduleJob {

//    @Resource
//    private TimeTaskCollectionService timeTaskCollectionService;

    @Resource
    private TaskService taskService;

    @Override
    protected void runTask(String runNo) {
        taskService.updateCouponStatus();
    }
}
