package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HappySendNewOrder {
    private Integer id;

    private Integer merchantLimitDays;
    private Integer repeatStatus;


    List<String> orderNoList;

    private String activeOrder;
    private String merchantNo;
    private String agentNo;
    private String agentN;
    private String agentGrade;
    private String agentName;
    private String oneAgentNo;
    private String oneAgentName;

    private String rewardAccountStatus;
    private String activityTargetStatus;//活动达标状态


    private BigDecimal targetAmount;
    private BigDecimal totalAmount;
    private BigDecimal rewardAmount;
    private BigDecimal rewardAmountConfig;

    private Date activeTime;
    private Date activityTargetTime;
    private Date rewardAccountTime;
    private Date rewardStartTime;
    private Date rewardEndTime;

    private Date minRewardAccountTime;
    private Date maxRewardAccountTime;
    private Date minTargetTime;
    private Date maxTargetTime;
    private Date minRewardEndTime;
    private Date maxRewardEndTime;
    private Date minActiveTime;
    private Date maxActiveTime;

    private Boolean countAll;//全部数据

    private Boolean pageAll;//当前页

    private String checkIds;//选中的id

    private String activityTypeNo;//欢乐返子类型
    private Integer hardId;//硬件ID
    private String teamId;//组织
    private String teamEntryId;//子组织
    private String teamName;
    private String teamEntryName;

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }

    public String getAgentN() {
        return agentN;
    }

    public void setAgentN(String agentN) {
        this.agentN = agentN;
    }

    public String getAgentGrade() {
        return agentGrade;
    }

    public void setAgentGrade(String agentGrade) {
        this.agentGrade = agentGrade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantLimitDays() {
        return merchantLimitDays;
    }

    public void setMerchantLimitDays(Integer merchantLimitDays) {
        this.merchantLimitDays = merchantLimitDays;
    }

    public Integer getRepeatStatus() {
        return repeatStatus;
    }

    public void setRepeatStatus(Integer repeatStatus) {
        this.repeatStatus = repeatStatus;
    }

    public String getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(String activeOrder) {
        this.activeOrder = activeOrder;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getRewardAccountStatus() {
        return rewardAccountStatus;
    }

    public void setRewardAccountStatus(String rewardAccountStatus) {
        this.rewardAccountStatus = rewardAccountStatus;
    }

    public String getActivityTargetStatus() {
        return activityTargetStatus;
    }

    public void setActivityTargetStatus(String activityTargetStatus) {
        this.activityTargetStatus = activityTargetStatus;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public BigDecimal getRewardAmountConfig() {
        return rewardAmountConfig;
    }

    public void setRewardAmountConfig(BigDecimal rewardAmountConfig) {
        this.rewardAmountConfig = rewardAmountConfig;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getRewardAccountTime() {
        return rewardAccountTime;
    }

    public void setRewardAccountTime(Date rewardAccountTime) {
        this.rewardAccountTime = rewardAccountTime;
    }

    public Date getRewardStartTime() {
        return rewardStartTime;
    }

    public void setRewardStartTime(Date rewardStartTime) {
        this.rewardStartTime = rewardStartTime;
    }

    public Date getRewardEndTime() {
        return rewardEndTime;
    }

    public void setRewardEndTime(Date rewardEndTime) {
        this.rewardEndTime = rewardEndTime;
    }

    public Date getActivityTargetTime() {
        return activityTargetTime;
    }

    public void setActivityTargetTime(Date activityTargetTime) {
        this.activityTargetTime = activityTargetTime;
    }

    public Date getMinRewardAccountTime() {
        return minRewardAccountTime;
    }

    public void setMinRewardAccountTime(Date minRewardAccountTime) {
        this.minRewardAccountTime = minRewardAccountTime;
    }

    public Date getMaxRewardAccountTime() {
        return maxRewardAccountTime;
    }

    public void setMaxRewardAccountTime(Date maxRewardAccountTime) {
        this.maxRewardAccountTime = maxRewardAccountTime;
    }

    public Date getMinTargetTime() {
        return minTargetTime;
    }

    public void setMinTargetTime(Date minTargetTime) {
        this.minTargetTime = minTargetTime;
    }

    public Date getMaxTargetTime() {
        return maxTargetTime;
    }

    public void setMaxTargetTime(Date maxTargetTime) {
        this.maxTargetTime = maxTargetTime;
    }

    public Date getMinRewardEndTime() {
        return minRewardEndTime;
    }

    public void setMinRewardEndTime(Date minRewardEndTime) {
        this.minRewardEndTime = minRewardEndTime;
    }

    public Date getMaxRewardEndTime() {
        return maxRewardEndTime;
    }

    public void setMaxRewardEndTime(Date maxRewardEndTime) {
        this.maxRewardEndTime = maxRewardEndTime;
    }

    public Date getMinActiveTime() {
        return minActiveTime;
    }

    public void setMinActiveTime(Date minActiveTime) {
        this.minActiveTime = minActiveTime;
    }

    public Date getMaxActiveTime() {
        return maxActiveTime;
    }

    public void setMaxActiveTime(Date maxActiveTime) {
        this.maxActiveTime = maxActiveTime;
    }


    public Boolean getCountAll() {
        return countAll;
    }

    public void setCountAll(Boolean countAll) {
        this.countAll = countAll;
    }

    public Boolean getPageAll() {
        return pageAll;
    }

    public void setPageAll(Boolean pageAll) {
        this.pageAll = pageAll;
    }

    public String getCheckIds() {
        return checkIds;
    }

    public void setCheckIds(String checkIds) {
        this.checkIds = checkIds;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public Integer getHardId() {
        return hardId;
    }

    public void setHardId(Integer hardId) {
        this.hardId = hardId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    @Override
    public String toString() {
        return "HappySendNewOrder{" +
                "id=" + id +
                ", merchantLimitDays=" + merchantLimitDays +
                ", repeatStatus=" + repeatStatus +
                ", orderNoList=" + orderNoList +
                ", activeOrder='" + activeOrder + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", agentN='" + agentN + '\'' +
                ", agentGrade='" + agentGrade + '\'' +
                ", agentName='" + agentName + '\'' +
                ", oneAgentNo='" + oneAgentNo + '\'' +
                ", oneAgentName='" + oneAgentName + '\'' +
                ", rewardAccountStatus='" + rewardAccountStatus + '\'' +
                ", activityTargetStatus='" + activityTargetStatus + '\'' +
                ", targetAmount=" + targetAmount +
                ", totalAmount=" + totalAmount +
                ", rewardAmount=" + rewardAmount +
                ", rewardAmountConfig=" + rewardAmountConfig +
                ", activeTime=" + activeTime +
                ", activityTargetTime=" + activityTargetTime +
                ", rewardAccountTime=" + rewardAccountTime +
                ", rewardStartTime=" + rewardStartTime +
                ", rewardEndTime=" + rewardEndTime +
                ", minRewardAccountTime=" + minRewardAccountTime +
                ", maxRewardAccountTime=" + maxRewardAccountTime +
                ", minTargetTime=" + minTargetTime +
                ", maxTargetTime=" + maxTargetTime +
                ", minRewardEndTime=" + minRewardEndTime +
                ", maxRewardEndTime=" + maxRewardEndTime +
                ", minActiveTime=" + minActiveTime +
                ", maxActiveTime=" + maxActiveTime +
                ", countAll=" + countAll +
                ", pageAll=" + pageAll +
                ", checkIds='" + checkIds + '\'' +
                '}';
    }
}
