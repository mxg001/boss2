package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SdbConfigDao;
import cn.eeepay.framework.service.SdbConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 */
@Service("sdbConfigService")
public class SdbConfigServiceImpl implements SdbConfigService {

    @Resource
    private SdbConfigDao sdbConfigDao;

    @Override
    public Map getSdbConfig(int team_id) {
        return sdbConfigDao.getSdbConfig(team_id);
    }

    @Override
    public int updateSdbConfigImg(int id,String team_ad_url) {
        return sdbConfigDao.updateSdbConfigImg(id,team_ad_url);
    }

    @Override
    public int addSdbConfig(int team_id, String team_ad_url) {
        return sdbConfigDao.addSdbConfig(team_id,team_ad_url);
    }

}
