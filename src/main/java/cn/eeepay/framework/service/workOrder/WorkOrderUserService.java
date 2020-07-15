package cn.eeepay.framework.service.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 11:39
 */
public interface WorkOrderUserService {

    int insert( WorkOrderUser info);

    int update(WorkOrderUser info);

    int updateStatusById(WorkOrderUser info);

    int del(Long id);

    void query(Page<WorkOrderUser> page, WorkOrderUser info);

    int updateStatus(WorkOrderUser info);

    List getDutyData(Integer dutyType);

    List<UserInfo> getAllUsers();

    WorkOrderUser getWorkUserById(Long id);

    WorkOrderUser getWorkUserByBossUserName(String bossUserName);


    List<WorkOrderUser> getCurrentDeptUser();


    List<String> getdutyAgent(WorkOrderUser workUser);

    boolean auth(WorkOrderUser workUser, WorkOrder order);
}
