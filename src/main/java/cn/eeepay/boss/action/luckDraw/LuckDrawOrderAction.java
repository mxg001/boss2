package cn.eeepay.boss.action.luckDraw;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;
import cn.eeepay.framework.service.luckDraw.LuckDrawOrderService;
import cn.eeepay.framework.util.Constants;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/30/030.
 * @author  liuks
 * 抽奖信息
 */
@Controller
@RequestMapping(value="/luckDrawOrder")
public class LuckDrawOrderAction {

    private static final Logger log = LoggerFactory.getLogger(LuckDrawOrderAction.class);


    @Resource
    private LuckDrawOrderService luckDrawOrderService;

    /**
     * 获取抽奖信息列表
     */
    @RequestMapping(value = "/selectAllList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectAllList(@ModelAttribute("page") Page<LuckDrawOrder> page,
                                              @RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LuckDrawOrder order = JSON.parseObject(param, LuckDrawOrder.class);
            luckDrawOrderService.selectAllList(order,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取抽奖信息列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取抽奖信息列表异常!");
        }
        return msg;
    }

    /**
     * 获取抽奖信息详情
     */
    @RequestMapping(value = "/getLuckDrawOrder")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getLuckDrawOrder(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LuckDrawOrder order=luckDrawOrderService.getLuckDrawOrder(id);
            msg.put("info", order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取抽奖信息详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取抽奖信息详情异常!");
        }
        return msg;
    }

    /**
     * 导出抽奖信息列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            LuckDrawOrder order = JSONObject.parseObject(param, LuckDrawOrder.class);
            List<LuckDrawOrder> list=luckDrawOrderService.importDetailSelect(order);
            luckDrawOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出抽奖信息列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出抽奖信息列表异常!");
        }
        return msg;
    }
}
