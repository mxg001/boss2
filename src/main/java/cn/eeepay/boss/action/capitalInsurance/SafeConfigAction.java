package cn.eeepay.boss.action.capitalInsurance;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeLadder;
import cn.eeepay.framework.service.capitalInsurance.SafeConfigService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 资金险配置
 */
@Controller
@RequestMapping(value = "/safeConfig")
public class SafeConfigAction {

    private static final Logger log = LoggerFactory.getLogger(SafeConfigAction.class);

    @Resource
    private SafeConfigService safeConfigService;

    /**
     * 查询资金险配置列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SafeConfig> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeConfig safe = JSONObject.parseObject(param, SafeConfig.class);
            safeConfigService.selectAllList(safe, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询资金险配置列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询资金险配置列表异常!");
        }
        return msg;
    }

    /**
     * 获取资金险配置详情
     */
    @RequestMapping(value = "/getSafeConfig")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getSafeConfig(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeConfig safe=safeConfigService.getSafeConfig(id);
            msg.put("safe",safe);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取资金险配置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取资金险配置详情异常!");
        }
        return msg;
    }

    /**
     *  修改资金损失险配置
     */
    @RequestMapping(value = "/saveSafeConfig")
    @ResponseBody
    @SystemLog(description = "修改资金损失险配置",operCode="safeConfig.saveSafeConfig")
    public Map<String,Object> saveSafeConfig(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeConfig safe = JSONObject.parseObject(param, SafeConfig.class);

            int num=safeConfigService.saveSafeConfig(safe);
            if(num>0){
                msg.put("msg","修改成功!");
                msg.put("status", true);
            }else{
                msg.put("msg","修改失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("修改资金损失险配置异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改资金损失险配置异常!");
        }
        return msg;
    }

    /**
     *  修改资金损失险配置
     */
    @RequestMapping(value = "/saveRouteScale")
    @ResponseBody
    public Map<String,Object> saveRouteScale(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            msg.put("msg","修改失败!");
            msg.put("status", false);
            List<JSONObject> jsSafes = JSONObject.parseObject(param, List.class);
            if(jsSafes!=null&&!jsSafes.isEmpty()){
                List<SafeConfig> safes=new ArrayList<>();
                BigDecimal allRouteScale=BigDecimal.ZERO;
                for(JSONObject json:jsSafes){
                    SafeConfig safe = JSONObject.parseObject(json.toJSONString(), SafeConfig.class);
                    safes.add(safe);
                    BigDecimal routeScale=new BigDecimal(safe.getNewRouteScale());
                    allRouteScale=allRouteScale.add(routeScale);
                }
                if(allRouteScale.compareTo(new BigDecimal("100.00"))==0){
                    int num=safeConfigService.saveRouteScale(safes);
                    if(num>0){
                        msg.put("msg","修改成功!");
                        msg.put("status", true);
                    }
                }else{
                    msg.put("msg","请控制路由比例总和为100%");
                    msg.put("status", false);
                }



            }

        } catch (Exception e){
            log.error("修改资金损失险通道路由比例异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改资金损失险通道路由比例异常!");
        }
        return msg;
    }

    /**
     * 保存资金险阶梯
     */
    @RequestMapping(value = "/saveSafeLadder")
    @ResponseBody
    @SystemLog(description = "保存资金险阶梯",operCode="safeConfig.saveSafeLadder")
    public Map<String,Object> saveSafeLadder(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeLadder ladder = JSONObject.parseObject(param, SafeLadder.class);
            int num=safeConfigService.saveSafeLadder(ladder);
            if(num>0){
                msg.put("msg","保存资金险阶梯成功!");
                msg.put("status", true);
            }else{
                msg.put("msg","保存资金险阶梯失败!");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("保存资金险阶梯异常!",e);
            msg.put("status", false);
            msg.put("msg", "保存资金险阶梯异常!");
        }
        return msg;
    }

    /**
     * 获取资金险配置列表
     */
    @RequestMapping(value = "/getSafeConfigList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getSafeConfigList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<SafeConfig> list=safeConfigService.getSafeConfigList();
            msg.put("status", true);
            msg.put("list", list);
        } catch (Exception e){
            log.error("获取资金险配置列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取资金险配置列表异常!");
        }
        return msg;
    }
}
