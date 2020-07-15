package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 欢乐返代理商活动奖励配置表
 *
 * @author Administrator
 */
public class HlfActivityAgentRule {
    private Integer id;
    private String activityName;

    private Integer scanActivityDays;
    private BigDecimal scanTargetAmount;
    private BigDecimal scanRewardAmount;
    private BigDecimal scanRepeatRewardAmount;

    private Integer allActivityDays;
    private BigDecimal allTargetAmount;
    private BigDecimal allRewardAmount;
    private BigDecimal allRepeatRewardAmount;

    private Date createTime;
    private String operator;
    private String lastUpdateTime;

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

    public Integer getScanActivityDays() {
        return scanActivityDays;
    }

    public void setScanActivityDays(Integer scanActivityDays) {
        this.scanActivityDays = scanActivityDays;
    }

    public BigDecimal getScanTargetAmount() {
        return scanTargetAmount;
    }

    public void setScanTargetAmount(BigDecimal scanTargetAmount) {
        this.scanTargetAmount = scanTargetAmount;
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

    public Integer getAllActivityDays() {
        return allActivityDays;
    }

    public void setAllActivityDays(Integer allActivityDays) {
        this.allActivityDays = allActivityDays;
    }

    public BigDecimal getAllTargetAmount() {
        return allTargetAmount;
    }

    public void setAllTargetAmount(BigDecimal allTargetAmount) {
        this.allTargetAmount = allTargetAmount;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
