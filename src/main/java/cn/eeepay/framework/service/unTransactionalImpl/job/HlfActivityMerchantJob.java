package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.HlfActivityMerchantJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class HlfActivityMerchantJob extends ScheduleJob {
    @Resource
    private HlfActivityMerchantJobService hlfActivityMerchantJobService;

    private static final Logger log = LoggerFactory.getLogger(HlfActivityMerchantJob.class);

    @Override
    protected void runTask(String runNo) {
        try {
            log.info("活跃商户活动交易金额累计线程开始:----------->");
            hlfActivityMerchantJobService.hlfActivityMerchantJob();
            log.info("活跃商户活动交易金额累计线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("活跃商户活动交易金额累计线程异常:"+e.getMessage());
        }
    }
}
