package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * desc:订单表汇总数据
 * @author tans
 * @date 2017-12-1
 */
public class ManorMainSum {

    private BigDecimal totalBonusSum;//收益总金额

    private BigDecimal totalBusinessSum;//业务分红汇总

    private  BigDecimal  totalPremiumSum;//溢价交易汇总

    private  BigDecimal  totalRandomSum;//每日随机汇总

    private  int  businessSum;//业务分红笔数

    private BigDecimal totalBasicBusinessSum;//业务基准分红汇总

    private  BigDecimal totalAccumulatedIncome;//领地累计收益

    private  BigDecimal totalRedIncome;//已转入红包账户领地累计收益

    public BigDecimal getTotalBonusSum() {
        return totalBonusSum;
    }

    public void setTotalBonusSum(BigDecimal totalBonusSum) {
        this.totalBonusSum = totalBonusSum;
    }

    public BigDecimal getTotalBusinessSum() {
        return totalBusinessSum;
    }

    public void setTotalBusinessSum(BigDecimal totalBusinessSum) {
        this.totalBusinessSum = totalBusinessSum;
    }

    public BigDecimal getTotalPremiumSum() {
        return totalPremiumSum;
    }

    public void setTotalPremiumSum(BigDecimal totalPremiumSum) {
        this.totalPremiumSum = totalPremiumSum;
    }

    public BigDecimal getTotalRandomSum() {
        return totalRandomSum;
    }

    public void setTotalRandomSum(BigDecimal totalRandomSum) {
        this.totalRandomSum = totalRandomSum;
    }

    public int getBusinessSum() {
        return businessSum;
    }

    public void setBusinessSum(int businessSum) {
        this.businessSum = businessSum;
    }

    public BigDecimal getTotalBasicBusinessSum() {
        return totalBasicBusinessSum;
    }

    public void setTotalBasicBusinessSum(BigDecimal totalBasicBusinessSum) {
        this.totalBasicBusinessSum = totalBasicBusinessSum;
    }

    public BigDecimal getTotalAccumulatedIncome() {
        return totalAccumulatedIncome;
    }

    public void setTotalAccumulatedIncome(BigDecimal totalAccumulatedIncome) {
        this.totalAccumulatedIncome = totalAccumulatedIncome;
    }

    public BigDecimal getTotalRedIncome() {
        return totalRedIncome;
    }

    public void setTotalRedIncome(BigDecimal totalRedIncome) {
        this.totalRedIncome = totalRedIncome;
    }
}