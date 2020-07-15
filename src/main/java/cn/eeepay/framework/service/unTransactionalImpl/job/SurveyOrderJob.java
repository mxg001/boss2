package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.risk.SurveyOrderService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2018/9/15/015.
 * @author  liuk
 * 调单订单更新
 */
@Component
@Scope("prototype")
public class SurveyOrderJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderJob.class);

    @Resource
    private SurveyOrderService surveyOrderService;

    @Override
    public void runTask(String runNo) {
        log.info("调单更新逾期订单定时执行开始...,runningNo:{}",runNo);
        surveyOrderService.updateOrderStateOverdue();
        log.info("调单更新逾期订单定时执行结束...,runningNo:{}",runNo);

    }
}
