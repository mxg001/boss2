package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditcardSource;

import java.util.List;

public interface CreditcardSourceService {

    List<CreditcardSource> selectList(CreditcardSource baseInfo, Page<CreditcardSource> page);

    CreditcardSource selectDetail(Long id);

    int addBank(CreditcardSource info);

    int updateBank(CreditcardSource info);

    int updateBankStatus(CreditcardSource info);
    
    List<CreditcardSource> getAllBanks();
}
