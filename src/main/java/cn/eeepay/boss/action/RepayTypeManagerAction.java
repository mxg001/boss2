package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayChannel;
import cn.eeepay.framework.model.RepayPlanInfo;
import cn.eeepay.framework.model.RepayType;
import cn.eeepay.framework.service.RepayTypeManagerService;
import cn.eeepay.framework.util.Constants;
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
 * 超级还订单类型
 * @author MXG
 * create 2018/08/29
 */
@Controller
@RequestMapping(value = "/repayType")
public class RepayTypeManagerAction {

    @Resource
    private RepayTypeManagerService repayTypeManagerService;

    private Logger log = LoggerFactory.getLogger(RepayMerchantAction.class);

    /**
     * 订单类型查询
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectRepayTypeList")
    @ResponseBody
    public Map<String, Object> selectRepayTypeList(@RequestParam String planType,
           @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        Map<String, Object> msg = new HashMap<>();
        try {
            Page<RepayPlanInfo> page = new Page<>(pageNo, pageSize);
            repayTypeManagerService.selectRepayTypeList(page, planType);
            msg.put("status", true);
            msg.put("page", page);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("订单类型查询失败", e);
        }
        return msg;
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/queryTypeDetailById")
    @ResponseBody
    public Map<String, Object> queryTypeDetailById(String id){
        Map<String, Object> msg = new HashMap<>();
        try {
            RepayPlanInfo info = repayTypeManagerService.queryTypeDetailById(id);
            List<RepayChannel> channels = repayTypeManagerService.selectChannelsByRepayType(info.getPlanType());
            RepayType repayType = new RepayType(info, channels);
            msg.put("status", true);
            msg.put("repayType", repayType);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("订单类型查询失败", e);
        }
        return msg;
    }

    /**
     * 修改
     * @param repayType
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody RepayType repayType){
        Map<String, Object> msg = new HashMap<>();
        //验证通道路由比例是否为100
        List<RepayChannel> list = repayType.getRepayChannelList();
        int percent = 0;
        for (RepayChannel channel : list) {
            percent+=channel.getPercent();
        }
        if(percent != 100){
            msg.put("status", false);
            msg.put("msg", "通道路由比例之和必须为100");
            return msg;
        }
        try {
            repayTypeManagerService.updateRepayPlanInfo(repayType.getRepayPlanInfo());
            //在更新通道之前，先将所有通道置为初始状态：即repay_type=null,percent=0
            List<RepayChannel> channels = repayTypeManagerService.selectChannelsByRepayType(repayType.getRepayPlanInfo().getPlanType());
            for (RepayChannel channel : channels) {
                repayTypeManagerService.relieve(channel.getId());
            }
            //重新绑定通道
            List<RepayChannel> repayChannelList = repayType.getRepayChannelList();
            for (RepayChannel channel : repayChannelList) {
                repayTypeManagerService.updateChannel(channel);
            }
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "修改失败");
            log.error("订单类型数据修改失败", e);
        }
        return msg;
    }

    /**
     * 回显所有未绑定订单类型的通道
     * @return
     */
    @RequestMapping(value = "/selectAllChannels")
    @ResponseBody
    public Map<String, Object> selectAllChannels(){
        Map<String, Object> msg = new HashMap<>();
        try {
            List<RepayChannel> channels = repayTypeManagerService.selectChannelsWithoutType();
            msg.put("channels", channels);
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            log.error("通道查询失败", e);
        }
        return msg;
    }

}
