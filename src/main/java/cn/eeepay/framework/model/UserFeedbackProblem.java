package cn.eeepay.framework.model;

import java.util.Date;

public class UserFeedbackProblem {
    private Long id;

    private String userId;

    private String userType;

    private String problemType;

    private String content;

    private String printscreen;

    private String title;

    private Date submitTime;
    
    private String typeName;
    
    private String userName;
    
    private String mobilephone;

    private Integer status;

    private String appNo;
    private String appName;

    private Date submitTimeBegin;
    private Date submitTimeEnd;

    private Date dealTimeBegin;
    private Date dealTimeEnd;
    private Date dealTime;

    private String dealResult;

    private Long dealUserId;
    private String dealUserName;

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

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType == null ? null : problemType.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPrintscreen() {
        return printscreen;
    }

    public void setPrintscreen(String printscreen) {
        this.printscreen = printscreen == null ? null : printscreen.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public Date getSubmitTimeBegin() {
        return submitTimeBegin;
    }

    public void setSubmitTimeBegin(Date submitTimeBegin) {
        this.submitTimeBegin = submitTimeBegin;
    }

    public Date getSubmitTimeEnd() {
        return submitTimeEnd;
    }

    public void setSubmitTimeEnd(Date submitTimeEnd) {
        this.submitTimeEnd = submitTimeEnd;
    }

    public Date getDealTimeBegin() {
        return dealTimeBegin;
    }

    public void setDealTimeBegin(Date dealTimeBegin) {
        this.dealTimeBegin = dealTimeBegin;
    }

    public Date getDealTimeEnd() {
        return dealTimeEnd;
    }

    public void setDealTimeEnd(Date dealTimeEnd) {
        this.dealTimeEnd = dealTimeEnd;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public Long getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(Long dealUserId) {
        this.dealUserId = dealUserId;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}