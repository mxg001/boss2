package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.OrderEventDao;
import cn.eeepay.framework.model.OrderEventInfo;
import cn.eeepay.framework.service.OrderEventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("orderEventService")
public class OrderEventServiceImpl implements OrderEventService {

    @Resource
    private OrderEventDao orderEventDao;


    @Override
    public List<OrderEventInfo> getOrderEventListJY(String orderNo) {
        List<OrderEventInfo> list= orderEventDao.getOrderEventListJY(orderNo,"1");
        return list;
    }

    @Override
    public List<OrderEventInfo> getOrderEventListDF(String orderNo) {
        List<OrderEventInfo> list= orderEventDao.getOrderEventListDF(orderNo,"2");
        return list;
    }
}
