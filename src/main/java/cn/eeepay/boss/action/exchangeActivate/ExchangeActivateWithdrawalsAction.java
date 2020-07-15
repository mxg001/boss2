package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateWithdrawals;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateWithdrawalsService;
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
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 查询用户提现记录
 */
@Controller
@RequestMapping(value = "/exchangeActivateWithdrawals")
public class ExchangeActivateWithdrawalsAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateWithdrawalsAction.class);


    @Resource
    private ExchangeActivateWithdrawalsService exchangeActivateWithdrawalsService;

    /**
     * 查询用户提现记录列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateWithdrawals> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateWithdrawals wi = JSONObject.parseObject(param, ExchangeActivateWithdrawals.class);
            exchangeActivateWithdrawalsService.selectAllList(wi, page);
            TotalAmount totalAmount=exchangeActivateWithdrawalsService.selectSum(wi, page);
            msg.put("page",page);
            msg.put("totalAmount",totalAmount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询用户提现记录列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询用户提现记录列表失败!");
        }
        return msg;
    }


    /**
     * 导出用户提现记录列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ExchangeActivateWithdrawals wi = JSONObject.parseObject(param, ExchangeActivateWithdrawals.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<ExchangeActivateWithdrawals> list=exchangeActivateWithdrawalsService.importDetailSelect(wi);
        try {
            exchangeActivateWithdrawalsService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出用户提现记录列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "导出用户提现记录列表失败!");
        }
        return msg;
    }
}
