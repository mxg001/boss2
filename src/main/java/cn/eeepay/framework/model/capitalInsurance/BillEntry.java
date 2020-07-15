package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

public class BillEntry {
	private Integer id;
	private String batchNo;//对账批次号
	private String insurer;//承保人，保险单位 见数据字典：insurer_number
	private Integer orderType;//订单类型：1 投保 2 退保
	private String productNo;//产品编码
	private String holder;//投保人
	private String acqOrderNo;//上游保险订单号
	private String sysOrderNo;//平台保险订单号
	private BigDecimal acqAmount;//上游渠道含税保费
	private BigDecimal sysAmount;//平台含税保费
	private String acqBillNo;//上游保单号
	private String sysBillNo;//平台保单号
	private BigDecimal insureAmount;//保额
	private String transStatus;//支付状态:SUCCESS：成功,FAILED：失败,INIT：初始化
	private String insureStatus;//平台投保状态 1 成功 2 失败
	private String acqBillStatus;//上游保单状态
	private String sysBillStatus;//平台保单状态
	private Date insureTime;//投保时间
	private Date billTime;//保单生成日期
	private Date effectiveStime;//保险起期
	private Date effectiveEtime;//保险止期
	private Integer checkStatus;//对账状态： 1 核对成功、2 上游单边、3 平台单边，4 金额不符、0 未核对
	private Integer reportStatus;//汇总状态： 1 已汇总、0 未汇总
	private String createPerson;//操作员，创建人
	private Date createTime;//创建时间
	private String remark;//备注
	private String  oneAgentNo;//一级代理商编号
	private String  reportBatchNo;//汇总批次号
	private BigDecimal billAmount;//平台售价
	private Date createTimeEnd;//创建时间
	private Date insureTimeEnd;//投保时间

	private String  oneAgentName;//一级代理商名称
	private Date billTimeBegin;//
	private Date billTimeEed;//


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getInsurer() {
		return insurer;
	}

	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getAcqOrderNo() {
		return acqOrderNo;
	}

	public void setAcqOrderNo(String acqOrderNo) {
		this.acqOrderNo = acqOrderNo;
	}

	public String getSysOrderNo() {
		return sysOrderNo;
	}

	public void setSysOrderNo(String sysOrderNo) {
		this.sysOrderNo = sysOrderNo;
	}

	public BigDecimal getAcqAmount() {
		return acqAmount;
	}

	public void setAcqAmount(BigDecimal acqAmount) {
		this.acqAmount = acqAmount;
	}

	public BigDecimal getSysAmount() {
		return sysAmount;
	}

	public void setSysAmount(BigDecimal sysAmount) {
		this.sysAmount = sysAmount;
	}

	public String getAcqBillNo() {
		return acqBillNo;
	}

	public void setAcqBillNo(String acqBillNo) {
		this.acqBillNo = acqBillNo;
	}

	public String getSysBillNo() {
		return sysBillNo;
	}

	public void setSysBillNo(String sysBillNo) {
		this.sysBillNo = sysBillNo;
	}

	public BigDecimal getInsureAmount() {
		return insureAmount;
	}

	public void setInsureAmount(BigDecimal insureAmount) {
		this.insureAmount = insureAmount;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getInsureStatus() {
		return insureStatus;
	}

	public void setInsureStatus(String insureStatus) {
		this.insureStatus = insureStatus;
	}

	public String getAcqBillStatus() {
		return acqBillStatus;
	}

	public void setAcqBillStatus(String acqBillStatus) {
		this.acqBillStatus = acqBillStatus;
	}

	public String getSysBillStatus() {
		return sysBillStatus;
	}

	public void setSysBillStatus(String sysBillStatus) {
		this.sysBillStatus = sysBillStatus;
	}

	public Date getInsureTime() {
		return insureTime;
	}

	public void setInsureTime(Date insureTime) {
		this.insureTime = insureTime;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public Date getEffectiveStime() {
		return effectiveStime;
	}

	public void setEffectiveStime(Date effectiveStime) {
		this.effectiveStime = effectiveStime;
	}

	public Date getEffectiveEtime() {
		return effectiveEtime;
	}

	public void setEffectiveEtime(Date effectiveEtime) {
		this.effectiveEtime = effectiveEtime;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getReportBatchNo() {
		return reportBatchNo;
	}

	public void setReportBatchNo(String reportBatchNo) {
		this.reportBatchNo = reportBatchNo;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Date getInsureTimeEnd() {
		return insureTimeEnd;
	}

	public void setInsureTimeEnd(Date insureTimeEnd) {
		this.insureTimeEnd = insureTimeEnd;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public Date getBillTimeBegin() {
		return billTimeBegin;
	}

	public void setBillTimeBegin(Date billTimeBegin) {
		this.billTimeBegin = billTimeBegin;
	}

	public Date getBillTimeEed() {
		return billTimeEed;
	}

	public void setBillTimeEed(Date billTimeEed) {
		this.billTimeEed = billTimeEed;
	}
}
