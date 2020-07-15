package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 欢乐返活动硬件配置
 *
 * @author mys
 */
public class HlfHardware {

    private Integer id;
    private String activityCode;
    private String activiyName;
    private Long hardId;
    private BigDecimal transAmount;
    private BigDecimal cashBackAmount;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String operator;
    private String typeName;

    private String activityTypeNo;
    private String activityTypeName;

    //liuks
    private BigDecimal emptyAmount;//未满扣N元（欢乐返）
    private BigDecimal fullAmount;//满奖M元（欢乐返）

    private BigDecimal cashLastAllyAmount;
    private BigDecimal repeatRegisterAmount;

    private Integer defaultStatus;//是否取默认活动内容，0：否，1：是
    private Integer cumulateTransDay;//累计交易天数（奖）（欢乐返）
    private BigDecimal cumulateAmountMinus;//累计交易（扣）（欢乐返）
    private BigDecimal cumulateAmountAdd;//累计交易（奖）（欢乐返）

    private Integer cumulateTransMinusDay;//累计交易天数（扣）（欢乐返）
    private Long teamId;//所属组织',
    private String teamEntryId;//所属子组织',
    private String teamName;//所属组织名称
    private BigDecimal repeatEmptyAmount;//重复注册不满扣N值（欢乐返）
    private BigDecimal repeatFullAmount;//重复注册满奖M值（欢乐返）
    private Integer repeatCumulateTransDay;//重复注册累计交易奖励时间（奖）（欢乐返）
    private BigDecimal repeatCumulateAmountMinus;//重复累计交易（扣）（欢乐返）
    private BigDecimal repeatCumulateAmountAdd;//重复累计交易（奖）（欢乐返）
    private Integer repeatCumulateTransMinusDay;//重复注册累计交易扣费时间（扣）（欢乐返）
    private Integer activityRewardConfigId;//0元个性化活动配置关联id
    private Integer activityMerchantId;//活跃商户活动配置关联id
    private Integer activityAgentId;//代理商奖励活动配置关联id
    private Integer isMerchant;
    private Integer isAgent;
    private String subType;//欢乐返子类型类型,1:原来的欢乐返,2:欢乐返新活动

    private BigDecimal oneRewardAmount;//激活后第1个周期，首次注册奖励金额
    private BigDecimal oneRepeatRewardAmount;//激活后第1个周期，重复注册奖励金额
    private BigDecimal twoRewardAmount;//激活后第2个周期，首次注册奖励金额
    private BigDecimal twoRepeatRewardAmount;//激活后第2个周期，重复注册奖励金额
    private BigDecimal threeRewardAmount;//激活后第3个周期，首次注册奖励金额
    private BigDecimal threeRepeatRewardAmount;//激活后第3个周期，重复注册奖励金额
    private BigDecimal fourRewardAmount;//激活后第4个周期，首次注册奖励金额
    private BigDecimal fourRepeatRewardAmount;//激活后第4个周期，重复注册奖励金额

    private String teamEntryName;//所属子组织名称,


    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActiviyName() {
        return activiyName;
    }

    public void setActiviyName(String activiyName) {
        this.activiyName = activiyName;
    }

    public Long getHardId() {
        return hardId;
    }

    public void setHardId(Long hardId) {
        this.hardId = hardId;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public BigDecimal getCashLastAllyAmount() {
        return cashLastAllyAmount;
    }

    public void setCashLastAllyAmount(BigDecimal cashLastAllyAmount) {
        this.cashLastAllyAmount = cashLastAllyAmount;
    }

    public BigDecimal getRepeatRegisterAmount() {
        return repeatRegisterAmount;
    }

    public void setRepeatRegisterAmount(BigDecimal repeatRegisterAmount) {
        this.repeatRegisterAmount = repeatRegisterAmount;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public Integer getCumulateTransDay() {
        return cumulateTransDay;
    }

    public void setCumulateTransDay(Integer cumulateTransDay) {
        this.cumulateTransDay = cumulateTransDay;
    }

    public BigDecimal getCumulateAmountMinus() {
        return cumulateAmountMinus;
    }

    public void setCumulateAmountMinus(BigDecimal cumulateAmountMinus) {
        this.cumulateAmountMinus = cumulateAmountMinus;
    }

    public BigDecimal getCumulateAmountAdd() {
        return cumulateAmountAdd;
    }

    public void setCumulateAmountAdd(BigDecimal cumulateAmountAdd) {
        this.cumulateAmountAdd = cumulateAmountAdd;
    }

    public Integer getCumulateTransMinusDay() {
        return cumulateTransMinusDay;
    }

    public void setCumulateTransMinusDay(Integer cumulateTransMinusDay) {
        this.cumulateTransMinusDay = cumulateTransMinusDay;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public Integer getRepeatCumulateTransDay() {
        return repeatCumulateTransDay;
    }

    public void setRepeatCumulateTransDay(Integer repeatCumulateTransDay) {
        this.repeatCumulateTransDay = repeatCumulateTransDay;
    }

    public BigDecimal getRepeatCumulateAmountMinus() {
        return repeatCumulateAmountMinus;
    }

    public void setRepeatCumulateAmountMinus(BigDecimal repeatCumulateAmountMinus) {
        this.repeatCumulateAmountMinus = repeatCumulateAmountMinus;
    }

    public BigDecimal getRepeatCumulateAmountAdd() {
        return repeatCumulateAmountAdd;
    }

    public void setRepeatCumulateAmountAdd(BigDecimal repeatCumulateAmountAdd) {
        this.repeatCumulateAmountAdd = repeatCumulateAmountAdd;
    }

    public Integer getRepeatCumulateTransMinusDay() {
        return repeatCumulateTransMinusDay;
    }

    public void setRepeatCumulateTransMinusDay(Integer repeatCumulateTransMinusDay) {
        this.repeatCumulateTransMinusDay = repeatCumulateTransMinusDay;
    }

    public Integer getActivityRewardConfigId() {
        return activityRewardConfigId;
    }

    public void setActivityRewardConfigId(Integer activityRewardConfigId) {
        this.activityRewardConfigId = activityRewardConfigId;
    }

    public Integer getActivityMerchantId() {
        return activityMerchantId;
    }

    public void setActivityMerchantId(Integer activityMerchantId) {
        this.activityMerchantId = activityMerchantId;
    }


    public Integer getActivityAgentId() {
        return activityAgentId;
    }

    public void setActivityAgentId(Integer activityAgentId) {
        this.activityAgentId = activityAgentId;
    }

    public Integer getIsMerchant() {
        return isMerchant;
    }

    public void setIsMerchant(Integer isMerchant) {
        this.isMerchant = isMerchant;
    }

    public Integer getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Integer isAgent) {
        this.isAgent = isAgent;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }
}
