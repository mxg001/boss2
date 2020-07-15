package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table settle_order_info
 * desc 出款订单表
 * @author tanghj
 */
public class SettleOrderInfo {
	private String settleOrder;
	private Date createTime;
	private String settleType;
	private String sourceOrderNo;
	private String sourceBatchNo;
	private String sourceSystem;
	private String createUser;
	private String settleUserType;
	private String settleUserNo;
	private String mobilephone;
	private String settleUserName;
	private String settleStatus;
	private String settleMsg;
	private String synStatus;
	private String settleOrderStatus;
	private BigDecimal settleAmount;
	private String agentNode;
	private String holidaysMark;
	private String acqEnname;
	private Date sdate;
	private Date edate;
	private String userName;
	private String status;
	private String mobile;
	private String transId;
	private String id;
	private BigDecimal amount;
	private BigDecimal outAmount;
	private BigDecimal feeAmount;
	private String orderNo;
	private String accountSerialNo;
	private String outServiceName;
	private String statusName;
	private String settleName;
	private String settleStatusName;
	private String account;
	private String outServiceId;
	private Integer subType;//提现类型(1:手刷,2:实体商户,3:欢乐送商户,4:欢乐送代理商,5:代理商分润,6:微创业分润,7:车管家代付)
	private String settleBankName;// '结算卡银行'
	private String settleAccountNo;//'结算卡号'
	private String settleAccountName;//'银行卡户名'
	private BigDecimal deductionFee;//抵扣手续费
	private BigDecimal actualFee;//实际手续费

	private String inAccName;//收款账户名称
	private String unionpayMerNo;//银联报备商户号
	private String inAccNo;//结算卡号
	private String inBankName;//结算银行名称
	private String agentNoOne;//一级代理商编号
	private String agentNameOne;//一级代理商名称

	private BigDecimal perAgentFee;
	
	/**
	 * 出款订单错误信息
	 */
	private String errMsg;

	private Integer historyStatus;//是否查询历史库，0：否，1：是


	private int pageFirst;//没有开始第几行

	private int pageSize;//没页大小大小

	private Integer queryTotalStatus;//是否查询汇总数据，0：否，1：是

	private String delivery;

	private Integer editState;

	public String getInAccNo() {
		return inAccNo;
	}
	public void setInAccNo(String inAccNo) {
		this.inAccNo = inAccNo;
	}
	public String getInBankName() {
		return inBankName;
	}
	public void setInBankName(String inBankName) {
		this.inBankName = inBankName;
	}
	public String getUnionpayMerNo() {
		return unionpayMerNo;
	}
	public void setUnionpayMerNo(String unionpayMerNo) {
		this.unionpayMerNo = unionpayMerNo;
	}
	public String getInAccName() {
		return inAccName;
	}
	public void setInAccName(String inAccName) {
		this.inAccName = inAccName;
	}
	public BigDecimal getDeductionFee() {
		return deductionFee;
	}
	public void setDeductionFee(BigDecimal deductionFee) {
		this.deductionFee = deductionFee;
	}
	public BigDecimal getActualFee() {
		return actualFee;
	}
	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
	}
	public String getSettleOrder() {
		return settleOrder;
	}
	public void setSettleOrder(String settleOrder) {
		this.settleOrder = settleOrder;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getSourceOrderNo() {
		return sourceOrderNo;
	}
	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
	}
	public String getSourceBatchNo() {
		return sourceBatchNo;
	}
	public void setSourceBatchNo(String sourceBatchNo) {
		this.sourceBatchNo = sourceBatchNo;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getSettleUserType() {
		return settleUserType;
	}
	public void setSettleUserType(String settleUserType) {
		this.settleUserType = settleUserType;
	}
	public String getSettleUserNo() {
		return settleUserNo;
	}
	public void setSettleUserNo(String settleUserNo) {
		this.settleUserNo = settleUserNo;
	}
	public String getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
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
	public String getSettleOrderStatus() {
		return settleOrderStatus;
	}
	public void setSettleOrderStatus(String settleOrderStatus) {
		this.settleOrderStatus = settleOrderStatus;
	}
	public BigDecimal getSettleAmount() {
		return settleAmount;
	}
	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getHolidaysMark() {
		return holidaysMark;
	}
	public void setHolidaysMark(String holidaysMark) {
		this.holidaysMark = holidaysMark;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getSettleUserName() {
		return settleUserName;
	}
	public void setSettleUserName(String settleUserName) {
		this.settleUserName = settleUserName;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAccountSerialNo() {
		return accountSerialNo;
	}
	public void setAccountSerialNo(String accountSerialNo) {
		this.accountSerialNo = accountSerialNo;
	}
	public String getOutServiceName() {
		return outServiceName;
	}
	public void setOutServiceName(String outServiceName) {
		this.outServiceName = outServiceName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getSettleName() {
		return settleName;
	}
	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}
	public String getSettleStatusName() {
		return settleStatusName;
	}
	public void setSettleStatusName(String settleStatusName) {
		this.settleStatusName = settleStatusName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOutServiceId() {
		return outServiceId;
	}
	public void setOutServiceId(String outServiceId) {
		this.outServiceId = outServiceId;
	}
	public Integer getSubType() {
		return subType;
	}
	public void setSubType(Integer subType) {
		this.subType = subType;
	}
	public String getSettleBankName() {
		return settleBankName;
	}
	public void setSettleBankName(String settleBankName) {
		this.settleBankName = settleBankName;
	}
	public String getSettleAccountNo() {
		return settleAccountNo;
	}
	public void setSettleAccountNo(String settleAccountNo) {
		this.settleAccountNo = settleAccountNo;
	}
	public String getSettleAccountName() {
		return settleAccountName;
	}
	public void setSettleAccountName(String settleAccountName) {
		this.settleAccountName = settleAccountName;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public BigDecimal getPerAgentFee() {
		return perAgentFee;
	}

	public void setPerAgentFee(BigDecimal perAgentFee) {
		this.perAgentFee = perAgentFee;
	}

	public String getAgentNoOne() {
		return agentNoOne;
	}

	public void setAgentNoOne(String agentNoOne) {
		this.agentNoOne = agentNoOne;
	}

	public String getAgentNameOne() {
		return agentNameOne;
	}

	public void setAgentNameOne(String agentNameOne) {
		this.agentNameOne = agentNameOne;
	}

	public Integer getHistoryStatus() {
		return historyStatus;
	}

	public void setHistoryStatus(Integer historyStatus) {
		this.historyStatus = historyStatus;
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

	public Integer getQueryTotalStatus() {
		return queryTotalStatus;
	}

	public void setQueryTotalStatus(Integer queryTotalStatus) {
		this.queryTotalStatus = queryTotalStatus;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public Integer getEditState() {
		return editState;
	}

	public void setEditState(Integer editState) {
		this.editState = editState;
	}
}
