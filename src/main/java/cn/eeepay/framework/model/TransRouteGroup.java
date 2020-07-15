package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.eeepay.framework.enums.AccountsPeriod;
import cn.eeepay.framework.enums.ServiceType;

public class TransRouteGroup {
    private Integer id;

    private String groupName;

    private Integer groupCode;

    private Integer acqId;

    private Integer acqServiceId;

    private Integer agentNo;

    private Integer serviceType;

    private Integer accountsPeriod;

    private Integer mySettle;

    private Integer salesNo;

    private BigDecimal allowMinAmount;

    private BigDecimal allowMaxAmount;

	private String allowTransStartTime;

	private String allowTransEndTime;

    private BigDecimal defAcqDayAmount;

    private String backupsGroupCode;

    private Integer merchantType;

    private String routeLast;

    private String routeDescribe;

    private Integer routeType;

    private Integer status;

    private String groupProvince;

    private String groupCity;
    private String province;
    
    private String city;

    private Date createTime;

    private String createPerson;

	private String acqName;// 收单机构名称

	private String serviceName;// 收单服务类型名称

	private String agentName;// 代理商名称
	
	private String acqServiceType; //收单服务类型
	
	private String warnMobile; //预警手机号
	
	private String merchantNo; //商户号
	
	private String merchantName; //商户名称
	
	private String oneAgentNo; //一级代理商编号
	
	private String acqMerchantName;
	
	private String agentNode; //所属代理商节点

    private String userName;//创建人姓名

	/**
	 * 映射集群
	 */
	private Integer mapGroupId;

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getAcqId() {
        return acqId;
    }

    public void setAcqId(Integer acqId) {
        this.acqId = acqId;
    }

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
    }

    public Integer getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(Integer agentNo) {
        this.agentNo = agentNo;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getAccountsPeriod() {
        return accountsPeriod;
    }

    public void setAccountsPeriod(Integer accountsPeriod) {
        this.accountsPeriod = accountsPeriod;
    }

    public Integer getMySettle() {
        return mySettle;
    }

    public void setMySettle(Integer mySettle) {
        this.mySettle = mySettle;
    }

    public Integer getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(Integer salesNo) {
        this.salesNo = salesNo;
    }

    public BigDecimal getAllowMinAmount() {
        return allowMinAmount;
    }

    public void setAllowMinAmount(BigDecimal allowMinAmount) {
        this.allowMinAmount = allowMinAmount;
    }

    public BigDecimal getAllowMaxAmount() {
        return allowMaxAmount;
    }

    public void setAllowMaxAmount(BigDecimal allowMaxAmount) {
        this.allowMaxAmount = allowMaxAmount;
    }

	public String getAllowTransStartTime() {
        return allowTransStartTime;
    }

	public void setAllowTransStartTime(String allowTransStartTime) {
        this.allowTransStartTime = allowTransStartTime;
    }

	public String getAllowTransEndTime() {
        return allowTransEndTime;
    }

	public void setAllowTransEndTime(String allowTransEndTime) {
        this.allowTransEndTime = allowTransEndTime;
    }

    public BigDecimal getDefAcqDayAmount() {
        return defAcqDayAmount;
    }

    public void setDefAcqDayAmount(BigDecimal defAcqDayAmount) {
        this.defAcqDayAmount = defAcqDayAmount;
    }

    public String getBackupsGroupCode() {
        return backupsGroupCode;
    }

    public void setBackupsGroupCode(String backupsGroupCode) {
        this.backupsGroupCode = backupsGroupCode;
    }

    public Integer getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(Integer merchantType) {
        this.merchantType = merchantType;
    }

    public String getRouteLast() {
        return routeLast;
    }

    public void setRouteLast(String routeLast) {
        this.routeLast = routeLast;
    }

    public String getRouteDescribe() {
        return routeDescribe;
    }

    public void setRouteDescribe(String routeDescribe) {
        this.routeDescribe = routeDescribe;
    }

    public Integer getRouteType() {
        return routeType;
    }

    public void setRouteType(Integer routeType) {
        this.routeType = routeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGroupProvince() {
        return groupProvince;
    }

    public void setGroupProvince(String groupProvince) {
        this.groupProvince = groupProvince;
    }

    public String getGroupCity() {
        return groupCity;
    }

    public void setGroupCity(String groupCity) {
        this.groupCity = groupCity;
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

	public String getAcqServiceType() {
		return acqServiceType;
	}

	public void setAcqServiceType(String acqServiceType) {
		this.acqServiceType = acqServiceType;
	}
	
	public String getWarnMobile() {
		return warnMobile;
	}

	public void setWarnMobile(String warnMobile) {
		this.warnMobile = warnMobile;
	}
	

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getAccountsPeriodTextByValue(int value) {
		String text = "";
		for (AccountsPeriod accountsPeriod : AccountsPeriod.values()) {
			if (accountsPeriod.getValue() == value) {
				text = accountsPeriod.getText();
				break;
			}
		}
		return text;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getServiceTypeTextByValue(int value) {
		String text = "";
		for (ServiceType serviceType : ServiceType.values()) {
			if (serviceType.getValue() == value) {
				text = serviceType.getText();
				break;
			}
		}
		return text;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public Integer getMapGroupId() {
		return mapGroupId;
	}

	public void setMapGroupId(Integer mapGroupId) {
		this.mapGroupId = mapGroupId;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}