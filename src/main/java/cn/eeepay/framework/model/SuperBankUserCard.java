package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class SuperBankUserCard {
    private Long id;

    private Long userCode;//用户编号

    private Date createDate;//创建时间

    private String accountName;//开户名

    private String accountPhone;//开户手机号

    private String accountIdNo;//结算卡对应的手机号

    private String cardNo;//结算银行卡号

    private String cnapsNo;//联行行号

    private String bankName;//结算银行名称

    private String bankBranchName;//结算银行支行名称

    private String bankProvince;//结算银行省份

    private String bankCity;//结算银行城市

    private String bankDistrict;//结算卡银行所在区

    private String bankAdress;//结算银行支行地址

    private String status;//状态

    private String positivePhoto;//银行卡正面照片地址

    private String positivePhotoUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(String cnapsNo) {
        this.cnapsNo = cnapsNo == null ? null : cnapsNo.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName == null ? null : bankBranchName.trim();
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince == null ? null : bankProvince.trim();
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity == null ? null : bankCity.trim();
    }

    public String getBankAdress() {
        return bankAdress;
    }

    public void setBankAdress(String bankAdress) {
        this.bankAdress = bankAdress == null ? null : bankAdress.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getPositivePhoto() {
        return positivePhoto;
    }

    public void setPositivePhoto(String positivePhoto) {
        this.positivePhoto = positivePhoto == null ? null : positivePhoto.trim();
    }

    public String getPositivePhotoUrl() {
        return positivePhotoUrl;
    }

    public void setPositivePhotoUrl(String positivePhotoUrl) {
        this.positivePhotoUrl = positivePhotoUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getAccountIdNo() {
        return accountIdNo;
    }

    public void setAccountIdNo(String accountIdNo) {
        this.accountIdNo = accountIdNo;
    }

    public String getBankDistrict() {
        return bankDistrict;
    }

    public void setBankDistrict(String bankDistrict) {
        this.bankDistrict = bankDistrict;
    }
}