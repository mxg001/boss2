package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * table risk_roll
 * desc 风控名单
 * @author thj
 *
 */
public class RiskRoll {
    private Integer id;

    private String rollNo;

    private String rollName;

    private Integer rollType;
    private String rollTypeName;

    private Integer rollStatus;

    private Date createTime;

    private String createPerson;
    
    private String userName;

    private Integer rollBelong;
    
    private String remark;
    
    private Date sdate;
    private Date edate;

    private String rollUser;
    private String rollMsg;
    private String userMsg;
    
	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo == null ? null : rollNo.trim();
    }

    public String getRollName() {
        return rollName;
    }

    public void setRollName(String rollName) {
        this.rollName = rollName == null ? null : rollName.trim();
    }

    public Integer getRollType() {
        return rollType;
    }

    public void setRollType(Integer rollType) {
        this.rollType = rollType;
    }

    public Integer getRollStatus() {
        return rollStatus;
    }

    public void setRollStatus(Integer rollStatus) {
        this.rollStatus = rollStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRollBelong() {
        return rollBelong;
    }

    public void setRollBelong(Integer rollBelong) {
        this.rollBelong = rollBelong;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRollTypeName() {
		return rollTypeName;
	}

	public void setRollTypeName(String rollTypeName) {
		this.rollTypeName = rollTypeName;
	}

    public String getRollUser() {
        return rollUser;
    }

    public void setRollUser(String rollUser) {
        this.rollUser = rollUser;
    }

    public String getRollMsg() {
        return rollMsg;
    }

    public void setRollMsg(String rollMsg) {
        this.rollMsg = rollMsg;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }
}