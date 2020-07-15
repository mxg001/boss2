package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransInfoFreezeQueryCollection {
    private Integer id;

    private String merchantNo;

    //商户冻结状态
    private String riskStatus;

    //商户状态
    private String merStatus;

    private String mobilephone;

    private String sTime;

    private String eTime;

    private String merchantName;

    private BigDecimal operMoney;

    private Date operTime;

    private String operName;

    private String operType;

    private String operLog;

    private String operReason;

    private String orderNo;

    private String bool;

    private String queryMode;
    //预冻结需要将日志转换的金额字段
    private String operStr;

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

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMerStatus() {
        return merStatus;
    }

    public void setMerStatus(String merStatus) {
        this.merStatus = merStatus;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getOperMoney() {
        return operMoney;
    }

    public void setOperMoney(BigDecimal operMoney) {
        this.operMoney = operMoney;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperLog() {
        return operLog;
    }

    public void setOperLog(String operLog) {
        this.operLog = operLog;
    }

    public String getOperReason() {
        return operReason;
    }

    public void setOperReason(String operReason) {
        this.operReason = operReason;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBool() {
        return bool;
    }

    public void setBool(String bool) {
        this.bool = bool;
    }

    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public String getOperStr() {
        return operStr;
    }

    public void setOperStr(String operStr) {
        this.operStr = operStr;
    }
}