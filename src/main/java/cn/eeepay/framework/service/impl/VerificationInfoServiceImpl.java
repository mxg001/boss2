package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.VerificationInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.VerificationInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class VerificationInfoServiceImpl implements VerificationInfoService {

    @Resource
    private VerificationInfoDao verificationInfoDao;

    @Override
    public List<Map<String,Object>> verificationInfoQuery(Map<String, String> params, Page<Map<String, Object>> page) {
        return  verificationInfoDao.verificationInfoQuery(params,page);
    }

    @Override
    public Map<String, Object> verificationInfoCount(Map<String, String> params) {
        return verificationInfoDao.verificationInfoCount(params);
    }

    @Override
    public List<Map<String, Object>> verificationAllInfoQuery(Map<String, String> params) {
        return verificationInfoDao.verificationAllInfoQuery(params);
    }
}
