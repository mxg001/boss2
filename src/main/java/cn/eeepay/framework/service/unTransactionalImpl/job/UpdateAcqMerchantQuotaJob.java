package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.dao.AcqMerchantDao;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 重置acq_merchant交易限额
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class UpdateAcqMerchantQuotaJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(UpdateAcqMerchantQuotaJob.class);

    @Resource
    private AcqMerchantDao acqMerchantDao;

    @Override
    protected void runTask(String runNo) {
        log.info("重置acq_merchant交易限额");
        acqMerchantDao.updateBatchAcqMerchantQuota();
    }
}
