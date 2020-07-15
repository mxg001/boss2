package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.WorkFlowNode;
import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 10:32
 */
public interface WorkOrderService {
    int del(Long id);
    void insert( WorkOrder info);
    void update(WorkOrder info);
    List<WorkOrder> query(Page<WorkOrder> page, WorkOrder info);
    WorkOrder getWorkOrderById(Long id);
    List<UserInfo> getCurrentDeptUser();

    WorkOrder getWorkOrderByOrderNo(String orderNo);

    WorkOrder getWorkOrderToRejectById(Long id);

    WorkOrder getWorkOrderDetailById(Long id);

    void close(Long id);

    int getToDo();

    WorkOrder getWorkOrderToExport(Long id);

    void export(WorkOrder order, HttpServletResponse response, HttpServletRequest request);

    /***
     * 是否显示代理商可见
     * @param id
     * @return
     */
    boolean showAgentShow(Long id);


    boolean replyStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo, WorkOrderUser workUser, WorkFlowNode node);

    boolean closeStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser);

    boolean rejectStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser,WorkFlowNode node);

    boolean remarkStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser,WorkFlowNode node);

    boolean detailStatusCheck(WorkOrder order, UserLoginInfo userLoginInfo,WorkOrderUser workUser);


    Boolean getCurrentWorkUser();
}
