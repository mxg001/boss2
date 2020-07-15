package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardAndReward;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.CouponActivityInfo;

public interface CardAndRewardManageDao {

	@SelectProvider(type = SqlProvider.class, method = "selectUserInfo")
	@ResultType(CardAndReward.class)
	List<CardAndReward> selectUserInfo(@Param("page") Page<CardAndReward> page, @Param("info") CardAndReward info);

	@SelectProvider(type = SqlProvider.class, method = "selectUserInfo")
	@ResultType(CardAndReward.class)
	List<CardAndReward> exportUserList(@Param("info") CardAndReward bean);
	
	
	@Select("SELECT id,coupon_name, coupon_amount,effective_days FROM coupon_activity_entity WHERE activetiy_code = #{activetiyCode} ")
	@ResultType(CouponActivityInfo.class)
	List<CouponActivityInfo> selectByKey(@Param("activetiyCode")String type);


	@Select("SELECT * FROM card_loan_hearten_log WHERE card_loan_hearten_id = #{id} order by create_time desc ")
	@ResultType(CardAndReward.class)
	List<CardAndReward> selectCardLoanHeartenLogById(@Param("id")String id);
	
	@Select("SELECT * FROM card_loan_hearten WHERE id = #{id} ")
	@ResultType(CardAndReward.class)
	CardAndReward selectCardLoanHeartenById(@Param("id")String id);

	@Select("SELECT * FROM coupon_activity_entity WHERE id = #{id} ")
	@ResultType(CouponActivityEntity.class)
	CouponActivityEntity selectCouponActivityInfoById(@Param("id")String sendTypeId);

	@Select("SELECT * FROM coupon_activity_info WHERE activetiy_code = #{type} ")
	@ResultType(CouponActivityInfo.class)
	CouponActivityInfo selectCouponActivityInfo(@Param("type")String type);

	@Update("update card_loan_hearten set given_type=#{info.givenType},given_status=#{info.givenStatus},update_time =#{info.updateTime},success_time=#{info.successTime},oper_time=#{info.operTime},oper_username=#{info.operUsername},ticket_id=#{info.ticketId},coupon_amount=#{info.couponAmount},effective_days=#{info.effectiveDays},given_channel=#{info.givenChannel} where id=#{info.id}")
	int updateCardLoanHearten(@Param("info")CardAndReward info);

	 @Insert("insert into card_loan_hearten_log ("
	 		+ "card_loan_hearten_id,username,phone,order_type,order_no,"
	 		+ "mech_name,org_name,given_channel,given_type,given_status,"
	 		+ "oper_username,oper_time,create_time,update_time,ticket_id,"
	 		+ "trans_amount,trans_time,status,success_time,effective_days,coupon_amount,merchant_no) "
	        + "values(#{info.id},#{info.username},#{info.phone},#{info.orderType},#{info.orderNo},"
	        + "#{info.mechName},#{info.orgName},#{info.givenChannel},#{info.givenType},#{info.givenStatus},"
	        + "#{info.operUsername},#{info.operTime},now(),#{info.updateTime},#{info.ticketId},"
	        + "#{info.transAmount},#{info.transTime},#{info.status},#{info.successTime},#{info.effectiveDays},#{info.couponAmount},#{info.merchantNo}"
	        + ")")
	 
	int insertCardLoanHeartenLog(@Param("info")CardAndReward card);

	@Select("SELECT * FROM card_loan_hearten WHERE merchant_no = #{merchantNo} and order_no = #{orderNo} ")
	@ResultType(CardAndReward.class)
    CardAndReward getCardLoanHearten(@Param("merchantNo")String merchantNo, @Param("orderNo")String orderNo);


    public class SqlProvider {

		public String selectUserInfo(Map<String, Object> param) {
			final CardAndReward info = (CardAndReward) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cu.*");
					FROM("card_loan_hearten cu");
					if (StringUtils.isNotBlank(info.getUsername())) {
						WHERE("cu.username LIKE #{info.username}");
					}
					if (StringUtils.isNotBlank(info.getPhone())) {
						WHERE("cu.phone = #{info.phone}");
					}
					if (StringUtils.isNotBlank(info.getOrgName())) {
						WHERE("cu.org_name = #{info.orgName}");
					}
					
					if (StringUtils.isNotBlank(info.getOrderNo())) {
						WHERE("cu.order_no = #{info.orderNo}");
					}
					if (StringUtils.isNotBlank(info.getStatus())) {
						if (info.getStatus().equals("5")) {
							WHERE("cu.status = 5");
						}else{
							WHERE("cu.status != 5");
						}
						
					}
					if (info.getGivenStatus()!=null) {
						WHERE("cu.given_status = #{info.givenStatus}");
					}
					if (StringUtils.isNotBlank(info.getMechName())) {
						WHERE("cu.mech_name = #{info.mechName}");
					}
					if (StringUtils.isNotBlank(info.getGivenType())) {
						WHERE("cu.given_type = #{info.givenType}");
					}
					
					//时间  交易 操作 赠送成功
					if (StringUtils.isNotBlank(info.getStransTime())) {
						WHERE("cu.trans_time >= #{info.stransTime}");
					}
					if (StringUtils.isNotBlank(info.getEtransTime())) {
						WHERE("cu.trans_time <= #{info.etransTime}");
					}
					if (StringUtils.isNotBlank(info.getSsuccessTime())) {
						WHERE("cu.success_time >= #{info.ssuccessTime}");
					}
					if (StringUtils.isNotBlank(info.getEsuccessTime())) {
						WHERE("cu.success_time <= #{info.esuccessTime}");
					}
					
					if (StringUtils.isNotBlank(info.getSoperTime())) {
						WHERE("cu.update_time >= #{info.soperTime}");
					}
					if (StringUtils.isNotBlank(info.getEoperTime())) {
						WHERE("cu.update_time <= #{info.eoperTime}");
					}
					
					WHERE("cu.order_type=#{info.orderType}");
					
					ORDER_BY("cu.create_time desc");
				}
			};
			return sql.toString();
		}

	}


}