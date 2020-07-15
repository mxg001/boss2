package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动明细
 *
 * @author Administrator
 *
 */
public class ActivityDetail {

	private Integer id;
	private String activityCode;
	private String activiyName;//数据库是这样，将错就错
	private Integer activityId;
	private String merchantNo;
	private String agentNode;
	private Date cashTime;
	private Date createTime;

	private Integer status;
	private String cashOrder;
	private String activeOrder;
	private Date activeTime;
	private Date enterTime;
	private BigDecimal frozenAmout;
	private BigDecimal transTotal;
	private BigDecimal targetAmout;
	private String agentNo;
	private String agentName;
	private String merchantName;//商户名称
	private String agentN;
	private String merchantN;

	private BigDecimal cashBackAmount;//返现金额

	private Integer checkStatus;//核算状态：1：同意，2：不同意，3：未核算
	private Integer discountStatus;//扣回状态：0：未扣回，1：已扣回
	private String checkOperator;//核算人
	private Date checkTime;//核算时间
	private String discountOperator;//扣回操作人
	private Date discountTime;//扣回时间

	private Date activeTimeStart;
	private Date activeTimeEnd;
	private Date enterTimeStart;
	private Date enterTimeEnd;
	private Integer acqOrgId;//收单机构ID
	private String acqEnname;//收单机构英文名称
	private BigDecimal transAmount;//商户交易手续费
	private BigDecimal merchantFee;//商户交易手续费
	private BigDecimal acqMerchantFee;//上游交易手续费
	private String settleTransferId;//出款明细ID
	private BigDecimal merchantFeeAmount;//商户提现费
	private BigDecimal merchantOutAmount;//商户到账金额
	private Date merchantSettleDate;//商户体现时间
	private String oneAgentNo;//一级代理商
	private String oneAgentName;//一级代理商名称

	private String liquidationStatus;// 清算核算状态
	private String liquidationOperator;// 清算核算操作人
	private Date liquidationTime;// 清算操作时间
	private Date liquidationTimeStart;
	private Date liquidationTimeEnd;
	private String accountCheckStatus;// 账务核算状态
	private String accountCheckOperator;// 账务核算操作人
	private Date accountCheckTime;// 账务操作时间

	private String checkStatusStr;
	private String statusStr;
	private String[] activityCodeList;
	/**
	 * 冗余字段，用于查询
	 */
	private Date transTime;//交易时间
	private String checkOperatorName;//核算操作人名称
	private String discountOperatorName;//扣回操作人名称
	private Date cashTimeStart;//提现时间起
	private Date cashTimeEnd;//提现时间止

	private BigDecimal cumulateTransAmount;	//累计交易金额（欢乐返）
	private Date endCumulateTime;		//截止累计日期（欢乐返）
	private BigDecimal cumulateAmountMinus;	//累计交易（扣）（欢乐返）
	private BigDecimal cumulateAmountAdd;	//累计交易（奖）（欢乐返）
	private BigDecimal emptyAmount;			// 未满扣N元（欢乐返）
	private BigDecimal fullAmount;			// 满奖M元（欢乐返）
	private String isStandard;			// 奖励是否达标,0:未达标, 1:已达标（欢乐返）
	private Date standardTime;		// 奖励达标时间（欢乐返）
	private Date minusAmountTime;		// 扣款时间（欢乐返）
	private Date addAmountTime;			// 奖励时间（欢乐返)

	private BigDecimal minCumulateTransAmount;
	private BigDecimal maxCumulateTransAmount;
	private BigDecimal minTransTotal;
	private BigDecimal maxTransTotal;
	private BigDecimal minFullAmount;
	private BigDecimal maxFullAmount;
	private BigDecimal minEmptyAmount;
	private BigDecimal maxEmptyAmount;
	private Date minStandardTime;
	private Date maxStandardTime;
	private Date minMinusAmountTime;
	private Date maxMinusAmountTime;
	private Date minAddAmountTime;
	private Date maxAddAmountTime;

	private Date overdueTime;

	private String agentType;

	private String merchantType; // 查询的商户类型  1.直营商户  2.所有代理商商户  3.所有商户

	private Boolean countAll;//全部数据

	private Boolean pageAll;//当前页

	private String checkIds;//选中的id

	private String batchOrOne;//单个操作还是批量操作 1 单个 2批量

	private String activityTypeName;//欢乐返子类型
	private String activityTypeNo;
	private String recommendedSource;
	private Integer repeatRegister;//是否重复注册 1是0否
	private Date minOverdueTime;
	private BigDecimal cumulateTransMinAmount;
	private Date endCumulateMinTime;
	private Date standardMinTime;

	private Date billingTime;  //欢乐返入账时间

	private String billingMsg;//入账描述

	private Integer billingStatus; //入账状态 0-未入账 1-已入账',

	private BigDecimal cashBackAmountHavePay;//欢乐返已经入账金额统计

	private BigDecimal cashBackAmountNotPay;//欢乐返未入账金额统计

	private Date billingTimeStart;  //欢乐返入账开始时间

	private Date billingTimeEnd;  //欢乐返入账结束时间


	private String countTradeScope;//子类型交易类型过滤

	private Integer hardId;//硬件ID
	private String teamId;//组织
	private String teamEntryId;//子组织
	private String teamName;
	private String teamEntryName;

	private Integer isExclusion; //是否互斥,1:不互斥,0:互斥（欢乐返）


	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public String getActiviyName() {
		return activiyName;
	}

	public void setActiviyName(String activiyName) {
		this.activiyName = activiyName;
	}

	public BigDecimal getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(BigDecimal cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getAgentN() {
		return agentN;
	}

	public void setAgentN(String agentN) {
		this.agentN = agentN;
	}

	public String getMerchantN() {
		return merchantN;
	}

	public void setMerchantN(String merchantN) {
		this.merchantN = merchantN;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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

	public Date getCashTime() {
		return cashTime;
	}

	public void setCashTime(Date cashTime) {
		this.cashTime = cashTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCashOrder() {
		return cashOrder;
	}

	public void setCashOrder(String cashOrder) {
		this.cashOrder = cashOrder;
	}

	public String getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(String activeOrder) {
		this.activeOrder = activeOrder;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	public BigDecimal getFrozenAmout() {
		return frozenAmout;
	}

	public void setFrozenAmout(BigDecimal frozenAmout) {
		this.frozenAmout = frozenAmout;
	}


	public BigDecimal getTransTotal() {
		return transTotal;
	}

	public void setTransTotal(BigDecimal transTotal) {
		this.transTotal = transTotal;
	}

	public BigDecimal getTargetAmout() {
		return targetAmout;
	}

	public void setTargetAmout(BigDecimal targetAmout) {
		this.targetAmout = targetAmout;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getDiscountStatus() {
		return discountStatus;
	}

	public void setDiscountStatus(Integer discountStatus) {
		this.discountStatus = discountStatus;
	}

	public Date getActiveTimeStart() {
		return activeTimeStart;
	}

	public void setActiveTimeStart(Date activeTimeStart) {
		this.activeTimeStart = activeTimeStart;
	}

	public Date getActiveTimeEnd() {
		return activeTimeEnd;
	}

	public void setActiveTimeEnd(Date activeTimeEnd) {
		this.activeTimeEnd = activeTimeEnd;
	}

	public Date getEnterTimeStart() {
		return enterTimeStart;
	}

	public void setEnterTimeStart(Date enterTimeStart) {
		this.enterTimeStart = enterTimeStart;
	}

	public Date getEnterTimeEnd() {
		return enterTimeEnd;
	}

	public void setEnterTimeEnd(Date enterTimeEnd) {
		this.enterTimeEnd = enterTimeEnd;
	}

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public BigDecimal getMerchantFee() {
		return merchantFee;
	}

	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}


	public BigDecimal getMerchantFeeAmount() {
		return merchantFeeAmount;
	}

	public void setMerchantFeeAmount(BigDecimal merchantFeeAmount) {
		this.merchantFeeAmount = merchantFeeAmount;
	}

	public BigDecimal getMerchantOutAmount() {
		return merchantOutAmount;
	}

	public void setMerchantOutAmount(BigDecimal merchantOutAmount) {
		this.merchantOutAmount = merchantOutAmount;
	}

	public Date getMerchantSettleDate() {
		return merchantSettleDate;
	}

	public void setMerchantSettleDate(Date merchantSettleDate) {
		this.merchantSettleDate = merchantSettleDate;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getCheckStatusStr() {
		return checkStatusStr;
	}

	public void setCheckStatusStr(String checkStatusStr) {
		this.checkStatusStr = checkStatusStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getSettleTransferId() {
		return settleTransferId;
	}

	public void setSettleTransferId(String settleTransferId) {
		this.settleTransferId = settleTransferId;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getCheckOperator() {
		return checkOperator;
	}

	public void setCheckOperator(String checkOperator) {
		this.checkOperator = checkOperator;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getDiscountOperator() {
		return discountOperator;
	}

	public void setDiscountOperator(String discountOperator) {
		this.discountOperator = discountOperator;
	}

	public Date getDiscountTime() {
		return discountTime;
	}

	public void setDiscountTime(Date discountTime) {
		this.discountTime = discountTime;
	}

	public String getCheckOperatorName() {
		return checkOperatorName;
	}

	public void setCheckOperatorName(String checkOperatorName) {
		this.checkOperatorName = checkOperatorName;
	}

	public String getDiscountOperatorName() {
		return discountOperatorName;
	}

	public void setDiscountOperatorName(String discountOperatorName) {
		this.discountOperatorName = discountOperatorName;
	}

	public Date getCashTimeStart() {
		return cashTimeStart;
	}

	public void setCashTimeStart(Date cashTimeStart) {
		this.cashTimeStart = cashTimeStart;
	}

	public Date getCashTimeEnd() {
		return cashTimeEnd;
	}

	public void setCashTimeEnd(Date cashTimeEnd) {
		this.cashTimeEnd = cashTimeEnd;
	}

	public String[] getActivityCodeList() {
		return activityCodeList;
	}

	public void setActivityCodeList(String[] activityCodeList) {
		this.activityCodeList = activityCodeList;
	}

	public String getLiquidationStatus() {
		return liquidationStatus;
	}

	public void setLiquidationStatus(String liquidationStatus) {
		this.liquidationStatus = liquidationStatus;
	}

	public String getLiquidationOperator() {
		return liquidationOperator;
	}

	public void setLiquidationOperator(String liquidationOperator) {
		this.liquidationOperator = liquidationOperator;
	}

	public Date getLiquidationTime() {
		return liquidationTime;
	}

	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public Date getLiquidationTimeStart() {
		return liquidationTimeStart;
	}

	public void setLiquidationTimeStart(Date liquidationTimeStart) {
		this.liquidationTimeStart = liquidationTimeStart;
	}

	public Date getLiquidationTimeEnd() {
		return liquidationTimeEnd;
	}

	public void setLiquidationTimeEnd(Date liquidationTimeEnd) {
		this.liquidationTimeEnd = liquidationTimeEnd;
	}

	public String getAccountCheckStatus() {
		return accountCheckStatus;
	}

	public void setAccountCheckStatus(String accountCheckStatus) {
		this.accountCheckStatus = accountCheckStatus;
	}

	public String getAccountCheckOperator() {
		return accountCheckOperator;
	}

	public void setAccountCheckOperator(String accountCheckOperator) {
		this.accountCheckOperator = accountCheckOperator;
	}

	public Date getAccountCheckTime() {
		return accountCheckTime;
	}

	public void setAccountCheckTime(Date accountCheckTime) {
		this.accountCheckTime = accountCheckTime;
	}

	public Integer getAcqOrgId() {
		return acqOrgId;
	}

	public void setAcqOrgId(Integer acqOrgId) {
		this.acqOrgId = acqOrgId;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public BigDecimal getAcqMerchantFee() {
		return acqMerchantFee;
	}

	public void setAcqMerchantFee(BigDecimal acqMerchantFee) {
		this.acqMerchantFee = acqMerchantFee;
	}

	public BigDecimal getCumulateTransAmount() {
		return cumulateTransAmount;
	}

	public void setCumulateTransAmount(BigDecimal cumulateTransAmount) {
		this.cumulateTransAmount = cumulateTransAmount;
	}

	public Date getEndCumulateTime() {
		return endCumulateTime;
	}

	public void setEndCumulateTime(Date endCumulateTime) {
		this.endCumulateTime = endCumulateTime;
	}

	public BigDecimal getCumulateAmountMinus() {
		return cumulateAmountMinus;
	}

	public void setCumulateAmountMinus(BigDecimal cumulateAmountMinus) {
		this.cumulateAmountMinus = cumulateAmountMinus;
	}

	public BigDecimal getCumulateAmountAdd() {
		return cumulateAmountAdd;
	}

	public void setCumulateAmountAdd(BigDecimal cumulateAmountAdd) {
		this.cumulateAmountAdd = cumulateAmountAdd;
	}

	public BigDecimal getEmptyAmount() {
		return emptyAmount;
	}

	public void setEmptyAmount(BigDecimal emptyAmount) {
		this.emptyAmount = emptyAmount;
	}

	public BigDecimal getFullAmount() {
		return fullAmount;
	}

	public void setFullAmount(BigDecimal fullAmount) {
		this.fullAmount = fullAmount;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public Date getStandardTime() {
		return standardTime;
	}

	public void setStandardTime(Date standardTime) {
		this.standardTime = standardTime;
	}

	public Date getMinusAmountTime() {
		return minusAmountTime;
	}

	public void setMinusAmountTime(Date minusAmountTime) {
		this.minusAmountTime = minusAmountTime;
	}

	public Date getAddAmountTime() {
		return addAmountTime;
	}

	public void setAddAmountTime(Date addAmountTime) {
		this.addAmountTime = addAmountTime;
	}

	public BigDecimal getMinCumulateTransAmount() {
		return minCumulateTransAmount;
	}

	public void setMinCumulateTransAmount(BigDecimal minCumulateTransAmount) {
		this.minCumulateTransAmount = minCumulateTransAmount;
	}

	public BigDecimal getMaxCumulateTransAmount() {
		return maxCumulateTransAmount;
	}

	public void setMaxCumulateTransAmount(BigDecimal maxCumulateTransAmount) {
		this.maxCumulateTransAmount = maxCumulateTransAmount;
	}

	public Date getMinStandardTime() {
		return minStandardTime;
	}

	public void setMinStandardTime(Date minStandardTime) {
		this.minStandardTime = minStandardTime;
	}

	public Date getMaxStandardTime() {
		return maxStandardTime;
	}

	public void setMaxStandardTime(Date maxStandardTime) {
		this.maxStandardTime = maxStandardTime;
	}

	public Date getMinMinusAmountTime() {
		return minMinusAmountTime;
	}

	public void setMinMinusAmountTime(Date minMinusAmountTime) {
		this.minMinusAmountTime = minMinusAmountTime;
	}

	public Date getMaxMinusAmountTime() {
		return maxMinusAmountTime;
	}

	public void setMaxMinusAmountTime(Date maxMinusAmountTime) {
		this.maxMinusAmountTime = maxMinusAmountTime;
	}

	public Date getMinAddAmountTime() {
		return minAddAmountTime;
	}

	public void setMinAddAmountTime(Date minAddAmountTime) {
		this.minAddAmountTime = minAddAmountTime;
	}

	public Date getMaxAddAmountTime() {
		return maxAddAmountTime;
	}

	public void setMaxAddAmountTime(Date maxAddAmountTime) {
		this.maxAddAmountTime = maxAddAmountTime;
	}

	public Date getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(Date overdueTime) {
		this.overdueTime = overdueTime;
	}

	public Boolean getCountAll() {
		return countAll;
	}

	public void setCountAll(Boolean countAll) {
		this.countAll = countAll;
	}

	public Boolean getPageAll() {
		return pageAll;
	}

	public void setPageAll(Boolean pageAll) {
		this.pageAll = pageAll;
	}

	public String getCheckIds() {
		return checkIds;
	}

	public void setCheckIds(String checkIds) {
		this.checkIds = checkIds;
	}

	public String getBatchOrOne() {
		return batchOrOne;
	}

	public void setBatchOrOne(String batchOrOne) {
		this.batchOrOne = batchOrOne;
	}

	public String getRecommendedSource() {
		return recommendedSource;
	}

	public void setRecommendedSource(String recommendedSource) {
		this.recommendedSource = recommendedSource;
	}

	public Integer getRepeatRegister() {
		return repeatRegister;
	}

	public void setRepeatRegister(Integer repeatRegister) {
		this.repeatRegister = repeatRegister;
	}

	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public Date getMinOverdueTime() {
		return minOverdueTime;
	}

	public void setMinOverdueTime(Date minOverdueTime) {
		this.minOverdueTime = minOverdueTime;
	}

	public BigDecimal getCumulateTransMinAmount() {
		return cumulateTransMinAmount;
	}

	public void setCumulateTransMinAmount(BigDecimal cumulateTransMinAmount) {
		this.cumulateTransMinAmount = cumulateTransMinAmount;
	}

	public Date getEndCumulateMinTime() {
		return endCumulateMinTime;
	}

	public void setEndCumulateMinTime(Date endCumulateMinTime) {
		this.endCumulateMinTime = endCumulateMinTime;
	}

	public Date getStandardMinTime() {
		return standardMinTime;
	}

	public void setStandardMinTime(Date standardMinTime) {
		this.standardMinTime = standardMinTime;
	}

	public BigDecimal getCashBackAmountHavePay() {
		return cashBackAmountHavePay;
	}

	public void setCashBackAmountHavePay(BigDecimal cashBackAmountHavePay) {
		this.cashBackAmountHavePay = cashBackAmountHavePay;
	}

	public BigDecimal getCashBackAmountNotPay() {
		return cashBackAmountNotPay;
	}

	public void setCashBackAmountNotPay(BigDecimal cashBackAmountNotPay) {
		this.cashBackAmountNotPay = cashBackAmountNotPay;
	}

	public Date getBillingTime() {
		return billingTime;
	}

	public void setBillingTime(Date billingTime) {
		this.billingTime = billingTime;
	}

	public String getBillingMsg() {
		return billingMsg;
	}

	public void setBillingMsg(String billingMsg) {
		this.billingMsg = billingMsg;
	}

	public Integer getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
	}

	public Date getBillingTimeStart() {
		return billingTimeStart;
	}

	public void setBillingTimeStart(Date billingTimeStart) {
		this.billingTimeStart = billingTimeStart;
	}

	public Date getBillingTimeEnd() {
		return billingTimeEnd;
	}

	public void setBillingTimeEnd(Date billingTimeEnd) {
		this.billingTimeEnd = billingTimeEnd;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getCountTradeScope() {
		return countTradeScope;
	}

	public void setCountTradeScope(String countTradeScope) {
		this.countTradeScope = countTradeScope;
	}

	public BigDecimal getMinTransTotal() {
		return minTransTotal;
	}

	public void setMinTransTotal(BigDecimal minTransTotal) {
		this.minTransTotal = minTransTotal;
	}

	public BigDecimal getMaxTransTotal() {
		return maxTransTotal;
	}

	public void setMaxTransTotal(BigDecimal maxTransTotal) {
		this.maxTransTotal = maxTransTotal;
	}

	public BigDecimal getMinFullAmount() {
		return minFullAmount;
	}

	public void setMinFullAmount(BigDecimal minFullAmount) {
		this.minFullAmount = minFullAmount;
	}

	public BigDecimal getMaxFullAmount() {
		return maxFullAmount;
	}

	public void setMaxFullAmount(BigDecimal maxFullAmount) {
		this.maxFullAmount = maxFullAmount;
	}

	public BigDecimal getMinEmptyAmount() {
		return minEmptyAmount;
	}

	public void setMinEmptyAmount(BigDecimal minEmptyAmount) {
		this.minEmptyAmount = minEmptyAmount;
	}

	public BigDecimal getMaxEmptyAmount() {
		return maxEmptyAmount;
	}

	public void setMaxEmptyAmount(BigDecimal maxEmptyAmount) {
		this.maxEmptyAmount = maxEmptyAmount;
	}

	public Integer getHardId() {
		return hardId;
	}

	public void setHardId(Integer hardId) {
		this.hardId = hardId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamEntryName() {
		return teamEntryName;
	}

	public void setTeamEntryName(String teamEntryName) {
		this.teamEntryName = teamEntryName;
	}

	public Integer getIsExclusion() {
		return isExclusion;
	}

	public void setIsExclusion(Integer isExclusion) {
		this.isExclusion = isExclusion;
	}
}
