package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceBonusConf;
import cn.eeepay.framework.model.InsuranceProduct;

import java.util.List;

public interface InsuranceBonusService {

    int updateInsuranceBonus(InsuranceBonusConf insuranceBonusConf);

    int  addInsuranceBonus(InsuranceBonusConf insuranceBonusConf);

    boolean checkProductIdExist(int productId);

    List<InsuranceBonusConf> getInsuranceBonus(InsuranceBonusConf insuranceBonusConf, Page<InsuranceBonusConf> page);

    List<InsuranceProduct> getByCompanyNo(int companyNo);
}
