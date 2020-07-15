package cn.eeepay.framework.service.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单service
 */
public interface SafeOrderService {

    List<SafeOrder> selectAllList(SafeOrder order, Page<SafeOrder> page);

    SafeOrder getSafeOrderDetail(int id);

    OrderTotal selectSum(SafeOrder order, Page<SafeOrder> page);

    void retreatsSafe(String ids, Map<String, Object> msg);

    List<SafeOrder> importDetailSelect(SafeOrder order);

    void importDetail(List<SafeOrder> list, HttpServletResponse response) throws Exception;
}
