package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.util.StringUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface VerificationInfoDao {


    @SelectProvider(type = SqlProvider.class,method = "verificationInfoQuery")
    @ResultType(Map.class)
    List<Map<String,Object>>  verificationInfoQuery(@Param("params") Map<String, String> params, Page<Map<String,Object>> page);


    @SelectProvider(type = SqlProvider.class,method = "verificationInfoQuery")
    @ResultType(Map.class)
    List<Map<String,Object>>  verificationAllInfoQuery(@Param("params") Map<String, String> params);

    @SelectProvider(type = SqlProvider.class,method = "verificationInfoCount")
    @ResultType(Map.class)
    Map<String,Object> verificationInfoCount(@Param("params") Map<String, String> params);





    public class SqlProvider{


        public String verificationInfoQuery(final Map<String,Object> params){

            final Map<String,String > param = (Map<String,String > )params.get("params");

            final SQL sql =   new SQL(){
                {
                    SELECT(" v.id,v.merchant_no merchantNo,mi.merchant_name merchantName," +
                            "mi.mobilephone mobileNo,ai.agent_name agentName,oneA.agent_name oneAgentName,\n" +
                            "sum(v.verification_fee) verFee, v.order_no orderNo,v.type,v.create_time createTime," +
                            " cto.trans_amount transAmount," +
                            "uc.coupon_code couponCode,uc.coupon_type couponType,uc.create_time getTime ");
                    FROM(fromSql());
                }
            };
            whereSql(param,sql);
            sql.GROUP_BY(" v.order_no");
            sql.GROUP_BY(" v.type ");
            //sql.ORDER_BY(" v.id desc ");
            sql.ORDER_BY(" uc.create_time desc ");
            return sql.toString();
        }

        public String verificationInfoCount(final Map<String,Object> params){

            final Map<String,String > param = (Map<String,String > )params.get("params");

            final SQL sql =   new SQL(){
                {
                    SELECT(" IFNULL(sum(v.verification_fee),0) verFee ");
                    FROM(fromSql());
                }
            };
            whereSql(param,sql);
            return sql.toString();
        }














        public String fromSql() {
            StringBuilder sb = new StringBuilder(" verification v ");
            sb.append(" LEFT JOIN collective_trans_order cto on cto.order_no = v.order_no ");
            sb.append(" LEFT JOIN user_coupon uc on uc.coupon_no = v.coupon_no ");
            sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no = v.merchant_no");
            sb.append(" LEFT JOIN agent_info oneA on oneA.agent_no = mi.one_agent_no");
            sb.append(" LEFT JOIN agent_info ai on ai.agent_no = mi.agent_no");
            return sb.toString();
        }

        public void whereSql(Map<String,String> param,SQL sql){
            if(param == null){
                return;
            }
            if(!StringUtil.isEmpty(param.get("orderNo"))){
                sql.WHERE(" v.order_no = #{params.orderNo}");
            }
            if(!StringUtil.isEmpty(param.get("merchantName"))){
                sql.WHERE(" ( mi.merchant_no like CONCAT('%',#{params.merchantName},'%') or mi.merchant_name like CONCAT('%',#{params.merchantName},'%') ) ");
            }
            if(!StringUtil.isEmpty(param.get("mobileNo"))){
                sql.WHERE(" mi.mobilephone = #{params.mobileNo}");
            }


            if(!StringUtil.isEmpty(param.get("useType"))){
                sql.WHERE(" v.type = #{params.useType}");
            }
            if(!StringUtil.isEmpty(param.get("startTime"))){
                sql.WHERE(" v.create_time >= #{params.startTime} ");
            }
            if(!StringUtil.isEmpty(param.get("endTime"))){
                sql.WHERE(" v.create_time <= #{params.endTime} ");
            }
            if(!StringUtil.isEmpty(param.get("getStartTime"))){
                sql.WHERE(" uc.create_time >= #{params.getStartTime} ");
            }
            if(!StringUtil.isEmpty(param.get("getEndTime"))){
                sql.WHERE(" uc.create_time <= #{params.getEndTime} ");
            }

            if(!StringUtil.isEmpty(param.get("agentName"))){
                sql.WHERE(" mi.agent_no = #{params.agentName}");
            }

            if(!StringUtil.isEmpty(param.get("verFeeBeg"))){
                sql.WHERE(" v.verification_fee >= #{params.verFeeBeg} ");
            }
            if(!StringUtil.isEmpty(param.get("verFeeEnd"))){
                sql.WHERE(" v.verification_fee <= #{params.verFeeEnd} ");
            }

            if(!StringUtil.isEmpty(param.get("couponNoBeg"))&&StringUtil.isEmpty(param.get("couponNoEnd"))){
                sql.WHERE(" v.coupon_no like CONCAT('%',#{params.couponNoBeg},'%')  ");
            }
            if(!StringUtil.isEmpty(param.get("couponNoEnd"))&&StringUtil.isEmpty(param.get("couponNoBeg"))){
                sql.WHERE("  v.coupon_no like CONCAT('%',#{params.couponNoEnd},'%') ");
            }
            if(!StringUtil.isEmpty(param.get("couponNoBeg"))&&!StringUtil.isEmpty(param.get("couponNoEnd"))){
                sql.WHERE("  v.coupon_no >= #{params.couponNoBeg} and  v.coupon_no <= #{params.couponNoEnd} ");
            }
            if(StringUtil.isNotBlank(param.get("couponCode"))){
                sql.WHERE("uc.coupon_code = #{params.couponCode}");
            }
            if(StringUtil.isNotBlank(param.get("couponType"))){
                sql.WHERE("uc.coupon_type = #{params.couponType}");
            }
        }


    }
}
