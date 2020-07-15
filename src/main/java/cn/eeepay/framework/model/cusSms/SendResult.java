package cn.eeepay.framework.model.cusSms;

public class SendResult {

    private int templateId;//短信模板id
    private String sendType;//发送类型
    private String sendStr;//手机号码/商户编号

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendStr() {
        return sendStr;
    }

    public void setSendStr(String sendStr) {
        this.sendStr = sendStr;
    }
}
