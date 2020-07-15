package cn.eeepay.framework.model.luckDraw;


import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/7/007.
 * @author  liuks
 * 抽奖记录
 * 对应表 awards_recode
 */
public class LuckDrawOrder {

    private Integer id;//
    private Integer awardsConfigId;//奖品信息配置
    private String seq;//奖品码
    private String awardDesc;//奖项说明
    private String awardName;//奖品名称
    private String activityName;//活动名称
    private Integer status;//状态 0:未抽奖，1:已中奖，2:未中奖，3:已发奖
    private String mobilephone;//手机
    private String userCode;//用户ID(商户编号)
    private Date  playTime;//抽奖时间
    private Date  playTimeBegin;
    private Date  playTimeEnd;

    private Date createTime;//创建时间
    private Date updateTime;//修改时间
    private Integer awardType;//奖品类型1鼓励金2超级积分3现金券

    private String userName;//商户名称


    private List<LuckDrawEntry> entryList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAwardsConfigId() {
        return awardsConfigId;
    }

    public void setAwardsConfigId(Integer awardsConfigId) {
        this.awardsConfigId = awardsConfigId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAwardType() {
        return awardType;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public Date getPlayTimeBegin() {
        return playTimeBegin;
    }

    public void setPlayTimeBegin(Date playTimeBegin) {
        this.playTimeBegin = playTimeBegin;
    }

    public Date getPlayTimeEnd() {
        return playTimeEnd;
    }

    public void setPlayTimeEnd(Date playTimeEnd) {
        this.playTimeEnd = playTimeEnd;
    }

    public List<LuckDrawEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<LuckDrawEntry> entryList) {
        this.entryList = entryList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
