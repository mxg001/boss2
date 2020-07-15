package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 超级银行家数据分析定时任务
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class AnalysisDataJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(AnalysisDataJob.class);

    @Resource
    private SuperBankService superBankService;

    @Override
    protected void runTask(String runNo) {
        log.info("======== 超级银行家数据分析定时任务 start ========");
        superBankService.analysisDataTask();
        log.info("======== 超级银行家数据分析定时任务 end ========");
    }
}
