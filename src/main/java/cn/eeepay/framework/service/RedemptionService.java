package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExchangeAwardsConfig;
import cn.eeepay.framework.model.ExchangeAwardsRecode;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RedemptionService {

    List<ExchangeAwardsRecode> queryRedemptionList(ExchangeAwardsRecode info, Page<ExchangeAwardsRecode> page);

    List<ExchangeAwardsConfig> queryRedemptionManageList(ExchangeAwardsConfig info);

    int addRedemptionManage(ExchangeAwardsConfig info);

    int updateRedemptionManage(ExchangeAwardsConfig info);

    int deleteRedemptionManage(Integer id);

    int addRedemption(ExchangeAwardsRecode info);

    ExchangeAwardsConfig queryRedemptionManageById(Integer id);

    int updateRedemptionStatus(Integer id);

    void exportRedemption(ExchangeAwardsRecode info, HttpServletResponse response)  throws Exception;

    void updateRedemptionExpired();
}
