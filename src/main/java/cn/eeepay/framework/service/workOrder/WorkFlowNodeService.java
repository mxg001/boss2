package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.model.workOrder.WorkFlowNode;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 16:23
 */
public interface WorkFlowNodeService {
    void saveWorkFlowNode(List<WorkFlowNode> workFlowNodes, String userName,Long workTypeId);

    void saveWorkFlowNode(List<WorkFlowNode> nodeList, String orderNo,String userName);

    List<WorkFlowNode> getNodesByWorkTypeID(Long workTypeId);

    List<WorkFlowNode> getNodesByOrderNo(String orderNo);

    WorkFlowNode getNodeByParentFlowNo(String currentFlowNo, String orderNo);

    WorkFlowNode getNodeByFlowNo(String currentFlowNo, String orderNo);

    List<WorkFlowNode> getNodesByFlowNos(String toString, String orderNo);

    void updateStatus(String orderNo,String flowNo,Integer status);
    int update(WorkFlowNode info);

    WorkFlowNode getNodeByOrderNoAndDeptNo(String orderNo, Integer deptId);
}
