package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.UserDistributionRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


public interface UserDistributionRecordDao {

	
	
	/**新增用户分布记录*/
	@Insert("insert into user_distribution_record  " +
			"(is_province,user_count,open_province,open_city,create_date,analysis_code,org_id)" +
			" values" +
			" (#{isProvince},#{userCount},#{openProvince},#{openCity},sysdate(),#{analysisCode},#{orgId})")
	int saveUserDistributionRecord(UserDistributionRecord record);

	/**获取用户省份分布数量*/
	@Select("select count(1) user_count,open_province from user_info where open_province is not null and org_id=#{orgId} GROUP BY open_province")
	@ResultType(UserDistributionRecord.class)
	List<UserDistributionRecord> getProvinceDistributionCount(@Param("orgId")String orgId);
	
	/**获取用户城市分布数量*/
	@Select("select count(1) user_count,open_province,open_city from user_info where open_city is not null and org_id=#{orgId} GROUP BY open_province,open_city")
	@ResultType(UserDistributionRecord.class)
	List<UserDistributionRecord> getCityDistributionCount(@Param("orgId")String orgId);
	
	/**获取已完善资料的用户数*/
	@Select("select count(1) from user_info where org_id=#{orgId} and user_name is not null and phone is not null and id_card_no is not null ")
	Long getConsummateDataUserCount(@Param("orgId")String orgId);
	
	/**公众号1.安卓，2.ios用户*/
	@Select("select count(1) from user_info where org_id=#{orgId} and user_name is not null and phone is not null and id_card_no is not null ")
	Long getPublicAndroidAndIosUserCount(@Param("orgId")String orgId);
	
	/**获取没有创建过订单或者创建过订单的用户数,sign:null，没创建过订单，1.创建过订单*/
	@SelectProvider(type = SqlProvider.class, method = "getOrderUserCount")
	@ResultType(Long.class)
	Long getOrderUserCount(@Param("sign")String sign,@Param("orgId")String orgId);
	
	/**获取订单,status:null，所有订单，5.成功的订单*/
	@SelectProvider(type = SqlProvider.class, method = "getOrderByStatusAndOrderTypeCount")
	@ResultType(Long.class)
	Long getOrderByStatusAndOrderTypeCount(@Param("status")String status,@Param("orderType")String orderType,@Param("orgId")String orgId);
	
	/**已付费交钱的用户 paid:1.未付费用户，2.已付费用户*/
	@SelectProvider(type = SqlProvider.class, method = "getPaidUserCount")
	@ResultType(Long.class)
	Long getPaidUserCount(@Param("paid")String paid,@Param("orgId")String orgId);
	
	/**新增用户数 day:1.次日新增，3.三日新增，7.七日新增，15.十五天新增，30.三十日新增会员*/
	@Select("select count(1) from user_info a where org_id=#{orgId} and TO_DAYS(create_date) = TO_DAYS(DATE_SUB(CURDATE(), INTERVAL #{day} DAY)) ")
	Long getNewIncreaseUserByDay(@Param("day")Integer day,@Param("orgId")String orgId);
	
	/**登录用户数 day:1.次日新增，3.三日新增，7.七日新增，15.十五天新增，30.三十日新增会员*/
	@Select("select count(distinct(user_code)) from page_view_record p where login_type='1' and org_id=#{orgId} " +
			"and  TO_DAYS(create_date) = TO_DAYS(DATE_SUB(CURDATE(), INTERVAL 1 DAY)) " +
			"and EXISTS (select user_code from user_info a where  TO_DAYS(create_date) = TO_DAYS(DATE_SUB(CURDATE(), INTERVAL #{day} DAY)) and p.user_code=a.user_code )")
	Long getloginUserByDay(@Param("day")Integer day,@Param("orgId")String orgId);
	
	/**获取分享成功过的用户数*/
	@Select("select count(1) from user_info where org_id=#{orgId} and shared_num > 0")
	@ResultType(Long.class)
	Long getSharedSuccessUserCount(@Param("orgId")String orgId);
	
	
	/**获取分享成功过的用户数*/
	@SelectProvider(type = SqlProvider.class, method = "getUserCountBySource")
	@ResultType(Long.class)
	Long getUserCountBySource(@Param("orgId")String orgId,@Param("openType")String openType,@Param("loginSource")String loginSource);
	
	public class SqlProvider{
	    
	     public String getOrderUserCount(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String sign = (String) param.get("sign");
	         final String orgId = (String) param.get("orgId");
	         sql.append("select count(1) from user_info a where 1=1 ");  
	         
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and a.org_id = #{orgId} ");
	         }
	         sql.append(" and ");
	         
	         if(StringUtils.isNotBlank(sign)){
	        	 if("1".equals(sign)){
	        		 sql.append(" not ");
	        	 }
	         }
	         sql.append(" EXISTS(select user_code from order_main b where a.user_code=b.user_code" );
	         
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and org_id = #{orgId} ");
	         }
	         sql.append(")");
	         return sql.toString();
	     }
	     
	     public String getUserCountBySource(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String openType = (String) param.get("openType");
	         final String orgId = (String) param.get("orgId");
	         final String loginSource = (String) param.get("loginSource");
	         sql.append("select count(1) from user_info where 1=1 ");  
	         
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and org_id = #{orgId} ");
	         }
	         if(StringUtils.isNotBlank(openType)){
	        	 sql.append(" and open_type = #{openType} ");
	         }
	         if(StringUtils.isNotBlank(loginSource)){
	        	 sql.append(" and login_source = #{loginSource} ");
	         }
	         return sql.toString();
	     }
	     
	     public String getOrderByStatusAndOrderTypeCount(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String status = (String) param.get("status");
	         final String orderType = (String)param.get("orderType");
	         final String orgId = (String)param.get("orgId");
	         sql.append("select count(1) from order_main where 1=1 ");  
	         if(StringUtils.isNotBlank(status)){
	        	 if("99".equals(status)){
	        		 sql.append(" and status in (4,5) ");
	        	 }else{
	        		 sql.append(" and status = #{status} ");
	        	 }
	         }
	         if(StringUtils.isNotBlank(orderType)){
	        	 sql.append(" and order_type = #{orderType} ");
	         }
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and org_id = #{orgId} ");
	         }
	         
	         return sql.toString();
	     }
	     
	     public String getPaidUserCount(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String paid = (String) param.get("paid");
	         final String orgId = (String) param.get("orgId");
	         sql.append("select count(1) from user_info where 1=1 ");  
	         if(StringUtils.isNotBlank(paid)){
	        	 if("1".equals(paid)){
	        		 sql.append(" and user_type = '10' ");
	        	 }else{
	        		 sql.append(" and user_type > '10' ");
	        	 }
	         }
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and org_id = #{orgId} ");
	         }
	         return sql.toString();
	     }
    }
	
	
	
	
}
