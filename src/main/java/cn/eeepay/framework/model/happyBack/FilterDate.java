package cn.eeepay.framework.model.happyBack;

import java.util.Date;

/**
 * Created by Administrator on 2018/12/27/027.
 * @author  liuks
 * 欢乐返统计,条件过滤实体
 */
public class FilterDate {

    private Date startDate;//开始
    private Date endDate;//结束


    private Date nowDate;//执行时间
    private String merchantNo;//商户编号
    private int timeNull;//minOverTime 是否为空 0为空 1不为空


    private String countTradeScope;//交易类型过滤

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getNowDate() {
        return nowDate;
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = nowDate;
    }

    public int getTimeNull() {
        return timeNull;
    }

    public void setTimeNull(int timeNull) {
        this.timeNull = timeNull;
    }

    public String getCountTradeScope() {
        return countTradeScope;
    }

    public void setCountTradeScope(String countTradeScope) {
        this.countTradeScope = countTradeScope;
    }

    @Override
    public String toString() {
        return "FilterDate{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", nowDate=" + nowDate +
                ", merchantNo='" + merchantNo + '\'' +
                ", timeNull=" + timeNull +
                ", countTradeScope='" + countTradeScope + '\'' +
                '}';
    }
}
