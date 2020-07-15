package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * table merchant_service_quota
 * desc  商户服务限额表
 */
public class MerchantServiceQuota {
    private Long id;

    private String serviceId;

    private String cardType;

    private String holidaysMark;

    private String merchantNo;

    private BigDecimal singleDayAmount;

    private BigDecimal singleCountAmount;
    
    private BigDecimal singleMinAmount;

    private BigDecimal singleDaycardAmount;

    private Integer singleDaycardCount;

    private Date efficientDate;

    private Date disabledDate;

    private String serviceName;
    
    private String useable;
    
    private String fixedMark;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark == null ? null : holidaysMark.trim();
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

    public Date getEfficientDate() {
        return efficientDate;
    }

    public void setEfficientDate(Date efficientDate) {
        this.efficientDate = efficientDate;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getFixedMark() {
		return fixedMark;
	}

	public void setFixedMark(String fixedMark) {
		this.fixedMark = fixedMark;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public BigDecimal getSingleMinAmount() {
		return singleMinAmount;
	}

	public void setSingleMinAmount(BigDecimal singleMinAmount) {
		this.singleMinAmount = singleMinAmount;
	}
}