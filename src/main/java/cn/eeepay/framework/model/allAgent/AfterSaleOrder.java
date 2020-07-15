package cn.eeepay.framework.model.allAgent;

import java.util.Date;

public class AfterSaleOrder {
    private Integer id;
    private String userCode;
    private String orderNo;
    private String payOrder;
    private String saleType;
    private String applyDesc;
    private String applyImg;
    private Date applyTime;
    private String dealDesc;
    private String dealImg;
    private Date dealTime;
    private Integer status;
    private Date createTime;
    private Integer handler;
    private Integer ascription;

    private String applyStartTime;
    private String applyEndTime;
    private String dealStartTime;
    private String dealEndTime;

    private Integer shipWay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(String payOrder) {
        this.payOrder = payOrder;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc;
    }

    public String getApplyImg() {
        return applyImg;
    }

    public void setApplyImg(String applyImg) {
        this.applyImg = applyImg;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public String getDealImg() {
        return dealImg;
    }

    public void setDealImg(String dealImg) {
        this.dealImg = dealImg;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getHandler() {
        return handler;
    }

    public void setHandler(Integer handler) {
        this.handler = handler;
    }

    public String getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(String applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getDealStartTime() {
        return dealStartTime;
    }

    public void setDealStartTime(String dealStartTime) {
        this.dealStartTime = dealStartTime;
    }

    public String getDealEndTime() {
        return dealEndTime;
    }

    public void setDealEndTime(String dealEndTime) {
        this.dealEndTime = dealEndTime;
    }

    public Integer getShipWay() {
        return shipWay;
    }

    public void setShipWay(Integer shipWay) {
        this.shipWay = shipWay;
    }

    public Integer getAscription() {
        return ascription;
    }

    public void setAscription(Integer ascription) {
        this.ascription = ascription;
    }
}
