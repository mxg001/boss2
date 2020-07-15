package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户实体
 * 对应表 redemption_merchant_info
 */
public class ExchangeActivateUser {

    private Long id;

    private String merchantNo;//商户号

    private String wxOpenid;//公众号openid

    private String unionid;//微信标识

    private String oneAgentNo;//一级代理商编号
    private String oneAgentName;

    private String agentNo;//所属代理商编号
    private String agentName;

    private String agentNode;//所属代理商节点

    private Date  createTime;//创建时间
    private Date  createTimeBegin;
    private Date  createTimeEnd;

    private String  realName;//真实姓名

    private String  idCardNo;//身份证号

    private String userName;//姓名

    private String mobileUsername;//手机号码

    private String mobilePassword;//密码

    private String merAttach;//附件

    private int merAccount;//商户是否已在账户(1为是0为否)

    private String headimgurl;//微信头像

    private String wechatQrcode;//微信二维码图片名称,文件存在阿里云中

    private String status;//商户状态 1 正常 2 不进不出 3 只进不出

    private BigDecimal freezeAmount;//预冻结金额字段

    private Date  lastUpdateTime;//最后修改时间

    private String oemNo;//组织编号

    private String oemName;//组织名称

    private String nickname;//昵称

    private String weixinhao;//微信号

    private BigDecimal totalBalance;//总额

    private BigDecimal freezeAmountBalance;//冻结金额

    private String freezeRemark;//预冻结备注

    private String freezeAmountState;//预冻结金额状态


    private String receiveMerchantNo;//收款商户号

    private String merchantStatus;//进件状态(1待进件2审核中3审核通过)

    private  Date successTime;//进件成功时间
    private  Date successTimeBegin;
    private  Date successTimeEnd;

    private String repayMerchantNo;//还款商户号

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

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
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

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getWechatQrcode() {
        return wechatQrcode;
    }

    public void setWechatQrcode(String wechatQrcode) {
        this.wechatQrcode = wechatQrcode;
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

    public String getFreezeRemark() {
        return freezeRemark;
    }

    public void setFreezeRemark(String freezeRemark) {
        this.freezeRemark = freezeRemark;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getFreezeAmountState() {
        return freezeAmountState;
    }

    public void setFreezeAmountState(String freezeAmountState) {
        this.freezeAmountState = freezeAmountState;
    }

    public String getReceiveMerchantNo() {
        return receiveMerchantNo;
    }

    public void setReceiveMerchantNo(String receiveMerchantNo) {
        this.receiveMerchantNo = receiveMerchantNo;
    }

    public String getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(String merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Date getSuccessTimeBegin() {
        return successTimeBegin;
    }

    public void setSuccessTimeBegin(Date successTimeBegin) {
        this.successTimeBegin = successTimeBegin;
    }

    public Date getSuccessTimeEnd() {
        return successTimeEnd;
    }

    public void setSuccessTimeEnd(Date successTimeEnd) {
        this.successTimeEnd = successTimeEnd;
    }

    public String getRepayMerchantNo() {
        return repayMerchantNo;
    }

    public void setRepayMerchantNo(String repayMerchantNo) {
        this.repayMerchantNo = repayMerchantNo;
    }
}
