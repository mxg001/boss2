package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CashBackAllAgent;
import cn.eeepay.framework.service.allAgent.CashBackAllAgentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cashBackAllAgent")
public class CashBackAllAgentAction {
    private static final Logger log = LoggerFactory.getLogger(CashBackAllAgentAction.class);

    @Resource
    private CashBackAllAgentService cashBackAllAgentService;

    /**
     * 盟主活动返现明细
     */
    @RequestMapping(value = "/getCashBackDetailAllAgent")
    @ResponseBody
    public Map<String,Object> getCashBackDetailAllAgent(@RequestParam("info") String param, @ModelAttribute("page")
            Page<CashBackAllAgent> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CashBackAllAgent info = JSONObject.parseObject(param, CashBackAllAgent.class);
            cashBackAllAgentService.queryCashBackDetailAllAgentList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("盟主活动返现明细异常!",e);
            msg.put("status", false);
            msg.put("msg", "盟主活动返现明细异常!");
        }
        return msg;
    }

    @RequestMapping("/exportCashBackAllAgent")
    public void exportCashBackAllAgent(@RequestParam String param, HttpServletResponse response){
        try {
            CashBackAllAgent info = JSON.parseObject(param,CashBackAllAgent.class);
            cashBackAllAgentService.exportCashBackAllAgent(info, response);
        } catch (Exception e) {
            log.info("导出盟主活动返现明细失败,参数:{}");
            log.info(e.toString());
        }
    }
}
