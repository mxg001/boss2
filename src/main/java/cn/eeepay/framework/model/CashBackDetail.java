package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商返现明细
 *
 * @author rpc
 *
 */
public class CashBackDetail {

	private Integer id;
	private String agentName;
	private String agentNo;
	private String agentLevel;
	private BigDecimal cashBackAmount;
	private String cashBackSwitch;
	private String entryStatus;
	private Date entryTime;
	private String remark;
	private Integer adId;

	private Date createTime;
	private String agentNode;
	private String activeOrder;

	private String parentId;//上级代理商id
	private String agentCashBackSwitch;//代理商返现开关

	private Integer amountType;
	private Integer preTransferStatus;
	private Date preTransferTime;
	private String fullPrizeSwitch;//满奖开关
	private String notFullDeductSwitch;//不满扣开关


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public BigDecimal getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(BigDecimal cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getCashBackSwitch() {
		return cashBackSwitch;
	}

	public void setCashBackSwitch(String cashBackSwitch) {
		this.cashBackSwitch = cashBackSwitch;
	}

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(String activeOrder) {
		this.activeOrder = activeOrder;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAgentCashBackSwitch() {
		return agentCashBackSwitch;
	}

	public void setAgentCashBackSwitch(String agentCashBackSwitch) {
		this.agentCashBackSwitch = agentCashBackSwitch;
	}

	public Integer getAmountType() {
		return amountType;
	}

	public void setAmountType(Integer amountType) {
		this.amountType = amountType;
	}

	public Integer getPreTransferStatus() {
		return preTransferStatus;
	}

	public void setPreTransferStatus(Integer preTransferStatus) {
		this.preTransferStatus = preTransferStatus;
	}

	public Date getPreTransferTime() {
		return preTransferTime;
	}

	public void setPreTransferTime(Date preTransferTime) {
		this.preTransferTime = preTransferTime;
	}

	public String getFullPrizeSwitch() {
		return fullPrizeSwitch;
	}

	public void setFullPrizeSwitch(String fullPrizeSwitch) {
		this.fullPrizeSwitch = fullPrizeSwitch;
	}

	public String getNotFullDeductSwitch() {
		return notFullDeductSwitch;
	}

	public void setNotFullDeductSwitch(String notFullDeductSwitch) {
		this.notFullDeductSwitch = notFullDeductSwitch;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}
}
