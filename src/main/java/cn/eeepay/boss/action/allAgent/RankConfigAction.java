package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.allAgent.NoticeAllAgent;
import cn.eeepay.framework.model.allAgent.RankConfig;
import cn.eeepay.framework.service.allAgent.RankConfigService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 * 排行榜设置
 */
@Controller
@RequestMapping(value = "/rankConfig")
public class RankConfigAction {

    private static final Logger log = LoggerFactory.getLogger(RankConfigAction.class);

    @Resource
    private RankConfigService rankConfigService;

    /**
     * 保存排行榜设置
     */
    @RequestMapping(value = "/rankConfigAdd")
    @ResponseBody
    @SystemLog(description = "保存排行榜设置",operCode="rankConfig.rankConfigAdd")
    public Map<String,Object> rankConfigAdd(@RequestParam("info") String param, @RequestParam("content") String content) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RankConfig info = JSONObject.parseObject(param, RankConfig.class);
            if(StringUtils.isNotBlank(info.getRankUrl())){
                info.setRankUrl(URLDecoder.decode(info.getRankUrl(),"utf-8"));
            }
            info.setRankRule(content);
            int num=rankConfigService.saveRankConfig(info);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "保存排行榜设置成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "保存排行榜设置失败!");
            }
        } catch (Exception e){
            log.error("保存排行榜设置异常!",e);
            msg.put("status", false);
            msg.put("msg", "保存排行榜设置异常!");
        }
        return msg;
    }

    /**
     * 获取排行榜设置
     */
    @RequestMapping(value = "/getRankConfig")
    @ResponseBody
    public Map<String,Object> getRankConfig() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RankConfig info=rankConfigService.getRankConfig();
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e){
            log.error("获取排行榜设置异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取排行榜设置异常!");
        }
        return msg;
    }
}
