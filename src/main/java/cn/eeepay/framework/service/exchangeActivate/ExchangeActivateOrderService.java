package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 */
public interface ExchangeActivateOrderService {

    List<ExchangeActivateOrder> selectAllList(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page);

    TotalAmount selectSum(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page);

    ExchangeActivateOrder getExchangeOrder(long id);

    List<ExchangeActivateOrder> importDetailSelect(ExchangeActivateOrder order);

    void importDetail(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception;

    List<ExchangeActivateOrder> selectAuditAll(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page);

    TotalAmount selectAuditSum(ExchangeActivateOrder order, Page<ExchangeActivateOrder> page);

    List<ExchangeActivateOrder> importAuditDetailSelect(ExchangeActivateOrder order);

    void importAuditDetail(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception;

    void importAuditDetailNoImg(List<ExchangeActivateOrder> list, HttpServletResponse response) throws Exception;

    ExchangeActivateOrder getAuditExchangeOrder(long id);

    int updateWriteOff(WriteOffHis writeOff);

    List<WriteOffHis> getWriteOffList(String orderNo);

    Map<String,Object> importDiscount(MultipartFile file) throws Exception ;

    int orderApiSelect(ExchangeActivateOrder order,Map<String, Object> msg);

    ExchangeActivateOrder getExchangeOrderLittle(long id);
}
