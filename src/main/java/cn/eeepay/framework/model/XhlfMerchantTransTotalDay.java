package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新欢乐返商户交易日累计表
 *
 * @author tans
 * @date 2019/9/26 18:07
 *
 */
public class XhlfMerchantTransTotalDay {

    private Integer id;
    private String merchantNo;//商户号
    private Date totalDay;//统计日期
    private BigDecimal totalAmount;//累计金额
    private String type;//统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额
    private Date createTime;
    private Date startDay;
    private Date endDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Date totalDay) {
        this.totalDay = totalDay;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }
}
