package cn.eeepay.framework.model;




/**
 * 会员地区分布记录表
 * @author dxy
 *
 */
public class UserDistributionRecord {

	
	private Long recordId;         //记录ID',
	private String analysisCode;   //数据分析记录编号
	private String isProvince;     //是否是省份记录 0.否，1.是',
    private String userCount;      //用户数量',
    private String openProvince;   //省份名称',
    private String openCity;       //城市名称',
    private String orgId;
    private java.util.Date createDate;//创建时间',
    
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getAnalysisCode() {
		return analysisCode;
	}
	public void setAnalysisCode(String analysisCode) {
		this.analysisCode = analysisCode;
	}
	public String getIsProvince() {
		return isProvince;
	}
	public void setIsProvince(String isProvince) {
		this.isProvince = isProvince;
	}
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}
	public String getOpenProvince() {
		return openProvince;
	}
	public void setOpenProvince(String openProvince) {
		this.openProvince = openProvince;
	}
	public String getOpenCity() {
		return openCity;
	}
	public void setOpenCity(String openCity) {
		this.openCity = openCity;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "UserDistributionRecord [recordId=" + recordId
				+ ", analysisCode=" + analysisCode + ", isProvince="
				+ isProvince + ", userCount=" + userCount + ", openProvince="
				+ openProvince + ", openCity=" + openCity + ", orgId=" + orgId
				+ ", createDate=" + createDate + "]";
	}
    
}
