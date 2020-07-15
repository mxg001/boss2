package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.service.allAgent.MerchantAllAgentService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/merchantAllAgent")
public class MerchantAllAgentAction {
    private static final Logger log = LoggerFactory.getLogger(MerchantAllAgentAction.class);

    @Resource
    private MerchantAllAgentService merchantAllAgentService;

    /**
     * 商户查询
     */
    @RequestMapping(value = "/selectMerchantAllAgent")
    @ResponseBody
    public Map<String,Object> selectMerchantAllAgent(@RequestParam("info") String param, @ModelAttribute("page")
            Page<MerchantAllAgent> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MerchantAllAgent merchantAllAgent = JSONObject.parseObject(param, MerchantAllAgent.class);
            merchantAllAgentService.queryMerchantAllAgent(merchantAllAgent, page);
            Map<String,Object> countSet=merchantAllAgentService.queryMerchantAllAgentCount(merchantAllAgent);
            msg.put("countSet",countSet);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("商户查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "商户查询异常!");
        }
        return msg;
    }
}
