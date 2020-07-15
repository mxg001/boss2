package cn.eeepay.framework.model;

import java.util.Date;

/**
 * cm_bannerInfo
 * @author mays
 * @date 2018年3月29日
 */
public class CmBannerInfo {

	private Integer id;
	private String orgId;
	private String picUrl;
	private String bannerTitle;
	private Integer showNo;
	private Date beginTime;
	private Date endTime;
	private Integer status;
	private String bannerUrl;
	private String remark;
	private Date createTime;
	private Integer positionType;
	private String picName;

	private String orgName;

	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getBannerTitle() {
		return bannerTitle;
	}
	public void setBannerTitle(String bannerTitle) {
		this.bannerTitle = bannerTitle;
	}
	public Integer getShowNo() {
		return showNo;
	}
	public void setShowNo(Integer showNo) {
		this.showNo = showNo;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getPositionType() {
		return positionType;
	}
	public void setPositionType(Integer positionType) {
		this.positionType = positionType;
	}

}
