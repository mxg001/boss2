package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateProduct;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 产品service
 */
public interface ExchangeActivateProductService {

    List<ExchangeActivateProduct> selectAllList(ExchangeActivateProduct pro, Page<ExchangeActivateProduct> page);

    List<ExchangeActivateProduct> importDetailSelect(ExchangeActivateProduct pro);

    int addExchangeProduct(ExchangeActivateProduct pro);

    List<ExchangeActivateProduct> getExchangeProductList(String typeCode);

    ExchangeActivateProduct getExchangeProduct(long id);

    int updateExchangeProduct(ExchangeActivateProduct pro);

    int deleteExchangeProduct(long id);

    boolean checkTypeName(ExchangeActivateProduct pro);

    List<ExchangeActivateProduct> getProductList(String name, String typeCode);

    List<ExchangeActivateProduct> productListSelect(String val);

    void importDetail(List<ExchangeActivateProduct> list, HttpServletResponse response) throws Exception;
}
