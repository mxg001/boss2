package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新欢乐返活动订单表 xhlf_activity_order
 * 
 * @author tans
 * @date 2019-09-26
 */
public class XhlfActivityOrder
{
	
	/** 流水号 */
	private Integer id;
	/** 激活订单号 */
	private String activeOrder;
	/** 激活时间 */
	private Date activeTime;
	/** 目标金额 */
	private BigDecimal targetAmount;
	/** 累计交易金额 */
	private BigDecimal totalAmount;
	/** 奖励金额 */
	private BigDecimal rewardAmount;
	/** 考核周期 */
	private String currentCycle;
	/** 当前考核达标状态,0:未开始,1:考核中,2:已达标,3:未达标 */
	private String currentTargetStatus;
	/** 当前考核达标时间 */
	private Date currentTargetTime;
	/** 奖励入账状态,0:未入账,1:已入账 */
	private String rewardAccountStatus;
	/** 奖励入账时间 */
	private Date rewardAccountTime;
	/** 活动达标状态,0:考核中,1:已达标,2:未达标 */
	private String activityTargetStatus;
	/** 活动达标时间 */
	private Date activityTargetTime;
	/** 奖励考核开始时间 */
	private Date rewardStartTime;
	/** 奖励考核结束时间 */
	private Date rewardEndTime;
	/** 商户号 */
	private String merchantNo;
	/** 所属代理商编号 */
	private String agentNo;
	/** 操作人 */
	private String operator;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;
	/** 奖励考核时间 */
	private Date rewardTime;
	/** 交易开始时间 */
	private Date transStartTime;
	/** 交易结束时间 */
	private Date transEndTime;

	private String checkIds;

	private Integer agentTransTotalType;//'累计交易量统计方式,1:只统计所有已绑卡后的交易,2:统计所有POS刷卡交易'

	private String xhlfActivityType;//'活动类型，1：新欢乐送，2：新欢乐送智能版'

	public String getXhlfActivityType() {
		return xhlfActivityType;
	}

	public void setXhlfActivityType(String xhlfActivityType) {
		this.xhlfActivityType = xhlfActivityType;
	}

	public Integer getAgentTransTotalType() {
		return agentTransTotalType;
	}

	public void setAgentTransTotalType(Integer agentTransTotalType) {
		this.agentTransTotalType = agentTransTotalType;
	}

	public String getCheckIds() {
		return checkIds;
	}

	public void setCheckIds(String checkIds) {
		this.checkIds = checkIds;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setActiveOrder(String activeOrder) 
	{
		this.activeOrder = activeOrder;
	}

	public String getActiveOrder() 
	{
		return activeOrder;
	}
	public void setActiveTime(Date activeTime) 
	{
		this.activeTime = activeTime;
	}

	public Date getActiveTime() 
	{
		return activeTime;
	}
	public void setTargetAmount(BigDecimal targetAmount) 
	{
		this.targetAmount = targetAmount;
	}

	public BigDecimal getTargetAmount() 
	{
		return targetAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) 
	{
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalAmount() 
	{
		return totalAmount;
	}
	public void setRewardAmount(BigDecimal rewardAmount) 
	{
		this.rewardAmount = rewardAmount;
	}

	public BigDecimal getRewardAmount() 
	{
		return rewardAmount;
	}
	public void setCurrentCycle(String currentCycle) 
	{
		this.currentCycle = currentCycle;
	}

	public String getCurrentCycle() 
	{
		return currentCycle;
	}
	public void setCurrentTargetStatus(String currentTargetStatus) 
	{
		this.currentTargetStatus = currentTargetStatus;
	}

	public String getCurrentTargetStatus() 
	{
		return currentTargetStatus;
	}
	public void setCurrentTargetTime(Date currentTargetTime) 
	{
		this.currentTargetTime = currentTargetTime;
	}

	public Date getCurrentTargetTime() 
	{
		return currentTargetTime;
	}
	public void setRewardAccountStatus(String rewardAccountStatus) 
	{
		this.rewardAccountStatus = rewardAccountStatus;
	}

	public String getRewardAccountStatus() 
	{
		return rewardAccountStatus;
	}
	public void setRewardAccountTime(Date rewardAccountTime) 
	{
		this.rewardAccountTime = rewardAccountTime;
	}

	public Date getRewardAccountTime() 
	{
		return rewardAccountTime;
	}
	public void setActivityTargetStatus(String activityTargetStatus) 
	{
		this.activityTargetStatus = activityTargetStatus;
	}

	public String getActivityTargetStatus() 
	{
		return activityTargetStatus;
	}
	public void setActivityTargetTime(Date activityTargetTime) 
	{
		this.activityTargetTime = activityTargetTime;
	}

	public Date getActivityTargetTime() 
	{
		return activityTargetTime;
	}
	public void setRewardStartTime(Date rewardStartTime) 
	{
		this.rewardStartTime = rewardStartTime;
	}

	public Date getRewardStartTime() 
	{
		return rewardStartTime;
	}
	public void setRewardEndTime(Date rewardEndTime) 
	{
		this.rewardEndTime = rewardEndTime;
	}

	public Date getRewardEndTime() 
	{
		return rewardEndTime;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setAgentNo(String agentNo) 
	{
		this.agentNo = agentNo;
	}

	public String getAgentNo() 
	{
		return agentNo;
	}
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}

	public String getOperator() 
	{
		return operator;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

	public Date getRewardTime() {
		return rewardTime;
	}

	public void setRewardTime(Date rewardTime) {
		this.rewardTime = rewardTime;
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

	@Override
	public String toString() {
		return "XhlfActivityOrder{" +
				"id=" + id +
				", activeOrder='" + activeOrder + '\'' +
				", activeTime=" + activeTime +
				", targetAmount=" + targetAmount +
				", totalAmount=" + totalAmount +
				", rewardAmount=" + rewardAmount +
				", currentCycle='" + currentCycle + '\'' +
				", currentTargetStatus='" + currentTargetStatus + '\'' +
				", currentTargetTime=" + currentTargetTime +
				", rewardAccountStatus='" + rewardAccountStatus + '\'' +
				", rewardAccountTime=" + rewardAccountTime +
				", activityTargetStatus='" + activityTargetStatus + '\'' +
				", activityTargetTime=" + activityTargetTime +
				", rewardStartTime=" + rewardStartTime +
				", rewardEndTime=" + rewardEndTime +
				", merchantNo='" + merchantNo + '\'' +
				", agentNo='" + agentNo + '\'' +
				", operator='" + operator + '\'' +
				", lastUpdateTime=" + lastUpdateTime +
				", rewardTime=" + rewardTime +
				", transStartTime=" + transStartTime +
				", transEndTime=" + transEndTime +
				", checkIds='" + checkIds + '\'' +
				'}';
	}
}
