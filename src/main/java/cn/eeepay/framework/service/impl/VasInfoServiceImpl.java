package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.VasInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.VasInfoService;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VasInfoServiceImpl implements VasInfoService {

    private static final Logger log = LoggerFactory.getLogger(VasInfoServiceImpl.class);

    @Resource
    private VasInfoDao vasInfoDao;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<VasShareRule> vasShareRuleQuery(Map<String, String> params, Page<VasShareRule> page) {
        String fromType = params.get("fromType");
        if ("1".equals(fromType)) {
            if(StringUtils.isNotBlank((String)params.get("agentNo")) && "1".equals(params.get("hasSub").toString())){
                AgentInfo info=agentInfoDao.selectByAgentNo((String)params.get("agentNo"));
                if(info!=null&&StringUtils.isNotBlank(info.getAgentNode())){
                    params.put("subAgentNo", info.getAgentNode()+"%");
                } else {
                    return null;
                }
            }
        }


        List<VasShareRule> list = vasInfoDao.vasShareRuleQuery(params, page);

        if ("1".equals(fromType)) {
            String vasId = params.get("id");
            VasShareRule vasShareRule0 = vasInfoDao.getVasShareRuleByVarId(vasId);

            List<VasShareRule> lists = page.getResult();
            if (lists != null && !lists.isEmpty()) {
                for (VasShareRule vas : lists) {
                    vas.setAgentPerFixCost(vasShareRule0.getAgentPerFixCost());
                    vas.setAgentCostRate(vasShareRule0.getAgentCostRate());
                }
            }
        }


        return list;
    }

    @Override
    public List<VasShareRuleTask> vasShareRuleTaskQuery(Map<String, String> params, Page<VasShareRuleTask> page) {
        return vasInfoDao.vasShareRuleTaskQuery(params, page);
    }

    @Override
    public int updateVasShareRuleSwitch(VasShareRule info) {

        if (info.getProfitSwitch() == 0 && info.getAgentNo() != null && !"".equals(info.getAgentNo()) && !"0".equals(info.getAgentNo())) {
            //联动关闭
            AgentInfo agentInfo = agentInfoDao.select(info.getAgentNo());
            info.setAgentNo(agentInfo.getAgentNode() + "%");
            if(info.getTeamEntryId()!=null){
                return vasInfoDao.updateVasShareRuleSwitchByAgentNode(info);
            }else{
                return vasInfoDao.updateVasShareRuleSwitchByAgentNode2(info);
            }

        }
        if(info.getProfitSwitch()==0){
            if(info.getTeamEntryId()!=null){
                return vasInfoDao.updateVasShareRuleSwitchByTeamId(info);
            }else{
                return vasInfoDao.updateVasShareRuleSwitchByTeamId2(info);
            }
        }else{
            if(info.getTeamEntryId()!=null){
               vasInfoDao.updateVasShareRuleSwitchByAgentLevel(info);
            }else{
               vasInfoDao.updateVasShareRuleSwitchByAgentLevel2(info);
            }
            return vasInfoDao.updateVasShareRuleSwitch(info);
        }


    }


    @Override
    public VasShareRule getVasShareRuleById(int id) {
        return vasInfoDao.getVasShareRuleById(id);
    }

    @Override
    public int updateVasShareRule(VasShareRule info, Map<String, Object> msg, String type) {
        //数据转换
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());

        boolean isOK1 = compareVasShareRule(info, 2);
        if (!isOK1) {
            msg.put("status", false);
            msg.put("msg", "修改失败!需符合条件:直属分润<=服务费-1级最高分润(包含未生效task)");
            return 0;
        }
       /* boolean isOK2 = compareVasShareRule(info, 5);
        if (!isOK2) {
            msg.put("status", false);
            msg.put("msg", "修改失败!需符合条件:默认分润<=服务费-1级最高分润(包含未生效task)");
            return 0;
        }*/

        VasShareRule oldInfo = vasInfoDao.getVasShareRuleById(info.getId());
        if (oldInfo != null && oldInfo.getDefaultShareProfitPercent() != null) {
            VasShareRuleTask task = new VasShareRuleTask();
            task.setVasRuleId(oldInfo.getId());
            task.setRateType(oldInfo.getRateType());
            task.setDefaultCostRate(oldInfo.getDefaultCostRate());
            task.setAgentCostRate(oldInfo.getAgentCostRate());
            task.setDefaultPerFixCost(oldInfo.getDefaultPerFixCost());
            task.setAgentPerFixCost(oldInfo.getAgentPerFixCost());
            task.setDefaultShareProfitPercent(oldInfo.getDefaultShareProfitPercent());
            task.setEffectiveDate(new Date());
            task.setEffectiveStatus(2);//已失效
            task.setRemark(oldInfo.getRemark());
            task.setOperator(oldInfo.getOperator());
            vasInfoDao.insertVasShareRuleTask(task);
        }
        List<AgentInfo> lists = vasInfoDao.getAgentNoNeedUpdate(info.getTeamId(), info.getTeamEntryId(), info.getVasServiceNo());
        if (lists != null && !lists.isEmpty()) {//补充1级代理商数据
            for (AgentInfo agent : lists) {
                info.setAgentNo(agent.getAgentNo());

                info.setProfitSwitch(oldInfo.getProfitSwitch());

                info.setPerFixCost(info.getDefaultPerFixCost());
                info.setCostRate(info.getDefaultCostRate());
                info.setShareProfitPercent(info.getDefaultShareProfitPercent());

                VasShareRule xInfo = vasInfoDao.getVasShareRuleByTeamId(info);
                if (xInfo == null) {
                    vasInfoDao.insertVasShareRule(info);
                }


            }
        }

        int num = vasInfoDao.updateVasShareRule(info);
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "修改成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "修改失败!");
        }
        return 0;
    }

    @Override
    public int updateAgentVasShareRule(VasShareRule info, Map<String, Object> msg) {


        VasShareRuleTask needUpdTask = vasInfoDao.getShareRuleTaskByRuleId(info.getId(), 0);
        if (needUpdTask != null) {
            msg.put("status", false);
            msg.put("msg", "修改失败,当前有一条未生效数据");
            return 0;
        }
        boolean isOK1 = compareVasShareRule(info, 3);
        if (!isOK1) {
            msg.put("status", false);
            msg.put("msg", "修改失败!需符合条件:直属分润<=服务费-1级最高分润(包含未生效task)");
            return 0;
        }

        //数据转换
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        info.setOperator(principal.getUsername());

        VasShareRuleTask task = new VasShareRuleTask();
        task.setVasRuleId(info.getId());
        task.setRateType(info.getRateType());
        task.setCostRate(info.getCostRate());
        task.setPerFixCost(info.getPerFixCost());
        task.setShareProfitPercent(info.getShareProfitPercent());
        //task.setEffectiveDate(new Date());
        task.setEffectiveStatus(0);//待生效
        task.setRemark(info.getRemark());
        task.setOperator(info.getOperator());
        int num = vasInfoDao.insertVasShareRuleTask(task);

        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "修改成功!");
        } else {
            msg.put("status", false);
            msg.put("msg", "修改失败!");
        }
        return 0;
    }

    @Override
    public void exportVasShareRuleInfo(HttpServletResponse response, Map<String, String> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        OutputStream ouputStream = null;
        try {
            Page<VasShareRule> page = new Page<>(0, Integer.MAX_VALUE);
            vasShareRuleQuery(params, page);
            List<VasShareRule> list = page.getResult();
            ListDataExcelExport export = new ListDataExcelExport();
            String fileName = "代理商分润列表" + sdf.format(new Date()) + export.getFileSuffix();
            String fileNameFormat = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<>();

            for (VasShareRule item : list) {
                Map<String, String> map = new HashMap<>();
                map.put("agentName", item.getAgentName());
                map.put("agentNo", item.getAgentNo());
                map.put("agentLevel", item.getAgentLevel());
                if (item.getProfitSwitch() == 0) {
                    map.put("profitSwitch", "关闭");
                } else if (item.getProfitSwitch() == 1) {
                    map.put("profitSwitch", "打开");
                }
                if ("1".equals(item.getRateType())) {
                    map.put("showRate", item.getPerFixCost() + "元");
                } else {
                    map.put("showRate", item.getCostRate() + "%");
                }
                if ("1".equals(item.getRateType())) {
                    map.put("showAgentRate", item.getAgentPerFixCost() + "元");
                } else {
                    map.put("showAgentRate", item.getAgentCostRate() + "%");
                }
                map.put("shareProfitPercent", item.getShareProfitPercent() + "%");
                map.put("parentAgentName", item.getParentAgentName());
                map.put("parentId", item.getParentId());
                map.put("oneLevelId", item.getOneLevelId());

                data.add(map);
            }
            String[] cols = new String[]{
                    "agentName", "agentNo", "agentLevel", "profitSwitch", "showRate",
                    "showAgentRate", "shareProfitPercent", "parentAgentName", "parentId", "oneLevelId"};
            String[] colsName = new String[]{
                    "代理商名称", "代理商编号", "代理商级别", "分润日结", "代理商分润",
                    "直属分润", "分润比例", "上级代理商名称", "上级代理商编号", "一级代理商编号"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出异常", e);
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public VasRate getVasRate(String vasServiceNo, String teamId, String teamEntryId) {
        if (teamEntryId != null) {
            return vasInfoDao.getVasRate(vasServiceNo, teamId, teamEntryId);
        }

        return vasInfoDao.getVasRateByTeamIdOnly(vasServiceNo, teamId);
    }

    @Override
    public int insertVasShareRuleTask(VasShareRuleTask info) {
        return vasInfoDao.insertVasShareRuleTask(info);
    }


    @Override
    public void updateVasShareRuleTask() {
        log.info("updateVasShareRuleTask开始执行");
        log.info("开始扫描vas_share_rule_task表effective_status=0的数据");
        List<VasShareRuleTask> lists = vasInfoDao.getShareRuleTaskNeedUpdate();
        if (lists != null && !lists.isEmpty()) {
            log.info("需要处理{}条数据", lists.size());
            for (VasShareRuleTask task : lists) {
                VasShareRule oldInfo = vasInfoDao.getVasShareRuleById(task.getVasRuleId());
                if (oldInfo != null) {
//                    VasShareRuleTask taskNew = new VasShareRuleTask();
//                    taskNew.setVasRuleId(oldInfo.getId());
//                    taskNew.setRateType(oldInfo.getRateType());
//
//                    taskNew.setCostRate(oldInfo.getCostRate());
//                    taskNew.setPerFixCost(oldInfo.getPerFixCost());
//                    taskNew.setShareProfitPercent(oldInfo.getShareProfitPercent());
//
//                    taskNew.setEffectiveDate(oldInfo.getEffectiveDate());
//                    taskNew.setEffectiveStatus(2);
//                    taskNew.setRemark(oldInfo.getRemark());
//                    taskNew.setOperator(oldInfo.getOperator());
//                    //插入一条新记录
//                    vasInfoDao.insertVasShareRuleTask(task);


                    //已生效改成已失效
                    vasInfoDao.updateVasShareRuleTaskStatusToEffect(task.getVasRuleId());


                    //旧记录改成已生效
                    task.setEffectiveStatus(1);
                    task.setEffectiveDate(new Date());
                    vasInfoDao.updateVasShareRuleTaskStatus(task);

                    //新记录数据写入rule表
                    oldInfo.setOperator(task.getOperator());
                    oldInfo.setPerFixCost(task.getPerFixCost());
                    oldInfo.setCostRate(task.getCostRate());
                    oldInfo.setShareProfitPercent(task.getShareProfitPercent());
                    oldInfo.setEffectiveDate(new Date());
                    vasInfoDao.updateAgentVasShareRule(oldInfo);

                }
            }
            log.info("数据处理完成");
        }
        log.info("updateVasShareRuleTask执行完毕");

    }

    @Override
    public List<VasShareRule> getVasShareRuleByAgentNo(String agentNo) {
        return vasInfoDao.getVasShareRuleByAgentNo(agentNo);
    }

    @Override
    public int insertVasShareRule(VasShareRule info) {
        return vasInfoDao.insertVasShareRule(info);
    }

    @Override
    public void checkVasShareRule(String agentNo) {
        //如果代理商新增了业务产品，首先根据业务产品组织查询增值
        //服务分润表如果代理商新增了业务产品，首先根据业务产品组织查询增值服务分润表
        //vas_ share_ rule,查询系统默认分润(agent_ no=0)。如果系统默认分润不为空(判
        //断agent_ cost rate_ type是否为空即可)，且当前代理商没有该增值服务分润,则需要
        //根据默认分润插入该代理商的增值服务分润
        //vas_ share_ rule,查询系统默认分润(agent_ no=0)。如果系统默认分润不为空(判
        //断agent_ cost rate_ type是否为空即可)，且当前代理商没有该增值服务分润,则需要
        //根据默认分润插入该代理商的增值服务分润
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<VasShareRule> vasLists = getVasShareRuleByAgentNo(agentNo);
        if (vasLists != null && !vasLists.isEmpty()) {
            for (VasShareRule vas : vasLists) {


                if (vas.getDefaultPerFixCost() == null
                        && vas.getDefaultCostRate() == null
                        && vas.getDefaultShareProfitPercent() == null) {
                    //3个值都为空不插入数据
                } else {
                    VasShareRule vasShareRule = new VasShareRule();
                    vasShareRule.setAgentNo(agentNo);
                    vasShareRule.setVasServiceNo(vas.getVasServiceNo());
                    vasShareRule.setOperator(principal.getUsername());
                    vasShareRule.setTeamId(vas.getTeamId());
                    vasShareRule.setTeamEntryId(vas.getTeamEntryId());
                    vasShareRule.setRateType(vas.getRateType());


                    vasShareRule.setPerFixCost(vas.getDefaultPerFixCost());
                    vasShareRule.setCostRate(vas.getDefaultCostRate());
                    vasShareRule.setShareProfitPercent(vas.getDefaultShareProfitPercent());

                    vasShareRule.setProfitSwitch(vas.getProfitSwitch());

                    VasShareRule oldInfo = vasInfoDao.getVasShareRuleByTeamId(vasShareRule);
                    if (oldInfo == null) {
                        vasInfoDao.insertVasShareRuleByAgent(vasShareRule);
                    }
                }


            }
        }


    }


    @Override
    public boolean compareVasShareRule(VasShareRule rule, Integer type) {
        log.info("修改的rule:" + rule);
        boolean isOk = true;
        VasRate rate = getVasRate(rule.getVasServiceNo(), rule.getTeamId(), rule.getTeamEntryId());
        log.info("比较的rate:" + rate);
        if (rate != null) {
            VasShareRuleTask ruleTask = vasInfoDao.getShareRuleTaskByRuleId(rule.getId(), 0);
            log.info("比较的task:" + ruleTask);
            //if (ruleTask != null) {
            isOk = compareVasRateAndRule(rate, rule, ruleTask, type);
            //}
        }
        return isOk;
    }


    @Override
    public boolean compareVasRate(VasRate rate) {
        log.info("修改的rate:" + rate);
        boolean isOk = true;
        //VasShareRule rule = vasInfoDao.getVasShareRuleMaxByRate(rate);
        VasShareRule info=new VasShareRule();
        info.setAgentNo("0");
        info.setTeamId(rate.getTeamId());
        info.setTeamEntryId(rate.getTeamEntryId());
        info.setVasServiceNo(rate.getVasServiceNo());
        VasShareRule rule = vasInfoDao.getVasShareRuleByTeamId(info);
        log.info("比较的rule:" + rule);
        if (rule != null) {
            //VasShareRuleTask ruleTask = vasInfoDao.getShareRuleTaskByRuleId(rule.getId(), 0);
            //log.info("比较的task:" + ruleTask);
            //if (ruleTask != null) {
            isOk = compareVasRateAndRule(rate, rule, null, 1);
            //}
        }
        return isOk;

    }

    public boolean compareVasRateAndRule(VasRate rate, VasShareRule rule,VasShareRuleTask ruleTask, Integer compareType) {
        boolean isOk = true;
        if (rate != null && rule != null) {
            BigDecimal baseRate = null;
            BigDecimal costRate = null;
            BigDecimal taskRate = null;
            BigDecimal agentRate = null;
            BigDecimal defaultRate = null;
            if ("1".equals(rule.getRateType())) {
                baseRate = rate.getSingleNumAmount();//服务费
                costRate = rule.getPerFixCost();//最高分润
                if (ruleTask != null) {
                    taskRate = ruleTask.getPerFixCost();//task未生效分润
                } else {
                    taskRate = BigDecimal.ZERO;
                }
                agentRate = rule.getAgentPerFixCost();//直属分润
                defaultRate = rule.getDefaultPerFixCost();//默认分润
            } else if ("2".equals(rule.getRateType())) {
                baseRate = rate.getRate();//服务费
                costRate = rule.getCostRate();//最高分润
                if (ruleTask != null) {
                    taskRate = ruleTask.getCostRate();//task未生效分润
                } else {
                    taskRate = BigDecimal.ZERO;
                }
                agentRate = rule.getAgentCostRate();//直属分润
                defaultRate = rule.getDefaultCostRate();//默认分润
            }
            if (compareType == 1) {
                //BigDecimal resuleRate = null;
                //哪个大比较哪个
               // if (taskRate.compareTo(costRate) == 1) {
                //    resuleRate = taskRate;
                //} else {
                //    resuleRate = costRate;
                //}
                if (baseRate.compareTo(defaultRate.add(agentRate)) >= 0) {
                    //符合条件
                    isOk = true;
                } else {
                    //不符合条件
                    isOk = false;
                }
            }else if (compareType == 2) {
                if (baseRate.compareTo(defaultRate.add(agentRate)) >= 0) {
                    //符合条件
                    isOk = true;
                } else {
                    //不符合条件
                    isOk = false;
                }
                return isOk;
            } else if (compareType == 3) {
                VasShareRule vasShareRule0 = vasInfoDao.getVasShareRuleByVarId(rule.getVasId() + "");
                log.info("比较的vasShareRule0:" + vasShareRule0);
                if ("1".equals(rule.getRateType())) {
                    agentRate = vasShareRule0.getAgentPerFixCost();
                } else if ("2".equals(rule.getRateType())) {
                    agentRate = vasShareRule0.getAgentCostRate();
                }

                if (baseRate.compareTo(costRate.add(agentRate)) >= 0) {
                    //符合条件
                    isOk = true;
                } else {
                    //不符合条件
                    isOk = false;
                }
                return isOk;
            } else if (compareType == 5) {
                VasShareRule rule2 = vasInfoDao.getVasShareRuleMaxByRule(rule);
                if(rule2!=null){
                    log.info("比较的rule2:" + rule2);
                    if ("1".equals(rule.getRateType())) {
                        costRate = rule2.getPerFixCost();//最高分润
                    } else if ("2".equals(rule.getRateType())) {
                        costRate = rule2.getCostRate();//服务费
                    }
                    if(costRate!=null){
                        BigDecimal resuleRate = null;
                        //哪个大比较哪个
                        if (taskRate.compareTo(costRate) == 1) {
                            resuleRate = taskRate;
                        } else {
                            resuleRate = costRate;
                        }
                        if (baseRate.compareTo(resuleRate.add(agentRate)) >= 0) {
                            //符合条件
                            isOk = true;
                        } else {
                            //不符合条件
                            isOk = false;
                        }
                    }

                }

            }


            /*if (costRate != null && taskRate != null &&  baseRate != null && agentRate != null && defaultRate != null) {
                BigDecimal resuleRate = null;
                //哪个大比较哪个
                if (taskRate.compareTo(costRate) == 1) {
                    resuleRate = taskRate;
                } else {
                    resuleRate = costRate;
                }
                switch (compareType) {
                    case 1://修改服务费
                        break;
                    case 2://修改直属分润
                        break;
                    case 3://修改1级分润
                        break;
                    case 4://修改下级分润
                        break;
                    case 5://修改默认分润
                        //直属分润<=服务费-一级最高分润
                        agentRate = defaultRate;
                        break;
                }
                if (baseRate.compareTo(resuleRate.add(agentRate)) >= 0) {
                    //符合条件
                    isOk = true;
                } else {
                    //不符合条件
                    isOk = false;
                }
            }*/
        }
        return isOk;
    }
}
