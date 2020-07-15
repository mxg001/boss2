package cn.eeepay.framework.model.exchange;

/**
 * Created by Administrator on 2018/4/9/009.
 * @author  liuks
 * 结算卡修改实体
 */
public class SettlementCard {

    private String merchantNo;//商户号

    private String cardNo;//开户账号

    private String bankName;//银行名称

    private String accountPhone;//银行留的手机号

    private String bankProvince;//开户行地区:省

    private String bankCity;//开户行地区:市

    private String cnapsNo;//支行


    private String oldCard;//老卡号

    private  String operator;//操作人

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(String cnapsNo) {
        this.cnapsNo = cnapsNo;
    }

    public String getOldCard() {
        return oldCard;
    }

    public void setOldCard(String oldCard) {
        this.oldCard = oldCard;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
