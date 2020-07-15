package cn.eeepay.framework.service;

/**
 * 定时任务集合
 * 不要事务的那种
 * @author tans
 * @date 2019/4/12 10:32
 */
public interface TaskService {

    /**
     * 未开户的商户到账户系统开户
     */
    void createMerAcc();

    /**
     * 交易的结算状态更新(定时任务系统调用接口)
     * @return
     */
    int updateSettleStatus();

    /**
     * 已过期的优惠劵更新状态
     */
    void updateCouponStatus();

    /**
     * YS同步商户(定时任务系统调用)
     */
    void ysMerAdd();

    /**
     * 车管家订单状态维护
     */
    void updateCarManagerStatus();
}
