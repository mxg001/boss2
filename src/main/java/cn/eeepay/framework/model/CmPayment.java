package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CmPayment {

	private Integer id;
	private String userNo;
	private String tradeNo;
	private String thirdTradeNo;
	private BigDecimal transAmount;
	private String transType;
	private String transSubject;
	private String transBody;
	private String buyerLogonId;
	private String transStatus;
	private String thirdStatus;
	private Date createTime;
	private Date paymentTime;
	private Date expireTime;

	private String orgName;
	private String orgId;
	private String agentNo;
	private String sExpireTime;
	private String eExpireTime;

	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getThirdTradeNo() {
		return thirdTradeNo;
	}
	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransSubject() {
		return transSubject;
	}
	public void setTransSubject(String transSubject) {
		this.transSubject = transSubject;
	}
	public String getTransBody() {
		return transBody;
	}
	public void setTransBody(String transBody) {
		this.transBody = transBody;
	}
	public String getBuyerLogonId() {
		return buyerLogonId;
	}
	public void setBuyerLogonId(String buyerLogonId) {
		this.buyerLogonId = buyerLogonId;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getThirdStatus() {
		return thirdStatus;
	}
	public void setThirdStatus(String thirdStatus) {
		this.thirdStatus = thirdStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getsExpireTime() {
		return sExpireTime;
	}
	public void setsExpireTime(String sExpireTime) {
		this.sExpireTime = sExpireTime;
	}
	public String geteExpireTime() {
		return eExpireTime;
	}
	public void seteExpireTime(String eExpireTime) {
		this.eExpireTime = eExpireTime;
	}

}
