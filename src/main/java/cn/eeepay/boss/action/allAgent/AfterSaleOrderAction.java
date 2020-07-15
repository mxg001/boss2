package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AfterSaleOrder;
import cn.eeepay.framework.service.allAgent.AfterSaleOrderService;
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
@RequestMapping(value = "/afterSaleOrder")
public class AfterSaleOrderAction {
    private static final Logger log = LoggerFactory.getLogger(AfterSaleOrderAction.class);

    @Resource
    private AfterSaleOrderService afterSaleOrderService;

    /**
     * 申购售后订单查询
     */
    @RequestMapping(value = "/queryAfterSaleOrderList")
    @ResponseBody
    public Map<String,Object> queryAfterSaleOrderList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<AfterSaleOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AfterSaleOrder info = JSONObject.parseObject(param, AfterSaleOrder.class);
            afterSaleOrderService.queryAfterSaleOrderList(info, page);
            Map<String,Object> pageCount=afterSaleOrderService.queryAfterSaleOrderCount(info);
            msg.put("page",page);
            msg.put("pageCount",pageCount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("申购售后订单异常!",e);
            msg.put("status", false);
            msg.put("msg", "申购售后订单异常!");
        }
        return msg;
    }

    /**
     * 立即处理
     */
    @RequestMapping(value = "/processAfterSaleOrder")
    @ResponseBody
    @SystemLog(description = "申购售后立即处理", operCode = "afterSaleOrder.processAfterSaleOrder")
    public Map<String, Object> processAfterSaleOrder(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            AfterSaleOrder info = JSONObject.parseObject(param, AfterSaleOrder.class);
            int num = afterSaleOrderService.updateProcessAfterSaleOrder(info);
            if (num > 0) {
                msg.put("msg", "处理成功!");
                msg.put("status", true);
            } else {
                msg.put("msg", "处理失败!");
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("立即处理异常!", e);
            msg.put("status", false);
            msg.put("msg", "立即处理异常!");
        }
        return msg;
    }

    @RequestMapping("/exportAfterSaleOrder")
    public void exportAfterSaleOrder(@RequestParam String param, HttpServletResponse response){
        try {
            AfterSaleOrder info = JSON.parseObject(param,AfterSaleOrder.class);
            afterSaleOrderService.exportAfterSaleOrder(info, response);
        } catch (Exception e) {
            log.info("导出盟主活动返现明细失败,参数:{}");
            log.info(e.toString());
        }
    }
}
