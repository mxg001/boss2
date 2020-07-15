package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 订单事件记录表
 * 对应表 order_event
 */
public class OrderEventInfo {

    private  Long id;
    private  String transOrderNo;//交易订单号
    private  String eventRemark;//事件描述
    private  Integer activityId; //活动编号activity_id,对应 activity_hardware 表 id
    private  String activityName;//名称
    private  String amountRemark;//金额变动文本描述
    private Date createTime;//创建时间



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getEventRemark() {
        return eventRemark;
    }

    public void setEventRemark(String eventRemark) {
        this.eventRemark = eventRemark;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getAmountRemark() {
        return amountRemark;
    }

    public void setAmountRemark(String amountRemark) {
        this.amountRemark = amountRemark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
