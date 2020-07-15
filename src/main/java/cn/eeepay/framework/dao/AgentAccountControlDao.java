package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAccountControl;
import cn.eeepay.framework.model.AgentInfo;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface AgentAccountControlDao {

    @SelectProvider(type =SqlProvider.class, method = "queryAgentAccountControl")
    @ResultType(AgentAccountControl.class)
    List<AgentAccountControl> queryAgentAccountControl(@Param("a")AgentAccountControl agentAccountControl,@Param("page") Page<AgentAccountControl> page);

    @Select("SELECT id,agent_no,agent_name,retain_amount,`status` from agent_account_control where default_status=1")
    @ResultType(AgentAccountControl.class)
    AgentAccountControl queryAgentAccountControlByDefault();

    @Update("update agent_account_control set retain_amount=#{a.retainAmount},status=#{a.status} where id=#{a.id}")
    int updateAgentAccountControlDefault(@Param("a")AgentAccountControl agentAccountControl);

    @Insert("insert into agent_account_control (agent_no,agent_name,retain_amount,status,create_time,default_status) values " +
            "(#{a.agentNo},#{a.agentName},#{a.retainAmount},#{a.status},now(),#{a.defaultStatus})")
    int insertAgentAccountControl(@Param("a")AgentAccountControl agentAccountControl);

    @Select("SELECT agent_no,agent_name from agent_info where agent_level=1 and status=1 and agent_no=#{agentNo}")
    @ResultType(AgentInfo.class)
    AgentInfo queryAgentByID(@Param("agentNo")String agentNo);

    @Select("SELECT count(agent_no) from agent_account_control where agent_no=#{agentNo}")
    int queryAgentAccountControlByAgentNoCount(@Param("agentNo")String agentNo);

    @Update("update agent_account_control set retain_amount=#{a.retainAmount},status=#{a.status} where agent_no=#{a.agentNo}")
    int updateAgentAccountControl(@Param("a")AgentAccountControl agentAccountControl);

    @Select("SELECT id,agent_no,agent_name,retain_amount,`status` from agent_account_control where agent_no=#{agentNo}")
    @ResultType(AgentAccountControl.class)
    AgentAccountControl queryAgentAccountControlByAgentNo(@Param("agentNo")String agentNo);

    @Delete("delete from agent_account_control where agent_no=#{agentNo}")
    int deleteAgentAccountControl(@Param("agentNo")String agentNo);

    public class SqlProvider {

        public String queryAgentAccountControl(Map<String, Object> param) {
            final AgentAccountControl a = (AgentAccountControl) param.get("a");
            String sql = new SQL() {
                {
                    SELECT("aac.id,aac.agent_no,aac.agent_name,aac.retain_amount,aac.`status`"
                    );
                    FROM("agent_account_control aac ");
                    WHERE("aac.default_status=0 ");
                    if (StringUtils.isNotBlank(a.getAgentNo())) {
                        WHERE(" aac.agent_no=#{a.agentNo}");
                    }
                    if (StringUtils.isNotBlank(a.getAgentName())) {
                        WHERE(" aac.agent_name=#{a.agentName}");
                    }
                }
            }.toString();
            return sql;
        }
    }
}
