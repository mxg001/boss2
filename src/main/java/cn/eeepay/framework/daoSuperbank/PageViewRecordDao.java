package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.eeepay.framework.model.CreditloanEnterData;




public interface PageViewRecordDao {

	 /**根据1.公众号，2.安卓，3，IOS获取日访问量或者独立访客
	  * @Param 
	  * 1.viewType ：1或空.Page view;2.Unique Visitor
	  * 2.viewNum：页面1.每日,7.周,30.月的访问量
	  * 3.loginType: 1.登录，0.查询页面
	  */
	@SelectProvider(type = SqlProvider.class, method = "selectPageView")
	@ResultType(Long.class)
	Long getPageViewOrUniqueVisitorBySource(@Param("loginSource")String loginSource,@Param("businessSource")String businessSource,
			@Param("openType")String openType,@Param("viewType")String viewType,@Param("viewNum")String viewNum,@Param("loginType")String loginType,
			@Param("orgId")String orgId,@Param("creditBank")String creditBank,@Param("loanSource")String loanSource);
	
	/**统计会员人数
	 * 
	 * */
	@SelectProvider(type = SqlProvider.class, method = "selectUserInfoNumber")
	@ResultType(Long.class)
	Long getUserInfoNumber(@Param("sex")String sex,@Param("orgId")String orgId);
	
	/**删除今天以前的查看记录*/
	@Delete("delete from from page_view_record where date(now()) > date(create_date)")
    int deletePageViewHistoryRecord();
	
	/**获取信用卡或者贷款点击数*/
	@SelectProvider(type = SqlProvider.class, method = "getCreditOrLoanClickNum")
	@ResultType(CreditloanEnterData.class)
	List<CreditloanEnterData> selectCreditOrLoanClickNum(@Param("businessSource")String businessSource,
			@Param("openType")String openType,@Param("orgId")String orgId,@Param("searchType")String searchType);
	
	/**计算操作的数量*/
	@Select("select count(1) from page_view_record where operate=#{operate} and org_id=#{orgId}")
	Long checkOperateCount(@Param("operate")String operate,@Param("orgId")String orgId);
	
	/**获取信用卡或者贷款Pv或者Uv数*/
	@SelectProvider(type = SqlProvider.class, method = "selectCleditOrLoanPvOrUvNumber")
	@ResultType(Long.class)
	Long selectCleditOrLoanPvOrUvNumber(@Param("businessSource")String businessSource,@Param("openType")String openType,
			@Param("type")String type,@Param("timeType")String timeType,@Param("orgId")String orgId,
			@Param("creditBank")String creditBank,@Param("loanSource")String loanSource,@Param("loginSource")String loginSource);

	/**业务访问量汇总Pv或者Uv数*/
	@SelectProvider(type = SqlProvider.class, method = "selectBusinessPageViewData")
	@ResultType(Long.class)
	Long selectBusinessPageViewData(@Param("businessSource")String businessSource,@Param("openType")String openType,
			@Param("type")String type,@Param("timeType")String timeType,@Param("orgId")String orgId,@Param("loginSource")String loginSource);
	

	public class SqlProvider{
	     public String selectPageView(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final String loginSource = (String) param.get("loginSource");
	           final String businessSource = (String) param.get("businessSource");
	           final String openType = (String) param.get("openType");
	           final String viewType = (String) param.get("viewType");
	           final String viewNum = (String) param.get("viewNum");
	           final String loginType = (String) param.get("loginType");
	           final String orgId = (String) param.get("orgId");
	           final String creditBank = (String) param.get("creditBank");
	           final String loanSource = (String) param.get("loanSource");
	           
	           if(StringUtils.isNotBlank(viewType)){
	        	   if(viewType == null || "1".equals(viewType)){
	        		   sql.append("select count(1)");
	        	   }else{
	        		   sql.append("select count(DISTINCT(user_code))");
	        	   }
	           }
	           sql.append((" from page_view_record where 1=1 "));
	           
	           if(StringUtils.isNotBlank(viewNum)){
		             if("1".equals(viewNum)){
		            	 sql.append(" and TO_DAYS(create_date) = TO_DAYS(DATE_SUB(CURDATE(), INTERVAL 1 DAY))");
		             }else if("7".equals(viewNum)){
		            	 sql.append(" and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_date)");
		             }else if("30".equals(viewNum)){
		            	 sql.append(" and DATE_FORMAT(create_date, '%Y%m') = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%Y%m')");
		             }
		       }
	           
	           if(StringUtils.isNotBlank(openType)){
	        	   sql.append(" and open_type=#{openType}");
		       }
	           if(StringUtils.isNotBlank(businessSource)){
		           sql.append(" and business_source=#{businessSource}");
		       }
	          
	           if(StringUtils.isNotBlank(loginSource)){
		           sql.append(" and login_source=#{loginSource}");
		       }

	           if(StringUtils.isNotBlank(loginType)){
	        	   sql.append(" and login_type=#{loginType}");
		       }
	           
	           if(StringUtils.isNotBlank(orgId)){
	        	   sql.append(" and org_id=#{orgId}");
		       }
	           
	           if(StringUtils.isNotBlank(creditBank)){
	        	   sql.append(" and credit_bank=#{creditBank}");
		       }
	           if(StringUtils.isNotBlank(loanSource)){
	        	   sql.append(" and loan_source=#{loanSource}");
		       }
	           
	           return sql.toString();
	     }
	     ////select count(1) enter_num,credit_bank from page_view_record where business_source='1' and login_source='1' and open_type='1' group by credit_bank
	     public String getCreditOrLoanClickNum(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String businessSource = (String) param.get("businessSource");
	         final String openType = (String) param.get("openType");
	         final String orgId = (String) param.get("orgId");
	         final String searchType = (String) param.get("searchType");
	         if("pv".equals(searchType)){
	        	 sql.append("select count(1) day_pv_num ");  
	         }else if("uv".equals(searchType)){
	        	 sql.append("select count(DISTINCT(user_code)) day_uv_num ");  
	         }
	         
	         sql.append(" ,credit_bank,loan_source ");
	         
	         sql.append(" from page_view_record where 1=1 ");
	         if(StringUtils.isNotBlank(businessSource)){
	        	 sql.append(" and business_source=#{businessSource}");
	         }
	         
	         if(StringUtils.isNotBlank(openType)){
	        	 sql.append(" and open_type=#{openType}");
		     }
	         if(StringUtils.isNotBlank(orgId)){
	        	 sql.append(" and org_id=#{orgId} ");
		     }
	         sql.append(" and  TO_DAYS(create_date) = TO_DAYS(DATE_SUB(CURDATE(), INTERVAL 1 DAY)) ");
	         if(StringUtils.isNotBlank(businessSource)){
	        	 if("1".equals(businessSource)){
	        		 sql.append(" group by credit_bank ");
	        	 }else if("2".equals(businessSource)){
	        		 sql.append(" group by loan_source ");
	        	 }
	         }
	         
	         return sql.toString();
	     }
	     
	     public String selectUserInfoNumber(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	         final String sex = (String) param.get("sex");
	         final String orgId = (String) param.get("orgId");
	         sql.append("select count(1) from user_info where 1=1 ");  
	         if(StringUtils.isNotBlank(sex)){
	        	 if("3".equals(sex)){ //所有完善资料的用户
	        		 sql.append(" and sex in ('1','2') "); 
	        	 }else{
	        		 sql.append(" and sex = #{sex} ");  
	        	 }
	        	 
	         }
	         if(StringUtils.isNotBlank(orgId)){
        	   sql.append(" and org_id=#{orgId}");
	         }
	         return sql.toString();
	     }
	     
	     public String selectCleditOrLoanPvOrUvNumber(Map<String, Object> param){
			 StringBuffer sql = new StringBuffer("");
			 final String openType = (String) param.get("openType");
			 final String type = (String) param.get("type");
			 final String timeType = (String) param.get("timeType");
			 final String orgId = (String) param.get("orgId");
			 final String businessSource = (String) param.get("businessSource");
		     final String creditBank = (String) param.get("creditBank");
	         final String loanSource = (String) param.get("loanSource");
	         final String loginSource = (String) param.get("loginSource");
			 sql.append("SELECT sum(");
			 sql.append( "day_" + type + "_num");
			 sql.append(") FROM creditloan_enter_data WHERE 1=1 ");
			 if(StringUtils.isNotBlank(orgId)){
				 sql.append(" and org_id=#{orgId} ");
			 }
			 if(StringUtils.isNotBlank(openType)){
				 sql.append(" and open_type=#{openType} ");
			 }
			 if(StringUtils.isNotBlank(businessSource)){
				 sql.append(" and business_source=#{businessSource} ");
			 }
			 if(StringUtils.isNotBlank(creditBank)){
	        	   sql.append(" and credit_bank=#{creditBank}");
		       }
             if(StringUtils.isNotBlank(loanSource)){
            	   sql.append(" and loan_source=#{loanSource}");
	         }
             if(StringUtils.isNotBlank(loginSource)){
          	   sql.append(" and login_source=#{loginSource} ");
	         }
			 if("week".equals(timeType)){//本周
				 sql.append(" and YEARWEEK(date_format(analysis_date,'%Y-%m-%d'),1) = YEARWEEK(date_format(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%Y-%m-%d'),1)");
			 }else if("month".equals(timeType)){//本月
				 sql.append(" and DATE_FORMAT(analysis_date, '%Y%m') = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%Y%m')");
			 }
	         return sql.toString();
		 }
	     
	     
	     public String selectBusinessPageViewData(Map<String, Object> param){
			 StringBuffer sql = new StringBuffer("");
			 final String openType = (String) param.get("openType");
			 final String type = (String) param.get("type");
			 final String timeType = (String) param.get("timeType");
			 final String orgId = (String) param.get("orgId");
			 final String businessSource = (String) param.get("businessSource");
			 final String loginSource = (String) param.get("loginSource");
			 sql.append("SELECT sum(");
			 sql.append( "day_" + type + "_num");
			 sql.append(") FROM business_page_view_data WHERE 1=1 ");
			 if(StringUtils.isNotBlank(orgId)){
				 sql.append(" and org_id=#{orgId} ");
			 }
			 if(StringUtils.isNotBlank(openType)){
				 sql.append(" and open_type=#{openType} ");
			 }
			 if(StringUtils.isNotBlank(loginSource)){
				 sql.append(" and login_source=#{loginSource} ");
			 }
			 if(StringUtils.isNotBlank(businessSource)){
				 sql.append(" and business_source=#{businessSource} ");
			 }
			 if("week".equals(timeType)){//本周
				 sql.append(" and YEARWEEK(date_format(analysis_date,'%Y-%m-%d'),1) = YEARWEEK(date_format(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%Y-%m-%d'),1) ");
			 }else if("month".equals(timeType)){//本月
				 sql.append(" and DATE_FORMAT(analysis_date, '%Y%m') = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'%Y%m')");
			 }
	         return sql.toString();
		 }
    }
	
	
	
	
	
}
