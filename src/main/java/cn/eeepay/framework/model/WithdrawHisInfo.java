package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawHisInfo {

	private Integer id;
	private String orderNo;
	private String merType;
	private String merNo;
	private String service;
	private String serviceOrderNo;
	private String transferOrderNo;
	private String cardNo;
	private String accNo;
	private String accName;
	private String mobileNo;
	private BigDecimal amount;
	private BigDecimal fee;
	private String status;
	private String resultMsg;
	private String orderData;
	private Integer successNotifyNum;
	private String withdrawChannel;
	private Date createTime;
	private Date lastUpdateTime;

	private String nickname;	//昵称
	private String bankOrderNo; //上游订单号
	private String channelName;	//出款渠道
	private String outAccNo;//上游商户号

	private String sAmount;
	private String eAmount;
	private String sCreateTime;
	private String eCreateTime;

	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMerType() {
		return merType;
	}
	public void setMerType(String merType) {
		this.merType = merType;
	}
	public String getMerNo() {
		return merNo;
	}
	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getServiceOrderNo() {
		return serviceOrderNo;
	}
	public void setServiceOrderNo(String serviceOrderNo) {
		this.serviceOrderNo = serviceOrderNo;
	}
	public String getTransferOrderNo() {
		return transferOrderNo;
	}
	public void setTransferOrderNo(String transferOrderNo) {
		this.transferOrderNo = transferOrderNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getOrderData() {
		return orderData;
	}
	public void setOrderData(String orderData) {
		this.orderData = orderData;
	}
	public Integer getSuccessNotifyNum() {
		return successNotifyNum;
	}
	public void setSuccessNotifyNum(Integer successNotifyNum) {
		this.successNotifyNum = successNotifyNum;
	}
	public String getWithdrawChannel() {
		return withdrawChannel;
	}
	public void setWithdrawChannel(String withdrawChannel) {
		this.withdrawChannel = withdrawChannel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getsAmount() {
		return sAmount;
	}
	public void setsAmount(String sAmount) {
		this.sAmount = sAmount;
	}
	public String geteAmount() {
		return eAmount;
	}
	public void seteAmount(String eAmount) {
		this.eAmount = eAmount;
	}
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
	public String getOutAccNo() {
		return outAccNo;
	}
	public void setOutAccNo(String outAccNo) {
		this.outAccNo = outAccNo;
	}
}
