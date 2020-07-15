package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.model.workOrder.WorkOrderItem;
import cn.eeepay.framework.model.workOrder.WorkRemarkRecord;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/29 14:50
 */
public interface WorkOrderItemService {
    void reply(WorkOrderItem info);
    void reject(WorkOrderItem info);
    Map<String,Object> transfer(String[] orderNoArr, Integer receiverId);

    void remark(WorkRemarkRecord info);

    List<WorkOrderItem> getItemsByOrderNo(String orderNo);

    List<WorkOrderItem> getItemsToExportByOrderNo(WorkOrder order);

    WorkOrderItem getLastestItem(String orderNo,Integer deptNo,Integer userId);
}
