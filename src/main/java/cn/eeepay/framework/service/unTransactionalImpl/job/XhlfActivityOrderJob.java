package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.XhlfActivityOrderJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2019/9/26 10:04
 */
@Component
@Scope("prototype")
public class XhlfActivityOrderJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(XhlfActivityOrderJob.class);

    @Resource
    private XhlfActivityOrderJobService xhlfActivityOrderJobService;

    @Override
    protected void runTask(String runNo) {
        log.info("--------------  新欢乐返活动订单，XhlfActivityOrderJob.start:--------------");
        xhlfActivityOrderJobService.countOrder();
        log.info("--------------  新欢乐返活动订单，XhlfActivityOrderJob.end--------------");
    }
}
