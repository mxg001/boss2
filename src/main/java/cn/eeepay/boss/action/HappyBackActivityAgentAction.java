package cn.eeepay.boss.action;


import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityAgent;
import cn.eeepay.framework.model.HappyBackActivityAgentDetail;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.HappyBackActivityAgentService;
import cn.eeepay.framework.service.HlfActivityAgentJobService;
import cn.eeepay.framework.service.HlfActivityMerchantJobService;
import cn.eeepay.framework.util.Constants;
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
import java.util.List;
import java.util.Map;

/**
 * @author rpc
 * @description 欢乐返代理商奖励查询
 * @date 2019/11/5
 */
@Controller
@RequestMapping(value = "/happyBackActivityAgent")
public class HappyBackActivityAgentAction {
    private static final Logger log = LoggerFactory.getLogger(HappyBackActivityAgentAction.class);

    @Resource
    private HappyBackActivityAgentService happyBackActivityAgentService;
    @Resource
    private HlfActivityAgentJobService hlfActivityAgentJobService;

    /**
     * 欢乐返代理商奖励活动查询
     *
     * @param params
     * @param page
     * @return
     */
    @RequestMapping(value = "/selectHappyBackActivityAgent")
    @ResponseBody
    public Object selectHappyBackActivityAgent(@RequestParam("baseInfo") String params,
                                               @ModelAttribute("page") Page<HappyBackActivityAgent> page) {
        HappyBackActivityAgent happyBackActivityAgent = JSON.parseObject(params, HappyBackActivityAgent.class);

        try {
            System.out.println(happyBackActivityAgent.toString());
            happyBackActivityAgentService.selectHappyBackActivityAgent(page, happyBackActivityAgent);
        } catch (Exception e) {
            log.error("欢乐返代理商奖励活动查询异常", e);
        }
        return page;
    }

    /**
     * 统计金额
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/countMoney")
    @ResponseBody
    public Object countMoney(@RequestParam("baseInfo") String params) {
        HappyBackActivityAgent happyBackActivityAgent = JSON.parseObject(params, HappyBackActivityAgent.class);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = happyBackActivityAgentService.countMoney(happyBackActivityAgent);
        } catch (Exception e) {
            log.error("欢乐返代理商奖励活动查询异常", e);
        }
        return resultMap;
    }

    /**
     * 导出欢乐返活跃商户活动列表
     *
     * @param baseInfo
     * @param response
     * @return
     */
    //@SystemLog(description = "欢乐返代理商奖励活动列表导出")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/exportExcel.do")
    public void exportAgentDetail(String baseInfo, HttpServletResponse response) {
        try {
            if (StringUtil.isNotBlank(baseInfo)) {
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            HappyBackActivityAgent info = JSON.parseObject(baseInfo, HappyBackActivityAgent.class);
            happyBackActivityAgentService.exportExcel(info, response);
        } catch (Exception e) {
            log.error("导出欢乐返代理商奖励活动列表失败,参数:{}", baseInfo);
            log.error("导出欢乐返代理商奖励活动列表失败", e);

        }
    }

    /**
     * 批量奖励入账
     *
     * @return
     */
    @RequestMapping(value = "/agentRewardAccountStatus")
    @ResponseBody
    @SystemLog(description = "批量奖励入账", operCode = "happyBackActivityAgent.agentRewardAccountStatus")
    public Map<String, Object> agentRewardAccountStatus(@RequestParam("ids") String ids) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            msg = hlfActivityAgentJobService.agentRewardAccountStatus(ids);
            return msg;
        } catch (Exception e) {
            log.error("批量奖励入账操作异常", e);
            msg.put("msg", "批量奖励入账操作异常!");
            msg.put("status", false);
        }
        return msg;
    }

    @RequestMapping(value = "/agentAwardDetail")
    @ResponseBody
    public Map<String, Object> agentAwardDetail(@RequestParam("id") String id) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            List<HappyBackActivityAgentDetail> data = happyBackActivityAgentService.agentAwardDetail(id);
            msg.put("status", true);
            msg.put("data", data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return msg;
    }


}
