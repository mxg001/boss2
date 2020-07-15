package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayChannel;
import cn.eeepay.framework.model.RepayPlanInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author MXG
 * create 2018/08/29
 */
public interface RepayTypeManagerDao {

    @Select("select id,plan_type,plan_name,status from yfb_plan_info where plan_type=#{planType}")
    @ResultType(RepayPlanInfo.class)
    List<RepayPlanInfo> selectRepayTypeByPlanType(@Param("page") Page<RepayPlanInfo> page, @Param("planType") String planType);

    @Select("select id,plan_type,plan_name,status from yfb_plan_info")
    @ResultType(RepayPlanInfo.class)
    List<RepayPlanInfo> selectRepayTypeList(@Param("page") Page<RepayPlanInfo> page);

    @Select("select * from yfb_plan_info where id=#{id}")
    @ResultType(RepayPlanInfo.class)
    RepayPlanInfo queryTypeDetailById(@Param("id") String id);

    @Select("select id,channel_name,repay_type,percent from yfb_pay_channel where repay_type=#{repayType}")
    @ResultType(RepayChannel.class)
    List<RepayChannel> selectChannelsByRepayType(@Param("repayType") String repayType);

    @Update("update yfb_plan_info set status=#{info.status},allow_begin_time=#{info.allowBeginTime},allow_end_time=#{info.allowEndTime}," +
            "allow_repay_min_amount=#{info.allowRepayMinAmount},allow_repay_max_amount=#{info.allowRepayMaxAmount}," +
            "allow_first_min_amount=#{info.allowFirstMinAmount},allow_first_max_amount=#{info.allowFirstMaxAmount}," +
            "allow_day_min_num=#{info.allowDayMinNum},allow_day_max_num=#{info.allowDayMaxNum},close_tip=#{info.closeTip} where id=#{info.id}")
    int UpdateRepayType(@Param("info") RepayPlanInfo info);

    @Select("select id,channel_name from yfb_pay_channel where repay_type is null")
    @ResultType(RepayChannel.class)
    List<RepayChannel> selectChannelsWithoutType();

    @Update("update yfb_pay_channel set repay_type=null,percent=0 where id=#{id}")
    int relieve(@Param("id") String id);

    @Update("update yfb_pay_channel set percent=#{channel.percent},repay_type=#{channel.repayType} where id=#{channel.id}")
    int updateChannel(@Param("channel")RepayChannel channel);
}
