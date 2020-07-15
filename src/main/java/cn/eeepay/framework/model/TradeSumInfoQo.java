package cn.eeepay.framework.model;

import java.util.List;

public class TradeSumInfoQo {

	private String startTime;
	private String endTime;

	private List<String> agentNoList;

	private String agentNo;

	private int showLower;

	private Integer incomeStatus;

	private String agentOem;
	private String recordedStartTime;
	private String recordedEndTime;
	private String teamEntryId;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getAgentNoList() {
		return agentNoList;
	}

	public void setAgentNoList(List<String> agentNoList) {
		this.agentNoList = agentNoList;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public int getShowLower() {
		return showLower;
	}

	public void setShowLower(int showLower) {
		this.showLower = showLower;
	}

	public Integer getIncomeStatus() {
		return incomeStatus;
	}

	public void setIncomeStatus(Integer incomeStatus) {
		this.incomeStatus = incomeStatus;
	}

	public String getAgentOem() {
		return agentOem;
	}

	public void setAgentOem(String agentOem) {
		this.agentOem = agentOem;
	}

	public String getRecordedStartTime() {
		return recordedStartTime;
	}

	public void setRecordedStartTime(String recordedStartTime) {
		this.recordedStartTime = recordedStartTime;
	}

	public String getRecordedEndTime() {
		return recordedEndTime;
	}

	public void setRecordedEndTime(String recordedEndTime) {
		this.recordedEndTime = recordedEndTime;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

}
