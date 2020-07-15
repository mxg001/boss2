package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author tans
 * @date 2019/6/20 16:51
 */
public interface BossSysConfigDao {
    @Select("select PARAM_VALUE from sys_config where PARAM_KEY = #{key} limit 1")
    @ResultType(String.class)
    String selectValueByKey(@Param("key") String key);

    @Update("update sys_config set PARAM_VALUE = #{value} where PARAM_KEY = #{key}")
    int updateValueByKey(@Param("key") String key, @Param("value") String value);
}
