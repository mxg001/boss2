package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.model.workOrder.WorkFileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/27 11:06
 */
public interface WorkFileInfoDao {


    @Insert("INSERT INTO `work_file_info`( `file_type`, `file_url`, `file_name`, `create_time`, `belong_type`, `belong_id`) VALUES " +
            "( #{info.fileType}, #{info.fileUrl}, #{info.fileName}, now(), #{info.belongType}, #{info.belongId})")
    @ResultType(Integer.class)
    int insert(@Param("info")WorkFileInfo info);


    @Delete("delete from work_file_info where belong_type=#{belongType} and belong_id=#{belongId}")
    @ResultType(Integer.class)
    int delByWorkTypeId(@Param("belongType")Integer belongType,@Param("belongId")Long belongId);


    @Select("select * from work_file_info where  belong_id=#{belongId}  and belong_type=#{belongType} and file_type not like 'image%'")
    @ResultType(List.class)
    List<WorkFileInfo> getFiles(@Param("belongType") Integer belongType,@Param("belongId")Long belongId);

    @Select("select * from work_file_info where  belong_id=#{belongId}  and belong_type=#{belongType} and file_type like 'image%'")
    @ResultType(List.class)
    List<WorkFileInfo> getImgs(@Param("belongType") Integer belongType,@Param("belongId")Long belongId);

}
