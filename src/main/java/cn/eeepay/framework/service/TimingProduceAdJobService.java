package cn.eeepay.framework.service;

/**
 * Created by Administrator on 2018/1/16/016.
 * @author  liuks
 * 预警添加定时任务service
 */
public interface TimingProduceAdJobService {

    String addTransactionTime();

    String addPaymentTimeSettlementing();

    String addPaymentTimeSettlemenFailure();

    String addPaymentServiceQuota();
}
