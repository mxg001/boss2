package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;

import java.util.List;

public interface SuperExcOrderService {

    List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page);

    OrderMainSum selectOrderSum(OrderMain baseInfo);

    OrderMain selectExcOrderDetail(String orderNo);

    String selectByOrgId(long orgId);
}
