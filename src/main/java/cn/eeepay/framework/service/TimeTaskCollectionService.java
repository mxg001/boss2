package cn.eeepay.framework.service;

/**
 * @author MXG
 * create 2018/11/06
 */
public interface TimeTaskCollectionService {

    /**
     * 未开户的商户到账户系统开户
     */
//    void createMerAcc();
//
//    /**
//     * 交易的结算状态更新(定时任务系统调用接口)
//     * @return
//     */
//    int updateSettleStatus();
//
//    /**
//     * 已过期的优惠劵更新状态
//     */
//    void updateCouponStatus();
//
//    /**
//     * YS同步商户(定时任务系统调用)
//     */
//    void ysMerAdd();
//
//    /**
//     * 再次出款定时任务
//     */
//    void reSettle();

    /**
     *  替换收单服务费率
     */
    void updateAcqServiceRate();

    /**
     * 代理商分润任务生效
     */
    void updateAgentShare();

    /**
     * 出款费率定时任务
     */
    void updateOutAccountService();

    /**
     * 车管家订单状态维护
     */
//    void updateCarManagerStatus();


}
