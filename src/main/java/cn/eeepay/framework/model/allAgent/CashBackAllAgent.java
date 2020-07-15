package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;
import java.util.Date;

public class CashBackAllAgent {
    private String activeOrder;
    private String userName;
    private String userCode;
    private String brandCode;
    private String oneUserCode;
    private String oneUserName;
    private String activityCode;
    private String activityTypeNo;
    private BigDecimal transAmount;
    private BigDecimal cashBackAmount;
    private String entryStatus;
    private Date activityTime;
    private Date entryTime;
    private String remark;

    private String startTime;
    private String endTime;
    private String activityTimeStart;
    private String activityTimeEnd;
    private String merchantNo;
    private String oneAgentNo;

    public String getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(String activeOrder) {
        this.activeOrder = activeOrder;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }

    public String getOneUserName() {
        return oneUserName;
    }

    public void setOneUserName(String oneUserName) {
        this.oneUserName = oneUserName;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public BigDecimal getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(BigDecimal cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActivityTimeStart() {
        return activityTimeStart;
    }

    public void setActivityTimeStart(String activityTimeStart) {
        this.activityTimeStart = activityTimeStart;
    }

    public String getActivityTimeEnd() {
        return activityTimeEnd;
    }

    public void setActivityTimeEnd(String activityTimeEnd) {
        this.activityTimeEnd = activityTimeEnd;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }
}
