package cn.eeepay.framework.model;

import java.util.Date;

public class UserFreezeOperLog {

	private Integer id; // 主键

	private String userCode; // 被操作的用户编码(被冻结解冻的用户userCode)

	private Integer operId; // 操作人id(登录用户的userId)

	private String operName; // 操作人名称(登录用户的userName)

	private String operType; // 操作类型：1，解冻；2，冻结

	private Date operTime; // 操作时间

	private String operReason; // 操作原因/备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Integer getOperId() {
		return operId;
	}

	public void setOperId(Integer operId) {
		this.operId = operId;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName == null ? null : operName.trim();
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType == null ? null : operType.trim();
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperReason() {
		return operReason;
	}

	public void setOperReason(String operReason) {
		this.operReason = operReason == null ? null : operReason.trim();
	}

}