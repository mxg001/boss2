package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 行用卡还款公告信息表查询实体
 * @author  liuks
 */
public class CreditRepayNotice {
    private int id;//'通告ID'

    private String noticeNo;//通告编号

    private String title;//'标题'

    private String link;//'链接'

    private int status;//'状态：1-待下发，2-正常,3-删除'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//'创建时间'
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;//'最后修改时间'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTime;//'下发时间'
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTimeEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
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

    public Date getIssuedTimeBegin() {
        return issuedTimeBegin;
    }

    public void setIssuedTimeBegin(Date issuedTimeBegin) {
        this.issuedTimeBegin = issuedTimeBegin;
    }

    public Date getIssuedTimeEnd() {
        return issuedTimeEnd;
    }

    public void setIssuedTimeEnd(Date issuedTimeEnd) {
        this.issuedTimeEnd = issuedTimeEnd;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }
}
