package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateBanner;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateBannerService;
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
@RequestMapping(value = "/exchangeActivateBanner")
public class ExchangeActivateBannerAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateBannerAction.class);

    @Resource
    private ExchangeActivateBannerService exchangeActivateBannerService;

    /**
     * 查询广告列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateBanner> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateBanner banner = JSONObject.parseObject(param, ExchangeActivateBanner.class);
            exchangeActivateBannerService.selectAllList(banner, page);
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
    @SystemLog(description = "新增广告",operCode="exchangeActivateBanner.addBanner")
    public Map<String,Object> addBanner(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateBanner banner = JSONObject.parseObject(param, ExchangeActivateBanner.class);
            int num=exchangeActivateBannerService.addBanner(banner);
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
            ExchangeActivateBanner banner=exchangeActivateBannerService.getBanner(id);
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
    @SystemLog(description = "修改广告",operCode="exchangeActivateBanner.updateBanner")
    public Map<String,Object> updateBanner(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateBanner banner = JSONObject.parseObject(param, ExchangeActivateBanner.class);
            int num=exchangeActivateBannerService.updateBanner(banner);
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
    @SystemLog(description = "删除广告",operCode="exchangeActivateBanner.deleteBanner")
    public Map<String,Object> deleteBanner(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeActivateBannerService.deleteBanner(id);
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
    @SystemLog(description = "开启/关闭广告",operCode="exchangeActivateBanner.closeBanner")
    public Map<String,Object> closeBanner(@RequestParam("id") long id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeActivateBannerService.closeBanner(id,state);
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
