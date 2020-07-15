package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cm_card_info
 * @author	mays
 * @date	2018年4月6日
 */
public class CmCardInfo {

	private Integer id;
	private String userNo;
	private String cardNo;
	private String bankName;
	private String mobileNo;
	private BigDecimal totalAmount;
	private String statementDate;
	private String repaymentDate;
	private Date createTime;
	private String remindTime;
	private Integer cardStatus;
	private Integer isShow;
	private String userName;
	private String mail;
	private String remark;
	private String cardIncreasePossible;
	private String cardHealthScore;
	private Integer taskId;
	private Integer healthId;
	private String password;
	private Date updateTime;
	private String creditScore;

	private String srcOrgId;
	private String orgName;
	private String userMobile;

	private String sCreateTime;
	private String eCreateTime;

	public String getsCreateTime() {
		return sCreateTime;
	}
	public void setsCreateTime(String sCreateTime) {
		this.sCreateTime = sCreateTime;
	}
	public String geteCreateTime() {
		return eCreateTime;
	}
	public void seteCreateTime(String eCreateTime) {
		this.eCreateTime = eCreateTime;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}
	public String getSrcOrgId() {
		return srcOrgId;
	}
	public void setSrcOrgId(String srcOrgId) {
		this.srcOrgId = srcOrgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}
	public Integer getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(Integer cardStatus) {
		this.cardStatus = cardStatus;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCardIncreasePossible() {
		return cardIncreasePossible;
	}
	public void setCardIncreasePossible(String cardIncreasePossible) {
		this.cardIncreasePossible = cardIncreasePossible;
	}
	public String getCardHealthScore() {
		return cardHealthScore;
	}
	public void setCardHealthScore(String cardHealthScore) {
		this.cardHealthScore = cardHealthScore;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getHealthId() {
		return healthId;
	}
	public void setHealthId(Integer healthId) {
		this.healthId = healthId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
