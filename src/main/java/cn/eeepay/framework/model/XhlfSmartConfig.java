package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新欢乐送智能版活动配置表
 *
 * @author Administrator
 */
public class XhlfSmartConfig {
    private Integer id;
    private String activityName;//活动名称

    private Integer oneLimitDays;//第1次考核配置，激活后X天内
    private BigDecimal oneTransAmount;//第1次考核配置，累计交易金额
    private BigDecimal oneRewardMerAmount;//第1次考核配置，首次注册奖励商户金额
    private BigDecimal oneRepeatRewardMerAmount;//第1次考核配置，重复注册奖励商户金额
    private BigDecimal oneRewardAgentAmount;//第1次考核配置，首次注册奖励代理商金额
    private BigDecimal oneRepeatRewardAgentAmount;//第1次考核配置，重复注册奖励代理商金额

    private Integer twoLimitDays;//第2次考核配置，激活后X天内
    private BigDecimal twoTransAmount;//第2次考核配置，累计交易金额
    private BigDecimal twoRewardAgentAmount;//第2次考核配置，首次注册奖励代理商金额
    private BigDecimal twoRepeatRewardAgentAmount;//第2次考核配置，重复注册奖励代理商金额

    private Integer threeLimitDays;//第3次考核配置，激活后X天内
    private BigDecimal threeTransAmount;//第3次考核配置，累计交易金额
    private BigDecimal threeRewardAgentAmount;//第3次考核配置，首次注册奖励代理商金额
    private BigDecimal threeRepeatRewardAgentAmount;//第3次考核配置，重复注册奖励代理商金额

    private Date createTime;
    private String minCreateTime;
    private String maxCreateTime;
    private String lastUpdateTime;

    private String operator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getOneLimitDays() {
        return oneLimitDays;
    }

    public void setOneLimitDays(Integer oneLimitDays) {
        this.oneLimitDays = oneLimitDays;
    }

    public BigDecimal getOneTransAmount() {
        return oneTransAmount;
    }

    public void setOneTransAmount(BigDecimal oneTransAmount) {
        this.oneTransAmount = oneTransAmount;
    }

    public BigDecimal getOneRewardMerAmount() {
        return oneRewardMerAmount;
    }

    public void setOneRewardMerAmount(BigDecimal oneRewardMerAmount) {
        this.oneRewardMerAmount = oneRewardMerAmount;
    }

    public BigDecimal getOneRepeatRewardMerAmount() {
        return oneRepeatRewardMerAmount;
    }

    public void setOneRepeatRewardMerAmount(BigDecimal oneRepeatRewardMerAmount) {
        this.oneRepeatRewardMerAmount = oneRepeatRewardMerAmount;
    }

    public BigDecimal getOneRewardAgentAmount() {
        return oneRewardAgentAmount;
    }

    public void setOneRewardAgentAmount(BigDecimal oneRewardAgentAmount) {
        this.oneRewardAgentAmount = oneRewardAgentAmount;
    }

    public BigDecimal getOneRepeatRewardAgentAmount() {
        return oneRepeatRewardAgentAmount;
    }

    public void setOneRepeatRewardAgentAmount(BigDecimal oneRepeatRewardAgentAmount) {
        this.oneRepeatRewardAgentAmount = oneRepeatRewardAgentAmount;
    }

    public Integer getTwoLimitDays() {
        return twoLimitDays;
    }

    public void setTwoLimitDays(Integer twoLimitDays) {
        this.twoLimitDays = twoLimitDays;
    }

    public BigDecimal getTwoTransAmount() {
        return twoTransAmount;
    }

    public void setTwoTransAmount(BigDecimal twoTransAmount) {
        this.twoTransAmount = twoTransAmount;
    }

    public BigDecimal getTwoRewardAgentAmount() {
        return twoRewardAgentAmount;
    }

    public void setTwoRewardAgentAmount(BigDecimal twoRewardAgentAmount) {
        this.twoRewardAgentAmount = twoRewardAgentAmount;
    }

    public BigDecimal getTwoRepeatRewardAgentAmount() {
        return twoRepeatRewardAgentAmount;
    }

    public void setTwoRepeatRewardAgentAmount(BigDecimal twoRepeatRewardAgentAmount) {
        this.twoRepeatRewardAgentAmount = twoRepeatRewardAgentAmount;
    }

    public Integer getThreeLimitDays() {
        return threeLimitDays;
    }

    public void setThreeLimitDays(Integer threeLimitDays) {
        this.threeLimitDays = threeLimitDays;
    }

    public BigDecimal getThreeTransAmount() {
        return threeTransAmount;
    }

    public void setThreeTransAmount(BigDecimal threeTransAmount) {
        this.threeTransAmount = threeTransAmount;
    }

    public BigDecimal getThreeRewardAgentAmount() {
        return threeRewardAgentAmount;
    }

    public void setThreeRewardAgentAmount(BigDecimal threeRewardAgentAmount) {
        this.threeRewardAgentAmount = threeRewardAgentAmount;
    }

    public BigDecimal getThreeRepeatRewardAgentAmount() {
        return threeRepeatRewardAgentAmount;
    }

    public void setThreeRepeatRewardAgentAmount(BigDecimal threeRepeatRewardAgentAmount) {
        this.threeRepeatRewardAgentAmount = threeRepeatRewardAgentAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMinCreateTime() {
        return minCreateTime;
    }

    public void setMinCreateTime(String minCreateTime) {
        this.minCreateTime = minCreateTime;
    }

    public String getMaxCreateTime() {
        return maxCreateTime;
    }

    public void setMaxCreateTime(String maxCreateTime) {
        this.maxCreateTime = maxCreateTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
