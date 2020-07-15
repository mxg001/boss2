package cn.eeepay.framework.service;

import cn.eeepay.framework.model.MerchantRequireHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public interface MerchantRequireHistoryService {

    List<MerchantRequireHistory> getMerchantRequireHistoryByMriId(String merchantNo, String mriId);

    int insertBankCard(MerchantRequireHistory mer);

}
