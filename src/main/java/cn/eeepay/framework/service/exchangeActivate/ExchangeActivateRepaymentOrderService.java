package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateRepaymentOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7/007.
 * @author  liuks
 * 商户收款service
 */
public interface ExchangeActivateRepaymentOrderService {

    List<ExchangeActivateRepaymentOrder> selectAllList(ExchangeActivateRepaymentOrder order, Page<ExchangeActivateRepaymentOrder> page);

    TotalAmount selectSum(ExchangeActivateRepaymentOrder order, Page<ExchangeActivateRepaymentOrder> page);

    ExchangeActivateRepaymentOrder getRepaymentOrder(long id);

    List<ExchangeActivateRepaymentOrder> importDetailSelect(ExchangeActivateRepaymentOrder order);

    void importDetail(List<ExchangeActivateRepaymentOrder> list, HttpServletResponse response) throws Exception;
}
