package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivityOrderInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ActivityOrderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityOrderInfoServiceImpl implements ActivityOrderInfoService {

    @Resource
    private ActivityOrderInfoDao activityOrderInfoDao;

    @Override
    public List<Map<String, Object>> actOrderInfoQuery(Map<String, String> params, Page<Map<String, Object>> page) {
        return activityOrderInfoDao.actOrderInfoQuery(params,page);
    }

    @Override
    public Map<String, Object> actOrderInfoCount(Map<String, String> params) {
        return activityOrderInfoDao.actOrderInfoCount(params);
    }

    @Override
    public List<Map<String, Object>> actOrderInfoExport(Map<String,String> params) {
        return activityOrderInfoDao.actOrderInfoExport(params);
    }

    @Override
    public SysDict sysDict(String sysKey, String sysValue) {
        return activityOrderInfoDao.sysDict(sysKey,sysValue);
    }

    @Override
    public Map<String, Object> actOrderInfo(String id) {
        return activityOrderInfoDao.actOrderInfo(id);
    }

    @Override
    public Map<String, Object> queryCouponInfo(String couponNo) {
        return activityOrderInfoDao.queryCouponInfo(couponNo);
    }

    @Override
    public int actOrderRemarkUpdate(Map<String, String> params) {
        return activityOrderInfoDao.actOrderRemarkUpdate(params);
    }

    @Override
    public Map<String, Object> actOrderSettleInfoQuery(String payOrderNo) {
        return activityOrderInfoDao.actOrderSettleInfoQuery(payOrderNo);
    }
}
