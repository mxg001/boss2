package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateReceiveOrder;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateReceiveOrderService;
import cn.eeepay.framework.util.StringUtil;
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
 * Created by Administrator on 2018/6/7/007.
 * @author  liuks
 * 超级兑激活版 收款订单
 */
@Controller
@RequestMapping(value = "/exchangeActivateReceiveOrder")
public class ExchangeActivateReceiveOrderAction {


    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateReceiveOrderAction.class);

    @Resource
    private ExchangeActivateReceiveOrderService exchangeActivateReceiveOrderService;
    /**
     * 收款订单查询列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateReceiveOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateReceiveOrder order = JSONObject.parseObject(param, ExchangeActivateReceiveOrder.class);
            exchangeActivateReceiveOrderService.selectAllList(order, page);
            TotalAmount totalAmount=exchangeActivateReceiveOrderService.selectSum(order, page);
            msg.put("page",page);
            msg.put("totalAmount",totalAmount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("收款订单查询列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "收款订单查询列表异常!");
        }
        return msg;
    }

    /**
     * 收款订单详情
     */
    @RequestMapping(value = "/getReceiveOrder")
    @ResponseBody
    public Map<String,Object> getReceiveOrder(@RequestParam("id") long id) throws Exception{
        return getUserDetail(id,0);
    }
    /**
      * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam("id") long id) throws Exception{
        return getUserDetail(id,3);
    }

    private Map<String,Object> getUserDetail(long id,int editState){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateReceiveOrder order=exchangeActivateReceiveOrderService.getReceiveOrder(id);
            if(0==editState){
                order.setMobileUsername(StringUtil.sensitiveInformationHandle(order.getMobileUsername(),0));
                order.setIdCardNo(StringUtil.sensitiveInformationHandle(order.getIdCardNo(),1));
            }
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询收款订单详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询收款订单详情异常!");
        }
        return msg;
    }
    /**
     * 导出收款订单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ExchangeActivateReceiveOrder order = JSONObject.parseObject(param, ExchangeActivateReceiveOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ExchangeActivateReceiveOrder> list=exchangeActivateReceiveOrderService.importDetailSelect(order);
        try {
            exchangeActivateReceiveOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出收款订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出收款订单列表异常!");
        }
        return msg;
    }
}
