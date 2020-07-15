package cn.eeepay.boss.action;


import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.service.HappyBackActivityMerchantService;
import cn.eeepay.framework.service.HlfActivityMerchantJobService;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
@Controller
@RequestMapping(value = "/happyBackActivityMerchant")
public class HappyBackActivityMerchantAction {
    private static final Logger log = LoggerFactory.getLogger(HappyBackActivityMerchantAction.class);

    @Resource
    private HappyBackActivityMerchantService happyBackActivityMerchantService;
    @Resource
    private HlfActivityMerchantJobService hlfActivityMerchantJobService;

    /**
     * 欢乐返活跃商户活动查询
     * @param params
     * @param page
     * @return
     */
    @SystemLog(description = "欢乐返活跃商户活动查询列表")
    @RequestMapping(value = "/selectHappyBackActivityMerchant")
    @ResponseBody
    public Object selectHappyBackActivityMerchant(@RequestParam("baseInfo")String params,
                                                  @ModelAttribute("page") Page<HappyBackActivityMerchant> page){
        HappyBackActivityMerchant happyBackActivityMerchant = JSON.parseObject(params,HappyBackActivityMerchant.class);
        try {
            happyBackActivityMerchantService.selectHappyBackActivityMerchant(page,happyBackActivityMerchant);
        } catch (Exception e) {
            log.error("欢乐返活跃商户活动查询异常", e);
        }
        return page;
    }

    /**
     * 统计金额
     * @param params
     * @return
     */
    @SystemLog(description = "欢乐返活跃商户活动查询统计")
    @RequestMapping(value = "/countMoney")
    @ResponseBody
    public Object countMoney(@RequestParam("baseInfo")String params){
        HappyBackActivityMerchant happyBackActivityMerchant = JSON.parseObject(params,HappyBackActivityMerchant.class);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = happyBackActivityMerchantService.countMoney(happyBackActivityMerchant);
        } catch (Exception e) {
            log.error("欢乐返活跃商户活动查询异常", e);
        }
        return resultMap;
    }

    /**
     * 导出欢乐返活跃商户活动列表
     * @param baseInfo
     * @param response
     * @return
     */
    @SystemLog(description = "欢乐返活跃商户活动列表导出")
    @RequestMapping("/exportExcel.do")
    public void exportShareDetail(String baseInfo, HttpServletResponse response) {
        try {
            if(StringUtil.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            HappyBackActivityMerchant info =  JSON.parseObject(baseInfo, HappyBackActivityMerchant.class);
            happyBackActivityMerchantService.exportExcel(info,response);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导出欢乐返活跃商户活动列表失败,参数:{}",baseInfo);
        }
    }

    /**
     * 批量奖励入账
     * @return
     */
    @RequestMapping(value="/merRewardAccountStatus")
    @ResponseBody
    @SystemLog(description = "批量奖励入账",operCode="happyBackActivityMerchant.merRewardAccountStatus")
    public Map<String, Object> merRewardAccountStatus(@RequestParam("ids")String ids){
        Map<String, Object> msg = new HashMap<String, Object>();
        try{
            msg = hlfActivityMerchantJobService.merRewardAccountStatus(ids);
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg.put("msg","批量奖励入账操作异常!");
            msg.put("status", false);
        }
        return msg;
    }
}
