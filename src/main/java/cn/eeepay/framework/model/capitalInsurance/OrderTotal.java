package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/7/24/024.
 * @author  liuks
 * 订单汇总
 */
public class OrderTotal {

    private Integer countTotal;//保险订单总笔数
    private BigDecimal nPrmTotal;//保费-售价汇总
    private BigDecimal nFeeTotal;//保费-成本价汇总

    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public BigDecimal getnPrmTotal() {
        return nPrmTotal;
    }

    public void setnPrmTotal(BigDecimal nPrmTotal) {
        this.nPrmTotal = nPrmTotal;
    }

    public BigDecimal getnFeeTotal() {
        return nFeeTotal;
    }

    public void setnFeeTotal(BigDecimal nFeeTotal) {
        this.nFeeTotal = nFeeTotal;
    }
}
