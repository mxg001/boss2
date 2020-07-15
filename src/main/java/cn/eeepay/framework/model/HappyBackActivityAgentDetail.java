package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author rpc
 * @description 欢乐返代理商活动奖励明细查询
 * @date 2019/11/7
 */
public class HappyBackActivityAgentDetail {
	private Integer id;
	private String activeOrder;//激活订单号
	private Integer hlfAgentRewardOrderId;

	private String agentNode;
	private String agentNo;
	private String agentName;
	private String agentLevel;
	private String merchantNo;


	private BigDecimal scanAmount;
	private BigDecimal realAccountAmount;
	private BigDecimal configAmount;
	private String scanAccountStatus;
	private Date scanAccountTime;

	private BigDecimal allAmount;
	private String allAccountStatus;
	private Date allAccountTime;

	private String scanRemark;
	private String allRemark;
	private String operator;

	private Date createTime;

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

	public Integer getHlfAgentRewardOrderId() {
		return hlfAgentRewardOrderId;
	}

	public void setHlfAgentRewardOrderId(Integer hlfAgentRewardOrderId) {
		this.hlfAgentRewardOrderId = hlfAgentRewardOrderId;
	}

	public BigDecimal getScanAmount() {
		return scanAmount;
	}

	public void setScanAmount(BigDecimal scanAmount) {
		this.scanAmount = scanAmount;
	}

	public String getScanAccountStatus() {
		return scanAccountStatus;
	}

	public void setScanAccountStatus(String scanAccountStatus) {
		this.scanAccountStatus = scanAccountStatus;
	}

	public Date getScanAccountTime() {
		return scanAccountTime;
	}

	public void setScanAccountTime(Date scanAccountTime) {
		this.scanAccountTime = scanAccountTime;
	}

	public BigDecimal getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(BigDecimal allAmount) {
		this.allAmount = allAmount;
	}

	public String getAllAccountStatus() {
		return allAccountStatus;
	}

	public void setAllAccountStatus(String allAccountStatus) {
		this.allAccountStatus = allAccountStatus;
	}

	public Date getAllAccountTime() {
		return allAccountTime;
	}

	public void setAllAccountTime(Date allAccountTime) {
		this.allAccountTime = allAccountTime;
	}

	public String getScanRemark() {
		return scanRemark;
	}

	public void setScanRemark(String scanRemark) {
		this.scanRemark = scanRemark;
	}

	public String getAllRemark() {
		return allRemark;
	}

	public void setAllRemark(String allRemark) {
		this.allRemark = allRemark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public BigDecimal getRealAccountAmount() {
		return realAccountAmount;
	}

	public void setRealAccountAmount(BigDecimal realAccountAmount) {
		this.realAccountAmount = realAccountAmount;
	}

	public BigDecimal getConfigAmount() {
		return configAmount;
	}

	public void setConfigAmount(BigDecimal configAmount) {
		this.configAmount = configAmount;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
}
