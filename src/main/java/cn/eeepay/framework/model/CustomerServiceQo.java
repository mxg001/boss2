package cn.eeepay.framework.model;

public class CustomerServiceQo {

	private Integer customServiceProblemType;
	private String problemName;
	private String appScope;

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getAppScope() {
		return appScope;
	}

	public void setAppScope(String appScope) {
		this.appScope = appScope;
	}

	public Integer getCustomServiceProblemType() {
		return customServiceProblemType;
	}

	public void setCustomServiceProblemType(Integer customServiceProblemType) {
		this.customServiceProblemType = customServiceProblemType;
	}

}
