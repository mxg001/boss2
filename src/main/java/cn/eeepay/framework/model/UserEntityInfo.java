package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;

/**
 * user_entity_info
 * @author tans
 */
public class UserEntityInfo   implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer Id;
	private String userId;
	private String userType;
	private String entityId;
	private String apply;
	private String manage;
	private String status;
	private Date lastNoticeTime;
	private String loginkey;
	private String loginTime;	
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public String getManage() {
		return manage;
	}
	public void setManage(String manage) {
		this.manage = manage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getLastNoticeTime() {
		return lastNoticeTime;
	}
	public void setLastNoticeTime(Date lastNoticeTime) {
		this.lastNoticeTime = lastNoticeTime;
	}
	public String getLoginkey() {
		return loginkey;
	}
	public void setLoginkey(String loginkey) {
		this.loginkey = loginkey;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
}
