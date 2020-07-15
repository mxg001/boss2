package cn.eeepay.framework.service.unTransactionalImpl;


import cn.eeepay.framework.service.VasInfoService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by rpc on 2020/05/18.
 * @author  rpc
 * 增值服务分润生效任务
 */
@Component
@Scope("prototype")
public class VasShareRuleTaskJob extends ScheduleJob {
    private final Logger log = LoggerFactory.getLogger(VasShareRuleTaskJob.class);

    @Resource
    private VasInfoService vasInfoService;


    @Override
    protected void runTask(String runNo) {
        log.info("增值服务分润生效定时任务执行......");
        vasInfoService.updateVasShareRuleTask();
    }


}
