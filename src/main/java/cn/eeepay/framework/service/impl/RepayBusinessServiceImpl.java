package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RepayBusinessDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayOemServiceCostBean;
import cn.eeepay.framework.service.RepayBusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 666666 on 2017/11/4.
 */
@Service
public class RepayBusinessServiceImpl implements RepayBusinessService {

    @Resource
    private RepayBusinessDao repayBusinessDao;

    @Override
    public List<RepayOemServiceCostBean> listRepayBusiness(Page<RepayOemServiceCostBean> page, RepayOemServiceCostBean info) {
        return repayBusinessDao.listRepayBusiness(page, info);
    }

    @Override
    public Object updateRepayBusiness(RepayOemServiceCostBean bean) {
        return repayBusinessDao.updateRepayBusiness(bean) == 1;
    }

	@Override
	public RepayOemServiceCostBean queryRepayServiceCost(String agentNo) {
		return repayBusinessDao.queryRepayServiceCost(agentNo);
	}
}
