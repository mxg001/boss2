package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import cn.eeepay.framework.model.cjt.CjtPushTemplate;

import java.util.List;
import java.util.Map;

/**
 * 超级推分润奖励规则 服务层
 *
 * @author tans
 * @date 2019-05-24
 */
public interface CjtProfitRuleService {

    /**
     * 查询分润规则
     * @param posTrade
     * @return
     */
    List<CjtProfitRule> selectProfitRuleByType(String posTrade);

    /**
     * 修改分润规则
     * @param cjtProfitRule
     * @return
     */
    int updateProfitRule(CjtProfitRule cjtProfitRule);

    CjtProfitRule selectFirstProfitRuleByType(String profitType,String userType);

    CjtPushTemplate selectFirstByType(String type_trans);

    int updateConfig(Map<String, Object> param);

    CjtProfitRule selectProfitRuleDetail(String profitRuleNo);
}
