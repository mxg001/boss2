package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.RedUserConf;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface RedUserConfDao {

    @Update("update red_user_conf set" +
            " user_limit=#{userLimit},member_limit=#{memberLimit},manager_limit=#{managerLimit}," +
            " banker_limit=#{bankerLimit},user_send_limit=#{userSendLimit},member_send_limit=#{memberSendLimit}," +
            " manager_send_limit=#{managerSendLimit},banker_send_limit=#{bankerSendLimit}," +
            " effective_time=#{effectiveTime},total_amount_min=#{totalAmountMin}," +
            " total_amount_max=#{totalAmountMax},red_number=#{redNumber},amount_min=#{amountMin}," +
            " scale_plate=#{scalePlate},scale_org=#{scaleOrg},scale_agent_total=#{scaleAgentTotal}," +
            " scale_user=#{scaleUser},scale_member=#{scaleMember},scale_manager=#{scaleManager}," +
            " scale_banker=#{scaleBanker},recovery_type=#{recoveryType},update_time=#{updateTime}," +
            " operator=#{operator}" +
            " where id = #{id}")
    int update(RedUserConf conf);

    @Select("select * from red_user_conf order by create_time desc limit 1")
    @ResultType(RedUserConf.class)
    RedUserConf getRedUserConf();

    @Select("select recovery_type from red_user_conf order by create_time desc limit 1")
    String getRecoveryType();
}
