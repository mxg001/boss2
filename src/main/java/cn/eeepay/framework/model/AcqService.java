package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * table Acq_service
 * desc 收单机构服务
 * @author thj
 *
 */
public class AcqService {
    private Integer id;

    private Integer acqId;

    private String serviceType;

    private String serviceName;

    private Integer feeIsCard;

    private Integer quotaIsCard;

    private Integer bankCardType;

    private String serviceRemark;

    private Integer serviceStatus;

	private String allowTransStartTime;

	private String allowTransEndTime;

    private Date createTime;

    private String createPerson;

	private String acqName;
	
	private String acqEnname;

    private Integer timeSwitch;//定时开关 0关闭;1开启

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date timeStartTime;//定时开始时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date timeEndTime;//定时结束时间

    private String closePrompt;//关闭提示语
    
    /** 周期性关闭通道时间 */
    private String periodicityStartTime;
    /** 周期性关闭通道结束时间 */
    private String periodicityEndTime;

    public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAcqId() {
        return acqId;
    }

    public void setAcqId(Integer acqId) {
        this.acqId = acqId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public Integer getFeeIsCard() {
        return feeIsCard;
    }

    public void setFeeIsCard(Integer feeIsCard) {
        this.feeIsCard = feeIsCard;
    }

    public Integer getQuotaIsCard() {
        return quotaIsCard;
    }

    public void setQuotaIsCard(Integer quotaIsCard) {
        this.quotaIsCard = quotaIsCard;
    }

    public Integer getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getServiceRemark() {
        return serviceRemark;
    }

    public void setServiceRemark(String serviceRemark) {
        this.serviceRemark = serviceRemark == null ? null : serviceRemark.trim();
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

	public String getAllowTransStartTime() {
		return allowTransStartTime;
	}

	public void setAllowTransStartTime(String allowTransStartTime) {
		this.allowTransStartTime = allowTransStartTime;
	}

	public String getAllowTransEndTime() {
		return allowTransEndTime;
	}

	public void setAllowTransEndTime(String allowTransEndTime) {
		this.allowTransEndTime = allowTransEndTime;
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

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}

    public Integer getTimeSwitch() {
        return timeSwitch;
    }

    public void setTimeSwitch(Integer timeSwitch) {
        this.timeSwitch = timeSwitch;
    }

    public Date getTimeStartTime() {
        return timeStartTime;
    }

    public void setTimeStartTime(Date timeStartTime) {
        this.timeStartTime = timeStartTime;
    }

    public Date getTimeEndTime() {
        return timeEndTime;
    }

    public void setTimeEndTime(Date timeEndTime) {
        this.timeEndTime = timeEndTime;
    }

    public String getClosePrompt() {
        return closePrompt;
    }

    public void setClosePrompt(String closePrompt) {
        this.closePrompt = closePrompt;
    }

	public String getPeriodicityStartTime() {
		return periodicityStartTime;
	}

	public void setPeriodicityStartTime(String periodicityStartTime) {
		this.periodicityStartTime = periodicityStartTime;
	}

	public String getPeriodicityEndTime() {
		return periodicityEndTime;
	}

	public void setPeriodicityEndTime(String periodicityEndTime) {
		this.periodicityEndTime = periodicityEndTime;
	}
    
}