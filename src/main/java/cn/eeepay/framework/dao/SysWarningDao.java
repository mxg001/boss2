package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface SysWarningDao {

    @Select("select * from sys_warning where type=#{type}")
    @ResultType(Map.class)
    Map getByType(@Param("type") String type);

    @Update("UPDATE sys_warning SET cycle=#{map.cycle},num=#{map.num}, phones=#{map.phones}, cd_time=#{map.cd_time} WHERE type=#{map.type}")
    int updateSysWarning(@Param("map") Map<String, Object> map);

    @Select("select * from sys_warning where type=#{type}")
    @ResultType(Map.class)
    List<Map> getListByType(@Param("type") String type);

    @DeleteProvider(type= SqlProvider.class,method="deleteWarningIds")
    int deleteWarningIds(@Param("ids")List<Integer> ids);

    @Update("UPDATE sys_warning SET content=#{map.content},cycle=#{map.cycle},num=#{map.num},err_code=#{map.err_code}, " +
            "cd_time=#{map.cd_time},phones=#{map.phones},type=#{map.type} WHERE id=#{map.id}")
    int updateSysWarningById(@Param("map") Map<String, Object> map);

    @Insert("INSERT INTO sys_warning ( content, cycle, num, cd_time, phones, type, create_time, remark, creator, err_code) VALUES " +
            "(#{map.content}, #{map.cycle}, #{map.num}, #{map.cd_time}, #{map.phones}, #{map.type}, NOW(), #{map.remark},#{map.creator}, #{map.err_code})")
    int intsertSysWarning(@Param("map") Map<String, Object> map);

    public class SqlProvider{
        public String deleteWarningIds(Map<String, List<Integer>> param){
            List<Integer> list = param.get("ids");
            StringBuilder sb = new StringBuilder();
            if(list!=null && list.size()>0){
                sb.append("delete from sys_warning where id in (");
                MessageFormat messageFormat = new MessageFormat("#'{'ids[{0}]},");
                for (int i = 0; i < list.size(); i++) {
                    sb.append(messageFormat.format(new Integer[]{i}));
                }
                sb.setLength(sb.length()-1);
                sb.append(")");
            }
            return sb.toString();
        }
    }
}
