package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CreditRepayOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrder;
import cn.eeepay.framework.service.CreditRepayOrderService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信用卡还款订单服务实现类
 * @author liuks
 */
@Service("creditRepayOrderService")
@Transactional
public class CreditRepayOrderServiceImpl implements CreditRepayOrderService {
    @Resource
    private CreditRepayOrderDao creditRepayOrderDao;

    @Override
    public List<CreditRepayOrder> selectAllList(CreditRepayOrder order, Page<CreditRepayOrder> page) {
        List<CreditRepayOrder> list=creditRepayOrderDao.selectAllList(order,page);
        dataProcessingList(page.getResult());
        return list;
    }

    /**
     * 数据处理List
     */
    private void dataProcessingList(List<CreditRepayOrder> list){
        if(list!=null&&list.size()>0){
            for(CreditRepayOrder item:list){
                if(item!=null){
                    item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
                }
            }
        }
    }

    /**
     * 数据处理item
     */
    private void dataProcessingItem(CreditRepayOrder item){
        if(item!=null){
            item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
            item.setIdCardNo(StringUtil.sensitiveInformationHandle(item.getIdCardNo(),1));
        }
    }

    @Override
    public CreditRepayOrder selectAllListSum(CreditRepayOrder order) {
        return creditRepayOrderDao.selectAllListSum(order);
    }

    @Override
    public List<CreditRepayOrder> importSelectAllList(CreditRepayOrder order) {
        List<CreditRepayOrder> list=creditRepayOrderDao.importSelectAllList(order);
        dataProcessingList(list);
        return list;
    }

    @Override
    public CreditRepayOrder selectById(String batchNo,String tallyOrderNo,int sta) {
        CreditRepayOrder cro=creditRepayOrderDao.selectById(batchNo);
        if(tallyOrderNo!=null&&!"".equals(tallyOrderNo)){
            CreditRepayOrder ct=creditRepayOrderDao.selectByIdAndTallyOrderNo(batchNo,tallyOrderNo);
            if(cro!=null&&ct!=null){
                cro.setBillingStatus(ct.getBillingStatus());
                cro.setTallyOrderNo(ct.getTallyOrderNo());
            }
        }
        if(1==sta){
            dataProcessingItem(cro);
        }
        return cro;
    }

	@Override
	public String selectStatusByBatchNo(String batchNo) {
		return creditRepayOrderDao.selectStatusByBatchNo(batchNo);
	}

    @Override
    public List<CreditRepayOrder> selectAbnormalByParam(CreditRepayOrder order) {
        return creditRepayOrderDao.selectAbnormalByParam(order);
    }

}
