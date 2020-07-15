package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 用户余额表 rdmp_balance
 */
public class UserManagementBalance {

    private Integer id;

    private String balanceNo;//钱包账户编号

    private String  merNo;//业务商户号

    private String status;//状态  0锁定  1正常

    private BigDecimal balance;//余额

    private BigDecimal  totalBalance;//总额

    private BigDecimal settleBalance;//已结算

    private Date createTime;//创建时间

    private Date lastUpdateTime;//最后更新时间

    private BigDecimal freezeAmount;//冻结金额字段

    private Integer rowLock;//行锁标识(操作次数)


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBalanceNo() {
        return balanceNo;
    }

    public void setBalanceNo(String balanceNo) {
        this.balanceNo = balanceNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getSettleBalance() {
        return settleBalance;
    }

    public void setSettleBalance(BigDecimal settleBalance) {
        this.settleBalance = settleBalance;
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

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Integer getRowLock() {
        return rowLock;
    }

    public void setRowLock(Integer rowLock) {
        this.rowLock = rowLock;
    }
}
