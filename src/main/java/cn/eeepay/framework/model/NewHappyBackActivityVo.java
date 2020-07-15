package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class NewHappyBackActivityVo {
	private Integer id;
	private String activeOrder;
	private Date activeTime;
	private BigDecimal targetAmount;
	private BigDecimal rewardAmount;
	private String currentCycle;
	private Date rewardStartTime;
	private Date rewardEndTime;
	private String currentTargetStatus;
	private Date currentTargetTime;
	private String rewardAccountStatus;
	private Date rewardAccountTime;
	private String activityTargetStatus;
	private Date activityTargetTime;
	private String merchantNo;
	private String agentNo;
	private String agentName;
	private String oneAgentNo;
	private String oneAgentName;
	private Integer agentTransTotalType;//'累计交易量统计方式,1:只统计所有已绑卡后的交易,2:统计所有POS刷卡交易'

	private String activityTypeNo;//欢乐返子类型
	private Integer hardId;//硬件ID
	private String teamId;//组织
	private String teamEntryId;//子组织
	private String teamName;
	private String teamEntryName;

	public Integer getAgentTransTotalType() {
		return agentTransTotalType;
	}

	public void setAgentTransTotalType(Integer agentTransTotalType) {
		this.agentTransTotalType = agentTransTotalType;
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

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	public BigDecimal getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(BigDecimal rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public String getCurrentCycle() {
		return currentCycle;
	}

	public void setCurrentCycle(String currentCycle) {
		this.currentCycle = currentCycle;
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

	public String getCurrentTargetStatus() {
		return currentTargetStatus;
	}

	public void setCurrentTargetStatus(String currentTargetStatus) {
		this.currentTargetStatus = currentTargetStatus;
	}

	public Date getCurrentTargetTime() {
		return currentTargetTime;
	}

	public void setCurrentTargetTime(Date currentTargetTime) {
		this.currentTargetTime = currentTargetTime;
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

	public Date getActivityTargetTime() {
		return activityTargetTime;
	}

	public void setActivityTargetTime(Date activityTargetTime) {
		this.activityTargetTime = activityTargetTime;
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

	public Date getRewardAccountTime() {
		return rewardAccountTime;
	}

	public void setRewardAccountTime(Date rewardAccountTime) {
		this.rewardAccountTime = rewardAccountTime;
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
}
