package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CashBackAllAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CashBackAllAgentDao {
    @SelectProvider(type=SqlProvider.class,method="queryCashBackDetailAllAgentList")
    @ResultType(CashBackAllAgent.class)
    List<CashBackAllAgent> queryCashBackDetailAllAgentList(@Param("info") CashBackAllAgent info, @Param("page") Page<CashBackAllAgent> page);

    class SqlProvider{

        public String queryCashBackDetailAllAgentList(final Map<String, Object> param) {
            final CashBackAllAgent info = (CashBackAllAgent) param.get("info");
            SQL sql = new SQL(){{
                SELECT("pcbd.*,pad.activity_code,pad.activity_type_no,pad.trans_amount,pad.activity_time," +
                        "u.brand_code,u.real_name user_name,u1.user_code one_user_code,u1.real_name one_user_name," +
                        "pau.agent_no one_agent_no,pad.merchant_no");
                FROM("pa_cash_back_detail pcbd");
                LEFT_OUTER_JOIN("pa_activity_detail pad on pcbd.active_order=pad.active_order");
                LEFT_OUTER_JOIN("pa_user_info u on pcbd.user_code=u.user_code");
                LEFT_OUTER_JOIN("pa_user_info u1 on u.one_user_code=u1.user_code");
                LEFT_OUTER_JOIN("pa_agent_user pau on pau.user_code=u1.user_code");
            }};
            where(sql, info);
            sql.ORDER_BY("pad.activity_time DESC");
            return sql.toString();
        }

        public void where(SQL sql, CashBackAllAgent info) {
            if(StringUtils.isNotBlank(info.getActiveOrder())){
                sql.WHERE("pcbd.active_order = #{info.activeOrder} ");
            }
            if(StringUtils.isNotBlank(info.getEntryStatus())){
                sql.WHERE("pcbd.entry_status = #{info.entryStatus} ");
            }
            if(StringUtils.isNotBlank(info.getBrandCode())){
                sql.WHERE("u.brand_code = #{info.brandCode} ");
            }
            if(StringUtils.isNotBlank(info.getUserName())){
                sql.WHERE("u.real_name = #{info.userName} ");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("u.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserName())){
                sql.WHERE("u1.real_name = #{info.oneUserName} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserCode())){
                sql.WHERE("u1.user_code = #{info.oneUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getActivityCode())){
                sql.WHERE("pad.activity_code = #{info.activityCode} ");
            }
            if(StringUtils.isNotBlank(info.getActivityTypeNo())){
                sql.WHERE("pad.activity_type_no = #{info.activityTypeNo} ");
            }
            if(StringUtils.isNotBlank(info.getStartTime())){
                sql.WHERE("pcbd.entry_time  >= #{info.startTime} ");
            }
            if(StringUtils.isNotBlank(info.getEndTime())){
                sql.WHERE("pcbd.entry_time  <= #{info.endTime} ");
            }
            if(StringUtils.isNotBlank(info.getActivityTimeStart())){
                sql.WHERE("pad.activity_time  >= #{info.activityTimeStart} ");
            }
            if(StringUtils.isNotBlank(info.getActivityTimeEnd())){
                sql.WHERE("pad.activity_time  <= #{info.activityTimeEnd} ");
            }
            if(StringUtils.isNotBlank(info.getOneAgentNo())){
                sql.WHERE("pau.agent_no  = #{info.oneAgentNo} ");
            }
            if(StringUtils.isNotBlank(info.getMerchantNo())){
                sql.WHERE("pad.merchant_no  = #{info.merchantNo} ");
            }
        }
    }
}
