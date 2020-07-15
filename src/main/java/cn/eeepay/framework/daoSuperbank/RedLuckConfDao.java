package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.RedLuckConf;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface RedLuckConfDao {

    @Select("select * from red_luck_conf order by create_time desc limit 1")
    @ResultType(RedLuckConf.class)
    RedLuckConf getLuckConf();

    @Update("update red_luck_conf" +
            " set init_value=#{initValue},lowest_value=#{lowestValue}," +
            " highest_value=#{highestValue},single_comment_value=#{singleCommentValue}," +
            " push_red_value=#{pushRedValue},first_comment_value=#{firstCommentValue}," +
            " receive_red_value=#{receiveRedValue},bad_comment_value=#{badCommentValue}," +
            " ad_comment_value=#{adCommentValue},update_time=#{updateTime}," +
            " operator=#{operator}" +
            " where id = #{id}")
    int updateLuckConf(RedLuckConf baseInfo);
}
