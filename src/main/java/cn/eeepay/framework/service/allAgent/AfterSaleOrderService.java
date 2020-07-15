package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AfterSaleOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AfterSaleOrderService {

    List<AfterSaleOrder> queryAfterSaleOrderList(AfterSaleOrder info, Page<AfterSaleOrder> page);

    Map<String, Object> queryAfterSaleOrderCount(AfterSaleOrder info);

    int updateProcessAfterSaleOrder(AfterSaleOrder info);

    void exportAfterSaleOrder(AfterSaleOrder info, HttpServletResponse response)  throws Exception;
}
