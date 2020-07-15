package cn.eeepay.framework.service;

/**
 *@author liuks
 * 车管家类的订单状态(当前时间的前一天00：00：00分到23:59:59)更新为不结算
 */
public interface UpdateCheGuanHomeOrderService {
    public int updateCheGuanHomeOrder();
}
