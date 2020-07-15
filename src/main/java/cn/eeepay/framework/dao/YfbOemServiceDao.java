package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @author tans
 * @date 2019/1/11 11:35
 */
public interface YfbOemServiceDao {

    @Select("SELECT oem_name FROM yfb_oem_service WHERE team_id=#{teamId}")
    @ResultType(String.class)
    String selectTeamName(@Param("teamId") String teamId);
}
