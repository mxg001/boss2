package cn.eeepay.framework.model;

import java.util.Date;

/**
 * limit_add_task_list
 * @author	mays
 * @date	2018年5月7日
 */
public class CmTaskInfo {

	private String id;
	private Integer cardId;
	private String taskType;
	private String taskValue;
	private String startTime;
	private String endTime;
	private String increasePossible;
	private String cardHealth;
	private String taskHaveComplete;
	private String haveNext;
	private String valueArray;
	private String bankName;
	private String taskStatus;
	private Date updateTime;

	private String cardNo;
	private String userNo;
	private String userName;
	private String taskTitle;
	private String dataType;
	private String targetId;

	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskValue() {
		return taskValue;
	}
	public void setTaskValue(String taskValue) {
		this.taskValue = taskValue;
	}
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
	public String getIncreasePossible() {
		return increasePossible;
	}
	public void setIncreasePossible(String increasePossible) {
		this.increasePossible = increasePossible;
	}
	public String getCardHealth() {
		return cardHealth;
	}
	public void setCardHealth(String cardHealth) {
		this.cardHealth = cardHealth;
	}
	public String getTaskHaveComplete() {
		return taskHaveComplete;
	}
	public void setTaskHaveComplete(String taskHaveComplete) {
		this.taskHaveComplete = taskHaveComplete;
	}
	public String getHaveNext() {
		return haveNext;
	}
	public void setHaveNext(String haveNext) {
		this.haveNext = haveNext;
	}
	public String getValueArray() {
		return valueArray;
	}
	public void setValueArray(String valueArray) {
		this.valueArray = valueArray;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
