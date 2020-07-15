package cn.eeepay.framework.model;

import cn.eeepay.framework.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class NewHappyBackActivityQo {
	private String activeOrder;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activeTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activeTimeEnd;
	private String currentCycle;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date rewardStartTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date rewardStartTimeEnd;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date rewardEndTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date rewardEndTimeEnd;
	private String currentTargetStatus;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date currentTargetTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date currentTargetTimeEnd;
	private String rewardAccountStatus;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private String rewardAccountTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date rewardAccountTimeEnd;
	private String activityTargetStatus;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activityTargetTimeStart;
	@DateTimeFormat
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date activityTargetTimeEnd;
	private String merchantNo;
	private String agentNo;
	private String containsLower;
	private String curAgentNode;

	private Integer pageNo;
	private Integer pageSize;

	List<String> orderNoList;

	private String subType;//欢乐返子类型

	private String activityTypeNo;//欢乐返子类型
	private Integer hardId;//硬件ID
	private String teamId;//组织
	private String teamEntryId;//子组织
	private String teamName;
	private String teamEntryName;

	public void init() {
		// 结束时间+1天 -一秒
		if (activeTimeEnd != null) {
			activeTimeEnd = DateUtils.addSecond(DateUtils.addDate(activeTimeEnd, 1), -1);
		}
		if (rewardStartTimeEnd != null) {
			rewardStartTimeEnd = DateUtils.addSecond(DateUtils.addDate(rewardStartTimeEnd, 1), -1);
		}
		if (rewardEndTimeEnd != null) {
			rewardEndTimeEnd = DateUtils.addSecond(DateUtils.addDate(rewardEndTimeEnd, 1), -1);
		}
		if (currentTargetTimeEnd != null) {
			currentTargetTimeEnd = DateUtils.addSecond(DateUtils.addDate(currentTargetTimeEnd, 1), -1);
		}
		if (rewardAccountTimeEnd != null) {
			rewardAccountTimeEnd = DateUtils.addSecond(DateUtils.addDate(rewardAccountTimeEnd, 1), -1);
		}
		if (activityTargetTimeEnd != null) {
			activityTargetTimeEnd = DateUtils.addSecond(DateUtils.addDate(activityTargetTimeEnd, 1), -1);
		}

	}

	public Date getRewardAccountTimeEnd() {
		return rewardAccountTimeEnd;
	}

	public void setRewardAccountTimeEnd(Date rewardAccountTimeEnd) {
		this.rewardAccountTimeEnd = rewardAccountTimeEnd;
	}

	public String getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(String activeOrder) {
		this.activeOrder = activeOrder;
	}

	public Date getActiveTimeStart() {
		return activeTimeStart;
	}

	public void setActiveTimeStart(Date activeTimeStart) {
		this.activeTimeStart = activeTimeStart;
	}

	public Date getActiveTimeEnd() {
		return activeTimeEnd;
	}

	public void setActiveTimeEnd(Date activeTimeEnd) {
		this.activeTimeEnd = activeTimeEnd;
	}

	public String getCurrentCycle() {
		return currentCycle;
	}

	public void setCurrentCycle(String currentCycle) {
		this.currentCycle = currentCycle;
	}

	public Date getRewardStartTimeStart() {
		return rewardStartTimeStart;
	}

	public void setRewardStartTimeStart(Date rewardStartTimeStart) {
		this.rewardStartTimeStart = rewardStartTimeStart;
	}

	public Date getRewardStartTimeEnd() {
		return rewardStartTimeEnd;
	}

	public void setRewardStartTimeEnd(Date rewardStartTimeEnd) {
		this.rewardStartTimeEnd = rewardStartTimeEnd;
	}

	public Date getRewardEndTimeStart() {
		return rewardEndTimeStart;
	}

	public void setRewardEndTimeStart(Date rewardEndTimeStart) {
		this.rewardEndTimeStart = rewardEndTimeStart;
	}

	public Date getRewardEndTimeEnd() {
		return rewardEndTimeEnd;
	}

	public void setRewardEndTimeEnd(Date rewardEndTimeEnd) {
		this.rewardEndTimeEnd = rewardEndTimeEnd;
	}

	public String getCurrentTargetStatus() {
		return currentTargetStatus;
	}

	public void setCurrentTargetStatus(String currentTargetStatus) {
		this.currentTargetStatus = currentTargetStatus;
	}

	public Date getCurrentTargetTimeStart() {
		return currentTargetTimeStart;
	}

	public void setCurrentTargetTimeStart(Date currentTargetTimeStart) {
		this.currentTargetTimeStart = currentTargetTimeStart;
	}

	public Date getCurrentTargetTimeEnd() {
		return currentTargetTimeEnd;
	}

	public void setCurrentTargetTimeEnd(Date currentTargetTimeEnd) {
		this.currentTargetTimeEnd = currentTargetTimeEnd;
	}

	public String getRewardAccountStatus() {
		return rewardAccountStatus;
	}

	public void setRewardAccountStatus(String rewardAccountStatus) {
		this.rewardAccountStatus = rewardAccountStatus;
	}

	public String getRewardAccountTimeStart() {
		return rewardAccountTimeStart;
	}

	public void setRewardAccountTimeStart(String rewardAccountTimeStart) {
		this.rewardAccountTimeStart = rewardAccountTimeStart;
	}

	public String getActivityTargetStatus() {
		return activityTargetStatus;
	}

	public void setActivityTargetStatus(String activityTargetStatus) {
		this.activityTargetStatus = activityTargetStatus;
	}

	public Date getActivityTargetTimeStart() {
		return activityTargetTimeStart;
	}

	public void setActivityTargetTimeStart(Date activityTargetTimeStart) {
		this.activityTargetTimeStart = activityTargetTimeStart;
	}

	public Date getActivityTargetTimeEnd() {
		return activityTargetTimeEnd;
	}

	public void setActivityTargetTimeEnd(Date activityTargetTimeEnd) {
		this.activityTargetTimeEnd = activityTargetTimeEnd;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getContainsLower() {
		return containsLower;
	}

	public void setContainsLower(String containsLower) {
		this.containsLower = containsLower;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurAgentNode() {
		return curAgentNode;
	}

	public void setCurAgentNode(String curAgentNode) {
		this.curAgentNode = curAgentNode;
	}

	public List<String> getOrderNoList() {
		return orderNoList;
	}

	public void setOrderNoList(List<String> orderNoList) {
		this.orderNoList = orderNoList;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}


	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public Integer getHardId() {
		return hardId;
	}

	public void setHardId(Integer hardId) {
		this.hardId = hardId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamEntryName() {
		return teamEntryName;
	}

	public void setTeamEntryName(String teamEntryName) {
		this.teamEntryName = teamEntryName;
	}

	@Override
	public String toString() {
		return "NewHappyBackActivityQo{" +
				"activeOrder='" + activeOrder + '\'' +
				", activeTimeStart=" + activeTimeStart +
				", activeTimeEnd=" + activeTimeEnd +
				", currentCycle='" + currentCycle + '\'' +
				", rewardStartTimeStart=" + rewardStartTimeStart +
				", rewardStartTimeEnd=" + rewardStartTimeEnd +
				", rewardEndTimeStart=" + rewardEndTimeStart +
				", rewardEndTimeEnd=" + rewardEndTimeEnd +
				", currentTargetStatus='" + currentTargetStatus + '\'' +
				", currentTargetTimeStart=" + currentTargetTimeStart +
				", currentTargetTimeEnd=" + currentTargetTimeEnd +
				", rewardAccountStatus='" + rewardAccountStatus + '\'' +
				", rewardAccountTimeStart='" + rewardAccountTimeStart + '\'' +
				", rewardAccountTimeEnd=" + rewardAccountTimeEnd +
				", activityTargetStatus='" + activityTargetStatus + '\'' +
				", activityTargetTimeStart=" + activityTargetTimeStart +
				", activityTargetTimeEnd=" + activityTargetTimeEnd +
				", merchantNo='" + merchantNo + '\'' +
				", agentNo='" + agentNo + '\'' +
				", containsLower='" + containsLower + '\'' +
				", curAgentNode='" + curAgentNode + '\'' +
				", pageNo=" + pageNo +
				", pageSize=" + pageSize +
				", orderNoList=" + orderNoList +
				'}';
	}
}
