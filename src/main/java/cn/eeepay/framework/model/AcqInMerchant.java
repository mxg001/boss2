package cn.eeepay.framework.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * table acq_merchant_info
 * 收单商户进件
 * @author zxs
 *
 */
public class AcqInMerchant {
    private Long id;

    //进件类型:1个体收单商户，2-企业收单商户
    private Integer merchantType;
    //商户名称
    private String merchantName;
    //法人姓名
    private String legalPerson;
    //法人身份证
    private String legalPersonId;
    
    //身份证有效时间
    private Date idValidStart;
    private Date idValidEnd;
    private String persionId;
    
    public String getPersionId() {
		return persionId;
	}
	public void setPersionId(String persionId) {
		this.persionId = persionId;
	}
	//省市区
    private String province;
    private String city;
    private String district;
    //详细地址
    private String address;
    
    private String allAddress;
    
    public String getAllAddress() {
    	StringBuffer sb = new StringBuffer("");
    	if (StringUtils.isNotBlank(province)) {
			sb.append(province);
		}
    	if (StringUtils.isNotBlank(city)) {
			sb.append(city);
		}
    	if (StringUtils.isNotBlank(district)) {
			sb.append(district);
		}
    	if (StringUtils.isNotBlank(address)) {
			sb.append(address);
		}
    	
		return sb.toString();
	}
	public void setAllAddress(String allAddress) {
		this.allAddress = allAddress;
	}
	//经营范围
    private String oneScope;
    private String twoScope;
    private String twoScopeX;

    //营业执照
    private String charterName;
    private String charterNo;
    
    private Date charterValidStart;
    private Date charterValidEnd;
    
    private String charterValidStr;
    
    public String getCharterValidStr() {
		return charterValidStr;
	}
	public void setCharterValidStr(String charterValidStr) {
		this.charterValidStr = charterValidStr;
	}
	//账户类型 1 对私 2对公
    private Integer accountType;
    //银行卡号
    private String bankNo;
    //开户名
    private String accountName;
    //开户银行
    private String accountBank;
    //开户地区
    private String accountProvince;
    private String accountCity;
    private String accountDistrict;
    
    private String accountAddress;

    private String generalMerchantNo;
    private String generalMerchantName;
    private String generalMerId;//普通商户进件Id
	private String changeMerBusinessInfo;
	private String bpName;

    
    
    public String getAccountAddress() {
		return accountProvince+accountCity+accountDistrict;
	}
	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}
	//支行
    private String bankBranch;
    //联行号
    private String lineNumber;
    //进件编号
    private String acqIntoNo;
    //进件来源
    private String intoSource;
    //审核状态
    private Integer auditStatus;
    //审核时间
    private Date auditTime;
    private Date auditSTime;
    private Date auditETime;
    //进件时间
    private Date createTime;
    private String createTimeStr;
    private Date createSTime;
    private Date createETime;
    
    private String mcc;
    
  //所属代理商
    private String agentNo;
    private String agentName;
    //所属一级代理商
    private String oneAgentNo;
    private String oneAgentName;
    
    
    public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public Date getAuditSTime() {
		return auditSTime;
	}
	public void setAuditSTime(Date auditSTime) {
		this.auditSTime = auditSTime;
	}
	public Date getAuditETime() {
		return auditETime;
	}
	public void setAuditETime(Date auditETime) {
		this.auditETime = auditETime;
	}
	public Date getCreateSTime() {
		return createSTime;
	}
	public void setCreateSTime(Date createSTime) {
		this.createSTime = createSTime;
	}
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public Date getCreateETime() {
		return createETime;
	}
	public void setCreateETime(Date createETime) {
		this.createETime = createETime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getMerchantType() {
		return merchantType;
	}
	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLegalPersonId() {
		return legalPersonId;
	}
	public void setLegalPersonId(String legalPersonId) {
		this.legalPersonId = legalPersonId;
	}
	public Date getIdValidStart() {
		return idValidStart;
	}
	public void setIdValidStart(Date idValidStart) {
		this.idValidStart = idValidStart;
	}
	public Date getIdValidEnd() {
		return idValidEnd;
	}
	public void setIdValidEnd(Date idValidEnd) {
		this.idValidEnd = idValidEnd;
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
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOneScope() {
		return oneScope;
	}
	public void setOneScope(String oneScope) {
		this.oneScope = oneScope;
	}
	public String getTwoScope() {
		return twoScope;
	}
	public void setTwoScope(String twoScope) {
		this.twoScope = twoScope;
	}
	public String getCharterName() {
		return charterName;
	}
	public void setCharterName(String charterName) {
		this.charterName = charterName;
	}
	public String getCharterNo() {
		return charterNo;
	}
	public void setCharterNo(String charterNo) {
		this.charterNo = charterNo;
	}
	public Date getCharterValidStart() {
		return charterValidStart;
	}
	public void setCharterValidStart(Date charterValidStart) {
		this.charterValidStart = charterValidStart;
	}
	public Date getCharterValidEnd() {
		return charterValidEnd;
	}
	public void setCharterValidEnd(Date charterValidEnd) {
		this.charterValidEnd = charterValidEnd;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public String getAccountProvince() {
		return accountProvince;
	}
	public void setAccountProvince(String accountProvince) {
		this.accountProvince = accountProvince;
	}
	public String getAccountCity() {
		return accountCity;
	}
	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}
	public String getAccountDistrict() {
		return accountDistrict;
	}
	public void setAccountDistrict(String accountDistrict) {
		this.accountDistrict = accountDistrict;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getAcqIntoNo() {
		return acqIntoNo;
	}
	public void setAcqIntoNo(String acqIntoNo) {
		this.acqIntoNo = acqIntoNo;
	}


	public String getTwoScopeX() {
		return twoScopeX;
	}

	public void setTwoScopeX(String twoScopeX) {
		this.twoScopeX = twoScopeX;
	}

	public String getIntoSource() {
		return intoSource;
	}
	public void setIntoSource(String intoSource) {
		this.intoSource = intoSource;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getGeneralMerchantNo() {
		return generalMerchantNo;
	}
	public void setGeneralMerchantNo(String generalMerchantNo) {
		this.generalMerchantNo = generalMerchantNo;
	}
	public String getGeneralMerchantName() {
		return generalMerchantName;
	}

	public void setGeneralMerchantName(String generalMerchantName) {
		this.generalMerchantName = generalMerchantName;
	}

	public String getGeneralMerId() {
		return generalMerId;
	}

	public void setGeneralMerId(String generalMerId) {
		this.generalMerId = generalMerId;
	}

	public String getChangeMerBusinessInfo() {
		return changeMerBusinessInfo;
	}

	public void setChangeMerBusinessInfo(String changeMerBusinessInfo) {
		this.changeMerBusinessInfo = changeMerBusinessInfo;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
}