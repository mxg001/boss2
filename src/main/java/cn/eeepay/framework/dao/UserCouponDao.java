package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserCoupon;
import cn.eeepay.framework.util.StringUtil;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface UserCouponDao {


    @SelectProvider(type = SqlProvider.class,method = "userCouponListIm")
    @ResultType(Map.class)
    List<Map<String,Object>> userCouponList(@Param("params")Map<String,String> params, Page<Map<String,Object>> page );

    @SelectProvider(type = SqlProvider.class,method = "userCpAcc")
    @ResultType(Map.class)
    Map<String,Object> userCpAcc(@Param("merchantNo") String merchantNo,@Param("codeNo") String[] codeNo);

    @Select("select sys_name text,sys_value value from sys_dict where sys_key = #{sysKey} and sys_value != 'INT'")
    @ResultType(Map.class)
    List<Map<String,Object>> coupCode(@Param("sysKey") String sysKey);

    @Select("select vf.id,vf.create_time startTime ,sum(verification_fee) verificationFee, " +
            " order_no orderNo ,uc.coupon_no couponNo,vf.type type from verification vf " +
            " left join user_coupon uc on vf.coupon_no = uc.coupon_no where vf.merchant_no = #{merchantNo} group by order_no ,type order by vf.create_time desc")
    @ResultType(Map.class)
    List<Map<String,Object>> verificationList(@Param("merchantNo") String merchantNo,Page<Map<String,Object>> page);

    @Select("select coupon_no cno from verification where order_no = #{orderNo} and type = #{type}")
    @ResultType(Map.class)
    List<Map<String,Object>> couponList(@Param("orderNo") String orderNo,@Param("type") String type);
    public class SqlProvider{


        public String userCpAcc(final Map<String,Object> params){
            final String merchantNo = StringUtil.filterNull(params.get("merchantNo"));
            final String[] codeNo = (String[])params.get("codeNo");
            SQL sql = new SQL(){{
                SELECT(" uc.coupon_code uCode,IFNULL(sum(face_value),0) allFaceValue ,IFNULL(sum(case when (coupon_status <> '4' and coupon_status <> '5' and CURDATE() <= end_time) then balance else 0 end ),0) allBalance, IFNULL((sum(face_value) - sum(balance) ),0) allUsedValue ,IFNULL(sum(case when coupon_status =  '4' or ( CURDATE() > end_time and coupon_status != '5') then balance else 0 end ),0) oldBalance from user_coupon uc" ) ;
                WHERE("  uc.merchant_no = #{merchantNo} ");
                StringBuilder sb = new StringBuilder();
                sb.append(" uc.coupon_code in (");
                for (String str : codeNo){
                    sb.append("'");
                    sb.append(str);
                    sb.append("'");
                    sb.append(",");
                }
                String temp = sb.toString();
                temp = temp.substring(0 ,temp.length()-1);
                temp+=")";
                WHERE(temp);
            }};
            return sql.toString();
        }

        public String userCouponListIm(final Map<String,Object> params){
            final Map<String ,String> param = (Map<String ,String>)params.get("params") ;

            return new SQL(){{
                SELECT("  uc.id,uc.coupon_code couponCode,uc.start_time startTime,uc.end_time endTime,uc.coupon_no coupNo " +
                        " ,IFNULL(cae.gift_amount,0) giftAmount,IFNULL(cto.trans_amount,0) transAmount,IFNULL(uc.face_value,0) faceValue \n" +
                        " ,IFNULL((uc.face_value - uc.balance),0) usedValue,uc.coupon_status couponStatus ," +
                        " case when (uc.coupon_status = '4' or CURDATE() > uc.end_time) then 0 else uc.balance end  balance,\n" +
                        " aoi.pay_order_no  orderNo " );
                FROM("  user_coupon uc \n" +
                        " left join activity_order_info aoi on uc.coupon_no = aoi.coupon_no " +
                        " LEFT JOIN coupon_activity_entity cae on cae.id = uc.activity_entity_id " +
                        " LEFT JOIN collective_trans_order cto on cto.order_no = aoi.pay_order_no ");
                if(!StringUtil.isEmpty(param.get("merchantNo"))){
                    WHERE(" uc.merchant_no = #{params.merchantNo} ");
                }
                if(!StringUtil.isEmpty(param.get("startTime"))){
                    WHERE(" uc.start_time >= #{params.startTime} ");
                }
                if(!StringUtil.isEmpty(param.get("endTime"))){
                    WHERE(" uc.start_time <= #{params.endTime} ");
                }
                if(!StringUtil.isEmpty(param.get("coupCode"))){
                    WHERE(" uc.coupon_code = #{params.coupCode} ");
                }
                if(!StringUtil.isEmpty(param.get("payAmountBeg"))){
                    WHERE(" uc.coupon_amount >= #{params.payAmountBeg} ");
                }
                if(!StringUtil.isEmpty(param.get("payAmountEnd"))){
                    WHERE(" uc.coupon_amount <= #{params.payAmountEnd} ");
                }

                if(!StringUtil.isEmpty(param.get("giftAmountBeg"))){
                    WHERE(" uc.gift_amount >= #{params.giftAmountBeg} ");
                }
                if(!StringUtil.isEmpty(param.get("giftAmountEnd"))){
                    WHERE(" uc.gift_amount <= #{params.giftAmountEnd} ");
                }

                if(!StringUtil.isEmpty(param.get("couponNoBeg"))&&StringUtil.isEmpty(param.get("couponNoEnd"))){
                    WHERE(" uc.coupon_no like CONCAT('%',#{params.couponNoBeg},'%')  ");
                }
                if(!StringUtil.isEmpty(param.get("couponNoEnd"))&&StringUtil.isEmpty(param.get("couponNoBeg"))){
                    WHERE("  uc.coupon_no like CONCAT('%',#{params.couponNoEnd},'%') ");
                }
                if(!StringUtil.isEmpty(param.get("couponNoBeg"))&&!StringUtil.isEmpty(param.get("couponNoEnd"))){
                    WHERE("  uc.coupon_no >= #{params.couponNoBeg} and  uc.coupon_no <= #{params.couponNoEnd} ");
                }
                ORDER_BY(" uc.id desc");

            }}.toString();

        }
        
        public String batchInsertexpCoupon(Map<String, Object> param){
        	final List<UserCoupon> list = (List<UserCoupon>) param.get("list");
        	StringBuilder sb = new StringBuilder();
			sb.append("insert into verification (order_no,merchant_no,coupon_no,face_value,balance,verification_fee,status,type) values ");
			MessageFormat message = new MessageFormat(
					"(#'{'list[{0}].token},#'{'list[{0}].merchantNo},#'{'list[{0}].couponNo},"
							+ "#'{'list[{0}].faceValue},#'{'list[{0}].balance},#'{'list[{0}].balance},"
							+ "#'{'list[{0}].couponStatus},#'{'list[{0}].cancelVerificationType}),");
			for (int i = 0; i < list.size(); i++) {
				sb.append(message.format(new Integer[] {i,0,3}));
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
        }
    }
    
    @Update("update user_coupon uc set uc.coupon_status='4'"
    		+ " where (uc.coupon_status='1' or uc.coupon_status='2') and uc.balance > 0 and"
    		+ " to_days(uc.end_time)<to_days(date(now()))")
	int updateCouponStatus();

    @Select("select uc.merchant_no,sum(uc.balance) as balance,cai.activity_notice "
    		+ "from user_coupon uc left join coupon_activity_info cai on uc.coupon_code=cai.activetiy_type "
    		+ "where (uc.coupon_code in ('3','6') or uc.coupon_type in ('3','6')) and uc.coupon_status='4' and uc.cancel_verification_code='2' "
    		+ "and uc.balance > 0 and to_days(uc.last_update_time)=to_days(now()) "
    		+ "group by uc.merchant_no")
	@ResultType(Map.class)
	List<Map<String, Object>> queryExpCoupon();

    @InsertProvider(type = SqlProvider.class, method = "batchInsertexpCoupon")
	int batchInsertexpCoupon(@Param("list") List<UserCoupon> expCoupon);

    @Select("select uc.merchant_no,uc.coupon_no,uc.face_value,uc.balance,uc.gift_amount,uc.coupon_type,uc.coupon_code,uc.end_time "
    		+ "from user_coupon uc where uc.balance > 0 "
    		+ "and uc.coupon_status='4' and uc.cancel_verification_code in ('2','1') "
    		+ "and to_days(uc.last_update_time)=to_days(now())")
    @ResultType(UserCoupon.class)
	List<UserCoupon> queryExpCoupons();
}
