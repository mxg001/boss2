package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TaskService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * YS商户每几分钟跑一次查询同步未成功再次发起一次同步
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class YSmerAddJob extends ScheduleJob {

    @Resource
    private TaskService taskService;

    @Override
    protected void runTask(String runNo) {
        taskService.ysMerAdd();
    }
}
