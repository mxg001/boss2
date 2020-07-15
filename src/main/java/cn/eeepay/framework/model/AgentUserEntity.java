package cn.eeepay.framework.model;

import java.util.Date;

public class AgentUserEntity {
    private Long id;

    private String userId;

    private String userType;

    private String entityId;

    private String apply;

    private String manage;

    private String status;

    private Date lastNoticeTime;

    private String loginkey;

    private Date loginTime;
    
    private String isAgent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId == null ? null : entityId.trim();
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply == null ? null : apply.trim();
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage == null ? null : manage.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
        this.loginkey = loginkey == null ? null : loginkey.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

	public String getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(String isAgent) {
		this.isAgent = isAgent;
	}
    
    
}