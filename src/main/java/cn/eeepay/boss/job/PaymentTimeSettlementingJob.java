package cn.eeepay.boss.job;

import cn.eeepay.framework.service.TimingProduceService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/16/016.
 * @author liuks
 * 出款结算中定时Job
 */
@DisallowConcurrentExecution
public class PaymentTimeSettlementingJob implements Job {

    private final Logger log = LoggerFactory.getLogger(PaymentTimeSettlementingJob.class);

    @Resource
    private TimingProduceService timingProduceService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("出款结算中预警定时任务执行......");
        timingProduceService.timingPaymentServiceInTheSettlement();
    }
}
