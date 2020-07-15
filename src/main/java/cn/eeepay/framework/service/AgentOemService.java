package cn.eeepay.framework.service;

/**
 * @author tans
 * @date 2018/11/13 17:13
 */
public interface AgentOemService {

    int checkExists(String agentNo);

    int insert(String agentNo, String oemType);
}
