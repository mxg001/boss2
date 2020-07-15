package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table：super.user_card_record
 * desc:超级银行家用户结算卡更改记录
 * @author tans
 * @date 2017-12-09
 */
public class SuperBankUserCardRecord {
    private Long id;

    private Long userCode;//用户编号

    private Date recordDate;//记录时间

    private String accountPhone;//结算卡对应的手机号

    private String cardNo;//结算银行卡号

    private String bankName;//结算银行名称

    private String bankBranchName;//结算银行支行名称

    private String bankAdress;//结算银行支行地址

    private String updateSource;//修改来源，1：boss，2：h5

    private String updateBy;//修改人

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

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
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

    public String getBankAdress() {
        return bankAdress;
    }

    public void setBankAdress(String bankAdress) {
        this.bankAdress = bankAdress == null ? null : bankAdress.trim();
    }

    public String getUpdateSource() {
        return updateSource;
    }

    public void setUpdateSource(String updateSource) {
        this.updateSource = updateSource == null ? null : updateSource.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }
}