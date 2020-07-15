package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TimingProduceService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 出款结算中预警
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class PaymentTimeSettlementingJob extends ScheduleJob {

    @Resource
    private TimingProduceService timingProduceService;

    private static final Logger log = LoggerFactory.getLogger(PaymentTimeSettlementingJob.class);

    @Override
    protected void runTask(String runNo) {
        log.info("出款结算中预警定时任务执行......");
        timingProduceService.timingPaymentServiceInTheSettlement();
    }
}
