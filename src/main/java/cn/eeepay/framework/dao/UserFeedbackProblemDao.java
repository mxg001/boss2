package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;

public interface UserFeedbackProblemDao {

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(UserFeedbackProblem.class)
	List<UserFeedbackProblem> selectAllInfo(Page<UserFeedbackProblem> page,@Param("ufp")UserFeedbackProblem ufp);
	
	@Select("SELECT ufp.*,ui.user_name,ui.mobilephone,pt.type_name from user_feedback_problem ufp "
			+ "LEFT JOIN user_info ui on ui.user_id=ufp.user_id "
			+ "LEFT JOIN problem_type pt on pt.problem_type=ufp.problem_type where ufp.id=#{id}")
	@ResultType(UserFeedbackProblem.class)
	UserFeedbackProblem selectDetailById(@Param("id")int id);
	
	@Select("SELECT * from problem_type")
	@ResultType(ProblemType.class)
	List<ProblemType> selectAllProblemInfo();

	@Update("update user_feedback_problem set deal_time=now(), deal_result=#{info.dealResult} , status = #{info.status},deal_user_id=#{info.dealUserId} where id = #{info.id}")
	void saveDealResult(@Param("info") UserFeedbackProblem info);

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(UserFeedbackProblem.class)
	List<UserFeedbackProblem> export(@Param("ufp")UserFeedbackProblem ufp);

	public class SqlProvider{
			public String selectAllInfo(Map<String,Object> param){
				final UserFeedbackProblem ufp=(UserFeedbackProblem)param.get("ufp");
				return new SQL(){{
					SELECT("ufp.*,ui.user_name,ui.mobilephone,pt.type_name,ai.app_name,ui2.real_name deal_user_name");
					FROM("user_feedback_problem ufp "
							+ "LEFT JOIN user_info ui on ui.user_id=ufp.user_id "
							+ "LEFT JOIN problem_type pt on pt.problem_type=ufp.problem_type");
					LEFT_OUTER_JOIN("app_info ai on ai.app_no = ufp.app_no");
					LEFT_OUTER_JOIN("boss_shiro_user ui2 on ui2.id = ufp.deal_user_id");
					if(StringUtils.isNotBlank(ufp.getTitle())){
						WHERE("ufp.title= #{ufp.title}");
					}
					if(StringUtils.isNotBlank(ufp.getUserName())){
						WHERE("ui.user_name= #{ufp.userName}");
					}
					if(StringUtils.isNotBlank(ufp.getProblemType())){
						WHERE("ufp.problem_type = #{ufp.problemType}");
					}
					if(StringUtils.isNotBlank(ufp.getUserType())){
						WHERE("ufp.user_type =#{ufp.userType}");
					}
					if(StringUtils.isNotBlank(ufp.getMobilephone())){
						WHERE("ui.mobilephone = #{ufp.mobilephone}");
					}
					if(ufp.getStatus()!=null){
						WHERE("ufp.status = #{ufp.status}");
					}
					if(StringUtils.isNotBlank(ufp.getAppNo())){
						WHERE("ufp.app_no = #{ufp.appNo}");
					}
					if(ufp.getSubmitTimeBegin()!=null){
						WHERE("ufp.submit_time > #{ufp.submitTimeBegin}");
					}
					if(ufp.getSubmitTimeEnd()!=null){
						WHERE("ufp.submit_time < #{ufp.submitTimeEnd}");
					}

					if(ufp.getDealTimeBegin()!=null){
						WHERE("ufp.deal_time > #{ufp.dealTimeBegin}");
					}
					if(ufp.getDealTimeEnd()!=null){
						WHERE("ufp.deal_time < #{ufp.dealTimeEnd}");
					}
					ORDER_BY("submit_time desc");
				}}.toString();
			}
	  }
	
}
