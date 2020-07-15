package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActProductMedia;
import cn.eeepay.framework.service.exchangeActivate.ExcActProductMediaService;
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
 * Created by Administrator on 2018/10/11/011.
 * @author  liuks
 * 路由商品媒体资源
 */
@Controller
@RequestMapping(value = "/excActProductMedia")
public class ExcActProductMediaAction {

    private static final Logger log = LoggerFactory.getLogger(ExcActProductMediaAction.class);

    @Resource
    private  ExcActProductMediaService excActProductMediaService;

    /**
     * 查询商品媒体资源列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExcActProductMedia> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActProductMedia media = JSONObject.parseObject(param, ExcActProductMedia.class);
            excActProductMediaService.selectAllList(media, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询商品媒体资源列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询商品媒体资源列表异常!");
        }
        return msg;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/deleteMedia")
    @ResponseBody
    @SystemLog(description = "删除",operCode="excActProductMedia.deleteMedia")
    public Map<String,Object> deleteMedia(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=excActProductMediaService.deleteMedia(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除失败!");
            }
        } catch (Exception e){
            log.error("删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除异常!");
        }
        return msg;
    }

    /**
     * 下载文档
     */
    @RequestMapping(value="/downloadMedia")
    @ResponseBody
    @SystemLog(description = "下载文档",operCode="excActProductMedia.downloadMedia")
    public Map<String, Object> downloadMedia(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();

        ExcActProductMedia media =excActProductMediaService.getProductMedia(id);
        if(media.getGoodFile()!=null){
            String name=media.getGoodFile();
            String reName="商品媒体资源["+media.getId()+"]";
            excActProductMediaService.downloadFile(name,reName,response,msg);
        }else{
            msg.put("status", false);
            msg.put("msg", "下载文件不存在!");
        }
        return msg;
    }

    /**
     * 上游下载资源
     */
    @RequestMapping(value = "/addProductMedia")
    @ResponseBody
    @SystemLog(description = "上游下载资源",operCode="excActProductMedia.addProductMedia")
    public Map<String,Object> addProductMedia(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActProductMedia media = JSONObject.parseObject(param, ExcActProductMedia.class);
            excActProductMediaService.addExcActProductMedia(media,msg);
        } catch (Exception e){
            log.error("下载异常!",e);
            msg.put("status", false);
            msg.put("msg", "下载异常!");
        }
        return msg;
    }
}
