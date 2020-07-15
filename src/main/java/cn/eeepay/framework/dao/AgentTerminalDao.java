package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/7/18/018.
 * @author  liuks
 */
public interface AgentTerminalDao {

    @Insert(
            "insert into pa_ter_info " +
                    " (agent_no,sn,user_code,status,create_time) " +
                    " values(#{agentNo},#{sn},#{userCode},#{status},now())"
    )
    int InsertAgentTerminal(@Param("agentNo")String agentNo,@Param("sn") String sn,@Param("userCode") String userCode,@Param("status") int status);
}
