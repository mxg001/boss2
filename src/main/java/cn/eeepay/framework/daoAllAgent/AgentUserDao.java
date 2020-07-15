package cn.eeepay.framework.daoAllAgent;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/18/018.
 * @author liuks
 */
public interface AgentUserDao {

    @Select(
            "select * from pa_agent_user where agent_no=#{agentNo}"
    )
    List<Map<String,Object>> selectAgentUser(@Param("agentNo") String agentNo);
}
