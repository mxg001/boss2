package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table  merchant_ifo
 * desc 商户信息表
 */
public class MerchantInfo {
    private Long id;

    private String merchantNo;

    private String merchantName;

    private String merchantType;

    private String lawyer;

    private String businessType;

    private String industryType;

    private String idCardNo;

    private String province;

    private String city;

    private String district;
    
    private String address;

    private String mobilephone;

    private String email;

    private String operator;

    private String agentNo;

    private Date createTime;

    private String status;

    private String parentNode;

    private String saleName;

    private String creator;

    private String mender;

    private Date lastUpdateTime;

    private String remark;

    private String oneAgentNo;
    
    private String teamId;
    
    private String teamName;
    
    //代理商名称
    private String agentName;
    
    //经营信息
    private String sysName;
    //二级经营信息
    private String TwoSysName;
    
    private String sn;
    
    //2.2.5 添加预冻结金额  
    private BigDecimal preFrozenAmount;

    private String oneAgentName;
    private String oneMerchantName;
    private String twoMerchantName;
    private String threeMerchantName;
    private String oneSaleName;//一级代理商所属销售
    private String businessTypeStr;//经营范围

    private String productStatus; //审核状态

    private String riskStatus; //冻结状态

    private String recommendedSource;

    private String registerSource;
    private String teamEntryId;
    
    public String getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(String registerSource) {
		this.registerSource = registerSource;
	}

	public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getProductStatus() {
        return productStatus;
    }
    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType == null ? null : merchantType.trim();
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer == null ? null : lawyer.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType == null ? null : industryType.trim();
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode == null ? null : parentNode.trim();
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName == null ? null : saleName.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender == null ? null : mender.trim();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getTwoSysName() {
		return TwoSysName;
	}

	public void setTwoSysName(String twoSysName) {
		TwoSysName = twoSysName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public BigDecimal getPreFrozenAmount() {
		return preFrozenAmount;
	}

	public void setPreFrozenAmount(BigDecimal preFrozenAmount) {
		this.preFrozenAmount = preFrozenAmount;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getOneMerchantName() {
		return oneMerchantName;
	}

	public void setOneMerchantName(String oneMerchantName) {
		this.oneMerchantName = oneMerchantName;
	}

	public String getTwoMerchantName() {
		return twoMerchantName;
	}

	public void setTwoMerchantName(String twoMerchantName) {
		this.twoMerchantName = twoMerchantName;
	}

	public String getThreeMerchantName() {
		return threeMerchantName;
	}

	public void setThreeMerchantName(String threeMerchantName) {
		this.threeMerchantName = threeMerchantName;
	}

	public String getOneSaleName() {
		return oneSaleName;
	}

	public void setOneSaleName(String oneSaleName) {
		this.oneSaleName = oneSaleName;
	}

	public String getBusinessTypeStr() {
		return businessTypeStr;
	}

	public void setBusinessTypeStr(String businessTypeStr) {
		this.businessTypeStr = businessTypeStr;
	}

    public String getRecommendedSource() {
        return recommendedSource;
    }

    public void setRecommendedSource(String recommendedSource) {
        this.recommendedSource = recommendedSource;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }
}