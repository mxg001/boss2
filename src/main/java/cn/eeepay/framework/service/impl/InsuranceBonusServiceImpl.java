package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.InsuranceBonusDao;
import cn.eeepay.framework.daoSuperbank.InsuranceProductDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceBonusConf;
import cn.eeepay.framework.model.InsuranceProduct;
import cn.eeepay.framework.service.InsuranceBonusService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insuranceBonusService")
@Transactional
public class InsuranceBonusServiceImpl implements InsuranceBonusService {

    @Resource
    private InsuranceBonusDao insuranceBonusDao;

    @Resource
    private InsuranceProductDao insuranceProductDao;

    @Override
    public int updateInsuranceBonus(InsuranceBonusConf insuranceBonusConf) {
        insuranceBonusConf.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        insuranceBonusConf.setUpdateDate(new Date());
        return insuranceBonusDao.updInsuranceBonusConf(insuranceBonusConf);
    }

    @Override
    public int addInsuranceBonus(InsuranceBonusConf insuranceBonusConf) {
        insuranceBonusConf.setCreateBy(CommonUtil.getLoginUser().getUsername());
        insuranceBonusConf.setCreateDate(new Date());
        return insuranceBonusDao.addInsuranceBonusConf(insuranceBonusConf);
    }

    @Override
    public boolean checkProductIdExist(int productId) {
        int i = insuranceBonusDao.selectByProductId(productId);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public List<InsuranceBonusConf> getInsuranceBonus(InsuranceBonusConf insuranceBonusConf, Page<InsuranceBonusConf> page) {
        List<InsuranceBonusConf> insuranceBonusConfs = insuranceBonusDao.selectList(insuranceBonusConf, page);
        for(InsuranceBonusConf record :insuranceBonusConfs){
            fiterModel(record);
        }
        return insuranceBonusConfs;
    }

    @Override
    public List<InsuranceProduct> getByCompanyNo(int companyNo) {
        return insuranceProductDao.getByCompanyNo(companyNo);
    }

    void fiterModel(InsuranceBonusConf record){
        record.setStatusStr(getStatus().get(record.getStatus()));
        if(StringUtil.isBlank(record.getOrgName())){
            record.setOrgName("默认");
        }
    }


    public Map<Integer, String> getStatus(){
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "购买成功");
        return map;
    }

}
