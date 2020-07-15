package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.model.risk.SurveyOrderLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2018/9/11/011.
 * @author  liuks
 * 调单操作日志
 */
public interface SurveyOrderLogDao {

    @Insert(
            "INSERT INTO survey_order_log" +
                    "(order_no,operate_type,operator,operate_detail,operate_time)" +
                    " VALUES" +
                    " (#{log.orderNo},#{log.operateType},#{log.operator},#{log.operateDetail},NOW())"
    )
    int insertSurveyOrderLog(@Param("log") SurveyOrderLog log);

    @Select(
            "select * from survey_order_log where order_no=#{orderNo} order by operate_time desc limit 20"
    )
    List<SurveyOrderLog> getSurveyOrderLogAll(@Param("orderNo") String orderNo);
}
