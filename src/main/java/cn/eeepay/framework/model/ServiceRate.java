package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * @author liusha
 * table: service_manage_rate
 * desc:服务管控费率
 */
public class ServiceRate {
    private Long id;

    private Long serviceId;

    private String teamId;
    
    private String serviceName;

    private String holidaysMark;

    private String cardType;

    private String quotaLevel;

    private String agentNo;

    private String rateType;

    private BigDecimal singleNumAmount;

    private BigDecimal rate;

    private BigDecimal capping;

    private BigDecimal safeLine;

    private Integer isGlobal;

    private Integer checkStatus;

    private Integer lockStatus;
    private BigDecimal ladder1Rate;
    private BigDecimal ladder1Max;
    private BigDecimal ladder2Rate;
    private BigDecimal ladder2Max;
    private BigDecimal ladder3Rate;
    private BigDecimal ladder3Max;
    private BigDecimal ladder4Rate;
    private BigDecimal ladder4Max;
    
    private String merRate;//商户费率
    private String bpId; //业务产品id
    private String bpName; //业务产品名称
    private Integer allowIndividualApply;//业务产品允许单独申请，1：是，0：否
    private Integer serviceType;//服务类型
    private Integer serviceType2;//主服务类型
    private Integer isTx;//是否是提现服务，1：是，0：否
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

    public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark == null ? null : holidaysMark.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getQuotaLevel() {
        return quotaLevel;
    }

    public void setQuotaLevel(String quotaLevel) {
        this.quotaLevel = quotaLevel == null ? null : quotaLevel.trim();
    }

    public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo == null ? null :agentNo.trim();
	}

	public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType == null ? null : rateType.trim();
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

    public Integer getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Integer isGlobal) {
		this.isGlobal = isGlobal;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getMerRate() {
		return merRate;
	}

	public void setMerRate(String merRate) {
		this.merRate = merRate;
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

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public Integer getAllowIndividualApply() {
		return allowIndividualApply;
	}

	public void setAllowIndividualApply(Integer allowIndividualApply) {
		this.allowIndividualApply = allowIndividualApply;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public Integer getServiceType2() {
		return serviceType2;
	}

	public void setServiceType2(Integer serviceType2) {
		this.serviceType2 = serviceType2;
	}

	public Integer getIsTx() {
		return isTx;
	}

	public void setIsTx(Integer isTx) {
		this.isTx = isTx;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}