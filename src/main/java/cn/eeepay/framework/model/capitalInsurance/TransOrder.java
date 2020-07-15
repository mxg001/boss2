package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

public class TransOrder {
	private String orderNo;
	private String holder;
	private BigDecimal insureAmount;
	private BigDecimal acqAmount;
	private BigDecimal sysAmount;
	private BigDecimal billAmount;
	private String sysBillStatus;
	private Date billTime;
	private Date effectiveStime;
	private Date effectiveEtime;
	private String  oneAgentNo;
	private String insurer;
	private Integer orderType;
	private String productNo;
	private String acqBillNo;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public BigDecimal getInsureAmount() {
		return insureAmount;
	}
	public void setInsureAmount(BigDecimal insureAmount) {
		this.insureAmount = insureAmount;
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
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public String getSysBillStatus() {
		return sysBillStatus;
	}
	public void setSysBillStatus(String sysBillStatus) {
		this.sysBillStatus = sysBillStatus;
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
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
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
	public String getAcqBillNo() {
		return acqBillNo;
	}
	public void setAcqBillNo(String acqBillNo) {
		this.acqBillNo = acqBillNo;
	}
	
	

}
