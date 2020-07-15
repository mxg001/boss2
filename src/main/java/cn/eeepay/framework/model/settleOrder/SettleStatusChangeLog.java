package cn.eeepay.framework.model.settleOrder;


import java.util.Date;

/***
 * table settle_status_change_log
 * 订单变更状态日志表
 */
public class SettleStatusChangeLog {

    private Long id;
    private Integer orderId;//'订单号'
    private String oldSettleStatus;//'变更前的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算'
    private String currSettleStatus;//'变更前的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算'
    private String operatePerson;//'操作人',
    private Date operateTime;//操作时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOldSettleStatus() {
        return oldSettleStatus;
    }

    public void setOldSettleStatus(String oldSettleStatus) {
        this.oldSettleStatus = oldSettleStatus;
    }

    public String getCurrSettleStatus() {
        return currSettleStatus;
    }

    public void setCurrSettleStatus(String currSettleStatus) {
        this.currSettleStatus = currSettleStatus;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
