package cn.eeepay.framework.service;

import cn.eeepay.framework.model.AcqTerminalStore;

/**
 * @author tans
 * @date 2019/3/26 17:06
 */
public interface AcqTerminalStoreService {
    AcqTerminalStore selectBySn(String sn);
}
