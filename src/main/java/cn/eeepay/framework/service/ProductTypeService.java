package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ProductType;
import cn.eeepay.framework.model.exchange.PropertyConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 超级兑产品类别service
 */
public interface ProductTypeService {

    List<ProductType> selectAllList(ProductType pt, Page<ProductType> page);

    List<ProductType> importDetailSelect(ProductType pt);

    int addProductType(ProductType pt,List<PropertyConfig> list);

    int deleteProductType(long id);

    int deleteProductTypeBatch(String ids,Map<String, Object> msg);

    boolean checkProduct(long id);

    int editProductType(ProductType pt,List<PropertyConfig> list,List<PropertyConfig> fileList);

    ProductType getProductType(long id);

    List<ProductType> getProductTypeList(String orgCode);

    boolean checkTypeName(ProductType pt);

    ProductType getProductOne(String typeCode);

    boolean  checkPrepaidCard(String typeCode);

    Map<String,String> getAddProductType(String typeCode);

    void importDetail(List<ProductType> list, HttpServletResponse response) throws Exception;
}
