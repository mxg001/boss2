package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WarningEvents;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10/010.
 * @author liuks
 * 预警事件dao
 */
public interface WarningEventsDao {

    /**
     *根据条件动态查询
     */
    @SelectProvider(type=WarningEventsDao.SqlProvider.class,method="selectAllWarningEvents")
    @ResultType(WarningEvents.class)
    List<WarningEvents> getWarningEventsAll(@Param("page") Page<WarningEvents> page,@Param("queryaram") WarningEvents we);

    /**
     *根据条件动态导出
     */
    @SelectProvider(type=WarningEventsDao.SqlProvider.class,method="selectAllWarningEvents")
    @ResultType(WarningEvents.class)
    List<WarningEvents> exportAllInfo(@Param("queryaram") WarningEvents we);


    @Insert(
            "INSERT INTO warning_events " +
                    "(event_number, acq_id,acq_enname,acq_name,trans_status,trans_status_number," +
                    " acq_service_id,service_name,out_service_id,out_service_name,message,status," +
                    " create_time,sms_time,sms_count,task_name,task_group,task_status) " +
                    "VALUES" +
                    "(#{we.eventNumber},#{we.acqId},#{we.acqEnname},#{we.acqName},#{we.transStatus},#{we.transStatusNumber}," +
                    " #{we.acqServiceId},#{we.serviceName},#{we.outServiceId},#{we.outServiceName},#{we.message},#{we.status}," +
                    " #{we.createTime},#{we.smsTime},1,#{we.taskName},#{we.taskGroup},#{we.taskStatus})"
    )
    int insertWarningEvents(@Param("we")WarningEvents we);

    @Select(
            " select * from warning_events " +
                    " where create_time>=#{we.createTimeBegin} " +
                    "       and create_time<=#{we.createTimeEnd} " +
                    "       and status=2 " +
                    "       and out_service_id=#{we.outServiceId}"
    )
    @ResultType(WarningEvents.class)
    List<WarningEvents> selectWarningEvents(@Param("we")WarningEvents we);

    @Select(
            " select * from warning_events " +
                    " where create_time>=#{we.createTimeBegin} " +
                    "       and create_time<=#{we.createTimeEnd} " +
                    "       and status=3 " +
                    "       and task_name=#{we.taskName} " +
                    "       and task_group=#{we.taskGroup} " +
                    "       and task_status='-1' "
    )
    @ResultType(WarningEvents.class)
    List<WarningEvents> selectWarningEventsTask(@Param("we")WarningEvents we);

    class SqlProvider{

        public String selectAllWarningEvents(final Map<String, Object> param){
            final WarningEvents we = (WarningEvents) param.get("queryaram");
            String str=new SQL() {{
                SELECT(" * ");
                FROM(" warning_events we");
                if(we.getAcqId()!=null){
                    WHERE(" we.acq_id=#{queryaram.acqId} ");
                }
                if (we.getStatus()>0) {
                    WHERE(" we.status=#{queryaram.status} ");
                }
                if(we.getCreateTimeBegin() != null){
                    WHERE("we.create_time >= #{queryaram.createTimeBegin}");
                }
                if(we.getCreateTimeEnd() != null){
                    WHERE("we.create_time <= #{queryaram.createTimeEnd}");
                }
                ORDER_BY("we.create_time desc");
            }}.toString();
            return str;
        }
    }
}
