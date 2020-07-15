package cn.eeepay.framework.model.workOrder;

import java.util.Date;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 16:35
 */
public class WorkType {
    private Long id;
    private String name;
    private Integer agentShow;
    private Integer replyType;
    private String desc;
    private String dealProcess;
    private String dealProcessName;
    private Date createTime;
    private List<WorkFlowNode> workFlowNodeList;
    private List<WorkFileInfo> workFileInfos;
    private String operator;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAgentShow() {
        return agentShow;
    }

    public void setAgentShow(Integer agentShow) {
        this.agentShow = agentShow;
    }

    public Integer getReplyType() {
        return replyType;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDealProcess() {
        return dealProcess;
    }

    public void setDealProcess(String dealProcess) {
        this.dealProcess = dealProcess;
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


    public List<WorkFlowNode> getWorkFlowNodeList() {
        return workFlowNodeList;
    }

    public void setWorkFlowNodeList(List<WorkFlowNode> workFlowNodeList) {
        this.workFlowNodeList = workFlowNodeList;
    }

    public List<WorkFileInfo> getWorkFileInfos() {
        return workFileInfos;
    }

    public void setWorkFileInfos(List<WorkFileInfo> workFileInfos) {
        this.workFileInfos = workFileInfos;
    }

    public String getDealProcessName() {
        return dealProcessName;
    }

    public void setDealProcessName(String dealProcessName) {
        this.dealProcessName = dealProcessName;
    }
}
