package cn.eeepay.framework.model;

import java.util.Date;

public class TransRouteGroupAcqMerchant {
    private Integer id;

    private Integer groupCode;

    private String acqMerchantNo;

    private Date createTime;

    private String createPerson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }
}