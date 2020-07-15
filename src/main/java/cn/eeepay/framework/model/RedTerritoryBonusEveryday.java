package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class RedTerritoryBonusEveryday {
    private Long id;

    private String profitDate;

    private Long territoryId;

    private String provinceName;

    private String cityName;

    private String regionName;

    private BigDecimal territoryAvgPrice;

    private BigDecimal territoryPrice;

    private BigDecimal adjustRatio;

    private BigDecimal profitAmount;

    private Long businessBonusCount;

    private BigDecimal businessBasicBonusAmount;

    private BigDecimal businessBonusAmount;

    private BigDecimal premiumTotalAmount;

    private BigDecimal premiumTotalAmountConf;

    private String premiumTotalAmountConfStr;

    private BigDecimal premiumBonusAmount;

    private BigDecimal premiumTradeBasicAmount;

    private BigDecimal premiumTradeAmount;

    private BigDecimal randomBonusTotalAmount;

    private BigDecimal randomBonusAmount;

    private BigDecimal randomBonusBasicAmount;

    private String userCode;

    private Long orgId;

    private Byte status;

    private Date receiveTime;
    private String profitDateStart;
    private  String profitDateEnd;
    private Date createTime;

    private String createBy;
    private Date updateTime;
    private String updateBy;
    private String orgName;
    private String phone;
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(String profitDate) {
        this.profitDate = profitDate;
    }

    public Long getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Long territoryId) {
        this.territoryId = territoryId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public BigDecimal getTerritoryAvgPrice() {
        return territoryAvgPrice;
    }

    public void setTerritoryAvgPrice(BigDecimal territoryAvgPrice) {
        this.territoryAvgPrice = territoryAvgPrice;
    }

    public BigDecimal getTerritoryPrice() {
        return territoryPrice;
    }

    public void setTerritoryPrice(BigDecimal territoryPrice) {
        this.territoryPrice = territoryPrice;
    }

    public BigDecimal getAdjustRatio() {
        return adjustRatio;
    }

    public void setAdjustRatio(BigDecimal adjustRatio) {
        this.adjustRatio = adjustRatio;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public Long getBusinessBonusCount() {
        return businessBonusCount;
    }

    public void setBusinessBonusCount(Long businessBonusCount) {
        this.businessBonusCount = businessBonusCount;
    }

    public BigDecimal getBusinessBasicBonusAmount() {
        return businessBasicBonusAmount;
    }

    public void setBusinessBasicBonusAmount(BigDecimal businessBasicBonusAmount) {
        this.businessBasicBonusAmount = businessBasicBonusAmount;
    }

    public BigDecimal getBusinessBonusAmount() {
        return businessBonusAmount;
    }

    public void setBusinessBonusAmount(BigDecimal businessBonusAmount) {
        this.businessBonusAmount = businessBonusAmount;
    }

    public BigDecimal getPremiumTotalAmount() {
        return premiumTotalAmount;
    }

    public void setPremiumTotalAmount(BigDecimal premiumTotalAmount) {
        this.premiumTotalAmount = premiumTotalAmount;
    }

    public BigDecimal getPremiumTotalAmountConf() {
        return premiumTotalAmountConf;
    }

    public void setPremiumTotalAmountConf(BigDecimal premiumTotalAmountConf) {
        this.premiumTotalAmountConf = premiumTotalAmountConf;
    }

    public BigDecimal getPremiumBonusAmount() {
        return premiumBonusAmount;
    }

    public void setPremiumBonusAmount(BigDecimal premiumBonusAmount) {
        this.premiumBonusAmount = premiumBonusAmount;
    }

    public BigDecimal getPremiumTradeBasicAmount() {
        return premiumTradeBasicAmount;
    }

    public void setPremiumTradeBasicAmount(BigDecimal premiumTradeBasicAmount) {
        this.premiumTradeBasicAmount = premiumTradeBasicAmount;
    }

    public BigDecimal getPremiumTradeAmount() {
        return premiumTradeAmount;
    }

    public void setPremiumTradeAmount(BigDecimal premiumTradeAmount) {
        this.premiumTradeAmount = premiumTradeAmount;
    }

    public BigDecimal getRandomBonusTotalAmount() {
        return randomBonusTotalAmount;
    }

    public void setRandomBonusTotalAmount(BigDecimal randomBonusTotalAmount) {
        this.randomBonusTotalAmount = randomBonusTotalAmount;
    }

    public BigDecimal getRandomBonusAmount() {
        return randomBonusAmount;
    }

    public void setRandomBonusAmount(BigDecimal randomBonusAmount) {
        this.randomBonusAmount = randomBonusAmount;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getProfitDateStart() {
        return profitDateStart;
    }

    public void setProfitDateStart(String profitDateStart) {
        this.profitDateStart = profitDateStart;
    }

    public String getProfitDateEnd() {
        return profitDateEnd;
    }

    public void setProfitDateEnd(String profitDateEnd) {
        this.profitDateEnd = profitDateEnd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPremiumTotalAmountConfStr() {
        return premiumTotalAmountConfStr;
    }

    public void setPremiumTotalAmountConfStr(String premiumTotalAmountConfStr) {
        this.premiumTotalAmountConfStr = premiumTotalAmountConfStr;
    }

    public BigDecimal getRandomBonusBasicAmount() {
        return randomBonusBasicAmount;
    }

    public void setRandomBonusBasicAmount(BigDecimal randomBonusBasicAmount) {
        this.randomBonusBasicAmount = randomBonusBasicAmount;
    }
}
