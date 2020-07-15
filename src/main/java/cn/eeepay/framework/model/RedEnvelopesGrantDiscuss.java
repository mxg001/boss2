package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/20/020.
 * @author  liuks
 * 红包评论
 * 对应表 red_orders_discuss
 */
public class RedEnvelopesGrantDiscuss {
    private Long id;//评论id

    private Long redOrderId;//红包订单id关联red_orders的主键

    private String userCode;//评论用户编号

    private String userNickName;//评论用户昵称

    private Date createDate;//评论时间

    private String status;//状态(0正常1删除)

    private String content;//评论内容

    private String reason;//删除原因

    private String reamrk;//备注

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedOrderId() {
        return redOrderId;
    }

    public void setRedOrderId(Long redOrderId) {
        this.redOrderId = redOrderId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk;
    }
}
