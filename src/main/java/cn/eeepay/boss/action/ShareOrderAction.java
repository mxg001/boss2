package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ShareOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.service.ShareOrderService;
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
 * 订单分润查询
 */
@Controller
@RequestMapping(value = "/shareOrder")
public class ShareOrderAction {


    private static final Logger log = LoggerFactory.getLogger(ShareOrderAction.class);

    @Resource
    private ShareOrderService shareOrderService;

    /**
     * 订单分润查询列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ShareOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ShareOrder order = JSONObject.parseObject(param, ShareOrder.class);
            shareOrderService.selectAllList(order, page);
            TotalAmount totalAmount=shareOrderService.selectSum(order, page);
            msg.put("page",page);
            msg.put("totalAmount",totalAmount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("订单分润查询列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "订单分润查询列表失败!");
        }
        return msg;
    }

    /**
     * 导出订单分润列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ShareOrder order = JSONObject.parseObject(param, ShareOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ShareOrder> list=shareOrderService.importDetailSelect(order);
        try {
            shareOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出订单分润列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "导出订单分润列表失败!");
        }
        return msg;
    }
}
