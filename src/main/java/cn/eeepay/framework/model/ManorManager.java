package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.redTerritory_conf
 */
public class ManorManager {
	private Long id;
	private BigDecimal premiumTerritory;
	private BigDecimal premiumProfit;
	private BigDecimal profitMember;
	private BigDecimal profitManager;
	private BigDecimal profitBanker;
	private BigDecimal premiumOem;
	private BigDecimal territoryPrice;
	private BigDecimal territoryRedProfit;
	private Integer userMaxTerritory;//user_max_territory
	private Integer memberMaxTerritory;//member_max_territory
	private Integer managerMaxTerritory;
	private Integer bankerMaxTerritory;
    private Date updateTime;
    private String updateBy;
    private BigDecimal fenhongTotalMoney;
    private  int fenhongBigNum;
    private String fenhongBigRate;
    private Date fenhongBeginDate;
    private Date fenhongEndDate;
	private String fenhongBeginDateStr;
	private String fenhongEndDateStr;
    private String premiumDividend;
    private  Date premiumBeginDate;
    private Date premiumEndDate;
	private String premiumBeginDateStr;
	private String premiumEndDateStr;
    private String redProfit;
    private String commissionerProfit;
    private String creditCardProfit;
    private String loanProfitProportion;
    private String loanProfit;
    private  String receiptProfit;
    private String insuranceProfit;
    private String bigDataProfit;
    private String payProfit;
    private String cardPaymentProfit;
    private Date benchmarkBeginDate;
    private Date benchmarkEndDate;
	private String benchmarkBeginDateStr;
	private String benchmarkEndDateStr;
	private   BigDecimal tradeFee;
	private  BigDecimal needImageAmount;
	private String fenhongType;
	private  BigDecimal fenhongProportion;
	private BigDecimal fenhongProportionMax;

	public BigDecimal getPremiumTerritory() {
		return premiumTerritory;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPremiumTerritory(BigDecimal premiumTerritory) {
		this.premiumTerritory = premiumTerritory;
	}
	public BigDecimal getPremiumProfit() {
		return premiumProfit;
	}
	public void setPremiumProfit(BigDecimal premiumProfit) {
		this.premiumProfit = premiumProfit;
	}
	public BigDecimal getProfitMember() {
		return profitMember;
	}
	public void setProfitMember(BigDecimal profitMember) {
		this.profitMember = profitMember;
	}
	public BigDecimal getProfitManager() {
		return profitManager;
	}
	public void setProfitManager(BigDecimal profitManager) {
		this.profitManager = profitManager;
	}
	public BigDecimal getProfitBanker() {
		return profitBanker;
	}
	public void setProfitBanker(BigDecimal profitBanker) {
		this.profitBanker = profitBanker;
	}
	public BigDecimal getPremiumOem() {
		return premiumOem;
	}
	public void setPremiumOem(BigDecimal premiumOem) {
		this.premiumOem = premiumOem;
	}
	public BigDecimal getTerritoryPrice() {
		return territoryPrice;
	}
	public void setTerritoryPrice(BigDecimal territoryPrice) {
		this.territoryPrice = territoryPrice;
	}
	public Integer getUserMaxTerritory() {
		return userMaxTerritory;
	}
	public BigDecimal getTerritoryRedProfit() {
		return territoryRedProfit;
	}
	public void setTerritoryRedProfit(BigDecimal territoryRedProfit) {
		this.territoryRedProfit = territoryRedProfit;
	}
	public void setUserMaxTerritory(Integer userMaxTerritory) {
		this.userMaxTerritory = userMaxTerritory;
	}
	public Integer getMemberMaxTerritory() {
		return memberMaxTerritory;
	}
	public void setMemberMaxTerritory(Integer memberMaxTerritory) {
		this.memberMaxTerritory = memberMaxTerritory;
	}
	public Integer getBankerMaxTerritory() {
		return bankerMaxTerritory;
	}
	public Integer getManagerMaxTerritory() {
		return managerMaxTerritory;
	}
	public void setManagerMaxTerritory(Integer managerMaxTerritory) {
		this.managerMaxTerritory = managerMaxTerritory;
	}
	public void setBankerMaxTerritory(Integer bankerMaxTerritory) {
		this.bankerMaxTerritory = bankerMaxTerritory;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public BigDecimal getFenhongTotalMoney() {
		return fenhongTotalMoney;
	}

	public void setFenhongTotalMoney(BigDecimal fenhongTotalMoney) {
		this.fenhongTotalMoney = fenhongTotalMoney;
	}

	public int getFenhongBigNum() {
		return fenhongBigNum;
	}

	public void setFenhongBigNum(int fenhongBigNum) {
		this.fenhongBigNum = fenhongBigNum;
	}

	public String getFenhongBigRate() {
		return fenhongBigRate;
	}

	public void setFenhongBigRate(String fenhongBigRate) {
		this.fenhongBigRate = fenhongBigRate;
	}

	public Date getFenhongBeginDate() {
		return fenhongBeginDate;
	}

	public void setFenhongBeginDate(Date fenhongBeginDate) {
		this.fenhongBeginDate = fenhongBeginDate;
	}

	public Date getFenhongEndDate() {
		return fenhongEndDate;
	}

	public void setFenhongEndDate(Date fenhongEndDate) {
		this.fenhongEndDate = fenhongEndDate;
	}

	public String getPremiumDividend() {
		return premiumDividend;
	}

	public void setPremiumDividend(String premiumDividend) {
		this.premiumDividend = premiumDividend;
	}

	public Date getPremiumBeginDate() {
		return premiumBeginDate;
	}

	public void setPremiumBeginDate(Date premiumBeginDate) {
		this.premiumBeginDate = premiumBeginDate;
	}

	public Date getPremiumEndDate() {
		return premiumEndDate;
	}

	public void setPremiumEndDate(Date premiumEndDate) {
		this.premiumEndDate = premiumEndDate;
	}

	public String getRedProfit() {
		return redProfit;
	}

	public void setRedProfit(String redProfit) {
		this.redProfit = redProfit;
	}

	public String getCommissionerProfit() {
		return commissionerProfit;
	}

	public void setCommissionerProfit(String commissionerProfit) {
		this.commissionerProfit = commissionerProfit;
	}

	public String getCreditCardProfit() {
		return creditCardProfit;
	}

	public void setCreditCardProfit(String creditCardProfit) {
		this.creditCardProfit = creditCardProfit;
	}

	public String getLoanProfitProportion() {
		return loanProfitProportion;
	}

	public void setLoanProfitProportion(String loanProfitProportion) {
		this.loanProfitProportion = loanProfitProportion;
	}

	public String getLoanProfit() {
		return loanProfit;
	}

	public void setLoanProfit(String loanProfit) {
		this.loanProfit = loanProfit;
	}

	public String getReceiptProfit() {
		return receiptProfit;
	}

	public void setReceiptProfit(String receiptProfit) {
		this.receiptProfit = receiptProfit;
	}

	public String getInsuranceProfit() {
		return insuranceProfit;
	}

	public void setInsuranceProfit(String insuranceProfit) {
		this.insuranceProfit = insuranceProfit;
	}

	public String getBigDataProfit() {
		return bigDataProfit;
	}

	public void setBigDataProfit(String bigDataProfit) {
		this.bigDataProfit = bigDataProfit;
	}

	public String getPayProfit() {
		return payProfit;
	}

	public void setPayProfit(String payProfit) {
		this.payProfit = payProfit;
	}

	public String getCardPaymentProfit() {
		return cardPaymentProfit;
	}

	public void setCardPaymentProfit(String cardPaymentProfit) {
		this.cardPaymentProfit = cardPaymentProfit;
	}

	public Date getBenchmarkBeginDate() {
		return benchmarkBeginDate;
	}

	public void setBenchmarkBeginDate(Date benchmarkBeginDate) {
		this.benchmarkBeginDate = benchmarkBeginDate;
	}

	public Date getBenchmarkEndDate() {
		return benchmarkEndDate;
	}

	public void setBenchmarkEndDate(Date benchmarkEndDate) {
		this.benchmarkEndDate = benchmarkEndDate;
	}

	public String getFenhongBeginDateStr() {
		return fenhongBeginDateStr;
	}

	public void setFenhongBeginDateStr(String fenhongBeginDateStr) {
		this.fenhongBeginDateStr = fenhongBeginDateStr;
	}

	public String getFenhongEndDateStr() {
		return fenhongEndDateStr;
	}

	public void setFenhongEndDateStr(String fenhongEndDateStr) {
		this.fenhongEndDateStr = fenhongEndDateStr;
	}

	public String getPremiumBeginDateStr() {
		return premiumBeginDateStr;
	}

	public void setPremiumBeginDateStr(String premiumBeginDateStr) {
		this.premiumBeginDateStr = premiumBeginDateStr;
	}

	public String getPremiumEndDateStr() {
		return premiumEndDateStr;
	}

	public void setPremiumEndDateStr(String premiumEndDateStr) {
		this.premiumEndDateStr = premiumEndDateStr;
	}

	public String getBenchmarkBeginDateStr() {
		return benchmarkBeginDateStr;
	}

	public void setBenchmarkBeginDateStr(String benchmarkBeginDateStr) {
		this.benchmarkBeginDateStr = benchmarkBeginDateStr;
	}

	public String getBenchmarkEndDateStr() {
		return benchmarkEndDateStr;
	}

	public void setBenchmarkEndDateStr(String benchmarkEndDateStr) {
		this.benchmarkEndDateStr = benchmarkEndDateStr;
	}

	public BigDecimal getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(BigDecimal tradeFee) {
		this.tradeFee = tradeFee;
	}

	public BigDecimal getNeedImageAmount() {
		return needImageAmount;
	}

	public void setNeedImageAmount(BigDecimal needImageAmount) {
		this.needImageAmount = needImageAmount;
	}

	public String getFenhongType() {
		return fenhongType;
	}

	public void setFenhongType(String fenhongType) {
		this.fenhongType = fenhongType;
	}

	public BigDecimal getFenhongProportion() {
		return fenhongProportion;
	}

	public void setFenhongProportion(BigDecimal fenhongProportion) {
		this.fenhongProportion = fenhongProportion;
	}

	public BigDecimal getFenhongProportionMax() {
		return fenhongProportionMax;
	}

	public void setFenhongProportionMax(BigDecimal fenhongProportionMax) {
		this.fenhongProportionMax = fenhongProportionMax;
	}
}
