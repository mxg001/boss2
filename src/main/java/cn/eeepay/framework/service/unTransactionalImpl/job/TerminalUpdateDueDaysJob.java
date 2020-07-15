package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TerminalUpdateDueDaysJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 机具活动考核更新考核剩余天数
 */
@Component
@Scope("prototype")
public class TerminalUpdateDueDaysJob extends ScheduleJob {

    @Resource
    private TerminalUpdateDueDaysJobService terminalChangeDueDaysJobService;

    private static final Logger log = LoggerFactory.getLogger(TerminalUpdateDueDaysJob.class);

    @Override
    protected void runTask(String runNo) {
        log.info("机具活动考核剩余天数定时任务开始......runningNo:{}", runNo);
        terminalChangeDueDaysJobService.terminalChangeDueDaysJob();
        log.info("机具活动考核剩余天数定时任务结束......runningNo:{}", runNo);
    }
}
