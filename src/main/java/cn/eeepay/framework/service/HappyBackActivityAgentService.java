package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityAgent;
import cn.eeepay.framework.model.HappyBackActivityAgentDetail;
import cn.eeepay.framework.model.HappyBackActivityMerchant;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author rpc
 * @description 欢乐返代理商奖励查询
 * @date 2019/11/7
 */
public interface HappyBackActivityAgentService {

    /**
     * 欢乐返代理商奖励查询列表
     *
     * @param page
     * @param happyBackActivityAgent
     */
    void selectHappyBackActivityAgent(Page<HappyBackActivityAgent> page,
                                      HappyBackActivityAgent happyBackActivityAgent);

    /**
     * 统计奖励金额和扣款金额
     *
     * @param happyBackActivityAgent
     * @return
     */
    Map<String, Object> countMoney(HappyBackActivityAgent happyBackActivityAgent);

    /**
     * 导出欢乐返代理商奖励活动列表
     *
     * @param happyBackActivityAgent
     * @param response
     */
    void exportExcel(HappyBackActivityAgent happyBackActivityAgent, HttpServletResponse response) throws Exception;


    List<HappyBackActivityAgentDetail> agentAwardDetail(String id);

    int updateHappyBackActivityAgentScan(HappyBackActivityAgent detail);

    int updateHappyBackActivityAgentAll(HappyBackActivityAgent detail);

    int updateHappyBackActivityAgentDetailScan(HappyBackActivityAgentDetail detail);

    int updateHappyBackActivityAgentDetailAll(HappyBackActivityAgentDetail detail);


}
