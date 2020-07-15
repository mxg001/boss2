package cn.eeepay.framework.model;

import java.util.Date;

/**
 * superbank.red_org
 * 红包业务组织管理表
 * @author tans
 */
public class RedOrg {
    private Long id;

    private String busCode;//活动编号,跟red_control表相关联

    private Long orgId;//品牌组织id

    private String orgName;//品牌组织名称

    private Date createTime;//创建时间

    private Date updateTime;//修改时间

    private Integer operator;//操作人ID

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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
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