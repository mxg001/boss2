package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;


public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Integer id;

    private String userName;

    private String password;

    private String realName;

    private String telNo;

    private String email;

    private Integer status;

    private String theme;

    private String createOperator;

    private Date createTime;

    private Date updatePwdTime;
    
    private String teamId;

    private String userId;
    
    private String sharedNum;
    
	private String openType;    //打开位置 1-APP 2-微信H5',
	
	private String loginSource; //设备访问来源 1.安卓；2.IOS；',

    private Integer deptId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}

	public String getSharedNum() {
		return sharedNum;
	}

	public void setSharedNum(String sharedNum) {
		this.sharedNum = sharedNum;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo == null ? null : telNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme == null ? null : theme.trim();
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator == null ? null : createOperator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdatePwdTime() {
        return updatePwdTime;
    }

    public void setUpdatePwdTime(Date updatePwdTime) {
        this.updatePwdTime = updatePwdTime;
    }

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}