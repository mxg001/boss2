package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PosCnapsDao;
import cn.eeepay.framework.model.PosCnaps;
import cn.eeepay.framework.service.PosCnapsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("posCnapsServiceImpl")
public class PosCnapsServiceImpl implements PosCnapsService {
    @Resource
    private PosCnapsDao posCnapsDao;

    @Override
    public List<PosCnaps> query(String bankName, String cityName) {
        return posCnapsDao.query(bankName, cityName);
    }
}
