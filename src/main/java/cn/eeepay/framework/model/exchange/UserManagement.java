package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户实体
 * 对应表 redemption_merchant_info
 */
public class UserManagement {

    private Long id;

    private String merchantNo;//商户号

    private String unionid;

    private String oneAgentNo;//一级代理商编号

    private String agentNo;//所属代理商编号

    private String agentNode;//所属代理商节点

    private Date  createTime;//创建时间
    private Date  createTimeBegin;
    private Date  createTimeEnd;

    private String userName;//姓名

    private String mobileUsername;//手机号码

    private String mobilePassword;//密码

    private String parMerNo;//上级商户号

    private String merLevel;//级别

    private String merCapa;//商户身份标识

    private String merActStatus;//代理状态 1 未激活 2 已激活

    private String merAttach;//附件

    private int merAccount;//商户是否已在账户(1为是0为否)

    private String status;//商户状态 1 正常 2 不进不出 3 只进不出

    private BigDecimal freezeAmount;//预冻结金额字段

    private Date  lastUpdateTime;//最后修改时间

    private String merNode;//商户节点 id + . 进行标识

    private String oemNo;//组织编号

    private String oemName;//组织名称

    private String nickname;//昵称

    private String weixinhao;//微信号

    private BigDecimal totalBalance;//总额

    private BigDecimal freezeAmountBalance;//冻结金额

    private int subordinate;//直营用户数量

    private int subordinateMoney;//下级代理数量

    private String accountNo;//结算卡号

    private String accountName;//开户名

    private String mobileNo;//银行预留手机号

    private String businessCode;//个人身份证号/企业信用代码

    private String yhkzmUrl;//银行卡正面

    private Date  paymentTime;//支付时间
    private Date  paymentTimeBegin;
    private Date  paymentTimeEnd;

    private String freezeRemark;//预冻结备注

    private String freezeAmountState;//预冻结金额状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getMobilePassword() {
        return mobilePassword;
    }

    public void setMobilePassword(String mobilePassword) {
        this.mobilePassword = mobilePassword;
    }

    public String getParMerNo() {
        return parMerNo;
    }

    public void setParMerNo(String parMerNo) {
        this.parMerNo = parMerNo;
    }

    public String getMerLevel() {
        return merLevel;
    }

    public void setMerLevel(String merLevel) {
        this.merLevel = merLevel;
    }

    public String getMerCapa() {
        return merCapa;
    }

    public void setMerCapa(String merCapa) {
        this.merCapa = merCapa;
    }

    public String getMerActStatus() {
        return merActStatus;
    }

    public void setMerActStatus(String merActStatus) {
        this.merActStatus = merActStatus;
    }

    public String getMerAttach() {
        return merAttach;
    }

    public void setMerAttach(String merAttach) {
        this.merAttach = merAttach;
    }

    public int getMerAccount() {
        return merAccount;
    }

    public void setMerAccount(int merAccount) {
        this.merAccount = merAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMerNode() {
        return merNode;
    }

    public void setMerNode(String merNode) {
        this.merNode = merNode;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWeixinhao() {
        return weixinhao;
    }

    public void setWeixinhao(String weixinhao) {
        this.weixinhao = weixinhao;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getFreezeAmountBalance() {
        return freezeAmountBalance;
    }

    public void setFreezeAmountBalance(BigDecimal freezeAmountBalance) {
        this.freezeAmountBalance = freezeAmountBalance;
    }

    public int getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(int subordinate) {
        this.subordinate = subordinate;
    }

    public int getSubordinateMoney() {
        return subordinateMoney;
    }

    public void setSubordinateMoney(int subordinateMoney) {
        this.subordinateMoney = subordinateMoney;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getYhkzmUrl() {
        return yhkzmUrl;
    }

    public void setYhkzmUrl(String yhkzmUrl) {
        this.yhkzmUrl = yhkzmUrl;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPaymentTimeBegin() {
        return paymentTimeBegin;
    }

    public void setPaymentTimeBegin(Date paymentTimeBegin) {
        this.paymentTimeBegin = paymentTimeBegin;
    }

    public Date getPaymentTimeEnd() {
        return paymentTimeEnd;
    }

    public void setPaymentTimeEnd(Date paymentTimeEnd) {
        this.paymentTimeEnd = paymentTimeEnd;
    }

    public String getFreezeRemark() {
        return freezeRemark;
    }

    public void setFreezeRemark(String freezeRemark) {
        this.freezeRemark = freezeRemark;
    }

    public String getFreezeAmountState() {
        return freezeAmountState;
    }

    public void setFreezeAmountState(String freezeAmountState) {
        this.freezeAmountState = freezeAmountState;
    }
}
