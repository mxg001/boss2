package cn.eeepay.boss.action.capitalInsurance;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;
import cn.eeepay.framework.service.capitalInsurance.SafeOrderService;
import cn.eeepay.framework.util.Constants;
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
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单Action
 */
@Controller
@RequestMapping(value = "/safeOrder")
public class SafeOrderAction {

    private static final Logger log = LoggerFactory.getLogger(SafeOrderAction.class);

    @Resource
    private SafeOrderService safeOrderService;

    /**
     * 查询保险订单列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SafeOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeOrder order = JSONObject.parseObject(param, SafeOrder.class);
            safeOrderService.selectAllList(order, page);
            OrderTotal total=safeOrderService.selectSum(order, page);
            msg.put("total",total);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询保险订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询保险订单列表异常!");
        }
        return msg;
    }

    /**
     * 查询保险订单详情
     */
    @RequestMapping(value = "/getSafeOrderDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getSafeOrderDetail(@RequestParam("id") int id) throws Exception{
        return getDetail(id,0);
    }
    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getDataProcessing(@RequestParam("id") int id) throws Exception{
        return getDetail(id,3);
    }

    private Map<String,Object> getDetail(int id,int editState){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeOrder order = safeOrderService.getSafeOrderDetail(id);
            if(order!=null&&0==editState){
                order.setcMobile(StringUtil.sensitiveInformationHandle(order.getcMobile(),0));
                order.setcCertfCde(StringUtil.sensitiveInformationHandle(order.getcCertfCde(),1));
                order.setcMobile1(StringUtil.sensitiveInformationHandle(order.getcMobile1(),0));
                order.setcCertNo(StringUtil.sensitiveInformationHandle(order.getcCertNo(),1));
            }
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询保险订单详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询保险订单详情异常!");
        }
        return msg;
    }
    /**
     * 手工退保
     */
    @RequestMapping(value = "/retreatsSafe")
    @ResponseBody
    @SystemLog(description = "手工退保",operCode="safeOrder.retreatsSafe")
    public Map<String,Object> retreatsSafe(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            safeOrderService.retreatsSafe(ids,msg);
        } catch (Exception e){
            log.error("手工退保异常!",e);
            msg.put("status", false);
            msg.put("msg", "手工退保异常!");
        }
        return msg;
    }

    /**
     * 导出保险订单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        SafeOrder order = JSONObject.parseObject(param, SafeOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<SafeOrder> list=safeOrderService.importDetailSelect(order);
        try {
            safeOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出保险订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出保险订单列表异常!");
        }
        return msg;
    }
}
