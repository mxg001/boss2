package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityConfig {

	private Integer id;
	private String activityCode;
	private Date startTime;
	private Date endTime;
	private String waitDay;
	private String cashServiceId;
	private String agentServiceId;
	private Integer cumulateTransDay;//累计交易时间
	private BigDecimal cumulateAmountMinus;//累计交易金额
	private BigDecimal cumulateAmountAdd;//累计交易金额
	private Integer cumulateTransMinusDay;//累计交易扣费时间

	public Integer getCumulateTransDay() {
		return cumulateTransDay;
	}
	public void setCumulateTransDay(Integer cumulateTransDay) {
		this.cumulateTransDay = cumulateTransDay;
	}
	public BigDecimal getCumulateAmountMinus() {
		return cumulateAmountMinus;
	}
	public void setCumulateAmountMinus(BigDecimal cumulateAmountMinus) {
		this.cumulateAmountMinus = cumulateAmountMinus;
	}
	public BigDecimal getCumulateAmountAdd() {
		return cumulateAmountAdd;
	}
	public void setCumulateAmountAdd(BigDecimal cumulateAmountAdd) {
		this.cumulateAmountAdd = cumulateAmountAdd;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAgentServiceId() {
		return agentServiceId;
	}
	public void setAgentServiceId(String agentServiceId) {
		this.agentServiceId = agentServiceId;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public Date getEndTime() {
		return endTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getWaitDay() {
		return waitDay;
	}
	public void setWaitDay(String waitDay) {
		this.waitDay = waitDay;
	}
	public String getCashServiceId() {
		return cashServiceId;
	}
	public void setCashServiceId(String cashServiceId) {
		this.cashServiceId = cashServiceId;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCumulateTransMinusDay() {
		return cumulateTransMinusDay;
	}

	public void setCumulateTransMinusDay(Integer cumulateTransMinusDay) {
		this.cumulateTransMinusDay = cumulateTransMinusDay;
	}
}
