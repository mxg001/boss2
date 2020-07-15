package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.BossSysConfigDao;
import cn.eeepay.framework.service.BossSysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2019/6/20 16:50
 */
@Service
public class BossSysConfigServiceImpl implements BossSysConfigService{
    @Resource
    private BossSysConfigDao bossSysConfigDao;

    @Override
    public String selectValueByKey(String key) {
        if(StringUtils.isEmpty(key)) {
            return null;
        }
        return bossSysConfigDao.selectValueByKey(key);
    }

    @Override
    public int updateValueByKey(String key, String value) {
        return bossSysConfigDao.updateValueByKey(key, value);
    }
}
