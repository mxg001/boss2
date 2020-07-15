package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.job.PaymentServiceQuotaJob;
import cn.eeepay.boss.job.PaymentTimeSettlemenFailureJob;
import cn.eeepay.boss.job.PaymentTimeSettlementingJob;
import cn.eeepay.boss.job.TransactionTimeJob;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TimingProduceAdJobService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/16/016.
 * @author  liuks
 * 预警添加定时任务service实现类
 */
@Service("timingProduceAdJobService")
public class TimingProduceAdJobServiceImpl implements TimingProduceAdJobService {

    private final Logger log = LoggerFactory.getLogger(TimingProduceAdJobServiceImpl.class);

    public static final String TRANSACTIONTIMEDAY="transactionTimeDay";//交易定时key
    public static final String PAYMENTTIMESETTLEMENTINGDAY="paymentTimeSettlementingDay";//出款结算中定时key
    public static final String PAYMENTTIMESETTLEMENFAILUREDAY="paymentTimeSettlemenFailureDay";//出款结算失败定时key
    public static final String PAYMENTSERVICEQUOTADAY="paymentServiceQuotaDay";//出款服务额度预警key

    @Resource
    private SysDictService sysDictService;

    @Resource
    private QuartzManager quartzManager;

    /**
     * 交易
     */
    @Override
    public String addTransactionTime(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_TRANSACTION);
        int cycle=0;
        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(TimingProduceAdJobServiceImpl.TRANSACTIONTIMEDAY);
        job.setJob_name(TimingProduceAdJobServiceImpl.TRANSACTIONTIMEDAY);
        job.setJob_group(TimingProduceAdJobServiceImpl.TRANSACTIONTIMEDAY);
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        try{
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("交易预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        if(cycle<=0){
            log.info("交易预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        job.setJob_time("0 0/"+cycle+" * * * ?");
        try {
            quartzManager.addJob(job,TransactionTimeJob.class);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("交易预警定时设置失败!");
        }
        return "OK";
    }
    /**
     * 出款结算中
     */
    @Override
    public String addPaymentTimeSettlementing(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENTING);
        int cycle=0;
        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENTINGDAY);
        job.setJob_name(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENTINGDAY);
        job.setJob_group(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENTINGDAY);
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        try{
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("出款结算中预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        if(cycle<=0){
            log.info("出款结算中预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        job.setJob_time("0 0/"+cycle+" * * * ?");
        try {
            quartzManager.addJob(job,PaymentTimeSettlementingJob.class);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("出款结算中预警定时设置失败!");
        }
        return "OK";
    }

    /**
     * 出款结算失败
     */
    @Override
    public String addPaymentTimeSettlemenFailure(){
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_SETTLEMENT_FAILURE);
        int cycle=0;
        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENFAILUREDAY);
        job.setJob_name(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENFAILUREDAY);
        job.setJob_group(TimingProduceAdJobServiceImpl.PAYMENTTIMESETTLEMENFAILUREDAY);
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        try{
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("出款结算失败预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        if(cycle<=0){
            log.info("出款结算失败预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        job.setJob_time("0 0/"+cycle+" * * * ?");
        try {
            quartzManager.addJob(job,PaymentTimeSettlemenFailureJob.class);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("出款结算失败预警定时设置失败!");
        }
        return "OK";
    }

    /**
     * 出款服务额度预警
     * @return
     */
    @Override
    public String addPaymentServiceQuota() {
        SysDict cycleSysDict=sysDictService.getByKey(TimingProduceServiceImpl.TIMING_CYCLE_PAYMENT_SERVICE_QUOTA);
        int cycle=0;
        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(TimingProduceAdJobServiceImpl.PAYMENTSERVICEQUOTADAY);
        job.setJob_name(TimingProduceAdJobServiceImpl.PAYMENTSERVICEQUOTADAY);
        job.setJob_group(TimingProduceAdJobServiceImpl.PAYMENTSERVICEQUOTADAY);
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        try{
            cycle=Integer.valueOf(cycleSysDict.getSysValue());
        }catch (Exception e){
            e.printStackTrace();
            log.info("出款服务额度预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        if(cycle<=0){
            log.info("出款服务额度预警数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        job.setJob_time("0 0/"+cycle+" * * * ?");
        try {
            quartzManager.addJob(job,PaymentServiceQuotaJob.class);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("出款服务额度预警定时设置失败!");
        }
        return "OK";
    }

}
