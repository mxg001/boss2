package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.recharge_record
 * 超级银行家账户充值记录表
 * @author tans
 * @date 2017/1/10
 */
public class RechargeRecord {
    private Integer id;

    private Date createDate;//创建时间

    private BigDecimal amountBalance;//充值前余额

    private BigDecimal rechargeAmount;//充值前余额

    private Integer userId;//操作人ID

    private String userName;//操作人名称

    private String createDateStart;
    private String createDateEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(BigDecimal amountBalance) {
        this.amountBalance = amountBalance;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }
}