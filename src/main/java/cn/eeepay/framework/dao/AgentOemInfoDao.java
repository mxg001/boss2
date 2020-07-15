package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface AgentOemInfoDao {

    @Insert("insert into agent_oem_info(one_agent_no,oem_type,create_time) values(#{oneAgentNo},#{oemType},now())")
    int insert(@Param("oneAgentNo") String oneAgentNo, @Param("oemType") String oemType);

    @Select("select count(1) from agent_oem_info where one_agent_no = #{oneAgentNo}")
    @ResultType(Integer.class)
    int checkExists(@Param("oneAgentNo") String oneAgentNo);
}
