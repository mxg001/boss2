package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/20/020.
 * @author  liuks
 * 红包操作记录
 *对应表 red_orders_option
 */
public class RedEnvelopesGrantOption {
    private Long id;

    private Long redOrderId;//红包订单id关联red_orders的主键

    private Date createDate;//操作时间

    private String optUserName;//操作人姓名

    private String optContent;//操作内容

    private String reason;//原因

    private String remark;//备注

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
        this.optUserName = optUserName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
