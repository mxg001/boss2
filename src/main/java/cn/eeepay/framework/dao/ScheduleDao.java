package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface ScheduleDao {

    @Insert("insert into " +
            " time_task_record" +
            " (running_no,source_system,running_status,interface_name) " +
            " value" +
            "(#{runNo},'BOSS','init',#{interfaceName})")
    int insert(@Param("runNo")String runNo,@Param("interfaceName")String interfaceName);

    @Update("update time_task_record set running_status =#{status} where running_no=#{runNo}")
    int updateStatus(@Param("runNo") String runNo, @Param("status") String status);

    @Select("select * from time_task_record where running_no=#{runNo}")
    Map<String, Object> query(@Param("runNo")String runNo);
}
