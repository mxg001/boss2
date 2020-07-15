package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/17/017.
 * @author  liuks
 * 用户结算卡
 */
public class ExchangeActivateUserCard {
    private Long id;

    private String merNo;//商户号

    private String accountNo;//开户账号

    private String accountName;//开户名

    private String businessCode;//个人身份证号/企业信用代码

    private String cardType;//卡类型（借记卡DEBIT/贷记卡CREDIT）

    private String accountType;//账户类型（2对公、1对私）

    private String bankNo;//清算行号
    private String bankName;//银行名称
    private String bankCode;//银行代码
    private String cnaps;//联行行号

    private String accountProvince;//开户行地区：省
    private String accountCity;//开户行地区：市
    private String accountDistrict;//开户行地区：区

    private Date   createTime;//创建时间

    private String yhkzmUrl;//银行卡正面URL

    private String zhName;//支行

    private String isSettle;//是否结算卡 1 是 2 否

    private Date   lastUpdateTime;//创建时间

    private String mobileNo;//银行留的手机号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
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

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCnaps() {
        return cnaps;
    }

    public void setCnaps(String cnaps) {
        this.cnaps = cnaps;
    }

    public String getAccountProvince() {
        return accountProvince;
    }

    public void setAccountProvince(String accountProvince) {
        this.accountProvince = accountProvince;
    }

    public String getAccountCity() {
        return accountCity;
    }

    public void setAccountCity(String accountCity) {
        this.accountCity = accountCity;
    }

    public String getAccountDistrict() {
        return accountDistrict;
    }

    public void setAccountDistrict(String accountDistrict) {
        this.accountDistrict = accountDistrict;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getYhkzmUrl() {
        return yhkzmUrl;
    }

    public void setYhkzmUrl(String yhkzmUrl) {
        this.yhkzmUrl = yhkzmUrl;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(String isSettle) {
        this.isSettle = isSettle;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
