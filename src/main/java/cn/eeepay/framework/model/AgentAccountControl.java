package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class AgentAccountControl {
    private Integer id;
    private String agentNo;
    private String agentName;
    private BigDecimal retainAmount;
    private Integer status;
    private String createTime;
    private Integer defaultStatus;

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

    public BigDecimal getRetainAmount() {
        return retainAmount;
    }

    public void setRetainAmount(BigDecimal retainAmount) {
        this.retainAmount = retainAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }
}
