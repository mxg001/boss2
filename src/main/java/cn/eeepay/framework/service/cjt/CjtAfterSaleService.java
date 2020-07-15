package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtAfterSale;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 售后订单 服务层
 *
 * @author tans
 * @date 2019-06-06
 */
public interface CjtAfterSaleService {

    /**
     * 条件查询售后订单
     * @param cjtAfterSale
     * @return
     */
    void selectPage(Page<CjtAfterSale> page, CjtAfterSale cjtAfterSale);

    void export(HttpServletResponse response, CjtAfterSale cjtAfterSale);

    Map<String,Object> selectTotal(CjtAfterSale baseInfo);

    int updateStatus(String orderNo, String status);

    int deal(CjtAfterSale baseInfo);

    void checkStatus(String orderNo);
}
