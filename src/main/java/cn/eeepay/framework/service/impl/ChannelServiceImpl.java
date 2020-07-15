package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ChannelDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExcludeCard;
import cn.eeepay.framework.model.RepayChannel;
import cn.eeepay.framework.service.ChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/08/30
 */
@Service
public class ChannelServiceImpl implements ChannelService{

    @Resource
    private ChannelDao channelDao;
    @Override
    public List<RepayChannel> selectChannels(Page<RepayChannel> page, String id) {
        if(StringUtils.isNotBlank(id)){
            return channelDao.selectChannelById(page, id);
        } else{
            return channelDao.selectChannels(page);
        }
    }

    @Override
    public RepayChannel selectById(String id) {
        return channelDao.selectById(id);
    }

    @Override
    public List<Map> selectOptionList() {
        return channelDao.selectOptionList();
    }

    @Override
    public int addChannel(RepayChannel repayChannel) {
        return channelDao.addChannel(repayChannel);
    }

    @Override
    public RepayChannel selectByChannelCode(String channelCode) {
        return channelDao.selectByChannelCode(channelCode);
    }

    @Override
    public int addExcludeCard(ExcludeCard card) {
        return channelDao.addExcludeCard(card);
    }

    @Override
    public List<ExcludeCard> queryExcludeCard(String condition,String channelCode) {
        return channelDao.queryExcludeCard(condition, channelCode);
    }

    @Override
    public int removeCard(int id) {
        return channelDao.removeCard(id);
    }

    @Override
    public ExcludeCard selectByBankCodeAndChannelCode(String bankCode, String channelCode) {
        return channelDao.selectByBankCodeAndChannelCode(bankCode, channelCode);
    }

    @Override
    public int updateChannel(RepayChannel channel) {
        return channelDao.updateChannel(channel);
    }

    @Override
    public List<ExcludeCard> selectExcludeCardListByChannelCode(String channelCode) {
        return channelDao.selectExcludeCardListByChannelCode(channelCode);
    }

    @Override
    public int deleteExcludeCardByChannelCode(String channelCode) {
        return channelDao.deleteExcludeCardByChannelCode(channelCode);
    }

}
