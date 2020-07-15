package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.ScheduleDao;
import cn.eeepay.framework.service.ScheduleService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import cn.eeepay.framework.service.unTransactionalImpl.job.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleDao scheduleDao;

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private BeanFactory beanFactory;

    @Override
    public int insertTask(String runNo,String interfaceName) {
        return scheduleDao.insert(runNo,interfaceName);
    }

    @Override
    public int updateTask(String runNo, String status) {
        return scheduleDao.updateStatus(runNo, status);
    }

    @Override
    public Map<String, Object> queryTask(String runNo) {
        return scheduleDao.query(runNo);
    }

    public Map<String, Object> process(String runNo, String interfaceType) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("runningNo", runNo);
        Map<String, Object> task = queryTask(runNo);
        if (task != null) {
            //针对定时系统 只支持传值running,complete类型
            if("init".equals(task.get("running_status"))){
                msg.put("runningStatus","running");
            }else{
                msg.put("runningStatus", task.get("running_status"));
            }
        } else {
            insertTask(runNo, interfaceType);
            ScheduleJob runnable = ScheduleJobEnum.getSpringBean(beanFactory, interfaceType);
            if (runnable != null) {
                scheduleDao.updateStatus(runNo, "running");
                runnable.setRunNo(runNo);
                taskExecutor.execute(runnable);
            }
            msg.put("runningStatus", "running");
        }
        return msg;
    }

    enum ScheduleJobEnum{
        happy(HappybackJob.class, "欢乐返定时任务")
        , surveyOverdue(SurveyOrderJob.class, "调单")
        , accountRecord(AccountRecordJob.class, "交易调账户系统记账")
        , createMerAcc(CreateMerAccJob.class, "未开户的商户到账户系统开户")
        , settleStatus(SettleStatusJob.class, "交易的结算状态更新")
        , couponStatus(CouponStatusJob.class, "已过期的优惠劵更新状态")
        , ysMerAdd(YSmerAddJob.class, "YS商户同步")
        , reSettle(ReSettleJob.class, "再次出款")
        , superBankUserAccount(SuperBankUserJob.class, "未开户的超级银行家账户系统开户")
        , outAccountServiceBalance(OutAccountServiceBalanceJob.class, "出款服务余额和上游同步")
        , transactionTime(TransactionTimeDayJob.class, "交易异常预警")
        , paymentTimeSettlementing(PaymentTimeSettlementingJob.class, "出款结算中预警")
        , paymentTimeSettlemenFailure(PaymentTimeSettlemenFailureJob.class, "出款结算失败预警")
        , paymentServiceQuota(PaymentServiceQuotaJob.class, "出款服务额度预警")
        , happyBackSumAmount(HappyBackCumulativeJob.class, "欢乐返金额统计")
        , activityDetailBackstage(ActivityDetailBackstageJob.class, "欢乐返活动延时核算清算定时")
        , lotteryMatch(LotteryMatchJob.class, "超级银行家彩票导入文件匹配的定时任务")
        , analysisData(AnalysisDataJob.class, "超级银行家数据分析定时任务")
        , updateAcqServiceRate(UpdateAcqServiceRateJob.class, "替换收单服务费率")
        , updateAgentShare(UpdateAgentShareJob.class, "代理商分润任务生效")
        , updateOutAccountService(UpdateOutAccountServiceJob.class, "出款费率定时任务")
        , updateAcqMerchantQuota(UpdateAcqMerchantQuotaJob.class, "重置acq_merchant交易限额")
        , merchantMigrate(MerchantMigrateJob.class, "商户迁移转移一级代理商")
        , updateCarManagerStatus(CarManagerJob.class, "车管家订单状态维护")
        , syncTransStatus(SyncTransStatusJob.class, "2小时同步一次交易状态")
        , couponTimeStatus(CouponActivityTimeJob.class, "2分钟更新上下线时间状态状态")
        , redemptionExpired(RedemptionJob.class, "兑奖状态过期")
        , amountSysWarning(SysWarningJob.class, "目标金额预警")
        , tradeSum(TradeSumJob.class, "三方交易汇总报表大约一小时")
        , tradeSumHistory(TradeSumHistoryJob.class, "三方交易汇总历史报表大约一小时")
        , initLuckCount(InitAwardsConfigCountJob.class, "抽奖次数初始化0凌晨0时更新")
        , subscribeVipPush(SubscribeVipPushJob.class, "VIP优享到期推送")
        , tradeRestrict(TradeRestrictJob.class, "风控交易限制记录失效")
        , terminalUpdateDueDays(TerminalUpdateDueDaysJob.class, "机具活动考核更新考核剩余天数")
        , PushManagerJob(PushManagerJob.class, "推送管理")
        , happyBackMerSumAmount(HlfActivityMerchantJob.class, "活跃商户金额统计")
        , xhlfActivityOrderJob(XhlfActivityOrderJob.class, "新欢乐送（返）定时任务")
        , hlfActivityAgentJob(HlfActivityAgentJob.class, "欢乐返代理商奖励定时任务")
        , deleteFileJob(DeleteFileJob.class, "")
        , merExamineWarningJob(MerExamineWarningJob.class, "商户审核预警")
        , autoTransferJob(AutoTransferJob.class, "T1自动出款定时任务")
        , vasShareRuleTaskJob(VasShareRuleTaskJob.class, "增值服务分润生效定时任务")
        ;

        private String remark;
        private Class<? extends ScheduleJob> clazz;

        ScheduleJobEnum(Class<? extends ScheduleJob> clazz, String remark) {
            this.remark = remark;
            this.clazz = clazz;
        }

        public static Class<? extends ScheduleJob> ofName(String jobName){
            for (ScheduleJobEnum job : ScheduleJobEnum.values()) {
                if(job.name().equals(jobName)){
                    return job.clazz;
                }
            }
            return null;
        }

        public static ScheduleJob getSpringBean(BeanFactory beanFactory, String jobName) {
            Object obj = beanFactory.getBean(ofName(jobName));
            if (obj instanceof ScheduleJob) {
                return (ScheduleJob) obj;
            }
            return null;
        }
    }

}
