package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.AgentOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.service.AgentOrderService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 代理费订单
 */
@Controller
@RequestMapping(value = "/agentOrder")
public class AgentOrderAction {

    private static final Logger log = LoggerFactory.getLogger(AgentOrderAction.class);

    @Resource
    private AgentOrderService agentOrderService;

    /**
     * 查询代理费订单列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<AgentOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AgentOrder order = JSONObject.parseObject(param, AgentOrder.class);
            agentOrderService.selectAllList(order, page);
            TotalAmount totalAmount=agentOrderService.selectSum(order, page);
            msg.put("page",page);
            msg.put("totalAmount",totalAmount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询代理费订单列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询代理费订单列表失败!");
        }
        return msg;
    }

    /**
     * 代理费订单详情
     */
    @RequestMapping(value = "/getAgentOrder")
    @ResponseBody
    public Map<String,Object> getAgentOrder(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AgentOrder order=agentOrderService.getAgentOrder(id);
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询代理费订单详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询代理费订单详情失败!");
        }
        return msg;
    }

    /**
     * 导出代理费订单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        AgentOrder order = JSONObject.parseObject(param, AgentOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<AgentOrder> list=agentOrderService.importDetailSelect(order);
        try {
            agentOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出代理费订单列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "导出代理费订单列表失败!");
        }
        return msg;
    }
}
