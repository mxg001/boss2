package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;

import java.util.List;
import java.util.Map;

public interface ActivityOrderInfoService {
     List<Map<String,Object>> actOrderInfoQuery(Map<String,String> params, Page<Map<String,Object>> page);
     Map<String,Object> actOrderInfoCount(Map<String,String> params);
     List<Map<String,Object>> actOrderInfoExport(Map<String,String> params);

     SysDict sysDict(String sysKey, String sysValue);

     Map<String,Object> actOrderInfo(String id);

     Map<String,Object> queryCouponInfo(String couponNo);

     int actOrderRemarkUpdate(Map<String, String> params);

     Map<String,Object> actOrderSettleInfoQuery(String payOrderNo);
}
