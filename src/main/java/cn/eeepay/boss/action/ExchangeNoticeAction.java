package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeNotice;
import cn.eeepay.framework.service.ExchangeNoticeService;
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
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 超级兑公告
 */
@Controller
@RequestMapping(value = "/exchangeNotice")
public class ExchangeNoticeAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeNoticeAction.class);

    @Resource
    private ExchangeNoticeService exchangeNoticeService;

    /**
     * 查询公告列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeNotice> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeNotice notice = JSONObject.parseObject(param, ExchangeNotice.class);
            exchangeNoticeService.selectAllList(notice, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询公告列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询公告列表异常!");
        }
        return msg;
    }

    /**
     * 新增公告
     */
    @RequestMapping(value = "/addNotice")
    @ResponseBody
    @SystemLog(description = "新增公告",operCode="exchangeNotice.addNotice")
    public Map<String,Object> addNotice(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeNotice notice = JSONObject.parseObject(param, ExchangeNotice.class);
            int num=exchangeNoticeService.addNotice(notice);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增公告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增公告失败!");
            }
        } catch (Exception e){
            log.error("新增公告异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增公告异常!");
        }
        return msg;
    }

    /**
     * 查询公告详情
     */
    @RequestMapping(value = "/getNotice")
    @ResponseBody
    public Map<String,Object> getNotice(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeNotice notice=exchangeNoticeService.getNotice(id);
            msg.put("notice",notice);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询公告详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询公告详情异常!");
        }
        return msg;
    }

    /**
     * 修改公告
     */
    @RequestMapping(value = "/updateNotice")
    @ResponseBody
    @SystemLog(description = "修改公告",operCode="exchangeNotice.updateNotice")
    public Map<String,Object> updateNotice(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeNotice notice = JSONObject.parseObject(param, ExchangeNotice.class);
            int num=exchangeNoticeService.updateNotice(notice);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改公告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改公告失败!");
            }
        } catch (Exception e){
            log.error("修改公告异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改公告异常!");
        }
        return msg;
    }

    /**
     * 删除公告
     */
    @RequestMapping(value = "/deleteNotice")
    @ResponseBody
    @SystemLog(description = "删除公告",operCode="exchangeNotice.deleteNotice")
    public Map<String,Object> deleteNotice(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeNoticeService.updateNoticeState(id,0);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除公告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除公告失败!");
            }
        } catch (Exception e){
            log.error("删除公告异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除公告异常!");
        }
        return msg;
    }

    /**
     * 更新公告状态
     */
    @RequestMapping(value = "/updateNoticeState")
    @ResponseBody
    @SystemLog(description = "更新公告状态",operCode="exchangeNotice.updateNoticeState")
    public Map<String,Object> updateNoticeState(@RequestParam("id") long id,@RequestParam("state")int state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeNoticeService.updateNoticeState(id,state);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("更新公告状态异常!",e);
            msg.put("status", false);
            msg.put("msg", "更新公告状态异常!");
        }
        return msg;
    }
}
