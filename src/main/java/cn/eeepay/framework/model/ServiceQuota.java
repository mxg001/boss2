package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * @author pc20160100
 * table service_manage_quota
 * desc 
 */
/**
 * @author liusha
 * table service_manage_quota
 * desc 服务管控费率
 */
public class ServiceQuota {
    private Long id;

    private Long serviceId;
    
    private String serviceName;

    private String holidaysMark;

    private String cardType;

    private String quotaLevel;

    private String agentNo;

    private BigDecimal singleDayAmount;

    private BigDecimal singleCountAmount;
    
    private BigDecimal singleMinAmount;

    private BigDecimal singleDaycardAmount;

    private Integer singleDaycardCount;

    private Integer checkStatus;

    private Integer lockStatus;
    
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
        this.serviceId = serviceId ;
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
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public BigDecimal getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(BigDecimal singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
    }

    public BigDecimal getSingleCountAmount() {
        return singleCountAmount;
    }

    public void setSingleCountAmount(BigDecimal singleCountAmount) {
        this.singleCountAmount = singleCountAmount;
    }

    public BigDecimal getSingleDaycardAmount() {
        return singleDaycardAmount;
    }

    public void setSingleDaycardAmount(BigDecimal singleDaycardAmount) {
        this.singleDaycardAmount = singleDaycardAmount;
    }

    public Integer getSingleDaycardCount() {
        return singleDaycardCount;
    }

    public void setSingleDaycardCount(Integer singleDaycardCount) {
        this.singleDaycardCount = singleDaycardCount;
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

	public Integer getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Integer isGlobal) {
		this.isGlobal = isGlobal;
	}

	public BigDecimal getSingleMinAmount() {
		return singleMinAmount;
	}

	public void setSingleMinAmount(BigDecimal singleMinAmount) {
		this.singleMinAmount = singleMinAmount;
	}
	

}