package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * Created by 666666 on 2017/10/27.
 */
public class ProviderBean {

    private String agentNo;
    private String agentName;
    private String mobilephone;
    private BigDecimal rate;
    private BigDecimal singleAmount;
    private String cost;
    private String agentLevel;
    private String oneLevelId;
    private BigDecimal fullRepayRate;
    private BigDecimal fullRepaySingleAmount;
    private String fullRepayCost;
    private String accountRatio;
    private BigDecimal perfectRepayRate;
	private BigDecimal perfectRepaySingleAmount;
	private String perfectRepayCost;

    public BigDecimal getPerfectRepayRate() {
		return perfectRepayRate;
	}

	public void setPerfectRepayRate(BigDecimal perfectRepayRate) {
		this.perfectRepayRate = perfectRepayRate;
	}

	public BigDecimal getPerfectRepaySingleAmount() {
		return perfectRepaySingleAmount;
	}

	public void setPerfectRepaySingleAmount(BigDecimal perfectRepaySingleAmount) {
		this.perfectRepaySingleAmount = perfectRepaySingleAmount;
	}

	public String getPerfectRepayCost() {
		return perfectRepayCost;
	}

	public void setPerfectRepayCost(String perfectRepayCost) {
		this.perfectRepayCost = perfectRepayCost;
	}

	public String getAccountRatio() {
        return accountRatio;
    }

    public ProviderBean setAccountRatio(String accountRatio) {
        this.accountRatio = accountRatio;
        return this;
    }

    public String getFullRepayCost() {
		return fullRepayCost;
	}

	public void setFullRepayCost(String fullRepayCost) {
		this.fullRepayCost = fullRepayCost;
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

	public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(BigDecimal singleAmount) {
        this.singleAmount = singleAmount;
    }

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getOneLevelId() {
		return oneLevelId;
	}

	public void setOneLevelId(String oneLevelId) {
		this.oneLevelId = oneLevelId;
	}
}
