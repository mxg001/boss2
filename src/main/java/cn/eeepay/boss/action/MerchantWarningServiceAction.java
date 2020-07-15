package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantWarning;
import cn.eeepay.framework.service.MerchantWarningService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户预警服务
 */
@Controller
@RequestMapping(value = "/merWarning")
public class MerchantWarningServiceAction {

    private static final Logger log = LoggerFactory.getLogger(MerchantWarningServiceAction.class);

    @Resource
    private MerchantWarningService merchantWarningService;

    @RequestMapping(value = "/selectMerchantWarningPage")
    public @ResponseBody Object selectMerchantWarningPage(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<MerchantWarning> page) throws Exception{
        try{
            MerchantWarning merchantWarning = JSONObject.parseObject(param, MerchantWarning.class);
            merchantWarningService.selectMerchantWarningPage(merchantWarning, page);
        } catch (Exception e){
            log.error("条件查询公告失败",e);
        }
        return page;
    }


    @RequestMapping(value = "/selectMerchantWarningDetail")
    @ResponseBody
    public Map<String,Object> selectMerchantWarningDetail(@RequestParam("id")Integer id) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try {
            MerchantWarning info = merchantWarningService.selectMerchantWarningDetail(id);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
        }
        return msg;
    }

    @RequestMapping(value = "/deleteMerchantWarning")
    @ResponseBody
    @SystemLog(description = "商户预警服务删除",operCode="merWarning.deleteMerchantWarning")
    public Map<String,Object> deleteMerchantWarning(@RequestParam("id")Integer id) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try {
            merchantWarningService.deleteMerchantWarning(id);
            msg.put("status", true);
            msg.put("msg", "删除成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "删除失败");
        }
        return msg;
    }

    @RequestMapping(value="/switchIsUsedStatus")
    @ResponseBody
    @SystemLog(description = "修改商户预警服务开关",operCode="merWarning.switchIsUsedStatus")
    public Map<String, Object> switchIsUsedStatus (@RequestBody MerchantWarning merchantWarning) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "操作失败");
        try{
            msg = merchantWarningService.updateIsUsedStatus(merchantWarning);
        } catch (Exception e){
            msg.put("status", false);
            log.error("修改商户预警服务开关,失败！",e);
        }
        return msg;
    }

    /**
     * 新增商户预警服务
     */
    @RequestMapping(value = "/insertMerWarning")
    @ResponseBody
    @SystemLog(description = "新增商户预警服务",operCode="merWarning.insertMerWarning")
    public Map<String, Object> insertMerWarning(@RequestBody String params) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(params);
            MerchantWarning merchantWarning = json.getObject("info", MerchantWarning.class);
            if(merchantWarning==null){
                msg.put("status", false);
                msg.put("msg", "参数错误");
                return msg;
            }
            merchantWarningService.insertMerWarning(merchantWarning);
            msg.put("status", true);
            msg.put("msg", "新增成功！");
        } catch (Exception e) {
            log.error("新增商户预警服务失败!", e);
            msg.put("status", false);
            msg.put("msg", "新增失败");
        }

        return msg;
    }

    /**
     * 新增商户预警服务
     */
    @RequestMapping(value = "/updateMerWarning")
    @ResponseBody
    @SystemLog(description = "修改商户预警服务",operCode="merWarning.updateMerWarning")
    public Map<String, Object> updateMerWarning(@RequestBody String params) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject json = JSON.parseObject(params);
            MerchantWarning merchantWarning = json.getObject("info", MerchantWarning.class);
            if(merchantWarning==null){
                msg.put("status", false);
                msg.put("msg", "参数错误");
                return msg;
            }
            merchantWarningService.updateMerWarning(merchantWarning);
            msg.put("status", true);
            msg.put("msg", "修改成功！");
        } catch (Exception e) {
            log.error("修改商户预警服务失败!", e);
            msg.put("status", false);
            msg.put("msg", "修改失败");
        }

        return msg;
    }
}
