package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cm_bill
 * @author	mays
 * @date	2018年4月8日
 */
public class CmBillInfo {

	private Integer id;
	private String cardNo;
	private Date startDate;
	private Date endDate;
	private Date genDate;
	private BigDecimal repayment;
	private BigDecimal lowestRepayment;
	private Integer limitAmt;
	private Integer refCardId;
	private Integer method;
	private Integer billStatus;
	private Date payDate;
	private String payWay;
	private String orderId;

	private Integer score;
	private Integer cashQuota;
	private String billMonth;
	private String billHealth;
	private String withrawScore;
	private Date reviewTime;
	private String orderMoney;
	private Date createTime;

	private String userNo;
	private String bankName;
	private String userName;
	private String statementDate;
	private String repaymentDate;
	private String mobileNo;

	private String sPayDate;
	private String ePayDate;

	public String getsPayDate() {
		return sPayDate;
	}
	public void setsPayDate(String sPayDate) {
		this.sPayDate = sPayDate;
	}
	public String getePayDate() {
		return ePayDate;
	}
	public void setePayDate(String ePayDate) {
		this.ePayDate = ePayDate;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getCashQuota() {
		return cashQuota;
	}
	public void setCashQuota(Integer cashQuota) {
		this.cashQuota = cashQuota;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getBillHealth() {
		return billHealth;
	}
	public void setBillHealth(String billHealth) {
		this.billHealth = billHealth;
	}
	public String getWithrawScore() {
		return withrawScore;
	}
	public void setWithrawScore(String withrawScore) {
		this.withrawScore = withrawScore;
	}
	public String getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	public Date getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	public String getStatementDate() {
		return statementDate;
	}
	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}
	public String getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getGenDate() {
		return genDate;
	}
	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}
	public BigDecimal getRepayment() {
		return repayment;
	}
	public void setRepayment(BigDecimal repayment) {
		this.repayment = repayment;
	}
	public BigDecimal getLowestRepayment() {
		return lowestRepayment;
	}
	public void setLowestRepayment(BigDecimal lowestRepayment) {
		this.lowestRepayment = lowestRepayment;
	}
	public Integer getLimitAmt() {
		return limitAmt;
	}
	public void setLimitAmt(Integer limitAmt) {
		this.limitAmt = limitAmt;
	}
	public Integer getRefCardId() {
		return refCardId;
	}
	public void setRefCardId(Integer refCardId) {
		this.refCardId = refCardId;
	}
	public Integer getMethod() {
		return method;
	}
	public void setMethod(Integer method) {
		this.method = method;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
