package cn.eeepay.framework.service.unTransactionalImpl.job;


import cn.eeepay.framework.service.SubscribeVipPushJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author  yyao
 *
 */
@Component
@Scope("prototype")
public class SubscribeVipPushJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(SubscribeVipPushJob.class);

    @Resource
    private SubscribeVipPushJobService subscribeVipPushJobService;

    @Override
    protected void runTask(String runNo) {
        log.info("VIP优享到期极光推送开始...,runningNo:{}",runNo);
        subscribeVipPushJobService.subscribeVipPushJob();
        log.info("VIP优享到期极光推送结束...,runningNo:{}",runNo);
    }
}
