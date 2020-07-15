package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface ActivityOrderInfoDao {

    @SelectProvider(type = SqlProvider.class,method = "actOrderInfoCount")
    @ResultType(Map.class)
    Map<String,Object> actOrderInfoCount(@Param("params") Map<String,String> params);


    @Select("select * from sys_dict where sys_key = #{sysKey} and sys_value = #{sysValue}")
    @ResultType(SysDict.class)
    SysDict sysDict(@Param("sysKey") String sysKey,@Param("sysValue") String sysValue);

    @SelectProvider(type = SqlProvider.class,method = "actOrderInfoQuery")
    @ResultType(Map.class)
    List<Map<String,Object>> actOrderInfoQuery(@Param("params") Map<String,String> params,Page<Map<String,Object>> page);


    @SelectProvider(type = SqlProvider.class,method = "actOrderInfoExport")
    @ResultType(Map.class)
    List<Map<String,Object>> actOrderInfoExport(@Param("params") Map<String, String> params);

    @Select(" select aoi.order_no orderNo,aoi.merchant_no merchantNo,aoi.trans_amount aoiTransAmount,aoi.trans_status aoiTransStatus," +
            "mi.merchant_name merchantName,\n" +
            " aoi.mobile_no mobileNo,aoi.coupon_code couponCode,cto.trans_amount transAmount,cto.merchant_no accMerNo,\n" +
            " cto.trans_status transStatus,aoi.create_time transTime,cto.settlement_method settlementMethod,cto.settle_status settleStatus ,aoi.remark ,aoi.pay_order_no payOrderNo,aoi.coupon_no couponNo from activity_order_info aoi \n" +
            " LEFT JOIN collective_trans_order cto on cto.order_no = aoi.pay_order_no\n" +
            " LEFT JOIN merchant_info mi on mi.merchant_no = aoi.merchant_no where aoi.id = #{id}")
    @ResultType(Map.class)
    Map<String,Object> actOrderInfo(@Param("id") String id);


    @Select(" select cae.coupon_name couponName,uc.start_time startTime ,uc.end_time endTime,\n" +
            " uc.face_value faceValue ,uc.gift_amount giftAmount,uc.coupon_amount couponAmount from user_coupon uc \n" +
            " LEFT JOIN coupon_activity_entity cae on cae.id = uc.activity_entity_id\n" +
            " where uc.coupon_no = #{couponNo} ")
    @ResultType(Map.class)
    Map<String,Object> queryCouponInfo(@Param("couponNo") String couponNo);

    @Update("update activity_order_info set remark = #{params.remark} where id = #{params.id} " )
    int actOrderRemarkUpdate(@Param("params") Map<String, String> params);


    @Select(" select si.service_name serviceName,bpd.bp_name bpName,cto.merchant_fee merchantFee,cto.merchant_rate merchantRate,\n" +
            " st.settle_type settleType,st.`status` ,st.create_time createTime,st.in_acc_no inAccNo,st.in_acc_name inAccName,\n" +
            " st.out_amount outAmount,st.in_bank_name inBankName,st.err_code errCode, st.err_msg errMsg from collective_trans_order cto \n" +
            " LEFT JOIN service_info si on cto.service_id = si.service_id\n" +
            " LEFT JOIN business_product_define bpd on bpd.bp_id = cto.business_product_id\n" +
            " LEFT JOIN settle_transfer st on st.order_no = cto.order_no\n" +
            " where cto.order_no =#{payOrderNo} ")
    @ResultType(Map.class)
    Map<String,Object> actOrderSettleInfoQuery(@Param("payOrderNo") String payOrderNo);

    public class SqlProvider{

        public String fromSql() {
            StringBuilder sb = new StringBuilder(" activity_order_info aoi ");
            sb.append(" LEFT JOIN collective_trans_order cto on cto.order_no = aoi.pay_order_no and aoi.pay_order_no<>'' ");
            sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no = aoi.merchant_no");
            sb.append(" LEFT JOIN agent_info ai on ai.agent_no=mi.agent_no");
            sb.append(" LEFT JOIN agent_info oai on oai.agent_no=mi.one_agent_no");
            return sb.toString();
        }

        public void whereSql(Map<String,String> param,SQL sql){
            if(param == null){
                return;
            }
            if(!StringUtil.isEmpty(param.get("orderNo"))){
                sql.WHERE(" aoi.order_no = #{params.orderNo}");
            }
            if(!StringUtil.isEmpty(param.get("merchantName"))){
                sql.WHERE(" ( mi.merchant_no like CONCAT('%',#{params.merchantName},'%') or mi.merchant_name like CONCAT('%',#{params.merchantName},'%') ) ");
            }
            if(!StringUtil.isEmpty(param.get("mobileNo"))){
                sql.WHERE(" aoi.mobile_no = #{params.mobileNo}");
            }
            //因为大POS购买鼓励金不涉及cto表，所以改成aoi的创建时间
            /*if(!StringUtil.isEmpty(param.get("startTime"))){
                sql.WHERE(" cto.trans_time >= #{params.startTime} ");
            }
            if(!StringUtil.isEmpty(param.get("endTime"))){
                sql.WHERE(" cto.trans_time <= #{params.endTime} ");
            }*/
            if(!StringUtil.isEmpty(param.get("startTime"))){
                sql.WHERE(" aoi.create_time >= #{params.startTime} ");
            }
            if(!StringUtil.isEmpty(param.get("endTime"))){
                sql.WHERE(" aoi.create_time <= #{params.endTime} ");
            }
            //大POS购买鼓励金的金额保存在aoi表
            if(!StringUtil.isEmpty(param.get("transAmountBeg"))){
                sql.WHERE(" (cto.trans_amount >= #{params.transAmountBeg} or aoi.trans_amount >= #{params.transAmountBeg})");
            }
            if(!StringUtil.isEmpty(param.get("transAmountEnd"))){
                sql.WHERE(" (cto.trans_amount <= #{params.transAmountEnd} or aoi.trans_amount <= #{params.transAmountEnd})");
            }
            if(!StringUtil.isEmpty(StringUtil.filterNull(param.get("couponCode")))){
                sql.WHERE(" aoi.coupon_code = #{params.couponCode}");
            }
            //大POS的trans_status保存在aoi表
            if(!StringUtil.isEmpty(StringUtil.filterNull(param.get("transStatus")))){
                sql.WHERE(" (cto.trans_status = #{params.transStatus} or aoi.trans_status=#{params.transStatus})");
            }
            if(!StringUtil.isEmpty(StringUtil.filterNull(param.get("agentNo")))){
                sql.WHERE(" mi.agent_no = #{params.agentNo}");
            }
            if(!StringUtil.isEmpty(StringUtil.filterNull(param.get("oneAgentNo")))){
                sql.WHERE(" mi.one_agent_no = #{params.oneAgentNo}");
            }
            if(StringUtils.isNotBlank(param.get("payMethod"))){
                sql.WHERE("aoi.pay_method=#{params.payMethod}");
            }
            if(StringUtils.isNotBlank(param.get("payOrderNo"))){
                sql.WHERE("aoi.pay_order_no=#{params.payOrderNo}");
            }
        }

        public String actOrderInfoExport(final Map<String,Object> params){
            final Map<String ,String> param = (Map<String ,String>)params.get("params") ;
            SQL sql =   new SQL(){
                {
                    SELECT(" aoi.id,aoi.order_no orderNo,aoi.merchant_no merchantNo,aoi.pay_method payMethod,aoi.trans_amount aoiTransAmount,aoi.trans_status aoiTransStatus," +
                            "mi.merchant_name merchantName,\n" +
                            " aoi.mobile_no mobileNo,cto.trans_amount transAmount,aoi.coupon_code couponCode ,cto.trans_status transStatus,\n" +
                            " cto.merchant_no merAccNo,aoi.create_time transTime,aoi.pay_order_no payOrderNo,aoi.remark,ai.agent_name agentName,oai.agent_name oneAgentName ");
                    FROM(fromSql());
                }
            };
            whereSql(param,sql);
            sql.ORDER_BY(" aoi.id desc");
            return sql.toString();
        }

        public String actOrderInfoQuery(final Map<String,Object> params){
            final Map<String ,String> param = (Map<String ,String>)params.get("params") ;
            SQL sql =   new SQL(){
                {
                    SELECT(" aoi.id,aoi.order_no orderNo,aoi.merchant_no merchantNo,aoi.pay_method payMethod," +
                            "aoi.pay_order_no payOrderNo,aoi.trans_amount aoiTransAmount,aoi.trans_status aoiTransStatus,mi.merchant_name merchantName,\n" +
                            " aoi.mobile_no mobileNo,cto.trans_amount transAmount,aoi.coupon_code couponCode ,cto.trans_status transStatus,\n" +
                            " cto.merchant_no merAccNo,cto.trans_time transTime,aoi.pay_order_no payOrderNo,aoi.create_time createTime,aoi.remark,ai.agent_name agentName,oai.agent_name oneAgentName ");
                    FROM(fromSql());
                }
            };
            whereSql(param,sql);
            sql.ORDER_BY(" aoi.id desc");
            return sql.toString();
        }

        public String actOrderInfoCount(final Map<String,Object> params){
            final Map<String ,String> param = (Map<String ,String>)params.get("params") ;
            SQL sql =   new SQL(){
                {
                    /*SELECT(" sum(cto.trans_amount) transAmount ");*/
                    SELECT("IFNULL(SUM(cto.trans_amount),0)+IFNULL(SUM(aoi.trans_amount),0) as transAmount");
                    FROM(fromSql());
                }
            };
            whereSql(param,sql);
            return sql.toString();
        }
    }
}
