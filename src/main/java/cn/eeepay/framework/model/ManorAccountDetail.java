package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * superbank.red_account_info
 */
public class ManorAccountDetail {
	
    private Long id;
    private String type;

    private Long relationId;
    
    private String tempId;
    
    public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	private String userName;
    
    private BigDecimal totalAmount;
    
    private Integer selectType;
    
    private String accountName;
    
    private BigDecimal transAmountSum;
	private  String freeMoneyStart;
	private  String freeMoneyEnd;

	public String getFreeMoneyStart() {
		return freeMoneyStart;
	}

	public void setFreeMoneyStart(String freeMoneyStart) {
		this.freeMoneyStart = freeMoneyStart;
	}

	public String getFreeMoneyEnd() {
		return freeMoneyEnd;
	}

	public void setFreeMoneyEnd(String freeMoneyEnd) {
		this.freeMoneyEnd = freeMoneyEnd;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getTransAmountSum() {
		return transAmountSum;
	}

	public void setTransAmountSum(BigDecimal transAmountSum) {
		this.transAmountSum = transAmountSum;
	}

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
    
    
}