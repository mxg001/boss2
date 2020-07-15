package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.creditRepay.YfbWarnInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author MXG
 * create 2018/10/22
 */
public interface YfbWarnDao {

    @Select("select * from yfb_warn_info where warn_code=#{code}")
    @ResultType(YfbWarnInfo.class)
    YfbWarnInfo selectWarnInfoByWarnCode(@Param("code") String code);

    @Update("update yfb_warn_info set status=#{info.status},warn_trigger_value=#{info.warnTriggerValue}," +
            "warn_count_time=#{info.warnCountTime},warn_phone=#{info.warnPhone},last_update_time=now() where id=#{info.id}")
    int update(@Param("info") YfbWarnInfo info);

    @Select("select * from yfb_warn_info where id=#{id}")
    @ResultType(YfbWarnInfo.class)
    YfbWarnInfo selectOne(@Param("id") String id);
}
