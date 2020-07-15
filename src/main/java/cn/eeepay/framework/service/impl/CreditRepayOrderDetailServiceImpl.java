package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CreditRepayOrderDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrderDetail;
import cn.eeepay.framework.service.CreditRepayOrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 还款订单处理流水
 * @author liuks
 */
@Service("creditRepayOrderDetailService")
@Transactional
public class CreditRepayOrderDetailServiceImpl implements CreditRepayOrderDetailService {
    @Override
    public BigDecimal selectDetailAllListSum(CreditRepayOrderDetail orderDetail) {
        return creditRepayOrderDetailDao.selectDetailAllListSum(orderDetail);
    }

    @Resource
    private CreditRepayOrderDetailDao creditRepayOrderDetailDao;

    @Override
    public List<CreditRepayOrderDetail> selectDetailList(String batchNo) {
        return creditRepayOrderDetailDao.selectDetailList(batchNo);
    }

    @Override
    public List<CreditRepayOrderDetail> selectDetailAllList(CreditRepayOrderDetail orderDetail, Page<CreditRepayOrderDetail> page) {
        return creditRepayOrderDetailDao.selectDetailAllList(orderDetail,page);
    }

    @Override
    public List<CreditRepayOrderDetail> importDetailAllList(CreditRepayOrderDetail orderDetail) {
        return creditRepayOrderDetailDao.importDetailAllList(orderDetail);
    }
}
