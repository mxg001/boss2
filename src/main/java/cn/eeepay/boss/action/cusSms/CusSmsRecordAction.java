package cn.eeepay.boss.action.cusSms;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsRecord;
import cn.eeepay.framework.service.cusSms.CusSmsRecordService;
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
import java.util.Map;

/**
 * @author liuks
 * 短信发送记录
 *
 */
@Controller
@RequestMapping(value = "/cusSmsRecord")
public class CusSmsRecordAction {

    private static final Logger log = LoggerFactory.getLogger(CusSmsRecordAction.class);

    @Resource
    private CusSmsRecordService cusSmsRecordService;

    /**
     * 查询短信发送列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page") Page<CusSmsRecord> page){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsRecord info = JSONObject.parseObject(param, CusSmsRecord.class);
            cusSmsRecordService.selectAllList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询短信发送列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询短信发送列表异常!");
        }
        return msg;
    }

    //屏蔽
    @RequestMapping(value = "/getCusSmsRecordInfo")
    @ResponseBody
    public Map<String,Object> getCusSmsRecordInfo(@RequestParam("id") int id){
        return getCusSmsRecordInfo(id,1);
    }
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam("id") int id){
        return getCusSmsRecordInfo(id,0);
    }
    private Map<String, Object> getCusSmsRecordInfo(int id,int sta){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CusSmsRecord info =  cusSmsRecordService.getCusSmsRecordInfo(id,sta);
            msg.put("info",info);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询短信发送实体异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询短信发送实体异常!");
        }
        return msg;
    }

    /**
     * 导出发送记录列表
     */
    @RequestMapping(value="/exportInfo")
    @ResponseBody
    @SystemLog(description = "导出发送记录列表", operCode = "cusSmsRecord.exportInfo")
    public Map<String, Object> exportInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            CusSmsRecord info = JSONObject.parseObject(param, CusSmsRecord.class);
            cusSmsRecordService.exportInfo(info,response);
        }catch (Exception e){
            log.error("导出发送记录列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出发送记录列表异常!");
        }
        return msg;
    }
}
