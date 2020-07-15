package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CmVipConfigAgent {
    private Integer id;
    private String agentNo;
    private String agentName;
    private String agentLevel;
    private String srcOrgPrduct;
    private String srcOrgId;
    private BigDecimal vipFee;
    private Integer validPeriod;
    private BigDecimal agentShare;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private String operator;

    private String startTime;
    private String endTime;

    private String errorResult;
    private String handle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getSrcOrgPrduct() {
        return srcOrgPrduct;
    }

    public void setSrcOrgPrduct(String srcOrgPrduct) {
        this.srcOrgPrduct = srcOrgPrduct;
    }

    public String getSrcOrgId() {
        return srcOrgId;
    }

    public void setSrcOrgId(String srcOrgId) {
        this.srcOrgId = srcOrgId;
    }

    public BigDecimal getVipFee() {
        return vipFee;
    }

    public void setVipFee(BigDecimal vipFee) {
        this.vipFee = vipFee;
    }

    public Integer getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }

    public BigDecimal getAgentShare() {
        return agentShare;
    }

    public void setAgentShare(BigDecimal agentShare) {
        this.agentShare = agentShare;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(String errorResult) {
        this.errorResult = errorResult;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
