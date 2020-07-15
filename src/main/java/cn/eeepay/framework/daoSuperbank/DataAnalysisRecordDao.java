package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.DataAnalysisRecord;
import cn.eeepay.framework.model.OrgInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


public interface DataAnalysisRecordDao {

	
	
	/**新增数据分析记录*/
	@Insert("insert into data_analysis_record  " +
			"(analysis_code,day_login_num,male_num,female_num,all_num,h5_user_num,android_user_num,ios_user_num,male_rate,female_rate,white_user,register_user," +
			  "register_rate,activate_user,product_success_rate,shared_user,shared_rate,paid_user,paid_rate,morrow_retention_rate,three_retention_rate,seven_retention_rate," + 
			  "fifteen_retention_rate,thity_retention_rate,create_date,finish_date,org_id,poster_apply_count,confirm_apply_count,goto_apply_loan_count,loan_poster_count," +
			  "day_h5_login_num,day_android_login_num,day_ios_login_num,finish_order_num,order_num,credit_order_num,credit_finish_order,day_h5_android_login_num,day_h5_ios_login_num,h5_android_user_num,h5_ios_user_num) " +
			" values" +
			" (#{analysisCode},#{dayLoginNum},#{maleNum},#{femaleNum},#{allNum},"+
			"#{h5UserNum},#{androidUserNum},#{iosUserNum},#{maleRate},#{femaleRate},#{whiteUser},#{registerUser},#{registerRate},#{activateUser},#{productSuccessRate},#{sharedUser},#{sharedRate},#{paidUser},"+
			"#{paidRate},#{morrowRetentionRate},#{threeRetentionRate},#{sevenRetentionRate},#{fifteenRetentionRate},#{thityRetentionRate},#{createDate},#{finishDate},#{orgId},#{posterApplyCount},#{confirmApplyCount}," +
			"#{gotoApplyLoanCount},#{loanPosterCount},#{dayH5LoginNum},#{dayAndroidLoginNum},#{dayIosLoginNum},#{finishOrderNum},#{orderNum},#{creditOrderNum},#{creditFinishOrder},#{dayH5AndroidLoginNum},#{dayH5IosLoginNum},#{h5AndroidUserNum},#{h5IosUserNum})")
	int saveDataAnalysisRecord(DataAnalysisRecord record);

	/**获取周,月访问量，独立访客：本周的记录日访问量相加:
	 * source:h5,android,ios;
	 * type:pv,uv
	 * timeType:day-获取同一个星期的数据,week：获取同一个月的数据
	 * */
	@SelectProvider(type = SqlProvider.class, method = "selectPvNumber")
	@ResultType(Long.class)
	Long getPvNumber(@Param("source")String source,@Param("type")String type,@Param("timeType")String timeType,@Param("orgId")String orgId);
	

	/**获取需要进行数据分析的组织*/
	@Select("select org_id, org_name from org_info where start_analysis_data='1' order by org_id")
	@ResultType(OrgInfo.class)
	List<OrgInfo> getAnalysisOrgInfoList();
	
	
	public class SqlProvider{
		 public String selectPvNumber(Map<String, Object> param){
			 StringBuffer sql = new StringBuffer("");
			 final String source = (String) param.get("source");
			 final String type = (String) param.get("type");
			 final String timeType = (String) param.get("timeType");
			 final String orgId = (String) param.get("orgId");
			 sql.append("SELECT sum(");
			 sql.append(source + "_day_" + type + "_num");
			 sql.append(") FROM data_analysis_record WHERE 1=1 ");
			 if(StringUtils.isNotBlank(orgId)){
				 sql.append(" and org_id=#{orgId} ");
			 }
			 if("week".equals(timeType)){//本周
				 sql.append(" and YEARWEEK(date_format(create_date,'%Y-%m-%d')) = YEARWEEK(DATE_SUB(CURDATE(), INTERVAL 1 DAY))");
			 }else if("month".equals(timeType)){//本月
				 sql.append(" and DATE_FORMAT(create_date, '%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m')");
			 }
	         return sql.toString();
		 }
	}
	
}
