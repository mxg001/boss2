package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.model.risk.SurveyReply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13/013.
 * @author  liuks
 * 回复信息
 */
public interface SurveyReplyDao {

    @Select(
            "select * from survey_reply_record where order_no=#{orderNo} order by create_time desc LIMIT 1 "
    )
    SurveyReply getMaxReply(@Param("orderNo")String orderNo);

    @Select(
            "select * from survey_reply_record where order_no=#{orderNo} order by create_time desc "
    )
    List<SurveyReply> getReplyList(@Param("orderNo")String orderNo);

    @Update(
            "update survey_reply_record set last_deal_status=#{status},last_deal_remark=#{message} where id=#{id}"
    )
    int updateNowSurveyReply(@Param("id")int id,@Param("status") String status, @Param("message")String message);
}
