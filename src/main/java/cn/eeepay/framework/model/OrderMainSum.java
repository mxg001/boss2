package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * desc:订单表汇总数据
 * @author tans
 * @date 2017-12-1
 */
public class OrderMainSum {

    private BigDecimal totalBonusSum;//奖励总金额

    private BigDecimal plateProfitSum;//平台分润汇总

    private BigDecimal orgProfitSum;//品牌分润汇总

    private BigDecimal profitSum;//收益汇总

    private BigDecimal obtainAmountSum;//结算金额汇总

    private BigDecimal realObtainAmountSum;//结算金额汇总

    private BigDecimal obtainFeeSum;//结算金额汇总

    private BigDecimal receiveAmountSum;//收款金额汇总 | 目标还款金额汇总

    private BigDecimal repayAmountSum;//实际还款金额汇总
    private BigDecimal repayTransscuconsSum;//实际消费金额汇总
    
    private BigDecimal creditcardBankBonusSum;//银行发卡奖金汇总
    private BigDecimal creditcardBankBonus2Sum;//银行首刷奖金汇总
    private BigDecimal plateProfit2Sum;//首刷平台分润汇总
    private BigDecimal orgProfit2Sum;//首刷品牌分润汇总
    private BigDecimal loanAmountSum;//贷款总金额汇总
    private BigDecimal priceSum;//首刷品牌分润汇总
    private BigDecimal redSum;//发红包金额汇总
    private BigDecimal actualSum;//平台实际分润汇总
    private BigDecimal territorySum;//领地分红汇总
    private BigDecimal actualConsumeSum;//实际消费金额汇总

    private  long countSum;

    public BigDecimal getTotalBonusSum() {
        return totalBonusSum != null ? totalBonusSum : BigDecimal.ZERO;
    }

    public void setTotalBonusSum(BigDecimal totalBonusSum) {
        this.totalBonusSum = totalBonusSum;
    }

    public BigDecimal getPlateProfitSum() {
        return plateProfitSum != null ? plateProfitSum : BigDecimal.ZERO;
    }

    public void setPlateProfitSum(BigDecimal plateProfitSum) {
        this.plateProfitSum = plateProfitSum;
    }

    public BigDecimal getOrgProfitSum() {
        return orgProfitSum != null ? orgProfitSum : BigDecimal.ZERO;
    }

    public void setOrgProfitSum(BigDecimal orgProfitSum) {
        this.orgProfitSum = orgProfitSum;
    }

    public BigDecimal getProfitSum() {
        return profitSum != null ? profitSum : BigDecimal.ZERO;
    }

    public void setProfitSum(BigDecimal profitSum) {
        this.profitSum = profitSum;
    }

    public BigDecimal getObtainAmountSum() {
        return obtainAmountSum != null ? obtainAmountSum : BigDecimal.ZERO;
    }

    public void setObtainAmountSum(BigDecimal obtainAmountSum) {
        this.obtainAmountSum = obtainAmountSum;
    }

    public BigDecimal getRealObtainAmountSum() {
        return realObtainAmountSum != null ? realObtainAmountSum : BigDecimal.ZERO;
    }

    public void setRealObtainAmountSum(BigDecimal realObtainAmountSum) {
        this.realObtainAmountSum = realObtainAmountSum;
    }

    public BigDecimal getObtainFeeSum() {
        return obtainFeeSum != null ? obtainFeeSum : BigDecimal.ZERO;
    }

    public void setObtainFeeSum(BigDecimal obtainFeeSum) {
        this.obtainFeeSum = obtainFeeSum;
    }

    public BigDecimal getReceiveAmountSum() {
        return receiveAmountSum != null ? receiveAmountSum : BigDecimal.ZERO;
    }

    public void setReceiveAmountSum(BigDecimal receiveAmountSum) {
        this.receiveAmountSum = receiveAmountSum;
    }

    public BigDecimal getRepayAmountSum() {
        return repayAmountSum != null ? repayAmountSum : BigDecimal.ZERO;
    }

    public void setRepayAmountSum(BigDecimal repayAmountSum) {
        this.repayAmountSum = repayAmountSum;
    }

    public BigDecimal getCreditcardBankBonusSum() {
        return creditcardBankBonusSum != null ? creditcardBankBonusSum : BigDecimal.ZERO;
    }

    public void setCreditcardBankBonusSum(BigDecimal creditcardBankBonusSum) {
        this.creditcardBankBonusSum = creditcardBankBonusSum;
    }

    public BigDecimal getCreditcardBankBonus2Sum() {
        return creditcardBankBonus2Sum != null ? creditcardBankBonus2Sum : BigDecimal.ZERO;
    }

    public void setCreditcardBankBonus2Sum(BigDecimal creditcardBankBonus2Sum) {
        this.creditcardBankBonus2Sum = creditcardBankBonus2Sum;
    }

    public BigDecimal getPlateProfit2Sum() {
        return plateProfit2Sum != null ? plateProfit2Sum : BigDecimal.ZERO;
    }

    public void setPlateProfit2Sum(BigDecimal plateProfit2Sum) {
        this.plateProfit2Sum = plateProfit2Sum;
    }

    public BigDecimal getOrgProfit2Sum() {
        return orgProfit2Sum != null ? orgProfit2Sum : BigDecimal.ZERO;
    }

    public void setOrgProfit2Sum(BigDecimal orgProfit2Sum) {
        this.orgProfit2Sum = orgProfit2Sum;
    }

    public BigDecimal getLoanAmountSum() {
        return loanAmountSum != null ? loanAmountSum : BigDecimal.ZERO;
    }

    public void setLoanAmountSum(BigDecimal loanAmountSum) {
        this.loanAmountSum = loanAmountSum;
    }

    public BigDecimal getPriceSum() {
        return priceSum != null ? priceSum : BigDecimal.ZERO;
    }

    public void setPriceSum(BigDecimal priceSum) {
        this.priceSum = priceSum;
    }

    public long getCountSum() {
        return countSum;
    }

    public void setCountSum(long countSum) {
        this.countSum = countSum;
    }

    public BigDecimal getActualSum() {
        return actualSum == null ? BigDecimal.ZERO : actualSum;
    }

    public void setActualSum(BigDecimal actualSum) {
        this.actualSum = actualSum;
    }

    public BigDecimal getTerritorySum() {
        return territorySum;
    }

    public void setTerritorySum(BigDecimal territorySum) {
        this.territorySum = territorySum;
    }

    public BigDecimal getRedSum() {
        return redSum;
    }

    public void setRedSum(BigDecimal redSum) {
        this.redSum = redSum;
    }

    public BigDecimal getActualConsumeSum() {
        return actualConsumeSum;
    }

    public void setActualConsumeSum(BigDecimal actualConsumeSum) {
        this.actualConsumeSum = actualConsumeSum;
    }

	public BigDecimal getRepayTransscuconsSum() {
		return repayTransscuconsSum == null ? BigDecimal.ZERO : repayTransscuconsSum;
	}

	public void setRepayTransscuconsSum(BigDecimal repayTransscuconsSum) {
		this.repayTransscuconsSum = repayTransscuconsSum;
	}
}