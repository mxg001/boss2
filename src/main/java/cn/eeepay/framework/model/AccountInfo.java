package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class AccountInfo {
	private String accountNo;
    private BigDecimal balance;
    private BigDecimal avaliBalance;
    private BigDecimal controlAmount;
    private BigDecimal settlingAmount;
    private BigDecimal preFreezeAmount;
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getControlAmount() {
		return controlAmount;
	}
	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}
	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}
	public BigDecimal getSettlingAmount() {
		return settlingAmount;
	}
	public void setSettlingAmount(BigDecimal settlingAmount) {
		this.settlingAmount = settlingAmount;
	}
	public BigDecimal getPreFreezeAmount() {
		return preFreezeAmount;
	}
	public void setPreFreezeAmount(BigDecimal preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
	}
    
    
}
