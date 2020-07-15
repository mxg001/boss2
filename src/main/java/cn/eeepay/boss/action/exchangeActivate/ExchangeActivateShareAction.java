package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateShareService;
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
@RequestMapping(value = "/exchangeActivateShare")
public class ExchangeActivateShareAction {


    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateShareAction.class);

    @Resource
    private ExchangeActivateShareService exchangeActivateShareService;

    /**
     * 订单分润查询列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateShare> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateShare order = JSONObject.parseObject(param, ExchangeActivateShare.class);
            exchangeActivateShareService.selectAllList(order, page);
            TotalAmount totalAmount=exchangeActivateShareService.selectSum(order, page);
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
        ExchangeActivateShare order = JSONObject.parseObject(param, ExchangeActivateShare.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ExchangeActivateShare> list=exchangeActivateShareService.importDetailSelect(order);
        try {
            exchangeActivateShareService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出订单分润列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "导出订单分润列表失败!");
        }
        return msg;
    }
}
