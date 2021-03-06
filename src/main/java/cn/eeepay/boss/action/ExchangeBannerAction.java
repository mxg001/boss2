package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeBanner;
import cn.eeepay.framework.service.ExchangeBannerService;
import cn.eeepay.framework.util.CommonUtil;
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
 * 超级兑广告
 */
@Controller
@RequestMapping(value = "/exchangeBanner")
public class ExchangeBannerAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeBannerAction.class);

    @Resource
    private ExchangeBannerService exchangeBannerService;

    /**
     * 查询广告列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeBanner> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeBanner banner = JSONObject.parseObject(param, ExchangeBanner.class);
            exchangeBannerService.selectAllList(banner, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询广告列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询广告列表异常!");
        }
        return msg;
    }

    /**
     * 新增广告
     */
    @RequestMapping(value = "/addBanner")
    @ResponseBody
    @SystemLog(description = "新增广告",operCode="exchangeBanner.addBanner")
    public Map<String,Object> addBanner(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeBanner banner = JSONObject.parseObject(param, ExchangeBanner.class);
            int num=exchangeBannerService.addBanner(banner);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增广告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增广告失败!");
            }
        } catch (Exception e){
            log.error("新增广告异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增广告异常!");
        }
        return msg;
    }

    /**
     * 查询广告详情
     */
    @RequestMapping(value = "/getBanner")
    @ResponseBody
    public Map<String,Object> getBanner(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeBanner banner=exchangeBannerService.getBanner(id);
            if(banner.getImgUrl()!=null&&!"".equals(banner.getImgUrl())){
                banner.setImgUrl(CommonUtil.getImgUrlAgent(banner.getImgUrl()));
            }
            msg.put("banner",banner);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询广告详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询广告详情异常!");
        }
        return msg;
    }

    /**
     * 修改广告
     */
    @RequestMapping(value = "/updateBanner")
    @ResponseBody
    @SystemLog(description = "修改广告",operCode="exchangeBanner.updateBanner")
    public Map<String,Object> updateBanner(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeBanner banner = JSONObject.parseObject(param, ExchangeBanner.class);
            int num=exchangeBannerService.updateBanner(banner);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改广告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改广告失败!");
            }
        } catch (Exception e){
            log.error("修改广告异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改广告异常!");
        }
        return msg;
    }

    /**
     * 删除广告
     */
    @RequestMapping(value = "/deleteBanner")
    @ResponseBody
    @SystemLog(description = "删除广告",operCode="exchangeBanner.deleteBanner")
    public Map<String,Object> deleteBanner(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeBannerService.deleteBanner(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除广告成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除广告失败!");
            }
        } catch (Exception e){
            log.error("删除广告异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除广告异常!");
        }
        return msg;
    }

    /**
     * 开启/关闭广告
     */
    @RequestMapping(value = "/closeBanner")
    @ResponseBody
    @SystemLog(description = "开启/关闭广告",operCode="exchangeBanner.closeBanner")
    public Map<String,Object> closeBanner(@RequestParam("id") long id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeBannerService.closeBanner(id,state);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("开启/关闭广告异常!",e);
            msg.put("status", false);
            msg.put("msg", "关闭广告异常!");
        }
        return msg;
    }
}
