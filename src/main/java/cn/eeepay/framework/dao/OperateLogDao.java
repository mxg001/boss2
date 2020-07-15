package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OperateLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author MXG
 * create 2018/06/29
 */
public interface OperateLogDao {

    @Select("SELECT * FROM yfb_operate_log " +
            "WHERE operate_code = #{operateCode} AND be_operator = #{merchantNo} ORDER BY operate_time DESC LIMIT 2")
    @ResultType(OperateLog.class)
    List<OperateLog> queryByOperateCodeAndBeOperator(@Param("operateCode")String operateCode, @Param("merchantNo") String merchantNo);

    @Select("SELECT * FROM yfb_operate_log " +
            "WHERE operate_code = #{operateCode} AND be_operator = #{merchantNo} ORDER BY operate_time DESC")
    @ResultType(OperateLog.class)
    List<OperateLog> queryAllMerStatusLog(@Param("page")Page<OperateLog> page, @Param("operateCode")String operateCode, @Param("merchantNo")String merchantNo);

    @Insert("INSERT INTO " +
            "yfb_operate_log(operator,operate_code,operate_table,pre_value,after_value,operate_detail," +
            "operate_time,operate_from,be_operator) " +
            "VALUE(#{operateLog.operator},#{operateLog.operateCode},#{operateLog.operateTable},#{operateLog.preValue}," +
            "#{operateLog.afterValue},#{operateLog.operateDetail},#{operateLog.operateTime}," +
            "#{operateLog.operateFrom},#{operateLog.beOperator})")
    int save(@Param("operateLog") OperateLog operateLog);
}
