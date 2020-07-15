package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新欢乐返代理商入账明细表 xhlf_agent_account_detail
 * 
 * @author tans
 * @date 2019-09-27
 */
public class XhlfAgentAccountDetail
{
	
	/** 流水号 */
	private Integer id;
	/** xhlf_activity_order.id */
	private Integer xhlfActivityOrderId;
	/** 激活订单号 */
	private String activeOrder;
	/** 考核周期 */
	private String currentCycle;
	/** 代理商编号 */
	private String agentNo;
	/** 上级代理商编号 */
	private String parentAgentNo;
	/** 代理商级别 */
	private String agentLevel;
	/** 奖励金额 */
	private BigDecimal amount;
	/** 奖励入账状态,0:未入账,1:已入账,-1:未开始,还没进到入账逻辑 */
	private String accountStatus;
	/** 奖励入账时间 */
	private Date accountTime;
	/** 备注 */
	private String remark;
	/** 操作人 */
	private String operator;
	/** 创建时间 */
	private Date createTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;
	private String oldAccountStatus;
	private String merchantNo;
	private String agentNode;

	private String subType;//欢乐返子类型标识

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setXhlfActivityOrderId(Integer xhlfActivityOrderId) 
	{
		this.xhlfActivityOrderId = xhlfActivityOrderId;
	}

	public Integer getXhlfActivityOrderId() 
	{
		return xhlfActivityOrderId;
	}
	public void setActiveOrder(String activeOrder) 
	{
		this.activeOrder = activeOrder;
	}

	public String getActiveOrder() 
	{
		return activeOrder;
	}
	public void setCurrentCycle(String currentCycle) 
	{
		this.currentCycle = currentCycle;
	}

	public String getCurrentCycle() 
	{
		return currentCycle;
	}
	public void setAgentNo(String agentNo) 
	{
		this.agentNo = agentNo;
	}

	public String getAgentNo() 
	{
		return agentNo;
	}
	public void setAmount(BigDecimal amount) 
	{
		this.amount = amount;
	}

	public BigDecimal getAmount() 
	{
		return amount;
	}
	public void setAccountStatus(String accountStatus) 
	{
		this.accountStatus = accountStatus;
	}

	public String getAccountStatus() 
	{
		return accountStatus;
	}
	public void setAccountTime(Date accountTime) 
	{
		this.accountTime = accountTime;
	}

	public Date getAccountTime() 
	{
		return accountTime;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}

	public String getOperator() 
	{
		return operator;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

	public String getParentAgentNo() {
		return parentAgentNo;
	}

	public void setParentAgentNo(String parentAgentNo) {
		this.parentAgentNo = parentAgentNo;
	}

	public String getOldAccountStatus() {
		return oldAccountStatus;
	}

	public void setOldAccountStatus(String oldAccountStatus) {
		this.oldAccountStatus = oldAccountStatus;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
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

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
}
