package cn.eeepay.framework.enums;

/**
 * @author tans
 * @date 2020/3/3 11:36
 */
public enum RepayEnum {
    REPAY("repay", "超级还款"),  // 超级还业务
    NFC("nfc", "超级碰一碰");    // nfc业务
    private String type;
    private String businessName;

    RepayEnum(String type, String businessName) {
        this.type = type;
        this.businessName = businessName;
    }

    public String getType() {
        return type;
    }
}

