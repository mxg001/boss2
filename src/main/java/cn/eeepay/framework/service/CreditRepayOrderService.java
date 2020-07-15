package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrder;

import java.util.List;

/**
 * 信用卡还款订单服务
 * @author liuks
 */
public interface CreditRepayOrderService {
    /**
     *动态分页查询
     */
    List<CreditRepayOrder> selectAllList(CreditRepayOrder order, Page<CreditRepayOrder> page);
    /**
     *动态分页查询统计总金额
     */
    CreditRepayOrder selectAllListSum(CreditRepayOrder order);
    /**
     *动态条件查询导出数据
     */
    List<CreditRepayOrder> importSelectAllList(CreditRepayOrder order);

    /**
     *通告批次号查询
     */
    CreditRepayOrder selectById(String batchNo,String tallyOrderNo,int sta);

    /**
     * 查询订单状态
     * @author	mays
     * @date	2018年5月10日
     */
    String selectStatusByBatchNo(String batchNo);

    /**
     * 查询订单（不分页）
     * @param condition
     * @return
     */
    List<CreditRepayOrder> selectAbnormalByParam(CreditRepayOrder condition);

}
