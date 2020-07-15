package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ChannelService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信用卡通道管理
 * @author MXG
 * create 2018/08/30
 */
@RequestMapping("/channel")
@Controller
public class ChannelAction {

    @Resource
    private ChannelService channelService;

    private Logger log = LoggerFactory.getLogger(ChannelAction.class);

    /**
     * 通道下拉框
     * @return
     */
    @RequestMapping("/selectOptionList")
    @ResponseBody
    public Map<String, Object> selectOptionList(){
        Map<String, Object> msg = new HashMap<>();
        try {
            List<Map> channelList = channelService.selectOptionList();
            msg.put("status", true);
            msg.put("channelList", channelList);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "通道下拉选项获取失败");
            log.error("通道下拉选项获取失败", e);
        }
        return msg;
    }

    /**
     * 通道查询
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectChannelList")
    @ResponseBody
    public Map<String, Object> selectChannelList(@RequestParam String id,
        @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        Map<String, Object> msg = new HashMap<>();
        try {
            Page<RepayChannel> page = new Page<>(pageNo, pageSize);
            channelService.selectChannels(page,id);
            msg.put("status", true);
            msg.put("page", page);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("通道查询失败", e);
        }
        return msg;
    }

    /**
     * 通道详情
     * @param id
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectById")
    @ResponseBody
    public Map<String, Object> selectById(String id){
        Map<String, Object> msg = new HashMap<>();
        try {
            RepayChannel repayChannel = channelService.selectById(id);
            List<ExcludeCard> excludeCardList = channelService.selectExcludeCardListByChannelCode(repayChannel.getChannelCode());
            Channel channel = new Channel(repayChannel, excludeCardList);
            msg.put("status", true);
            msg.put("channel", channel);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("通道查询失败", e);
        }
        return msg;
    }

    /**
     * 新增通道
     * @return
     */
    @RequestMapping("/addChannel")
    @ResponseBody
    public Map<String, Object> addChannel(@RequestBody Channel channel){
        Map<String, Object> msg = new HashMap<>();
        RepayChannel repayChannel = channel.getRepayChannel();
        RepayChannel existChannel = channelService.selectByChannelCode(repayChannel.getChannelCode());
        if(existChannel != null){
            msg.put("status", false);
            msg.put("msg", "通道编码"+existChannel.getChannelCode()+"已经存在");
            return msg;
        }
        try {
            channelService.addChannel(repayChannel);
            List<ExcludeCard> excludeCardList = channel.getExcludeCardList();
            for (ExcludeCard excludeCard : excludeCardList) {
                ExcludeCard existCard = channelService.selectByBankCodeAndChannelCode(excludeCard.getBankCode(), excludeCard.getChannelCode());
                if(existCard != null){
                    continue;
                }
                channelService.addExcludeCard(excludeCard);
            }
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "添加通道失败");
            log.error("添加通道失败", e);
        }
        return msg;
    }


    /**
     * 修改通道
     * @return
     */
    @RequestMapping("/updateChannel")
    @ResponseBody
    public Map<String, Object> updateChannel(@RequestBody Channel channel){
        Map<String, Object> msg = new HashMap<>();
        try {
            channelService.updateChannel(channel.getRepayChannel());
            //更新不支持银行卡列表
            //先删除
            channelService.deleteExcludeCardByChannelCode(channel.getRepayChannel().getChannelCode());
            List<ExcludeCard> excludeCardList = channel.getExcludeCardList();
            for (ExcludeCard excludeCard : excludeCardList) {
                ExcludeCard existCard = channelService.selectByBankCodeAndChannelCode(excludeCard.getBankCode(), excludeCard.getChannelCode());
                if(existCard != null){
                    continue;
                }
                channelService.addExcludeCard(excludeCard);
            }
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            log.error("修改通道失败", e);
        }
        return msg;
    }

    /**
     * 新增通道时获取通道信息
     * @param id
     * @return
     */
    @RequestMapping("/channelInfo")
    @ResponseBody
    public Map<String, Object> channelInfo(String id){
        Map<String, Object> msg = new HashMap<>();
        RepayChannel channel = channelService.selectById(id);
        msg.put("channel", channel);
        return msg;
    }


    /**
     * 条件查询不支持银行卡列表
     * @return
     */
    @RequestMapping("/queryExcludeCard")
    @ResponseBody
    public Map<String, Object> queryExcludeCard(@RequestBody String params){
        JSONObject object = JSONObject.parseObject(params);
        String condition = object.getString("condition");
        String channelCode = object.getString("channelCode");
        Map<String, Object> msg = new HashMap<>();
        try {
            List<ExcludeCard> excludeCardList = channelService.queryExcludeCard(condition,channelCode);
            msg.put("status", true);
            msg.put("excludeCardList", excludeCardList);
        } catch (Exception e) {
            msg.put("status", false);
            log.error("查詢不支持银行卡失败", e);
        }
        return msg;
    }


    /**
     * 移除不支持银行卡
     * @param ids
     * @return
     */
    @RequestMapping("/removeCard")
    @ResponseBody
    public Map<String, Object> removeCard(int[] ids){
        Map<String, Object> msg = new HashMap<>();
        try {
            for (int id : ids) {
                channelService.removeCard(id);
            }
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            log.error("删除不支持银行卡异常", e);
        }
        return msg;
    }



}
