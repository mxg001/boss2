package cn.eeepay.framework.model;

import java.util.Date;

public class TransRouteGroupMerchant {
    private Integer id;

    private Integer groupCode;

    private String posMerchantNo;

    private String serviceType;
    
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

    public String getPosMerchantNo() {
        return posMerchantNo;
    }

    public void setPosMerchantNo(String posMerchantNo) {
        this.posMerchantNo = posMerchantNo;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}