package cn.eeepay.framework.model;

import java.util.Date;

/**
 * cm_notice
 * @author	mays
 * @date	2018年4月2日
 */
public class CmNoticeInfo {

	private Integer id;
	private String title;
	private String content;
	private String iconUrl;
	private String orgId;
	private String link;
	private String picPosition;
	private String status;
	private Date createTime;
	private Date updateTime;
	private String iconName;
	private String createBy;
	private Date sendTime;
	private String senderId;
	private String senderName;
	private Integer popSwitch;
	private String remark;

	private String startCreateTime;
	private String endCreateTime;
	private String startSendTime;
	private String endSendTime;

	private String sendTimeStr;
	private String submitType;//1：立即发布，2：先保存，稍后发布

	public String getSendTimeStr() {
		return sendTimeStr;
	}
	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getStartCreateTime() {
		return startCreateTime;
	}
	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}
	public String getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	public String getStartSendTime() {
		return startSendTime;
	}
	public void setStartSendTime(String startSendTime) {
		this.startSendTime = startSendTime;
	}
	public String getEndSendTime() {
		return endSendTime;
	}
	public void setEndSendTime(String endSendTime) {
		this.endSendTime = endSendTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPicPosition() {
		return picPosition;
	}
	public void setPicPosition(String picPosition) {
		this.picPosition = picPosition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public Integer getPopSwitch() {
		return popSwitch;
	}
	public void setPopSwitch(Integer popSwitch) {
		this.popSwitch = popSwitch;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
