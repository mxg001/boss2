package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动
 * @author Administrator
 *
 */
public class ActivityRewardConfig {

	private Integer id;
	private String activityName;
	private Date startTime;
	private Date endTime;
	private BigDecimal cumulateAmountMinus;//首次注册不满扣N值
	private BigDecimal cumulateAmountAdd;//首次注册满奖M值
	private Integer cumulateTransDay;//首次注册累计交易扣费时间
	private Integer cumulateTransMinusDay;//首次注册累计交易奖励时间
	private BigDecimal repeatCumulateAmountMinus;//重复注册不满扣N值
	private BigDecimal repeatCumulateAmountAdd;//重复注册满奖M值
	private Integer repeatCumulateTransDay;//重复注册累计交易扣费时间
	private Integer repeatCumulateTransMinusDay;//重复注册累计交易奖励时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Integer getCumulateTransDay() {
		return cumulateTransDay;
	}

	public void setCumulateTransDay(Integer cumulateTransDay) {
		this.cumulateTransDay = cumulateTransDay;
	}

	public Integer getCumulateTransMinusDay() {
		return cumulateTransMinusDay;
	}

	public void setCumulateTransMinusDay(Integer cumulateTransMinusDay) {
		this.cumulateTransMinusDay = cumulateTransMinusDay;
	}

	public BigDecimal getRepeatCumulateAmountMinus() {
		return repeatCumulateAmountMinus;
	}

	public void setRepeatCumulateAmountMinus(BigDecimal repeatCumulateAmountMinus) {
		this.repeatCumulateAmountMinus = repeatCumulateAmountMinus;
	}

	public BigDecimal getRepeatCumulateAmountAdd() {
		return repeatCumulateAmountAdd;
	}

	public void setRepeatCumulateAmountAdd(BigDecimal repeatCumulateAmountAdd) {
		this.repeatCumulateAmountAdd = repeatCumulateAmountAdd;
	}

	public Integer getRepeatCumulateTransDay() {
		return repeatCumulateTransDay;
	}

	public void setRepeatCumulateTransDay(Integer repeatCumulateTransDay) {
		this.repeatCumulateTransDay = repeatCumulateTransDay;
	}

	public Integer getRepeatCumulateTransMinusDay() {
		return repeatCumulateTransMinusDay;
	}

	public void setRepeatCumulateTransMinusDay(Integer repeatCumulateTransMinusDay) {
		this.repeatCumulateTransMinusDay = repeatCumulateTransMinusDay;
	}
}
