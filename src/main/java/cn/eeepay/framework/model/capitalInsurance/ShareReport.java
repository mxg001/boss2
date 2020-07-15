package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

public class ShareReport {
	  private Integer id;
	  private String batchNo;//汇总批次号
	  private String billMonth;//保单创建月份
	  private String oneAgentNo;//一级代理商编号
	  private BigDecimal totalAmount;//保费总额
	  private Integer totalCount;//保单总数
	  private BigDecimal shareRate;//代理商分润百分比
	  private BigDecimal shareAmount;//代理商分润金额
	  private Integer accountStatus;//入账状态： 1 入账成功、2 入账失败 0 未入账
	  private Date accountTime;//入账时间
	  private Integer reportStatus;//汇总状态： 1 已汇总、0 未汇总
	  private String createPerson;//操作员，创建人
	  private Date createTime;//创建时间
	  private String remark;//备注
	  
	  private Date sAccountTime;
	  private Date eAccountTime;
	  private String oneAgentName;
	  private BigDecimal sShareAmount;
	  private BigDecimal eShareAmount;
	  private String agentType;
	  
	  
	  
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public BigDecimal getsShareAmount() {
		return sShareAmount;
	}
	public void setsShareAmount(BigDecimal sShareAmount) {
		this.sShareAmount = sShareAmount;
	}
	public BigDecimal geteShareAmount() {
		return eShareAmount;
	}
	public void seteShareAmount(BigDecimal eShareAmount) {
		this.eShareAmount = eShareAmount;
	}
	public Date getsAccountTime() {
		return sAccountTime;
	}
	public void setsAccountTime(Date sAccountTime) {
		this.sAccountTime = sAccountTime;
	}
	public Date geteAccountTime() {
		return eAccountTime;
	}
	public void seteAccountTime(Date eAccountTime) {
		this.eAccountTime = eAccountTime;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
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
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public BigDecimal getShareRate() {
		return shareRate;
	}
	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate;
	}
	public BigDecimal getShareAmount() {
		return shareAmount;
	}
	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}
	public Integer getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Date getAccountTime() {
		return accountTime;
	}
	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
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
	  
	  
}
