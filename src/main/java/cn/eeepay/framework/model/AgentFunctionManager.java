package cn.eeepay.framework.model;

import java.util.Date;
/**
 * 代理商管理
 * @author Administrator
 *
 */
public class AgentFunctionManager {

	private int id;
	
	private String agentNo;
	
	private String agentName;
	
	private Date createTime;
	
	private String createUser;
	
	private String functionNumber;
	
	private String teamId;
	
	private Integer containsLower;
	private Integer blacklist;
	
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getFunctionNumber() {
		return functionNumber;
	}

	public void setFunctionNumber(String functionNumber) {
		this.functionNumber = functionNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Integer getContainsLower() {
		return containsLower;
	}

	public void setContainsLower(Integer containsLower) {
		this.containsLower = containsLower;
	}

	public Integer getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Integer blacklist) {
		this.blacklist = blacklist;
	}

	
}
