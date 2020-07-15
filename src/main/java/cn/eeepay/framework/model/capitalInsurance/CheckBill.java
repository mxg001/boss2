package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

public class CheckBill {
	private Integer id;
	private String batchNo;//对账批次号
	private String insurer;//承保人，保险单位 见数据字典：insurer_number
	  private Integer orderType;//订单类型：1 投保 2 退保
	  private BigDecimal acqTotalAmount;//上游含税保费总金额
	  private Integer acqTotalCount;//上游对账文件总笔数
	  private Integer acqSuccessCount;//上游对账文件成功笔数
	  private Integer acqFailCount;//上游对账文件失败笔数
	  private BigDecimal sysTotalAmount;//平台含税保费总金额-成本价
	  private Integer sysTotalCount;//平台交易总笔数
	  private Integer sysSuccessCount;//平台对账成功总笔数
	  private Integer sysFailCount;//平台对账失败总笔数
	  private Integer checkStatus;//对账结果： 0 初始化 1成功 2失败
	  private String fileDate;//对账文件日期
	  private Date checkTime;//对账时间
	  private String fileName;//对账文件名称
	  private String createPerson;//操作员，创建人
	  private Date createTime;//创建时间
	  private String remark;//备注
	  
	  
	  private Date createTimeEnd;//创建时间
	  
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
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
	public BigDecimal getAcqTotalAmount() {
		return acqTotalAmount;
	}
	public void setAcqTotalAmount(BigDecimal acqTotalAmount) {
		this.acqTotalAmount = acqTotalAmount;
	}
	public Integer getAcqTotalCount() {
		return acqTotalCount;
	}
	public void setAcqTotalCount(Integer acqTotalCount) {
		this.acqTotalCount = acqTotalCount;
	}
	public Integer getAcqSuccessCount() {
		return acqSuccessCount;
	}
	public void setAcqSuccessCount(Integer acqSuccessCount) {
		this.acqSuccessCount = acqSuccessCount;
	}
	public Integer getAcqFailCount() {
		return acqFailCount;
	}
	public void setAcqFailCount(Integer acqFailCount) {
		this.acqFailCount = acqFailCount;
	}
	public BigDecimal getSysTotalAmount() {
		return sysTotalAmount;
	}
	public void setSysTotalAmount(BigDecimal sysTotalAmount) {
		this.sysTotalAmount = sysTotalAmount;
	}
	public Integer getSysTotalCount() {
		return sysTotalCount;
	}
	public void setSysTotalCount(Integer sysTotalCount) {
		this.sysTotalCount = sysTotalCount;
	}
	public Integer getSysSuccessCount() {
		return sysSuccessCount;
	}
	public void setSysSuccessCount(Integer sysSuccessCount) {
		this.sysSuccessCount = sysSuccessCount;
	}
	public Integer getSysFailCount() {
		return sysFailCount;
	}
	public void setSysFailCount(Integer sysFailCount) {
		this.sysFailCount = sysFailCount;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	  
	  
}
