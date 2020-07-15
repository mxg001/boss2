package cn.eeepay.framework.model;

import java.util.Date;

import javax.xml.ws.ServiceMode;

public class DefTransRouteGroup {
    private Integer id;

    private Integer productId;

    private Integer acqOrgId;

    private String defGroupCode;

    private Integer acqServiceType;
    
    private Integer serviceId;
    
    private Integer startPc;
    
    private Integer defType;

    private Date createTime;

    private String createPerson;

    private String acqName;
    
    private String groupName;
    
    private String bpName;
    
    private String serviceName;
    
    private String serviceType;
    
    private Integer serviceModel;//服务模式
    
    private String liquidationChannel;//直清通道
    
    
    public Integer getServiceModel() {
		return serviceModel;
	}

	public void setServiceModel(Integer serviceModel) {
		this.serviceModel = serviceModel;
	}

	public String getLiquidationChannel() {
		return liquidationChannel;
	}

	public void setLiquidationChannel(String liquidationChannel) {
		this.liquidationChannel = liquidationChannel;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(Integer acqOrgId) {
        this.acqOrgId = acqOrgId;
    }

    public String getDefGroupCode() {
        return defGroupCode;
    }

    public void setDefGroupCode(String defGroupCode) {
        this.defGroupCode = defGroupCode == null ? null : defGroupCode.trim();
    }

    public Integer getDefType() {
        return defType;
    }

    public void setDefType(Integer defType) {
        this.defType = defType;
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
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getAcqServiceType() {
		return acqServiceType;
	}

	public void setAcqServiceType(Integer acqServiceType) {
		this.acqServiceType = acqServiceType;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getStartPc() {
		return startPc;
	}

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}

	public void setStartPc(Integer startPc) {
		this.startPc = startPc;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}


}