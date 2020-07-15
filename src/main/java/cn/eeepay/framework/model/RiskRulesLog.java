package cn.eeepay.framework.model;

import java.util.Date;

public class RiskRulesLog {

	 private Integer id;
	
	 private Integer rulesNo;
	 
	 private String content;
	 
	 private Date updateTime;
	 
	 private String updatePerson;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRulesNo() {
		return rulesNo;
	}

	public void setRulesNo(Integer rulesNo) {
		this.rulesNo = rulesNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
}
