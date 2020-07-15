package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.ZxProductOrder;
import cn.eeepay.framework.rpc.entity.ZxApplyProductsEntity;

import java.util.List;

public interface ZxProductOrderService {

   List<ZxProductOrder> selectByPage(ZxProductOrder record, Page<ZxProductOrder> page);

   ZxProductOrder selectByOrderNo(String orderNo);

   OrderMainSum selectOrderSum(ZxProductOrder record);

   List <ZxApplyProductsEntity> selectProductAll();
}
