package cn.eeepay.framework.service.workOrder;


import cn.eeepay.framework.model.workOrder.WorkRemarkRecord;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 15:55
 */
public interface WorkRemarkRecordService {

    Long insert(WorkRemarkRecord info);
    List<WorkRemarkRecord> getRemarks(int belongType,Long belongId);

}
