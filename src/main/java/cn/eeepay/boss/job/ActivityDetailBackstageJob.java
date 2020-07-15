package cn.eeepay.boss.job;

import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.UpdatehappyBackSumAmountService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/30/030.
 */
@DisallowConcurrentExecution
public class ActivityDetailBackstageJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(HappyBackCumulativeJob.class);

    @Resource
    private ActivityDetailBackstageService activityDetailBackstageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("欢乐送,欢乐返活动延时核算清算线程开始:----------->");
            activityDetailBackstageService.execute();
            log.info("欢乐送,欢乐返活动延时核算清算线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("欢乐送,欢乐返活动延时核算清算线程异常:"+e.getMessage());
        }
    }
}
