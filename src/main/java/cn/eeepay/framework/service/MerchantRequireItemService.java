package cn.eeepay.framework.service;


import cn.eeepay.framework.model.IndustryMcc;
import cn.eeepay.framework.model.MerchantRequireItem;

import java.util.List;

public interface MerchantRequireItemService {

    public MerchantRequireItem selectByMriId(String mriId, String merId);

    public int updateBymriId(Long id, String status);

    public int updateByMbpId(MerchantRequireItem record);

    MerchantRequireItem selectByAccountNo(String merId);

    MerchantRequireItem selectByIdCardNo(String merId);

    public List<MerchantRequireItem> getByMer(String merchantNo);

    public List<MerchantRequireItem> getItemByMerId(String merchantNo);

    public List<MerchantRequireItem> selectItemByBpIdAndMerNo(String merchantNo,String bpId);

    public IndustryMcc selectIndustryMccByMcc(String mcc);

    public List<IndustryMcc> selectIndustryMccByLevel(String industryLevel);

    public List<IndustryMcc> selectIndustryMccByParentId(String parentId);

    public int updateMccById(String id,String mcc);

    public int updateMccByMerNo(String merNo,String mcc);

    List<MerchantRequireItem> getMerchantRequireItemList(String merchantNo,String bpId);
}
