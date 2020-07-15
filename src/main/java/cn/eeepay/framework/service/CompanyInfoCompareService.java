package cn.eeepay.framework.service;

import cn.eeepay.framework.model.CompanyInfoCompare;

import java.util.List;

public interface CompanyInfoCompareService {

    List<CompanyInfoCompare> getCompanyInfoCompareOne(String companyName,String licenseSocialCode,String legalName);
}
