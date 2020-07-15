package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 欢乐返活动延时核算清算定时
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class ActivityDetailBackstageJob extends ScheduleJob {

    @Resource
    private ActivityDetailBackstageService activityDetailBackstageService;

    private static final Logger log = LoggerFactory.getLogger(HappyBackCumulativeJob.class);

    @Override
    protected void runTask(String runNo) {
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
