package cn.eeepay.framework.service;


import cn.eeepay.framework.db.pagination.Page;

import java.util.List;
import java.util.Map;

public interface VerificationInfoService {

    List<Map<String,Object>> verificationInfoQuery(Map<String, String> params, Page<Map<String,Object>> page);

    Map<String,Object> verificationInfoCount(Map<String, String> params);

    List<Map<String,Object>> verificationAllInfoQuery(Map<String, String> params);
}
