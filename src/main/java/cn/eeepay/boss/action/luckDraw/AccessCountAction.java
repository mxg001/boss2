package cn.eeepay.boss.action.luckDraw;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.AccessCount;
import cn.eeepay.framework.service.luckDraw.AccessCountService;
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
 * Created by Administrator on 2018/11/7/007.
 * @author liuks
 * 访问抽奖界面统计
 */
@Controller
@RequestMapping(value="/accessCount")
public class AccessCountAction {

    private static final Logger log = LoggerFactory.getLogger(AccessCountAction.class);


    @Resource
    private AccessCountService accessCountService;

    /**
     * 查询访问统计列表
     */
    @RequestMapping(value = "/selectAllList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectAllList(@ModelAttribute("page") Page<AccessCount> page,
                                            @RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AccessCount acc = JSON.parseObject(param, AccessCount.class);
            List<AccessCount> list=accessCountService.selectAllList(acc,page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询访问统计列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询访问统计列表异常!");
        }
        return msg;
    }

    /**
     * 导出访问统计列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            AccessCount acc = JSONObject.parseObject(param, AccessCount.class);
            List<AccessCount> list=accessCountService.importDetailSelect(acc);
            accessCountService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出访问统计列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出访问统计列表异常!");
        }
        return msg;
    }
}
