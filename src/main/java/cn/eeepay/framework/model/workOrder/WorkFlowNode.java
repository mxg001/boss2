package cn.eeepay.framework.model.workOrder;

import java.util.Date;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 16:34
 */
public class WorkFlowNode {
    private Long id;
    private String flowNo;
    private String parentFlowNo;
    private String flowNode;
    private Integer level;
    private Long workTypeId;
    private Long deptNo;
    private String deptName;
    private String flowDesc;
    private Integer endReplyDays;
    private Date endReplyTime;
    private String orderNo;
    private Date createTime;
    private String operator;
    private Long currentUserId;
    private Integer currentStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getParentFlowNo() {
        return parentFlowNo;
    }

    public void setParentFlowNo(String parentFlowNo) {
        this.parentFlowNo = parentFlowNo;
    }

    public String getFlowNode() {
        return flowNode;
    }

    public void setFlowNode(String flowNode) {
        this.flowNode = flowNode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(Long workTypeId) {
        this.workTypeId = workTypeId;
    }

    public Long getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Long deptNo) {
        this.deptNo = deptNo;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    public Integer getEndReplyDays() {
        return endReplyDays;
    }

    public void setEndReplyDays(Integer endReplyDays) {
        this.endReplyDays = endReplyDays;
    }

    public Date getEndReplyTime() {
        return endReplyTime;
    }

    public void setEndReplyTime(Date endReplyTime) {
        this.endReplyTime = endReplyTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }
}
