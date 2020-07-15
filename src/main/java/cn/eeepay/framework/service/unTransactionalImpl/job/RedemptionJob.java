package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.RedemptionService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class RedemptionJob extends ScheduleJob {

    @Resource
    private RedemptionService redemptionService;

    private static final Logger log = LoggerFactory.getLogger(RedemptionJob.class);

    @Override
    protected void runTask(String runNo) {
        try {
            log.info("兑奖状态过期更新线程开始:----------->");
            redemptionService.updateRedemptionExpired();
            log.info("兑奖状态过期更新线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("兑奖状态过期更新线程异常:"+e.getMessage());
        }
    }
}
