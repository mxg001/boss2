package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * table merchant_service_rate
 * desc 商户服务签约费率
 */
public class MerchantServiceRate {
    private Long id;

    private String serviceId;

    private String merchantNo;

    private String holidaysMark;

    private String cardType;

    private String rateType;

    private BigDecimal singleNumAmount;

    private BigDecimal rate;

    private BigDecimal capping;

    private BigDecimal safeLine;

    private Date efficientDate;

    private Date disabledDate;

    private String serviceName;
    
    private String useable;
    
    private String fixedMark;
    
    private BigDecimal ladder1Rate;
    private BigDecimal ladder1Max;
    private BigDecimal ladder2Rate;
    private BigDecimal ladder2Max;
    private BigDecimal ladder3Rate;
    private BigDecimal ladder3Max;
    private BigDecimal ladder4Rate;
    private BigDecimal ladder4Max;
    
    private String merRate;
    
    //一级管控费率
    private String oneRate;
    
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

	public String getMerRate() {
		return merRate;
	}

	public void setMerRate(String merRate) {
		this.merRate = merRate;
	}

	public String getOneRate() {
		return oneRate;
	}

	public void setOneRate(String oneRate) {
		this.oneRate = oneRate;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
}