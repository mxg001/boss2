package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微创业订单表
 * 
 * @author junhu
 *
 */
public class SupertuiOrder {
    private Long id;

    private String orderId;

    private String merchantId;

    private String bpId;

    private String supertuiRuleId;

    private String orderStatus;

    private String lawyer;

    private String mobilephone;

    private String address;

    private String remark;

    private Date createTime;

    private String merchantNo;

    private BigDecimal recommendedAmount;

    private String orderUserType;

    private String orderUserId;

    private String orderAgentId;

    private BigDecimal orderAgentFee;

    private Date orderTime;

    private Date orderDisabledTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getSupertuiRuleId() {
        return supertuiRuleId;
    }

    public void setSupertuiRuleId(String supertuiRuleId) {
        this.supertuiRuleId = supertuiRuleId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(BigDecimal recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    public String getOrderUserType() {
        return orderUserType;
    }

    public void setOrderUserType(String orderUserType) {
        this.orderUserType = orderUserType;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderAgentId() {
        return orderAgentId;
    }

    public void setOrderAgentId(String orderAgentId) {
        this.orderAgentId = orderAgentId;
    }

    public BigDecimal getOrderAgentFee() {
        return orderAgentFee;
    }

    public void setOrderAgentFee(BigDecimal orderAgentFee) {
        this.orderAgentFee = orderAgentFee;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getOrderDisabledTime() {
        return orderDisabledTime;
    }

    public void setOrderDisabledTime(Date orderDisabledTime) {
        this.orderDisabledTime = orderDisabledTime;
    }
}