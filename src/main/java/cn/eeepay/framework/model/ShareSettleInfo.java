package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ShareSettleInfo {
	  private long id;
	  private String shareSettleNo;//分润代付结算单号,
	  private String  orderNo;//来源单号,
	  private String merchantNo;//商户号,
	  private String type;//来源类型：1交易 2出款,
	  private String acqEnname;//收单机构,
	  private String zqMerchantNo;//收单商户号,
	  private BigDecimal amount;//金额,
	  private String status;//状态（0成功 1失败 2待处理  3处理中 4需要重出）,
	  private Date startTime;//创建时间,
	  private Date originTime;//创建时间,
	  private Date endTime;//完成时间,
	  private String orderType;//订单类型，活动类型
	  
	  //****用于接收查询参数*********/
	 private BigDecimal amountMin;
	 private BigDecimal amountMax;
	 private Date startTimeMin;//创建时间,
	 private Date startTimeMax;//创建时间,
	 private Date endTimeMin;//完成时间,
	 private Date endTimeMax;//完成时间,

	private Date originTimeMin;//完成时间,
	private Date originTimeMax;//完成时间,
	private String  opertor;//完成时间,
	private String  terminalNo;//完成时间,

	private String errMsg;//错误消息

	private BigDecimal nPrm;//保费


	public BigDecimal getAmountMin() {
		return amountMin;
	}
	public void setAmountMin(BigDecimal amountMin) {
		this.amountMin = amountMin;
	}
	public BigDecimal getAmountMax() {
		return amountMax;
	}
	public void setAmountMax(BigDecimal amountMax) {
		this.amountMax = amountMax;
	}
	public Date getStartTimeMin() {
		return startTimeMin;
	}
	public void setStartTimeMin(Date startTimeMin) {
		this.startTimeMin = startTimeMin;
	}
	public Date getStartTimeMax() {
		return startTimeMax;
	}
	public void setStartTimeMax(Date startTimeMax) {
		this.startTimeMax = startTimeMax;
	}
	public Date getEndTimeMin() {
		return endTimeMin;
	}
	public void setEndTimeMin(Date endTimeMin) {
		this.endTimeMin = endTimeMin;
	}
	public Date getEndTimeMax() {
		return endTimeMax;
	}
	public void setEndTimeMax(Date endTimeMax) {
		this.endTimeMax = endTimeMax;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getShareSettleNo() {
		return shareSettleNo;
	}
	public void setShareSettleNo(String shareSettleNo) {
		this.shareSettleNo = shareSettleNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getZqMerchantNo() {
		return zqMerchantNo;
	}
	public void setZqMerchantNo(String zqMerchantNo) {
		this.zqMerchantNo = zqMerchantNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getOriginTimeMax() {
		return originTimeMax;
	}

	public void setOriginTimeMax(Date originTimeMax) {
		this.originTimeMax = originTimeMax;
	}

	public Date getOriginTimeMin() {
		return originTimeMin;
	}

	public void setOriginTimeMin(Date originTimeMin) {
		this.originTimeMin = originTimeMin;
	}


	public String getOpertor() {
		return opertor;
	}

	public void setOpertor(String opertor) {
		this.opertor = opertor;
	}

	public Date getOriginTime() {
		return originTime;
	}

	public void setOriginTime(Date originTime) {
		this.originTime = originTime;
	}

	public BigDecimal getnPrm() {
		return nPrm;
	}

	public void setnPrm(BigDecimal nPrm) {
		this.nPrm = nPrm;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
}
