package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.CouponActivityInfo;
import cn.eeepay.framework.model.CouponActivityTime;
import cn.eeepay.framework.model.CouponTotal;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;
	/**
	 * 
	 * @author ivan
	 *
	 */
public interface CouponActivityDao {
	/**
	 * 优惠券活动列表
	 * @return
	 */
	@Select("select ra.id,ra.activetiy_code,sd.sys_name as activetiy_type,ra.activity_explain "
			+ "from coupon_activity_info ra left join sys_dict sd on sd.sys_value = ra.activetiy_type "
			+ "where sd.sys_key ='COUPON_CODE' order by (ra.activetiy_code+0)")
    @ResultType(CouponActivityInfo.class)
    List<CouponActivityInfo> getAllInfo();
	
	@Select("Select count(1) from card_loan_hearten where id=#{id} and given_status=1")
    @ResultType(Integer.class)
	int selectForStatusById(@Param("id")Object object);
	
	/**
	 * 优惠券活动详情
	 * @param actId
	 * @return
	 */
	@Select("SELECT "+
			"ca.id, "+
			"ca.activity_name, "+
			"ca.activity_explain, "+
			"cae.coupon_amount, "+
			"sd.sys_name as activetiyType, "+
			"ca.activity_first, "+
			"cae.coupon_count, "+
			"ca.effective_days, "+
			"sd1.sys_name as cancel_verification_code, "+
			"ca.activity_notice, "+ 
			"cae.id as coupon_id,cae.trans_rate "+
			"FROM "+
			" coupon_activity_info ca "+
			"LEFT JOIN coupon_activity_entity cae on cae.activetiy_code = ca.activetiy_code "+
			"LEFT JOIN sys_dict sd ON sd.sys_value = cae.coupon_code and sd.sys_key = 'COUPON_CODE' "+
			"LEFT JOIN sys_dict sd1 ON sd1.sys_value = cae.cancel_verification_code and sd1.sys_key = 'CANCEL_VERIFICATION_CODE' "+
			"WHERE "+
			"	ca.`status` = '0' "+
			"AND ca.id = #{actId}")
    @ResultType(CouponActivityInfo.class)
	CouponActivityInfo queryRewardDetail(int actId);

	@Select("SELECT ca.id, ca.activity_name, ca.activity_explain, sd.sys_name as activetiyType, ca.activity_first, "+
			"cae.coupon_count, ca.effective_days, sd1.sys_name as cancel_verification_code, ca.activity_notice "+ 
			"FROM coupon_activity_info ca "+
			"LEFT JOIN coupon_activity_entity cae on cae.activetiy_code = ca.activetiy_code "+
			"LEFT JOIN sys_dict sd ON sd.sys_value = cae.coupon_code and sd.sys_key = 'COUPON_CODE' "+
			"LEFT JOIN sys_dict sd1 ON sd1.sys_value = cae.cancel_verification_code and sd1.sys_key = 'CANCEL_VERIFICATION_CODE' "+
			"WHERE ca.`status` = '0' AND ca.id = 1 LIMIT 1")
	@ResultType(CouponActivityInfo.class)
	CouponActivityInfo queryRegisteredDetail();

	@Select("SELECT coupon_name, coupon_amount,effective_days FROM coupon_activity_entity WHERE activetiy_code = '1' ")
	@ResultType(CouponActivityInfo.class)
	List<CouponActivityInfo> selectRegisteredCoupon();

	/**
	 * 修改注册返券面值
	 * @author	mays
	 * @date	2018年1月23日
	 */
	@Update("update coupon_activity_entity set coupon_amount = #{info.couponAmount},"
			+ " update_time = now(),effective_days=#{info.effectiveDays}, operator = #{info.operator} where coupon_name = #{info.couponName}")
	int updateRegisteredCoupon(@Param("info") CouponActivityInfo info);

	/**
	 * 更新优惠券活动信息
	 * @param reward
	 * @return
	 */
	@Update("update coupon_activity_info set activity_explain =#{reward.activityExplain},"
			+ " activity_first=#{reward.activityFirst},"
			+ " activity_notice=#{reward.activityNotice},update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where id=#{reward.id}")
	int rewardEntrySave(@Param("reward") CouponActivityInfo reward);
	/**
	 * 更新活动优惠券配置
	 * @param reward
	 * @return
	 */
	@Update("update coupon_activity_entity set coupon_amount =#{reward.couponAmount},"
			+ " coupon_count=#{reward.couponCount},update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where id=#{reward.couponId}")
	int rewardSave(@Param("reward") CouponActivityInfo reward);

	
	@Update("update coupon_activity_entity set "
			+ " coupon_count=#{reward.couponCount},update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where id=#{reward.couponId}")
	int cardAndRewardSave(@Param("reward") CouponActivityInfo reward);
	
	@Update("update coupon_activity_entity set "
			+ " coupon_count=#{reward.couponCount},update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where activetiy_code = '1'")
	int registeredSave(@Param("reward") CouponActivityInfo reward);

	/**
	 * 明细查询
	 * @param
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectCouponAllInfo")
	@ResultType(CouponTotal.class)
	List<CouponTotal> queryCouponAll(@Param("coupon") CouponTotal coupon);
	

	/**
	 * 查询统计
	 * @param coupon
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectCouponTotal")
	@ResultType(CouponTotal.class)
	CouponTotal queryCouponAllTotal(@Param("coupon") CouponTotal coupon);

		@SelectProvider(type=SqlProvider.class,method="queryCouponList")
		@ResultType(CouponTotal.class)
		List<CouponTotal> queryCouponList(@Param("coupon") CouponTotal coupon);

        @Select("select * from sys_config where param_key = #{key} limit 1")
        @ResultType(Map.class)
        Map<String,Object> querySysConfig(@Param("key") String key);

		@SelectProvider(type=SqlProvider.class,method="actEntityList")
		@ResultType(Map.class)
        List<Map<String,Object>> actEntityList(@Param("couponCoude") String couponCoude);

		@Select("select * from activity_order_info where pay_order_no = #{orderNo} limit 1")
		@ResultType(Map.class)
		Map<String,Object> activtyOrderInfo(@Param("orderNo") String orderNo);

		@Select("select * from user_coupon where id = #{id}")
		@ResultType(Map.class)
		Map<String,Object> couponInfo(@Param("id") String id);

        @Update(" update merchant_info set bonus_flag = 1 where merchant_no = #{merchantNo}")
        int updateMerActStatus(@Param("merchantNo") String merchantNo);

		@Update("update activity_order_info set coupon_no = #{couponNo} where pay_order_no = #{orderNo} ")
		int addCouponOrderInfo(@Param("couponNo") String couponNo, @Param("orderNo") String orderNo);

		@Update("update user_coupon set coupon_status = '5' where id = #{id}")
		int updateCouponStatus(@Param("id") String id);

		@Select("select nextval(#{seqName}) as t")
		@ResultType(Map.class)
		Map<String,Object> getNext(@Param("seqName") String seqName);

		@Select("SELECT i.activetiy_code  ,e.coupon_code, e.coupon_amount ,e.gift_amount,e.effective_days , " +
				"e.coupon_count , e.cancel_verification_code , e.activity_first , e.id , e.coupon_amount, e.gift_amount,e.back_rate,e.coupon_standard ,e.coupon_type" +
				" from coupon_activity_info i ,coupon_activity_entity e " +
				" where i.activetiy_code=e.activetiy_code " +
				"  and e.id=#{activityEntityId}")
		@ResultType(Map.class)
		Map<String,Object> getActivityById(@Param("activityEntityId") int activityEntityId);

		@Select("SELECT i.activetiy_code , e.effective_days , e.coupon_amount ,e.gift_amount," +
				" e.coupon_count , e.cancel_verification_code , i.activity_first , e.coupon_amount, e.gift_amount,e.coupon_type, " +
				" e.activity_type " +
				" from coupon_activity_info i ,coupon_activity_entity e " +
				" where i.activetiy_code=e.activetiy_code " +
				" and i.activetiy_code=#{couponCode} and e.id=#{activityEntityId}")
		@ResultType(Map.class)
		Map<String,Object> getActivityById2(@Param("couponCode") String couponCode, @Param("activityEntityId") int activityEntityId);

		@Select(" SELECT i.activetiy_code , i.effective_days , e.coupon_amount ,e.gift_amount, " +
						" e.coupon_count , e.cancel_verification_code , i.activity_first , e.coupon_amount, e.gift_amount,e.coupon_type " +
						" from coupon_activity_info i ,coupon_activity_entity e " +
						" where i.activetiy_code=e.activetiy_code " +
						" and i.activetiy_code=#{couponCode} ")
		@ResultType(Map.class)
		Map<String,Object> getActivityByCode(@Param("couponCode") String couponCode);

		
		
		
		@Select("select * from coupon_activity_entity where id=#{couponCode}")
		@ResultType(Map.class)
		Map<String,Object> getCouponActivityEntityById(@Param("couponCode") String couponCode);

		@Update("insert into user_coupon(coupon_no,face_value,balance,coupon_code,cancel_verification_code," +
				"coupon_status,start_time,end_time,token,merchant_no,activity_first,create_time,last_update_time," +
				" activity_entity_id, coupon_amount , gift_amount,coupon_type,coupon_standard,back_rate ) values(#{couponMap.coupon_no}," +
				"#{couponMap.face_value},#{couponMap.balance},#{couponMap.coupon_code},#{couponMap.cancel_verification_code},#{couponMap.coupon_status}" +
				",#{couponMap.start_time},#{couponMap.end_time},#{couponMap.token},#{couponMap.merchant_no},#{couponMap.activity_first}" +
				",now(),now(),#{couponMap.activity_entity_id},#{couponMap.coupon_amount},#{couponMap.gift_amount},#{couponMap.coupon_type}" +
				",#{couponMap.coupon_standard},#{couponMap.back_rate})")
		int addUserCoupon(@Param("couponMap") Map<String, Object> couponMap);

		@Select("select * from coupon_activity_info where activetiy_code = #{activetiyCode}")
		@ResultType(CouponActivityInfo.class)
		CouponActivityInfo selectInfoDetail(@Param("activetiyCode") String activetiyCode);

		@Update("update coupon_activity_info set activity_explain=#{activityExplain},status=#{status}," +
				"operator=#{operator},update_time=#{updateTime} where activetiy_code = #{activetiyCode}")
		int updateInfo(CouponActivityInfo baseInfo);

		@Select("select * from activity_vip where activity_code = #{activityCode} ORDER BY sort_num")
		@ResultType(Map.class)
		List<Map> couponActivityVip(@Param("activityCode") String activityCode);

		@Update("update activity_vip set is_recommend =#{val}"
				+ " where id=#{id}")
		int updateIsRecommend(@Param("id") int id, @Param("val")int val);

		@Update("update activity_vip set is_recommend =0 where team_id=#{teamId}")
		int updateIsRecommendClose(@Param("teamId") String teamId);

		@Update("update activity_vip set is_switch =#{val}"
				+ " where id=#{id}")
		int updateIsSwitch(@Param("id") int id, @Param("val")int val);

		@Insert("INSERT INTO activity_vip (name,time,original_price,discount_price,sort_num,is_recommend,"
				+ "is_switch,create_time,record_creator,activity_code,team_id)"+
				" VALUES (#{map.name},#{map.time},#{map.original_price},#{map.discount_price},#{map.sort_num},0,"
				+ "0,now(),#{map.record_creator},#{map.activity_code},#{map.team_id}) ")
		int addActivityVip(@Param("map") Map<String, Object> map);

		@Update("update activity_vip set name =#{map.name},time =#{map.time},original_price =#{map.original_price}," +
				"discount_price =#{map.discount_price},sort_num =#{map.sort_num},team_id=#{map.team_id},update_time =now()"
				+ " where id=#{map.id}")
		int updateActivityVip(@Param("map") Map<String, Object> map);

		@Select("select count(1) from user_coupon where activity_entity_id=#{activityEntityId} and create_time>=#{begin} and create_time<#{end}")
		@ResultType(Integer.class)
		Integer countTodayPurchaseCount(@Param("activityEntityId") Integer activityEntityId,@Param("begin")String begin,@Param("end")String end);

		@Select("select count(1) from user_coupon where activity_entity_id=#{activityEntityId} and create_time>=#{begin} and create_time<#{end} and merchant_no=#{merchantNo}")
		@ResultType(Integer.class)
		Integer countMerchantTodayPurchaseCount(@Param("activityEntityId") Integer activityEntityId, @Param("begin") String begin, @Param("end") String end, @Param("merchantNo") String merchantNo);

		@Insert("INSERT INTO coupon_activity_entity (activetiy_code,coupon_code,coupon_type," +
				"coupon_name,coupon_amount,effective_days,activity_first,coupon_standard,back_rate,coupon_explain,cancel_verification_code,operator) " +
				"values(#{entity.activetiyCode},#{entity.couponCode},#{entity.couponType},#{entity.couponName}," +
				"#{entity.couponAmount},#{entity.effectiveDays},#{entity.activityFirst},#{entity.couponStandard}," +
				"#{entity.backRate},#{entity.couponExplain},#{entity.cancelVerificationCode},#{entity.operator})")
		@Options(useGeneratedKeys=true, keyProperty="entity.id", keyColumn="id")
		int saveVipTicket(@Param("entity") CouponActivityEntity entity);

		@Update("update coupon_activity_entity set coupon_type = #{entity.couponType},coupon_name = #{entity.couponName},coupon_amount = #{entity.couponAmount}" +
				",effective_days = #{entity.effectiveDays},activity_first = #{entity.activityFirst},coupon_standard = #{entity.couponStandard},cancel_verification_code=#{entity.cancelVerificationCode} " +
				" ,back_rate = #{entity.backRate} ,coupon_explain = #{entity.couponExplain},operator=#{entity.operator} " +
				"   where id = #{entity.id}")
		int updateVipTicket(@Param("entity") CouponActivityEntity entity);

		@Delete("delete from coupon_activity_entity where id = #{id}")
		int delVipEntity(@Param("id") Integer id);

		class SqlProvider{

			public String actEntityList(Map<String,Object> param){
				final String couponCoude = (String) param.get("couponCoude");
				SQL sql = new SQL(){{
					SELECT("coupon_name couponName,id");
					FROM("coupon_activity_entity");
					if("8".equals(couponCoude)||"9".equals(couponCoude)){
						WHERE("coupon_code in ('3','6')");
					}else{
						WHERE("coupon_code = #{couponCoude}");
					}
				}};
				return sql.toString();
			}

    		public String selectCouponAllInfo(Map<String,Object> param){
				final CouponTotal coupon = (CouponTotal)param.get("coupon");
				SQL sql = new SQL(){{
					SELECT("coupon.id,"+
					" mer.merchant_name,"+
					" mer.merchant_no,"+
					" mer.mobilephone,"+
					" coupon.gift_amount,"+
					" aoi.pay_order_no,aoi.pay_method,"+
					"  coupon.coupon_amount trans_amount,"+
					" (select sd.sys_name from sys_dict sd where sd.sys_value = coupon.coupon_code AND sd.sys_key = 'COUPON_CODE' ) as coupon_code, "+
					" agent.agent_name,"+
					" oneAgent.agent_name as one_agent_name,"+
					" coupon.face_value,"+
					"CASE WHEN (coupon.coupon_status = 4 OR coupon.coupon_status = 5 OR DATEDIFF(coupon.end_time,CURRENT_DATE) < 0) THEN 0 ELSE coupon.balance END balance,"+
					" coupon.face_value - coupon.balance as use_value,"+
					" (select st.sys_name from sys_dict st where st.sys_value = coupon.coupon_status AND st.sys_key = 'COUPON_STATUS' ) as coupon_status, "+
					" coupon.start_time,"+
					" coupon.end_time,coupon.coupon_type,"+
					" coupon.coupon_no");
					FROM(fromSql());
				}};
				whereSql(coupon,sql);
				String sqlStr=sql.toString()+" LIMIT "+coupon.getPageFirst()+","+coupon.getPageSize();
				return sqlStr;
    		}

    		public String queryCouponList(Map<String,Object> param){
				final CouponTotal coupon = (CouponTotal)param.get("coupon");
				SQL sql = new SQL(){{
					SELECT("coupon.id,"+
					" mer.merchant_name,"+
					" mer.merchant_no,"+
					" mer.mobilephone,"+
					" coupon.gift_amount,"+
					" aoi.pay_order_no,aoi.pay_method,"+
					" coupon.coupon_amount trans_amount,"+
					" (select sd.sys_name from sys_dict sd where sd.sys_value = coupon.coupon_code AND sd.sys_key = 'COUPON_CODE' ) as coupon_code, "+
					" agent.agent_name,"+
					" oneAgent.agent_name as one_agent_name,"+
					" coupon.face_value,"+
					"CASE WHEN (coupon.coupon_status = 4 OR coupon.coupon_status = 5 OR DATEDIFF(coupon.end_time,CURRENT_DATE) < 0) THEN 0 ELSE coupon.balance END balance,"+
					" coupon.face_value - coupon.balance as use_value,"+
					" (select st.sys_name from sys_dict st where st.sys_value = coupon.coupon_status AND st.sys_key = 'COUPON_STATUS' ) as coupon_status, "+
					" coupon.start_time,"+
					" coupon.end_time,coupon.coupon_type,"+
					" coupon.coupon_no");
					FROM(fromSql());
				}};
				whereSql(coupon,sql);
				String sqlStr=sql.toString();
				return sqlStr;
    		}
			public String selectCouponTotal(Map<String,Object> param){
				final CouponTotal coupon = (CouponTotal)param.get("coupon");
				SQL sql = new SQL(){{
					SELECT("sum(coupon.face_value) AS total_face_value, "+
							" count(*) AS total_num, "+
							" sum(coupon.coupon_amount) AS trans_amount,"+
							" sum(coupon.gift_amount) AS gift_amount,"+
						   "sum(CASE WHEN (coupon.coupon_status <> 4 and coupon.coupon_status <> 5 and DATEDIFF(coupon.end_time,CURRENT_DATE) >= 0)   THEN coupon.balance ELSE 0 END) AS total_balance, "+
						   "sum(coupon.face_value - coupon.balance) AS  total_use_value, "+
						   "sum(CASE WHEN (coupon.coupon_status = '4' or ( DATEDIFF(coupon.end_time,CURRENT_DATE) < 0) and coupon.coupon_status != '5')    THEN coupon.balance ELSE 0 END) AS  total_invalid_value");
					FROM(fromSql());
				}};
				whereSql(coupon,sql);
				return sql.toString();
			}
			/**
			 * fromSQL：明细查询、统计
			 * @return
			 */
			private  String fromSql(){
				StringBuffer sql = new StringBuffer(" user_coupon coupon ");
				sql.append(" LEFT JOIN merchant_info mer ON mer.merchant_no = coupon.merchant_no");
				sql.append(" LEFT JOIN agent_info agent ON mer.agent_no = agent.agent_no");
				sql.append(" LEFT JOIN agent_info oneAgent ON mer.one_agent_no = oneAgent.agent_no");
				sql.append(" LEFT JOIN activity_order_info aoi ON aoi.coupon_no = coupon.coupon_no");
				return sql.toString();
			}
			/**
			 * whereSql：明细查询、统计
			 * @param coupon
			 * @param sql
			 */
			private  void whereSql(CouponTotal coupon,SQL sql){
				if(StringUtils.isNotBlank(coupon.getMerchantName())){
					sql.WHERE(" (mer.merchant_no=#{coupon.merchantName} or mer.merchant_name=#{coupon.merchantName})");
				}
				if(StringUtils.isNotBlank(coupon.getMobilephone())){
					sql.WHERE(" mer.mobilephone=#{coupon.mobilephone}");
				}
				if(StringUtils.isNotBlank(coupon.getCouponNo())){
					sql.WHERE(" coupon.coupon_no=#{coupon.couponNo}");
				}
				if(StringUtils.isNotBlank(coupon.getCouponCode())){
					sql.WHERE(" coupon.coupon_code=#{coupon.couponCode}");
				}
				//券类型过滤
				if(StringUtils.isNotBlank(coupon.getCouponType())){
					sql.WHERE(" coupon.coupon_type=#{coupon.couponType}");
				}
				if(StringUtils.isNotBlank(coupon.getAgentName())){
					sql.WHERE(" mer.agent_no=#{coupon.agentName}");
				}
				if(StringUtils.isNotBlank(coupon.getOneAgentName())){
					sql.WHERE(" mer.one_agent_no=#{coupon.oneAgentName}");
				}
				if(coupon.getStartTime() != null){
					sql.WHERE(" coupon.create_time>=#{coupon.startTime}");
				}
				if(coupon.getEndTime() != null){
					sql.WHERE(" coupon.create_time<=#{coupon.endTime}");
				}
				if(coupon.getEndStartTime() != null){
					sql.WHERE(" coupon.end_time>=#{coupon.endStartTime}");
				}
				if(coupon.getEndEndTime() != null){
					sql.WHERE(" coupon.end_time<=#{coupon.endEndTime}");
				}
				if(coupon.getTransAmountBeg() != null){
					sql.WHERE("  coupon.coupon_amount >= #{coupon.transAmountBeg}");
				}
				if(coupon.getTransAmountEnd() != null){
					sql.WHERE(" coupon.coupon_amount <= #{coupon.transAmountEnd}");
				}

				if(coupon.getGiftAmountBeg() != null){
					sql.WHERE(" coupon.gift_amount >= #{coupon.giftAmountBeg}");
				}
				if(coupon.getGiftAmountEnd() != null){
					sql.WHERE(" coupon.gift_amount <= #{coupon.giftAmountEnd}");
				}
				if(StringUtils.isNotBlank(coupon.getPayMethod())){
					sql.WHERE("aoi.pay_method=#{coupon.payMethod}");
				}

				if(StringUtils.isNotBlank(coupon.getCouponNoBeg())&& StringUtils.isBlank(coupon.getCouponNoEnd())){
					sql.WHERE(" coupon.coupon_no like CONCAT('%', #{coupon.couponNoBeg},'%')");
				}else if(StringUtils.isBlank(coupon.getCouponNoBeg())&& StringUtils.isNotBlank(coupon.getCouponNoEnd())){
					sql.WHERE(" coupon.coupon_no like CONCAT('%', #{coupon.couponNoEnd},'%')");
				}else  if(StringUtils.isNotBlank(coupon.getCouponNoBeg() )&& StringUtils.isNotBlank(coupon.getCouponNoEnd())){
					sql.WHERE(" coupon.coupon_no >= #{coupon.couponNoBeg} and coupon.coupon_no <= #{coupon.couponNoEnd}");
				}
			}
    	
  }
	/**
	 * 
	 * @param actId
	 * @return
	 */
	@Select("select * from  coupon_activity_info where id=#{actId}")
    @ResultType(CouponActivityInfo.class)
	CouponActivityInfo queryCouponRecharge(int actId);
	/**
	 * 
	 * @return
	 */
	@Select("select cae.id,cae.order_by,cae.activetiy_code,cae.coupon_name,case  when  (cae.coupon_count- IFNULL(uc.uccount ,0 ))<0 then 0 else (cae.coupon_count- IFNULL(uc.uccount ,0 )) end  as surplus_count,"+
			" cae.coupon_explain,case when cae.status = 0 then 1 else 0 end isstatus,cae.isshelves,cae.trans_rate, " +
			" cae.coupon_standard, cae.back_rate, cae.effective_days, cae.coupon_amount, cae.activity_first ,cae.cancel_verification_code"+
			" from coupon_activity_entity cae "+
		    " LEFT JOIN (select count(0) as uccount,activity_entity_id from user_coupon where create_time BETWEEN CONCAT(DATE(NOW()), ' 00:00:00') AND CONCAT(DATE(NOW()), ' 23:59:59') group by activity_entity_id ) uc on uc.activity_entity_id = cae.id"+
			" where cae.activetiy_code =#{activityCode}")
    @ResultType(CouponActivityEntity.class)
	List<CouponActivityEntity> couponRechargeEntry(String activityCode);
	/**
	 * 
	 * @return
	 */
	@Update("update coupon_activity_info set activity_explain =#{couponActivity.activityExplain},"
			+ " activity_notice=#{couponActivity.activityNotice},update_time=now(),operator=#{couponActivity.operator}," +
			"up_open_explan=#{couponActivity.upOpenExplan},open_explan=#{couponActivity.openExplan},buy_push=#{couponActivity.buyPush},maturity_give_push=#{couponActivity.maturityGivePush}"
			+ " where id=#{couponActivity.id}")
	int saveCouponActivity(@Param("couponActivity") CouponActivityInfo couponActivity);
	/**
	 * 
	 * @param entry
	 * @return
	 */
	@Insert("INSERT INTO coupon_activity_entity (order_by,activetiy_code,coupon_code,coupon_name,coupon_amount,coupon_count,"
			+ "cancel_verification_code,create_time,operator,status,gift_amount,coupon_explain,purchase_count,"
			+ "integral_scale,isshelves,activity_first,effective_days,coupon_type )"+
            " VALUES (#{entry.orderBy},#{entry.activetiyCode},#{entry.couponCode},#{entry.couponName},#{entry.couponAmount},#{entry.couponCount},"
            + "#{entry.cancelVerificationCode},now(),#{entry.operator},#{entry.isstatus},#{entry.giftAmount},"
            + "#{entry.couponExplain},#{entry.purchaseCount},#{entry.integralScale},#{entry.isshelves},"
            + "#{entry.activityFirst},#{entry.effectiveDays },#{entry.couponType }) ")
	@Options(useGeneratedKeys=true, keyProperty="entry.id", keyColumn="id")
	int insertCouponEntity(@Param("entry") CouponActivityEntity entry);

		@Select("SELECT * from coupon_activity_entity where id=#{id}")
		@ResultType(CouponActivityEntity.class)
		CouponActivityEntity selectCouponEntityById(@Param("id") Integer id);

	/**
	 * 
	 * @param entry
	 * @return
	 */
	@Update("update coupon_activity_entity set update_time=now(),operator=#{entry.operator},coupon_count=#{entry.couponCount},"
			+ "purchase_count=#{entry.purchaseCount},coupon_explain=#{entry.couponExplain},coupon_amount=#{entry.couponAmount},"
			+ "gift_amount=#{entry.giftAmount},activity_first=#{entry.activityFirst},order_by=#{entry.orderBy},effective_days=#{entry.effectiveDays},coupon_name=#{entry.couponName}"
			+ " where id=#{entry.id}")
	int saveCouponEntity(@Param("entry") CouponActivityEntity entry);

	@Insert("INSERT INTO coupon_activity_time (entity_id,start_time,end_time) VALUES " +
			"(#{c.entityId},#{c.startTime},#{c.endTime})")
	int insertCouponTime(@Param("c") CouponActivityTime couponActivityTime);

	@Update("update coupon_activity_time set start_time=#{c.startTime},end_time=#{c.endTime} where id=#{c.id}")
	int saveCouponTime(@Param("c") CouponActivityTime couponActivityTime);

	@Delete("delete from coupon_activity_time where id = #{id}")
	int deleteCouponTime(@Param("id") Integer id);

	@Delete("delete from coupon_activity_time where entity_id = #{entityId} and start_time<now() and end_time>now()")
	int deleteCouponTimeByEntityId(@Param("entityId") Integer entityId);

	@Select("select id,entity_id,DATE_FORMAT(start_time, '%Y-%m-%d %H:%i:%S') start_time,DATE_FORMAT(end_time, '%Y-%m-%d %H:%i:%S') end_time from coupon_activity_time where entity_id = #{entityId}")
	@ResultType(CouponActivityTime.class)
	List<CouponActivityTime> getCouponActivityTime(@Param("entityId") Integer entityId);

	@Select("SELECT ce.id,ce.isshelves,sum(if(ct.start_time<=now() and ct.end_time>now(),1,0)) sum " +
			"from coupon_activity_entity ce LEFT JOIN coupon_activity_time ct on ct.entity_id=ce.id " +
			"where ct.id is not null GROUP BY ce.id")
	@ResultType(CouponActivityEntity.class)
	List<CouponActivityEntity> getCouponActivityTimeJob();

	/**
	 * 
	 * @param id
	 * @param val
	 * @return
	 */
	@Update("update coupon_activity_entity set status =#{val}"
			+ " where id=#{id}")
	int updateStatus(@Param("id") int id, @Param("val")int val);
	
	/**
	 * 
	 * @param id
	 * @param val
	 * @return
	 */
	@Update("update coupon_activity_entity set isshelves =#{val}"
			+ " where id=#{id}")
	int updateIsshelves(@Param("id") int id, @Param("val")int val);
	@Select("select cae.id,cae.order_by,cae.activetiy_code,sd.sys_name as coupon_code,cae.coupon_name,cae.coupon_amount,cae.coupon_count,cae.activity_first,"+
			"sd1.sys_name as cancel_verification_code,cae.gift_amount,cae.coupon_explain,cae.purchase_count,cae.purchase_total,cae.integral_scale,cae.status as isstatus,cae.isshelves,cae.effective_days "+
			",cae.coupon_type,cae.coupon_standard,cae.back_rate,cae.cancel_verification_code from coupon_activity_entity cae "+
			"LEFT JOIN sys_dict sd ON sd.sys_value = cae.coupon_code and sd.sys_key = 'COUPON_CODE' "+
			"LEFT JOIN sys_dict sd1 ON sd1.sys_value = cae.cancel_verification_code and sd1.sys_key = 'CANCEL_VERIFICATION_CODE' "+
			"where cae.id =#{entityId}")
    @ResultType(CouponActivityEntity.class)
	CouponActivityEntity couponEntityView(@Param("entityId") int entityId);
	
	/**
	 * 云闪付活动
	 * @param reward
	 */
	@Update("update coupon_activity_info set activity_explain =#{reward.activityExplain},"
			+ " activity_notice=#{reward.activityNotice},update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where id=#{reward.id}")
	void cloudPaySave(@Param("reward")  CouponActivityInfo reward);
	
	/**
	 * 云闪付活动
	 * @param reward
	 */
	@Update("update coupon_activity_entity set trans_rate = #{reward.transRate}, update_time=#{reward.updateTime},operator=#{reward.operator}"
			+ " where id=#{reward.couponId}")
	void cloudPayEntrySave(@Param("reward") CouponActivityInfo reward);

	@Select("SELECT id,coupon_name, coupon_amount,effective_days FROM coupon_activity_entity WHERE activetiy_code = #{activetiyCode} ")
	@ResultType(CouponActivityInfo.class)
	List<CouponActivityInfo> selectCardAndReward(@Param("activetiyCode") String activetiyCode);

	@Select("SELECT team_id FROM merchant_info WHERE merchant_no = #{merchantNo} ")
	String selectTeamIdByMerchantNo(@Param("merchantNo")String merchantNo);

}
