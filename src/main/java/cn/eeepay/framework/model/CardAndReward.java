package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class CardAndReward {

	 private long id;	 
	 private long cardLoanHeartenId;
	 public long getCardLoanHeartenId() {
		return cardLoanHeartenId;
	}
	public void setCardLoanHeartenId(long cardLoanHeartenId) {
		this.cardLoanHeartenId = cardLoanHeartenId;
	}
	private String username;	 // '用户名称',
	 private String phone;	 // '手机号码',
	 private Integer  orderType; //订单类型 2信用卡办理 6 贷款',
	 private String orderTypeName; //订单类型 2信用卡办理 6 贷款',
	 public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	private String orderNo; //订单号
	 private String mechName; //订单所属组织
	 private String orgName; //机构名称
	 private String givenChannel; //赠送渠道
	 private String givenChannelName; //赠送渠道
	 
	 
	 public String getGivenChannelName() {
		return givenChannelName;
	}
	public void setGivenChannelName(String givenChannelName) {
		this.givenChannelName = givenChannelName;
	}
	private String givenType; //赠送类型
	 private Integer givenStatus;//'赠送状态 1为成功 2为失败 3未赠送',
	 private String givenStatusName;//'赠送状态 1为成功 2为失败 3未赠送',
	 public String getGivenStatusName() {
		return givenStatusName;
	}
	public void setGivenStatusName(String givenStatusName) {
		this.givenStatusName = givenStatusName;
	}
	private String operUsername; //操作用户名称
	
	 private int ticketId;//赠送券id
	 private String transAmount;//交易金额
	 
	 private Date transTime;
	 private String stransTime;
	 private String etransTime;

	 private String profit;//'佣金'
	 private String remark;//订单备注
	 private String status;//5 为成功订单 其他为失败订单
	 private String statusName;//5 为成功订单 其他为失败订单
	 
	 public String getStatusName() {
		return statusName;
	 }
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	 }
	
	
	
	private String sendId;//赠送渠道
	 private String sendTypeId;//赠送券类型
	 
	 private String merchantNo;
	 
	 private int effectiveDays;//'有效天数
	 private BigDecimal couponAmount; //优惠券面值
	 
	 public int getEffectiveDays() {
		return effectiveDays;
	}
	public void setEffectiveDays(int effectiveDays) {
		this.effectiveDays = effectiveDays;
	}
	public BigDecimal getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getSendId() {
		return sendId;
	}
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
	public String getSendTypeId() {
		return sendTypeId;
	}
	public void setSendTypeId(String sendTypeId) {
		this.sendTypeId = sendTypeId;
	}
	//操作时间
	 private Date operTime;
	 private String soperTime;
	 private String eoperTime;
	 //创建时间
	 private Date createTime;
	 //更新时间
	 private Date updateTime;
	 private String supdateTime;
	 private String eupdateTime;
	 //赠送成功
	 private Date successTime;
	 private String ssuccessTime;
	 private String esuccessTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMechName() {
		return mechName;
	}
	public void setMechName(String mechName) {
		this.mechName = mechName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getGivenChannel() {
		return givenChannel;
	}
	public void setGivenChannel(String givenChannel) {
		this.givenChannel = givenChannel;
	}
	public String getGivenType() {
		return givenType;
	}
	public void setGivenType(String givenType) {
		this.givenType = givenType;
	}
	public Integer getGivenStatus() {
		return givenStatus;
	}
	public void setGivenStatus(Integer givenStatus) {
		this.givenStatus = givenStatus;
	}
	public String getOperUsername() {
		return operUsername;
	}
	public void setOperUsername(String operUsername) {
		this.operUsername = operUsername;
	}
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getStransTime() {
		return stransTime;
	}
	public void setStransTime(String stransTime) {
		this.stransTime = stransTime;
	}
	public String getEtransTime() {
		return etransTime;
	}
	public void setEtransTime(String etransTime) {
		this.etransTime = etransTime;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	public String getSoperTime() {
		return soperTime;
	}
	public void setSoperTime(String soperTime) {
		this.soperTime = soperTime;
	}
	public String getEoperTime() {
		return eoperTime;
	}
	public void setEoperTime(String eoperTime) {
		this.eoperTime = eoperTime;
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
	public String getSupdateTime() {
		return supdateTime;
	}
	public void setSupdateTime(String supdateTime) {
		this.supdateTime = supdateTime;
	}
	public String getEupdateTime() {
		return eupdateTime;
	}
	public void setEupdateTime(String eupdateTime) {
		this.eupdateTime = eupdateTime;
	}
	public Date getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	public String getSsuccessTime() {
		return ssuccessTime;
	}
	public void setSsuccessTime(String ssuccessTime) {
		this.ssuccessTime = ssuccessTime;
	}
	public String getEsuccessTime() {
		return esuccessTime;
	}
	public void setEsuccessTime(String esuccessTime) {
		this.esuccessTime = esuccessTime;
	}
	 
	 
	

}
