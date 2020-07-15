package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 红包订单操作记录表
 */
public class RedOrdersOption {
    private Long id;

    private Long redOrderId;//红包订单id关联red_orders的主键

    private Date createDate;//操作时间

    private String optUserName;//操作人姓名

    private String optContent;//操作内容

    private String reason;//原因

    private String remark;//备注

    private String status;//状态：0关闭，1开启

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedOrderId() {
        return redOrderId;
    }

    public void setRedOrderId(Long redOrderId) {
        this.redOrderId = redOrderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOptUserName() {
        return optUserName;
    }

    public void setOptUserName(String optUserName) {
        this.optUserName = optUserName == null ? null : optUserName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}