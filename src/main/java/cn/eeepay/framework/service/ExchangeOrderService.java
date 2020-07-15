package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 */
public interface ExchangeOrderService {

    List<ExchangeOrder> selectAllList(ExchangeOrder order, Page<ExchangeOrder> page);

    TotalAmount selectSum(ExchangeOrder order, Page<ExchangeOrder> page);

    ExchangeOrder getExchangeOrder(long id);

    List<ExchangeOrder> importDetailSelect(ExchangeOrder order);

    void importDetail(List<ExchangeOrder> list, HttpServletResponse response) throws Exception;

    List<ExchangeOrder> selectAuditAll(ExchangeOrder order, Page<ExchangeOrder> page);

    TotalAmount selectAuditSum(ExchangeOrder order, Page<ExchangeOrder> page);

    List<ExchangeOrder> importAuditDetailSelect(ExchangeOrder order);

    void importAuditDetail(List<ExchangeOrder> list, HttpServletResponse response) throws Exception;

    void importAuditDetailNoImg(List<ExchangeOrder> list, HttpServletResponse response) throws Exception;

    ExchangeOrder getAuditExchangeOrder(long id);

    int updateWriteOff(WriteOffHis writeOff);

    int addExchangeOrder(ExchangeOrder order, Map<String,Object> msg);

    ExchangeOrder getExchangeOrerEdit(long id);

    int saveExchangeOrder(ExchangeOrder order, Map<String, Object> msg);

    List<WriteOffHis> getWriteOffList(String orderNo);

    Map<String,Object> importDiscount(MultipartFile file) throws Exception ;

    ExchangeOrder getExchangeOrderLittle(long id);

    int orderApiSelect(ExchangeOrder order, Map<String, Object> msg);
}
