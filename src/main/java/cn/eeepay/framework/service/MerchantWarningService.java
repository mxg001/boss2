package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantWarning;

import java.util.List;
import java.util.Map;


public interface MerchantWarningService {

    List<MerchantWarning> selectMerchantWarningPage(MerchantWarning merchantWarning, Page<MerchantWarning> page);

    MerchantWarning selectMerchantWarningDetail(Integer id);

    int deleteMerchantWarning(Integer id);

    Map<String, Object> updateIsUsedStatus(MerchantWarning merchantWarning);

    int insertMerWarning(MerchantWarning merchantWarning);

    int updateMerWarning(MerchantWarning merchantWarning);
}
