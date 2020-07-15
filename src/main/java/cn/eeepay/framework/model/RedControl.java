package cn.eeepay.framework.model;

import java.util.Date;

/**
 * superbank.red_control
 * 红包业务管理
 * @author tans
 */
public class RedControl {
    private Long id;

    private String busCode;//活动编号

    private String busType;//业务类型(0个人发红包1新用户红包、2信用卡奖励红包、3贷款奖励红包、4登录红包、5敲门红包)

    private Integer openStatus;//功能是否开启(0关闭1开启)

    private Integer orgStatus;//组织是否开启(0关闭1开启)

    private Date createTime;

    private Date updateTime;

    private Integer operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode == null ? null : busCode.trim();
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType == null ? null : busType.trim();
    }

    public Integer getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }

    public Integer getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(Integer orgStatus) {
        this.orgStatus = orgStatus;
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