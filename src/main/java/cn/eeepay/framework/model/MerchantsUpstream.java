package cn.eeepay.framework.model;

/**
 * @author liuks
 * 商户上游账户查询查询实体
 * zq_merchant_info
 */
public class MerchantsUpstream {

    private int id;//id

    private int rowNo;//序号

    private String channelCode;//通道编码

    private String unionpayMerNo;//银联报备商户号

    private String mbpId;//商户进件编号

    private String merchantNo;//商户编号

    private String merchantName;//商户名称

    private String mobilephone;//手机号

    //查询回填数据
    private String balance;//账户余额

    private String bankAccount;//银行账号

    private String bankCode;//银行编码

    private String bankName;//银行名称

    private String wxRates;//微信汇率档

    private String zfbRates;//支付宝汇率档

    private String result;//查询结果

    private String errorMsg;//错误描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getUnionpayMerNo() {
        return unionpayMerNo;
    }

    public void setUnionpayMerNo(String unionpayMerNo) {
        this.unionpayMerNo = unionpayMerNo;
    }

    public String getMbpId() {
        return mbpId;
    }

    public void setMbpId(String mbpId) {
        this.mbpId = mbpId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getWxRates() {
        return wxRates;
    }

    public void setWxRates(String wxRates) {
        this.wxRates = wxRates;
    }

    public String getZfbRates() {
        return zfbRates;
    }

    public void setZfbRates(String zfbRates) {
        this.zfbRates = zfbRates;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
