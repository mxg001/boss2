package cn.eeepay.framework.model.allAgent;

import java.util.Date;

public class TerminalBack {
    private Integer id;
    private String userCode;
    private String receiveUserCode;
    private Integer status;
    private Date createTime;
    private Date lastUpdateTime;
    private String orderNo;

    private String oneUserCode;
    private Integer receiveUserType;
    private String backStartTime;
    private String backEndTime;
    private String receiveStartTime;
    private String receiveEndTime;
    private Integer count;

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

    public String getReceiveUserCode() {
        return receiveUserCode;
    }

    public void setReceiveUserCode(String receiveUserCode) {
        this.receiveUserCode = receiveUserCode;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }

    public Integer getReceiveUserType() {
        return receiveUserType;
    }

    public void setReceiveUserType(Integer receiveUserType) {
        this.receiveUserType = receiveUserType;
    }

    public String getBackStartTime() {
        return backStartTime;
    }

    public void setBackStartTime(String backStartTime) {
        this.backStartTime = backStartTime;
    }

    public String getBackEndTime() {
        return backEndTime;
    }

    public void setBackEndTime(String backEndTime) {
        this.backEndTime = backEndTime;
    }

    public String getReceiveStartTime() {
        return receiveStartTime;
    }

    public void setReceiveStartTime(String receiveStartTime) {
        this.receiveStartTime = receiveStartTime;
    }

    public String getReceiveEndTime() {
        return receiveEndTime;
    }

    public void setReceiveEndTime(String receiveEndTime) {
        this.receiveEndTime = receiveEndTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
