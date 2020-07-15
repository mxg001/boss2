package cn.eeepay.framework.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class YinShangOrder {

    private String bankName;

    private String orderName;

    private String  orderPhone;

    private  String orderIdNo;

    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    private String ysOrder;

    private String ysOrderNo;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderIdNo() {
        return orderIdNo;
    }

    public void setOrderIdNo(String orderIdNo) {
        this.orderIdNo = orderIdNo;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getYsOrder() {
        return ysOrder;
    }

    public void setYsOrder(String ysOrder) {
        this.ysOrder = ysOrder;
    }

    public String getYsOrderNo() {
        return ysOrderNo;
    }

    public void setYsOrderNo(String ysOrderNo) {
        this.ysOrderNo = ysOrderNo;
    }
}
