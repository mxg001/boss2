package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceProduct;

import java.util.List;

public interface InsuranceProductService {

    List<InsuranceProduct> selectList(InsuranceProduct baseInfo, Page<InsuranceProduct> page);

    InsuranceProduct selectDetail(Long id);

    int addProduct(InsuranceProduct info);

    int updateProduct(InsuranceProduct info);

    int updateProductStatus(InsuranceProduct info);

    boolean selectOrderExists(Integer showOrder);

    boolean selectOrderIdExists(Integer showOrder,Long productId);

    boolean selectProductIdExists(String uppProductId);

    boolean selectProductIdExists(String uppProductId,Long productId);

    boolean selectProductNameExists(String productName);

    boolean selectProductNameIdExists(String productName,Long productId);

    List<InsuranceProduct> getListAll();

    int deleteProduct(InsuranceProduct productId);
}
