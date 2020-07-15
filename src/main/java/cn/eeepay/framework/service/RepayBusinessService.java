package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayOemServiceCostBean;

import java.util.List;

/**
 * Created by 666666 on 2017/11/4.
 */
public interface RepayBusinessService {

    /**
     * 分页查询oem服务商jiaoyi
     * @param page
     * @return
     */
    List<RepayOemServiceCostBean> listRepayBusiness(Page<RepayOemServiceCostBean> page, RepayOemServiceCostBean info);

    /**
     * 修改服务费率
     * @param bean
     * @return
     */
    Object updateRepayBusiness(RepayOemServiceCostBean bean);

    /**
     * 查询服务商成本
     * @author	mays
     * @date	2018年1月24日
     */
	RepayOemServiceCostBean queryRepayServiceCost(String agentNo);
}
