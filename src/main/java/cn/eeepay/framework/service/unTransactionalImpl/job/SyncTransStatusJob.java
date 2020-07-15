package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 2小时同步一次交易状态
 * @author MXG
 * create 2018/11/07
 */
@Component
@Scope("prototype")
public class SyncTransStatusJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(SyncTransStatusJob.class);

    @Resource
    private TransInfoService transInfoService;

    @Override
    protected void runTask(String runNo) {
        log.info("同步交易状态定时任务开始");
        transInfoService.syncTransStatus();
        log.info("同步交易状态定时任务结束");
    }
}
