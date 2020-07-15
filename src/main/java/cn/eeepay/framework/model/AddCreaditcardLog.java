package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 商户绑定信用卡记录表
 *
 * @author tans
 * @date 2019/9/20 14:27
 */
public class AddCreaditcardLog {

    private Long id;
    private String merchantNo;//商户号
    private String accountNo;//信用卡号
    private String bankName;//银行名称
    private String bankCode;//银行编码
    private Date createTime;//创建时间
    private String bindStatus;//绑定状态,0:否,1:是
    private String encryptAccountNo;//加密后的卡号

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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getEncryptAccountNo() {
        return encryptAccountNo;
    }

    public void setEncryptAccountNo(String encryptAccountNo) {
        this.encryptAccountNo = encryptAccountNo;
    }
}
