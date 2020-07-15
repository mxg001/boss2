package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransInfo {
    private Integer id;

    private String orderNo;

    private String acqEnname;

    private String acqCode;

    private String acqMerchantNo;

    private String acqTerminalNo;

    private String acqAuthNo;

    private String acqReferenceNo;

    private String acqBatchNo;

    private String acqSerialNo;

    private String acqResponseCode;

    private String agentNo;

    private String merchantNo;

    private String terminalNo;

    private String batchNo;

    private String serialNo;

    private String accountNo;

    private String readCard;

    private String cardType;

    private String currencyType;

    private BigDecimal transAmount;

    private BigDecimal merchantFee;

    private String vasRate;

    private String accountSerialNo;

    private String merchantRate;

    private BigDecimal acqMerchantFee;

    private String acqMerchantRate;

    private String transType;

    private String transStatus;

    private String transSource;

    private String oriAcqBatchNo;

    private String oriAcqSerialNo;

    private String oriBatchNo;

    private String oriSerialNo;

    private Date acqSettleDate;

    private Date merchantSettleDate;

    private Integer mySettle;

    private String reviewStatus;

    private Date transTime;

    private Date lastUpdateTime;

    private Date createTime;

    private String settleStatus;

    private Integer belongPay;

    private String settlementMethod;

    private String bagSettle;

    private String transMsg;

    private String cardholderPhone;

    private String transId;

    private String freezeStatus;

    private String signImg;

    private String deviceSn;

    private String signCheckPerson;

    private Date signCheckTime;

    private Integer isIccard;

    private String icMsg;

    private Integer insurance;

    private String expired;

    private String posType;

    private String msgId;

    private Integer accStatus;

    private Integer issuedStatus;

    private String serviceId;

    private BigDecimal singleShareAmount;

    private String acqServiceId;

    private Integer synStatus;
    
    private String mobilephone;
    
    private String terType;
    
    private String payMethod;
    
    private String bpId;

    private String serviceType;
    
    private String smoney;
    
    private String emoney;
    
    private Date sdate;
    
    private Date edate;
    
    private String merchantName;
    
    private String totalNum;
    
    private String totalMoney;
    
    private String acqServiceType;
    private String typeName;
    private String agentName;
    private String oneAgentNo;
    private String acqMerchantName;
    private String businessProductId;
    private String bool;
    private String bpName;
    private String serviceName;

    private String teamId;

    private Integer groupCode;

    public String getVasRate() {
        return vasRate;
    }

    public void setVasRate(String vasRate) {
        this.vasRate = vasRate;
    }

    public String getAccountSerialNo() {
        return accountSerialNo;
    }

    public void setAccountSerialNo(String accountSerialNo) {
        this.accountSerialNo = accountSerialNo;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
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
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname == null ? null : acqEnname.trim();
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode == null ? null : acqCode.trim();
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo == null ? null : acqMerchantNo.trim();
    }

    public String getAcqTerminalNo() {
        return acqTerminalNo;
    }

    public void setAcqTerminalNo(String acqTerminalNo) {
        this.acqTerminalNo = acqTerminalNo == null ? null : acqTerminalNo.trim();
    }

    public String getAcqAuthNo() {
        return acqAuthNo;
    }

    public void setAcqAuthNo(String acqAuthNo) {
        this.acqAuthNo = acqAuthNo == null ? null : acqAuthNo.trim();
    }

    public String getAcqReferenceNo() {
        return acqReferenceNo;
    }

    public void setAcqReferenceNo(String acqReferenceNo) {
        this.acqReferenceNo = acqReferenceNo == null ? null : acqReferenceNo.trim();
    }

    public String getAcqBatchNo() {
        return acqBatchNo;
    }

    public void setAcqBatchNo(String acqBatchNo) {
        this.acqBatchNo = acqBatchNo == null ? null : acqBatchNo.trim();
    }

    public String getAcqSerialNo() {
        return acqSerialNo;
    }

    public void setAcqSerialNo(String acqSerialNo) {
        this.acqSerialNo = acqSerialNo == null ? null : acqSerialNo.trim();
    }

    public String getAcqResponseCode() {
        return acqResponseCode;
    }

    public void setAcqResponseCode(String acqResponseCode) {
        this.acqResponseCode = acqResponseCode == null ? null : acqResponseCode.trim();
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo == null ? null : terminalNo.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getReadCard() {
        return readCard;
    }

    public void setReadCard(String readCard) {
        this.readCard = readCard;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public BigDecimal getMerchantFee() {
        return merchantFee;
    }

    public void setMerchantFee(BigDecimal merchantFee) {
        this.merchantFee = merchantFee;
    }

    public String getMerchantRate() {
        return merchantRate;
    }

    public void setMerchantRate(String merchantRate) {
        this.merchantRate = merchantRate == null ? null : merchantRate.trim();
    }

    public BigDecimal getAcqMerchantFee() {
        return acqMerchantFee;
    }

    public void setAcqMerchantFee(BigDecimal acqMerchantFee) {
        this.acqMerchantFee = acqMerchantFee;
    }

    public String getAcqMerchantRate() {
        return acqMerchantRate;
    }

    public void setAcqMerchantRate(String acqMerchantRate) {
        this.acqMerchantRate = acqMerchantRate == null ? null : acqMerchantRate.trim();
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    public String getTransSource() {
        return transSource;
    }

    public void setTransSource(String transSource) {
        this.transSource = transSource == null ? null : transSource.trim();
    }

    public String getOriAcqBatchNo() {
        return oriAcqBatchNo;
    }

    public void setOriAcqBatchNo(String oriAcqBatchNo) {
        this.oriAcqBatchNo = oriAcqBatchNo == null ? null : oriAcqBatchNo.trim();
    }

    public String getOriAcqSerialNo() {
        return oriAcqSerialNo;
    }

    public void setOriAcqSerialNo(String oriAcqSerialNo) {
        this.oriAcqSerialNo = oriAcqSerialNo == null ? null : oriAcqSerialNo.trim();
    }

    public String getOriBatchNo() {
        return oriBatchNo;
    }

    public void setOriBatchNo(String oriBatchNo) {
        this.oriBatchNo = oriBatchNo == null ? null : oriBatchNo.trim();
    }

    public String getOriSerialNo() {
        return oriSerialNo;
    }

    public void setOriSerialNo(String oriSerialNo) {
        this.oriSerialNo = oriSerialNo == null ? null : oriSerialNo.trim();
    }

    public Date getAcqSettleDate() {
        return acqSettleDate;
    }

    public void setAcqSettleDate(Date acqSettleDate) {
        this.acqSettleDate = acqSettleDate;
    }

    public Date getMerchantSettleDate() {
        return merchantSettleDate;
    }

    public void setMerchantSettleDate(Date merchantSettleDate) {
        this.merchantSettleDate = merchantSettleDate;
    }

    public Integer getMySettle() {
        return mySettle;
    }

    public void setMySettle(Integer mySettle) {
        this.mySettle = mySettle;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus == null ? null : reviewStatus.trim();
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus == null ? null : settleStatus.trim();
    }

    public Integer getBelongPay() {
        return belongPay;
    }

    public void setBelongPay(Integer belongPay) {
        this.belongPay = belongPay;
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod == null ? null : settlementMethod.trim();
    }

    public String getBagSettle() {
        return bagSettle;
    }

    public void setBagSettle(String bagSettle) {
        this.bagSettle = bagSettle == null ? null : bagSettle.trim();
    }

    public String getTransMsg() {
        return transMsg;
    }

    public void setTransMsg(String transMsg) {
        this.transMsg = transMsg == null ? null : transMsg.trim();
    }

    public String getCardholderPhone() {
        return cardholderPhone;
    }

    public void setCardholderPhone(String cardholderPhone) {
        this.cardholderPhone = cardholderPhone == null ? null : cardholderPhone.trim();
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId == null ? null : transId.trim();
    }

    public String getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(String freezeStatus) {
        this.freezeStatus = freezeStatus == null ? null : freezeStatus.trim();
    }

    public String getSignImg() {
        return signImg;
    }

    public void setSignImg(String signImg) {
        this.signImg = signImg == null ? null : signImg.trim();
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn == null ? null : deviceSn.trim();
    }

    public String getSignCheckPerson() {
        return signCheckPerson;
    }

    public void setSignCheckPerson(String signCheckPerson) {
        this.signCheckPerson = signCheckPerson == null ? null : signCheckPerson.trim();
    }

    public Date getSignCheckTime() {
        return signCheckTime;
    }

    public void setSignCheckTime(Date signCheckTime) {
        this.signCheckTime = signCheckTime;
    }

    public Integer getIsIccard() {
        return isIccard;
    }

    public void setIsIccard(Integer isIccard) {
        this.isIccard = isIccard;
    }

    public String getIcMsg() {
        return icMsg;
    }

    public void setIcMsg(String icMsg) {
        this.icMsg = icMsg == null ? null : icMsg.trim();
    }

    public Integer getInsurance() {
        return insurance;
    }

    public void setInsurance(Integer insurance) {
        this.insurance = insurance;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired == null ? null : expired.trim();
    }

    public String getPosType() {
        return posType;
    }

    public void setPosType(String posType) {
        this.posType = posType == null ? null : posType.trim();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    public Integer getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(Integer accStatus) {
        this.accStatus = accStatus;
    }

    public Integer getIssuedStatus() {
        return issuedStatus;
    }

    public void setIssuedStatus(Integer issuedStatus) {
        this.issuedStatus = issuedStatus;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public BigDecimal getSingleShareAmount() {
        return singleShareAmount;
    }

    public void setSingleShareAmount(BigDecimal singleShareAmount) {
        this.singleShareAmount = singleShareAmount;
    }

    public String getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(String acqServiceId) {
        this.acqServiceId = acqServiceId == null ? null : acqServiceId.trim();
    }

    public Integer getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Integer synStatus) {
        this.synStatus = synStatus;
    }

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTerType() {
		return terType;
	}

	public void setTerType(String terType) {
		this.terType = terType;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSmoney() {
		return smoney;
	}

	public void setSmoney(String smoney) {
		this.smoney = smoney;
	}

	public String getEmoney() {
		return emoney;
	}

	public void setEmoney(String emoney) {
		this.emoney = emoney;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getAcqServiceType() {
		return acqServiceType;
	}

	public void setAcqServiceType(String acqServiceType) {
		this.acqServiceType = acqServiceType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
	}

	public String getBusinessProductId() {
		return businessProductId;
	}

	public void setBusinessProductId(String businessProductId) {
		this.businessProductId = businessProductId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getBool() {
		return bool;
	}

	public void setBool(String bool) {
		this.bool = bool;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}