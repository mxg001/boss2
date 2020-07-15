package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.risk.TradeRestrictService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/5/20/020.
 * @author liuks
 * 交易限制失效时间处理
 *
 */
@Component
@Scope("prototype")
public class TradeRestrictJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(TradeRestrictJob.class);

    @Resource
    private TradeRestrictService tradeRestrictService;

    @Override
    protected void runTask(String runNo) {
        log.info("--------------  风控交易限制记录失效,定时任务开始:--------------");
        tradeRestrictService.updateFailureTime();
        log.info("--------------  风控交易限制记录失效，定时任务结束--------------");
    }
}
