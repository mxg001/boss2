package cn.eeepay.framework.model;

public class HappyBackBilling {
    private String activeOrder;
    private String billingStatusText;
    private String billingMsg;

    public String getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(String activeOrder) {
        this.activeOrder = activeOrder;
    }

    public String getBillingStatusText() {
        return billingStatusText;
    }

    public void setBillingStatusText(String billingStatusText) {
        this.billingStatusText = billingStatusText;
    }

    public String getBillingMsg() {
        return billingMsg;
    }

    public void setBillingMsg(String billingMsg) {
        this.billingMsg = billingMsg;
    }
}
