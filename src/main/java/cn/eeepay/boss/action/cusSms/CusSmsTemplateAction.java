package cn.eeepay.boss.action.cusSms;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsTemplate;
import cn.eeepay.framework.model.cusSms.SendResult;
import cn.eeepay.framework.service.cusSms.CusSmsTemplateService;
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
 * @author  liuks
 * 投诉下发验证码-短信模板
 */
@Controller
@RequestMapping(value = "/cusSmsTemplate")
public class CusSmsTemplateAction {

    private static final Logger log = LoggerFactory.getLogger(CusSmsTemplateAction.class);

    @Resource
    private CusSmsTemplateService cusSmsTemplateService;
    /**
     * 查询短信模板列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page") Page<CusSmsTemplate> page){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsTemplate info = JSONObject.parseObject(param, CusSmsTemplate.class);
            cusSmsTemplateService.selectAllList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询短信模板列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询短信模板列表异常!");
        }
        return msg;
    }

    /**
     * 新增短信模板
     */
    @RequestMapping(value = "/addSmsTemplate")
    @ResponseBody
    @SystemLog(description = "新增短信模板", operCode = "cusSmsTemplate.addSmsTemplate")
    public Map<String,Object> addSmsTemplate(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsTemplate info = JSONObject.parseObject(param, CusSmsTemplate.class);
            int num=cusSmsTemplateService.addSmsTemplate(info);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增短信模板成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增短信模板失败!");
            }
        } catch (Exception e){
            log.error("新增短信模板异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增短信模板异常!");
        }
        return msg;
    }
    /**
     * 修改短信模板
     */
    @RequestMapping(value = "/editSmsTemplate")
    @ResponseBody
    @SystemLog(description = "修改短信模板", operCode = "cusSmsTemplate.editSmsTemplate")
    public Map<String,Object> editSmsTemplate(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsTemplate info = JSONObject.parseObject(param, CusSmsTemplate.class);
            int num=cusSmsTemplateService.editSmsTemplate(info);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改短信模板成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改短信模板失败!");
            }
        } catch (Exception e){
            log.error("修改短信模板异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改短信模板异常!");
        }
        return msg;
    }
    /**
     * 删除短信模板
     */
    @RequestMapping(value = "/deleteSmsTemplate")
    @ResponseBody
    @SystemLog(description = "删除短信模板", operCode = "cusSmsTemplate.deleteSmsTemplate")
    public Map<String,Object> deleteSmsTemplate(@RequestParam("id") int id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=cusSmsTemplateService.deleteSmsTemplate(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除短信模板成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除短信模板失败!");
            }
        } catch (Exception e){
            log.error("删除短信模板异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除短信模板异常!");
        }
        return msg;
    }

    /**
     * 获取短信模板
     */
    @RequestMapping(value = "/getSmsTemplateInfo")
    @ResponseBody
    public Map<String,Object> getSmsTemplateInfo(@RequestParam("id") int id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsTemplate info =cusSmsTemplateService.getSmsTemplateInfo(id);
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e){
            log.error("获取短信模板异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取短信模板异常!");
        }
        return msg;
    }

    /**
     * 短信模板发送
     */
    @RequestMapping(value = "/sendTemplate")
    @ResponseBody
    @SystemLog(description = "短信模板发送", operCode = "cusSmsTemplate.sendTemplate")
    public Map<String,Object> sendTemplate(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SendResult info = JSONObject.parseObject(param, SendResult.class);
            cusSmsTemplateService.sendTemplate(info,msg);
        } catch (Exception e){
            log.error("短信模板发送异常!",e);
            msg.put("status", false);
            msg.put("msg", "短信模板发送异常!");
        }
        return msg;
    }
}
