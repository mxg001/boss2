package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ZxIndustryConfigDao;
import cn.eeepay.framework.model.ZxIndustryConfig;
import cn.eeepay.framework.service.ZxIndustryConfigService;
import cn.eeepay.framework.util.BossBaseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tans
 * @date 2018/9/1 15:28
 */
@Service("zxIndustryConfigService")
public class ZxIndustryConfigServiceImpl implements ZxIndustryConfigService {

    @Resource
    private ZxIndustryConfigDao zxIndustryConfigDao;

    @Override
    public List<ZxIndustryConfig> selectList(String activetiyCode) {
        return zxIndustryConfigDao.selectList(activetiyCode);
    }

    @Override
    public int update(ZxIndustryConfig config) {
        return zxIndustryConfigDao.update(config);
    }

    @Override
    public int updateBatch(List<ZxIndustryConfig> list) {
        int num = 0;
        if(list != null && list.size() > 0){
            for(ZxIndustryConfig config: list){
                num += zxIndustryConfigDao.update(config);
            }
        }
        return num;
    }
}
