package cn.eeepay.framework.service;


import cn.eeepay.framework.db.pagination.Page;

import java.util.List;
import java.util.Map;

public interface UserCouponService {
    List<Map<String,Object>> userCouponList(Map<String,String> params, Page<Map<String,Object>> page);
    Map<String,Object> userCpAcc(String merchantNo,String[] codeNo);
    List<Map<String,Object>> coupCode(String sysKey);
    List<Map<String,Object>> couponList(String orderNo,String type);
    List<Map<String,Object>> verificationList(String merchantNo,Page<Map<String,Object>> page);


}
