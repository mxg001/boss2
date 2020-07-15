package cn.eeepay.framework.model;

import java.util.Date;

public class CmUserInfo {

	private Integer id;
	private String srcUserId;
	private String srcOrgId;
	private String srcOrgPrduct;
	private String userNo;
	private String mobileNo;
	private String userName;
	private Integer userType;
	private String userLevel;
	private Date memberExpire;
	private Date createTime;

	private String sCreateTime;
	private String eCreateTime;

	private String orgName;
	private String oneAgentNo;
	private String oneAgentName;
	private String agentNo;
	private String agentName;
	private String agentNode;
	private String supUserNo;

	private String isVip;//是否会员，1-是，0或空为否
	private String contain;//查询条件，是否包含下级，1-是，0-否

	public String getIsVip() {
		return isVip;
	}
	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}
	public String getContain() {
		return contain;
	}
	public void setContain(String contain) {
		this.contain = contain;
	}
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getSupUserNo() {
		return supUserNo;
	}
	public void setSupUserNo(String supUserNo) {
		this.supUserNo = supUserNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSrcUserId() {
		return srcUserId;
	}
	public void setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
	}
	public String getSrcOrgId() {
		return srcOrgId;
	}
	public void setSrcOrgId(String srcOrgId) {
		this.srcOrgId = srcOrgId;
	}
	public String getSrcOrgPrduct() {
		return srcOrgPrduct;
	}
	public void setSrcOrgPrduct(String srcOrgPrduct) {
		this.srcOrgPrduct = srcOrgPrduct;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public Date getMemberExpire() {
		return memberExpire;
	}
	public void setMemberExpire(Date memberExpire) {
		this.memberExpire = memberExpire;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getsCreateTime() {
		return sCreateTime;
	}
	public void setsCreateTime(String sCreateTime) {
		this.sCreateTime = sCreateTime;
	}
	public String geteCreateTime() {
		return eCreateTime;
	}
	public void seteCreateTime(String eCreateTime) {
		this.eCreateTime = eCreateTime;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
