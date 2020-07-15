package cn.eeepay.framework.model;

import java.util.Date;

public class PosCardBin {
    private Integer id;

    private String bankName;

    private String cardName;

    private Integer cardLength;

    private String cardType;

    private Integer verifyLength;

    private Long verifyCode;

    private Date createTime;

    private String bankNo;

    private String bankDesc;

    private String bankCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName == null ? null : cardName.trim();
    }

    public Integer getCardLength() {
        return cardLength;
    }

    public void setCardLength(Integer cardLength) {
        this.cardLength = cardLength;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public Integer getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(Integer verifyLength) {
        this.verifyLength = verifyLength;
    }

    public Long getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(Long verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getBankDesc() {
        return bankDesc;
    }

    public void setBankDesc(String bankDesc) {
        this.bankDesc = bankDesc == null ? null : bankDesc.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }
}