package cn.eeepay.framework.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class TerminalApply {

	private String id;
	private String merchantNo;
	private String status;
	private String productType;
	private Date createTime;
	private String address;
	private String mobilephone;
	private String merchantName;
	private String merAccount;
	private Date sTime;
	private Date eTime;
	private String hpName;
	private String teamId;
	private Boolean isBind;	//商户是否绑定机具
	private String isBindParam;	//查询条件:商户是否绑定机具;null为查所有,0为是,1为否
	private String remark;
	private Date updateTime;
	private String agentName;//所属代理商名称

	//liuks 微创业二期
	private String sn;//机具SN
	private String SNisNull;//sn是否为空,1非空,0空,判断机具原归属是否属于一级
	private String agentNo;//所属代理商编号
	private  String oneAgentName;//一级代理商名称
	private String oneAgentNo;//一级代理商编号
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsBindParam() {
		return isBindParam;
	}
	public void setIsBindParam(String isBindParam) {
		this.isBindParam = isBindParam;
	}
	public Boolean getIsBind() {
		return isBind;
	}
	public void setIsBind(Boolean isBind) {
		this.isBind = isBind;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getMerAccount() {
		return merAccount;
	}
	public void setMerAccount(String merAccount) {
		this.merAccount = merAccount;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public String getHpName() {
		return hpName;
	}
	public void setHpName(String hpName) {
		this.hpName = hpName;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSNisNull() {
		return SNisNull;
	}

	public void setSNisNull(String SNisNull) {
		this.SNisNull = SNisNull;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
}
