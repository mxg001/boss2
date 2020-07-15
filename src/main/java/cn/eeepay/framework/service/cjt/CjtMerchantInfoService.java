package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushUser;
import cn.eeepay.framework.model.cjt.CjtMerchantInfo;

import java.util.Map;

/**
 * 新版超级推商户 服务层
 *
 * @author tans
 * @date 2019-06-14
 */
public interface CjtMerchantInfoService {

    /**
     * 条件查询新版超级推商户
     * @param cjtMerchantInfo
     * @return
     */
    void selectPage(Page<CjtMerchantInfo> page, CjtMerchantInfo cjtMerchantInfo);

    Map<String,Object> selectTotal(CjtMerchantInfo baseInfo);

    CjtMerchantInfo selectDetail(String merchantNo);

    CjtMerchantInfo selectAccountDetail(String merchantNo);

    void selectCashPage(SettleOrderInfo settleOrderInfo, Page<SettleOrderInfo> page);
}
