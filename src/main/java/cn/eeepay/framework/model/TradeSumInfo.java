package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class TradeSumInfo {
	private Long id;
	private Date createTime;
	private Date updateTime;
	private String agentNo;
	private String branch;
	private String oneLevel;
	private String twoLevel;
	private String threeLevel;
	private String fourLevel;
	private String fiveLevel;
	private BigDecimal tradeSum;
	private Integer merSum;
	private Integer activateSum;
	private Integer machinesStock;
	private Integer unusedMachines;
	private Integer expiredNotActivated;
	private BigDecimal threeIncome;
	private Date recordedDate;
	private Integer recordedStatus;

	private String teamId;
	private Integer incomeCalc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getOneLevel() {
		return oneLevel;
	}

	public void setOneLevel(String oneLevel) {
		this.oneLevel = oneLevel;
	}

	public String getTwoLevel() {
		return twoLevel;
	}

	public void setTwoLevel(String twoLevel) {
		this.twoLevel = twoLevel;
	}

	public String getThreeLevel() {
		return threeLevel;
	}

	public void setThreeLevel(String threeLevel) {
		this.threeLevel = threeLevel;
	}

	public String getFourLevel() {
		return fourLevel;
	}

	public void setFourLevel(String fourLevel) {
		this.fourLevel = fourLevel;
	}

	public String getFiveLevel() {
		return fiveLevel;
	}

	public void setFiveLevel(String fiveLevel) {
		this.fiveLevel = fiveLevel;
	}

	public BigDecimal getTradeSum() {
		return tradeSum;
	}

	public void setTradeSum(BigDecimal tradeSum) {
		this.tradeSum = tradeSum;
	}

	public Integer getMerSum() {
		return merSum;
	}

	public void setMerSum(Integer merSum) {
		this.merSum = merSum;
	}

	public Integer getActivateSum() {
		return activateSum;
	}

	public void setActivateSum(Integer activateSum) {
		this.activateSum = activateSum;
	}

	public Integer getMachinesStock() {
		return machinesStock;
	}

	public void setMachinesStock(Integer machinesStock) {
		this.machinesStock = machinesStock;
	}

	public Integer getUnusedMachines() {
		return unusedMachines;
	}

	public void setUnusedMachines(Integer unusedMachines) {
		this.unusedMachines = unusedMachines;
	}

	public Integer getExpiredNotActivated() {
		return expiredNotActivated;
	}

	public void setExpiredNotActivated(Integer expiredNotActivated) {
		this.expiredNotActivated = expiredNotActivated;
	}

	public BigDecimal getThreeIncome() {
		return threeIncome;
	}

	public void setThreeIncome(BigDecimal threeIncome) {
		this.threeIncome = threeIncome;
	}

	public Date getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}

	public Integer getRecordedStatus() {
		return recordedStatus;
	}

	public void setRecordedStatus(Integer recordedStatus) {
		this.recordedStatus = recordedStatus;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getIncomeCalc() {
		return incomeCalc;
	}

	public void setIncomeCalc(Integer incomeCalc) {
		this.incomeCalc = incomeCalc;
	}
}