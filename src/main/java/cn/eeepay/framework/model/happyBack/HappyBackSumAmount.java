package cn.eeepay.framework.model.happyBack;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/12/27/027.
 * @author  liuks
 * 欢乐返统计金额
 */
public class HappyBackSumAmount {

    private String merchantNo;//商户编号
    private BigDecimal total;//汇总金额

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
