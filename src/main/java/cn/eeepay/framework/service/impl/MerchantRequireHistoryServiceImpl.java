package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantRequireHistoryDao;
import cn.eeepay.framework.model.MerchantRequireHistory;
import cn.eeepay.framework.service.MerchantRequireHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6/006.
 */
@Service("merchantRequireHistoryService")
public class MerchantRequireHistoryServiceImpl implements MerchantRequireHistoryService {

    @Resource
    private MerchantRequireHistoryDao  merchantRequireHistoryDao;
    @Override
    public List<MerchantRequireHistory> getMerchantRequireHistoryByMriId(String merchantNo, String mriId) {
        return merchantRequireHistoryDao.getMerchantRequireHistoryByMriId(merchantNo,mriId);
    }

    @Override
    public int insertBankCard(MerchantRequireHistory mer) {
        return merchantRequireHistoryDao.insertBankCard(mer);
    }
}
