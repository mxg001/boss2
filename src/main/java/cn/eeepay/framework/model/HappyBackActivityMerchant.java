package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/19
 */
public class HappyBackActivityMerchant {
	private Integer id;
	private String activeOrder;//激活订单号
	private String targetStatus;//活动达标状态
	private String rewardAccountStatus;//奖励入账状态
	private String deductStatus;//扣款状态
	private BigDecimal rewardAmount;//奖励金额
	private BigDecimal rewardAmountMin;
	private BigDecimal rewardAmountMax;
	private BigDecimal deductAmount;//扣款金额
	private BigDecimal deductAmountMin;
	private BigDecimal deductAmountMax;
	private String merchantNo;//所属商户编号
	private String agentNode;//所属代理商节点
	private String agentNo;//所属代理商编号
	private String agentName;//所属代理商名称
	private String oneAgentNo;//所属一级代理商编号
	private String oneAgentName;//所属一级代理商名称
	private Date activeTime;//激活日期
	private String activeTimeStart;
	private String activeTimeEnd;
	private Date targetTime;//达标日期
	private String targetTimeStart;
	private String targetTimeEnd;
	private Date rewardAccountTime;//奖励入账日期
	private String rewardAccountTimeStart;
	private String rewardAccountTimeEnd;
	private Date deductTime;//扣款/调账日期
	private String deductTimeStart;
	private String deductTimeEnd;
	private BigDecimal totalAmount;//累计交易金额
	private String operator;//操作人
	private String currentAgentNode;//当前登录代理商节点

	private String rewardStartTime;//奖励考核开始时间
	private String rewardEndTime;//奖励考核结束时间
	private String deductStartTime;//扣款考核开始时间
	private String deductEndTime;//扣款考核结束时间

	private String repeatStatus;//是否重复注册
	private Integer rewardType;
	private Integer rewardMonth;
	private BigDecimal rewardAmountConfig;
	private BigDecimal rewardTotalAmount;
	private Integer deductType;
	private Integer deductMonth;
	private BigDecimal deductAmountConfig;
	private BigDecimal deductTotalAmount;

	private String countTradeScope;

	private String activityTypeNo;//欢乐返子类型
	private Integer hardId;//硬件ID
	private String teamId;//组织
	private String teamEntryId;//子组织
	private String teamName;
	private String teamEntryName;

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

	public String getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}

	public String getRewardAccountStatus() {
		return rewardAccountStatus;
	}

	public void setRewardAccountStatus(String rewardAccountStatus) {
		this.rewardAccountStatus = rewardAccountStatus;
	}

	public String getDeductStatus() {
		return deductStatus;
	}

	public void setDeductStatus(String deductStatus) {
		this.deductStatus = deductStatus;
	}

	public BigDecimal getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(BigDecimal rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public BigDecimal getRewardAmountMin() {
		return rewardAmountMin;
	}

	public void setRewardAmountMin(BigDecimal rewardAmountMin) {
		this.rewardAmountMin = rewardAmountMin;
	}

	public BigDecimal getRewardAmountMax() {
		return rewardAmountMax;
	}

	public void setRewardAmountMax(BigDecimal rewardAmountMax) {
		this.rewardAmountMax = rewardAmountMax;
	}

	public BigDecimal getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(BigDecimal deductAmount) {
		this.deductAmount = deductAmount;
	}

	public BigDecimal getDeductAmountMin() {
		return deductAmountMin;
	}

	public void setDeductAmountMin(BigDecimal deductAmountMin) {
		this.deductAmountMin = deductAmountMin;
	}

	public BigDecimal getDeductAmountMax() {
		return deductAmountMax;
	}

	public void setDeductAmountMax(BigDecimal deductAmountMax) {
		this.deductAmountMax = deductAmountMax;
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

	public Date getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(Date targetTime) {
		this.targetTime = targetTime;
	}

	public String getTargetTimeStart() {
		return targetTimeStart;
	}

	public void setTargetTimeStart(String targetTimeStart) {
		this.targetTimeStart = targetTimeStart;
	}

	public String getTargetTimeEnd() {
		return targetTimeEnd;
	}

	public void setTargetTimeEnd(String targetTimeEnd) {
		this.targetTimeEnd = targetTimeEnd;
	}

	public Date getRewardAccountTime() {
		return rewardAccountTime;
	}

	public void setRewardAccountTime(Date rewardAccountTime) {
		this.rewardAccountTime = rewardAccountTime;
	}

	public String getRewardAccountTimeStart() {
		return rewardAccountTimeStart;
	}

	public void setRewardAccountTimeStart(String rewardAccountTimeStart) {
		this.rewardAccountTimeStart = rewardAccountTimeStart;
	}

	public String getRewardAccountTimeEnd() {
		return rewardAccountTimeEnd;
	}

	public void setRewardAccountTimeEnd(String rewardAccountTimeEnd) {
		this.rewardAccountTimeEnd = rewardAccountTimeEnd;
	}

	public Date getDeductTime() {
		return deductTime;
	}

	public void setDeductTime(Date deductTime) {
		this.deductTime = deductTime;
	}

	public String getDeductTimeStart() {
		return deductTimeStart;
	}

	public void setDeductTimeStart(String deductTimeStart) {
		this.deductTimeStart = deductTimeStart;
	}

	public String getDeductTimeEnd() {
		return deductTimeEnd;
	}

	public void setDeductTimeEnd(String deductTimeEnd) {
		this.deductTimeEnd = deductTimeEnd;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCurrentAgentNode() {
		return currentAgentNode;
	}

	public void setCurrentAgentNode(String currentAgentNode) {
		this.currentAgentNode = currentAgentNode;
	}

	public String getRepeatStatus() {
		return repeatStatus;
	}

	public void setRepeatStatus(String repeatStatus) {
		this.repeatStatus = repeatStatus;
	}

	public Integer getRewardType() {
		return rewardType;
	}

	public void setRewardType(Integer rewardType) {
		this.rewardType = rewardType;
	}

	public Integer getRewardMonth() {
		return rewardMonth;
	}

	public void setRewardMonth(Integer rewardMonth) {
		this.rewardMonth = rewardMonth;
	}

	public BigDecimal getRewardAmountConfig() {
		return rewardAmountConfig;
	}

	public void setRewardAmountConfig(BigDecimal rewardAmountConfig) {
		this.rewardAmountConfig = rewardAmountConfig;
	}

	public BigDecimal getRewardTotalAmount() {
		return rewardTotalAmount;
	}

	public void setRewardTotalAmount(BigDecimal rewardTotalAmount) {
		this.rewardTotalAmount = rewardTotalAmount;
	}

	public Integer getDeductType() {
		return deductType;
	}

	public void setDeductType(Integer deductType) {
		this.deductType = deductType;
	}

	public Integer getDeductMonth() {
		return deductMonth;
	}

	public void setDeductMonth(Integer deductMonth) {
		this.deductMonth = deductMonth;
	}

	public BigDecimal getDeductAmountConfig() {
		return deductAmountConfig;
	}

	public void setDeductAmountConfig(BigDecimal deductAmountConfig) {
		this.deductAmountConfig = deductAmountConfig;
	}

	public BigDecimal getDeductTotalAmount() {
		return deductTotalAmount;
	}

	public void setDeductTotalAmount(BigDecimal deductTotalAmount) {
		this.deductTotalAmount = deductTotalAmount;
	}

	public String getCountTradeScope() {
		return countTradeScope;
	}

	public void setCountTradeScope(String countTradeScope) {
		this.countTradeScope = countTradeScope;
	}

	public String getRewardStartTime() {
		return rewardStartTime;
	}

	public void setRewardStartTime(String rewardStartTime) {
		this.rewardStartTime = rewardStartTime;
	}

	public String getRewardEndTime() {
		return rewardEndTime;
	}

	public void setRewardEndTime(String rewardEndTime) {
		this.rewardEndTime = rewardEndTime;
	}

	public String getDeductStartTime() {
		return deductStartTime;
	}

	public void setDeductStartTime(String deductStartTime) {
		this.deductStartTime = deductStartTime;
	}

	public String getDeductEndTime() {
		return deductEndTime;
	}

	public void setDeductEndTime(String deductEndTime) {
		this.deductEndTime = deductEndTime;
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
