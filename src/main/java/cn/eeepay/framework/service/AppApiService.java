package cn.eeepay.framework.service;

import cn.eeepay.framework.model.MobileVerInfo;

public interface AppApiService {

    /**
     * 根据当前版本获取需要更新的版本信息
     * @param currentVer
     * @return
     */
    MobileVerInfo getVersion(MobileVerInfo currentVer);
}
