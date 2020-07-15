package cn.eeepay.framework.model;

import java.util.Date;

public class TransInfoFreezeNewLog {
    private Integer id;

    private String orderNo;

    private String transType;

    private String operType;

    private String operReason;

    private String freezeWay;

    private String freezeDay;

    private Date operTime;

    private String operId;

    private String operName;

    private Date settleTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType == null ? null : operType.trim();
    }

    public String getOperReason() {
        return operReason;
    }

    public void setOperReason(String operReason) {
        this.operReason = operReason == null ? null : operReason.trim();
    }

    public String getFreezeWay() {
        return freezeWay;
    }

    public void setFreezeWay(String freezeWay) {
        this.freezeWay = freezeWay == null ? null : freezeWay.trim();
    }

    public String getFreezeDay() {
        return freezeDay;
    }

    public void setFreezeDay(String freezeDay) {
        this.freezeDay = freezeDay == null ? null : freezeDay.trim();
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId == null ? null : operId.trim();
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }
}