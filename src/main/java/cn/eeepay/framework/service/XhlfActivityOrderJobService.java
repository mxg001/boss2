package cn.eeepay.framework.service;

import cn.eeepay.framework.model.XhlfActivityMerchantOrder;
import cn.eeepay.framework.model.XhlfActivityOrder;

import java.util.Date;
import java.util.List;

/**
 * @author tans
 * @date 2019/9/26 10:07
 */
public interface XhlfActivityOrderJobService {
    /**
     * 定时任务，新欢乐送代理商、商户批量统计，并批量入账
     */
    void countOrder();

    /**
     * 新欢乐送代理商批量入账
     * @param orderList
     * @return
     */
    int accountList(List<XhlfActivityOrder> orderList);

    void countOrder(Date startShortDate);

    /**
     * 新欢乐送商户批量入账
     * @param orderList
     * @return
     */
    int accountMerchantList(List<XhlfActivityMerchantOrder> orderList);

    /**
     * 统计指定日期的代理商奖励订单进行考核
     * @param nowShortDate
     */
    int countDateAgentOrder(Date nowShortDate);
}
