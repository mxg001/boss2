package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.TerminalBack;
import cn.eeepay.framework.service.allAgent.TerminalBackService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/terminalBack")
public class TerminalBackAction {
    private static final Logger log = LoggerFactory.getLogger(TerminalBackAction.class);

    @Resource
    private TerminalBackService terminalBackService;

    /**
     * SN号回拨记录查询
     */
    @RequestMapping(value = "/queryTerminalBackList")
    @ResponseBody
    public Map<String,Object> queryTerminalBackList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<TerminalBack> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TerminalBack info = JSONObject.parseObject(param, TerminalBack.class);
            terminalBackService.queryTerminalBackList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("申购售后订单异常!",e);
            msg.put("status", false);
            msg.put("msg", "申购售后订单异常!");
        }
        return msg;
    }

    /**
     * SN列表
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/queryTerminalBackSN")
    @ResponseBody
    public Map<String,Object> queryTerminalBackSN(@RequestParam("orderNo") String orderNo){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<Map<String, Object>> list=terminalBackService.queryTerminalBackSN(orderNo);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("SN列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "SN列表异常!");
        }
        return msg;
    }

    @RequestMapping("/exportTerminalBack")
    public void exportTerminalBack(@RequestParam String param, HttpServletResponse response){
        try {
            TerminalBack info = JSON.parseObject(param,TerminalBack.class);
            terminalBackService.exportTerminalBack(info, response);
        } catch (Exception e) {
            log.info("导出盟主活动返现明细失败,参数:{}");
            log.info(e.toString());
        }
    }
}
