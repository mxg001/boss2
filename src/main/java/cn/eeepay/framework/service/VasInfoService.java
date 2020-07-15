package cn.eeepay.framework.service;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.VasRate;
import cn.eeepay.framework.model.VasShareRule;
import cn.eeepay.framework.model.VasShareRuleTask;
import sun.reflect.generics.tree.VoidDescriptor;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface VasInfoService {

    List<VasShareRule> vasShareRuleQuery(Map<String, String> params, Page<VasShareRule> page);

    void exportVasShareRuleInfo(HttpServletResponse response, Map<String, String> params);

    List<VasShareRuleTask> vasShareRuleTaskQuery(Map<String, String> params, Page<VasShareRuleTask> page);

    int updateVasShareRuleSwitch(VasShareRule info);

    VasShareRule getVasShareRuleById(int id);

    VasRate getVasRate(String vasServiceNo, String teamId, String teamEntryId);

    List<VasShareRule> getVasShareRuleByAgentNo(String agentNo);

    int updateVasShareRule(VasShareRule info, Map<String, Object> msg,String type);

    int updateAgentVasShareRule(VasShareRule info, Map<String, Object> msg);

    int insertVasShareRuleTask(VasShareRuleTask info);

    int insertVasShareRule(VasShareRule info);

    void updateVasShareRuleTask();

    void checkVasShareRule(String agentNo);

    boolean compareVasShareRule(VasShareRule rule,Integer type);

    boolean compareVasRate(VasRate rate);

}
