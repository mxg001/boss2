package cn.eeepay.boss.action.risk;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.TradeRestrict;
import cn.eeepay.framework.service.risk.TradeRestrictService;
import cn.eeepay.framework.util.Constants;
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
 * Created by Administrator on 2019/5/5/005.
 * @author  liuks
 * 交易限制记录
 */
@Controller
@RequestMapping(value = "/tradeRestrict")
public class TradeRestrictAction {

    private static final Logger log = LoggerFactory.getLogger(TradeRestrictAction.class);

    @Resource
    private TradeRestrictService tradeRestrictService;

    /**
     * 查询交易限制记录列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<TradeRestrict> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TradeRestrict info = JSONObject.parseObject(param, TradeRestrict.class);
            tradeRestrictService.selectAllList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询交易限制记录列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询交易限制记录列表异常!");
        }
        return msg;
    }


    /**
     * 导出交易限制记录列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        TradeRestrict info = JSONObject.parseObject(param, TradeRestrict.class);

        Map<String, Object> msg=new HashMap<String,Object>();
        List<TradeRestrict> list=tradeRestrictService.importDetailSelect(info);
        try {
            tradeRestrictService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出交易限制记录列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出交易限制记录列表异常!");
        }
        return msg;
    }

    /**
     * 批量修改状态
     */
    @RequestMapping(value = "/riskStatusChangeBatch")
    @ResponseBody
    @SystemLog(description = "批量修改状态",operCode="tradeRestrict.riskStatusChangeBatch")
    public Map<String,Object> riskStatusChangeBatch(@RequestParam("ids") String ids,@RequestParam("status") int status) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            tradeRestrictService.riskStatusChangeBatch(ids,status,msg);
        } catch (Exception e){
            log.error("批量修改状态失败!",e);
            msg.put("status", false);
            msg.put("msg", "批量修改状态失败!");
        }
        return msg;
    }

    /**
     * 修改状态
     */
    @RequestMapping(value = "/changeStatus")
    @ResponseBody
    @SystemLog(description = "修改状态",operCode="tradeRestrict.changeStatus")
    public Map<String,Object> changeStatus(@RequestParam("id") int id,@RequestParam("status") int status) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            tradeRestrictService.changeStatus(id,status,msg);
        } catch (Exception e){
            log.error("修改状态失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改状态失败!");
        }
        return msg;
    }
}
