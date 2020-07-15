package cn.eeepay.framework.daoSuperbank;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface SysIdDao {

    @Update("update sys_id SET `key_value`=#{value} WHERE (`key_id`=#{key})")
    int update(@Param("key") String key,@Param("value") String value);

    @Select("select key_value from sys_id where key_id=#{key}")
    Map<String,Object> select(@Param("key")String key);

    @Update("update sys_pinfu_chaju SET `p_value`=#{value} WHERE `code`=#{key}")
	int updateSysPinfuChaju(@Param("key")String key,@Param("value")String value);

}
