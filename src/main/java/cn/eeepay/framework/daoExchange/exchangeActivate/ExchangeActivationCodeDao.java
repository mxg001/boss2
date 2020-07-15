package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivationCodeBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface ExchangeActivationCodeDao {

    @Insert("INSERT INTO yfb_activation_code(uuid_code)VALUE(UUID())")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="bean.id", before=false, resultType=Long.class)
    int insert(@Param("bean") ExchangeActivationCodeBean activationCodeBean);

    @InsertProvider(type=SqlProvider.class, method = "batchInsert")
    void batchInsert(@Param("count") int count);

    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<ExchangeActivationCodeBean> listActivationCode(@Param("bean") ExchangeActivationCodeBean bean, Page<ExchangeActivationCodeBean> page);

//    @Select("select count(*) from yfb_activation_code where id between #{startId} and #{endId} and status=#{status}")
//    int countActivationCodeByStatus(@Param("startId") long startId,@Param("endId") long endId,@Param("status") String status);

    @Update("update yfb_activation_code " +
            "set status = 1, " +
            "parent_id = 0, " +
            "agent_no = #{agent.agentNo}, " +
            "agent_node=#{agent.agentNode}," +
            "oem_no = #{oemNo}," +
            "one_agent_no = #{agent.agentNo} " +
            "where id between #{startId} and #{endId} " +
            "and status = 0")
    @ResultType(Long.class)
    long allotActivationCode2Agent(@Param("startId") long startId, @Param("endId") long endId, @Param("agent") AgentInfo agentInfo, @Param("oemNo") String oemNo);

    @Update("update yfb_activation_code " +
            "set status = 0, " +
            "agent_no = null, " +
            "agent_node = null," +
            "oem_no = null," +
            "parent_id = 0," +
            "one_agent_no = null " +
            "where id between #{startId} and #{endId} " +
            "and status = 1 ")
    @ResultType(Long.class)
    long recoveryActivation(@Param("startId") long startId, @Param("endId") long endId);


    @Select("select oem_no from yfb_oem_agent where agent_no = #{agentNo} limit 1")
    String queryOemNoByAgentNo(@Param("agentNo") String agentNo);

    @Select("select count(1) from yfb_agent_share_config where agent_no = #{agentNo}")
    int chechAgentOpenExchange(@Param("agentNo")String agentNo);


    public static class SqlProvider{
        public String batchInsert(Map<String, Object> param){
            final int count = (Integer) param.get("count");
            StringBuilder batchInsertStr = new StringBuilder();
            batchInsertStr.append("insert into yfb_activation_code(uuid_code)values ");
            for (int i = 0; i < count; i ++){
                batchInsertStr.append("(UUID()),");
            }
            batchInsertStr.deleteCharAt(batchInsertStr.lastIndexOf(","));
            return batchInsertStr.toString();
        }
        public String listActivationCode(Map<String, Object> param){
            final ExchangeActivationCodeBean bean = (ExchangeActivationCodeBean) param.get("bean");
            SQL sql = new SQL(){{

                SELECT("yac.id, yac.uuid_code,yac.mer_no,yac.agent_no,yac.one_agent_no, yac.status,yac.activate_time, yac.create_time,yac.update_time");
                FROM("yfb_activation_code yac");
//                LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = yac.agent_no");
//                LEFT_OUTER_JOIN("agent_info pai ON pai.agent_no = ai.one_level_id");
//                LEFT_OUTER_JOIN("yfb_repay_merchant_info yrmi ON yrmi.merchant_no = yac.unified_merchant_no");
                if (StringUtils.isNotBlank(bean.getAgentNode())){
                    WHERE("yac.agent_node like CONCAT(#{bean.agentNode}, '%')");
                }
                if (StringUtils.isNotBlank(bean.getStatus())){
                    WHERE("yac.status = #{bean.status}");
                }
                if (StringUtils.isNotBlank(bean.getMerNo())){
                    WHERE("yac.mer_no = #{bean.merNo}");
                }
                if (StringUtils.isNotBlank(bean.getMinId())){
                    WHERE("yac.id >= #{bean.minId}");
                }
                if (StringUtils.isNotBlank(bean.getMaxId())){
                    WHERE("yac.id <= #{bean.maxId}");
                }
                if (StringUtils.isNotBlank(bean.getStartActivateTime())){
                    WHERE("yac.activate_time >= #{bean.startActivateTime}");
                }
                if (StringUtils.isNotBlank(bean.getEndActivateTime())){
                    WHERE("yac.activate_time <= #{bean.endActivateTime}");
                }
            }};
            return sql.toString();
        }
    }
}
