package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class OutAccountService {
    private Integer id;

    private Integer acqOrgId;

    private Integer serviceType;

    private String serviceName;
    
    private BigDecimal dayTotalAmount;

    private BigDecimal outAccountMinAmount;

    private BigDecimal outAccountMaxAmount;

    private BigDecimal dayOutAccountAmount;

    private BigDecimal outAmountWarning;

    private BigDecimal transformationAmount;

    private String level;

    private String antoCloseMsg;

    private Integer outAccountStatus;
    
    private String acqEnname;//收单机构的英文名

	private String acqOrgName;// 收单机构名称
	
	private BigDecimal lastAmount;

    private Integer dayResetLimit;//每日重置限额 0不重置;1重置

    private BigDecimal userBalance;//平台在上游余额

    private String userCode;//平台在上游的账号

    private String remark;//备注

    private BigDecimal icMonthMaxAmount;//身份证月累积最大提现额

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public String getAcqOrgName() {
		return acqOrgName;
	}

	public void setAcqOrgName(String acqOrgName) {
		this.acqOrgName = acqOrgName;
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

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getOutAccountMinAmount() {
        return outAccountMinAmount;
    }

    public void setOutAccountMinAmount(BigDecimal outAccountMinAmount) {
        this.outAccountMinAmount = outAccountMinAmount;
    }

    public BigDecimal getOutAccountMaxAmount() {
        return outAccountMaxAmount;
    }

    public void setOutAccountMaxAmount(BigDecimal outAccountMaxAmount) {
        this.outAccountMaxAmount = outAccountMaxAmount;
    }

    public BigDecimal getDayOutAccountAmount() {
        return dayOutAccountAmount;
    }

    public void setDayOutAccountAmount(BigDecimal dayOutAccountAmount) {
        this.dayOutAccountAmount = dayOutAccountAmount;
    }

    public BigDecimal getOutAmountWarning() {
        return outAmountWarning;
    }

    public void setOutAmountWarning(BigDecimal outAmountWarning) {
        this.outAmountWarning = outAmountWarning;
    }

    public BigDecimal getTransformationAmount() {
        return transformationAmount;
    }

    public void setTransformationAmount(BigDecimal transformationAmount) {
        this.transformationAmount = transformationAmount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAntoCloseMsg() {
        return antoCloseMsg;
    }

    public void setAntoCloseMsg(String antoCloseMsg) {
        this.antoCloseMsg = antoCloseMsg;
    }

    public Integer getOutAccountStatus() {
        return outAccountStatus;
    }

    public void setOutAccountStatus(Integer outAccountStatus) {
        this.outAccountStatus = outAccountStatus;
    }

	public BigDecimal getLastAmount() {
		return lastAmount;
	}

	public void setLastAmount(BigDecimal lastAmount) {
		this.lastAmount = lastAmount;
	}

	public BigDecimal getDayTotalAmount() {
		return dayTotalAmount;
	}

	public void setDayTotalAmount(BigDecimal dayTotalAmount) {
		this.dayTotalAmount = dayTotalAmount;
	}

    public Integer getDayResetLimit() {
        return dayResetLimit;
    }

    public void setDayResetLimit(Integer dayResetLimit) {
        this.dayResetLimit = dayResetLimit;
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getIcMonthMaxAmount() {
        return icMonthMaxAmount;
    }

    public void setIcMonthMaxAmount(BigDecimal icMonthMaxAmount) {
        this.icMonthMaxAmount = icMonthMaxAmount;
    }
}