package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/4/18/018.
 * 订单分润统计
 */
public class TotalAmount {

    private BigDecimal plateShareTotal;//平台分润总额

    private BigDecimal oemShareTotal;//品牌分润总额

    private BigDecimal totalShareAmountTotal;//交易总额

    private BigDecimal shareAmountTotal;//分润总额

    private BigDecimal priceTotal;//兑换总额

    private BigDecimal amountTotal;//提现金额总额

    private BigDecimal feeTotal;//手续费总额

    private BigDecimal amountWithdrawalsTotal;//出款总金额总额

    private BigDecimal receiveAmountTotal;//收款汇总

    private BigDecimal planAmountTotal;//目标还款金额汇总

    private BigDecimal actualAmountTotal;//实际还款金额汇总

    private BigDecimal agentAmoutTotal;//代理商分润汇总
    private BigDecimal merAmoutTotal;//用户分润汇总

    private BigDecimal writeOffPriceTotal;//核销价格汇总

    public BigDecimal getPlateShareTotal() {
        return plateShareTotal;
    }

    public void setPlateShareTotal(BigDecimal plateShareTotal) {
        this.plateShareTotal = plateShareTotal;
    }

    public BigDecimal getOemShareTotal() {
        return oemShareTotal;
    }

    public void setOemShareTotal(BigDecimal oemShareTotal) {
        this.oemShareTotal = oemShareTotal;
    }

    public BigDecimal getTotalShareAmountTotal() {
        return totalShareAmountTotal;
    }

    public void setTotalShareAmountTotal(BigDecimal totalShareAmountTotal) {
        this.totalShareAmountTotal = totalShareAmountTotal;
    }

    public BigDecimal getShareAmountTotal() {
        return shareAmountTotal;
    }

    public void setShareAmountTotal(BigDecimal shareAmountTotal) {
        this.shareAmountTotal = shareAmountTotal;
    }

    public BigDecimal getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(BigDecimal priceTotal) {
        this.priceTotal = priceTotal;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
    }

    public BigDecimal getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(BigDecimal feeTotal) {
        this.feeTotal = feeTotal;
    }

    public BigDecimal getAmountWithdrawalsTotal() {
        return amountWithdrawalsTotal;
    }

    public void setAmountWithdrawalsTotal(BigDecimal amountWithdrawalsTotal) {
        this.amountWithdrawalsTotal = amountWithdrawalsTotal;
    }

    public BigDecimal getReceiveAmountTotal() {
        return receiveAmountTotal;
    }

    public void setReceiveAmountTotal(BigDecimal receiveAmountTotal) {
        this.receiveAmountTotal = receiveAmountTotal;
    }

    public BigDecimal getPlanAmountTotal() {
        return planAmountTotal;
    }

    public void setPlanAmountTotal(BigDecimal planAmountTotal) {
        this.planAmountTotal = planAmountTotal;
    }

    public BigDecimal getActualAmountTotal() {
        return actualAmountTotal;
    }

    public void setActualAmountTotal(BigDecimal actualAmountTotal) {
        this.actualAmountTotal = actualAmountTotal;
    }

    public BigDecimal getAgentAmoutTotal() {
        return agentAmoutTotal;
    }

    public void setAgentAmoutTotal(BigDecimal agentAmoutTotal) {
        this.agentAmoutTotal = agentAmoutTotal;
    }

    public BigDecimal getMerAmoutTotal() {
        return merAmoutTotal;
    }

    public void setMerAmoutTotal(BigDecimal merAmoutTotal) {
        this.merAmoutTotal = merAmoutTotal;
    }

    public BigDecimal getWriteOffPriceTotal() {
        return writeOffPriceTotal;
    }

    public void setWriteOffPriceTotal(BigDecimal writeOffPriceTotal) {
        this.writeOffPriceTotal = writeOffPriceTotal;
    }
}
