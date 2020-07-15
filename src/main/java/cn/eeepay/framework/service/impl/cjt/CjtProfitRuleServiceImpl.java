package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.cjt.CjtProfitRuleDao;
import cn.eeepay.framework.dao.cjt.CjtPushTemplateDao;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import cn.eeepay.framework.model.cjt.CjtPushTemplate;
import cn.eeepay.framework.service.cjt.CjtProfitRuleService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 超级推分润奖励规则 服务层实现
 * @author tans
 * @date 2019-05-24
 */
@Service
public class CjtProfitRuleServiceImpl implements CjtProfitRuleService {

    @Resource
    private CjtProfitRuleDao cjtProfitRuleDao;

    @Resource
    private CjtPushTemplateDao cjtPushTemplateDao;

    @Resource
    private SysDictDao sysDictDao;

    @Override
    public List<CjtProfitRule> selectProfitRuleByType(String profitType) {
        List<CjtProfitRule> list =  cjtProfitRuleDao.selectProfitRuleByType(profitType,"M");
        if(list != null && list.size() > 0) {
            for(CjtProfitRule item: list) {
                //如果是固定比例，则0.0004需要转换为0.04%，这样子
                if(CjtProfitRule.profitMode2.equals(item.getProfitMode())) {
                    String profit1 = item.getProfit1();
                    profit1 = setProfit(profit1, 1);
                    item.setProfit1(profit1);

                    String profit2 = item.getProfit2();
                    profit2 = setProfit(profit2, 1);
                    item.setProfit2(profit2);
                }
            }
        }
        return list;
    }

    /**
     * 修改超级推分润奖励规则
     * @param cjtProfitRule
     * @return
     */
    @Override
    public int updateProfitRule(CjtProfitRule cjtProfitRule) {
        setProfit(cjtProfitRule, 2);

        return cjtProfitRuleDao.updateProfit(cjtProfitRule);
    }

    @Override
    public CjtProfitRule selectFirstProfitRuleByType(String profitType,String userType) {
        return cjtProfitRuleDao.selectFirstProfitRuleByType(profitType,userType);
    }

    @Override
    public CjtPushTemplate selectFirstByType(String type) {
        return cjtPushTemplateDao.selectFirstByType(type);
    }

    @Override
    @Transactional
    public int updateConfig(Map<String, Object> param) {
        JSONObject jsonObject = new JSONObject(param);
        //是否关联鼓励金设置
        CjtProfitRule registerRule = jsonObject.getObject("registerRule",CjtProfitRule.class );
        //推荐奖励设置
        CjtProfitRule recommondRule = jsonObject.getObject("recommondRule",CjtProfitRule.class );
        //活动补贴设置
        CjtProfitRule activityRule = jsonObject.getObject("activityRule",CjtProfitRule.class );

        //代理商-推荐奖励设置
        CjtProfitRule recommondRuleAgent = jsonObject.getObject("recommondRuleAgent",CjtProfitRule.class );
        //代理商-活动补贴设置
        CjtProfitRule activityRuleAgent = jsonObject.getObject("activityRuleAgent",CjtProfitRule.class );
        //交易分润补贴归属公司直属代理设置
        CjtProfitRule fenAmountProfitAgent = jsonObject.getObject("fenAmountProfitAgentRule",CjtProfitRule.class );

        //提现服务ID
        String cjtTxServiceId = jsonObject.getString("cjtTxServiceId");
        //交易分润推送模板
        CjtPushTemplate transTemplate = jsonObject.getObject("transTemplate",CjtPushTemplate.class );
        //推荐奖励推送模板
        CjtPushTemplate recommendTemplate = jsonObject.getObject("recommendTemplate",CjtPushTemplate.class );
        //活动补贴模板-直推商户
        CjtPushTemplate activity1Template = jsonObject.getObject("activity1Template",CjtPushTemplate.class );
        //活动补贴模板-间推商户
        CjtPushTemplate activity2Template = jsonObject.getObject("activity2Template",CjtPushTemplate.class );

        if (activityRule != null && StringUtils.isNotEmpty(activityRule.getSuccessDay())
                                  && StringUtils.isNotEmpty(activityRule.getPosAmount()) ) {
            activityRule.setProfitOrgs(activityRule.getSuccessDay() + "," + activityRule.getPosAmount());
        }
        if (activityRuleAgent != null && StringUtils.isNotEmpty(activityRuleAgent.getSuccessDay())
                && StringUtils.isNotEmpty(activityRuleAgent.getPosAmount()) ) {
            activityRuleAgent.setProfitOrgs(activityRuleAgent.getSuccessDay() + "," + activityRuleAgent.getPosAmount());
        }
        cjtProfitRuleDao.updateProfit(registerRule);
        cjtProfitRuleDao.updateProfit(recommondRule);
        cjtProfitRuleDao.updateProfit(activityRule);
        cjtProfitRuleDao.updateProfit(recommondRuleAgent);
        cjtProfitRuleDao.updateProfit(activityRuleAgent);
        cjtProfitRuleDao.updateProfit(fenAmountProfitAgent);
        cjtPushTemplateDao.update(transTemplate);
        cjtPushTemplateDao.update(recommendTemplate);
        cjtPushTemplateDao.update(activity1Template);
        cjtPushTemplateDao.update(activity2Template);
        sysDictDao.updateValueByKey(cjtTxServiceId, CjtProfitRule.cjt_tx_service_id);
        return 1;
    }

    @Override
    public CjtProfitRule selectProfitRuleDetail(String profitRuleNo) {
        CjtProfitRule rule = cjtProfitRuleDao.selectProfitRuleDetail(profitRuleNo);
        setProfit(rule, 1);
        return rule;
    }

    /**
     *
     * @param rule
     * @param type type 1表示乘以100返回，2表示除以100返回
     */
    private void setProfit(CjtProfitRule rule, int type) {
        String profitMode = rule.getProfitMode();
        if (CjtProfitRule.profitMode2.equals(profitMode)) {
            String profit1 = rule.getProfit1();
            profit1 = setProfit(profit1, type);
            rule.setProfit1(profit1);

            String profit2 = rule.getProfit2();
            profit2 = setProfit(profit2, type);
            rule.setProfit2(profit2);
        }
    }

    /**
     *
     * @param profit1
     * @param type 1表示乘以100返回，2表示除以100返回
     * @return
     */
    private String setProfit(String profit1, Integer type) {
        if(StringUtils.isNotEmpty(profit1)) {
            BigDecimal oneHundred = new BigDecimal(100);
            BigDecimal profit1Dec;
            if(type == 1) {
                profit1Dec = new BigDecimal(profit1).multiply(oneHundred).setScale(3);
            } else {
                profit1Dec = new BigDecimal(profit1).divide(oneHundred).setScale(5);
            }
            profit1 = profit1Dec.toString();
        }
        return profit1;
    }
}
