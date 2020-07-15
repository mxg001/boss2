package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 用户提现记录实体
 * 对应表 yfb_extraction_his
 */
public class ExchangeActivateWithdrawals {
    private  long id;

    private String orderNo;//提现编号

    private String merNo;//商户号

    private String accNo;//账号

    private String accName;//户名

    private String  serialNo;//出款流水号

    private String mobileNo;//手机号

    private BigDecimal amount;//提现金额(扣手续费前金额)
    private BigDecimal amountBegin;
    private BigDecimal amountEnd;

    private BigDecimal fee;//手续费

    private String status;//状态  0-初始化 1-提现中 2-提现成功 3-提现失败

    private String resultMsg;//提现结果描述

    private String isReverse;//是否需要冲正1 需要 0 不需要

    private String reverseStatus;//冲正状态 1 冲正成功，2 冲正失败

    private String orderData;//订单数据

    private String tradeSource;//来源 默认路由default

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Date lastUpdateTime;//最后更新时间

    private String userName;//昵称

    private BigDecimal amountWithdrawals;//出款金额

    private String oemNo;//组织编码
    private String oemName;//组织名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountBegin() {
        return amountBegin;
    }

    public void setAmountBegin(BigDecimal amountBegin) {
        this.amountBegin = amountBegin;
    }

    public BigDecimal getAmountEnd() {
        return amountEnd;
    }

    public void setAmountEnd(BigDecimal amountEnd) {
        this.amountEnd = amountEnd;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getIsReverse() {
        return isReverse;
    }

    public void setIsReverse(String isReverse) {
        this.isReverse = isReverse;
    }

    public String getReverseStatus() {
        return reverseStatus;
    }

    public void setReverseStatus(String reverseStatus) {
        this.reverseStatus = reverseStatus;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(String tradeSource) {
        this.tradeSource = tradeSource;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getAmountWithdrawals() {
        return amountWithdrawals;
    }

    public void setAmountWithdrawals(BigDecimal amountWithdrawals) {
        this.amountWithdrawals = amountWithdrawals;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }
}
