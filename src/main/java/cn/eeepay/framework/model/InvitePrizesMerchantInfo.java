package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvitePrizesMerchantInfo {

	private Integer id;
	private String merchantNo;
	private String agentNode;
	private BigDecimal prizesAmount;
	private String accountStatus;
	private Date accountTime;
	private Date createTime;
	private Date updateTime;
	private String operator;

	private String merchantName;
	private String agentNo;
	private String agentName;
	private String oneLevelId;

	private String containSub;	// 查询条件,是否包含下级
	private String startCreateTime;
	private String endCreateTime;

	private String realName;
	private String prizesType;	//奖励用户类型:1商户,2一级代理商
	private String orderNo;	//交易订单号
	private String prizesObject;	//邀请人商户编号或者一级代理商编号

	public String getPrizesObject() {
		return prizesObject;
	}
	public void setPrizesObject(String prizesObject) {
		this.prizesObject = prizesObject;
	}
	public String getPrizesType() {
		return prizesType;
	}
	public void setPrizesType(String prizesType) {
		this.prizesType = prizesType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getStartCreateTime() {
		return startCreateTime;
	}
	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}
	public String getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	public String getContainSub() {
		return containSub;
	}
	public void setContainSub(String containSub) {
		this.containSub = containSub;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public BigDecimal getPrizesAmount() {
		return prizesAmount;
	}
	public void setPrizesAmount(BigDecimal prizesAmount) {
		this.prizesAmount = prizesAmount;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Date getAccountTime() {
		return accountTime;
	}
	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getOneLevelId() {
		return oneLevelId;
	}
	public void setOneLevelId(String oneLevelId) {
		this.oneLevelId = oneLevelId;
	}

}
