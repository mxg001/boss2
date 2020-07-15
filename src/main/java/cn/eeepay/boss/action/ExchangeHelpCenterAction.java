package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.HelpCenter;
import cn.eeepay.framework.service.ExchangeHelpCenterService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 超级兑 帮助中心管理
 */
@Controller
@RequestMapping(value = "/exchangeHelpCenter")
public class ExchangeHelpCenterAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeNoticeAction.class);

    @Resource
    private ExchangeHelpCenterService exchangeHelpCenterService;
    /**
     * 查询帮助中心列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<HelpCenter> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            HelpCenter help = JSONObject.parseObject(param, HelpCenter.class);
            exchangeHelpCenterService.selectAllList(help, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询帮助中心列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询帮助中心列表异常!");
        }
        return msg;
    }

    /**
     * 新增帮助中心
     */
    @RequestMapping(value = "/addHelpCenter")
    @ResponseBody
    @SystemLog(description = "新增公告",operCode="exchangeHelpCenter.addHelpCenter")
    public Map<String,Object> addHelpCenter(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            HelpCenter help = JSONObject.parseObject(param, HelpCenter.class);
            int num=exchangeHelpCenterService.addHelpCenter(help);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增帮助中心成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增帮助中心失败!");
            }
        } catch (Exception e){
            log.error("新增帮助中心异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增帮助中心异常!");
        }
        return msg;
    }

    /**
     * 查询帮助中心详情
     */
    @RequestMapping(value = "/getHelpCenter")
    @ResponseBody
    public Map<String,Object> getHelpCenter(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            HelpCenter help=exchangeHelpCenterService.getHelpCenter(id);
            msg.put("help",help);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询帮助中心详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询帮助中心详情异常!");
        }
        return msg;
    }

    /**
     * 修改帮助中心
     */
    @RequestMapping(value = "/updateHelpCenter")
    @ResponseBody
    @SystemLog(description = "修改帮助中心",operCode="exchangeHelpCenter.updateHelpCenter")
    public Map<String,Object> updateHelpCenter(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            HelpCenter help = JSONObject.parseObject(param, HelpCenter.class);
            int num=exchangeHelpCenterService.updateHelpCenter(help);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改帮助中心成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改帮助中心失败!");
            }
        } catch (Exception e){
            log.error("修改帮助中心异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改帮助中心异常!");
        }
        return msg;
    }
    /**
     * 删除帮助中心
     */
    @RequestMapping(value = "/deleteHelpCenter")
    @ResponseBody
    @SystemLog(description = "删除帮助中心",operCode="exchangeHelpCenter.deleteHelpCenter")
    public Map<String,Object> deleteHelpCenter(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeHelpCenterService.deleteHelpCenter(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除帮助中心成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除帮助中心失败!");
            }
        } catch (Exception e){
            log.error("删除帮助中心异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除帮助中心异常!");
        }
        return msg;
    }
    /**
     * 开启/关闭帮助中心
     */
    @RequestMapping(value = "/updateHelpCenterState")
    @ResponseBody
    @SystemLog(description = "开启/关闭帮助中心",operCode="exchangeHelpCenter.updateHelpCenterState")
    public Map<String,Object> updateHelpCenterState(@RequestParam("id") long id,
                                                    @RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeHelpCenterService.updateHelpCenterState(id,state);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("开启/关闭帮助中心异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭帮助中心异常!");
        }
        return msg;
    }
}
