package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.OutAccountServiceService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 查询出款服务在上游的剩余额度，同步BOSS里的余额
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class OutAccountServiceBalanceJob extends ScheduleJob {

    @Resource
    private OutAccountServiceService outAccountServiceService;

    private static final Logger log = LoggerFactory.getLogger(OutAccountServiceBalanceJob.class);

    @Override
    protected void runTask(String runNo) {
        log.info("定时更新出款服务的上游余额start=====");
        //更新上游余额
        outAccountServiceService.updateUserBalance();
        log.info("定时更新出款服务的上游余额end=====");
    }
}
