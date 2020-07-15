package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface MerchantAllAgentDao {

    @SelectProvider(type=SqlProvider.class,method="queryMerchantAllAgent")
    @ResultType(MerchantAllAgent.class)
    List<MerchantAllAgent> queryMerchantAllAgent(@Param("info") MerchantAllAgent info, @Param("page") Page<MerchantAllAgent> page);

    @SelectProvider(type=SqlProvider.class,method="queryMerchantAllAgentCount")
    @ResultType(Map.class)
    Map<String,Object> queryMerchantAllAgentCount(@Param("info") MerchantAllAgent info);

    @Select("SELECT * FROM pa_mer_info where merchant_no=#{merchantNo}")
    @ResultType(MerchantAllAgent.class)
    MerchantAllAgent queryMerchantAllAgentByMerNo(@Param("merchantNo") String merchantNo);

    @Update("UPDATE pa_mer_info SET status=#{info.status},mobile_phone=#{info.mobilePhone} WHERE merchant_no=#{info.merchantNo}")
    int updateMerchantAllAgentByMerNo(@Param("info") MerchantAllAgent info);

    class SqlProvider{

        public String queryMerchantAllAgent(final Map<String, Object> param) {
            final MerchantAllAgent info = (MerchantAllAgent) param.get("info");
            SQL sql = new SQL(){{
                SELECT("pmi.id,pmi.merchant_no,pmi.create_time,pmi.mobile_phone,u.user_code,u.real_name,u.nick_name," +
                        "u.mobile,u.brand_code,u2.user_code one_user_code,pau.agent_no one_agent_no,pb.brand_name");
                FROM("pa_mer_info pmi");
                LEFT_OUTER_JOIN("pa_user_info u on u.user_code=pmi.user_code");
                LEFT_OUTER_JOIN("pa_user_info u2 on u2.user_code=u.one_user_code");
                LEFT_OUTER_JOIN("pa_agent_user pau on pau.user_code=u2.user_code");
                LEFT_OUTER_JOIN("pa_brand pb on pb.brand_code=u.brand_code");
            }};
            where(sql, info);
            sql.ORDER_BY("pmi.create_time DESC");
            return sql.toString();
        }

        public String queryMerchantAllAgentCount(final Map<String, Object> param) {
            final MerchantAllAgent info = (MerchantAllAgent) param.get("info");
            SQL sql = new SQL(){{
                SELECT("count(*) merchantCount");
                FROM("pa_mer_info pmi");
                LEFT_OUTER_JOIN("pa_user_info u on u.user_code=pmi.user_code");
                LEFT_OUTER_JOIN("pa_user_info u2 on u2.user_code=u.one_user_code");
                LEFT_OUTER_JOIN("pa_agent_user pau on pau.user_code=u2.user_code");
                LEFT_OUTER_JOIN("pa_brand pb on pb.brand_code=u.brand_code");
            }};
            where(sql, info);
            return sql.toString();
        }

        public void where(SQL sql, MerchantAllAgent info) {
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("u.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getRealName())){
                sql.WHERE("u.real_name = #{info.realName} ");
            }
            if(StringUtils.isNotBlank(info.getNickName())){
                sql.WHERE("u.nick_name like concat('%',#{info.nickName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getMobile())){
                sql.WHERE("u.mobile = #{info.mobile} ");
            }
            if(StringUtils.isNotBlank(info.getMerchantNo())){
                sql.WHERE("pmi.merchant_no  = #{info.merchantNo} ");
            }
            if(StringUtils.isNotBlank(info.getMobilePhone())){
                sql.WHERE("pmi.mobile_phone  = #{info.mobilePhone} ");
            }
        }

    }
}
