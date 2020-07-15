package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/27.
 */
public class ZqMerchantLog {

    private Long id;

    private Long merServiceId;

    private String syncStatus;

    private String channelCode;

    private String unionpayMerNo;

    private String terminalNo;

    private String syncRemark;

    private Date createTime;

    private String operator;

    private String syncRequires;

    private String serviceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerServiceId() {
        return merServiceId;
    }

    public void setMerServiceId(Long merServiceId) {
        this.merServiceId = merServiceId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSyncRequires() {
        return syncRequires;
    }

    public void setSyncRequires(String syncRequires) {
        this.syncRequires = syncRequires;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
