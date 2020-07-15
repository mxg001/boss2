package cn.eeepay.framework.model;

import java.util.Date;

public class MerchantService {
    private Long id;

    private String bpId;

    private String merchantNo;

    private String serviceId;

    private Date createDate;

    private Date disabledDate;

    private String status;

    private String serviceType;

    private String serviceName;

    private String tradeType;

    private String channelCode;

    private String syncStatus;

    private String unionpayMerNo;

    private String terminalNo;

    private String syncRemark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getUnionpayMerNo() {
        return unionpayMerNo;
    }

    public void setUnionpayMerNo(String unionpayMerNo) {
        this.unionpayMerNo = unionpayMerNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getSyncRemark() {
        return syncRemark;
    }

    public void setSyncRemark(String syncRemark) {
        this.syncRemark = syncRemark;
    }
}