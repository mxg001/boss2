package cn.eeepay.framework.model;

import java.util.Date;

/**
 * superbank.red_luck_conf
 * 红包幸运值配置表
 */
public class RedLuckConf {
    private Integer id;

    private String initValue;//初始幸运值

    private String lowestValue;//最低幸运值

    private String highestValue;//最高值

    private String singleCommentValue;//单个评论的加减值

    private String pushRedValue;//发一个红包加减幸运值

    private String firstCommentValue;//单个红包首位评论

    private String receiveRedValue;//领取红包无评论消耗的幸运值

    private String badCommentValue;//恶意评论

    private String adCommentValue;//评论发广告

    private Date createTime;//创建时间

    private Date updateTime;//修改时间

    private Integer operator;//操作人

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public String getLowestValue() {
        return lowestValue;
    }

    public void setLowestValue(String lowestValue) {
        this.lowestValue = lowestValue;
    }

    public String getHighestValue() {
        return highestValue;
    }

    public void setHighestValue(String highestValue) {
        this.highestValue = highestValue;
    }

    public String getSingleCommentValue() {
        return singleCommentValue;
    }

    public void setSingleCommentValue(String singleCommentValue) {
        this.singleCommentValue = singleCommentValue;
    }

    public String getPushRedValue() {
        return pushRedValue;
    }

    public void setPushRedValue(String pushRedValue) {
        this.pushRedValue = pushRedValue;
    }

    public String getFirstCommentValue() {
        return firstCommentValue;
    }

    public void setFirstCommentValue(String firstCommentValue) {
        this.firstCommentValue = firstCommentValue;
    }

    public String getReceiveRedValue() {
        return receiveRedValue;
    }

    public void setReceiveRedValue(String receiveRedValue) {
        this.receiveRedValue = receiveRedValue;
    }

    public String getBadCommentValue() {
        return badCommentValue;
    }

    public void setBadCommentValue(String badCommentValue) {
        this.badCommentValue = badCommentValue;
    }

    public String getAdCommentValue() {
        return adCommentValue;
    }

    public void setAdCommentValue(String adCommentValue) {
        this.adCommentValue = adCommentValue;
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

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }
}