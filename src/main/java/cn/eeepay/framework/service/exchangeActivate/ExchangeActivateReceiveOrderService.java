package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateReceiveOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7/007.
 * @author  liuks
 * 商户收款service
 */
public interface ExchangeActivateReceiveOrderService {

    List<ExchangeActivateReceiveOrder> selectAllList(ExchangeActivateReceiveOrder order, Page<ExchangeActivateReceiveOrder> page);

    TotalAmount selectSum(ExchangeActivateReceiveOrder order, Page<ExchangeActivateReceiveOrder> page);

    ExchangeActivateReceiveOrder getReceiveOrder(long id);

    List<ExchangeActivateReceiveOrder> importDetailSelect(ExchangeActivateReceiveOrder order);

    void importDetail(List<ExchangeActivateReceiveOrder> list, HttpServletResponse response) throws Exception;
}
