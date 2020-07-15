package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author rpc
 * @description 欢乐返代理商活动奖励查询
 * @date 2019/11/6
 */
public class HappyBackActivityAgent {
    private Integer id;
    private String activeOrder;//激活订单号

    private Integer scanActivityDays;
    private BigDecimal scanTargetAmount;
    private BigDecimal scanRewardAmount;
    private String scanTargetStatus;
    private String scanAccountStatus;

    private Integer allActivityDays;
    private BigDecimal allTargetAmount;
    private BigDecimal allRewardAmount;
    private String allTargetStatus;
    private String allAccountStatus;


    private String merchantNo;//所属商户编号
    private String agentNode;//所属代理商节点
    private String agentNo;//所属代理商编号
    private String agentName;//所属代理商名称
    private String oneAgentNo;//所属一级代理商编号
    private String oneAgentName;//所属一级代理商名称
    private String containsLower;//包含下级

    private Date activeTime;//激活日期
    private String activeTimeStart;
    private String activeTimeEnd;

    private Date scanTargetTime;//扫码交易满奖达标日期
    private String scanTargetTimeStart;
    private String scanTargetTimeEnd;

    private Date scanRewardEndTime;//扫码交易满奖截止日期
    private String scanRewardEndTimeStart;
    private String scanRewardEndTimeEnd;

    private Date scanAccountTime;//扫码交易满奖入账日期
    private String scanAccountTimeStart;
    private String scanAccountTimeEnd;

    private Date allTargetTime;//全部交易满奖达标日期
    private String allTargetTimeStart;
    private String allTargetTimeEnd;

    private Date allRewardEndTime;//全部交易满奖截止日期
    private String allRewardEndTimeStart;
    private String allRewardEndTimeEnd;

    private Date allAccountTime;//全部交易满奖入账日期
    private String allAccountTimeStart;
    private String allAccountTimeEnd;

    /**
     * 交易开始时间
     */
    private Date transStartTime;
    /**
     * 交易结束时间
     */
    private Date transEndTime;


    private String activityTypeNo;//欢乐返子类型
    private Integer hardId;//硬件ID
    private String teamId;//组织
    private String teamEntryId;//子组织
    private String teamName;
    private String teamEntryName;

    public String getContainsLower() {
        return containsLower;
    }

    public void setContainsLower(String containsLower) {
        this.containsLower = containsLower;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(String activeOrder) {
        this.activeOrder = activeOrder;
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

    public String getScanTargetStatus() {
        return scanTargetStatus;
    }

    public void setScanTargetStatus(String scanTargetStatus) {
        this.scanTargetStatus = scanTargetStatus;
    }

    public String getScanAccountStatus() {
        return scanAccountStatus;
    }

    public void setScanAccountStatus(String scanAccountStatus) {
        this.scanAccountStatus = scanAccountStatus;
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

    public String getAllTargetStatus() {
        return allTargetStatus;
    }

    public void setAllTargetStatus(String allTargetStatus) {
        this.allTargetStatus = allTargetStatus;
    }

    public String getAllAccountStatus() {
        return allAccountStatus;
    }

    public void setAllAccountStatus(String allAccountStatus) {
        this.allAccountStatus = allAccountStatus;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
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

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public String getActiveTimeStart() {
        return activeTimeStart;
    }

    public void setActiveTimeStart(String activeTimeStart) {
        this.activeTimeStart = activeTimeStart;
    }

    public String getActiveTimeEnd() {
        return activeTimeEnd;
    }

    public void setActiveTimeEnd(String activeTimeEnd) {
        this.activeTimeEnd = activeTimeEnd;
    }

    public Date getScanTargetTime() {
        return scanTargetTime;
    }

    public void setScanTargetTime(Date scanTargetTime) {
        this.scanTargetTime = scanTargetTime;
    }

    public String getScanTargetTimeStart() {
        return scanTargetTimeStart;
    }

    public void setScanTargetTimeStart(String scanTargetTimeStart) {
        this.scanTargetTimeStart = scanTargetTimeStart;
    }

    public String getScanTargetTimeEnd() {
        return scanTargetTimeEnd;
    }

    public void setScanTargetTimeEnd(String scanTargetTimeEnd) {
        this.scanTargetTimeEnd = scanTargetTimeEnd;
    }

    public Date getScanRewardEndTime() {
        return scanRewardEndTime;
    }

    public void setScanRewardEndTime(Date scanRewardEndTime) {
        this.scanRewardEndTime = scanRewardEndTime;
    }

    public String getScanRewardEndTimeStart() {
        return scanRewardEndTimeStart;
    }

    public void setScanRewardEndTimeStart(String scanRewardEndTimeStart) {
        this.scanRewardEndTimeStart = scanRewardEndTimeStart;
    }

    public String getScanRewardEndTimeEnd() {
        return scanRewardEndTimeEnd;
    }

    public void setScanRewardEndTimeEnd(String scanRewardEndTimeEnd) {
        this.scanRewardEndTimeEnd = scanRewardEndTimeEnd;
    }

    public Date getScanAccountTime() {
        return scanAccountTime;
    }

    public void setScanAccountTime(Date scanAccountTime) {
        this.scanAccountTime = scanAccountTime;
    }

    public String getScanAccountTimeStart() {
        return scanAccountTimeStart;
    }

    public void setScanAccountTimeStart(String scanAccountTimeStart) {
        this.scanAccountTimeStart = scanAccountTimeStart;
    }

    public String getScanAccountTimeEnd() {
        return scanAccountTimeEnd;
    }

    public void setScanAccountTimeEnd(String scanAccountTimeEnd) {
        this.scanAccountTimeEnd = scanAccountTimeEnd;
    }

    public Date getAllTargetTime() {
        return allTargetTime;
    }

    public void setAllTargetTime(Date allTargetTime) {
        this.allTargetTime = allTargetTime;
    }

    public String getAllTargetTimeStart() {
        return allTargetTimeStart;
    }

    public void setAllTargetTimeStart(String allTargetTimeStart) {
        this.allTargetTimeStart = allTargetTimeStart;
    }

    public String getAllTargetTimeEnd() {
        return allTargetTimeEnd;
    }

    public void setAllTargetTimeEnd(String allTargetTimeEnd) {
        this.allTargetTimeEnd = allTargetTimeEnd;
    }

    public Date getAllRewardEndTime() {
        return allRewardEndTime;
    }

    public void setAllRewardEndTime(Date allRewardEndTime) {
        this.allRewardEndTime = allRewardEndTime;
    }

    public String getAllRewardEndTimeStart() {
        return allRewardEndTimeStart;
    }

    public void setAllRewardEndTimeStart(String allRewardEndTimeStart) {
        this.allRewardEndTimeStart = allRewardEndTimeStart;
    }

    public String getAllRewardEndTimeEnd() {
        return allRewardEndTimeEnd;
    }

    public void setAllRewardEndTimeEnd(String allRewardEndTimeEnd) {
        this.allRewardEndTimeEnd = allRewardEndTimeEnd;
    }

    public Date getAllAccountTime() {
        return allAccountTime;
    }

    public void setAllAccountTime(Date allAccountTime) {
        this.allAccountTime = allAccountTime;
    }

    public String getAllAccountTimeStart() {
        return allAccountTimeStart;
    }

    public void setAllAccountTimeStart(String allAccountTimeStart) {
        this.allAccountTimeStart = allAccountTimeStart;
    }

    public String getAllAccountTimeEnd() {
        return allAccountTimeEnd;
    }

    public void setAllAccountTimeEnd(String allAccountTimeEnd) {
        this.allAccountTimeEnd = allAccountTimeEnd;
    }

    public Date getTransStartTime() {
        return transStartTime;
    }

    public void setTransStartTime(Date transStartTime) {
        this.transStartTime = transStartTime;
    }

    public Date getTransEndTime() {
        return transEndTime;
    }

    public void setTransEndTime(Date transEndTime) {
        this.transEndTime = transEndTime;
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
        return "HappyBackActivityAgent{" +
                "id=" + id +
                ", activeOrder='" + activeOrder + '\'' +
                ", scanActivityDays=" + scanActivityDays +
                ", scanTargetAmount=" + scanTargetAmount +
                ", scanRewardAmount=" + scanRewardAmount +
                ", scanTargetStatus='" + scanTargetStatus + '\'' +
                ", scanAccountStatus='" + scanAccountStatus + '\'' +
                ", allActivityDays=" + allActivityDays +
                ", allTargetAmount=" + allTargetAmount +
                ", allRewardAmount=" + allRewardAmount +
                ", allTargetStatus='" + allTargetStatus + '\'' +
                ", allAccountStatus='" + allAccountStatus + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", agentNode='" + agentNode + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", agentName='" + agentName + '\'' +
                ", oneAgentNo='" + oneAgentNo + '\'' +
                ", oneAgentName='" + oneAgentName + '\'' +
                ", containsLower='" + containsLower + '\'' +
                ", activeTime=" + activeTime +
                ", activeTimeStart='" + activeTimeStart + '\'' +
                ", activeTimeEnd='" + activeTimeEnd + '\'' +
                ", scanTargetTime=" + scanTargetTime +
                ", scanTargetTimeStart='" + scanTargetTimeStart + '\'' +
                ", scanTargetTimeEnd='" + scanTargetTimeEnd + '\'' +
                ", scanRewardEndTime=" + scanRewardEndTime +
                ", scanRewardEndTimeStart='" + scanRewardEndTimeStart + '\'' +
                ", scanRewardEndTimeEnd='" + scanRewardEndTimeEnd + '\'' +
                ", scanAccountTime=" + scanAccountTime +
                ", scanAccountTimeStart='" + scanAccountTimeStart + '\'' +
                ", scanAccountTimeEnd='" + scanAccountTimeEnd + '\'' +
                ", allTargetTime=" + allTargetTime +
                ", allTargetTimeStart='" + allTargetTimeStart + '\'' +
                ", allTargetTimeEnd='" + allTargetTimeEnd + '\'' +
                ", allRewardEndTime=" + allRewardEndTime +
                ", allRewardEndTimeStart='" + allRewardEndTimeStart + '\'' +
                ", allRewardEndTimeEnd='" + allRewardEndTimeEnd + '\'' +
                ", allAccountTime=" + allAccountTime +
                ", allAccountTimeStart='" + allAccountTimeStart + '\'' +
                ", allAccountTimeEnd='" + allAccountTimeEnd + '\'' +
                ", transStartTime=" + transStartTime +
                ", transEndTime=" + transEndTime +
                '}';
    }
}
