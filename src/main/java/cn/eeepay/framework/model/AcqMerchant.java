package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table Acp_merchant
 * desc 收单机构商户
 * @author thj
 *
 */
public class AcqMerchant {
    private Integer id;

    private Integer acqOrgId;

    private Integer acqServiceId;

    private String acqMerchantNo;
    private String oldAcqMerchantNo;

    private String acqMerchantName;

    private String merchantNo;
    private String oldMerchantNo;
    private String merchantName;

    private String agentNo;

    private String mcc;

    private Integer largeSmallFlag;

    private Integer rateType;

    private BigDecimal singleAmount;

    private BigDecimal rate;

    private BigDecimal capping;

    private BigDecimal ladderRate;

    private BigDecimal ladderAmount;

    private BigDecimal quota;

    private Integer quotaStatus;

    private Integer locked;

    private String lockedMsg;

    private Date lockedTime;

    private Integer repPay;

    private Integer overQuota;

    private Date createTime;

    private String createPerson;

    private Integer acqStatus;
    
    private String merchantServiceType;
    
    //代理商名字
    private String agentName;
    
    //收单机构名称
    private String acqName;
    
    //收单服务类型
    private String serviceName;
    
    private String checkDayAmount;
    
    private BigDecimal dayAmount;
    
    private BigDecimal dayQuota;
    
    private String stratRemaimAmount;
    private String endRemaimAmount;
    //收单商户类别
    private Integer acqMerchantType;
    
    private String acqMerchantCode;

    private String special;

    private String source;//来源途径,数据字典acqMerSource值对应
    private String bind;//是否绑定特约商户 1-绑定 2-不绑定

    public String getAcqMerchantCode() {
		return acqMerchantCode;
	}

	public void setAcqMerchantCode(String acqMerchantCode) {
		this.acqMerchantCode = acqMerchantCode;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(Integer acqOrgId) {
        this.acqOrgId = acqOrgId;
    }

    public String getOldMerchantNo() {
        return oldMerchantNo;
    }

    public void setOldMerchantNo(String oldMerchantNo) {
        this.oldMerchantNo = oldMerchantNo;
    }

    public Integer getAcqStatus() {
		return acqStatus;
	}

	public void setAcqStatus(Integer acqStatus) {
		this.acqStatus = acqStatus;
	}

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo == null ? null : acqMerchantNo.trim();
    }

    public String getAcqMerchantName() {
        return acqMerchantName;
    }

    public void setAcqMerchantName(String acqMerchantName) {
        this.acqMerchantName = acqMerchantName == null ? null : acqMerchantName.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc == null ? null : mcc.trim();
    }

    public Integer getLargeSmallFlag() {
        return largeSmallFlag;
    }

    public void setLargeSmallFlag(Integer largeSmallFlag) {
        this.largeSmallFlag = largeSmallFlag;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
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

    public BigDecimal getLadderRate() {
        return ladderRate;
    }

    public void setLadderRate(BigDecimal ladderRate) {
        this.ladderRate = ladderRate;
    }

    public BigDecimal getLadderAmount() {
        return ladderAmount;
    }

    public void setLadderAmount(BigDecimal ladderAmount) {
        this.ladderAmount = ladderAmount;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public Integer getQuotaStatus() {
        return quotaStatus;
    }

    public void setQuotaStatus(Integer quotaStatus) {
        this.quotaStatus = quotaStatus;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getLockedMsg() {
        return lockedMsg;
    }

    public void setLockedMsg(String lockedMsg) {
        this.lockedMsg = lockedMsg == null ? null : lockedMsg.trim();
    }

    public Date getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(Date lockedTime) {
        this.lockedTime = lockedTime;
    }

    public Integer getRepPay() {
        return repPay;
    }

    public void setRepPay(Integer repPay) {
        this.repPay = repPay;
    }

    public Integer getOverQuota() {
        return overQuota;
    }

    public void setOverQuota(Integer overQuota) {
        this.overQuota = overQuota;
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
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	
	public String getMerchantServiceType() {
		return merchantServiceType;
	}

	public void setMerchantServiceType(String merchantServiceType) {
		this.merchantServiceType = merchantServiceType;
	}

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCheckDayAmount() {
		return checkDayAmount;
	}

	public void setCheckDayAmount(String checkDayAmount) {
		this.checkDayAmount = checkDayAmount;
	}

	public BigDecimal getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(BigDecimal dayAmount) {
		this.dayAmount = dayAmount;
	}

	public BigDecimal getDayQuota() {
		return dayQuota;
	}

	public void setDayQuota(BigDecimal dayQuota) {
		this.dayQuota = dayQuota;
	}

	public String getStratRemaimAmount() {
		return stratRemaimAmount;
	}

	public void setStratRemaimAmount(String stratRemaimAmount) {
		this.stratRemaimAmount = stratRemaimAmount;
	}

	public String getEndRemaimAmount() {
		return endRemaimAmount;
	}

	public void setEndRemaimAmount(String endRemaimAmount) {
		this.endRemaimAmount = endRemaimAmount;
	}

	public Integer getAcqMerchantType() {
		return acqMerchantType;
	}

	public void setAcqMerchantType(Integer acqMerchantType) {
		this.acqMerchantType = acqMerchantType;
	}

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getOldAcqMerchantNo() {
        return oldAcqMerchantNo;
    }

    public void setOldAcqMerchantNo(String oldAcqMerchantNo) {
        this.oldAcqMerchantNo = oldAcqMerchantNo;
    }
}