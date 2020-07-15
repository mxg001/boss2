package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class YfbBalance {

	private Integer id;
	private String balanceNo;
	private String merNo;
	private String status;
	private String balanceChannel;
	private BigDecimal balance;
	private BigDecimal balance1;
	private String encryptTotalBalance;
	private BigDecimal freezeAmount;
	private Date createTime;
	private Date lastUpdateTime;
	private Integer rowLock;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getMerNo() {
		return merNo;
	}
	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBalanceChannel() {
		return balanceChannel;
	}
	public void setBalanceChannel(String balanceChannel) {
		this.balanceChannel = balanceChannel;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getBalance1() {
		return balance1;
	}
	public void setBalance1(BigDecimal balance1) {
		this.balance1 = balance1;
	}
	public String getEncryptTotalBalance() {
		return encryptTotalBalance;
	}
	public void setEncryptTotalBalance(String encryptTotalBalance) {
		this.encryptTotalBalance = encryptTotalBalance;
	}
	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}
	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getRowLock() {
		return rowLock;
	}
	public void setRowLock(Integer rowLock) {
		this.rowLock = rowLock;
	}

}
