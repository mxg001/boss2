package cn.eeepay.framework.service;

import cn.eeepay.framework.model.OrderEventInfo;

import java.util.List;

public interface OrderEventService {

    List<OrderEventInfo> getOrderEventListJY(String orderNo);
    List<OrderEventInfo> getOrderEventListDF(String orderNo);

}
