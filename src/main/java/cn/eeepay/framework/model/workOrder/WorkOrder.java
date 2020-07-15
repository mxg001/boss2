package cn.eeepay.framework.model.workOrder;

import java.util.Date;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 10:08
 */
public class WorkOrder {
    private Long id;
    private String orderNo;
    private Long workTypeId;
    private String workTypeName;
    private Integer status;
    private String createType;
    private Long createUserId;
    private String createUserName;
    private Integer currentStatus;
    private Integer currentDeptNo;
    private Integer currentUserId;
    private Long createDeptNo;
    private String currentDeptName;
    private String currentUserName;
    private String currentFlowNo;
    private String dealProcess;
    private String dealProcessName;
    private String flowDesc;
    private String receiveAgentNode;
    private String receiverAgentName;
    private String agentNo;
    private String agentName;
    private String oneAgentNo;
    private String oneAgentName;
    private Integer replyType;
    private Integer agentReplyStatus;
    private Integer urgeStatus;
    private Integer readStatus;
    private String workContent;
    private Integer endReplyDays;
    private Date endReplyTime;
    private Date createTime;
    private String createTimeStr;
    private Date createTimeBegin;
    private Date createTimeEnd;
    private Date lastUpdateTimeBegin;
    private Date lastUpdateTimeEnd;
    private Date lastUpdateTime;
    private Date endReplyTimeBegin;
    private Date endReplyTimeEnd;
    private List<WorkFileInfo> workFileInfos;
    private Integer agentShow;
    private List<WorkFlowNode> preNodes;
    private List<WorkOrderItem> items;
    private List<WorkRemarkRecord> remarks;
    private boolean remarkStatus;
    private boolean replyStatus;
    private boolean closeStatus;
    private boolean rejectStatus;
    private Integer detailStatus;
    private Integer transferStatus;
    private Integer overDueReply;
    private Integer queryType;
    private Integer deptNo;
    private String oneAgentNode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(Long workTypeId) {
        this.workTypeId = workTypeId;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getCurrentDeptNo() {
        return currentDeptNo;
    }

    public void setCurrentDeptNo(Integer currentDeptNo) {
        this.currentDeptNo = currentDeptNo;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Long getCreateDeptNo() {
        return createDeptNo;
    }

    public void setCreateDeptNo(Long createDeptNo) {
        this.createDeptNo = createDeptNo;
    }

    public String getCurrentDeptName() {
        return currentDeptName;
    }

    public void setCurrentDeptName(String currentDeptName) {
        this.currentDeptName = currentDeptName;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getCurrentFlowNo() {
        return currentFlowNo;
    }

    public void setCurrentFlowNo(String currentFlowNo) {
        this.currentFlowNo = currentFlowNo;
    }

    public String getDealProcess() {
        return dealProcess;
    }

    public void setDealProcess(String dealProcess) {
        this.dealProcess = dealProcess;
    }

    public String getDealProcessName() {
        return dealProcessName;
    }

    public void setDealProcessName(String dealProcessName) {
        this.dealProcessName = dealProcessName;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    public String getReceiveAgentNode() {
        return receiveAgentNode;
    }

    public void setReceiveAgentNode(String receiveAgentNode) {
        this.receiveAgentNode = receiveAgentNode;
    }

    public String getReceiverAgentName() {
        return receiverAgentName;
    }

    public void setReceiverAgentName(String receiverAgentName) {
        this.receiverAgentName = receiverAgentName;
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

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public Integer getReplyType() {
        return replyType;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }

    public Integer getAgentReplyStatus() {
        return agentReplyStatus;
    }

    public void setAgentReplyStatus(Integer agentReplyStatus) {
        this.agentReplyStatus = agentReplyStatus;
    }

    public Integer getUrgeStatus() {
        return urgeStatus;
    }

    public void setUrgeStatus(Integer urgeStatus) {
        this.urgeStatus = urgeStatus;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getLastUpdateTimeBegin() {
        return lastUpdateTimeBegin;
    }

    public void setLastUpdateTimeBegin(Date lastUpdateTimeBegin) {
        this.lastUpdateTimeBegin = lastUpdateTimeBegin;
    }

    public Date getLastUpdateTimeEnd() {
        return lastUpdateTimeEnd;
    }

    public void setLastUpdateTimeEnd(Date lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getEndReplyTimeBegin() {
        return endReplyTimeBegin;
    }

    public void setEndReplyTimeBegin(Date endReplyTimeBegin) {
        this.endReplyTimeBegin = endReplyTimeBegin;
    }

    public Date getEndReplyTimeEnd() {
        return endReplyTimeEnd;
    }

    public void setEndReplyTimeEnd(Date endReplyTimeEnd) {
        this.endReplyTimeEnd = endReplyTimeEnd;
    }

    public List<WorkFileInfo> getWorkFileInfos() {
        return workFileInfos;
    }

    public void setWorkFileInfos(List<WorkFileInfo> workFileInfos) {
        this.workFileInfos = workFileInfos;
    }

    public Integer getAgentShow() {
        return agentShow;
    }

    public void setAgentShow(Integer agentShow) {
        this.agentShow = agentShow;
    }

    public List<WorkFlowNode> getPreNodes() {
        return preNodes;
    }

    public void setPreNodes(List<WorkFlowNode> preNodes) {
        this.preNodes = preNodes;
    }

    public List<WorkOrderItem> getItems() {
        return items;
    }

    public void setItems(List<WorkOrderItem> items) {
        this.items = items;
    }

    public List<WorkRemarkRecord> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<WorkRemarkRecord> remarks) {
        this.remarks = remarks;
    }

    public boolean isRemarkStatus() {
        return remarkStatus;
    }

    public void setRemarkStatus(boolean remarkStatus) {
        this.remarkStatus = remarkStatus;
    }

    public boolean isReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(boolean replyStatus) {
        this.replyStatus = replyStatus;
    }

    public boolean isCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(boolean closeStatus) {
        this.closeStatus = closeStatus;
    }

    public boolean isRejectStatus() {
        return rejectStatus;
    }

    public void setRejectStatus(boolean rejectStatus) {
        this.rejectStatus = rejectStatus;
    }

    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Integer getOverDueReply() {
        return overDueReply;
    }

    public void setOverDueReply(Integer overDueReply) {
        this.overDueReply = overDueReply;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public Integer getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Integer deptNo) {
        this.deptNo = deptNo;
    }

    public String getOneAgentNode() {
        return oneAgentNode;
    }

    public void setOneAgentNode(String oneAgentNode) {
        this.oneAgentNode = oneAgentNode;
    }
}
