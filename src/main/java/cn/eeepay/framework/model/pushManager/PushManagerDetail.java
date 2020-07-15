package cn.eeepay.framework.model.pushManager;

/***
 * table push_manager_detail
 */
public class PushManagerDetail {

    private Long id;
    private String merchantNo;
    private Integer pushStatus;
    private String mobileId;
    private Long pushId;
    private Integer pushAll;
    private String msgId;
    private String msgResult;
    private String pushObj;


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

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public Long getPushId() {
        return pushId;
    }

    public void setPushId(Long pushId) {
        this.pushId = pushId;
    }

    public Integer getPushAll() {
        return pushAll;
    }

    public void setPushAll(Integer pushAll) {
        this.pushAll = pushAll;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgResult() {
        return msgResult;
    }

    public void setMsgResult(String msgResult) {
        this.msgResult = msgResult;
    }

    public String getPushObj() {
        return pushObj;
    }

    public void setPushObj(String pushObj) {
        this.pushObj = pushObj;
    }
}
