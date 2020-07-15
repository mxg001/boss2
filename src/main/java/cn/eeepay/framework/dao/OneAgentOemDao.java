package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/10/010.
 * @author  liuks
 *  对应表：agent_oem_info
 *  oem一级代理商信息
 */
public interface OneAgentOemDao {

    //添加
    @Insert(
            "insert into agent_oem_info" +
                    "(one_agent_no,oem_type,create_time) " +
                    "values " +
                    "(#{oneAgentNo},#{oemType},now()) "
    )
    int insertOneAgentNo(@Param("oneAgentNo")String oneAgentNo,@Param("oemType")String oemType);

    @Select(
            "select * from agent_oem_info where one_agent_no=#{oneAgentNo}"
    )
    List<Map<Object,Object>> getOneAgentNo(@Param("oneAgentNo")String oneAgentNo);

    @Select(
            "select * from rdmp_agent_share_config where agent_no=#{agentNo}"
    )
    List<Map<Object,Object>> checkAgent(@Param("agentNo")String agentNo);

    @Insert(
            "INSERT INTO rdmp_agent_share_config" +
                    " (agent_no,share_rate,oem_fee,receive_share_rate,repayment_share_rate,share_type,create_time) " +
                    " VALUES " +
                    "(#{agentNo},#{share},#{fee},#{receiveShare},#{repaymentShare},'D',NOW())"
    )
    int insertOneAgentShare(@Param("agentNo")String agentNo,
                            @Param("share") BigDecimal share,
                            @Param("fee") BigDecimal fee,
                            @Param("receiveShare") BigDecimal receiveShare,
                            @Param("repaymentShare") BigDecimal repaymentShare
    );

    @Update(
            "update rdmp_agent_share_config " +
                    "set share_rate=#{share},oem_fee=#{fee},receive_share_rate=#{receiveShare}, " +
                    "    repayment_share_rate=#{repaymentShare} " +
                    " where agent_no=#{agentNo} "
    )
    int updateOneAgentShare(@Param("agentNo")String agentNo,
                            @Param("share") BigDecimal share,
                            @Param("fee") BigDecimal fee,
                            @Param("receiveShare") BigDecimal receiveShare,
                            @Param("repaymentShare") BigDecimal repaymentShare
    );
}
