package cn.eeepay.framework.service;

import cn.eeepay.framework.model.TimedTask;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/11/011.
 * @author liuks
 * 定时任务逻辑service
 */
public interface TimingProduceService {

    void timingTransaction();

    void timingPaymentServiceInTheSettlement();

    void timingPaymentServiceSettlementFailure();

    void timingPaymentServiceQuota();

    void taskWarningEvents(Date startDate, Date endDate, TimedTask tim,int state);
}
