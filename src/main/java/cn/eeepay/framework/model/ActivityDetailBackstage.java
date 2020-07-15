package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/30/030.
 * @author  liuks
 * 欢乐送,欢乐返活动延时核算清算 实体
 * 对应表 activity_detail_backstage
 */
public class ActivityDetailBackstage {
    private Integer id;

    private String batchNo;

    private Integer actId;

    private String actState;

    private Date createTime;

    private Integer userId;

    private Integer sendNum;//请求次数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActState() {
        return actState;
    }

    public void setActState(String actState) {
        this.actState = actState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }
}
