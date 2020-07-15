package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 *  分润明细
 */
public interface ExchangeActivateShareService {

    List<ExchangeActivateShare> getOrderShare(String shareType, String orderId);

    List<ExchangeActivateShare> selectAllList(ExchangeActivateShare order, Page<ExchangeActivateShare> page);

    List<ExchangeActivateShare> importDetailSelect(ExchangeActivateShare order);

    void importDetail(List<ExchangeActivateShare> list, HttpServletResponse response) throws Exception;

    TotalAmount selectSum(ExchangeActivateShare order, Page<ExchangeActivateShare> page);
}
