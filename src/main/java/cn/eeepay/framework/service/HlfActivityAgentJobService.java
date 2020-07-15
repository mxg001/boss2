package cn.eeepay.framework.service;

import java.util.Map;

public interface HlfActivityAgentJobService {

    void hlfActivityAgentJob();

    Map<String, Object> agentRewardAccountStatus(String ids);
}
