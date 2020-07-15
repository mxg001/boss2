package cn.eeepay.framework.service;

import cn.eeepay.framework.model.AddCreaditcardLog;

import java.util.List;

/**
 * @author tans
 * @date 2019/9/20 14:30
 */
public interface AddCreaditcardLogService {
    List<AddCreaditcardLog> selectMerchantCreditcard(String merchantNo);

    AddCreaditcardLog selectMerchantCreditcardDetail(Long id);
}
