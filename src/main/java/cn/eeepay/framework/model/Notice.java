package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 超级银行家-公告表
 * 
 * @author Administrator
 * 
 */
public class Notice {

	private Long id;
	
	private String noticeType;											// 公告类型：1、系统公告
	
	private String title;												// 标题
	
	private String link;												// 连接
	
	private String content;												// 内容
	
	private String status;												// 状态:0待发布；1已发布 2已废弃
	
	private String createBy;											// 创建人
	
	private Date createDate;											// 创建时间
	
	private String updateBy;											// 修改人
	
	private Date updateDate;											// 修改时间
	
	private Date sendTime;											// 下发时间
	private String sendTimeStr;
	
	private String startCreateTime;										//查询条件
	
	private String endCreateTime;										//查询条件
	
	private String startReleaseTime;									//查询条件
	
	private String endReleaseTime;										//查询条件

	private String newsImage;//消息图片
	private String newsImageUrl;//消息图片
	private String imagePosition;//图片位置：1 顶部，2 底部
	private String orgId;//下发组织，如有多个用逗号分隔，-1表示全部
	private Integer popSwitch;//弹窗提示开关：0 关闭，1 开启
	private Integer sendById;//下发人
	private String sendByName;//下发人
	private String remark;//备注
	private Integer submitType;//提交类型：1 立即发布，0 先保存，稍后发布

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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

	public String getStartReleaseTime() {
		return startReleaseTime;
	}

	public void setStartReleaseTime(String startReleaseTime) {
		this.startReleaseTime = startReleaseTime;
	}

	public String getEndReleaseTime() {
		return endReleaseTime;
	}

	public void setEndReleaseTime(String endReleaseTime) {
		this.endReleaseTime = endReleaseTime;
	}

	public String getNewsImage() {
		return newsImage;
	}

	public void setNewsImage(String newsImage) {
		this.newsImage = newsImage;
	}

	public String getImagePosition() {
		return imagePosition;
	}

	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getPopSwitch() {
		return popSwitch;
	}

	public void setPopSwitch(Integer popSwitch) {
		this.popSwitch = popSwitch;
	}

	public Integer getSendById() {
		return sendById;
	}

	public void setSendById(Integer sendById) {
		this.sendById = sendById;
	}

	public String getSendByName() {
		return sendByName;
	}

	public void setSendByName(String sendByName) {
		this.sendByName = sendByName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSubmitType() {
		return submitType;
	}

	public void setSubmitType(Integer submitType) {
		this.submitType = submitType;
	}

	public String getNewsImageUrl() {
		return newsImageUrl;
	}

	public void setNewsImageUrl(String newsImageUrl) {
		this.newsImageUrl = newsImageUrl;
	}

	public String getSendTimeStr() {
		return sendTimeStr;
	}

	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}
}
