package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanSource;

public interface LoanInstitutionManagementService {

    List<LoanSource> selectList(LoanSource baseInfo, Page<LoanSource> page);

    LoanSource selectDetail(Long id);

    int addLoan(LoanSource info);

    int updateLoan(LoanSource info);

    int updateLoanStatus(LoanSource info);
}
