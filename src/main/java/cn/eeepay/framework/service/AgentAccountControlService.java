package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAccountControl;
import cn.eeepay.framework.model.AgentInfo;

import java.util.List;

public interface AgentAccountControlService {

    List<AgentAccountControl>  queryAgentAccountControl(AgentAccountControl agentAccountControl,Page<AgentAccountControl> page);

    AgentAccountControl queryAgentAccountControlByDefault();

    boolean saveAgentAccountControl(AgentAccountControl agentAccountControl);

    AgentInfo queryAgentByID(String agentNo);

    boolean queryAgentAccountControlByAgentNoCount(String agentNo);

    boolean addAgentAccountControl(AgentAccountControl agentAccountControl);

    boolean updateAgentAccountControl(AgentAccountControl agentAccountControl);

    AgentAccountControl queryAgentAccountControlByAgentNo(String agentNo);

    boolean deleteAgentAccountControl(String agentNo);
}
