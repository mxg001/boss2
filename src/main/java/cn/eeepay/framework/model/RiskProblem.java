package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table risk_problem 
 * desc 风控问题
 * @author thj
 *
 */
public class RiskProblem {
    private Integer problemId;

    private Integer problemType;

    private Integer riskRulesNo;

    private String problemTitle;

    private String problemDescription;

    private Integer status;

    private String dealPerson;

    private String dealMeasures;

    private String auditPerson;

    private String auditOpinion;

    private String createPerson;

    private Date createTime;

    private Integer auditResult;
    
    private String realName;

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public Integer getRiskRulesNo() {
        return riskRulesNo;
    }

    public void setRiskRulesNo(Integer riskRulesNo) {
        this.riskRulesNo = riskRulesNo;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle == null ? null : problemTitle.trim();
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription == null ? null : problemDescription.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDealPerson() {
        return dealPerson;
    }

    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson == null ? null : dealPerson.trim();
    }

    public String getDealMeasures() {
        return dealMeasures;
    }

    public void setDealMeasures(String dealMeasures) {
        this.dealMeasures = dealMeasures == null ? null : dealMeasures.trim();
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson == null ? null : auditPerson.trim();
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion == null ? null : auditOpinion.trim();
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}