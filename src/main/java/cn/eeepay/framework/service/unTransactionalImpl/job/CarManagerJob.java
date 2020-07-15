package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TaskService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 车管家前一天的已完成的订单修改成为5状态
 * @author MXG
 * create 2018/11/07
 */
@Component
@Scope("prototype")
public class CarManagerJob extends ScheduleJob {

    @Resource
    private TaskService taskService;

    @Override
    protected void runTask(String runNo) {
        taskService.updateCarManagerStatus();
    }
}
