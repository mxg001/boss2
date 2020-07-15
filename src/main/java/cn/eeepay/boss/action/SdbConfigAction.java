package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.service.SdbConfigService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 盛代宝配置
 */
@Controller
@RequestMapping(value = "/sdbConfig")
public class SdbConfigAction {

    private static final Logger log = LoggerFactory.getLogger(SdbConfigAction.class);

    @Resource
    private SdbConfigService sdbConfigService;

    /**
     * 盛代宝配置
     */
    @RequestMapping(value = "/getSdbConfig")
    @ResponseBody
    public Map<String, Object> getSdbConfig(@RequestParam("team_id") int team_id) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map sdbConfig = sdbConfigService.getSdbConfig(team_id);
            String imgUrls[]={"","",""};
            String imgs[]={"","",""};
            if(StringUtil.isNotBlank(sdbConfig)){
                String detailImages = sdbConfig.get("team_ad_url").toString();
                if(StringUtils.isNotBlank(detailImages)){
                    String[] strs = detailImages.split(",");
                    for (int i=0;i<strs.length;i++) {
                        if(i>=3){
                            break;
                        }
                        String imgUrl = CommonUtil.getImgUrlAgent(strs[i]);
                        imgUrls[i]=imgUrl;
                        imgs[i]=strs[i];
                    }
                }
            }
            msg.put("img", imgs[0]);
            msg.put("img2", imgs[1]);
            msg.put("img3", imgs[2]);
            msg.put("imgUrl", imgUrls[0]);
            msg.put("imgUrl2", imgUrls[1]);
            msg.put("imgUrl3", imgUrls[2]);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("盛代宝配置异常!", e);
            msg.put("status", false);
            msg.put("msg", "盛代宝配置异常!");
        }
        return msg;
    }

    /**
     * 盛代宝配置图片删除
     */
    @RequestMapping(value = "/deleteSdbConfigImg")
    @ResponseBody
    @SystemLog(description = "盛代宝配置图片删除", operCode = "sdbConfig.deleteSdbConfigImg")
    public Map<String, Object> deleteSdbConfigImg(@RequestParam("team_id") int team_id, @RequestParam("status") int status) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map sdbConfig = sdbConfigService.getSdbConfig(team_id);
            String team_ad_url="";
            String detailImages = sdbConfig.get("team_ad_url").toString();
            if(StringUtils.isNotBlank(detailImages)){
                String[] imgs = detailImages.split(",");
                for (int i=0;i<imgs.length;i++) {
                    if(i+1==status){
                        imgs[i]="";
                    }
                    if(!imgs[i].equals("")){
                        team_ad_url+=imgs[i]+",";
                    }
                }
                if(!team_ad_url.equals("")){
                    team_ad_url=team_ad_url.substring(0,team_ad_url.length()-1);
                }
            }
            sdbConfigService.updateSdbConfigImg(team_id, team_ad_url);
            msg.put("msg", "删除成功!");
            msg.put("status", true);
        } catch (Exception e) {
            log.error("图片删除异常!", e);
            msg.put("status", false);
            msg.put("msg", "图片删除异常!");
        }
        return msg;
    }

    /**
     * 图片修改
     */
    @RequestMapping(value = "/saveSdbConfigImg")
    @ResponseBody
    @SystemLog(description = "盛代宝配置图片修改", operCode = "sdbConfig.saveSdbConfigImg")
    public Map<String, Object> saveSdbConfigImg(@RequestParam("team_id") int team_id, @RequestParam("team_ad_url") String team_ad_url) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map sdbConfig = sdbConfigService.getSdbConfig(team_id);
            if(StringUtil.isNotBlank(sdbConfig)){
                sdbConfigService.updateSdbConfigImg(team_id,team_ad_url);
            }else{
                sdbConfigService.addSdbConfig(team_id,team_ad_url);
            }
            msg.put("msg", "保存成功!");
            msg.put("status", true);
        } catch (Exception e) {
            log.error("保存异常!", e);
            msg.put("status", false);
            msg.put("msg", "保存异常!");
        }
        return msg;
    }

}
