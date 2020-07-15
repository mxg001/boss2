package cn.eeepay.framework.service;

/**
 * @author tans
 * @date 2019/6/20 16:50
 */
public interface BossSysConfigService {
    String selectValueByKey(String key);

    int updateValueByKey(String key, String value);
}
