package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.UserCouponDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserCoupon;
import cn.eeepay.framework.service.UserCouponService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Resource
    private UserCouponDao userCouponDao;

    private final Logger log = LoggerFactory.getLogger(UserCouponServiceImpl.class);

    @Override
    public List<Map<String, Object>> userCouponList(Map<String, String> params, Page<Map<String,Object>> page) {
        return userCouponDao.userCouponList(params,page);
    }

    @Override
    public Map<String, Object> userCpAcc(String merchantNo,String[] codeNo) {
        return userCouponDao.userCpAcc(merchantNo,codeNo);
    }

    @Override
    public List<Map<String, Object>> coupCode(String sysKey) {
        return userCouponDao.coupCode(sysKey);
    }

    @Override
    public List<Map<String, Object>> couponList(String orderNo,String type) {
        return userCouponDao.couponList(orderNo,type);
    }

    @Override
    public List<Map<String, Object>> verificationList(String merchantNo,Page<Map<String,Object>> page) {
        return userCouponDao.verificationList(merchantNo,page);
    }


}
