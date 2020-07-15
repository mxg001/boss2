package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * 超级还款服务商成本
 * Created by 666666 on 2017/11/3.
 */
public class RepayOemServiceCostBean {

	private String agentNo;
	private String agentName;
	private BigDecimal rate;
	private BigDecimal singleAmount;
	private String rateAndSingleAmount;
	private String oemNo;
	private BigDecimal fullRepayRate;
	private BigDecimal fullRepaySingleAmount;
	private String fullRepayRateAndSingleAmount;
	private BigDecimal perfectRate;
	private BigDecimal perfectSingleAmount;
	private String perfectRateAndSingleAmount;

	public RepayOemServiceCostBean(String agentNo, BigDecimal rate, BigDecimal singleAmount, BigDecimal fullRepayRate,
			BigDecimal fullRepaySingleAmount, BigDecimal perfectRate, BigDecimal perfectSingleAmount) {
		this.agentNo = agentNo;
		this.rate = rate;
		this.singleAmount = singleAmount;
		this.fullRepayRate = fullRepayRate;
		this.fullRepaySingleAmount = fullRepaySingleAmount;
		this.perfectRate = perfectRate;
		this.perfectSingleAmount = perfectSingleAmount;
	}

	public BigDecimal getPerfectRate() {
		return perfectRate;
	}

	public void setPerfectRate(BigDecimal perfectRate) {
		this.perfectRate = perfectRate;
	}

	public BigDecimal getPerfectSingleAmount() {
		return perfectSingleAmount;
	}

	public void setPerfectSingleAmount(BigDecimal perfectSingleAmount) {
		this.perfectSingleAmount = perfectSingleAmount;
	}

	public String getPerfectRateAndSingleAmount() {
		return perfectRateAndSingleAmount;
	}

	public void setPerfectRateAndSingleAmount(String perfectRateAndSingleAmount) {
		this.perfectRateAndSingleAmount = perfectRateAndSingleAmount;
	}

	public String getFullRepayRateAndSingleAmount() {
		return fullRepayRateAndSingleAmount;
	}

	public void setFullRepayRateAndSingleAmount(String fullRepayRateAndSingleAmount) {
		this.fullRepayRateAndSingleAmount = fullRepayRateAndSingleAmount;
	}

	public BigDecimal getFullRepayRate() {
		return fullRepayRate;
	}

	public void setFullRepayRate(BigDecimal fullRepayRate) {
		this.fullRepayRate = fullRepayRate;
	}

	public BigDecimal getFullRepaySingleAmount() {
		return fullRepaySingleAmount;
	}

	public void setFullRepaySingleAmount(BigDecimal fullRepaySingleAmount) {
		this.fullRepaySingleAmount = fullRepaySingleAmount;
	}

	public RepayOemServiceCostBean() {
	}

	public String getRateAndSingleAmount() {
		return rateAndSingleAmount;
	}

	public RepayOemServiceCostBean setRateAndSingleAmount(String rateAndSingleAmount) {
		this.rateAndSingleAmount = rateAndSingleAmount;
		return this;
	}

	public String getAgentName() {
		return agentName;
	}

	public RepayOemServiceCostBean setAgentName(String agentName) {
		this.agentName = agentName;
		return this;
	}

	public String getOemNo() {
		return oemNo;
	}

	public RepayOemServiceCostBean setOemNo(String oemNo) {
		this.oemNo = oemNo;
		return this;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public RepayOemServiceCostBean setAgentNo(String agentNo) {
		this.agentNo = agentNo;
		return this;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public RepayOemServiceCostBean setRate(BigDecimal rate) {
		this.rate = rate;
		return this;
	}

	public BigDecimal getSingleAmount() {
		return singleAmount;
	}

	public RepayOemServiceCostBean setSingleAmount(BigDecimal singleAmount) {
		this.singleAmount = singleAmount;
		return this;
	}
}
