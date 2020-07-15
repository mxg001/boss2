package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.luckDraw.PrizeConfigureService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/4/1/001.
 * 初始化抽奖
 */
@Component
@Scope("prototype")
public class InitAwardsConfigCountJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(InitAwardsConfigCountJob.class);

    @Resource
    private PrizeConfigureService prizeConfigureService;

    @Override
    protected void runTask(String runNo) {
        log.info("抽奖次数初始化 start ...,runningNo:{}",runNo);
        prizeConfigureService.initializationAwardsConfigCount();
        log.info("抽奖次数初始化 end ...,runningNo:{}",runNo);
    }
}
