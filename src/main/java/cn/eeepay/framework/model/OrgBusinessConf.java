package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 组织业务配置
 */
public class OrgBusinessConf {
    public enum Opear{
        INSERT,
        UPDATE,
        NONE
    }
    private Long id;
    private Long orgId;     //组织id
    private Long businessId;    //业务ID
    private String isEnable;    //是否勾选 0-否 1-是
    private String createBy;    //添加人
    private Date createDate;    //添加时间
    private String updateBy;    //修改人
    private Date updateDate;    //修改时间
    private Opear opear;        //操作

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Opear getOpear() {
        return opear;
    }

    public void setOpear(Opear opear) {
        this.opear = opear;
    }


}
