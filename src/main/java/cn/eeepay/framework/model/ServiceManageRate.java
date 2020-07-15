package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class ServiceManageRate {

	private Long id;

    private Long serviceId;

    private String holidaysMark;

    private String cardType;

    private String quotaLevel;

    private String agentNo;

    private String rateType;

    private BigDecimal singleNumAmount;

    private BigDecimal rate;

    private BigDecimal capping;

    private BigDecimal safeLine;

    private String checkStatus;

    private String lockStatus;

    private BigDecimal ladder1Rate;

    private BigDecimal ladder1Max;

    private BigDecimal ladder2Rate;

    private BigDecimal ladder2Max;

    private BigDecimal ladder3Rate;

    private BigDecimal ladder3Max;

    private BigDecimal ladder4Rate;

    private BigDecimal ladder4Max;

    private Integer isGlobal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getQuotaLevel() {
        return quotaLevel;
    }

    public void setQuotaLevel(String quotaLevel) {
        this.quotaLevel = quotaLevel;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getSingleNumAmount() {
        return singleNumAmount;
    }

    public void setSingleNumAmount(BigDecimal singleNumAmount) {
        this.singleNumAmount = singleNumAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getCapping() {
        return capping;
    }

    public void setCapping(BigDecimal capping) {
        this.capping = capping;
    }

    public BigDecimal getSafeLine() {
        return safeLine;
    }

    public void setSafeLine(BigDecimal safeLine) {
        this.safeLine = safeLine;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public BigDecimal getLadder1Rate() {
        return ladder1Rate;
    }

    public void setLadder1Rate(BigDecimal ladder1Rate) {
        this.ladder1Rate = ladder1Rate;
    }

    public BigDecimal getLadder1Max() {
        return ladder1Max;
    }

    public void setLadder1Max(BigDecimal ladder1Max) {
        this.ladder1Max = ladder1Max;
    }

    public BigDecimal getLadder2Rate() {
        return ladder2Rate;
    }

    public void setLadder2Rate(BigDecimal ladder2Rate) {
        this.ladder2Rate = ladder2Rate;
    }

    public BigDecimal getLadder2Max() {
        return ladder2Max;
    }

    public void setLadder2Max(BigDecimal ladder2Max) {
        this.ladder2Max = ladder2Max;
    }

    public BigDecimal getLadder3Rate() {
        return ladder3Rate;
    }

    public void setLadder3Rate(BigDecimal ladder3Rate) {
        this.ladder3Rate = ladder3Rate;
    }

    public BigDecimal getLadder3Max() {
        return ladder3Max;
    }

    public void setLadder3Max(BigDecimal ladder3Max) {
        this.ladder3Max = ladder3Max;
    }

    public BigDecimal getLadder4Rate() {
        return ladder4Rate;
    }

    public void setLadder4Rate(BigDecimal ladder4Rate) {
        this.ladder4Rate = ladder4Rate;
    }

    public BigDecimal getLadder4Max() {
        return ladder4Max;
    }

    public void setLadder4Max(BigDecimal ladder4Max) {
        this.ladder4Max = ladder4Max;
    }

    public Integer getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Integer isGlobal) {
        this.isGlobal = isGlobal;
    }
}