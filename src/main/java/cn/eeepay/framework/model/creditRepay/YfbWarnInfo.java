package cn.eeepay.framework.model.creditRepay;

/**
 * @author MXG
 * create 2018/10/22
 */
public class YfbWarnInfo {
    private String id;
    private String warnCode;
    private String warnName;
    private String status;
    private String warnTriggerValue;
    private String warnCountTime;
    private String warnPhone;//预警短信通知人,多人以;隔开
    private String warnMsgModel;
    private String createTime;
    private String lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarnCode() {
        return warnCode;
    }

    public void setWarnCode(String warnCode) {
        this.warnCode = warnCode;
    }

    public String getWarnName() {
        return warnName;
    }

    public void setWarnName(String warnName) {
        this.warnName = warnName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarnTriggerValue() {
        return warnTriggerValue;
    }

    public void setWarnTriggerValue(String warnTriggerValue) {
        this.warnTriggerValue = warnTriggerValue;
    }

    public String getWarnCountTime() {
        return warnCountTime;
    }

    public void setWarnCountTime(String warnCountTime) {
        this.warnCountTime = warnCountTime;
    }

    public String getWarnPhone() {
        return warnPhone;
    }

    public void setWarnPhone(String warnPhone) {
        this.warnPhone = warnPhone;
    }

    public String getWarnMsgModel() {
        return warnMsgModel;
    }

    public void setWarnMsgModel(String warnMsgModel) {
        this.warnMsgModel = warnMsgModel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
