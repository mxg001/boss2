package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.HlfActivityAgentJobService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class HlfActivityAgentJob extends ScheduleJob {
    @Resource
    private HlfActivityAgentJobService hlfActivityAgentJobService;

    private static final Logger log = LoggerFactory.getLogger(HlfActivityAgentJob.class);

    @Override
    protected void runTask(String runNo) {
        try {
            log.info("代理商奖励活动交易金额累计线程开始:----------->");
            hlfActivityAgentJobService.hlfActivityAgentJob();
            log.info("代理商奖励活动交易金额累计线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("代理商奖励活动交易金额累计线程异常:"+e.getMessage());
        }
    }
}
