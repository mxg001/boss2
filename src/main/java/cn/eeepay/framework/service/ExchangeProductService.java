package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeProduct;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 产品service
 */
public interface ExchangeProductService {

    List<ExchangeProduct> selectAllList(ExchangeProduct pro, Page<ExchangeProduct> page);

    List<ExchangeProduct> importDetailSelect(ExchangeProduct pro);

    int addExchangeProduct(ExchangeProduct pro);

    List<ExchangeProduct> getExchangeProductList(String typeCode);

    ExchangeProduct getExchangeProduct(long id);

    int updateExchangeProduct(ExchangeProduct pro);

    int deleteExchangeProduct(long id);

    boolean checkTypeName(ExchangeProduct pro);

    List<ExchangeProduct> getProductList(String name,String typeCode);

    List<ExchangeProduct> productListSelect(String val);

    void importDetail(List<ExchangeProduct> list, HttpServletResponse response) throws Exception;

}
