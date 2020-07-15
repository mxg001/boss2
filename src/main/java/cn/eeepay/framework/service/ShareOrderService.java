package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ShareOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17/017.
 */
public interface ShareOrderService {

    List<ShareOrder> getOrderShare(String shareType, String orderId);

    List<ShareOrder> selectAllList(ShareOrder order, Page<ShareOrder> page);

    List<ShareOrder> importDetailSelect(ShareOrder order);

    void importDetail(List<ShareOrder> list, HttpServletResponse response) throws Exception;

    TotalAmount selectSum(ShareOrder order, Page<ShareOrder> page);
}
