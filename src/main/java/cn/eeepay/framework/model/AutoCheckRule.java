package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 自动审核规则
 * 
 * @author Administrator
 *
 */
public class AutoCheckRule {

	private int id;
	
	private String ruleCode;// 规则标识
	
	private String ruleDis;// 规则说明
	
	private Integer isOpen;// 是否打开 1开 0 是关
	
	private Integer isPass;// 是否必过 1开 0 是关
	
	private String fileList;// 规则需要的文件参数名
	
	private Date createTime;// 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleDis() {
		return ruleDis;
	}

	public void setRuleDis(String ruleDis) {
		this.ruleDis = ruleDis;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public String getFileList() {
		return fileList;
	}

	public void setFileList(String fileList) {
		this.fileList = fileList;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	
}
