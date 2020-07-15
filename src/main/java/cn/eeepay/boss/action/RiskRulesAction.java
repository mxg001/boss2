package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.RiskRollService;
import cn.eeepay.framework.service.RiskRulesService;

@Controller
@RequestMapping(value="/riskRulesAction")
public class RiskRulesAction {

    private static final Logger log = LoggerFactory.getLogger(RiskRulesAction.class);

    @Resource
    private RiskRulesService riskRulesService;
    @Resource
    private SysDictService sysDictService;

    @Resource
    private RiskRollService riskRollService;

    /**
     * 查询所有规则
     *
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Object selectAll() throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            List<RiskRules> list = riskRulesService.selectAll();
            jsonMap.put("bols", true);
            jsonMap.put("rulesList", list);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 查询所有规则
     *
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllWithOutStatus")
    @ResponseBody
    public Object selectAllWithOutStatus() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<RiskRules> list = riskRulesService.selectAllWithOutStatus();
            result.put("status", true);
            result.put("rulesList", list);
        } catch (Exception e) {
            log.error("报错!!!", e);
            result.put("status", false);
        }
        return result;
    }

    /**
     * 初始化和模糊查询分页
     *
     * @param page
     * @param param
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectAllInfo")
    @ResponseBody
    public Object selectAllInfo(@ModelAttribute("page") Page<RiskRules> page, @RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            RiskRules rr = JSON.parseObject(param, RiskRules.class);
            riskRulesService.selectAllInfo(page, rr);

            List<RiskRules> RiskRulesList = page.getResult();
            for (RiskRules riskRules : RiskRulesList) {

                if (StringUtils.isBlank(riskRules.getRulesValues())) {
                    continue;
                }
                ObjectMapper om = new ObjectMapper();
                Map<String, Object> valuesMap = om.readValue(riskRules.getRulesValues(), Map.class);
                String rulesEngine = riskRules.getRulesEngine();
                for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
                    log.info("{}={}", new Object[]{entry.getKey(), entry.getValue()});
                    String key = "\\{" + entry.getKey() + "\\}";
                    String val = String.valueOf(entry.getValue());
                    rulesEngine = rulesEngine.replaceAll(key, val);
                }


                riskRules.setRulesEngine(rulesEngine);
            }

            jsonMap.put("bols", true);
            jsonMap.put("page", page);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 详情查询
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectRiskRulesDetailById")
    @ResponseBody
    public Object selectRiskRulesDetailById(@RequestParam("ids") String ids) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            int id = JSON.parseObject(ids, Integer.class);
            RiskRules rr = riskRulesService.selectDetail(id);

            if (rr.getRulesProvinces() != null && !"".equals(rr.getRulesProvinces())) {
                rr.setProvincesList(rr.getRulesProvinces().split(","));
            }
            if (rr.getRulesCity() != null && !"".equals(rr.getRulesCity())) {
                rr.setCityList(rr.getRulesCity().split(","));
            }

            if (id != 114 && id != 129) {
                ObjectMapper om = new ObjectMapper();
                Map<String, Object> valuesMap = om.readValue(rr.getRulesValues(), Map.class);
                String rulesEngine = rr.getRulesEngine();
                for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
                    log.info("{}={}", new Object[]{entry.getKey(), entry.getValue()});
                    String key = "\\{" + entry.getKey() + "\\}";
                    String val = String.valueOf(entry.getValue());
                    if (id == 115 && "riskStatus".equals(entry.getKey())) {
                        String value = "";
                        String[] status = val.split(",");
                        if ("2".equals(status[0])) {
                            value = value + " 不进不出 ";
                        }
                        if ("3".equals(status[1])) {
                            value = value + "只进不出";
                        }
                        val = value;
                    }
                    rulesEngine = rulesEngine.replaceAll(key, val);
                }
                rr.setRulesEngine(rulesEngine);
            }

            jsonMap.put("result", rr);
            jsonMap.put("bols", true);

            try {
                jsonMap.put("teaminfos", riskRulesService.getAllScope());
                jsonMap.put("merTypeInfos", sysDictService.selectByKey("MERCHANT_TYPE_LIST"));
            } catch (Exception e) {
                log.error("查询适用范围组织报错!!!", e);
                jsonMap.put("bols", false);
            }

        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    /**
     * 详情查询(设置)
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Object selectByParam(@RequestParam("ids") String ids) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            int id = JSON.parseObject(ids, Integer.class);
            RiskRules rr = riskRulesService.selectDetail(id);

            if (rr.getRulesProvinces() != null && !"".equals(rr.getRulesProvinces())) {
                rr.setProvincesList(rr.getRulesProvinces().split(","));
            }
            if (rr.getRulesCity() != null && !"".equals(rr.getRulesCity())) {
                rr.setCityList(rr.getRulesCity().split(","));
            }

            String values = rr.getRulesValues();
            Map<String, Object> valuesMap = null;
            if (rr.getRulesNo() != 114 && rr.getRulesNo() != 129) {
                ObjectMapper om = new ObjectMapper();
                valuesMap = om.readValue(values, Map.class);
            }

            jsonMap.put("result", rr);
            jsonMap.put("values", valuesMap);
            jsonMap.put("bols", true);

            try {
                jsonMap.put("teaminfos", riskRulesService.getAllScope());
                jsonMap.put("merTypeInfos", sysDictService.selectByKey("MERCHANT_TYPE_LIST"));
            } catch (Exception e) {
                log.error("查询适用范围组织报错!!!", e);
                jsonMap.put("bols", false);
            }


        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getAllScope")
    @ResponseBody
    public Object getAllScope() {
        List<TeamInfo> list = null;
        try {
            list = riskRulesService.getAllScope();
        } catch (Exception e) {
            log.error("查询范围出错！！", e);
        }
        return list;
    }


    /**
     * 规则指令修改
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modifyRulesInstruction")
    @ResponseBody
    @SystemLog(description = "风控规则指令修改", operCode = "riskRule.switch")
    public Object modifyRulesInstruction(@RequestBody String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            JSONObject json = JSON.parseObject(param);
            String rulesNo = json.getString("rulesNo").trim();
            String rulesInstruction = json.getString("rulesInstruction").trim();
            if (!StringUtils.isNotBlank(rulesNo)) {
                jsonMap.put("msg", "修改失败~~~~");
                jsonMap.put("bols", false);
                return jsonMap;
            }
            if (!StringUtils.isNotBlank(rulesInstruction)) {
                jsonMap.put("msg", "修改失败~~~~");
                jsonMap.put("bols", false);
                return jsonMap;
            }

            RiskRules rr = new RiskRules();
            rr.setRulesNo(Integer.valueOf(rulesNo));
            rr.setRulesInstruction(Integer.valueOf(rulesInstruction));
            int i = riskRulesService.updateRulesInstruction(rr);
            if (i > 0) {
                jsonMap.put("msg", "修改成功~~~~");
                jsonMap.put("bols", true);
            } else {
                jsonMap.put("msg", "修改失败~~~~");
                jsonMap.put("bols", false);
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
            jsonMap.put("msg", "修改报错~~~~");
        }
        return jsonMap;
    }

    /**
     * 状态修改
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateStatusInfo")
    @ResponseBody
    @SystemLog(description = "风控规则状态开关", operCode = "riskRule.switch")
    public Object updateStatusInfo(@RequestParam("info") String info) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String str = "关闭";
        try {
            RiskRules rr = JSON.parseObject(info, RiskRules.class);
            if (rr.getStatus() == 1) {
                str = "开启";
            }
            int i = riskRulesService.updateStatus(rr);
            if (i > 0) {
                jsonMap.put("msg", str + "成功~~~~");
                jsonMap.put("bols", true);
            } else {
                jsonMap.put("msg", str + "失败~~~~");
                jsonMap.put("bols", false);
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
            jsonMap.put("msg", str + "报错~~~~");
        }
        return jsonMap;
    }

    /**
     * 修改
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateInfo")
    @ResponseBody
    @SystemLog(description = "设置风控规则", operCode = "riskRule.update")
    public Object updateInfo(@RequestParam("info") String info, @RequestParam("values") String values) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            RiskRules rr = JSON.parseObject(info, RiskRules.class);
            List<AreaInfo> pList = null;
            List<AreaInfo> cList = null;
            if (rr.getRulesProvincesList() != null && !"".equals(rr.getRulesProvincesList())) {
                pList = JSON.parseArray(rr.getRulesProvincesList(), AreaInfo.class);
            }
            if (rr.getRulesCityList() != null && !"".equals(rr.getRulesCityList())) {
                cList = JSON.parseArray(rr.getRulesCityList(), AreaInfo.class);
            }
            if (rr.getRulesNo() != 114 && rr.getRulesNo() != 129) {
                ObjectMapper om = new ObjectMapper();
                Map<String, Object> valuesMap = om.readValue(values, Map.class);
                for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
                    log.info("{}={}", new Object[]{entry.getKey(), entry.getValue()});
                }
                String valuesJson = new ObjectMapper().writeValueAsString(valuesMap);
                rr.setRulesValues(valuesJson);
            } else {
                if (pList != null && pList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (AreaInfo province : pList) {
                        sb.append(province.getName()).append(",");
                    }
                    rr.setRulesProvinces(sb.substring(0, sb.length() - 1));
                    //设置市
                    if (cList != null && cList.size() > 0) {
                        StringBuilder ss = new StringBuilder();
                        for (AreaInfo p : cList) {
                            ss.append(p.getName()).append(",");
                        }
                        rr.setRulesCity(ss.substring(0, ss.length() - 1));
                    } else {
                        rr.setRulesCity(null);
                    }
                } else {
                    rr.setRulesProvinces(null);
                }
            }
            int i = riskRulesService.updateInfo(rr);
            if (i > 0) {
                jsonMap.put("msg", "设置成功~~~~");
                jsonMap.put("bols", true);
            } else {
                jsonMap.put("msg", "设置失败~~~~");
                jsonMap.put("bols", false);
            }
        } catch (Exception e) {
            log.error("报错!!!", e);
            System.out.println(e);
            System.out.println(e.getMessage());
            jsonMap.put("bols", false);
            jsonMap.put("msg", "设置报错~~~~");
        }
        return jsonMap;
    }

    /**
     * 白名单规则配置
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/rulesConfig")
    @ResponseBody
    @SystemLog(description = "白名单规则配置", operCode = "whiteList.setRule")
    public Object rulesConfig(@RequestParam("riskRulesJson") String riskRulesJson) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String str = "关闭";
        try {
            ObjectMapper om = new ObjectMapper();
            List<RiskRules> list = om.readValue(riskRulesJson, new TypeReference<List<RiskRules>>() {
            });

            //进行批量更新操作
            int result = riskRulesService.updateBatch(list);
            if (result > 0) {
                jsonMap.put("bols", true);
                jsonMap.put("msg", "白名单规则配置成功");
            } else {
                jsonMap.put("bols", false);
                jsonMap.put("msg", "白名单规则配置失败");
            }

        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("bols", false);
            jsonMap.put("msg", str + "报错~~~~");
        }
        return jsonMap;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/faceRecognition")
    @ResponseBody
    public Object faceRecognition() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            jsonMap.put("bols", true);
            jsonMap.put("faceJs", sysDictService.getValueByKey("FACE_RECOGNITION_JSKJ"));
            jsonMap.put("faceXs", sysDictService.getValueByKey("FACE_RECOGNITION_XSKJ"));
        } catch (Exception e) {
            log.error("详情查询报错~~~~~~");
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }

    @RequestMapping(value = "/updateFaceRecognition")
    @ResponseBody
    public Object updateFaceRecognition(@RequestParam("baseInfo") String param) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            Map<String, String> map = JSONUtil.toBean(param, Map.class);
            String jskj = map.get("faceJs");
            String xskj = map.get("faceXs");

            if((Integer.valueOf(jskj)+Integer.valueOf(xskj))!=100){
                jsonMap.put("bols", false);
                jsonMap.put("msg", "请控制活体检测通道路由百分比之和为100!");
                return jsonMap;
            }
            riskRulesService.updateFaceRecognition(jskj,xskj);
            jsonMap.put("bols", true);
            jsonMap.put("msg", "修改成功");

        } catch (Exception e) {
            log.error("详情查询报错~~~~~~");
            jsonMap.put("bols", false);
        }
        return jsonMap;
    }
}