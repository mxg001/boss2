package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CollectiveTransOrder {
	
    private Integer id;
    private String orderNo;
    private String mobileNo;
    private String merchantNo;
    private String merchantType;
    private String address;
    private BigDecimal transAmount;
    private String payMethod;
    private String transStatus;
    private String authCodeStatus;
    private Date transTime;
    private Date createTime;
    private String accountNo;
    private String cardType;
    private String holidaysMark;
    private Integer businessProductId;
    private Integer serviceId;
    private Integer acqOrgId;
    private String acqName;
    private String acqEnname;
    private Integer acqServiceId;
    private BigDecimal merchantFee;
    private String merchantRate;
    private String merchantRateType;
    private String vasRate;
    private BigDecimal acqMerchantFee;
    private String acqMerchantRate;
    private String acqRateType;
    private String agentNode;
    private String settlementMethod;
    private Integer hardwareProduct;
    private String deviceSn;
    private BigDecimal profits1;
    private BigDecimal profits2;
    private BigDecimal profits3;
    private BigDecimal profits4;
    private BigDecimal profits5;
    private BigDecimal profits6;
    private BigDecimal profits7;
    private BigDecimal profits8;
    private BigDecimal profits9;
    private BigDecimal profits10;
    private BigDecimal profits11;
    private BigDecimal profits12;
    private BigDecimal profits13;
    private BigDecimal profits14;
    private BigDecimal profits15;
    private BigDecimal profits16;
    private BigDecimal profits17;
    private BigDecimal profits18;
    private BigDecimal profits19;
    private BigDecimal profits20;
    private String synStatus;
    private String settleMsg;
    private String settleStatus;
    private String freezeStatus;
    private String typeName;
    private String agentName;
    private String oneAgentNo;
    private String bool;
    private String initAgentNo;
    private String acqMerchantName;
    private String bpName;
    private String serviceName;
    private Date sdate;
    private Date edate;
    private String merchantName;
    private String totalNum;
    private String totalMoney;
    private BigDecimal totalMoneys;
    private String agentNo;
    private String mobilephone;
    private String serviceType;
    private String smoney;
    private String emoney;
    private String acqMerchantNo;
    private String acqReferenceNo;
    private String SignImg;
    private String terType;
    private String acqTerminalNo;
    private Integer isIccard;
    private String acqBatchNo;
    private String acqSerialNo;
    private String terminalNo;
    private String batchNo;
    private String serialNo;
    private Date acqSettleDate;
    private Date merchantSettleDate;
    private String currencyType;
    private String settles;//是否出账设置是否结算功能
    private String settleErrCode;
    private String isSettleMethod;
    private String acqAuthNo;
    private String acqServiceType;
    private BigDecimal num;
    private String tisId;
    private String accountSerialNo;
    private String acqId;
    private String account;
    private String amount;
    private String outAmount;
    private String feeAmount;
    private String outActualFee;
    private String actualFee;
    private String deductionFee;

    //活动类型
  	private String activityType;
  	
  	//销售名称
  	private String saleName;
    
    //按交易出账新增  作为查询条件
    private String settleType;//出款类型
    private String settleOrder;//出款订单ID
    private String resMsg;//交易信息
    private String readCard;
    
    private Date transTimeStart;
    private Date transTimeEnd;
    private String remark;
    private String transMsg;
    
    private BigDecimal totalMerchantFee; //交易总手续费
    private BigDecimal totalAmount; //出款总金额
    private BigDecimal totalOutAmount; //到账总金额
    private BigDecimal totalFeeAmount; //出款手续费总金额
    private BigDecimal totalDeductionFee;//抵扣手续费总金额
    private BigDecimal totalMerchantPrice; //自选商户手续费总金额
    private BigDecimal totalDeductionMerFee;//抵扣自选商户手续费总金额
    private BigDecimal totalNPrm;//保费总金额


    private String superPushStatus;//是否是微创业：0 不是 1 是
    private String superPushBpId;//微创业业务产品ID
    private String recommendedSource;//商户推广来源(0:正常,1:为微创业，2：代理商推荐的商户)
    private Integer orderType;//订单类型：订单类型：0 普通订单、1 微创业订单

	private String unionpayMerNo;// 直清银联报备号

	private BigDecimal quickRate;// 云闪付费率
	private BigDecimal quickFee;// 云闪付金额

    private BigDecimal merchantPrice;//自选商户手续费
    private BigDecimal deductionMerFee;//抵扣自选商户手续费
    private BigDecimal actualMerchantPrice;//实际自选商户手续费

    private String groupCode;//集群编号

    private BigDecimal nPrm;//保费

    private String zxRate;//自选行业手续费率

    private Integer zxStatus;//是否自选，0：否，1：是

    private String merAgentNode;//商户代理商节点

    private String paUserNo;//超级盟主用户编号
    private String userNode;//超级盟主用户节点

    private Integer teamId;//组织编号
    private String teamEntryId; // 子组织

    private String transType;// 交易类型
    private Integer transTypeExt;//交易类型 1.标准刷卡 2.优享 3.活动 4.vip刷卡 5.跳转集群


    private Integer tradeCountNum;//交易订单总数
    private int pageFirst;//每页开始第几行
    private int pageSize;//每页大小
    private Integer queryTotalStatus;//是否查询汇总数据，0：否，1：是

    private String delivery;

    private Integer days;// 交易日期至当日间隔天数
    private Integer editState;

    private Integer sourceSysSta;
    private String sourceSys;//是否是代理商推广
    private String profitType;

    public String getVasRate() {
        return vasRate;
    }

    public void setVasRate(String vasRate) {
        this.vasRate = vasRate;
    }

    public BigDecimal getTotalMerchantPrice() {
        return totalMerchantPrice;
    }

    public void setTotalMerchantPrice(BigDecimal totalMerchantPrice) {
        this.totalMerchantPrice = totalMerchantPrice;
    }

    public BigDecimal getTotalDeductionMerFee() {
        return totalDeductionMerFee;
    }

    public void setTotalDeductionMerFee(BigDecimal totalDeductionMerFee) {
        this.totalDeductionMerFee = totalDeductionMerFee;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAuthCodeStatus() {
        return authCodeStatus;
    }

    public void setAuthCodeStatus(String authCodeStatus) {
        this.authCodeStatus = authCodeStatus;
    }

    public BigDecimal getQuickRate() {
		return quickRate;
	}

	public void setQuickRate(BigDecimal quickRate) {
		this.quickRate = quickRate;
	}

	public BigDecimal getQuickFee() {
		return quickFee;
	}

	public void setQuickFee(BigDecimal quickFee) {
		this.quickFee = quickFee;
	}

	public String getUnionpayMerNo() {
		return unionpayMerNo;
	}

	public void setUnionpayMerNo(String unionpayMerNo) {
		this.unionpayMerNo = unionpayMerNo;
	}

	public String getReadCard() {
		return readCard;
	}

	public void setReadCard(String readCard) {
		this.readCard = readCard;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod == null ? null : payMethod.trim();
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public Integer getBusinessProductId() {
        return businessProductId;
    }

    public void setBusinessProductId(Integer businessProductId) {
        this.businessProductId = businessProductId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(Integer acqOrgId) {
        this.acqOrgId = acqOrgId;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName == null ? null : acqName.trim();
    }

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
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

    public String getMerchantRateType() {
        return merchantRateType;
    }

    public void setMerchantRateType(String merchantRateType) {
        this.merchantRateType = merchantRateType == null ? null : merchantRateType.trim();
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

    public String getAcqRateType() {
        return acqRateType;
    }

    public void setAcqRateType(String acqRateType) {
        this.acqRateType = acqRateType == null ? null : acqRateType.trim();
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode == null ? null : agentNode.trim();
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod == null ? null : settlementMethod.trim();
    }

    public Integer getHardwareProduct() {
        return hardwareProduct;
    }

    public void setHardwareProduct(Integer hardwareProduct) {
        this.hardwareProduct = hardwareProduct;
    }

    public BigDecimal getProfits1() {
        return profits1;
    }

    public void setProfits1(BigDecimal profits1) {
        this.profits1 = profits1;
    }

    public BigDecimal getProfits2() {
        return profits2;
    }

    public void setProfits2(BigDecimal profits2) {
        this.profits2 = profits2;
    }

    public BigDecimal getProfits3() {
        return profits3;
    }

    public void setProfits3(BigDecimal profits3) {
        this.profits3 = profits3;
    }

    public BigDecimal getProfits4() {
        return profits4;
    }

    public void setProfits4(BigDecimal profits4) {
        this.profits4 = profits4;
    }

    public BigDecimal getProfits5() {
        return profits5;
    }

    public void setProfits5(BigDecimal profits5) {
        this.profits5 = profits5;
    }

    public BigDecimal getProfits6() {
        return profits6;
    }

    public void setProfits6(BigDecimal profits6) {
        this.profits6 = profits6;
    }

    public BigDecimal getProfits7() {
        return profits7;
    }

    public void setProfits7(BigDecimal profits7) {
        this.profits7 = profits7;
    }

    public BigDecimal getProfits8() {
        return profits8;
    }

    public void setProfits8(BigDecimal profits8) {
        this.profits8 = profits8;
    }

    public BigDecimal getProfits9() {
        return profits9;
    }

    public void setProfits9(BigDecimal profits9) {
        this.profits9 = profits9;
    }

    public BigDecimal getProfits10() {
        return profits10;
    }

    public void setProfits10(BigDecimal profits10) {
        this.profits10 = profits10;
    }

    public BigDecimal getProfits11() {
        return profits11;
    }

    public void setProfits11(BigDecimal profits11) {
        this.profits11 = profits11;
    }

    public BigDecimal getProfits12() {
        return profits12;
    }

    public void setProfits12(BigDecimal profits12) {
        this.profits12 = profits12;
    }

    public BigDecimal getProfits13() {
        return profits13;
    }

    public void setProfits13(BigDecimal profits13) {
        this.profits13 = profits13;
    }

    public BigDecimal getProfits14() {
        return profits14;
    }

    public void setProfits14(BigDecimal profits14) {
        this.profits14 = profits14;
    }

    public BigDecimal getProfits15() {
        return profits15;
    }

    public void setProfits15(BigDecimal profits15) {
        this.profits15 = profits15;
    }

    public BigDecimal getProfits16() {
        return profits16;
    }

    public void setProfits16(BigDecimal profits16) {
        this.profits16 = profits16;
    }

    public BigDecimal getProfits17() {
        return profits17;
    }

    public void setProfits17(BigDecimal profits17) {
        this.profits17 = profits17;
    }

    public BigDecimal getProfits18() {
        return profits18;
    }

    public void setProfits18(BigDecimal profits18) {
        this.profits18 = profits18;
    }

    public BigDecimal getProfits19() {
        return profits19;
    }

    public void setProfits19(BigDecimal profits19) {
        this.profits19 = profits19;
    }

    public BigDecimal getProfits20() {
        return profits20;
    }

    public void setProfits20(BigDecimal profits20) {
        this.profits20 = profits20;
    }

	public String getHolidaysMark() {
		return holidaysMark;
	}

	public void setHolidaysMark(String holidaysMark) {
		this.holidaysMark = holidaysMark;
	}

	public String getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getBool() {
		return bool;
	}

	public void setBool(String bool) {
		this.bool = bool;
	}

	public String getInitAgentNo() {
		return initAgentNo;
	}

	public void setInitAgentNo(String initAgentNo) {
		this.initAgentNo = initAgentNo;
	}

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
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

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTerType() {
		return terType;
	}

	public void setTerType(String terType) {
		this.terType = terType;
	}

	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}

	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}

	public String getAcqReferenceNo() {
		return acqReferenceNo;
	}

	public void setAcqReferenceNo(String acqReferenceNo) {
		this.acqReferenceNo = acqReferenceNo;
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

	public String getSignImg() {
		return SignImg;
	}

	public void setSignImg(String signImg) {
		SignImg = signImg;
	}

	public String getAcqTerminalNo() {
		return acqTerminalNo;
	}

	public void setAcqTerminalNo(String acqTerminalNo) {
		this.acqTerminalNo = acqTerminalNo;
	}

	public String getAcqBatchNo() {
		return acqBatchNo;
	}

	public void setAcqBatchNo(String acqBatchNo) {
		this.acqBatchNo = acqBatchNo;
	}

	public String getAcqSerialNo() {
		return acqSerialNo;
	}

	public void setAcqSerialNo(String acqSerialNo) {
		this.acqSerialNo = acqSerialNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getSettleErrCode() {
		return settleErrCode;
	}

	public void setSettleErrCode(String settleErrCode) {
		this.settleErrCode = settleErrCode;
	}

	public String getAcqAuthNo() {
		return acqAuthNo;
	}

	public void setAcqAuthNo(String acqAuthNo) {
		this.acqAuthNo = acqAuthNo;
	}

	public String getSettles() {
		return settles;
	}

	public void setSettles(String settles) {
		this.settles = settles;
	}

	public String getIsSettleMethod() {
		return isSettleMethod;
	}

	public void setIsSettleMethod(String isSettleMethod) {
		this.isSettleMethod = isSettleMethod;
	}

	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public String getAcqServiceType() {
		return acqServiceType;
	}

	public void setAcqServiceType(String acqServiceType) {
		this.acqServiceType = acqServiceType;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getNum() {
		return num;
	}

	public String getTisId() {
		return tisId;
	}

	public void setTisId(String tisId) {
		this.tisId = tisId;
	}

	public String getSettleMsg() {
		return settleMsg;
	}

	public void setSettleMsg(String settleMsg) {
		this.settleMsg = settleMsg;
	}

	public String getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(String synStatus) {
		this.synStatus = synStatus;
	}

	public String getAccountSerialNo() {
		return accountSerialNo;
	}

	public void setAccountSerialNo(String accountSerialNo) {
		this.accountSerialNo = accountSerialNo;
	}

	public BigDecimal getTotalMoneys() {
		return totalMoneys;
	}

	public void setTotalMoneys(BigDecimal totalMoneys) {
		this.totalMoneys = totalMoneys;
	}

	public String getAcqId() {
		return acqId;
	}

	public void setAcqId(String acqId) {
		this.acqId = acqId;
	}


	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getIsIccard() {
		return isIccard;
	}

	public void setIsIccard(Integer isIccard) {
		this.isIccard = isIccard;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(String outAmount) {
		this.outAmount = outAmount;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getSettleOrder() {
		return settleOrder;
	}

	public void setSettleOrder(String settleOrder) {
		this.settleOrder = settleOrder;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getTransTimeStart() {
		return transTimeStart;
	}

	public void setTransTimeStart(Date transTimeStart) {
		this.transTimeStart = transTimeStart;
	}

	public Date getTransTimeEnd() {
		return transTimeEnd;
	}

	public void setTransTimeEnd(Date transTimeEnd) {
		this.transTimeEnd = transTimeEnd;
	}
	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTransMsg() {
		return transMsg;
	}

	public void setTransMsg(String transMsg) {
		this.transMsg = transMsg;
	}

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public String getOutActualFee() {
        return outActualFee;
    }

    public void setOutActualFee(String outActualFee) {
        this.outActualFee = outActualFee;
    }

    public String getDeductionFee() {
        return deductionFee;
    }

    public void setDeductionFee(String deductionFee) {
        this.deductionFee = deductionFee;
    }

	public BigDecimal getTotalMerchantFee() {
		return totalMerchantFee;
	}

	public void setTotalMerchantFee(BigDecimal totalMerchantFee) {
		this.totalMerchantFee = totalMerchantFee;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalOutAmount() {
		return totalOutAmount;
	}

	public void setTotalOutAmount(BigDecimal totalOutAmount) {
		this.totalOutAmount = totalOutAmount;
	}

	public BigDecimal getTotalFeeAmount() {
		return totalFeeAmount;
	}

	public void setTotalFeeAmount(BigDecimal totalFeeAmount) {
		this.totalFeeAmount = totalFeeAmount;
	}

	public BigDecimal getTotalDeductionFee() {
		return totalDeductionFee;
	}

	public void setTotalDeductionFee(BigDecimal totalDeductionFee) {
		this.totalDeductionFee = totalDeductionFee;
	}

	public String getSuperPushStatus() {
		return superPushStatus;
	}

	public void setSuperPushStatus(String superPushStatus) {
		this.superPushStatus = superPushStatus;
	}

	public String getSuperPushBpId() {
		return superPushBpId;
	}

	public void setSuperPushBpId(String superPushBpId) {
		this.superPushBpId = superPushBpId;
	}

	public String getRecommendedSource() {
		return recommendedSource;
	}

	public void setRecommendedSource(String recommendedSource) {
		this.recommendedSource = recommendedSource;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

    public BigDecimal getMerchantPrice() {
        return merchantPrice;
    }

    public void setMerchantPrice(BigDecimal merchantPrice) {
        this.merchantPrice = merchantPrice;
    }

    public BigDecimal getDeductionMerFee() {
        return deductionMerFee;
    }

    public void setDeductionMerFee(BigDecimal deductionMerFee) {
        this.deductionMerFee = deductionMerFee;
    }

    public BigDecimal getActualMerchantPrice() {
        return actualMerchantPrice;
    }

    public void setActualMerchantPrice(BigDecimal actualMerchantPrice) {
        this.actualMerchantPrice = actualMerchantPrice;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public BigDecimal getnPrm() {
        return nPrm;
    }

    public void setnPrm(BigDecimal nPrm) {
        this.nPrm = nPrm;
    }

    public BigDecimal getTotalNPrm() {
        return totalNPrm;
    }

    public void setTotalNPrm(BigDecimal totalNPrm) {
        this.totalNPrm = totalNPrm;
    }

    public String getZxRate() {
        return zxRate;
    }

    public void setZxRate(String zxRate) {
        this.zxRate = zxRate;
    }

    public Integer getZxStatus() {
        return zxStatus;
    }

    public void setZxStatus(Integer zxStatus) {
        this.zxStatus = zxStatus;
    }

    public String getMerAgentNode() {
        return merAgentNode;
    }

    public void setMerAgentNode(String merAgentNode) {
        this.merAgentNode = merAgentNode;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getPaUserNo() {
        return paUserNo;
    }

    public void setPaUserNo(String paUserNo) {
        this.paUserNo = paUserNo;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }

    public Integer getTradeCountNum() {
        return tradeCountNum;
    }

    public void setTradeCountNum(Integer tradeCountNum) {
        this.tradeCountNum = tradeCountNum;
    }

    public int getPageFirst() {
        return pageFirst;
    }

    public void setPageFirst(int pageFirst) {
        this.pageFirst = pageFirst;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public Integer getQueryTotalStatus() {
        return queryTotalStatus;
    }

    public void setQueryTotalStatus(Integer queryTotalStatus) {
        this.queryTotalStatus = queryTotalStatus;
    }

    public Integer getTransTypeExt() {
        return transTypeExt;
    }

    public void setTransTypeExt(Integer transTypeExt) {
        this.transTypeExt = transTypeExt;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Integer getSourceSysSta() {
        return sourceSysSta;
    }

    public void setSourceSysSta(Integer sourceSysSta) {
        this.sourceSysSta = sourceSysSta;
    }

    public String getSourceSys() {
        return sourceSys;
    }

    public void setSourceSys(String sourceSys) {
        this.sourceSys = sourceSys;
    }
    public Integer getEditState() {
        return editState;
    }

    public void setEditState(Integer editState) {
        this.editState = editState;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }
}