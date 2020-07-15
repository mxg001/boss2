package cn.eeepay.framework.service;

import cn.eeepay.framework.model.ZxIndustryConfig;

import java.util.List;

/**
 * @author tans
 * @date 2018/9/1 15:28
 */
public interface ZxIndustryConfigService {
    List<ZxIndustryConfig> selectList(String activetiyCode);

    int update(ZxIndustryConfig config);

    int updateBatch(List<ZxIndustryConfig> list);
}
