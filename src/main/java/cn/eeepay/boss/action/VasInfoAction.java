package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.VasShareRule;
import cn.eeepay.framework.model.VasShareRuleTask;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserCouponService;
import cn.eeepay.framework.service.VasInfoService;
import cn.eeepay.framework.service.VerificationInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/vasInfo")
public class VasInfoAction {
    private static final Logger log = LoggerFactory.getLogger(VasInfoAction.class);


    @Resource
    private VasInfoService vasInfoService;

    @Resource
    private SysDictService sysDictService;


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping("/vasShareRuleQuery")
    public Object vasShareRuleQuery(@RequestParam String info, @Param("page") Page<VasShareRule> page) {
        Map<String, Object> res = new HashMap<>();
        Map<String, String> params = JSONObject.parseObject(info, Map.class);
        log.info("params=" + params);
        vasInfoService.vasShareRuleQuery(params, page);
        res.put("page", page);
        return res;
    }

    /***/
    @RequestMapping("/agentVasShareRuleQuery/{id}")
    @ResponseBody
    public Object findDetail(@PathVariable("id") String id) {
        Map<String, Object> res = new HashMap<>();

        log.info("\n查询详情的ID---------------" + id + "--------------------\n");


        return res;
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping("/vasShareRuleTaskQuery")
    public Object vasShareRuleTaskQuery(@RequestParam String info, @Param("page") Page<VasShareRuleTask> page) {
        Map<String, Object> res = new HashMap<>();
        Map<String, String> params = JSONObject.parseObject(info, Map.class);
        log.info("params=" + params);
        vasInfoService.vasShareRuleTaskQuery(params, page);
        res.put("page", page);
        return res;
    }

    /**
     * 获取组织管控实体信息
     *
     * @return
     */
    @RequestMapping(value = "/getVasShareRule")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> getVasShareRule(@RequestParam("id") int id) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            VasShareRule info = vasInfoService.getVasShareRuleById(id);
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e) {
            log.error("获取增值业务分润信息异常!", e);
            msg.put("status", false);
            msg.put("msg", "获取增值业务分润信息异常!");
        }
        return msg;
    }

    /**
     * 修改直属，默认分润
     *
     * @return
     */
    @RequestMapping(value = "/updateVasShareRule")
    @ResponseBody
    public Map<String, Object> updateVasShareRule(@RequestParam("info") String param, @RequestParam("type") String type) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            VasShareRule info = JSONObject.parseObject(param, VasShareRule.class);
            log.info(info.toString());
            vasInfoService.updateVasShareRule(info, msg, type);
        } catch (Exception e) {
            log.error("修改增值业务分润异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改增值业务分润异常!");
        }
        return msg;
    }

    /**
     * 修改代理商分润
     *
     * @return
     */
    @RequestMapping(value = "/updateAgentVasShareRule")
    @ResponseBody
    public Map<String, Object> updateAgentVasShareRule(@RequestParam("info") String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            VasShareRule info = JSONObject.parseObject(param, VasShareRule.class);
            log.info(info.toString());
            vasInfoService.updateAgentVasShareRule(info, msg);
        } catch (Exception e) {
            log.error("修改增值业务分润异常!", e);
            msg.put("status", false);
            msg.put("msg", "修改增值业务分润异常!");
        }
        return msg;
    }

    @RequestMapping(value = "/updateVasShareRuleSwitch.do")
    @ResponseBody
    public Object updateVasShareRuleSwitch(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        VasShareRule info = JSON.parseObject(param, VasShareRule.class);
        try {
            log.info("info.toString()=" + info.toString());
            SysDict sysDict = sysDictService.getByKey("vas_agent_rule_switch");
            if (sysDict != null && "0".equals(sysDict.getSysValue())) {
                msg.put("status", false);
                msg.put("msg", "账户汇总中，暂不支持修改");
                return msg;
            }
            if (info.getVasId() != 0 && info.getProfitSwitch() == 1) {
                VasShareRule rule0 = vasInfoService.getVasShareRuleById(info.getVasId());
                log.info("rule0=" + rule0.toString());
                if (rule0 != null && rule0.getProfitSwitch() == 0) {
                    msg.put("status", false);
                    msg.put("msg", "暂不支持修改");
                    return msg;
                }
            }

            int i = vasInfoService.updateVasShareRuleSwitch(info);
            if (i > 0) {
                msg.put("status", true);
                msg.put("msg", "设置成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "设置失败");
            }
        } catch (Exception e) {
            msg.put("msg", "服务异常");
            e.printStackTrace();
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 导出预警阀值
     *
     * @return
     */
    @RequestMapping(value = "/exportVasShareRuleInfo")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public void exportVasShareRuleInfo(@RequestParam String info, HttpServletResponse response) {
        try {
            Map<String, Object> res = new HashMap<>();
            Map<String, String> params = JSONObject.parseObject(info, Map.class);
            log.info("params=" + params);
            vasInfoService.exportVasShareRuleInfo(response, params);
        } catch (Exception e) {
            log.error("导出异常", e);
        }
        return;
    }


}
