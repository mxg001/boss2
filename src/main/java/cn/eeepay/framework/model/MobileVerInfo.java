package cn.eeepay.framework.model;

import java.util.Date;

public class MobileVerInfo {

	private int id;
	private int platform;
	private String version;
	private String appUrl;
	private String url;
	private int downFlag;
	private String verDesc;
	private String appType;
	private String lowestVersion;
	private String appLogo;
	private Date createTime;
	private String appName;
	private String logUrl;
	private String logAppLogo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDownFlag() {
		return downFlag;
	}
	public void setDownFlag(int downFlag) {
		this.downFlag = downFlag;
	}
	public String getVerDesc() {
		return verDesc;
	}
	public void setVerDesc(String verDesc) {
		this.verDesc = verDesc;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getLowestVersion() {
		return lowestVersion;
	}
	public void setLowestVersion(String lowestVersion) {
		this.lowestVersion = lowestVersion;
	}
	public String getAppLogo() {
		return appLogo;
	}
	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLogUrl() {
		return logUrl;
	}
	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}
	public String getLogAppLogo() {
		return logAppLogo;
	}
	public void setLogAppLogo(String logAppLogo) {
		this.logAppLogo = logAppLogo;
	}
}

