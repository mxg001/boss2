package cn.eeepay.framework.model;

/**
 * 功能控制开关
 * 
 * @author Administrator
 *
 */
public class FunctionManager {

	private int id;

	private String functionNumber;

	private String functionName;

	private Integer functionSwitch;

	private Integer agentControl;

	//liuks 进件开关需求
	private Integer agentIsControl;//0:默认 不显示代理商控制 1,显示代理商控制

	private String setting;//是否特定配置页面,0-无,1-有

	private String blacklist;//是否显示黑名单按钮,0:否,1:是

	private String merBlackList;//是否显示商户黑名单按钮,0:否,1:是

	private String remark;//功能说明

	private String valueInfo;//业务设置，放在sys_config的字符串

	private String forceT1ShowStatus;//显示状态，仅针对强制T1，放在sys_config表


	private String limitSys;

	public String getMerBlackList() {
		return merBlackList;
	}

	public void setMerBlackList(String merBlackList) {
		this.merBlackList = merBlackList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFunctionNumber() {
		return functionNumber;
	}

	public void setFunctionNum(String functionNumber) {
		this.functionNumber = functionNumber;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Integer getFunctionSwitch() {
		return functionSwitch;
	}

	public void setFunctionSwitch(Integer functionSwitch) {
		this.functionSwitch = functionSwitch;
	}

	public Integer getAgentControl() {
		return agentControl;
	}

	public void setAgentControl(Integer agentControl) {
		this.agentControl = agentControl;
	}

	public Integer getAgentIsControl() {
		return agentIsControl;
	}

	public void setAgentIsControl(Integer agentIsControl) {
		this.agentIsControl = agentIsControl;
	}

	public void setFunctionNumber(String functionNumber) {
		this.functionNumber = functionNumber;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getValueInfo() {
		return valueInfo;
	}

	public void setValueInfo(String valueInfo) {
		this.valueInfo = valueInfo;
	}

	public String getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}

	public String getForceT1ShowStatus() {
		return forceT1ShowStatus;
	}

	public void setForceT1ShowStatus(String forceT1ShowStatus) {
		this.forceT1ShowStatus = forceT1ShowStatus;
	}

	public String getLimitSys() {
		return limitSys;
	}

	public void setLimitSys(String limitSys) {
		this.limitSys = limitSys;
	}
}
