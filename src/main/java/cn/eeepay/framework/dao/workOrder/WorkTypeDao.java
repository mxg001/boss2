package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.workOrder.WorkType;
import cn.eeepay.framework.util.StringUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 16:35
 */
public interface WorkTypeDao {

    @Select("select name v,id k from work_type ")
    List<Map<String,String>> getAllWorkTypes();


    @Select("select * from work_type where id = #{id}")
    @ResultType(WorkType.class)
    WorkType getWorkTypeById(@Param("id") Long id);


    @Select("select id,name,deal_process from work_type")
    @ResultType(List.class)
    List<WorkType> getAll();


    @Delete("delete from work_type  where id = #{id}")
    @ResultType(Integer.class)
    int del(@Param("id") Long id );

    @Insert("INSERT INTO `work_type`( `name`, `agent_show`, `reply_type`, `desc`, `deal_process`, `create_time`, `operator`) VALUES" +
            " ( #{info.name}, #{info.agentShow}, #{info.replyType}, #{info.desc}, #{info.dealProcess}, now(), #{info.operator}); ")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "info.id", before = false, resultType = Long.class)
    Long insert(@Param("info") WorkType info);

    @Update("UPDATE `work_type` " +
            "SET `name` = #{info.name}, " +
            "`agent_show` = #{info.agentShow}, " +
            "`reply_type` = #{info.replyType}," +
            " `desc` = #{info.desc}," +
            " `deal_process` = #{info.dealProcess}, " +
            "`operator` = #{info.operator} WHERE `id` = #{info.id};")
    @ResultType(Integer.class)
    int update(@Param("info") WorkType info) ;

    @SelectProvider(type = WorkTypeDao.SqlProvider.class, method = "query")
    @ResultType(List.class)
    List<WorkType> query(@Param("page") Page<WorkType> page, @Param("info") WorkType info);

    class SqlProvider {
        public String query(Map<String, Object> param) {
            final WorkType info = (WorkType) param.get("info");
            return new SQL() {
                {
                    SELECT("*");
                    FROM("work_type wt ");

                    if(StringUtil.isNotBlank(info.getId())){
                        WHERE("wt.id = #{info.id}");
                    }

                    if(StringUtil.isNotBlank(info.getName())){
                        WHERE("wt.name = #{info.name}");
                    }

                    if(StringUtil.isNotBlank(info.getReplyType())){
                        WHERE("wt.reply_type=#{info.replyType}");
                    }
                    ORDER_BY("create_time desc");

                }
            }.toString();
        }

    }


}
