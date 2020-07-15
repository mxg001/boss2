package cn.eeepay.framework.model;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 新欢乐返活动商户奖励订单表 xhlf_activity_merchant_order
 * 
 * @author tans
 * @date 2019-09-27
 */
public class XhlfActivityMerchantOrder
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
	/** 激活后商户累计交易天数 */
	private Integer merchantLimitDays;
	/** 是否重复注册,0:否,1:是 */
	private Integer repeatStatus;
	/** 配置的奖励金额 */
	private BigDecimal rewardAmountConfig;
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

	private String xhlfActivityType;//'活动类型，1：新欢乐送，2：新欢乐送智能版'

	public String getXhlfActivityType() {
		return xhlfActivityType;
	}

	public void setXhlfActivityType(String xhlfActivityType) {
		this.xhlfActivityType = xhlfActivityType;
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
	public void setMerchantLimitDays(Integer merchantLimitDays) 
	{
		this.merchantLimitDays = merchantLimitDays;
	}

	public Integer getMerchantLimitDays() 
	{
		return merchantLimitDays;
	}
	public void setRepeatStatus(Integer repeatStatus) 
	{
		this.repeatStatus = repeatStatus;
	}

	public Integer getRepeatStatus() 
	{
		return repeatStatus;
	}
	public void setRewardAmountConfig(BigDecimal rewardAmountConfig) 
	{
		this.rewardAmountConfig = rewardAmountConfig;
	}

	public BigDecimal getRewardAmountConfig() 
	{
		return rewardAmountConfig;
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
}
