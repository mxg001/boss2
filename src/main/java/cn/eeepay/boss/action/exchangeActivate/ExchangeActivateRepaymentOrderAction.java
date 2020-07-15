package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateRepaymentOrder;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateRepaymentOrderService;
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
 * 超级兑激活版 还款订单
 */
@Controller
@RequestMapping(value = "/exchangeActivateRepaymentOrder")
public class ExchangeActivateRepaymentOrderAction {


    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateRepaymentOrderAction.class);

    @Resource
    private ExchangeActivateRepaymentOrderService exchangeActivateRepaymentOrderService;
    /**
     * 还款订单查询列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateRepaymentOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateRepaymentOrder order = JSONObject.parseObject(param, ExchangeActivateRepaymentOrder.class);
            exchangeActivateRepaymentOrderService.selectAllList(order, page);
            TotalAmount totalAmount=exchangeActivateRepaymentOrderService.selectSum(order, page);
            msg.put("page",page);
            msg.put("totalAmount",totalAmount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("还款订单查询列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "还款订单查询列表异常!");
        }
        return msg;
    }

    /**
     * 还款订单详情
     */
    @RequestMapping(value = "/getRepaymentOrder")
    @ResponseBody
    public Map<String,Object> getRepaymentOrder(@RequestParam("id") long id) throws Exception{
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
            ExchangeActivateRepaymentOrder order=exchangeActivateRepaymentOrderService.getRepaymentOrder(id);
            if(0==editState){
                order.setMobileUsername(StringUtil.sensitiveInformationHandle(order.getMobileUsername(),0));
                order.setIdCardNo(StringUtil.sensitiveInformationHandle(order.getIdCardNo(),1));
            }
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询还款订单详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询还款订单详情异常!");
        }
        return msg;
    }
    /**
     * 导出还款订单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ExchangeActivateRepaymentOrder order = JSONObject.parseObject(param, ExchangeActivateRepaymentOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ExchangeActivateRepaymentOrder> list=exchangeActivateRepaymentOrderService.importDetailSelect(order);
        try {
            exchangeActivateRepaymentOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出还款订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出还款订单列表异常!");
        }
        return msg;
    }
}
