package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.workOrder.WorkType;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/26 10:29
 */
public interface WorkTypeService {
    void insert(WorkType info);
    void update(WorkType info);
    List<WorkType> getAll();
    List<WorkType> query(Page<WorkType> page,WorkType info);
    void del(Long id);
    WorkType getWorkTypeById( Long id);

    List<WorkType> getMyDutyType();

    List<Map<String, String>> getAllWorkTypes();
}
