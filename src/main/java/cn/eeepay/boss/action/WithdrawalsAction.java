package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.Withdrawals;
import cn.eeepay.framework.service.WithdrawalsService;
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
@RequestMapping(value = "/withdrawals")
public class WithdrawalsAction {

    private static final Logger log = LoggerFactory.getLogger(WithdrawalsAction.class);


    @Resource
    private WithdrawalsService  withdrawalsService;

    /**
     * 查询用户提现记录列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<Withdrawals> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Withdrawals wi = JSONObject.parseObject(param, Withdrawals.class);
            withdrawalsService.selectAllList(wi, page);
            TotalAmount totalAmount=withdrawalsService.selectSum(wi, page);
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
        Withdrawals wi = JSONObject.parseObject(param, Withdrawals.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<Withdrawals> list=withdrawalsService.importDetailSelect(wi);
        try {
            withdrawalsService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出用户提现记录列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "导出用户提现记录列表失败!");
        }
        return msg;
    }
}
