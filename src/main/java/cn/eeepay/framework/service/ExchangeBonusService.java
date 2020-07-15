package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BonusConf;

import java.util.List;

public interface ExchangeBonusService {

    int updateBonus(BonusConf bonusConf);

    int  addBonus(BonusConf bonusConf);

    boolean checkOrgExist(long orgId);

    List<BonusConf> getBonus(BonusConf bonusConf, Page<BonusConf> page);

    List<BonusConf> getAllBonus(String type);

}
