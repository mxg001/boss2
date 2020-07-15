package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteGoodService;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteService;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 超级兑路由
 */
@Controller
@RequestMapping(value = "/excActRoute")
public class ExcActRouteAction {

    private static final Logger log = LoggerFactory.getLogger(ExcActRouteAction.class);

    @Resource
    private ExcActRouteService excActRouteService;

    @Resource
    private ExcActRouteGoodService excActRouteGoodService;


    /**
     * 查询路由列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExcActRoute> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRoute route = JSONObject.parseObject(param, ExcActRoute.class);
            excActRouteService.selectAllList(route, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询路由列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询路由列表异常!");
        }
        return msg;
    }

    /**
     * 新增核销渠道
     */
    @RequestMapping(value = "/addRoute")
    @ResponseBody
    @SystemLog(description = "新增核销渠道",operCode="excActRoute.addRoute")
    public Map<String,Object> addRoute(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRoute route = JSONObject.parseObject(param, ExcActRoute.class);
            if(excActRouteService.checkRoute(route)){
                int num=excActRouteService.addRoute(route);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "新增核销渠道成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增核销渠道失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "该核销渠道编号已存在!");
            }
        } catch (Exception e){
            log.error("新增核销渠道异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增核销渠道异常!");
        }
        return msg;
    }

    /**
     * 查询核销渠道详情
     */
    @RequestMapping(value = "/getRoute")
    @ResponseBody
    public Map<String,Object> getRoute(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRoute route =excActRouteService.getRoute(id);
            msg.put("info",route);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询核销渠道详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询核销渠道详情异常!");
        }
        return msg;
    }

    /**
     * 修改核销渠道
     */
    @RequestMapping(value = "/updateRoute")
    @ResponseBody
    @SystemLog(description = "修改核销渠道",operCode="excActRoute.updateRoute")
    public Map<String,Object> updateRoute(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRoute route = JSONObject.parseObject(param, ExcActRoute.class);
            if(excActRouteService.checkRoute(route)){
                int num=excActRouteService.updateRoute(route);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "修改核销渠道成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "修改核销渠道失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "该核销渠道编号已存在!");
            }
        } catch (Exception e){
            log.error("修改核销渠道异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改核销渠道异常!");
        }
        return msg;
    }

    /**
     * 开启/关闭路由
     */
    @RequestMapping(value = "/closeRoute")
    @ResponseBody
    @SystemLog(description = "开启/关闭路由",operCode="excActRoute.closeRoute")
    public Map<String,Object> closeRoute(@RequestParam("id") int id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=excActRouteService.closeRoute(id,state);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("开启/关闭路由异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭路由异常!");
        }
        return msg;
    }

    /**
     * 新增核销渠道商品
     */
    @RequestMapping(value = "/addGood")
    @ResponseBody
    @SystemLog(description = "新增核销渠道商品",operCode="excActRoute.addGood")
    public Map<String,Object> addGood(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRouteGood good = JSONObject.parseObject(param, ExcActRouteGood.class);
            if(excActRouteGoodService.checkRoute(good)){
                int num=excActRouteGoodService.addExcActRouteGood(good);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "新增核销渠道商品成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "新增核销渠道商品失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "该核销渠道下该对应商品ID已存在!");
            }
        } catch (Exception e){
            log.error("新增核销渠道异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增核销渠道异常!");
        }
        return msg;
    }

    /**
     * 模板下载
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"exaRouteGoodTemplate.xls";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"批量导入商品模板.xlsx");
        return null;
    }
    /**
     * 批量导入
     */
    @RequestMapping(value="/importDiscount")
    @ResponseBody
    public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file){
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "导入文件格式错误!");
                    return msg;
                }
            }
            msg = excActRouteService.importDiscount(file);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导入失败!");
            log.error("导入失败!",e);
        }
        return msg;
    }

    /**
     * 获取核销渠道List
     */
    @RequestMapping(value = "/getRouteALLList")
    @ResponseBody
    public Map<String,Object> getRouteALLList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExcActRoute> list =excActRouteService.getRouteALLList();
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询核销渠道详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询核销渠道详情异常!");
        }
        return msg;
    }
}
