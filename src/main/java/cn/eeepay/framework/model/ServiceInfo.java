package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;


/**
 * @author liusha
 * table service_info
 * desc 服务基本信息表
 */
public class ServiceInfo {

    private Long serviceId;

    private String serviceName;

    private String agentShowName;

    private Integer serviceType;
    private String serviceTypeName;

    private Integer hardwareIs;

    private Integer bankCard;

    private String exclusive;

    private String business;

    private Date saleStarttime;

    private Date saleEndtime;
    @JSONField(format = "yyyy-MM-dd")
    private Date useStarttime;
    @JSONField(format = "yyyy-MM-dd")
    private Date useEndtime;

    private String proxy;

    private String getcashId;

    private Integer rateCard;

    private Integer rateHolidays;

    private Integer quotaHolidays;

    private Integer quotaCard;

    private String oemId;
    
    private Integer tFlag;
    
    private String cashSubject;

    private List<ServiceRate> rates;
    
    private List<ServiceQuota> quotas;
    
    private String remark;
    
    private int fixedRate;
    
    private int fixedQuota;

    private int serviceStatus;
    private String tradStart;
    private String tradEnd;
    
    private Long linkService;
    
    private String linkServiceName;
    
    //服务是否被使用，0：否，1：是
    private int usedStatus;
    
    private String t0TurnT1;

    private Integer effectiveStatus;//生效状态,0:失效,1:生效,默认为1

    private Date createTime;//创建时间

    private String createPerson;//创建人


    private String jRate;//借记卡费率

    private String dRate;//贷记卡商户费率

    private Integer loanMergeShow;

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getjRate() {
        return jRate;
    }

    public void setjRate(String jRate) {
        this.jRate = jRate;
    }

    public String getdRate() {
        return dRate;
    }

    public void setdRate(String dRate) {
        this.dRate = dRate;
    }

    public String getT0TurnT1() {
		return t0TurnT1;
	}

	public void setT0TurnT1(String t0TurnT1) {
		this.t0TurnT1 = t0TurnT1;
	}

	public int getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(int usedStatus) {
		this.usedStatus = usedStatus;
	}

	public String getLinkServiceName() {
		return linkServiceName;
	}

	public void setLinkServiceName(String linkServiceName) {
		this.linkServiceName = linkServiceName;
	}

	public Long getLinkService() {
		return linkService;
	}

	public void setLinkService(Long linkService) {
		this.linkService = linkService;
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
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getHardwareIs() {
        return hardwareIs;
    }

    public void setHardwareIs(Integer hardwareIs) {
        this.hardwareIs = hardwareIs;
    }

    public Integer getBankCard() {
        return bankCard;
    }

    public void setBankCard(Integer bankCard) {
        this.bankCard = bankCard;
    }

    public String getExclusive() {
        return exclusive;
    }

    public void setExclusive(String exclusive) {
        this.exclusive = exclusive == null ? null : exclusive.trim();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business == null ? null : business.trim();
    }

    public Date getSaleStarttime() {
        return saleStarttime;
    }

    public void setSaleStarttime(Date saleStarttime) {
        this.saleStarttime = saleStarttime;
    }

    public Date getSaleEndtime() {
        return saleEndtime;
    }

    public void setSaleEndtime(Date saleEndtime) {
        this.saleEndtime = saleEndtime;
    }

    public Date getUseStarttime() {
        return useStarttime;
    }

    public void setUseStarttime(Date useStarttime) {
        this.useStarttime = useStarttime;
    }

    public Date getUseEndtime() {
        return useEndtime;
    }

    public void setUseEndtime(Date useEndtime) {
        this.useEndtime = useEndtime;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy == null ? null : proxy.trim();
    }

    public String getGetcashId() {
        return getcashId;
    }

    public void setGetcashId(String getcashId) {
        this.getcashId = getcashId == null ? null : getcashId.trim();
    }

    public Integer getRateCard() {
        return rateCard;
    }

    public void setRateCard(Integer rateCard) {
        this.rateCard = rateCard;
    }

    public Integer getRateHolidays() {
        return rateHolidays;
    }

    public void setRateHolidays(Integer rateHolidays) {
        this.rateHolidays = rateHolidays;
    }

    public Integer getQuotaHolidays() {
        return quotaHolidays;
    }

    public void setQuotaHolidays(Integer quotaHolidays) {
        this.quotaHolidays = quotaHolidays;
    }

    public Integer getQuotaCard() {
        return quotaCard;
    }

    public void setQuotaCard(Integer quotaCard) {
        this.quotaCard = quotaCard;
    }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId == null ? null : oemId.trim();
    }

	public List<ServiceRate> getRates() {
		return rates;
	}

	public void setRates(List<ServiceRate> rates) {
		this.rates = rates;
	}

	public List<ServiceQuota> getQuotas() {
		return quotas;
	}

	public void setQuotas(List<ServiceQuota> quotas) {
		this.quotas = quotas;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer gettFlag() {
		return tFlag;
	}

	public void settFlag(Integer tFlag) {
		this.tFlag =  tFlag;
	}

	public String getCashSubject() {
		return cashSubject;
	}

	public void setCashSubject(String cashSubject) {
		this.cashSubject = cashSubject == null ? null : cashSubject.trim();
	}

	public int getFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(int fixedRate) {
		this.fixedRate = fixedRate;
	}

	public int getFixedQuota() {
		return fixedQuota;
	}

	public void setFixedQuota(int fixedQuota) {
		this.fixedQuota = fixedQuota;
	}

	public int getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(int serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getTradStart() {
		return tradStart;
	}

	public void setTradStart(String tradStart) {
		if(tradStart!=null&&tradStart.length()==5){
			tradStart+=":00";
		}
		this.tradStart = tradStart;
	}

	public String getTradEnd() {
		return tradEnd;
	}

	public void setTradEnd(String tradEnd) {
		if(tradEnd!=null&&tradEnd.length()==5){
			tradEnd+=":59";
		}
		this.tradEnd = tradEnd;
	}

    public Integer getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(Integer effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
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

    public String getAgentShowName() {
        return agentShowName;
    }

    public void setAgentShowName(String agentShowName) {
        this.agentShowName = agentShowName;
    }

    public Integer getLoanMergeShow() {
        return loanMergeShow;
    }

    public void setLoanMergeShow(Integer loanMergeShow) {
        this.loanMergeShow = loanMergeShow;
    }
}