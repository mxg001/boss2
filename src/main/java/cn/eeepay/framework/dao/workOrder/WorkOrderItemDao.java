package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.model.workOrder.WorkOrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/29 14:44
 */
public interface WorkOrderItemDao {


    @Insert("INSERT INTO `work_order_item`(`order_no`, `flow_no`, `reject_status`, `transfer_status`, `sender_id`, " +
                   " `sender_dept_no`, `receiver_id`, `receiver_dept_no`, `receiver_flow_no`, `reply_content`," +
                   "  `reject_reason`, `create_time`) " +
                   "VALUES " +
                   "(#{info.orderNo}, #{info.flowNo}, #{info.rejectStatus}, #{info.transferStatus}, #{info.senderId}," +
                   "  #{info.senderDeptNo}, #{info.receiverId}, #{info.receiverDeptNo}, #{info.receiverFlowNo}, " +
                   "#{info.replyContent},  #{info.rejectReason}, now() )")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Long.class)
    Long insert(@Param("info") WorkOrderItem info);


    @Select("select woi.*," +
            "bsu.real_name sender_name," +
            "sd1.sys_name receiverDeptName," +
            "sd.sys_name senderDeptName from work_order_item woi " +
            " left join boss_shiro_user  bsu on bsu.id = woi.sender_id " +
            " left join sys_dict  sd on sd.sys_key = 'DEPT_LIST' and sd.sys_value=woi.sender_dept_no " +
            " left join sys_dict  sd1 on sd1.sys_key = 'DEPT_LIST' and sd1.sys_value=woi.receiver_dept_no " +
            " where woi.order_no=#{orderNo} and flow_no=#{flowNo}  order by create_time desc limit 1")
    @ResultType(WorkOrderItem.class)
    WorkOrderItem getItemByOrderNoAndFlowNo(@Param("orderNo") String orderNo, @Param("flowNo") String flowNo);



    @Select("select woi.*," +
            "bsu.real_name sender_name," +
            "sd1.sys_name receiverDeptName," +
            "sd.sys_name senderDeptName from work_order_item woi " +
            " left join boss_shiro_user  bsu on bsu.id = woi.sender_id " +
            " left join sys_dict  sd on sd.sys_key = 'DEPT_LIST' and sd.sys_value=woi.sender_dept_no " +
            " left join sys_dict  sd1 on sd1.sys_key = 'DEPT_LIST' and sd1.sys_value=woi.receiver_dept_no " +
            " where woi.order_no=#{orderNo}")
    @ResultType(List.class)
    List<WorkOrderItem> getItemsByOrderNo(@Param("orderNo") String orderNo);

    @Select("select * from work_order_item where order_no=#{orderNo} and sender_dept_no = #{senderDeptNo} order by create_time desc limit 1")
    @ResultType(WorkOrderItem.class)
    WorkOrderItem getLastestItemByOrderNo(@Param("orderNo") String orderNo,@Param("senderDeptNo")Long senderDeptNo);

    @Select("select *from work_order_item where order_no=#{orderNo} and sender_dept_no=#{deptNo} and sender_id = #{userId} order by create_time desc limit 1")
    @ResultType(WorkOrderItem.class)
    WorkOrderItem getLastestItem(@Param("orderNo") String orderNo,@Param("deptNo")Integer deptNo,@Param("userId") Integer userId);
}
