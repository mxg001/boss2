package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrderDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * 还款订单处理流水
 * @author liuks
 */
public interface CreditRepayOrderDetailService {
    /**
     * 通告批次号查询订单子详情
     */
    List<CreditRepayOrderDetail> selectDetailList(String batchNo);

    /**
     * 分页查询还款计划流水详情
     * @param orderDetail
     * @param page
     * @return
     */
    List<CreditRepayOrderDetail> selectDetailAllList(CreditRepayOrderDetail orderDetail, Page<CreditRepayOrderDetail> page);

    /**
     *动态条件查询导出数据
     */
    List<CreditRepayOrderDetail> importDetailAllList(CreditRepayOrderDetail orderDetail);

    /**
     * 统计还款订单流水总金额
     * @param orderDetail
     * @return
     */
    BigDecimal selectDetailAllListSum(CreditRepayOrderDetail orderDetail);
}
