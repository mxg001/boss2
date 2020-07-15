package cn.eeepay.framework.model.pushManager;

import java.util.Date;

/***
 *  table push_manager
 */
public class PushManager {


    private Long id;
    private String pushTitle; //'推送标题'
    private String pushContent;//'推送内容'
    private String jumpUrl;//'跳转链接'
    private String pushObj;//'推送对象'
    private String pushObjName;//推送对象名称
    private Integer mobileTerminalType;//'移动端类型 0:全部 1:ios 2:android'
    private Integer targetUser;//'目标用户 0:全部 1:部分'
    private Date pushTime;//'推送时间'
    private Date pushTimeBegin;
    private Date pushTimeEnd;
    private Date timerTime;//'定时推送时间'
    private Date timerTimeBegin;//'查询 定时推送时间'
    private Date timerTimeEnd;//'查询 定时推送时间'
    private Date actualTime;//实际推送时间
    private Integer pushStatus;//'推送状态 0:未推送 1:已推送 2:推送失败',
    private String createPerson;//'创建人'
    private Date createTime;//'创建时间'
    private Date createTimeBegin;
    private Date createTimeEnd;
    private Integer dingshiOrNow;//定时或者实时 0:定时 1:实时
    private String errMsg;//失败原因
    private Integer pushTimes;//推送次数




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getPushObj() {
        return pushObj;
    }

    public void setPushObj(String pushObj) {
        this.pushObj = pushObj;
    }

    public Integer getMobileTerminalType() {
        return mobileTerminalType;
    }

    public void setMobileTerminalType(Integer mobileTerminalType) {
        this.mobileTerminalType = mobileTerminalType;
    }

    public Integer getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Integer targetUser) {
        this.targetUser = targetUser;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getPushTimeBegin() {
        return pushTimeBegin;
    }

    public void setPushTimeBegin(Date pushTimeBegin) {
        this.pushTimeBegin = pushTimeBegin;
    }

    public Date getPushTimeEnd() {
        return pushTimeEnd;
    }

    public void setPushTimeEnd(Date pushTimeEnd) {
        this.pushTimeEnd = pushTimeEnd;
    }

    public Date getTimerTime() {
        return timerTime;
    }

    public void setTimerTime(Date timerTime) {
        this.timerTime = timerTime;
    }

    public Date getActualTime() {
        return actualTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Integer getDingshiOrNow() {
        return dingshiOrNow;
    }

    public void setDingshiOrNow(Integer dingshiOrNow) {
        this.dingshiOrNow = dingshiOrNow;
    }

    public String getPushObjName() {
        return pushObjName;
    }

    public void setPushObjName(String pushObjName) {
        this.pushObjName = pushObjName;
    }

    public Date getTimerTimeBegin() {
        return timerTimeBegin;
    }

    public void setTimerTimeBegin(Date timerTimeBegin) {
        this.timerTimeBegin = timerTimeBegin;
    }

    public Date getTimerTimeEnd() {
        return timerTimeEnd;
    }

    public void setTimerTimeEnd(Date timerTimeEnd) {
        this.timerTimeEnd = timerTimeEnd;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getPushTimes() {
        return pushTimes;
    }

    public void setPushTimes(Integer pushTimes) {
        this.pushTimes = pushTimes;
    }
}
