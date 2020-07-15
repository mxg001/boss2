package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WarningPeople;
import cn.eeepay.framework.model.WarningSet;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/8/008.
 * @author liuks
 * 预警人dao
 */
public interface WarningPeopleDao {
    /**
     *根据条件动态查询
     */
    @SelectProvider(type=WarningPeopleDao.SqlProvider.class,method="selectWarningPeople")
    @ResultType(WarningPeople.class)
    List<WarningPeople> getWarningPeople(@Param("page") Page<WarningPeople> page,@Param("queryaram")WarningPeople wp);

    @Delete("delete from warning_people where id=#{id}")
    int deleteWarningPeople(int id);

    @Insert(
            "INSERT INTO warning_people " +
                    "(user_id,user_name,name,phone,status,create_time,modify_time) " +
                    "VALUES " +
                    "(#{wp.userId},#{wp.userName},#{wp.name},#{wp.phone},#{wp.status},NOW(),NOW())"
    )
    int addaddWarningPeople(@Param("wp")WarningPeople wp);

    @Select(
            "select * from warning_people where user_id=#{userId} and status=#{status}"
    )
    WarningPeople findWarningPeoplebyUserId(@Param("userId")int userId,@Param("status")int status);

    @SelectProvider(type=WarningPeopleDao.SqlProvider.class,method="sumWarningPeople")
    int sumWarningPeople(@Param("queryaram")WarningPeople wp);

    @Select(
            "select * from warning_people where status=#{status}"
    )
    List<WarningPeople> getWarningPeopleAll(@Param("status")int status);


    @Update(
            "update warning_people set name=#{wp.name},phone=#{wp.phone} where id=#{wp.id}"
    )
    int updateWarningPeople(@Param("wp")WarningPeople wp);

    @Select(
            "select * from warning_people where id=#{id}"
    )
    WarningPeople getWarningPeopleById(@Param("id")int id);

    @Update(
            " update warning_people set assignment_task=#{ass} where id=#{id}"
    )
    int updateWarningPeopleByAssignmentTask(@Param("ass")String assignmentTask,@Param("id")int id);

    class SqlProvider {

        public String selectWarningPeople(final Map<String, Object> param){
            final WarningPeople wp = (WarningPeople) param.get("queryaram");
            String str=new SQL() {{
                    SELECT(" * ");
                    FROM(" warning_people wp");
                    if (wp.getStatus()!=null) {
                         WHERE(" wp.status=#{queryaram.status} ");
                    }
                }}.toString();
            return str;
        }
        public String sumWarningPeople(final Map<String, Object> param){
            final WarningPeople wp = (WarningPeople) param.get("queryaram");
            String str=new SQL() {{
                SELECT(" count(*) as total ");
                FROM(" warning_people wp");
                if (wp.getStatus()!=null) {
                    WHERE(" wp.status=#{queryaram.status} ");
                }
            }}.toString();
            return str;
        }
    }
    @Insert(
            "INSERT INTO warning_set " +
                    "(service_id,service_name,waring_cycle,exception_number,failur_waring_cycle,failur_exception_number,status) " +
                    "VALUES " +
                    "(#{ws.serviceId},#{ws.serviceName},#{ws.waringCycle},#{ws.exceptionNumber},#{ws.failurWaringCycle},#{ws.failurExceptionNumber},1)"
    )
	int saveWarningSet(@Param("ws")WarningSet ws);
    @Select(
            "select * from warning_set where service_id=#{ws.serviceId} AND status=#{ws.status}"
    )
	WarningSet getWarnningSet(@Param("ws")WarningSet ws);

    @Update(
            "update warning_set set service_id=#{ws.serviceId},service_name=#{ws.serviceName},waring_cycle=#{ws.waringCycle},exception_number=#{ws.exceptionNumber},failur_waring_cycle=#{ws.failurWaringCycle},failur_exception_number=#{ws.failurExceptionNumber} where service_id=#{ws.serviceId} AND status=1 "
    )
	int updateWarningSet(@Param("ws")WarningSet ws);
    
    @Select(
            "select * from warning_set where service_id=#{serviceId} AND status=#{status}"
    )
	WarningSet getWarningSet(@Param("serviceId")Integer serviceId, @Param("status")Integer status);

    @Select(
            "select service_id,exception_number from warning_set WHERE `status`=1 and warn_status = 1 AND exception_number IS NOT NULL GROUP BY service_id"
    )
    @ResultType(WarningSet.class)
    List<WarningSet> getExceptionNumber();
   
    @Select(
            "select service_id,failur_exception_number from warning_set WHERE `status`=1 and warn_status = 1 AND failur_exception_number IS NOT NULL GROUP BY service_id"
    )
    @ResultType(WarningSet.class)
	List<WarningSet> getFailurExceptionNumber();

    @Select(
            "select * from warning_people where status=#{status} AND id=#{id}"
    )
	WarningPeople getWarningPeopleByIdAndStatus(@Param("id")Integer id, @Param("status")int status);

    @Update( "update warning_people w_p set w_p.sids=#{sids} where w_p.id=#{id}")
	int updateWarningSids(@Param("sids")String param, @Param("id")Integer id,int i);

    @Update( "update warning_people w_p set w_p.sids=#{sids} where w_p.id=#{id}")
  	int updateSidsById(@Param("sids")String sids,@Param("id") Integer id);
}
