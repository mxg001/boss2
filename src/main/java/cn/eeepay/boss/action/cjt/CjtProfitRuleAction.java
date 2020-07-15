package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import cn.eeepay.framework.model.cjt.CjtPushTemplate;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.cjt.CjtProfitRuleService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级推分润奖励规则
 * @author tans
 * @date 2019-05-24
 */
@RestController
@RequestMapping("/cjtProfitRule")
public class CjtProfitRuleAction {

    private static final Logger log = LoggerFactory.getLogger(CjtProfitRuleAction.class);

    @Resource
    private CjtProfitRuleService cjtProfitRuleService;

    @Resource
    private SysDictService sysDictService;

    /**
     * 查询超级推分润奖励规则
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectProfitRule")
    public Result selectProfitRule(){
        Result result = new Result();
        try {
            //刷卡交易分润规则
            List<CjtProfitRule> posProfitRuleList = cjtProfitRuleService.selectProfitRuleByType(CjtProfitRule.posTrade);
            //无卡交易分润规则
            List<CjtProfitRule> noCardProfitRuleList = cjtProfitRuleService.selectProfitRuleByType(CjtProfitRule.noCardTrade);
            //是否关联鼓励金设置
            CjtProfitRule registerRule = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.register,"M");
            //推荐奖励设置
            CjtProfitRule recommondRule = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.recommend,"M");
            //活动补贴设置
            CjtProfitRule activityRule = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.activity,"M");
            //交易分润补贴归属公司直属代理设置
            CjtProfitRule fenAmountProfitAgentRule = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.fenAmountProfitAgent,"M");

            if (activityRule != null && StringUtils.isNotEmpty(activityRule.getProfitOrgs())) {
                String[] strArr = activityRule.getProfitOrgs().split(",");
                if(strArr.length == 2) {
                    activityRule.setSuccessDay(strArr[0]);
                    activityRule.setPosAmount(strArr[1]);
                }
            }
            //代理商-推荐奖励设置
            CjtProfitRule recommondRuleAgent = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.recommend,"A");
            //代理商-活动补贴设置
            CjtProfitRule activityRuleAgent = cjtProfitRuleService.selectFirstProfitRuleByType(CjtProfitRule.activity,"A");
            if (activityRuleAgent != null && StringUtils.isNotEmpty(activityRuleAgent.getProfitOrgs())) {
                String[] strArr = activityRuleAgent.getProfitOrgs().split(",");
                if(strArr.length == 2) {
                    activityRuleAgent.setSuccessDay(strArr[0]);
                    activityRuleAgent.setPosAmount(strArr[1]);
                }
            }

            //提现服务ID
            String cjtTxServiceId = sysDictService.getFirstValueByKey(CjtProfitRule.cjt_tx_service_id);
            //交易分润推送模板
            CjtPushTemplate transTemplate = cjtProfitRuleService.selectFirstByType(CjtPushTemplate.type_trans);
            //推荐奖励推送模板
            CjtPushTemplate recommendTemplate = cjtProfitRuleService.selectFirstByType(CjtPushTemplate.type_recommend);
            //活动补贴模板-直推商户
            CjtPushTemplate activity1Template = cjtProfitRuleService.selectFirstByType(CjtPushTemplate.type_activity_one);
            //活动补贴模板-间推商户
            CjtPushTemplate activity2Template = cjtProfitRuleService.selectFirstByType(CjtPushTemplate.type_activity_two);

            Map<String, Object> ruleMap = new HashMap<>();
            ruleMap.put("posProfitRuleList", posProfitRuleList);
            ruleMap.put("noCardProfitRuleList", noCardProfitRuleList);
            ruleMap.put("registerRule", registerRule);
            ruleMap.put("recommondRule", recommondRule);
            ruleMap.put("activityRule", activityRule);
            ruleMap.put("recommondRuleAgent", recommondRuleAgent);
            ruleMap.put("activityRuleAgent", activityRuleAgent);
            ruleMap.put("fenAmountProfitAgentRule", fenAmountProfitAgentRule);

            ruleMap.put("cjtTxServiceId", cjtTxServiceId);
            ruleMap.put("transTemplate", transTemplate);
            ruleMap.put("recommendTemplate", recommendTemplate);
            ruleMap.put("activity1Template", activity1Template);
            ruleMap.put("activity2Template", activity2Template);


            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(ruleMap);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询超级推分润奖励规则异常", e);
        }
        return result;
    }

    /**
     * 修改超级推分润奖励规则
     * @param cjtProfitRule
     * @return
     */
    @RequestMapping("/updateProfitRule")
    @SystemLog(operCode = "cjtProfitRule.updateProfitRule", description = "修改分润配置")
    public Result updateProfitRule(@RequestBody CjtProfitRule cjtProfitRule){
        Result result = new Result();
        try {
            String profit1 = cjtProfitRule.getProfit1();
            String profit2 = cjtProfitRule.getProfit2();
            checkProfit(profit1);
            checkProfit(profit2);
            int num = cjtProfitRuleService.updateProfitRule(cjtProfitRule);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改保存超级推分润奖励规则异常", e);
        }
        return result;
    }

    private void checkProfit(String profit1) {
        if(StringUtil.isEmpty(profit1)) {
            throw new BossBaseException("奖励不能为空");
        }
        if (! StringUtil.isNumeric(profit1, false, 5, 3)){
            throw new BossBaseException("奖励格式不正确,最多三位小数");
        }
    }

    /**
     * 修改超级推奖励其他配置和推送信息
     * @param param
     * @return
     */
    @RequestMapping("/updateConfig")
    @SystemLog(operCode = "cjtProfitRule.updateConfig", description = "修改超级推奖励其他配置和推送信息")
    public Result updateConfig(@RequestBody Map<String, Object> param){
        Result result = new Result();
        try {
            int num = cjtProfitRuleService.updateConfig(param);
            if(num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改超级推奖励其他配置和推送信息异常", e);
        }
        return result;
    }

    /**
     * 查询分润规则详情
     *
     * @param profitRuleNo
     * @return
     */
    @RequestMapping("/profitRuleDetail")
    public Result profitRuleDetail(String profitRuleNo){
        Result result = new Result();
        try {
            CjtProfitRule rule = cjtProfitRuleService.selectProfitRuleDetail(profitRuleNo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(rule);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询分润规则详情异常", e);
        }
        return result;
    }

}
