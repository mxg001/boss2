package cn.eeepay.framework.model;

import java.util.Date;

public class StaticUserInfo {

	private Integer staticUserId;
	private String  mobilephone;
	private String userName;
	private String enableFlag;
	private String remark;
	private String department;
	
	private String merchantNo;
	private String totalAmt;
	private String totalCnt;
	private String netAmt;
	private String netCnt;
	
	private Date sTime;
	private Date eTime;
	public Integer getStaticUserId() {
		return staticUserId;
	}
	public void setStaticUserId(Integer staticUserId) {
		this.staticUserId = staticUserId;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}
	public String getNetCnt() {
		return netCnt;
	}
	public void setNetCnt(String netCnt) {
		this.netCnt = netCnt;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
}
