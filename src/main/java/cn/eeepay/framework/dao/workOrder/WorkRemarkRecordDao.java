package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.model.workOrder.WorkRemarkRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/5/6 10:10
 */
public interface WorkRemarkRecordDao {

    @Insert("INSERT INTO `work_remark_record`( `belong_type`, `belong_id`, `remark_content`, `agent_show`, `operator`, `create_time`) " +
            "VALUES ( #{info.belongType}, #{info.belongId}, #{info.remarkContent}, #{info.agentShow},#{info.operator},now())")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Long.class)
    Long insert(@Param("info")WorkRemarkRecord info);


    @Select("select wrr.*,u.real_name operatorName from work_remark_record wrr " +
            " left join boss_shiro_user u on u.user_name=wrr.operator " +
            " where wrr.belong_id=#{belonogId} and wrr.belong_type=#{belongType} order by wrr.create_time")
    List<WorkRemarkRecord> getRemarks(@Param("belongType") int belongType, @Param("belonogId")Long belongId);
}
