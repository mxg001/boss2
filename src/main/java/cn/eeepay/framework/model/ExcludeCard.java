package cn.eeepay.framework.model;

/**
 * 不支持银行列表
 * @author MXG
 * create 2018/09/01
 */
public class ExcludeCard {
    private String id;
    private String bankCode;
    private String bankName;
    private String channelCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
