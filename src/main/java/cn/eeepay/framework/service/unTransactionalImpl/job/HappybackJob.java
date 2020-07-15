package cn.eeepay.framework.service.unTransactionalImpl.job;


import cn.eeepay.framework.service.HappyReturnJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/9/15/015.
 * @author  杨耀
 * liuks 改动
 */
@Component
@Scope("prototype")
public class HappybackJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(HappybackJob.class);

    @Resource
    private HappyReturnJobService happyReturnJobService;

    @Override
    protected void runTask(String runNo) {
        log.info("欢乐返入账开始...,runningNo:{}",runNo);
        happyReturnJobService.happyReturnJob();
        log.info("欢乐返入账结束...,runningNo:{}",runNo);
    }
}
