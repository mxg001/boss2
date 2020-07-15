package cn.eeepay.framework.model;

import java.util.Date;

/**
 * cm_user_message
 * @author	mays
 * @date	2018年4月9日
 */
public class CmUserMessageInfo {

	private Integer id;
	private String userNo;
	private String cardNo;
	private String msgTitle;
	private String msgContent;
	private String msgType;
	private String isRead;
	private Date createTime;
	private Date updateTime;
	private String isDel;

	private String sCreateTime;
	private String eCreateTime;
	private String msgStatus;//{text:"未读",value:'0'},{text:"已读",value:'1'},{text:"关闭",value:'2'}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
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
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
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
	public String getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

}
