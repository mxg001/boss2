package cn.eeepay.framework.service.impl.cjt;


import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.SettleOrderInfoDao;
import cn.eeepay.framework.dao.cjt.CjtMerchantInfoDao;
import cn.eeepay.framework.db.pagination.Page;

import cn.eeepay.framework.model.AccountInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushUser;
import cn.eeepay.framework.model.cjt.CjtMerchantInfo;
import cn.eeepay.framework.service.cjt.CjtMerchantInfoService;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 新版超级推商户 服务层实现
 * @author tans
 * @date 2019-06-14
 */
@Service
public class CjtMerchantInfoServiceImpl implements CjtMerchantInfoService {

    private static final Logger log = LoggerFactory.getLogger(CjtMerchantInfoServiceImpl.class);

    @Resource
    private CjtMerchantInfoDao cjtMerchantInfoDao;

    @Resource
    private SettleOrderInfoDao settleOrderInfoDao;

    @Resource
    private MerchantInfoDao merchantInfoDao;

    /**
     * 条件查询新版超级推商户
     * @param baseInfo
     * @return
     */
    @Override
    public void selectPage(Page<CjtMerchantInfo> page, CjtMerchantInfo baseInfo) {
        cjtMerchantInfoDao.selectPage(page, baseInfo);
        if(page != null) {
            List<CjtMerchantInfo> resultList = page.getResult();
            if(resultList != null && resultList.size() > 0) {
                for(CjtMerchantInfo item: resultList) {
                    if(StringUtils.isNotEmpty(item.getMerchantNo())){
                        MerchantInfo merchantInfo = merchantInfoDao.selectByMerNo(item.getMerchantNo());
                        item.setMerchantName(merchantInfo != null ? merchantInfo.getMerchantName() : "");

                        BigDecimal totalAmount = cjtMerchantInfoDao.selectTotalAmount(item.getMerchantNo());
                        item.setProfitAmount(totalAmount);
                    }
                }
            }
        }
        return;
    }

    @Override
    public Map<String, Object> selectTotal(CjtMerchantInfo baseInfo) {
        return cjtMerchantInfoDao.selectTotal(baseInfo);
    }

    @Override
    public CjtMerchantInfo selectDetail(String merchantNo) {
        return cjtMerchantInfoDao.selectDetail(merchantNo);
    }

    @Override
    public CjtMerchantInfo selectAccountDetail(String merchantNo) {
        //基本信息
        CjtMerchantInfo baseInfo = cjtMerchantInfoDao.selectSmallDetail(merchantNo);
        //收益总额
        BigDecimal totalAmount = cjtMerchantInfoDao.selectTotalAmount(merchantNo);
        //可用余额
        BigDecimal avaliBalance = null;
        try {
            String str = ClientInterface.getSuperPushUserBalance(merchantNo);
            if(StringUtils.isNotEmpty(str)) {
                JSONObject json = JSON.parseObject(str);
                if ((boolean) json.get("status")) {// 返回成功
                    AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
                    avaliBalance = ainfo.getAvaliBalance();
                }
            }
        } catch (Exception e) {
            log.error("查询商户超级推余额失败", e);
        }
        baseInfo.setProfitAmount(totalAmount == null ? BigDecimal.ZERO : totalAmount);
        baseInfo.setAvaliBalance(avaliBalance == null ? BigDecimal.ZERO : avaliBalance);
        return baseInfo;
    }

    @Override
    public void selectCashPage(SettleOrderInfo settleOrderInfo, Page<SettleOrderInfo> page) {
        settleOrderInfoDao.getCashPage(settleOrderInfo, page);
    }
}
