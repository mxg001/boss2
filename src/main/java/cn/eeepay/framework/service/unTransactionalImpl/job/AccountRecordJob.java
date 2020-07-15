package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.AccountRecordService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 将所有交易成功且记账不成功的记录，循环调用账户接口
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class AccountRecordJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(AccountRecordJob.class);

//    @Resource
//    private TimeTaskCollectionService timeTaskCollectionService;

    @Resource
    private AccountRecordService accountRecordService;

    @Override
    protected void runTask(String runNo) {
        log.info("--------------  调用账户的接口，定时任务开始:--------------");
        accountRecordService.accountRecordTask();
        log.info("--------------  调用账户的接口，定时任务结束--------------");
    }
}
