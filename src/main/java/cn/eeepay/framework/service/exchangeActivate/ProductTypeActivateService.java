package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ProductTypeActivate;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 超级兑产品类别service
 */
public interface ProductTypeActivateService {

    List<ProductTypeActivate> selectAllList(ProductTypeActivate pt, Page<ProductTypeActivate> page);

    List<ProductTypeActivate> importDetailSelect(ProductTypeActivate pt);

    int addProductType(ProductTypeActivate pt, List<PropertyConfigActivate> list);

    int deleteProductType(long id);

    int deleteProductTypeBatch(String ids, Map<String, Object> msg);

    boolean checkProduct(long id);

    int editProductType(ProductTypeActivate pt, List<PropertyConfigActivate> list, List<PropertyConfigActivate> fileList);

    ProductTypeActivate getProductType(long id);

    List<ProductTypeActivate> getProductTypeList(String orgCode);

    boolean checkTypeName(ProductTypeActivate pt);

    ProductTypeActivate getProductOne(String typeCode);

    Map<String,String> getAddProductType(String typeCode);

    void importDetail(List<ProductTypeActivate> list, HttpServletResponse response) throws Exception;
}
