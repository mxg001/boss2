package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * table risk_event_record
 * desc 风控事件记录
 * @author WCK
 *
 */
public class RiskEventRecord {
	
	private Integer id;//主键
	
	private Date createTime;//创建时间
	
	private String rollNo;//编号（关联黑名单主表）
	
	private String merchantNo;//商户ID
	
	private String merchantName;//商户名称
	
	private String agentNo;//代理商编号
	
	private String agentName;//代理商名称
	
	private Integer rulesNo;//规则编号
	
	private String rulesEngine;//规则引擎
	
	private Integer rulesInstruction;//规则指令

	private Integer handleStatus;//处理状态：1已处理  0未处理
	
	private Integer handleResults;//处理结果：1安全  2可疑  3风险
	
	private Date handleTime;//处理时间
	
	private String handlePerson;//处理人
	
	private String handleRemark;//处理备注
	
	private Integer rollStatus;//处理状态，管理黑名单表

	private String orderNo;//交易订单号

	private String ruleDesc;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date screateTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ecreateTime;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date shandleTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ehandleTime;

    private String mobilephone;

    private String location;

	private String ip;
	private String ipArea;
	private String ipDecision;//ip判断结果
	private String gnb;
	private String gnbDecision;//基站判断结果
	private String lbs;
	private String lbsDecision;//经纬度判断结果
	private String decision;

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public Integer getRulesNo() {
		return rulesNo;
	}

	public void setRulesNo(Integer rulesNo) {
		this.rulesNo = rulesNo;
	}

	public String getRulesEngine() {
		return rulesEngine;
	}

	public void setRulesEngine(String rulesEngine) {
		this.rulesEngine = rulesEngine;
	}

	public Integer getRulesInstruction() {
		return rulesInstruction;
	}

	public void setRulesInstruction(Integer rulesInstruction) {
		this.rulesInstruction = rulesInstruction;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Integer getHandleResults() {
		return handleResults;
	}

	public void setHandleResults(Integer handleResults) {
		this.handleResults = handleResults;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandlePerson() {
		return handlePerson;
	}

	public void setHandlePerson(String handlePerson) {
		this.handlePerson = handlePerson;
	}

	public String getHandleRemark() {
		return handleRemark;
	}

	public void setHandleRemark(String handleRemark) {
		this.handleRemark = handleRemark;
	}

	public Integer getRollStatus() {
		return rollStatus;
	}

	public void setRollStatus(Integer rollStatus) {
		this.rollStatus = rollStatus;
	}

	public Date getScreateTime() {
		return screateTime;
	}

	public void setScreateTime(Date screateTime) {
		this.screateTime = screateTime;
	}

	public Date getEcreateTime() {
		return ecreateTime;
	}

	public void setEcreateTime(Date ecreateTime) {
		this.ecreateTime = ecreateTime;
	}

	public Date getShandleTime() {
		return shandleTime;
	}

	public void setShandleTime(Date shandleTime) {
		this.shandleTime = shandleTime;
	}

	public Date getEhandleTime() {
		return ehandleTime;
	}

	public void setEhandleTime(Date ehandleTime) {
		this.ehandleTime = ehandleTime;
	}


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpArea() {
		return ipArea;
	}

	public void setIpArea(String ipArea) {
		this.ipArea = ipArea;
	}

	public String getIpDecision() {
		return ipDecision;
	}

	public void setIpDecision(String ipDecision) {
		this.ipDecision = ipDecision;
	}

	public String getGnb() {
		return gnb;
	}

	public void setGnb(String gnb) {
		this.gnb = gnb;
	}

	public String getGnbDecision() {
		return gnbDecision;
	}

	public void setGnbDecision(String gnbDecision) {
		this.gnbDecision = gnbDecision;
	}

	public String getLbs() {
		return lbs;
	}

	public void setLbs(String lbs) {
		this.lbs = lbs;
	}

	public String getLbsDecision() {
		return lbsDecision;
	}

	public void setLbsDecision(String lbsDecision) {
		this.lbsDecision = lbsDecision;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}
}