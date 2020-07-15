package cn.eeepay.boss.job;

import cn.eeepay.framework.service.UpdatehappyBackSumAmountService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/19/019.
 */
@DisallowConcurrentExecution
public class HappyBackCumulativeJob implements Job{

    private static final Logger log = LoggerFactory.getLogger(HappyBackCumulativeJob.class);

    @Resource
    private UpdatehappyBackSumAmountService updatehappyBackSumAmountService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
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
