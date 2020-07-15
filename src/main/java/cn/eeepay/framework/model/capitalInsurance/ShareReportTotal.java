package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;

public class ShareReportTotal {
	  private BigDecimal totalAmount;//累计分润金额
	  private BigDecimal accountAmount;//已入账金额
	  private BigDecimal unAccountAmount;//未入账金额
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getAccountAmount() {
		return accountAmount;
	}
	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}
	public BigDecimal getUnAccountAmount() {
		return unAccountAmount;
	}
	public void setUnAccountAmount(BigDecimal unAccountAmount) {
		this.unAccountAmount = unAccountAmount;
	}
	  
}
