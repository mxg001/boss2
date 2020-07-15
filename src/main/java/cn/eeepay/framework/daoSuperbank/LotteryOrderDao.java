package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface LotteryOrderDao {

	
	 @SelectProvider(type=SqlProvider.class,method="selectLotteryOrder")
	 @ResultType(LotteryOrder.class)
	 List<LotteryOrder> selectLotteryOrder(@Param("baseInfo")LotteryOrder baseInfo,@Param("page")Page<LotteryOrder> page);
	 
	 @SelectProvider(type=SqlProvider.class,method="selectOrderSum")
	 @ResultType(LotteryOrder.class)
	 LotteryOrder selectOrderSum(@Param("baseInfo")LotteryOrder baseInfo);
	 
	 class SqlProvider{
		 public String selectLotteryOrder(Map<String,Object> param){
			 final LotteryOrder info = (LotteryOrder)param.get("baseInfo");
			 SQL sql = new SQL();
			 sql.SELECT("ly.order_no,ly.device_jno,oi.org_id,oi.org_name,om.status order_status,ui.user_code,ui.user_name,ui.phone,om.price,om.loan_amount,om.profit_type,ly.award_require,om.loan_bank_rate,om.total_bonus,om.loan_org_rate,"+
						"ly.bet_time,ly.lottery_type,ly.issue,ly.redeem_time,ly.award_amount,ly.is_big_prize,ly.redeem_flag,om.one_user_code one_code,ui1.user_name one_name,om.one_user_type one_role,om.one_user_profit one_profit,"+
						"om.two_user_code two_code,ui2.user_name two_name,om.two_user_type two_role,om.two_user_profit two_profit,om.thr_user_code threeCode,ui3.user_name three_name,om.thr_user_type three_role,om.thr_user_profit three_profit,"+
						"om.fou_user_code four_code,ui4.user_name four_name,om.fou_user_type four_role,om.fou_user_profit four_profit,om.plate_profit,om.org_profit,om.account_status,"+
						"ui.open_province,ui.open_city,ui.open_region,om.remark,om.company_bonus_conf,om.org_bonus_conf,ly.buy_status,ly.menchant_no,ly.batch_no,om.pay_order_no,om.create_date");
			 whereSql(sql,info);
			 sql.ORDER_BY(" om.create_date desc ");
			 return sql.toString();
		 }
	
		 public String selectOrderSum(Map<String,Object> param){
			 final LotteryOrder info = (LotteryOrder)param.get("baseInfo");
			 SQL sql = new SQL();
			 sql.SELECT("sum(om.price) as sumAmount");
			 sql.SELECT("sum(om.plate_profit) as sumPlatProfit");
			 sql.SELECT("sum(om.org_profit) as sumOrgProfit");
			 whereSql(sql,info);
			 return sql.toString();
		 }
		 
		 public void whereSql(SQL sql, LotteryOrder baseInfo){
			 sql.FROM("lottery_order ly");
			 sql.   INNER_JOIN  ("   order_main om on ly.order_no=om.order_no");
			 sql.LEFT_OUTER_JOIN("   org_info oi on om.org_id=oi.org_id");
			 sql.LEFT_OUTER_JOIN("   user_info ui on om.user_code=ui.user_code");
			 sql.LEFT_OUTER_JOIN("   user_info ui1 on om.one_user_code=ui1.user_code");
			 sql.LEFT_OUTER_JOIN("   user_info ui2 on om.two_user_code=ui2.user_code");
			 sql.LEFT_OUTER_JOIN("   user_info ui3 on om.thr_user_code=ui3.user_code");
			 sql.LEFT_OUTER_JOIN("   user_info ui4 on om.fou_user_code=ui4.user_code");
			 
			 if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
				 sql.WHERE(" ly.order_no = #{baseInfo.orderNo}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getStartTime())){
				 sql.WHERE("  ly.bet_time >= #{baseInfo.startTime}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getEndTime())){
				 sql.WHERE("  ly.bet_time <= #{baseInfo.endTime}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
				 sql.WHERE("  om.account_status = #{baseInfo.accountStatus}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOrgId()) && !"-1".equals(baseInfo.getOrgId())){
				 sql.WHERE("  oi.org_id = #{baseInfo.orgId}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getUserCode())){
				 sql.WHERE("  ui.user_code = #{baseInfo.userCode}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getUserName())){
				 sql.WHERE("  ui.user_name = #{baseInfo.userName}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getPhone())){
				 sql.WHERE("  ui.phone = #{baseInfo.phone}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOneCode())){
				 sql.WHERE("  ui1.user_code = #{baseInfo.oneCode}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOneName())){
				 sql.WHERE("  ui1.user_name = #{baseInfo.oneName}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOneRole())){
				 sql.WHERE("  ui1.user_type = #{baseInfo.oneRole}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getTwoCode())){
				 sql.WHERE("  ui2.user_code = #{baseInfo.twoCode}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getTwoName())){
				 sql.WHERE("  ui2.user_name = #{baseInfo.twoName}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getTwoRole())){
				 sql.WHERE("  ui2.user_type = #{baseInfo.twoRole}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getThreeCode())){
				 sql.WHERE("  ui3.user_code = #{baseInfo.threeCode}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getThreeName())){
				 sql.WHERE("  ui3.user_name = #{baseInfo.threeName}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getThreeRole())){
				 sql.WHERE("  ui3.user_type = #{baseInfo.threeRole}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getFourCode())){
				 sql.WHERE("  ui4.user_code = #{baseInfo.fourCode}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getFourName())){
				 sql.WHERE("  ui4.user_name = #{baseInfo.fourName}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getFourRole())){
				 sql.WHERE("  ui4.user_type = #{baseInfo.fourRole}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOpenProvince()) && !"全部".equals(baseInfo.getOpenProvince())){
				 sql.WHERE("  ui.open_province = #{baseInfo.openProvince}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOpenCity()) && !"全部".equals(baseInfo.getOpenCity())){
				 sql.WHERE("  ui.open_city = #{baseInfo.openCity}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getOpenRegion()) && !"全部".equals(baseInfo.getOpenRegion())){
				 sql.WHERE("  ui.open_region = #{baseInfo.openRegion}");
			 }
			 
			 if(StringUtils.isNotBlank(baseInfo.getRemark())){
				 sql.WHERE("  om.remark = #{baseInfo.remark}");
			 }
			 if(StringUtils.isNotBlank(baseInfo.getBatchNo())){
				 sql.WHERE("  ly.batch_no = #{baseInfo.batchNo}");
			 }
		 }
	 }
	 
	 @Select("select count(1) from lottery_order where device_no = #{deviceNo} and device_jno = #{deviceJno}")
     @ResultType(Integer.class)
     int checkExists(LotteryOrder lotteryOrder);
	 
	 @Select("select count(1) from lottery_order where out_order_no = #{outOrderNo} ")
     @ResultType(Integer.class)
     int checkSportLotteryExists(LotteryOrder lotteryOrder);
	 
	 /**新增彩票记录*/
	@Insert("insert into lottery_order " +
			"(out_id,order_no,out_order_no,out_user_id,device_no,device_jno,menchant_no,menchant_name,lottery_type,issue,bet_time,redeem_flag,redeem_time,buy_status,is_big_prize,consume_e,award_amount,create_date,remark,user_code,award_require,batch_no,sport_lottery)" +
			" values " +
			"(#{outId},#{orderNo},#{outOrderNo},#{outUserId},#{deviceNo},#{deviceJno},#{menchantNo},#{menchantName},#{lotteryType},#{issue},#{betTime},#{redeemFlag},#{redeemTime},#{buyStatus},#{isBigPrize},#{consumeE},#{awardAmount},sysdate(),'100e豆=1元',#{userCode},#{awardRequire},#{batchNo},#{sportLottery})")
	int saveLotteryOrder(LotteryOrder lotteryOrder);
	
	/**修改彩票订单，更新批次号*/
	@Update("update lottery_order set batch_no = #{batchNo} where device_no = #{deviceNo} and device_jno = #{deviceJno} ")
	int updateLotteryOrder(LotteryOrder lotteryOrder);
	
	/**批次修改状态*/
	@Update("<script>"
            + " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
            + "         close=\"\" separator=\";\"> "
            + " update lottery_order "
            + " set batch_no=#{item.batchNo}"
            + "   where device_no = #{item.deviceNo} and device_jno = #{item.deviceJno}"
            + "     </foreach> "
            + " </script>")
    int updateLotteryBatchNoBatch(@Param("list")List<LotteryOrder> list);

	/**批次修改状态*/
	@Update("<script>"
            + " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
            + "         close=\"\" separator=\";\"> "
            + " update lottery_order "
            + " set batch_no=#{item.batchNo}"
            + "   where out_order_no = #{item.outOrderNo}"
            + "     </foreach> "
            + " </script>")
    int batchUpdbatchNo(@Param("list")List<LotteryOrder> list);
}