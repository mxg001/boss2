package cn.eeepay.framework.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author tans
 * @date 2017-12-8
 * 账户记录
 */
public class AccountInfoRecord {
    private String id;
    private String serialNo;
    private String balance;
    private String avaliBalance;
    private String recordDate;
    private String childSerialNo;
    private String recordAmount;
    private String accountNo;
    private String recordTime;
    private String debitCreditSide;

    private String recordTimeStart;
    private String recordTimeEnd;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvaliBalance() {
        return avaliBalance;
    }

    public void setAvaliBalance(String avaliBalance) {
        this.avaliBalance = avaliBalance;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getChildSerialNo() {
        return childSerialNo;
    }

    public void setChildSerialNo(String childSerialNo) {
        this.childSerialNo = childSerialNo;
    }

    public String getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(String recordAmount) {
        this.recordAmount = recordAmount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getDebitCreditSide() {
        return debitCreditSide;
    }

    public void setDebitCreditSide(String debitCreditSide) {
        this.debitCreditSide = debitCreditSide;
    }

    public String getRecordTimeStart() {
        return recordTimeStart;
    }

    public void setRecordTimeStart(String recordTimeStart) {
        this.recordTimeStart = recordTimeStart;
    }

    public String getRecordTimeEnd() {
        return recordTimeEnd;
    }

    public void setRecordTimeEnd(String recordTimeEnd) {
        this.recordTimeEnd = recordTimeEnd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
