package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收单服务费率任务表
 * 
 * @author junhu
 *
 */
public class AcqServiceRateTask {
    private Integer id;

    private Integer acqServiceId;
    
    private Integer acqServiceRateId;

    private Integer rateType;

    private Integer cardRateType;

    private BigDecimal singleAmount;

    private BigDecimal rate;

    private BigDecimal safeLine;

    private BigDecimal capping;

    private BigDecimal ladder1Rate;

    private BigDecimal ladder1Max;

    private BigDecimal ladder2Rate;

    private BigDecimal ladder2Max;

    private BigDecimal ladder3Rate;

    private BigDecimal ladder3Max;

    private BigDecimal ladder4Rate;

    private BigDecimal ladder4Max;

    private Integer effectiveStatus;

    private Date effectiveDate;

    private Date createTime;

    private String createPerson;

	private String serviceRate; // 服务费率

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getAcqServiceId() {
		return acqServiceId;
	}

	public void setAcqServiceId(Integer acqServiceId) {
		this.acqServiceId = acqServiceId;
	}

	public Integer getAcqServiceRateId() {
        return acqServiceRateId;
    }

    public void setAcqServiceRateId(Integer acqServiceRateId) {
        this.acqServiceRateId = acqServiceRateId;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public Integer getCardRateType() {
        return cardRateType;
    }

    public void setCardRateType(Integer cardRateType) {
        this.cardRateType = cardRateType;
    }

    public BigDecimal getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(BigDecimal singleAmount) {
        this.singleAmount = singleAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getSafeLine() {
        return safeLine;
    }

    public void setSafeLine(BigDecimal safeLine) {
        this.safeLine = safeLine;
    }

    public BigDecimal getCapping() {
        return capping;
    }

    public void setCapping(BigDecimal capping) {
        this.capping = capping;
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

    public Integer getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(Integer effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

	public String getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(String serviceRate) {
		this.serviceRate = serviceRate;
	}

}