package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceCompany;

import java.util.List;

public interface InsuranceCompanyService {

    List<InsuranceCompany> selectList(InsuranceCompany baseInfo, Page<InsuranceCompany> page);

    List<InsuranceCompany> getCompanyList();

    InsuranceCompany selectDetail(Long id);

    int addCompany(InsuranceCompany info);

    int updateCompany(InsuranceCompany info);

    int updateCompanyStatus(InsuranceCompany info);

    boolean selectNickExists( String companyNickName);

    boolean selectNickIdExists( String companyNickName,Long companyNo);

    boolean selectOrderExists(Integer showOrder);

    boolean selectOrderIdExists(Integer showOrder,Long productId);


}
