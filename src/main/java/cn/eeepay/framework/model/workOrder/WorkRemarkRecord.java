package cn.eeepay.framework.model.workOrder;

import java.util.Date;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/5/6 10:08
 */
public class WorkRemarkRecord {
    private Long id;
    private Integer belongType;
    private Long belongId;
    private Integer agentShow;
    private String remarkContent;
    private String operator;
    private String operatorName;
    private Long orderId;
    private List<WorkFileInfo> workFileInfos;
    private List<WorkFileInfo> fileInfos;
    private List<WorkFileInfo> imgInfos;
    private String createTimeStr;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBelongType() {
        return belongType;
    }

    public void setBelongType(Integer belongType) {
        this.belongType = belongType;
    }

    public Long getBelongId() {
        return belongId;
    }

    public void setBelongId(Long belongId) {
        this.belongId = belongId;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getAgentShow() {
        return agentShow;
    }

    public void setAgentShow(Integer agentShow) {
        this.agentShow = agentShow;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<WorkFileInfo> getWorkFileInfos() {
        return workFileInfos;
    }

    public void setWorkFileInfos(List<WorkFileInfo> workFileInfos) {
        this.workFileInfos = workFileInfos;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<WorkFileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<WorkFileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public List<WorkFileInfo> getImgInfos() {
        return imgInfos;
    }

    public void setImgInfos(List<WorkFileInfo> imgInfos) {
        this.imgInfos = imgInfos;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
