package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.AgentOrder;
import cn.eeepay.framework.model.exchange.AgentShare;
import cn.eeepay.framework.model.exchange.TotalAmount;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17/017.
 */
public interface AgentOrderService {

    List<AgentOrder> selectAllList(AgentOrder order, Page<AgentOrder> page);

    AgentOrder getAgentOrder(long id);

    List<AgentOrder> importDetailSelect(AgentOrder order);

    void importDetail(List<AgentOrder> list, HttpServletResponse response) throws Exception;

    TotalAmount selectSum(AgentOrder order, Page<AgentOrder> page);

    List<AgentShare> getOrderShare(String orderNo);
}
