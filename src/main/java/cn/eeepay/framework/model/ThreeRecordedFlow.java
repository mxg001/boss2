package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ThreeRecordedFlow {
	private Long id;

    private String agentNo;

    private Date createTime;

    private BigDecimal activteAmount;

    private Integer recordedStatus;

    private BigDecimal recordedSum;

    private String fromSerialNo;

    private String transOrderNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getActivteAmount() {
        return activteAmount;
    }

    public void setActivteAmount(BigDecimal activteAmount) {
        this.activteAmount = activteAmount;
    }

    public Integer getRecordedStatus() {
        return recordedStatus;
    }

    public void setRecordedStatus(Integer recordedStatus) {
        this.recordedStatus = recordedStatus;
    }

    public BigDecimal getRecordedSum() {
        return recordedSum;
    }

    public void setRecordedSum(BigDecimal recordedSum) {
        this.recordedSum = recordedSum;
    }

    public String getFromSerialNo() {
        return fromSerialNo;
    }

    public void setFromSerialNo(String fromSerialNo) {
        this.fromSerialNo = fromSerialNo;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }
}