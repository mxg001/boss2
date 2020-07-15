package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table sys_msg_info
 * desc 提示语
 * 
 * @author tans
 */
public class Msg {
	private String msgCode;
	private String msgType;
	private String moduleName;
	private String userMsg;
	private String reason;
	private String solution;
	private String sourceOrg;
	
	private String mdName;
	private String mtName;

	public String getMtName() {
		return mtName;
	}

	public void setMtName(String mtName) {
		this.mtName = mtName;
	}

	public String getMdName() {
		return mdName;
	}

	public void setMdName(String mdName) {
		this.mdName = mdName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	private String sourceName;
	private String sourceCode;
	private String sourceType;
	private String sourceMsg;
	private String sourceRemark;
	private Date createTime;
	private Date last_updateTime;

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getUserMsg() {
		return userMsg;
	}

	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getSourceOrg() {
		return sourceOrg;
	}

	public void setSourceOrg(String sourceOrg) {
		this.sourceOrg = sourceOrg;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceMsg() {
		return sourceMsg;
	}

	public void setSourceMsg(String sourceMsg) {
		this.sourceMsg = sourceMsg;
	}

	public String getSourceRemark() {
		return sourceRemark;
	}

	public void setSourceRemark(String sourceRemark) {
		this.sourceRemark = sourceRemark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLast_updateTime() {
		return last_updateTime;
	}

	public void setLast_updateTime(Date last_updateTime) {
		this.last_updateTime = last_updateTime;
	}




}