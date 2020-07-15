package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.InsuranceCompanyDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceCompany;
import cn.eeepay.framework.service.InsuranceCompanyService;
import cn.eeepay.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insuranceCompanyService")
@Transactional
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService {

    @Resource
    private InsuranceCompanyDao insuranceCompanyDao;

    @Override
    public List<InsuranceCompany> selectList(InsuranceCompany baseInfo, Page<InsuranceCompany> page) {
        List<InsuranceCompany> insuranceCompanies = insuranceCompanyDao.selectList(baseInfo, page);
       for(InsuranceCompany insuranceCompany:insuranceCompanies){
           if(StringUtils.isNotBlank(insuranceCompany.getShowLogo())){
               insuranceCompany.setShowLogoUrl(CommonUtil.getImgUrlAgent(insuranceCompany.getShowLogo()));
           }
           insuranceCompany.setCreateOrderTypeStr(getCreateOrderType().get(insuranceCompany.getCreateOrderType()));
       }
        return insuranceCompanies;
    }

    @Override
    public List<InsuranceCompany> getCompanyList() {
        return insuranceCompanyDao.getCompanyList();
    }

    @Override
    public InsuranceCompany selectDetail(Long id) {
        InsuranceCompany insuranceCompany = insuranceCompanyDao.selectDetail(id);
        if (StringUtils.isNotBlank(insuranceCompany.getShowLogo())) {
            insuranceCompany.setShowLogoUrl(CommonUtil.getImgUrlAgent(insuranceCompany.getShowLogo()));
        }
        insuranceCompany.setCreateOrderTypeStr(getCreateOrderType().get(insuranceCompany.getCreateOrderType()));
        insuranceCompany.setStatusStr(getStatus().get(insuranceCompany.getStatus()));
        return insuranceCompany;
    }

    public Map<Integer, String> getCreateOrderType(){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "实际回调创建");
        map.put(2, "批量导入创建");
        return map;
    }

    public Map<Integer, String> getStatus(){
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "关闭");
        map.put(1, "开启");
        return map;
    }

    @Override
    public int addCompany(InsuranceCompany info) {
        info.setCreateBy(CommonUtil.getLoginUser().getUsername());
        info.setCreateDate(new Date());
        info.setStatus(0);
        return insuranceCompanyDao.insert(info);
    }

    @Override
    public int updateCompany(InsuranceCompany info) {
        info.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        info.setUpdateDate(new Date());
        return insuranceCompanyDao.updateCompany(info);
    }

    @Override
    public int updateCompanyStatus(InsuranceCompany info) {
        info.setUpdateBy(CommonUtil.getLoginUser().getUsername());
        info.setUpdateDate(new Date());
        return insuranceCompanyDao.updateCompanyStatus(info);
    }


    @Override
    public boolean selectNickExists(String companyNickName) {
        int i = insuranceCompanyDao.selectNickExists(companyNickName);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean selectNickIdExists(String companyNickName, Long companyNo) {
        int i = insuranceCompanyDao.selectNickIdExists(companyNickName, companyNo);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean selectOrderExists(Integer showOrder) {
        int i = insuranceCompanyDao.selectOrderExists(showOrder);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean selectOrderIdExists(Integer showOrder, Long productId) {
        int i = insuranceCompanyDao.selectOrderIdExists(showOrder,productId);
        if(i>0){
            return true;
        }
        return false;
    }
}
