package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityHardwareType {

    private Integer id;
    private String teamId;
    private String activityTypeNo;
    private String agentNo;
    private String agentNode;
    private String activiyName;
    private String smartActivityName;
    private String activityTypeName;
    private String activityCode;
    private BigDecimal transAmount;
    private BigDecimal cashBackAmount;
    private String remark;
    private String activityDetails;
    private Date createTime;
    private Date updateTime;
    private BigDecimal repeatRegisterAmount;
    private BigDecimal repeatRegisterRatio;

    private BigDecimal cashLastAllyAmount;
    private BigDecimal cashLastTeamAmount;

    private String functionName;//活动名称
    private BigDecimal taxRate;//税额百分比
    private Integer updateAgentStatus;
    private Long hpId;
    private String typeName;

    private String countTradeScope;//欢乐返子类型交易统计类型范围,多类型以,分隔。值为数据字典key为PAY_METHOD_TYPE的值
    private Integer ruleId;
    private String ruleName;
    private Integer hlfAgentRewardConfigId;
    private Integer xhlfSmartConfigId;
    private String activityName;

    private BigDecimal scanRewardAmount;
    private BigDecimal scanRepeatRewardAmount;

    private BigDecimal allRewardAmount;
    private BigDecimal allRepeatRewardAmount;


    private BigDecimal emptyAmount;//未满扣N元（欢乐返）
    private BigDecimal fullAmount;//满奖M元（欢乐返）
    private BigDecimal repeatEmptyAmount;//重复注册不满扣N值（欢乐返）
    private BigDecimal repeatFullAmount;//重复注册满奖M值（欢乐返）

    private String subType;//欢乐返子类型类型,1:原来的欢乐返,2:欢乐返新活动
    private BigDecimal oneTransAmount;//激活后第一个周期，累计交易金额
    private BigDecimal oneRewardAmount;//激活后第一个周期，首次注册奖励金额
    private BigDecimal oneRepeatRewardAmount;//激活后第一个周期，重复注册奖励金额
    private BigDecimal twoTransAmount;//激活后第二个周期，累计交易金额
    private BigDecimal twoRewardAmount;//激活后第二个周期，首次注册奖励金额
    private BigDecimal twoRepeatRewardAmount;//激活后第二个周期，重复注册奖励金额
    private BigDecimal threeTransAmount;//激活后第三个周期，累计交易金额
    private BigDecimal threeRewardAmount;//激活后第三个周期，首次注册奖励金额
    private BigDecimal threeRepeatRewardAmount;//激活后第三个周期，重复注册奖励金额
    private BigDecimal fourTransAmount;//激活后第四个周期，累计交易金额
    private BigDecimal fourRewardAmount;//激活后第四个周期，首次注册奖励金额
    private BigDecimal fourRepeatRewardAmount;//激活后第四个周期，重复注册奖励金额
    private Integer oneLimitDays;//激活后第一个周期,多少天内
    private Integer twoLimitDays;//激活后第二个周期,多少天内
    private Integer threeLimitDays;//激活后第三个周期,多少天内
    private Integer fourLimitDays;//激活后第四个周期,多少天内
    private Integer merchantLimitDays;//'激活后商户累计交易天数'
    private BigDecimal merchantTransAmount;//'激活后商户累计交易金额'
    private BigDecimal merchantRewardAmount;//'激活后，商户首次注册奖励金额'
    private BigDecimal merchantRepeatRewardAmount;//'激活后，商户重复注册奖励金额'

    private Integer agentTransTotalType;//'累计交易量统计方式,1:只统计所有已绑卡后的交易,2:统计所有POS刷卡交易'

    private Long orgId;//组织ID
    private String orgName;//组织名称
    private String teamEntryId;//子级组织ID
    private String teamEntryName;//子级组织名称


    private Integer sOneLimitDays;//第1次考核配置，激活后X天内
    private BigDecimal sOneTransAmount;//第1次考核配置，累计交易金额
    private BigDecimal oneRewardMerAmount;//第1次考核配置，首次注册奖励商户金额
    private BigDecimal oneRepeatRewardMerAmount;//第1次考核配置，重复注册奖励商户金额
    private BigDecimal oneRewardAgentAmount;//第1次考核配置，首次注册奖励代理商金额
    private BigDecimal oneRepeatRewardAgentAmount;//第1次考核配置，重复注册奖励代理商金额

    private Integer sTwoLimitDays;//第2次考核配置，激活后X天内
    private BigDecimal sTwoTransAmount;//第2次考核配置，累计交易金额
    private BigDecimal twoRewardAgentAmount;//第2次考核配置，首次注册奖励代理商金额
    private BigDecimal twoRepeatRewardAgentAmount;//第2次考核配置，重复注册奖励代理商金额

    private Integer sThreeLimitDays;//第3次考核配置，激活后X天内
    private BigDecimal sThreeTransAmount;//第3次考核配置，累计交易金额
    private BigDecimal threeRewardAgentAmount;//第3次考核配置，首次注册奖励代理商金额
    private BigDecimal threeRepeatRewardAgentAmount;//第3次考核配置，重复注册奖励代理商金额

    private BigDecimal oneSubTransAmount;//'第一次子考核累计金额'
    private BigDecimal oneSubRewardAmount;//'第一次子考核奖励金额'
    private BigDecimal oneSubRepeatReward;//'第一次子考核重复奖励金额'

    public Integer getsOneLimitDays() {
        return sOneLimitDays;
    }

    public void setsOneLimitDays(Integer sOneLimitDays) {
        this.sOneLimitDays = sOneLimitDays;
    }

    public BigDecimal getsOneTransAmount() {
        return sOneTransAmount;
    }

    public void setsOneTransAmount(BigDecimal sOneTransAmount) {
        this.sOneTransAmount = sOneTransAmount;
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

    public Integer getsTwoLimitDays() {
        return sTwoLimitDays;
    }

    public void setsTwoLimitDays(Integer sTwoLimitDays) {
        this.sTwoLimitDays = sTwoLimitDays;
    }

    public BigDecimal getsTwoTransAmount() {
        return sTwoTransAmount;
    }

    public void setsTwoTransAmount(BigDecimal sTwoTransAmount) {
        this.sTwoTransAmount = sTwoTransAmount;
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

    public Integer getsThreeLimitDays() {
        return sThreeLimitDays;
    }

    public void setsThreeLimitDays(Integer sThreeLimitDays) {
        this.sThreeLimitDays = sThreeLimitDays;
    }

    public BigDecimal getsThreeTransAmount() {
        return sThreeTransAmount;
    }

    public void setsThreeTransAmount(BigDecimal sThreeTransAmount) {
        this.sThreeTransAmount = sThreeTransAmount;
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

    public Integer getXhlfSmartConfigId() {
        return xhlfSmartConfigId;
    }

    public void setXhlfSmartConfigId(Integer xhlfSmartConfigId) {
        this.xhlfSmartConfigId = xhlfSmartConfigId;
    }

    public String getSmartActivityName() {
        return smartActivityName;
    }

    public void setSmartActivityName(String smartActivityName) {
        this.smartActivityName = smartActivityName;
    }

    public Integer getAgentTransTotalType() {
        return agentTransTotalType;
    }

    public void setAgentTransTotalType(Integer agentTransTotalType) {
        this.agentTransTotalType = agentTransTotalType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    public BigDecimal getMerchantRewardAmount() {
        return merchantRewardAmount;
    }

    public void setMerchantRewardAmount(BigDecimal merchantRewardAmount) {
        this.merchantRewardAmount = merchantRewardAmount;
    }

    public BigDecimal getMerchantRepeatRewardAmount() {
        return merchantRepeatRewardAmount;
    }

    public void setMerchantRepeatRewardAmount(BigDecimal merchantRepeatRewardAmount) {
        this.merchantRepeatRewardAmount = merchantRepeatRewardAmount;
    }

    public String getActiviyName() {
        return activiyName;
    }

    public void setActiviyName(String activiyName) {
        this.activiyName = activiyName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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

    public BigDecimal getOneRewardAmount() {
        return oneRewardAmount;
    }

    public void setOneRewardAmount(BigDecimal oneRewardAmount) {
        this.oneRewardAmount = oneRewardAmount;
    }

    public BigDecimal getOneRepeatRewardAmount() {
        return oneRepeatRewardAmount;
    }

    public void setOneRepeatRewardAmount(BigDecimal oneRepeatRewardAmount) {
        this.oneRepeatRewardAmount = oneRepeatRewardAmount;
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

    public BigDecimal getTwoRewardAmount() {
        return twoRewardAmount;
    }

    public void setTwoRewardAmount(BigDecimal twoRewardAmount) {
        this.twoRewardAmount = twoRewardAmount;
    }

    public BigDecimal getTwoRepeatRewardAmount() {
        return twoRepeatRewardAmount;
    }

    public void setTwoRepeatRewardAmount(BigDecimal twoRepeatRewardAmount) {
        this.twoRepeatRewardAmount = twoRepeatRewardAmount;
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

    public BigDecimal getThreeRewardAmount() {
        return threeRewardAmount;
    }

    public void setThreeRewardAmount(BigDecimal threeRewardAmount) {
        this.threeRewardAmount = threeRewardAmount;
    }

    public BigDecimal getThreeRepeatRewardAmount() {
        return threeRepeatRewardAmount;
    }

    public void setThreeRepeatRewardAmount(BigDecimal threeRepeatRewardAmount) {
        this.threeRepeatRewardAmount = threeRepeatRewardAmount;
    }

    public Integer getFourLimitDays() {
        return fourLimitDays;
    }

    public void setFourLimitDays(Integer fourLimitDays) {
        this.fourLimitDays = fourLimitDays;
    }

    public BigDecimal getFourTransAmount() {
        return fourTransAmount;
    }

    public void setFourTransAmount(BigDecimal fourTransAmount) {
        this.fourTransAmount = fourTransAmount;
    }

    public BigDecimal getFourRewardAmount() {
        return fourRewardAmount;
    }

    public void setFourRewardAmount(BigDecimal fourRewardAmount) {
        this.fourRewardAmount = fourRewardAmount;
    }

    public BigDecimal getFourRepeatRewardAmount() {
        return fourRepeatRewardAmount;
    }

    public void setFourRepeatRewardAmount(BigDecimal fourRepeatRewardAmount) {
        this.fourRepeatRewardAmount = fourRepeatRewardAmount;
    }

    public Integer getMerchantLimitDays() {
        return merchantLimitDays;
    }

    public void setMerchantLimitDays(Integer merchantLimitDays) {
        this.merchantLimitDays = merchantLimitDays;
    }

    public BigDecimal getMerchantTransAmount() {
        return merchantTransAmount;
    }

    public void setMerchantTransAmount(BigDecimal merchantTransAmount) {
        this.merchantTransAmount = merchantTransAmount;
    }

    public String getCountTradeScope() {
        return countTradeScope;
    }

    public void setCountTradeScope(String countTradeScope) {
        this.countTradeScope = countTradeScope;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public BigDecimal getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(BigDecimal cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public BigDecimal getCashLastAllyAmount() {
        return cashLastAllyAmount;
    }

    public void setCashLastAllyAmount(BigDecimal cashLastAllyAmount) {
        this.cashLastAllyAmount = cashLastAllyAmount;
    }

    public BigDecimal getCashLastTeamAmount() {
        return cashLastTeamAmount;
    }

    public void setCashLastTeamAmount(BigDecimal cashLastTeamAmount) {
        this.cashLastTeamAmount = cashLastTeamAmount;
    }

    public BigDecimal getRepeatRegisterAmount() {
        return repeatRegisterAmount;
    }

    public void setRepeatRegisterAmount(BigDecimal repeatRegisterAmount) {
        this.repeatRegisterAmount = repeatRegisterAmount;
    }

    public BigDecimal getRepeatRegisterRatio() {
        return repeatRegisterRatio;
    }

    public void setRepeatRegisterRatio(BigDecimal repeatRegisterRatio) {
        this.repeatRegisterRatio = repeatRegisterRatio;
    }

    public Integer getUpdateAgentStatus() {
        return updateAgentStatus;
    }

    public void setUpdateAgentStatus(Integer updateAgentStatus) {
        this.updateAgentStatus = updateAgentStatus;
    }

    public Long getHpId() {
        return hpId;
    }

    public void setHpId(Long hpId) {
        this.hpId = hpId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getEmptyAmount() {
        return emptyAmount;
    }

    public void setEmptyAmount(BigDecimal emptyAmount) {
        this.emptyAmount = emptyAmount;
    }

    public BigDecimal getFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(BigDecimal fullAmount) {
        this.fullAmount = fullAmount;
    }

    public BigDecimal getRepeatEmptyAmount() {
        return repeatEmptyAmount;
    }

    public void setRepeatEmptyAmount(BigDecimal repeatEmptyAmount) {
        this.repeatEmptyAmount = repeatEmptyAmount;
    }

    public BigDecimal getRepeatFullAmount() {
        return repeatFullAmount;
    }

    public void setRepeatFullAmount(BigDecimal repeatFullAmount) {
        this.repeatFullAmount = repeatFullAmount;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Integer getHlfAgentRewardConfigId() {
        return hlfAgentRewardConfigId;
    }

    public void setHlfAgentRewardConfigId(Integer hlfAgentRewardConfigId) {
        this.hlfAgentRewardConfigId = hlfAgentRewardConfigId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getScanRewardAmount() {
        return scanRewardAmount;
    }

    public void setScanRewardAmount(BigDecimal scanRewardAmount) {
        this.scanRewardAmount = scanRewardAmount;
    }

    public BigDecimal getScanRepeatRewardAmount() {
        return scanRepeatRewardAmount;
    }

    public void setScanRepeatRewardAmount(BigDecimal scanRepeatRewardAmount) {
        this.scanRepeatRewardAmount = scanRepeatRewardAmount;
    }

    public BigDecimal getAllRewardAmount() {
        return allRewardAmount;
    }

    public void setAllRewardAmount(BigDecimal allRewardAmount) {
        this.allRewardAmount = allRewardAmount;
    }

    public BigDecimal getAllRepeatRewardAmount() {
        return allRepeatRewardAmount;
    }

    public void setAllRepeatRewardAmount(BigDecimal allRepeatRewardAmount) {
        this.allRepeatRewardAmount = allRepeatRewardAmount;
    }

    public BigDecimal getOneSubTransAmount() {
        return oneSubTransAmount;
    }

    public ActivityHardwareType setOneSubTransAmount(BigDecimal oneSubTransAmount) {
        this.oneSubTransAmount = oneSubTransAmount;
        return this;
    }

    public ActivityHardwareType setOneSubWhenIsNull(){
        setOneSubTransAmount(getOneSubTransAmount() == null ? BigDecimal.ZERO : getOneSubTransAmount());
        setOneSubRepeatReward(getOneSubRepeatReward() == null ? BigDecimal.ZERO : getOneSubRepeatReward());
        setOneSubRewardAmount(getOneSubRewardAmount() == null ? BigDecimal.ZERO : getOneSubRewardAmount());
        return this;
    }

    public BigDecimal getOneSubRewardAmount() {
        return oneSubRewardAmount;
    }

    public ActivityHardwareType setOneSubRewardAmount(BigDecimal oneSubRewardAmount) {
        this.oneSubRewardAmount = oneSubRewardAmount;
        return this;
    }

    public BigDecimal getOneSubRepeatReward() {
        return oneSubRepeatReward;
    }

    public ActivityHardwareType setOneSubRepeatReward(BigDecimal oneSubRepeatReward) {
        this.oneSubRepeatReward = oneSubRepeatReward;
        return this;
    }

    @Override
    public String toString() {
        return "ActivityHardwareType{" +
                "id=" + id +
                ", teamId='" + teamId + '\'' +
                ", activityTypeNo='" + activityTypeNo + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", agentNode='" + agentNode + '\'' +
                ", activiyName='" + activiyName + '\'' +
                ", activityTypeName='" + activityTypeName + '\'' +
                ", activityCode='" + activityCode + '\'' +
                ", transAmount=" + transAmount +
                ", cashBackAmount=" + cashBackAmount +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", repeatRegisterAmount=" + repeatRegisterAmount +
                ", repeatRegisterRatio=" + repeatRegisterRatio +
                ", cashLastAllyAmount=" + cashLastAllyAmount +
                ", cashLastTeamAmount=" + cashLastTeamAmount +
                ", functionName='" + functionName + '\'' +
                ", taxRate=" + taxRate +
                ", updateAgentStatus=" + updateAgentStatus +
                ", hpId=" + hpId +
                ", typeName='" + typeName + '\'' +
                ", countTradeScope='" + countTradeScope + '\'' +
                ", ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", hlfAgentRewardConfigId=" + hlfAgentRewardConfigId +
                ", activityName='" + activityName + '\'' +
                ", scanRewardAmount=" + scanRewardAmount +
                ", scanRepeatRewardAmount=" + scanRepeatRewardAmount +
                ", allRewardAmount=" + allRewardAmount +
                ", allRepeatRewardAmount=" + allRepeatRewardAmount +
                ", emptyAmount=" + emptyAmount +
                ", fullAmount=" + fullAmount +
                ", repeatEmptyAmount=" + repeatEmptyAmount +
                ", repeatFullAmount=" + repeatFullAmount +
                ", subType='" + subType + '\'' +
                ", oneTransAmount=" + oneTransAmount +
                ", oneRewardAmount=" + oneRewardAmount +
                ", oneRepeatRewardAmount=" + oneRepeatRewardAmount +
                ", twoTransAmount=" + twoTransAmount +
                ", twoRewardAmount=" + twoRewardAmount +
                ", twoRepeatRewardAmount=" + twoRepeatRewardAmount +
                ", threeTransAmount=" + threeTransAmount +
                ", threeRewardAmount=" + threeRewardAmount +
                ", threeRepeatRewardAmount=" + threeRepeatRewardAmount +
                ", fourTransAmount=" + fourTransAmount +
                ", fourRewardAmount=" + fourRewardAmount +
                ", fourRepeatRewardAmount=" + fourRepeatRewardAmount +
                ", oneLimitDays=" + oneLimitDays +
                ", twoLimitDays=" + twoLimitDays +
                ", threeLimitDays=" + threeLimitDays +
                ", fourLimitDays=" + fourLimitDays +
                ", merchantLimitDays=" + merchantLimitDays +
                ", merchantTransAmount=" + merchantTransAmount +
                ", merchantRewardAmount=" + merchantRewardAmount +
                ", merchantRepeatRewardAmount=" + merchantRepeatRewardAmount +
                ", oneSubTransAmount=" + oneSubTransAmount +
                ", oneSubRewardAmount=" + oneSubRewardAmount +
                ", oneSubRepeatReward=" + oneSubRepeatReward +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", teamEntryId='" + teamEntryId + '\'' +
                ", teamEntryName='" + teamEntryName + '\'' +
                '}';
    }
}
