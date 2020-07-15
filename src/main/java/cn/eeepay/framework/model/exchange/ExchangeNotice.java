package cn.eeepay.framework.model.exchange;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 超级兑广告实体
 * 对应表 rdmp_notice
 */
public class ExchangeNotice {

    private Long id;

    private String title;//标题

    private String content;//内容

    private String status;//状态:0删除1正常2待下发3置顶

    private String createUser;//创建人

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Date lastUpdateTime;//最后修改时间


    private Date sendTime;//下发时间
    private Date sendTimeBegin;
    private Date sendTimeEnd;

    private String sendUser;//下发人

    private Date upTime;//上线时间

    private Date downTime;//下线时间

    private String remark;//备注

    private String oemNoSet;//下发组织(oem no ,间隔)

    private String oemNo;

    private List<String> oemNoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getSendTimeBegin() {
        return sendTimeBegin;
    }

    public void setSendTimeBegin(Date sendTimeBegin) {
        this.sendTimeBegin = sendTimeBegin;
    }

    public Date getSendTimeEnd() {
        return sendTimeEnd;
    }

    public void setSendTimeEnd(Date sendTimeEnd) {
        this.sendTimeEnd = sendTimeEnd;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public Date getDownTime() {
        return downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOemNoSet() {
        return oemNoSet;
    }

    public void setOemNoSet(String oemNoSet) {
        this.oemNoSet = oemNoSet;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public List<String> getOemNoList() {
        return oemNoList;
    }

    public void setOemNoList(List<String> oemNoList) {
        this.oemNoList = oemNoList;
    }
}
