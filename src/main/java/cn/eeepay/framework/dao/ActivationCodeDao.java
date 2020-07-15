package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.model.AgentInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface ActivationCodeDao {

    @Insert("INSERT INTO yfb_activation_code(uuid_code,code_type)VALUE(UUID(),#{bean.codeType})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="bean.id", before=false, resultType=Long.class)
    int insert(@Param("bean") ActivationCodeBean activationCodeBean);

    @InsertProvider(type=SqlProvider.class, method = "batchInsert")
    void batchInsert(@Param("count") int count, @Param("codeType") String codeType);

    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<ActivationCodeBean> listActivationCode(@Param("bean") ActivationCodeBean bean, Page<ActivationCodeBean> page);

//    @Select("select count(*) from yfb_activation_code where id between #{startId} and #{endId} and status=#{status}")
//    int countActivationCodeByStatus(@Param("startId") long startId,@Param("endId") long endId,@Param("status") String status);

    @Update("update yfb_activation_code " +
            "set status = 1, " +
            "agent_no = #{agent.agentNo}, " +
            "agent_node=#{agent.agentNode}," +
            "oem_no = #{oemNo}," +
            "root_agent_no = #{agent.agentNo} " +
            "where id between #{startId} and #{endId} " +
            "and code_type = #{codeType} " +
            "and status = 0")
    @ResultType(Long.class)
    long allotActivationCode2Agent(@Param("startId") long startId, @Param("endId") long endId, @Param("agent") AgentInfo agentInfo,
                                   @Param("oemNo") String oemNo, @Param("codeType") String codeType);

    @Update("update yfb_activation_code " +
            "set status = 0, " +
            "agent_no = null, " +
            "agent_node = null," +
            "oem_no = null," +
            "root_agent_no = null, " +
            "nfc_orig_code = null " +
            "where id between #{startId} and #{endId} " +
            "and code_type = #{codeType} " +
            "and status = 1 ")
    @ResultType(Long.class)
    long recoveryActivation(@Param("startId") long startId, @Param("endId") long endId, @Param("codeType") String codeType);

    @Select("select count(1) from yfb_service_cost where agent_no = #{agentNo} and service_type = #{serviceType}")
    int chechAgentOpenRepay(@Param("agentNo") String agentNo, @Param("serviceType") String serviceType);

    public static class SqlProvider{
        public String batchInsert(Map<String, Object> param){
            final int count = (Integer) param.get("count");
            StringBuilder batchInsertStr = new StringBuilder();
            batchInsertStr.append("insert into yfb_activation_code(uuid_code,code_type)values ");
            for (int i = 0; i < count; i ++){
                batchInsertStr.append("(UUID(),#{codeType}),");
            }
            batchInsertStr.deleteCharAt(batchInsertStr.lastIndexOf(","));
            return batchInsertStr.toString();
        }
        public String listActivationCode(Map<String, Object> param){
            final ActivationCodeBean bean = (ActivationCodeBean) param.get("bean");
            SQL sql = new SQL(){{
                SELECT("yac.id, yac.uuid_code,yac.unified_merchant_no," +
//                        "yrmi.user_name unifiedMerchantName,\n" +
                        "yac.agent_no,yac.root_agent_no one_agent_no,\n" +
                        "yac.status,yac.activate_time,\n" +
                        "yac.create_time,yac.update_time");
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
                if (StringUtils.isNotBlank(bean.getUnifiedMerchantNo())){
                    WHERE("yac.unified_merchant_no = #{bean.unifiedMerchantNo}");
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
                if(StringUtils.isNotBlank(bean.getCodeType())) {
                    WHERE("yac.code_type = #{bean.codeType}");
                }
            }};
            return sql.toString();
        }
    }
}
