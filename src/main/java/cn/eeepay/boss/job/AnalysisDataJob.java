package cn.eeepay.boss.job;

import cn.eeepay.framework.service.SuperBankService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 超级银行家数据分析定时任务
 * @author tans
 * @date 2018-08-24
 *
 */
@DisallowConcurrentExecution
public class AnalysisDataJob implements Job{

    private static final Logger log = LoggerFactory.getLogger(AnalysisDataJob.class);

    @Resource
    private SuperBankService superBankService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("======== 超级银行家数据分析定时任务 start ========");
        superBankService.analysisDataTask();
        log.info("======== 超级银行家数据分析定时任务 end ========");
    }
}


