package cn.eeepay.framework.service.workOrder.impl;

import cn.eeepay.framework.dao.workOrder.WorkFlowNodeDao;
import cn.eeepay.framework.exception.WorkOrderException;
import cn.eeepay.framework.model.workOrder.WorkFlowNode;
import cn.eeepay.framework.service.impl.SeqService;
import cn.eeepay.framework.service.workOrder.WorkFlowNodeService;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 16:23
 */
@Service
public class WorkFlowNodeServiceImpl implements WorkFlowNodeService {

    @Resource
    private WorkFlowNodeDao workFlowNodeDao;

    @Resource
    private SeqService seqService;

    private Logger log = LoggerFactory.getLogger(WorkFlowNodeServiceImpl.class);


    @Override
    public void saveWorkFlowNode(List<WorkFlowNode> nodeList, String userName,Long workTypeId) {
        if(nodeList==null || nodeList.size()<=0){
            return;
        }
        int level = 1;
        //先保存父节点
        WorkFlowNode parentNode = nodeList.get(0);
        parentNode.setParentFlowNo("0");
        parentNode.setWorkTypeId(workTypeId);
        parentNode.setLevel(level);
        parentNode.setOrderNo("0");
        parentNode.setOperator(userName);
        parentNode.setFlowNo(seqService.createKey("work_flow_no"));
        parentNode.setFlowNode("0-"+parentNode.getFlowNo());
        workFlowNodeDao.insert(parentNode);

        //是否还存在剩余子节点
        if(nodeList.size()>1){
            for (int i = 1; i < nodeList.size(); i++) {
                level += 1;
                WorkFlowNode childNode = nodeList.get(i);
                childNode.setWorkTypeId(workTypeId);
                childNode.setLevel(level);
                childNode.setOrderNo("0");
                childNode.setOperator(userName);
                childNode.setFlowNo(seqService.createKey("work_flow_no"));
                childNode.setParentFlowNo(parentNode.getFlowNo());
                childNode.setFlowNode(parentNode.getFlowNode()+"-"+childNode.getFlowNo());
                workFlowNodeDao.insert(childNode);
                parentNode = childNode;
            }
        }
    }

    /**
     *  新增工单的时候保存节点副本
     * @param nodeList
     * @param orderNo
     */
    @Override
    public void saveWorkFlowNode(List<WorkFlowNode> nodeList, String orderNo,String userName) {
        if(nodeList==null || nodeList.size()<=0 || StringUtil.isBlank(orderNo)){
            return;
        }
        //先保存父节点
        WorkFlowNode parentNode = nodeList.get(0);
        parentNode.setId(null);
        parentNode.setOrderNo(orderNo);
        parentNode.setCurrentStatus(0);
        parentNode.setOperator(userName);
        Long insert = workFlowNodeDao.insert(parentNode);
        if(insert <= 0){
            throw new WorkOrderException("保存父节点失败");
        }

        //是否还存在剩余子节点
        if(nodeList.size()>1){
            for (int i = 1; i < nodeList.size(); i++) {
                WorkFlowNode childNode = nodeList.get(i);
                childNode.setId(null);
                childNode.setOrderNo(orderNo);
                childNode.setOperator(userName);
                log.info("保存工单节点 flowNo:{},orderNo:{}",childNode.getFlowNo(),orderNo);
                Long insert1 = workFlowNodeDao.insert(childNode);
                if(insert1 <= 0){
                    throw new WorkOrderException("保存子节点失败");
                }
            }
        }
    }

    @Override
    public List<WorkFlowNode> getNodesByWorkTypeID(Long workTypeId) {
        return workFlowNodeDao.getNodesByWorkTypeID(workTypeId);
    }

    @Override
    public List<WorkFlowNode> getNodesByOrderNo(String orderNo) {
        return workFlowNodeDao.getNodesByOrderNo(orderNo);
    }

    @Override
    public WorkFlowNode getNodeByParentFlowNo(String currentFlowNo, String orderNo) {
        return workFlowNodeDao.getNodeByParentFlowNo(currentFlowNo,orderNo);
    }

    @Override
    public WorkFlowNode getNodeByFlowNo(String currentFlowNo, String orderNo) {
        System.out.println("currentFlowNo = " + currentFlowNo);
        System.out.println("orderNo = " + orderNo);
        return workFlowNodeDao.getNodeByFlowNo(currentFlowNo,orderNo);
    }

    @Override
    public List<WorkFlowNode> getNodesByFlowNos(String flowNos, String orderNo) {
        return workFlowNodeDao.getNodesByFlowNos(flowNos,orderNo);
    }

    @Override
    public void updateStatus(String orderNo,String flowNo,Integer status) {
        workFlowNodeDao.updateStatus(flowNo,orderNo,status);

    }

    @Override
    public int update(WorkFlowNode info) {
        return workFlowNodeDao.update(info);
    }


    @Override
    public WorkFlowNode getNodeByOrderNoAndDeptNo(String orderNo, Integer deptId) {
        return workFlowNodeDao.getNodeByOrderNoAndDeptNo(orderNo,deptId);
    }
}
