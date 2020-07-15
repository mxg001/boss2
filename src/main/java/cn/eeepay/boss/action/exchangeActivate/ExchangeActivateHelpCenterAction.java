package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateHelpCenter;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateHelpCenterService;
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
@RequestMapping(value = "/exchangeActivateHelpCenter")
public class ExchangeActivateHelpCenterAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateHelpCenterAction.class);

    @Resource
    private ExchangeActivateHelpCenterService exchangeActivateHelpCenterService;
    /**
     * 查询帮助中心列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateHelpCenter> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateHelpCenter help = JSONObject.parseObject(param, ExchangeActivateHelpCenter.class);
            exchangeActivateHelpCenterService.selectAllList(help, page);
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
    @SystemLog(description = "新增公告",operCode="exchangeActivateHelpCenter.addHelpCenter")
    public Map<String,Object> addHelpCenter(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateHelpCenter help = JSONObject.parseObject(param, ExchangeActivateHelpCenter.class);
            int num=exchangeActivateHelpCenterService.addHelpCenter(help);
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
            ExchangeActivateHelpCenter help=exchangeActivateHelpCenterService.getHelpCenter(id);
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
    @SystemLog(description = "修改帮助中心",operCode="exchangeActivateHelpCenter.updateHelpCenter")
    public Map<String,Object> updateHelpCenter(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateHelpCenter help = JSONObject.parseObject(param, ExchangeActivateHelpCenter.class);
            int num=exchangeActivateHelpCenterService.updateHelpCenter(help);
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
    @SystemLog(description = "删除帮助中心",operCode="exchangeActivateHelpCenter.deleteHelpCenter")
    public Map<String,Object> deleteHelpCenter(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeActivateHelpCenterService.deleteHelpCenter(id);
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
    @SystemLog(description = "开启/关闭帮助中心",operCode="exchangeActivateHelpCenter.updateHelpCenterState")
    public Map<String,Object> updateHelpCenterState(@RequestParam("id") long id,
                                                    @RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeActivateHelpCenterService.updateHelpCenterState(id,state);
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
