package cn.eeepay.framework.model;

import java.util.Date;

public class AutoCheckResult {

	private Integer id;
	private String ruleCode;
	private String checkResult;
	private String checkVerdict;
	private String checkInfo;
	private String merchantNo;
	private String resInfo;
	private Date createTime;
	private String bpId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckVerdict() {
		return checkVerdict;
	}
	public void setCheckVerdict(String checkVerdict) {
		this.checkVerdict = checkVerdict;
	}
	public String getCheckInfo() {
		return checkInfo;
	}
	public void setCheckInfo(String checkInfo) {
		this.checkInfo = checkInfo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getResInfo() {
		return resInfo;
	}
	public void setResInfo(String resInfo) {
		this.resInfo = resInfo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
}
