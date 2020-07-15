package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * oem 实体
 * 对应表yfb_oem_service
 */
public class ExchangeActivateOem {

    private long id;//id

    private String oemNo;//OEM编号

    private String oemName;//OEM名称

    private String oemType;//OEM类型

    private Date createTime;//创建时间

    private String companyNo;//主体编号

    private String companyName;//主体名称

    private String servicePhone;//主体客服电话

    private String androidAppKey;//极光推送android key

    private String androidMasterSecret;//极光推送android secret

    private String iosAppKey;//极光推送ios key

    private String iosMasterSecret;//极光推送ios secret

    private String publicAccount;//公众号账号

    private String publicAccountName;//公众号名称

    private String appid;//公众号appid

    private String secret;//公众号secret

    private String encodingAesKey;//公众号密文

    private String wxToken;//公众号token

    private String wxTicket;//公众号ticket

    private String remark;//备注信息

    private String mailbox;//商务邮箱

    private String agreement;//平台服务协议

    private  String agentNo;//一级代理商编码

    private String teamId;//一级代理商组织

    private  String clientAppid;//客户端appid
    private  String clientSecret;//客户端secret

    private  String repaymentOemNo;//对应超级还组织编号
    private  String  receiveOemNo;//对应V2商户收款组织编号

    private String openOemState;//开通超级还oem状态
    private String agentAccount;//代理商是否已在账户(1为是0为否)


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getOemType() {
        return oemType;
    }

    public void setOemType(String oemType) {
        this.oemType = oemType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getAndroidAppKey() {
        return androidAppKey;
    }

    public void setAndroidAppKey(String androidAppKey) {
        this.androidAppKey = androidAppKey;
    }

    public String getAndroidMasterSecret() {
        return androidMasterSecret;
    }

    public void setAndroidMasterSecret(String androidMasterSecret) {
        this.androidMasterSecret = androidMasterSecret;
    }

    public String getIosAppKey() {
        return iosAppKey;
    }

    public void setIosAppKey(String iosAppKey) {
        this.iosAppKey = iosAppKey;
    }

    public String getIosMasterSecret() {
        return iosMasterSecret;
    }

    public void setIosMasterSecret(String iosMasterSecret) {
        this.iosMasterSecret = iosMasterSecret;
    }

    public String getPublicAccount() {
        return publicAccount;
    }

    public void setPublicAccount(String publicAccount) {
        this.publicAccount = publicAccount;
    }

    public String getPublicAccountName() {
        return publicAccountName;
    }

    public void setPublicAccountName(String publicAccountName) {
        this.publicAccountName = publicAccountName;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    public String getWxTicket() {
        return wxTicket;
    }

    public void setWxTicket(String wxTicket) {
        this.wxTicket = wxTicket;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getClientAppid() {
        return clientAppid;
    }

    public void setClientAppid(String clientAppid) {
        this.clientAppid = clientAppid;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRepaymentOemNo() {
        return repaymentOemNo;
    }

    public void setRepaymentOemNo(String repaymentOemNo) {
        this.repaymentOemNo = repaymentOemNo;
    }

    public String getReceiveOemNo() {
        return receiveOemNo;
    }

    public void setReceiveOemNo(String receiveOemNo) {
        this.receiveOemNo = receiveOemNo;
    }

    public String getOpenOemState() {
        return openOemState;
    }

    public void setOpenOemState(String openOemState) {
        this.openOemState = openOemState;
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount;
    }
}
