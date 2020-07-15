package cn.eeepay.framework.model;

public class MbpStatusCondition {

    private Integer val;//操作状态

    private Boolean bankCardStatus;//银行卡状态

    private Boolean addressStatus;//开户行全称，开户地址

    private Boolean reexamineStatus;//复审退件操作

    private Boolean appStatus;//APP计数3次直接只走人工

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Boolean getBankCardStatus() {
        return bankCardStatus;
    }

    public void setBankCardStatus(Boolean bankCardStatus) {
        this.bankCardStatus = bankCardStatus;
    }

    public Boolean getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(Boolean addressStatus) {
        this.addressStatus = addressStatus;
    }

    public Boolean getReexamineStatus() {
        return reexamineStatus;
    }

    public void setReexamineStatus(Boolean reexamineStatus) {
        this.reexamineStatus = reexamineStatus;
    }

    public Boolean getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Boolean appStatus) {
        this.appStatus = appStatus;
    }
}
