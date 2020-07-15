package cn.eeepay.boss.action.luckDraw;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.luckDraw.Prize;
import cn.eeepay.framework.model.luckDraw.PrizeBlacklist;
import cn.eeepay.framework.model.luckDraw.PrizeConfigure;
import cn.eeepay.framework.service.luckDraw.PrizeConfigureService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/5/005.
 * @author  liuks
 * 抽奖配置
 */
@Controller
@RequestMapping(value = "/prizeConfigure")
public class PrizeConfigureAction {

    private static final Logger log = LoggerFactory.getLogger(PrizeConfigureAction.class);

    @Resource
    private PrizeConfigureService prizeConfigureService;

    /**
     * 查询抽奖配置详情
     */
    @RequestMapping(value = "/getPrizeConfigure")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getPrizeConfigure(@RequestParam("funcCode") String funcCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeConfigure info =prizeConfigureService.getPrizeConfigure(funcCode);
            msg.put("info",info);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询抽奖配置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询抽奖配置详情异常!");
        }
        return msg;
    }

    /**
     * 获取奖项列表
     */
    @RequestMapping(value = "/getPrizeList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getPrizeList(@RequestParam("funcCode") String funcCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<Prize> list =prizeConfigureService.getPrizeList(funcCode);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取奖项列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取奖项列表异常!");
        }
        return msg;
    }

    /**
     * 修改抽奖配置
     */
    @RequestMapping(value = "/updatePrizeConfigure")
    @ResponseBody
    @SystemLog(description = "修改抽奖配置",operCode="prizeConfigure.updatePrizeConfigure")
    public Map<String,Object> updatePrizeConfigure(@RequestParam("info") String param,
                                                   @RequestParam("funcDesc") String funcDesc) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeConfigure pc = JSONObject.parseObject(param, PrizeConfigure.class);
            pc.setFuncDesc(funcDesc);
            int num=prizeConfigureService.updatePrizeConfigure(pc);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改抽奖配置成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改抽奖配置失败!");
            }
        } catch (Exception e){
            log.error("查询抽奖配置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询抽奖配置详情异常!");
        }
        return msg;
    }

    /**
     * 获取奖项里详情
     */
    @RequestMapping(value = "/getPrize")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getPrize(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Prize prize =prizeConfigureService.getPrize(id);
            msg.put("prize",prize);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取奖项里详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取奖项里详情异常!");
        }
        return msg;
    }
    /**
     * 新增奖项
     */
    @RequestMapping(value = "/addPrize")
    @ResponseBody
    @SystemLog(description = "新增奖项",operCode="prizeConfigure.addPrize")
    public Map<String,Object> addPrize(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Prize prize= JSONObject.parseObject(param, Prize.class);
            prizeConfigureService.addPrize(prize,msg);
        } catch (Exception e){
            log.error("新增奖项异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增奖项异常!");
        }
        return msg;
    }
    /**
     * 修改奖项
     */
    @RequestMapping(value = "/updatePrize")
    @ResponseBody
    @SystemLog(description = "修改奖项",operCode="prizeConfigure.updatePrize")
    public Map<String,Object> updatePrize(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Prize prize= JSONObject.parseObject(param, Prize.class);
            prizeConfigureService.updatePrize(prize,msg);
        } catch (Exception e){
            log.error("修改奖项异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改奖项异常!");
        }
        return msg;
    }

    /**
     * 删除奖项
     */
    @RequestMapping(value = "/deletePrize")
    @ResponseBody
    @SystemLog(description = "删除奖项",operCode="prizeConfigure.deletePrize")
    public Map<String,Object> deletePrize(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =prizeConfigureService.closeOpenPrize(id,2);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除奖项成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除奖项失败!");
            }
        } catch (Exception e){
            log.error("获取奖项里详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取奖项里详情异常!");
        }
        return msg;
    }

    /**
     * 开启/关闭奖项
     */
    @RequestMapping(value = "/closeOpenPrize")
    @ResponseBody
    @SystemLog(description = "开启/关闭奖项",operCode="prizeConfigure.closeOpenPrize")
    public Map<String,Object> closeOpenPrize(@RequestParam("id") int id,@RequestParam("status") int status) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num =prizeConfigureService.closeOpenPrize(id,status);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("开启/关闭奖项异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭奖项异常!");
        }
        return msg;
    }

    /**
     * 获取卷列表
     */
    @RequestMapping(value = "/getCouponList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getCouponList(@RequestParam("funcCode") String funcCode) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<CouponActivityEntity> list=prizeConfigureService.getCouponList(funcCode);
            msg.put("status", true);
            msg.put("list",list);
        } catch (Exception e){
            log.error("获取卷列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取卷列表异常!");
        }
        return msg;
    }

    /**
     * 获取奖项列表黑名单
     */
    @RequestMapping(value = "/getBlacklist")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getPrizeBlacklist(@RequestParam("info") String param,
                                                @ModelAttribute("page") Page<PrizeBlacklist> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeBlacklist blacklist=JSONObject.parseObject(param, PrizeBlacklist.class);
            prizeConfigureService.getPrizeBlacklist(blacklist,page);
            msg.put("status", true);
            msg.put("page",page);
        } catch (Exception e){
            log.error("获取奖项列表黑名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取奖项列表黑名单异常!");
        }
        return msg;
    }
    //新增黑名单
    @RequestMapping(value = "/addBlacklist")
    @ResponseBody
    @SystemLog(description = "新增奖项黑名单",operCode="prizeConfigure.addBlacklist")
    public Map<String,Object> addBlacklist(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            PrizeBlacklist blacklist=JSONObject.parseObject(param, PrizeBlacklist.class);
            prizeConfigureService.addPrizeBlacklist(blacklist,msg);
        } catch (Exception e){
            log.error("新增黑名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增黑名单异常!");
        }
        return msg;
    }

    //删除黑名单
    @RequestMapping(value = "/deleteBlacklist")
    @ResponseBody
    @SystemLog(description = "删除奖项黑名单",operCode="prizeConfigure.deleteBlacklist")
    public Map<String,Object> deleteBlacklist(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=prizeConfigureService.deleteBlacklist(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg","删除黑名单成功");
            }else{
                msg.put("status", false);
                msg.put("msg","删除黑名单失败");
            }
        } catch (Exception e){
            log.error("删除黑名单异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除黑名单异常!");
        }
        return msg;
    }

    //获取商户
    @RequestMapping(value = "/getMerchantInfo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getMerchantInfo(@RequestParam("merchantNo") String merchantNo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MerchantInfo info=prizeConfigureService.getMerchantInfo(merchantNo);
            msg.put("status", true);
            msg.put("info",info);
        } catch (Exception e){
            log.error("获取商户异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取商户异常!");
        }
        return msg;
    }
}
