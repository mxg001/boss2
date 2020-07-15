package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.BonusConfDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BonusConf;
import cn.eeepay.framework.service.ExchangeBonusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("exchangeBonusService")
public class ExchangeBonusServiceImpl implements ExchangeBonusService {

    @Resource
    private BonusConfDao bonusConfDao;
    @Override
    public int updateBonus(BonusConf bonusConf) {
        return bonusConfDao.updBonusConf(bonusConf);
    }

    @Override
    public int addBonus(BonusConf bonusConf) {
        return bonusConfDao.addBonusConf(bonusConf);
    }

    @Override
    public boolean checkOrgExist(long orgId) {
        return bonusConfDao.checkOrgExist(orgId)>0?true:false;
    }

    @Override
    public List<BonusConf> getBonus(BonusConf bonusConf, Page<BonusConf> page) {
        List<BonusConf> bonusConfs = bonusConfDao.selectList(bonusConf, page);
        for(BonusConf record:bonusConfs){
            if(record.getOrgId()==0){
                record.setOrgName("默认");
            }
        }
        return bonusConfs;
    }

    @Override
    public List<BonusConf> getAllBonus(String type) {
        return bonusConfDao.getByType(type);
    }
}
