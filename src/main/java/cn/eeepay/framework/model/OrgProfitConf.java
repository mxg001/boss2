package cn.eeepay.framework.model;

/**
 * 组织分润配置
 * @author Administrator
 *
 */
public class OrgProfitConf {

	private Long id; 
	
	private Long orgId ; 			// 组织id
	
	private String productType;  	//项目类型:
	
	private String profitType;  	//1固定金额  2比率
	
	private String seltMember;  	//专员直推
	
	private String seltManager; 	// 经理直推
	
	private String seltBanker;  	//银行家直推
	
	private String seltBanker2; 	//二级银行家分润
	
	private String updateBy;  		//修改人
	
	private java.util.Date updateDate;  //修改时间
	
	private String orgName;
	
	
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
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProfitType() {
		return profitType;
	}
	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}
	public String getSeltMember() {
		return seltMember;
	}
	public void setSeltMember(String seltMember) {
		this.seltMember = seltMember;
	}
	public String getSeltManager() {
		return seltManager;
	}
	public void setSeltManager(String seltManager) {
		this.seltManager = seltManager;
	}
	public String getSeltBanker() {
		return seltBanker;
	}
	public void setSeltBanker(String seltBanker) {
		this.seltBanker = seltBanker;
	}
	public String getSeltBanker2() {
		return seltBanker2;
	}
	public void setSeltBanker2(String seltBanker2) {
		this.seltBanker2 = seltBanker2;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}
