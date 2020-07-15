package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 欢乐返活跃商户配置表
 * @author Administrator
 *
 */
public class HlfActivityMerchantRule {
	private Integer ruleId;
	private String ruleName;
	private Date startTime;
	private Date endTime;
	private Integer firstRewardType;
	private Integer firstRewardMonth;
	private BigDecimal firstRewardTotalAmount;
	private BigDecimal firstRewardAmount;
	private Integer firstDeductType;
	private Integer firstDeductMonth;
	private BigDecimal firstDeductTotalAmount;
	private BigDecimal firstDeductAmount;
	private Integer firstRepeatStatus;
	private Integer repeatRewardType;
	private Integer repeatRewardMonth;
	private BigDecimal repeatRewardTotalAmount;
	private BigDecimal repeatRewardAmount;
	private Integer repeatDeductType;
	private Integer repeatDeductMonth;
	private BigDecimal repeatDeductTotalAmount;
	private BigDecimal repeatDeductAmount;
	private Date createTime;
	private String operator;
	private String lastUpdateTime;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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

	public Integer getFirstRewardType() {
		return firstRewardType;
	}

	public void setFirstRewardType(Integer firstRewardType) {
		this.firstRewardType = firstRewardType;
	}

	public Integer getFirstRewardMonth() {
		return firstRewardMonth;
	}

	public void setFirstRewardMonth(Integer firstRewardMonth) {
		this.firstRewardMonth = firstRewardMonth;
	}

	public BigDecimal getFirstRewardTotalAmount() {
		return firstRewardTotalAmount;
	}

	public void setFirstRewardTotalAmount(BigDecimal firstRewardTotalAmount) {
		this.firstRewardTotalAmount = firstRewardTotalAmount;
	}

	public BigDecimal getFirstRewardAmount() {
		return firstRewardAmount;
	}

	public void setFirstRewardAmount(BigDecimal firstRewardAmount) {
		this.firstRewardAmount = firstRewardAmount;
	}

	public Integer getFirstDeductType() {
		return firstDeductType;
	}

	public void setFirstDeductType(Integer firstDeductType) {
		this.firstDeductType = firstDeductType;
	}

	public Integer getFirstDeductMonth() {
		return firstDeductMonth;
	}

	public void setFirstDeductMonth(Integer firstDeductMonth) {
		this.firstDeductMonth = firstDeductMonth;
	}

	public BigDecimal getFirstDeductTotalAmount() {
		return firstDeductTotalAmount;
	}

	public void setFirstDeductTotalAmount(BigDecimal firstDeductTotalAmount) {
		this.firstDeductTotalAmount = firstDeductTotalAmount;
	}

	public BigDecimal getFirstDeductAmount() {
		return firstDeductAmount;
	}

	public void setFirstDeductAmount(BigDecimal firstDeductAmount) {
		this.firstDeductAmount = firstDeductAmount;
	}

	public Integer getFirstRepeatStatus() {
		return firstRepeatStatus;
	}

	public void setFirstRepeatStatus(Integer firstRepeatStatus) {
		this.firstRepeatStatus = firstRepeatStatus;
	}

	public Integer getRepeatRewardType() {
		return repeatRewardType;
	}

	public void setRepeatRewardType(Integer repeatRewardType) {
		this.repeatRewardType = repeatRewardType;
	}

	public Integer getRepeatRewardMonth() {
		return repeatRewardMonth;
	}

	public void setRepeatRewardMonth(Integer repeatRewardMonth) {
		this.repeatRewardMonth = repeatRewardMonth;
	}

	public BigDecimal getRepeatRewardTotalAmount() {
		return repeatRewardTotalAmount;
	}

	public void setRepeatRewardTotalAmount(BigDecimal repeatRewardTotalAmount) {
		this.repeatRewardTotalAmount = repeatRewardTotalAmount;
	}

	public BigDecimal getRepeatRewardAmount() {
		return repeatRewardAmount;
	}

	public void setRepeatRewardAmount(BigDecimal repeatRewardAmount) {
		this.repeatRewardAmount = repeatRewardAmount;
	}

	public Integer getRepeatDeductType() {
		return repeatDeductType;
	}

	public void setRepeatDeductType(Integer repeatDeductType) {
		this.repeatDeductType = repeatDeductType;
	}

	public Integer getRepeatDeductMonth() {
		return repeatDeductMonth;
	}

	public void setRepeatDeductMonth(Integer repeatDeductMonth) {
		this.repeatDeductMonth = repeatDeductMonth;
	}

	public BigDecimal getRepeatDeductTotalAmount() {
		return repeatDeductTotalAmount;
	}

	public void setRepeatDeductTotalAmount(BigDecimal repeatDeductTotalAmount) {
		this.repeatDeductTotalAmount = repeatDeductTotalAmount;
	}

	public BigDecimal getRepeatDeductAmount() {
		return repeatDeductAmount;
	}

	public void setRepeatDeductAmount(BigDecimal repeatDeductAmount) {
		this.repeatDeductAmount = repeatDeductAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
