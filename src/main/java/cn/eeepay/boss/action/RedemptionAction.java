package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExchangeAwardsConfig;
import cn.eeepay.framework.model.ExchangeAwardsRecode;
import cn.eeepay.framework.service.RedemptionService;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.RandomNumber;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/redemption")
public class RedemptionAction {
    private static final Logger log = LoggerFactory.getLogger(RedemptionAction.class);

    @Resource
    private RedemptionService redemptionService;
    /**
     * 兑奖列表查询
     */
    @RequestMapping(value = "/queryRedemptionList")
    @ResponseBody
    public Map<String,Object> queryRedemptionList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeAwardsRecode> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeAwardsRecode info = JSONObject.parseObject(param, ExchangeAwardsRecode.class);
            redemptionService.queryRedemptionList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("兑奖列表查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "兑奖列表查询异常!");
        }
        return msg;
    }

    /**
     * 奖券类型查询
     */
    @RequestMapping(value = "/queryRedemptionManageList")
    @ResponseBody
    public Map<String,Object> queryRedemptionManageList(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeAwardsConfig info = JSONObject.parseObject(param, ExchangeAwardsConfig.class);
            List<ExchangeAwardsConfig> list=redemptionService.queryRedemptionManageList(info);
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("奖券类型查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖券类型查询异常!");
        }
        return msg;
    }

    /**
     * 奖券类型新增
     */
    @RequestMapping(value = "/addRedemptionManage")
    @SystemLog(description = "奖券类型新增", operCode = "redemption.addRedemptionManage")
    public @ResponseBody Map<String, Object> addRedemptionManage(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ExchangeAwardsConfig info = jsonObject.getObject("info", ExchangeAwardsConfig.class);
            redemptionService.addRedemptionManage(info);
            msg.put("status", true);
            msg.put("msg", "奖券类型新增成功");
        } catch (Exception e) {
            log.error("奖券类型新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "奖券类型新增失败");
        }
        return msg;
    }

    /**
     * 奖券类型修改
     */
    @RequestMapping(value = "/updateRedemptionManage")
    @SystemLog(description = "奖券类型修改", operCode = "redemption.updateRedemptionManage")
    public @ResponseBody Map<String, Object> updateRedemptionManage(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ExchangeAwardsConfig info = jsonObject.getObject("info", ExchangeAwardsConfig.class);
            redemptionService.updateRedemptionManage(info);
            msg.put("status", true);
            msg.put("msg", "奖券类型修改成功");
        } catch (Exception e) {
            log.error("奖券类型修改失败！", e);
            msg.put("status", false);
            msg.put("msg", "奖券类型修改失败");
        }
        return msg;
    }

    /**
     * 奖券类型删除
     */
    @RequestMapping(value = "/deleteRedemptionManage")
    @ResponseBody
    @SystemLog(description = "奖券类型删除",operCode="redemption.deleteRedemptionManage")
    public Map<String,Object> deleteRedemptionManage(@RequestParam("id") Integer id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=redemptionService.deleteRedemptionManage(id);
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
     * 新增兑奖码
     */
    @RequestMapping(value = "/addRedemption")
    @SystemLog(description = "新增兑奖码", operCode = "redemption.addRedemption")
    public @ResponseBody Map<String, Object> addRedemption(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        int num=0;
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ExchangeAwardsRecode info = jsonObject.getObject("info", ExchangeAwardsRecode.class);
            ExchangeAwardsConfig config=redemptionService.queryRedemptionManageById(info.getAwardsConfigId());
            info.setAwardDesc(config.getAwardDesc());
            info.setAwardName(config.getAwardName());
            info.setAwardType(config.getAwardType());
            Date startDate=new Date();
            // 获得系统日期的前一天
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(startDate);// 把当前时间赋给日历
            info.setOffBeginTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, (info.getDay()-1));
            Date endDate = calendar.getTime();
            info.setOffEndTime(endDate);
            for(int i=0;i<info.getNum();i++){
                info.setExcCode((Md5.md5Str(UUID.randomUUID().toString()).substring(0,9)+RandomNumber.mumberRandom("",1,4)).toUpperCase());
                num+=redemptionService.addRedemption(info);
            }
            msg.put("status", true);
            msg.put("msg", "成功新增兑奖码"+num+"条");
        } catch (Exception e) {
            log.error("新增兑奖码失败！", e);
            msg.put("status", false);
            msg.put("msg", "成功新增兑奖码"+num+"条");
        }
        return msg;
    }

    /**
     * 奖券作废
     */
    @RequestMapping(value = "/invalidRedemption")
    @ResponseBody
    @SystemLog(description = "奖券作废",operCode="redemption.invalidRedemption")
    public Map<String,Object> updateRedemptionStatus(@RequestParam("id") Integer id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=redemptionService.updateRedemptionStatus(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "奖券作废成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "奖券作废失败!");
            }
        } catch (Exception e){
            log.error("奖券作废异常!",e);
            msg.put("status", false);
            msg.put("msg", "奖券作废异常!");
        }
        return msg;
    }

    @RequestMapping("/exportRedemption")
    public void exportRedemption(@RequestParam String param, HttpServletResponse response){
        try {
            ExchangeAwardsRecode info = JSON.parseObject(param,ExchangeAwardsRecode.class);
            redemptionService.exportRedemption(info, response);
        } catch (Exception e) {
            log.info("导出兑奖活动失败,参数:{}");
            log.info(e.toString());
        }
    }
}
