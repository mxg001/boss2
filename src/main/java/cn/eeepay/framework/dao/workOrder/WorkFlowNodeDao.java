package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.model.workOrder.WorkFlowNode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/27 10:46
 */
public interface WorkFlowNodeDao {


    @Insert("INSERT INTO `work_flow_node`( `flow_no`, `parent_flow_no`, `flow_node`, `level`, `work_type_id`, `dept_no`, `flow_desc`, `end_reply_days`, " +
            "`order_no`, `create_time`, `operator`, `current_status`) " +
            "VALUES ( #{info.flowNo}, #{info.parentFlowNo}, #{info.flowNode}, #{info.level}, #{info.workTypeId}, #{info.deptNo}, " +
            "#{info.flowDesc}, #{info.endReplyDays}, #{info.orderNo}, now(), #{info.operator}, #{info.currentStatus})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Long.class)
    Long insert(@Param("info") WorkFlowNode info);




    @Delete("delete from work_flow_node where work_type_id=#{id} and order_no='0'")
    @ResultType(Integer.class)
    int delByWorkTypeId(@Param("id")Long id);


    @Select("select wfn.*,sd.sys_name deptName from work_flow_node wfn " +
            " left join sys_dict sd on wfn.dept_no=sd.sys_value and sd.sys_key='DEPT_LIST'" +
            " where wfn.work_type_id=#{id} and wfn.order_no='0' order by level asc")
    @ResultType(List.class)
    List<WorkFlowNode> getNodesByWorkTypeID(@Param("id")Long id);


    @Select("select * from work_flow_node where parent_flow_no = #{parentFlowNo} and order_no = #{orderNo}")
    @ResultType(WorkFlowNode.class)
    WorkFlowNode getNodeByParentFlowNo(@Param("parentFlowNo") String parentFlowNo,@Param("orderNo") String orderNo);

    @Select("select wfn.*,sd.sys_name deptName from work_flow_node wfn " +
            " left join sys_dict sd on wfn.dept_no=sd.sys_value and sd.sys_key='DEPT_LIST'" +
            " where wfn.order_no=#{orderNo} order by level asc")
    @ResultType(List.class)
    List<WorkFlowNode> getNodesByOrderNo(@Param("orderNo") String orderNo);

    @Select("select * from work_flow_node where flow_no = #{flowNo} and order_no = #{orderNo}")
    @ResultType(WorkFlowNode.class)
    WorkFlowNode getNodeByFlowNo(@Param("flowNo") String flowNo,@Param("orderNo") String orderNo);


    @Select("select wfn.*,sd.sys_name deptName from work_flow_node wfn " +
            " left join sys_dict sd on wfn.dept_no=sd.sys_value and sd.sys_key='DEPT_LIST'" +
            " where   flow_no in (${flowNoStr}) and wfn.order_no=#{orderNo}order by level asc")
    @ResultType(List.class)
    List<WorkFlowNode> getNodesByFlowNos(@Param("flowNoStr")String flowNoStr,@Param("orderNo") String orderNo);

    @Update("UPDATE `work_flow_node`" +
            " SET `flow_no` =#{info.flowNo}," +
            " `parent_flow_no` = #{info.parentFlowNo}," +
            " `flow_node` = #{info.flowNode}," +
            " `level` = #{info.level}," +
            " `work_type_id` = #{info.workTypeId}," +
            " `dept_no` = #{info.deptNo}," +
            " `flow_desc` = #{info.flowDesc}," +
            " `end_reply_days` = #{info.endReplyDays}," +
            " `end_reply_time` = #{info.endReplyTime}," +
            " `order_no` = #{info.orderNo}," +
            " `current_user_id` = #{info.currentUserId}," +
            " `current_status` = #{info.currentStatus}" +
            " WHERE `id` = #{info.id}")
    int update(@Param("info") WorkFlowNode info);

    @Update("update work_flow_node set current_status=#{currentStatus} where flow_no = #{flowNo} and order_no=#{orderNo}")
    @ResultType(Integer.class)
    int updateStatus(@Param("flowNo") String flowNo, @Param("orderNo") String orderNo,@Param("currentStatus")Integer currentStatus);

    @Select(" select t1.* from ( " +
            " select w1.* from work_flow_node w1 " +
            " where w1.order_no=#{orderNo} and w1.dept_no=#{deptNo})t1 " +
            " inner join " +
            " (select max(level),id from work_flow_node w2 where w2.order_no=#{orderNo} and w2.dept_no=#{deptNo} )t2 " +
            " on t1.id = t2.id  " +
            " limit 1 ")
    WorkFlowNode getNodeByOrderNoAndDeptNo(@Param("orderNo") String orderNo, @Param("deptNo") Integer deptNo);

    @Update("UPDATE `work_flow_node`" +
            " SET `flow_no` =#{info.flowNo}," +
            " `parent_flow_no` = #{info.parentFlowNo}," +
            " `flow_node` = #{info.flowNode}," +
            " `level` = #{info.level}," +
            " `work_type_id` = #{info.workTypeId}," +
            " `dept_no` = #{info.deptNo}," +
            " `flow_desc` = #{info.flowDesc}," +
            " `end_reply_days` = #{info.endReplyDays}," +
            " `end_reply_time` = #{info.endReplyTime}," +
            " `order_no` = #{info.orderNo}," +
            " `current_user_id` = #{info.currentUserId}," +
            " `current_status` = #{info.currentStatus}" +
            " WHERE `id` = #{info.id} and current_status = 0")
    int reply(@Param("info") WorkFlowNode currNode);
}
