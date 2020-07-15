package cn.eeepay.framework.model;

import java.util.Date;
import java.util.List;

public class YfbCardManage {

	private Integer id;
	private String cardNo;
	private String accountNo;
	private String accountName;
	private String businessCode;
	private String cardType;
	private String accountType;
	private String bankNo;
	private String bankName;
	private String bankCode;
	private String cnaps;
	private String accountProvince;
	private String accountCity;
	private String accountDistrict;
	private String statementDate;
	private String repaymentDate;
	private String yhkzmUrl;
	private String zhName;
	private String mobileNo;
	private Date createTime;
	private Date lastUpdateTime;

	private String isSettleCard;	//是否结算卡
	private String title;	//标题，界面用

	private List<YfbBindCardRecord> bindCardRecords;



	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCnaps() {
		return cnaps;
	}
	public void setCnaps(String cnaps) {
		this.cnaps = cnaps;
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
	public String getStatementDate() {
		return statementDate;
	}
	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}
	public String getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public String getYhkzmUrl() {
		return yhkzmUrl;
	}
	public void setYhkzmUrl(String yhkzmUrl) {
		this.yhkzmUrl = yhkzmUrl;
	}
	public String getZhName() {
		return zhName;
	}
	public void setZhName(String zhName) {
		this.zhName = zhName;
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
	public String getIsSettleCard() {
		return isSettleCard;
	}
	public void setIsSettleCard(String isSettleCard) {
		this.isSettleCard = isSettleCard;
	}

	public List<YfbBindCardRecord> getBindCardRecords() {
		return bindCardRecords;
	}

	public void setBindCardRecords(List<YfbBindCardRecord> bindCardRecords) {
		this.bindCardRecords = bindCardRecords;
	}
}
