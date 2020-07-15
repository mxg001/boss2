package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.*;

import java.util.Map;

public interface SdbConfigDao {

    @Select("select * from team_ad good where team_id=#{team_id}")
    @ResultType(Map.class)
    Map getSdbConfig(@Param("team_id") int team_id);

    @Update("update team_ad set team_ad_url=#{team_ad_url} where team_id=#{team_id}")
    int updateSdbConfigImg(@Param("team_id") int team_id, @Param("team_ad_url") String team_ad_url);

    @Insert(" INSERT INTO team_ad (team_id,team_ad_url) VALUES (#{team_id},#{team_ad_url})")
    int addSdbConfig(@Param("team_id") int team_id, @Param("team_ad_url") String team_ad_url);

}
