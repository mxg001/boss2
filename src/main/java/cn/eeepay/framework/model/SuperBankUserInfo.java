package cn.eeepay.framework.model;

import cn.eeepay.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级银行家user_info表
 * 
 * @author Administrator
 *
 */
public class
SuperBankUserInfo {

	private Long userId;
	private String userCode;
	private String userName;
	private String nickName;
	private String phone;
	private String idCardNo;
	private String weixinCode;
	private String userType;//代理身份1:普通用户； 2专员；3经理；4银行家'
	private String userLogo;
	private String qrCode;
	private Long orgId;// 组织id
	private String status;
	private String statusAgent;
	private String statusRepayment;
	private String statusReceive;
	private String statusMentor;
	private String statusMentorName;
	private BigDecimal totalProfit;//总收益
	private String createBy;
	private Date createDate;
	private Date toagentDate;
	private String topOneCode;
	private String topTwoCode;
	private String topThreeCode;
	private String repaymentUserNo;//超级还用户编号
	private String receiveUserNo;//收款商户编号
	private String userNode;
	private Date updateDate;
	private String wxOpenId;// 微信唯一标识OPENID
	private String payBack;// 是否退款，1 已退款,0未退款
	private String accountStatus;//是否开户，0：未开户，1：已开户
	private Date toManagerDate;
	private Date toBankerDate;

	private String createDateStr;
	private String toagentDateStr;
	private String createDateStart;
	private String createDateEnd;
	private String orgName;
	private String topOneUserName;
	private String topTwoUserName;
	private String topThreeUserName;
	private String topOneNickName;
	private String topTwoNickName;
	private String topThreeNickName;
	private String topOneUserType;
	private String topTwoUserType;
	private String topThreeUserType;
	private String userLogoUrl;
	private String qrCodeUrl;
	private String itemId;//merchant_business_product，商户进件的ID
	private Integer subUserCount;//直属用户总数
	private Integer subAgentCount;//直属代理总数
	private String payBackStr;//还款状态
	private String toManagerDateStr;
	private String toBankerDateStr;
	private String topOnePhone;//上一级手机号
	private String topTwoPhone;//上二级手机号
	private String topThreePhone;//上三级手机号
	private String payMoneyStatus;//是否缴费，0 未交费，1 已缴费
	private String toagentDateStart;//成为代理时间（支付时间）
	private String toagentDateEnd;
	private String openProvince;//省
	private String openCity;//市
	private String openRegion;//区
	private String remark;//用户备注

	private String operReason;//用户冻结解冻备注原因
	
	public String getOperReason() {
		return operReason;
	}

	public void setOperReason(String operReason) {
		this.operReason = operReason;
	}

	private String isOpen;//是否外放
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getWeixinCode() {
		return weixinCode;
	}

	public void setWeixinCode(String weixinCode) {
		this.weixinCode = weixinCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusAgent() {
		return statusAgent;
	}

	public void setStatusAgent(String statusAgent) {
		this.statusAgent = statusAgent;
	}

	public String getStatusRepayment() {
		return statusRepayment;
	}

	public void setStatusRepayment(String statusRepayment) {
		this.statusRepayment = statusRepayment;
	}

	public String getStatusReceive() {
		return statusReceive;
	}

	public void setStatusReceive(String statusReceive) {
		this.statusReceive = statusReceive;
	}

	public BigDecimal getTotalProfit() {
		return totalProfit != null ? totalProfit : new BigDecimal(0);
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getToagentDate() {
		return toagentDate;
	}

	public void setToagentDate(Date toagentDate) {
		this.toagentDate = toagentDate;
	}

	public String getTopOneCode() {
		return topOneCode;
	}

	public void setTopOneCode(String topOneCode) {
		this.topOneCode = topOneCode;
	}

	public String getTopTwoCode() {
		return topTwoCode;
	}

	public void setTopTwoCode(String topTwoCode) {
		this.topTwoCode = topTwoCode;
	}

	public String getTopThreeCode() {
		return topThreeCode;
	}

	public void setTopThreeCode(String topThreeCode) {
		this.topThreeCode = topThreeCode;
	}

	public String getRepaymentUserNo() {
		return repaymentUserNo;
	}

	public void setRepaymentUserNo(String repaymentUserNo) {
		this.repaymentUserNo = repaymentUserNo;
	}

	public String getReceiveUserNo() {
		return receiveUserNo;
	}

	public void setReceiveUserNo(String receiveUserNo) {
		this.receiveUserNo = receiveUserNo;
	}

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public String getCreateDateStr() {
		return createDate != null ? DateUtil.getLongFormatDate(createDate) : "";
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public String getPayBack() {
		return payBack;
	}

	public void setPayBack(String payBack) {
		this.payBack = payBack;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getToagentDateStr() {
		return toagentDate != null ? DateUtil.getLongFormatDate(toagentDate) : "";
	}

	public void setToagentDateStr(String toagentDateStr) {
		this.toagentDateStr = toagentDateStr;
	}

	public String getTopOneUserName() {
		return topOneUserName;
	}

	public void setTopOneUserName(String topOneUserName) {
		this.topOneUserName = topOneUserName;
	}

	public String getTopTwoUserName() {
		return topTwoUserName;
	}

	public void setTopTwoUserName(String topTwoUserName) {
		this.topTwoUserName = topTwoUserName;
	}

	public String getTopThreeUserName() {
		return topThreeUserName;
	}

	public void setTopThreeUserName(String topThreeUserName) {
		this.topThreeUserName = topThreeUserName;
	}

	public String getTopOneNickName() {
		return topOneNickName;
	}

	public void setTopOneNickName(String topOneNickName) {
		this.topOneNickName = topOneNickName;
	}

	public String getTopTwoNickName() {
		return topTwoNickName;
	}

	public void setTopTwoNickName(String topTwoNickName) {
		this.topTwoNickName = topTwoNickName;
	}

	public String getTopThreeNickName() {
		return topThreeNickName;
	}

	public void setTopThreeNickName(String topThreeNickName) {
		this.topThreeNickName = topThreeNickName;
	}

	public String getTopOneUserType() {
		return topOneUserType;
	}

	public void setTopOneUserType(String topOneUserType) {
		this.topOneUserType = topOneUserType;
	}

	public String getTopTwoUserType() {
		return topTwoUserType;
	}

	public void setTopTwoUserType(String topTwoUserType) {
		this.topTwoUserType = topTwoUserType;
	}

	public String getTopThreeUserType() {
		return topThreeUserType;
	}

	public void setTopThreeUserType(String topThreeUserType) {
		this.topThreeUserType = topThreeUserType;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getSubUserCount() {
		return subUserCount;
	}

	public void setSubUserCount(Integer subUserCount) {
		this.subUserCount = subUserCount;
	}

	public Integer getSubAgentCount() {
		return subAgentCount;
	}

	public void setSubAgentCount(Integer subAgentCount) {
		this.subAgentCount = subAgentCount;
	}

	public String getPayBackStr() {
		return payBackStr;
	}

	public void setPayBackStr(String payBackStr) {
		this.payBackStr = payBackStr;
	}

	public Date getToManagerDate() {
		return toManagerDate;
	}

	public void setToManagerDate(Date toManagerDate) {
		this.toManagerDate = toManagerDate;
	}

	public Date getToBankerDate() {
		return toBankerDate;
	}

	public void setToBankerDate(Date toBankerDate) {
		this.toBankerDate = toBankerDate;
	}

	public String getToManagerDateStr() {
		return toManagerDate != null ? DateUtil.getLongFormatDate(toManagerDate) : "";
	}

	public void setToManagerDateStr(String toManagerDateStr) {
		this.toManagerDateStr = toManagerDateStr;
	}

	public String getToBankerDateStr() {
		return toBankerDate != null ? DateUtil.getLongFormatDate(toBankerDate) : "";
	}

	public void setToBankerDateStr(String toBankerDateStr) {
		this.toBankerDateStr = toBankerDateStr;
	}

	public String getTopOnePhone() {
		return topOnePhone;
	}

	public void setTopOnePhone(String topOnePhone) {
		this.topOnePhone = topOnePhone;
	}

	public String getTopTwoPhone() {
		return topTwoPhone;
	}

	public void setTopTwoPhone(String topTwoPhone) {
		this.topTwoPhone = topTwoPhone;
	}

	public String getTopThreePhone() {
		return topThreePhone;
	}

	public void setTopThreePhone(String topThreePhone) {
		this.topThreePhone = topThreePhone;
	}

	public String getPayMoneyStatus() {
		return payMoneyStatus;
	}

	public void setPayMoneyStatus(String payMoneyStatus) {
		this.payMoneyStatus = payMoneyStatus;
	}

	public String getToagentDateStart() {
		return toagentDateStart;
	}

	public void setToagentDateStart(String toagentDateStart) {
		this.toagentDateStart = toagentDateStart;
	}

	public String getToagentDateEnd() {
		return toagentDateEnd;
	}

	public void setToagentDateEnd(String toagentDateEnd) {
		this.toagentDateEnd = toagentDateEnd;
	}

	public String getOpenProvince() {
		return openProvince;
	}

	public String getStatusMentor() {
		return statusMentor;
	}

	public void setStatusMentor(String statusMentor) {
		this.statusMentor = statusMentor;
	}

	public String getStatusMentorName() {
		return statusMentorName;
	}

	public void setStatusMentorName(String statusMentorName) {
		this.statusMentorName = statusMentorName;
	}

	public void setOpenProvince(String openProvince) {
		this.openProvince = openProvince;
	}

	public String getOpenCity() {
		return openCity;
	}

	public void setOpenCity(String openCity) {
		this.openCity = openCity;
	}

	public String getOpenRegion() {
		return openRegion;
	}

	public void setOpenRegion(String openRegion) {
		this.openRegion = openRegion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
