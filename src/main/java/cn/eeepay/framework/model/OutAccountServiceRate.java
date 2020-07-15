package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class OutAccountServiceRate {
    private Integer id;

    private Integer outAccountServiceId;

    private Integer agentRateType;

    private Integer costRateType;

    private BigDecimal singleAmount;

    private BigDecimal rate;

    private BigDecimal capping;

    private BigDecimal safeLine;

    private BigDecimal ladder1Rate;

    private BigDecimal ladder1Max;

    private BigDecimal ladder2Rate;

    private BigDecimal ladder2Max;

    private BigDecimal ladder3Rate;

    private BigDecimal ladder3Max;

    private BigDecimal ladder4Rate;

    private BigDecimal ladder4Max;
    private BigDecimal ladder1SafeLine;
    private BigDecimal ladder2SafeLine;
    private BigDecimal ladder3SafeLine;
    private BigDecimal ladder4SafeLine;
    private Integer effectiveStatus;

    private Date effectiveDate;

    private Date createTime;

    private String createPerson;

	private String serviceRate; // 服务费率

	public String getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(String serviceRate) {
		this.serviceRate = serviceRate;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOutAccountServiceId() {
        return outAccountServiceId;
    }

    public void setOutAccountServiceId(Integer outAccountServiceId) {
        this.outAccountServiceId = outAccountServiceId;
    }

    public Integer getAgentRateType() {
        return agentRateType;
    }

    public void setAgentRateType(Integer agentRateType) {
        this.agentRateType = agentRateType;
    }

    public Integer getCostRateType() {
        return costRateType;
    }

    public void setCostRateType(Integer costRateType) {
        this.costRateType = costRateType;
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

	public BigDecimal getLadder1SafeLine() {
		return ladder1SafeLine;
	}

	public void setLadder1SafeLine(BigDecimal ladder1SafeLine) {
		this.ladder1SafeLine = ladder1SafeLine;
	}

	public BigDecimal getLadder2SafeLine() {
		return ladder2SafeLine;
	}

	public void setLadder2SafeLine(BigDecimal ladder2SafeLine) {
		this.ladder2SafeLine = ladder2SafeLine;
	}

	public BigDecimal getLadder3SafeLine() {
		return ladder3SafeLine;
	}

	public void setLadder3SafeLine(BigDecimal ladder3SafeLine) {
		this.ladder3SafeLine = ladder3SafeLine;
	}

	public BigDecimal getLadder4SafeLine() {
		return ladder4SafeLine;
	}

	public void setLadder4SafeLine(BigDecimal ladder4SafeLine) {
		this.ladder4SafeLine = ladder4SafeLine;
	}
}