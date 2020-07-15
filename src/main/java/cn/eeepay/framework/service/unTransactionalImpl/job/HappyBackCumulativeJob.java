package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.UpdatehappyBackSumAmountService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class HappyBackCumulativeJob extends ScheduleJob {

    @Resource
    private UpdatehappyBackSumAmountService updatehappyBackSumAmountService;

    private static final Logger log = LoggerFactory.getLogger(HappyBackCumulativeJob.class);

    @Override
    protected void runTask(String runNo) {
        try {
            log.info("欢乐返商户交易金额累计线程开始:----------->");
            updatehappyBackSumAmountService.updatehappyBackCumulative();
            log.info("欢乐返商户交易金额累计线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("欢乐返商户交易金额累计线程异常:"+e.getMessage());
        }
    }
}
