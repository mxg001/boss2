package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AcqTerminalStoreDao;
import cn.eeepay.framework.model.AcqTerminalStore;
import cn.eeepay.framework.service.AcqTerminalStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2019/3/26 17:07
 */
@Service("acqTerminalStoreService")
public class AcqTerminalStoreServiceImpl implements AcqTerminalStoreService {

    @Resource
    private AcqTerminalStoreDao acqTerminalStoreDao;

    @Override
    public AcqTerminalStore selectBySn(String sn) {
        return acqTerminalStoreDao.selectBySn(sn);
    }
}
