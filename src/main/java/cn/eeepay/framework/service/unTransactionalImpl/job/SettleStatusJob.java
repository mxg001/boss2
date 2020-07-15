package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TaskService;
import cn.eeepay.framework.service.TimeTaskCollectionService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 交易的结算状态,t0交易转T1结算
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class SettleStatusJob extends ScheduleJob {



    @Resource
    private TaskService taskService;

    @Override
    protected void runTask(String runNo) {
        taskService.updateSettleStatus();
    }
}
