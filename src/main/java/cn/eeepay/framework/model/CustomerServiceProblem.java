package cn.eeepay.framework.model;

import java.util.Date;
import java.util.List;

public class CustomerServiceProblem {

	private Integer problemId;

	private String problemType;

	private String problemName;

	private String problemContent;

	private String appScope;

	private Integer clicks;

	private Date createTime;

	private Date updateTime;

	private String createUser;

	private String updateUser;

	private Integer problemStatus;

	//
	private String problemTypeName;
	private String appScopeName;


	private Integer solveNum;//解决次数
	private Integer noSolveNum;//未解决次数

	private List<AppScopeInfo> appScopeList;

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getProblemContent() {
		return problemContent;
	}

	public void setProblemContent(String problemContent) {
		this.problemContent = problemContent;
	}

	public String getAppScope() {
		return appScope;
	}

	public void setAppScope(String appScope) {
		this.appScope = appScope;
	}

	public Integer getClicks() {
		return clicks;
	}

	public void setClicks(Integer clicks) {
		this.clicks = clicks;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getProblemStatus() {
		return problemStatus;
	}

	public void setProblemStatus(Integer problemStatus) {
		this.problemStatus = problemStatus;
	}

	public String getProblemTypeName() {
		return problemTypeName;
	}

	public void setProblemTypeName(String problemTypeName) {
		this.problemTypeName = problemTypeName;
	}

	public String getAppScopeName() {
		return appScopeName;
	}

	public void setAppScopeName(String appScopeName) {
		this.appScopeName = appScopeName;
	}

	public List<AppScopeInfo> getAppScopeList() {
		return appScopeList;
	}

	public void setAppScopeList(List<AppScopeInfo> appScopeList) {
		this.appScopeList = appScopeList;
	}

	public Integer getSolveNum() {
		return solveNum;
	}

	public void setSolveNum(Integer solveNum) {
		this.solveNum = solveNum;
	}

	public Integer getNoSolveNum() {
		return noSolveNum;
	}

	public void setNoSolveNum(Integer noSolveNum) {
		this.noSolveNum = noSolveNum;
	}
}