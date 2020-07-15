package cn.eeepay.framework.dao.workOrder;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.workOrder.WorkOrderUser;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 10:56
 */
public interface WorkOrderUserDao {
    @Insert("INSERT INTO `work_user`( `boss_user_name`, `role_type`, `status`, `duty_type`, `duty_data`, `create_time`,  `operator`) VALUES" +
            " ( #{info.bossUserName},  #{info.roleType}, #{info.status}, #{info.dutyType}, #{info.dutyData}, now(),  #{info.operator})")
    @ResultType(Integer.class)
    int insert(@Param("info")WorkOrderUser info);


    @Update("UPDATE `work_user` SET `boss_user_name` = #{info.bossUserName}, " +
            "`role_type` = #{info.roleType}," +
            " `status` = #{info.status}," +
            " `duty_type` = #{info.dutyType}," +
            " `duty_data` = #{info.dutyData}, " +
            "`operator` = #{info.operator}" +
            " WHERE `id` = #{info.id};")
    @ResultType(Integer.class)
    int update(@Param("info")WorkOrderUser info);

    @Update("update work_user set status=#{info.status}  where id = #{info.id}")
    @ResultType(Integer.class)
    int updateStatusById(@Param("info")WorkOrderUser info);


    @Delete("delete from work_user where id = #{id}")
    @ResultType(Integer.class)
    int del(@Param("id")Long id);



    @SelectProvider(type = WorkOrderUserDao.SqlProvider.class, method = "query")
    @ResultType(WorkOrderUser.class)
    List<WorkOrderUser> query(@Param("page") Page<WorkOrderUser> page, @Param("info") WorkOrderUser info);

    @Select("select real_name v,user_name k from boss_shiro_user where dept_id=11")
    @ResultType(Map.class)
    List<Map<String,String>> getSaleUsers();

    @Select("select * from work_user where boss_user_name=#{bossUserName}")
    @ResultType(WorkOrderUser.class)
    WorkOrderUser getAdminByName(@Param("bossUserName") String bossUserName);


    @Select("select 1 from work_user where find_in_set(#{info.dutyDataParam},duty_data) and duty_type=#{info.dutyType} and boss_user_name=#{info.bossUserName}")
    int queryForAuth(@Param("info") WorkOrderUser info);


    @Select("select * from work_user where id = #{id}")
    @ResultType(WorkOrderUser.class)
    WorkOrderUser getWorkUserById(@Param("id") Long id);

    @Select("select * from work_user where boss_user_name = #{bossUserName} and status = 1")
    @ResultType(WorkOrderUser.class)
    WorkOrderUser getWorkUserByBossUserName(@Param("bossUserName") String bossUserName);

    @Select(" select wr.*,bsu.real_name bossRealName ,bsu.id userId from work_user wr " +
            " left join boss_shiro_user bsu on bsu.user_name = wr.boss_user_name" +
            " where wr.boss_user_name in (${bossUserNames})")
    @ResultType(List.class)
    List<WorkOrderUser> getWorkUserByUserNames(@Param("bossUserNames")String bossUserNames);


    class SqlProvider {

        public String query(Map<String, Object> param) {
            final WorkOrderUser info = (WorkOrderUser) param.get("info");
            StringBuilder sb = new StringBuilder();
            sb.append(" select wu.*,bu.real_name bossRealName,sd.sys_name deptName");
            sb.append(" from work_user wu");
            sb.append(" left join boss_shiro_user bu on wu.boss_user_name=bu.user_name");
            sb.append(" left join sys_dict sd on sd.sys_key='DEPT_LIST' and sd.sys_value=bu.dept_id where 1=1 ");
            if(StringUtil.isNotBlank(info.getDeptNo())){
                sb.append(" and bu.dept_id=#{info.deptNo}");
            }
            if(StringUtil.isNotBlank(info.getRoleType())){
                sb.append(" and wu.role_type=#{info.roleType}");
            }

            if(info.getStatus()!=null){
                sb.append(" and wu.status=#{info.status}");
            }
            if(StringUtil.isNotBlank(info.getBossUserName())){
                sb.append(" and (bu.real_name like concat('%',#{info.bossUserName},'%') or wu.boss_user_name like concat('%',#{info.bossUserName},'%'))");
            }
            sb.append(" order by create_time desc");
            return sb.toString();
        }

    }


}
