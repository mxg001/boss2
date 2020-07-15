package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedEnvelopesReceive;
import cn.eeepay.framework.service.RedEnvelopesReceiveService;
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

import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2018/1/18/018.
 * 红包领取查询 Action
 * @author liuks
 */
@Controller
@RequestMapping(value = "/redEnvelopesReceive")
public class RedEnvelopesReceiveAction {

    private static final Logger log = LoggerFactory.getLogger(RedEnvelopesReceiveAction.class);

    @Resource
    private RedEnvelopesReceiveService redEnvelopesReceiveService;

    /**
     * 红包领取查询列表
     */
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<RedEnvelopesReceive> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RedEnvelopesReceive order = JSONObject.parseObject(param, RedEnvelopesReceive.class);
            order.setSumState(0);
            redEnvelopesReceiveService.selectAllByParam(order, page);
            order.setSumState(1);
            RedEnvelopesReceive sunOrder= redEnvelopesReceiveService.sumCount(order);
            msg.put("page",page);
            msg.put("sunOrder",sunOrder);
            msg.put("status",true);
        } catch (Exception e){
            log.error("红包领取查询列表失败!",e);
            msg.put("msg","红包领取查询列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 红包领取记录查询
     */
    @RequestMapping(value = "/selectRedEnvelopesReceive")
    @ResponseBody
    public Map<String, Object> selectRedEnvelopesReceive(@RequestParam("id") Long id, @ModelAttribute("page")
            Page<RedEnvelopesReceive> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            redEnvelopesReceiveService.selectRedEnvelopesReceive(id, page);
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("红包领取记录查询!",e);
            msg.put("msg","红包领取记录查询!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 红包领取查询列表导出
     */
    @RequestMapping(value="/exportInfo")
    @ResponseBody
    public void exportInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        try {
        	param = URLDecoder.decode(param,"UTF-8");
        	
            RedEnvelopesReceive order = JSONObject.parseObject(param, RedEnvelopesReceive.class);
            order.setSumState(0);
            List<RedEnvelopesReceive> list=redEnvelopesReceiveService.exportInfo(order);
            redEnvelopesReceiveService.exportRedEnvelopesReceive(list,response);
        } catch (Exception e){
            log.error("红包领取查询列表导出异常", e);
        }
    }

}
