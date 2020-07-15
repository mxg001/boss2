package cn.eeepay.framework.model.workOrder;

import java.util.Date;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/29 14:32
 */
public class WorkOrderItem {
    private Long id;
    private String orderNo;
    private String flowNo;
    private Integer rejectStatus;
    private Integer transferStatus;
    private Long senderId;
    private String senderName;
    private Long senderDeptNo;
    private String senderDeptName;
    private Long receiverId;
    private Long receiverDeptNo;
    private String receiverDeptName;
    private String receiverFlowNo;
    private String replyContent;
    private String remarkContent;
    private String rejectReason;
    private List<WorkFileInfo> fileInfos;
    private List<WorkFileInfo> imgInfos;
    private List<WorkFileInfo> workFileInfos;
    private List<WorkRemarkRecord> remarks;
    private Date createTime;
    private String createTimeStr;
    private String status;
    private WorkFlowNode node;

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

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public Integer getRejectStatus() {
        return rejectStatus;
    }

    public void setRejectStatus(Integer rejectStatus) {
        this.rejectStatus = rejectStatus;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderDeptNo() {
        return senderDeptNo;
    }

    public void setSenderDeptNo(Long senderDeptNo) {
        this.senderDeptNo = senderDeptNo;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getReceiverDeptNo() {
        return receiverDeptNo;
    }

    public void setReceiverDeptNo(Long receiverDeptNo) {
        this.receiverDeptNo = receiverDeptNo;
    }

    public String getReceiverFlowNo() {
        return receiverFlowNo;
    }

    public void setReceiverFlowNo(String receiverFlowNo) {
        this.receiverFlowNo = receiverFlowNo;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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

    public List<WorkRemarkRecord> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<WorkRemarkRecord> remarks) {
        this.remarks = remarks;
    }

    public String getSenderDeptName() {
        return senderDeptName;
    }

    public void setSenderDeptName(String senderDeptName) {
        this.senderDeptName = senderDeptName;
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

    public List<WorkFileInfo> getWorkFileInfos() {
        return workFileInfos;
    }

    public void setWorkFileInfos(List<WorkFileInfo> workFileInfos) {
        this.workFileInfos = workFileInfos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverDeptName() {
        return receiverDeptName;
    }

    public void setReceiverDeptName(String receiverDeptName) {
        this.receiverDeptName = receiverDeptName;
    }

    public WorkFlowNode getNode() {
        return node;
    }

    public void setNode(WorkFlowNode node) {
        this.node = node;
    }
}
