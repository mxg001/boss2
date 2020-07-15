package cn.eeepay.framework.model;

import java.util.Date;

/**
 * cm_org_info
 * @author mays
 * @date 2018年3月29日
 */
public class CmOrgInfo {

	private Integer id;
	private String orgId;
	private String orgName;
	private Date createTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
