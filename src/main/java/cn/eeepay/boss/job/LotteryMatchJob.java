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
 * 超级银行家用户开户
 * @author tans
 * @date 2017-12-16
 *
 */
@DisallowConcurrentExecution
public class LotteryMatchJob implements Job{

    private static final Logger log = LoggerFactory.getLogger(LotteryMatchJob.class);

    @Resource
    private SuperBankService superBankService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("======== 超级银行家彩票匹配定时任务 start ========");
        superBankService.lotteryMatchTask();
        log.info("======== 超级银行家彩票匹配定时任务 end ========");
    }
}


