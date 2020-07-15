package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.workOrder.WorkOrder;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;
import cn.eeepay.framework.util.StringUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/28 10:15
 */
public interface WorkOrderDao {

    @Insert("INSERT INTO `work_order`( `order_no`, `work_type_id`, `work_type_name`,`status`, `create_type`, `create_user_id`, `create_dept_no`," +
            " `current_flow_no`,  `deal_process`, `receive_agent_node`, `reply_type`, `agent_reply_status`, `urge_status`, `read_status`, `work_content`," +
            " `end_reply_days`, `end_reply_time`, `create_time`,`agent_show`,`current_dept_no`,`current_user_id`) " +
            "VALUES " +
            "(#{info.orderNo}, #{info.workTypeId},#{info.workTypeName}, #{info.status}, #{info.createType}, #{info.createUserId}, #{info.createDeptNo}, " +
            " #{info.currentFlowNo},  #{info.dealProcess}, " +
            "#{info.receiveAgentNode}, #{info.replyType}, #{info.agentReplyStatus}, #{info.urgeStatus}, #{info.readStatus}, #{info.workContent}, " +
            "#{info.endReplyDays}, #{info.endReplyTime}, now(),#{info.agentShow},#{info.currentDeptNo},#{info.currentUserId})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Long.class)
    Long insert(@Param("info") WorkOrder info);



    @Select("select wr.*,ai2.agent_no oneAgentNo," +
            " ai2.agent_name oneAgentName," +
            " ai.agent_name receiver_agent_name from work_order wr " +
            " left join agent_info ai on ai.agent_node = wr.receive_agent_node" +
            " left join agent_info ai2 on ai2.agent_no = ai.one_level_id" +
            " where wr.id = #{id} ")
    @ResultType(WorkOrder.class)
    WorkOrder getWorkOrderById(@Param("id")Long id);





    @Update("UPDATE `work_order` SET " +
            " `work_type_id` = #{info.workTypeId}," +
            " `status` = #{info.status}, " +
            "`create_type` =  #{info.createType}, " +
            "`create_user_id` = #{info.createUserId}," +
            " `create_dept_no` = #{info.createDeptNo},"  +
            " `current_flow_no` = #{info.currentFlowNo}, " +
            " `current_dept_no` = #{info.currentDeptNo}, " +
            " `current_user_id` = #{info.currentUserId}, " +
            "`deal_process` = #{info.dealProcess}, " +
            "`receive_agent_node` = #{info.receiveAgentNode}," +
            " `reply_type` =  #{info.replyType}, " +
            "`agent_reply_status` =#{info.agentReplyStatus}, " +
            " `urge_status` = #{info.urgeStatus}," +
            " `read_status` = #{info.readStatus}, " +
            "`work_content` = #{info.workContent}," +
            " `end_reply_days` = #{info.endReplyDays}, " +
            "`end_reply_time` = #{info.endReplyTime} " +
            " WHERE `id` = #{info.id} ")
    int update(@Param("info") WorkOrder info);

    @Delete("delete from work_order where id=#{id}")
    int del(@Param("id") Long id);


    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "query")
    @ResultType(List.class)
    List<WorkOrder> query(@Param("page") Page<WorkOrder> page, @Param("info") WorkOrder info,@Param("workUser") WorkOrderUser workUser);


    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMySelf")
    @ResultType(List.class)
    List<WorkOrder> queryMySelf(@Param("page") Page<WorkOrder> page, @Param("info") WorkOrder info,@Param("workUser") WorkOrderUser workUser);


    @Select("select * from work_order where order_no = #{orderNo} ")
    @ResultType(WorkOrder.class)
    WorkOrder getWorkOrderByOrderNo(@Param("orderNo") String orderNo);


    @Update("update work_order set status=4,agent_reply_status=5 where id = #{id}")
    @ResultType(Integer.class)
    int close(@Param("id") Long id);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "getToDo")
    @ResultType(Integer.class)
    int getToDo(@Param("deptId") Integer deptId, @Param("workUser") WorkOrderUser workUser);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMySelf")
    @ResultType(List.class)
    List<WorkOrder> exportMySelf(@Param("info") WorkOrder order, @Param("workUser") WorkOrderUser workUser);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "query")
    @ResultType(List.class)
    List<WorkOrder> export(@Param("info") WorkOrder order, @Param("workUser") WorkOrderUser workUser);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMyAgent")
    @ResultType(List.class)
    List<WorkOrder> queryMyAgent(@Param("page") Page<WorkOrder> page, @Param("info") WorkOrder info, @Param("agentNodes") List<String>  agentNodes);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMyAgent")
    @ResultType(List.class)
    List<WorkOrder> exportMyAgent(@Param("info") WorkOrder info, @Param("agentNodes") List<String>  agentNodes);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMyAgent")
    @ResultType(List.class)
    WorkOrder queryMyAgentByOrderNo(@Param("info") WorkOrder info, @Param("agentNodes") List<String>  agentNodes);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMyDept")
    @ResultType(List.class)
    List<WorkOrder> queryMyDept(@Param("page") Page<WorkOrder> page, @Param("info") WorkOrder info, @Param("workUser") WorkOrderUser workUser);

    @SelectProvider(type = WorkOrderDao.SqlProvider.class, method = "queryMyDept")
    @ResultType(List.class)
    List<WorkOrder> exportMyDept(@Param("info") WorkOrder info, @Param("workUser") WorkOrderUser workUser);

    class SqlProvider {
        public String getToDo(Map<String, Object> param) {
            final Integer deptId = (Integer) param.get("deptId");
            final WorkOrderUser workUser = (WorkOrderUser) param.get("workUser");
            return new SQL() {
                {

                    SELECT("count(1) ");
                    FROM("work_flow_node wfn");
                    JOIN("work_order wr on wr.order_no = wfn.order_no and wr.status = 1");
                    WHERE("wfn.order_no <>'0'  ");
                    WHERE("wfn.dept_no=#{deptId}");
                    WHERE("wfn.current_status = 0");
                    //权限过滤 管理员和负责销售类型 可以查看全部类型的工单 否则只能查询配置的工单
                    if("1".equals(workUser.getRoleType())){
                        if(workUser.getDutyType().intValue()==2){
                            String[] dutys = workUser.getDutyData().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String duty : dutys) {
                                sb.append(duty).append(",");
                            }
                            sb.setLength(sb.length()-1);
                            WHERE("wr.work_type_id in("+sb.toString()+")");
                        }
                    }
                    ORDER_BY("wr.create_time desc");

                }
            }.toString();
        }
        public String queryMyAgent(Map<String, Object> param) {
            final WorkOrder info = (WorkOrder) param.get("info");
            final List<String> agentNodes = (List) param.get("agentNodes");

            StringBuilder sb = new StringBuilder();
            sb.append(" select wfn.current_status,wfn.current_user_id,wfn.flow_no current_flow_no, ");
            sb.append(" wr.id,wr.order_no,wr.work_type_id,wr.work_type_name,wr.status,wr.create_type ");
            sb.append(" ,wr.create_user_id,wr.create_dept_no,wr.deal_process,wr.receive_agent_node,wr.reply_type, ");
            sb.append(" wr.agent_reply_status,wr.urge_status,wr.read_status,wr.work_content,wr.end_reply_days, ");
            sb.append(" wr.end_reply_time,wr.create_time,wr.last_update_time,wr.agent_show,wr.over_due_reply ");
            sb.append(" ,sd.sys_name currentDeptName,bsu1.real_name currentUserName,ai.agent_no,ai.agent_name,ai2.agent_no oneAgentNo,ai2.agent_name oneAgentName ");
            sb.append(" from work_order wr");
            sb.append(" left join work_flow_node wfn on wr.order_no = wfn.order_no and wr.current_flow_no = wfn.flow_no ");
            sb.append(" left join boss_shiro_user bsu on bsu.id = wr.create_user_id ");
            sb.append(" left join boss_shiro_user bsu1 on bsu1.id = wfn.current_user_id ");
            sb.append(" left join sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=wfn.dept_no ");
            sb.append(" left join agent_info ai on ai.agent_node = wr.receive_agent_node ");
            sb.append(" left join agent_info ai2  on ai2.agent_no = ai.one_level_id ");
            sb.append(" left join agent_info ai3  on ai3.agent_no = wr.create_user_id ");

            sb.append(" where wr.order_no like 'A%'  ");
            if(agentNodes!=null && agentNodes.size()>0){
                sb.append(" and ( ");
                for (String agentNode : agentNodes) {
                    sb.append(" wr.receive_agent_node like concat('").append(agentNode).append("','%') or");
                }
                sb.setLength(sb.length()-2);
                sb.append(" ) ");
            }
            if(StringUtil.isNotBlank(info.getOneAgentNode())){
                sb.append(" and ai2.agent_node = #{info.oneAgentNode}");
            }
            if(StringUtil.isNotBlank(info.getCurrentDeptNo())){
                sb.append(" and wr.current_dept_no = #{info.currentDeptNo}");
            }
            if(StringUtil.isNotBlank(info.getStatus())){
                sb.append(" and wr.status=#{info.status}");
            }
            if(info.getOverDueReply()!=null){
                sb.append(" and wr.over_due_reply = #{info.overDueReply}");
            }
            if(info.getCreateUserId()!=null){
                sb.append(" and wr.create_user_id=#{info.createUserId}  and wr.create_dept_no =#{info.createDeptNo}");
            }
            if(StringUtil.isNotBlank(info.getOrderNo())){
                sb.append(" and wr.order_no=#{info.orderNo}");
            }
            if(StringUtil.isNotBlank(info.getWorkTypeId())){
                sb.append(" and wr.work_type_id=#{info.workTypeId}");
            }
            if(StringUtil.isNotBlank(info.getCreateType())){
                sb.append(" and wr.create_type=#{info.createType}");
            }
            if(StringUtil.isNotBlank(info.getCreateTimeBegin())){
                sb.append(" and wr.create_time > #{info.createTimeBegin}");
            }
            if(StringUtil.isNotBlank(info.getCreateTimeEnd())){
                sb.append(" and wr.create_time < #{info.createTimeEnd}");
            }
            if(StringUtil.isNotBlank(info.getLastUpdateTimeBegin())){
                sb.append(" and wr.last_update_time > #{info.lastUpdateTimeBegin}");
            }
            if(StringUtil.isNotBlank(info.getLastUpdateTimeEnd())){
                sb.append(" and wr.last_update_time < #{info.lastUpdateTimeEnd}");
            }
            if(StringUtil.isNotBlank(info.getEndReplyTimeBegin())){
                sb.append(" and wr.end_reply_time > #{info.endReplyTimeBegin}");
            }
            if(StringUtil.isNotBlank(info.getEndReplyTimeEnd())){
                sb.append(" and wr.end_reply_time< #{info.endReplyTimeEnd}");
            }
            if(info.getOverDueReply()!=null){
                sb.append(" and wr.over_due_reply = #{info.overDueReply}");
            }
            sb.append(" order by wr.create_time desc");
            return sb.toString();
        }

        public String queryMyDept(Map<String, Object> param) {
            final WorkOrder info = (WorkOrder) param.get("info");
            final WorkOrderUser workUser = (WorkOrderUser) param.get("workUser");
            return new SQL() {
                {
                    SELECT("wfn.current_status,wfn.current_user_id ");

                    SELECT("wr.current_flow_no,wr.id,wr.order_no,wr.work_type_id,wr.work_type_name,wr.status,wr.create_type" +
                            ",wr.create_user_id,wr.create_dept_no,wr.deal_process,wr.receive_agent_node,wr.reply_type," +
                            "wr.agent_reply_status,wr.urge_status,wr.read_status,wr.work_content,wr.end_reply_days," +
                            "wr.end_reply_time,wr.create_time,wr.last_update_time,wr.agent_show,wr.over_due_reply");

                    SELECT("sd.sys_name currentDeptName," +
                            "bsu1.real_name currentUserName,ai.agent_no,ai.agent_name,ai2.agent_no oneAgentNo,ai2.agent_name oneAgentName");

                    FROM("work_order wr");
                    LEFT_OUTER_JOIN(" work_flow_node wfn on wr.order_no = wfn.order_no and wr.current_flow_no = wfn.flow_no ");
                    LEFT_OUTER_JOIN("boss_shiro_user bsu on bsu.id = wr.create_user_id");
                    LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id = wfn.current_user_id");
                    LEFT_OUTER_JOIN("sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=wfn.dept_no");
                    LEFT_OUTER_JOIN("agent_info ai on ai.agent_node = wr.receive_agent_node");
                    LEFT_OUTER_JOIN("agent_info ai2  on ai2.agent_no = ai.one_level_id");
                    LEFT_OUTER_JOIN("agent_info ai3  on ai3.agent_no = wr.create_user_id");


                    WHERE("wr.create_dept_no=#{info.createDeptNo} ");
                    if(StringUtil.isNotBlank(info.getCreateUserName())){
                        WHERE("  (bsu.real_name like CONCAT('%',#{info.createUserName},'%') or ai3.agent_name like CONCAT('%',#{info.createUserName},'%') )");
                    }

                    if(StringUtil.isNotBlank(info.getCurrentDeptNo())){
                        WHERE("  wr.current_dept_no = #{info.currentDeptNo}");
                    }
                    if(StringUtil.isNotBlank(info.getOneAgentNode())){
                        WHERE(" ai2.agent_node = #{info.oneAgentNode}");
                    }
                    if(StringUtil.isNotBlank(info.getStatus())){
                        WHERE("  wr.status=#{info.status}");
                    }
                    if(info.getOverDueReply()!=null){
                        WHERE("wr.over_due_reply = #{info.overDueReply}");
                    }
                    if(StringUtil.isNotBlank(info.getOrderNo())){
                        WHERE("  wr.order_no=#{info.orderNo}");
                    }
                    if(StringUtil.isNotBlank(info.getWorkTypeId())){
                        WHERE("  wr.work_type_id=#{info.workTypeId}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateType())){
                        WHERE("  wr.create_type=#{info.createType}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeBegin())){
                        WHERE("  wr.create_time > #{info.createTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeEnd())){
                        WHERE("  wr.create_time < #{info.createTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeBegin())){
                        WHERE("  wr.last_update_time > #{info.lastUpdateTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeEnd())){
                        WHERE("  wr.last_update_time < #{info.lastUpdateTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeBegin())){
                        WHERE("  wr.end_reply_time > #{info.endReplyTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeEnd())){
                        WHERE("  wr.end_reply_time< #{info.endReplyTimeEnd}");
                    }
                    if(info.getOverDueReply()!=null){
                        WHERE("  wr.over_due_reply = #{info.overDueReply}");
                    }
                    ORDER_BY("wr.create_time desc");
                }
            }.toString();
        }


        public String queryMySelf(Map<String, Object> param) {
            final WorkOrder info = (WorkOrder) param.get("info");
            final WorkOrderUser workUser = (WorkOrderUser) param.get("workUser");
            return new SQL() {
                {
                    SELECT("wfn.current_status,wfn.current_user_id ");

                    SELECT("wr.current_flow_no,wr.id,wr.order_no,wr.work_type_id,wr.work_type_name,wr.status,wr.create_type" +
                            ",wr.create_user_id,wr.create_dept_no,wr.deal_process,wr.receive_agent_node,wr.reply_type," +
                            "wr.agent_reply_status,wr.urge_status,wr.read_status,wr.work_content,wr.end_reply_days," +
                            "wr.end_reply_time,wr.create_time,wr.last_update_time,wr.agent_show,wr.over_due_reply");

                    SELECT("sd.sys_name currentDeptName," +
                            "bsu1.real_name currentUserName,ai.agent_no,ai.agent_name,ai2.agent_no oneAgentNo,ai2.agent_name oneAgentName");

                    FROM("work_order wr");
                    LEFT_OUTER_JOIN(" work_flow_node wfn on wr.order_no = wfn.order_no and wr.current_flow_no = wfn.flow_no ");
                    LEFT_OUTER_JOIN("boss_shiro_user bsu on bsu.id = wr.create_user_id");
                    LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id = wfn.current_user_id");
                    LEFT_OUTER_JOIN("sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=wfn.dept_no");
                    LEFT_OUTER_JOIN("agent_info ai on ai.agent_node = wr.receive_agent_node");
                    LEFT_OUTER_JOIN("agent_info ai2  on ai2.agent_no = ai.one_level_id");
                    LEFT_OUTER_JOIN("agent_info ai3  on ai3.agent_no = wr.create_user_id");
                    //权限过滤 管理员和负责销售类型 可以查看全部类型的工单 否则只能查询配置的工单
                    if("1".equals(workUser.getRoleType())){
                        if(workUser.getDutyType().intValue()==2){
                            String[] dutys = workUser.getDutyData().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String duty : dutys) {
                                sb.append(duty).append(",");
                            }
                            sb.setLength(sb.length()-1);
                            WHERE("wr.work_type_id in("+sb.toString()+")");
                        }
                    }

                    WHERE("wr.create_user_id=#{info.createUserId} ");
                    WHERE("wr.create_dept_no=#{info.createDeptNo} ");
                    if(StringUtil.isNotBlank(info.getCurrentDeptNo())){
                        WHERE("  wr.current_dept_no = #{info.currentDeptNo}");
                    }
                    if(StringUtil.isNotBlank(info.getOneAgentNode())){
                        WHERE(" ai2.agent_node = #{info.oneAgentNode}");
                    }
                    if(StringUtil.isNotBlank(info.getStatus())){
                        WHERE("  wr.status=#{info.status}");
                    }
                    if(info.getOverDueReply()!=null){
                        WHERE("wr.over_due_reply = #{info.overDueReply}");
                    }
                    if(StringUtil.isNotBlank(info.getOrderNo())){
                        WHERE("  wr.order_no=#{info.orderNo}");
                    }
                    if(StringUtil.isNotBlank(info.getWorkTypeId())){
                        WHERE("  wr.work_type_id=#{info.workTypeId}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateType())){
                        WHERE("  wr.create_type=#{info.createType}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeBegin())){
                        WHERE("  wr.create_time > #{info.createTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeEnd())){
                        WHERE("  wr.create_time < #{info.createTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeBegin())){
                        WHERE("  wr.last_update_time > #{info.lastUpdateTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeEnd())){
                        WHERE("  wr.last_update_time < #{info.lastUpdateTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeBegin())){
                        WHERE("  wr.end_reply_time > #{info.endReplyTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeEnd())){
                        WHERE("  wr.end_reply_time< #{info.endReplyTimeEnd}");
                    }
                    if(info.getOverDueReply()!=null){
                        WHERE("  wr.over_due_reply = #{info.overDueReply}");
                    }
                    ORDER_BY("wr.create_time desc");

                }
            }.toString();
        }



        public String query(Map<String, Object> param) {
            final WorkOrder info = (WorkOrder) param.get("info");
            final WorkOrderUser workUser = (WorkOrderUser) param.get("workUser");

                    StringBuilder sql = new StringBuilder();

                    sql.append("select t1.* from (");

                    sql.append(" select ");
                    sql.append(" wfn.level,wfn.current_status,wfn.current_user_id,wfn.flow_no current_flow_no,");

                    sql.append(" wr.id,wr.order_no,wr.work_type_id,wr.work_type_name,wr.status,wr.create_type" +
                            ",wr.create_user_id,wr.create_dept_no,wr.deal_process,wr.receive_agent_node,wr.reply_type," +
                            "wr.agent_reply_status,wr.urge_status,wr.read_status,wr.work_content,wr.end_reply_days," +
                            "wr.end_reply_time,wr.create_time,wr.last_update_time,wr.agent_show,wr.over_due_reply,");

                    sql.append(" sd.sys_name currentDeptName," +
                            "bsu1.real_name currentUserName,ai.agent_no,ai.agent_name,ai2.agent_no oneAgentNo,ai2.agent_name oneAgentName");

                    sql.append(" from work_flow_node wfn");
                    sql.append(" join work_order wr on wr.order_no = wfn.order_no");
                    sql.append(" left join boss_shiro_user bsu on bsu.id = wr.create_user_id");
                    sql.append(" left join boss_shiro_user bsu1 on bsu1.id = wfn.current_user_id");
                    sql.append(" left join sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=wfn.dept_no");
                    sql.append(" left join agent_info ai on ai.agent_node = wr.receive_agent_node");
                    sql.append(" left join agent_info ai2  on ai2.agent_no = ai.one_level_id");
                    sql.append(" left join agent_info ai3  on ai3.agent_no = wr.create_user_id");

                    sql.append(" where wfn.dept_no=#{info.deptNo}");

                    if(StringUtil.isNotBlank(info.getCurrentDeptNo())){
                        sql.append(" and wr.current_dept_no = #{info.currentDeptNo}");
                    }

                    if(StringUtil.isNotBlank(info.getOneAgentNode())){
                        sql.append(" and ai2.agent_node = #{info.oneAgentNode}");
                    }

                    if(StringUtil.isNotBlank(info.getQueryType())){
                        if(info.getQueryType()==2){
                            sql.append(" and wfn.current_status in (2,3)");
                        }else{
                            sql.append(" and wfn.current_status=#{info.currentStatus} ");
                        }
                    }
                    if(StringUtil.isNotBlank(info.getStatus())){
                        sql.append(" and wr.status=#{info.status}");
                    }
                    if(info.getCreateUserId()!=null){
                        sql.append(" and wr.create_user_id=#{info.createUserId}");
                    }
                    //权限过滤 管理员和负责销售类型 可以查看全部类型的工单 否则只能查询配置的工单
                    if("1".equals(workUser.getRoleType())){
                        if(workUser.getDutyType().intValue()==2){
                            String[] dutys = workUser.getDutyData().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String duty : dutys) {
                                sb.append(duty).append(",");
                            }
                            sb.setLength(sb.length()-1);
                            sql.append(" and wr.work_type_id in("+sb.toString()+")");
                        }
                    }

                    if(StringUtil.isNotBlank(info.getOrderNo())){
                        sql.append(" and wr.order_no=#{info.orderNo}");
                    }
                    if(StringUtil.isNotBlank(info.getWorkTypeId())){
                        sql.append(" and wr.work_type_id=#{info.workTypeId}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateUserName())){
                        sql.append(" and (bsu.real_name like CONCAT('%',#{info.createUserName},'%') or ai3.agent_name like CONCAT('%',#{info.createUserName},'%') )");
                    }
                    if(StringUtil.isNotBlank(info.getCreateType())){
                        sql.append(" and wr.create_type=#{info.createType}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeBegin())){
                        sql.append(" and wr.create_time > #{info.createTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getCreateTimeEnd())){
                        sql.append(" and wr.create_time < #{info.createTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeBegin())){
                        sql.append(" and wr.last_update_time > #{info.lastUpdateTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getLastUpdateTimeEnd())){
                        sql.append(" and wr.last_update_time < #{info.lastUpdateTimeEnd}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeBegin())){
                        sql.append(" and wr.end_reply_time > #{info.endReplyTimeBegin}");
                    }
                    if(StringUtil.isNotBlank(info.getEndReplyTimeEnd())){
                        sql.append(" and wr.end_reply_time< #{info.endReplyTimeEnd}");
                    }
                    if(info.getOverDueReply()!=null){
                        sql.append(" and wr.over_due_reply = #{info.overDueReply}");
                    }


                    sql.append(" )t1  ");
                    if(info.getQueryType()==2){
                        sql.append(" inner join " );
                        sql.append( " ( select max(wfn.level) level,wfn.order_no from work_flow_node wfn JOIN work_order wr ON wr.order_no = wfn.order_no " );
                        sql.append(" left join boss_shiro_user bsu on bsu.id = wr.create_user_id");
                        sql.append(" left join boss_shiro_user bsu1 on bsu1.id = wfn.current_user_id");
                        sql.append(" left join sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=wfn.dept_no");
                        sql.append(" left join agent_info ai on ai.agent_node = wr.receive_agent_node");
                        sql.append(" left join agent_info ai2  on ai2.agent_no = ai.one_level_id");
                        sql.append(" left join agent_info ai3  on ai3.agent_no = wr.create_user_id");

                        sql.append(" where wfn.current_status  in (2,3) " );

                        sql.append(" and wfn.dept_no=#{info.deptNo}");

                        if(StringUtil.isNotBlank(info.getCurrentDeptNo())){
                            sql.append(" and wr.current_dept_no = #{info.currentDeptNo}");
                        }
                        if(StringUtil.isNotBlank(info.getOneAgentNode())){
                            sql.append(" and ai2.agent_node = #{info.oneAgentNode}");
                        }

                        if(StringUtil.isNotBlank(info.getStatus())){
                            sql.append(" and wr.status=#{info.status}");
                        }
                        if(info.getCreateUserId()!=null){
                            sql.append(" and wr.create_user_id=#{info.createUserId}");
                        }
                        //权限过滤 管理员和负责销售类型 可以查看全部类型的工单 否则只能查询配置的工单
                        if("1".equals(workUser.getRoleType())){
                            if(workUser.getDutyType().intValue()==2){
                                String[] dutys = workUser.getDutyData().split(",");
                                StringBuilder sb = new StringBuilder();
                                for (String duty : dutys) {
                                    sb.append(duty).append(",");
                                }
                                sb.setLength(sb.length()-1);
                                sql.append(" and wr.work_type_id in("+sb.toString()+")");
                            }
                        }

                        if(StringUtil.isNotBlank(info.getOrderNo())){
                            sql.append(" and wr.order_no=#{info.orderNo}");
                        }
                        if(StringUtil.isNotBlank(info.getWorkTypeId())){
                            sql.append(" and wr.work_type_id=#{info.workTypeId}");
                        }
                        if(info.getCurrentUserId()!=null){
                            sql.append(" and wfn.current_user_id=#{info.currentUserId}");
                        }
                        if(StringUtil.isNotBlank(info.getCreateUserName())){
                            sql.append(" and (bsu.real_name like CONCAT('%',#{info.createUserName},'%') or ai3.agent_name like CONCAT('%',#{info.createUserName},'%'))");
                        }
                        if(StringUtil.isNotBlank(info.getCreateType())){
                            sql.append(" and wr.create_type=#{info.createType}");
                        }
                        if(StringUtil.isNotBlank(info.getCreateTimeBegin())){
                            sql.append(" and wr.create_time > #{info.createTimeBegin}");
                        }
                        if(StringUtil.isNotBlank(info.getCreateTimeEnd())){
                            sql.append(" and wr.create_time < #{info.createTimeEnd}");
                        }
                        if(StringUtil.isNotBlank(info.getLastUpdateTimeBegin())){
                            sql.append(" and wr.last_update_time > #{info.lastUpdateTimeBegin}");
                        }
                        if(StringUtil.isNotBlank(info.getLastUpdateTimeEnd())){
                            sql.append(" and wr.last_update_time < #{info.lastUpdateTimeEnd}");
                        }
                        if(StringUtil.isNotBlank(info.getEndReplyTimeBegin())){
                            sql.append(" and wr.end_reply_time > #{info.endReplyTimeBegin}");
                        }
                        if(StringUtil.isNotBlank(info.getEndReplyTimeEnd())){
                            sql.append(" and wr.end_reply_time< #{info.endReplyTimeEnd}");
                        }
                        if(info.getOverDueReply()!=null){
                            sql.append(" and wr.over_due_reply = #{info.overDueReply}");
                        }


                        sql.append(" group by order_no ) t2 " +
                        " on t2.order_no = t1.order_no and t2.level = t1.level " +
                        "  ");
                    }
                    sql.append(" order by t1.create_time desc ");




             return sql.toString();
        }
    }
}
