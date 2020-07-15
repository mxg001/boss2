package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExcludeCard;
import cn.eeepay.framework.model.RepayChannel;

import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/08/30
 */
public interface ChannelService {

    List<RepayChannel> selectChannels(Page<RepayChannel> page, String id);

    RepayChannel selectById(String id);

    List<Map> selectOptionList();

    int addChannel(RepayChannel repayChannel);

    RepayChannel selectByChannelCode(String channelCode);

    int addExcludeCard(ExcludeCard card);

    List<ExcludeCard> queryExcludeCard(String condition,String channelCode);

    int removeCard(int id);

    ExcludeCard selectByBankCodeAndChannelCode(String bankCode, String channelCode);

    int updateChannel(RepayChannel repayChannel);

    List<ExcludeCard> selectExcludeCardListByChannelCode(String channelCode);

    int deleteExcludeCardByChannelCode(String channelCode);

}
