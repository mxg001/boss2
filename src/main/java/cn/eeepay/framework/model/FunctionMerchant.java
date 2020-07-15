package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 功能开关商户
 * @author Administrator
 *
 */
public class FunctionMerchant {

	private int id;
	private String functionNumber;
	
	private String merchantNo;
	private String merchantName;

	private String type;
	
	private Date createTime;
	private Date lastUpdateTime;

	private String operator;
	
	private String teamId;
	private String teamName;
	private String teamEntryId;
	private String teamEntryName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFunctionNumber() {
		return functionNumber;
	}

	public void setFunctionNumber(String functionNumber) {
		this.functionNumber = functionNumber;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

	public String getTeamEntryName() {
		return teamEntryName;
	}

	public void setTeamEntryName(String teamEntryName) {
		this.teamEntryName = teamEntryName;
	}

	@Override
	public String toString() {
		return "FunctionMerchant{" +
				"id=" + id +
				", functionNumber='" + functionNumber + '\'' +
				", merchantNo='" + merchantNo + '\'' +
				", merchantName='" + merchantName + '\'' +
				", type='" + type + '\'' +
				", createTime=" + createTime +
				", lastUpdateTime=" + lastUpdateTime +
				", operator='" + operator + '\'' +
				", teamId='" + teamId + '\'' +
				", teamName='" + teamName + '\'' +
				", teamEntryId='" + teamEntryId + '\'' +
				", teamEntryName='" + teamEntryName + '\'' +
				'}';
	}
}
