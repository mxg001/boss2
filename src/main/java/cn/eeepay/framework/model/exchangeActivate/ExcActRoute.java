package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/29/029.
 * @author  liuks
 * 超级兑路由实体
 * 对应表act_pass_route
 */
public class ExcActRoute {

    private  Integer  id;
    private  String channelNo;//核销渠道编号
    private String channelName;//核销渠道名称
    private String priority;//优先级 A,B,C,D,E,F,G...
    private Integer status;//路由状态 0关闭 1开启
    private String channelAccountNumber;//核销渠道登入账号
    private String channelPassword;//核销渠道登入密码
    private String remark;//核销渠道描述
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间

    private Long pId;//商品ID


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChannelAccountNumber() {
        return channelAccountNumber;
    }

    public void setChannelAccountNumber(String channelAccountNumber) {
        this.channelAccountNumber = channelAccountNumber;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }
}
