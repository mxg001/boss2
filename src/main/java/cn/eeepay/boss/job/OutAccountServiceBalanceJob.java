package cn.eeepay.boss.job;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.OutAccountServiceService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2018/8/24 16:26
 */
public class OutAccountServiceBalanceJob implements Job {

    private final Logger log = LoggerFactory.getLogger(OutAccountServiceBalanceJob.class);

    @Resource
    private OutAccountServiceService outAccountServiceService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       /* log.info("定时更新出款服务的上游余额start=====");

        //更新上游余额
        outAccountServiceService.updateUserBalance();

        log.info("定时更新出款服务的上游余额end=====");*/
    }
}
