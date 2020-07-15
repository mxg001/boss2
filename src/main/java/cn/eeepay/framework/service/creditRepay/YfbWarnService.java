package cn.eeepay.framework.service.creditRepay;

import cn.eeepay.framework.model.creditRepay.YfbWarnInfo;

/**
 * @author MXG
 * create 2018/10/22
 */
public interface YfbWarnService {

    YfbWarnInfo selectWarnInfoByCode(String code);

    void update(YfbWarnInfo info);
}
