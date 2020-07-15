package cn.eeepay.framework.model;

import java.util.Date;

public class AgentAwardDetailVo {
	private String agentName;
	private String agentNo;
	private String agentLevel;
	private String amount;
	private String accountStatus;
	private String remark;
	private Date accountTime;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}

}
