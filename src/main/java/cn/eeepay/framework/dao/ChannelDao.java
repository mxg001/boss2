package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExcludeCard;
import cn.eeepay.framework.model.RepayChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/08/30
 */
public interface ChannelDao {

    @Select("select * from yfb_pay_channel")
    @ResultType(RepayChannel.class)
    List<RepayChannel> selectChannels(@Param("page")Page<RepayChannel> page);

    @Select("select * from yfb_pay_channel where id=#{id}")
    @ResultType(RepayChannel.class)
    List<RepayChannel> selectChannelById(@Param("page")Page<RepayChannel> page, @Param("id") String id);

    @Select("select * from yfb_pay_channel where id=#{id}")
    @ResultType(RepayChannel.class)
    RepayChannel selectById(@Param("id")String id);

    @Select("select id as value,channel_name as text from yfb_pay_channel")
    @ResultType(Map.class)
    List<Map> selectOptionList();

    @Insert("insert into yfb_pay_channel(channel_code,channel_name,channel_status,allow_begin_time," +
            "allow_end_time,allow_quick_min_amount,allow_quick_max_amount,allow_split_minute) " +
            "values(#{channel.channelCode},#{channel.channelName},#{channel.channelStatus},#{channel.allowBeginTime}," +
            "#{channel.allowEndTime},#{channel.allowQuickMinAmount},#{channel.allowQuickMaxAmount},#{channel.allowSplitMinute})")
    int addChannel(@Param("channel") RepayChannel channel);

    @Select("select * from yfb_pay_channel where channel_code=#{channelCode}")
    @ResultType(RepayChannel.class)
    RepayChannel selectByChannelCode(@Param("channelCode") String channelCode);

    @Insert("insert into yfb_exclude_card(bank_code,bank_name,channel_code) values(#{card.bankCode},#{card.bankName},#{card.channelCode})")
    int addExcludeCard(@Param("card") ExcludeCard card);

    @Select("select * from yfb_exclude_card where channel_code=#{channelCode} and (bank_code like concat(#{condition}, '%') or bank_name like concat(#{condition},'%'))")
    @ResultType(ExcludeCard.class)
    List<ExcludeCard> queryExcludeCard(@Param("condition") String condition, @Param("channelCode") String channelCode);

    @Delete("delete from yfb_exclude_card where id=#{id}")
    int removeCard(@Param("id") int id);

    @Select("select * from yfb_exclude_card where bank_code=#{bankCode} and channel_code=#{channelCode}")
    @ResultType(ExcludeCard.class)
    ExcludeCard selectByBankCodeAndChannelCode(@Param("bankCode") String bankCode, @Param("channelCode")String channelCode);

    @Update("update yfb_pay_channel set channel_name=#{channel.channelName}," +
            "channel_status=#{channel.channelStatus},allow_begin_time=#{channel.allowBeginTime}," +
            "allow_end_time=#{channel.allowEndTime},allow_quick_min_amount=#{channel.allowQuickMinAmount}," +
            "allow_quick_max_amount=#{channel.allowQuickMaxAmount},allow_split_minute=#{channel.allowSplitMinute} where id=#{channel.id}")
    int updateChannel(@Param("channel") RepayChannel channel);

    @Select("select * from yfb_exclude_card where channel_code=#{channelCode}")
    @ResultType(ExcludeCard.class)
    List<ExcludeCard> selectExcludeCardListByChannelCode(@Param("channelCode") String channelCode);

    @Delete("delete from yfb_exclude_card where channel_code=#{channelCode}")
    int deleteExcludeCardByChannelCode(@Param("channelCode") String channelCode);

}
