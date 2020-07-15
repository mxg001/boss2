package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table merchant_card_info
 * desc 商户银行卡表
 */
public class MerchantCardInfo {
    private Long id;

    private String merchantNo;

    private String cardType;

    private String quickPay;

    private String defQuickPay;

    private String defSettleCard;

    private String accountType;

    private String bankName;

    private String cnapsNo;

    private String accountName;

    private String accountNo;
    private String logAccountNo;
    private String status;

    private Date createTime;
    
    private String accountProvince;
    private String accountCity;
    private String accountDistrict;
    private String accountArea;//开户行地区

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getQuickPay() {
        return quickPay;
    }

    public void setQuickPay(String quickPay) {
        this.quickPay = quickPay == null ? null : quickPay.trim();
    }

    public String getDefQuickPay() {
        return defQuickPay;
    }

    public void setDefQuickPay(String defQuickPay) {
        this.defQuickPay = defQuickPay == null ? null : defQuickPay.trim();
    }

    public String getDefSettleCard() {
        return defSettleCard;
    }

    public void setDefSettleCard(String defSettleCard) {
        this.defSettleCard = defSettleCard == null ? null : defSettleCard.trim();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(String cnapsNo) {
        this.cnapsNo = cnapsNo == null ? null : cnapsNo.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAccountProvince() {
		return accountProvince;
	}

	public void setAccountProvince(String accountProvince) {
		this.accountProvince = accountProvince;
	}

	public String getAccountCity() {
		return accountCity;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public String getAccountDistrict() {
		return accountDistrict;
	}

	public void setAccountDistrict(String accountDistrict) {
		this.accountDistrict = accountDistrict;
	}

	public String getLogAccountNo() {
		return logAccountNo;
	}

	public void setLogAccountNo(String logAccountNo) {
		this.logAccountNo = logAccountNo;
	}

	public String getAccountArea() {
		return accountArea;
	}

	public void setAccountArea(String accountArea) {
		this.accountArea = accountArea;
	}
	
}