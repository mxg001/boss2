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
import cn.eeepay.framework.model.RiskProblem;
import cn.eeepay.framework.model.RiskProblemAuditRecord;
import cn.eeepay.framework.model.ShiroUser;

public interface RiskProblemDao {

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(RiskProblem.class)
	List<RiskProblem> selectAllInfo(@Param("page")Page<RiskProblem>page,@Param("record")RiskProblem record);
	
	@SelectProvider(type=SqlProvider.class,method="selectAuditAllInfo")
	@ResultType(RiskProblem.class)
	List<RiskProblem> selectAuditAllInfo(@Param("page")Page<RiskProblem>page,@Param("record")RiskProblem record);
	
	@Select("select rpar.*,bsu.real_name from risk_problem_audit_record rpar "
			+ "LEFT JOIN boss_shiro_user bsu on bsu.id=rpar.audit_person "
			+ " where problem_id=#{pid}")
	@ResultType(RiskProblemAuditRecord.class)
	List<RiskProblemAuditRecord> selectRecordAllInfo(@Param("pid")int pid);

	@Select("select rps.*,bsu.real_name from risk_problem rps "
			+ "LEFT JOIN boss_shiro_user bsu on bsu.id=rps.deal_person "
			+ "where problem_id=#{id}")
	@ResultType(RiskProblem.class)
	RiskProblem selectInfo(@Param("id")int id);
	
	//查询BOSS用户
	@Select("select * from boss_shiro_user")
	@ResultType(ShiroUser.class)
	List<ShiroUser> selectBossAllInfo();
	
	@Insert("insert into risk_problem(problem_type,risk_rules_no,problem_title,problem_description,"
			+ "status,deal_person,create_person) "
			+ "values(#{record.problemType},#{record.riskRulesNo},#{record.problemTitle},#{record.problemDescription}"
			+ ",#{record.status},#{record.dealPerson},#{record.createPerson})")
	int insertInfo(@Param("record")RiskProblem record);
	
	@Select("select * from risk_problem where risk_rules_no=#{ruleNo}")
	@ResultType(RiskProblem.class)
	RiskProblem selectInfoByRuleNo(@Param("ruleNo")int ruleNo);
	
	
	@Update("update risk_problem set deal_person=#{record.dealPerson},deal_measures=#{record.dealMeasures},status=2 "
			+ "where problem_id=#{record.problemId}")
	int updateFeedback(@Param("record")RiskProblem record);
	
	@Update("update risk_problem set status=#{record.status} "
			+ "where problem_id=#{record.problemId}")
	int updateStatus(@Param("record")RiskProblem record);
	
	@Update("update risk_problem set problem_type=#{record.problemType},risk_rules_no=#{record.riskRulesNo},problem_title=#{record.problemTitle}"
			+ ",problem_description=#{record.problemDescription},status=#{record.status},deal_person=#{record.dealPerson},deal_measures=#{record.dealMeasures} "
			+ "where problem_id=#{record.problemId}")
	int updateInfo(@Param("record")RiskProblem record);
	
	@Insert("insert into risk_problem_audit_record(problem_id,audit_status,audit_opinion,audit_person) "
			+ "values(#{record.problemId},#{record.auditStatus},#{record.auditOpinion},#{record.auditPerson})")
	int insertAuditRecord(@Param("record")RiskProblemAuditRecord record);
	
	 public class SqlProvider{
	    	
	    	public String selectAllInfo(Map<String,Object> param){
	    		final RiskProblem rp=(RiskProblem)param.get("record");
	    		return new SQL(){{
	    			SELECT("*");
	    			FROM("risk_problem");
	    			if(rp.getProblemId()!=null){
	    				WHERE(" problem_id=#{record.problemId}");
	    			}
	    			if(rp.getProblemType()!=null && rp.getProblemType()!=-1){
	    				WHERE(" problem_type=#{record.problemType}");
	    			}
	    			if(rp.getStatus()!=null && rp.getStatus()!=-1){
	    				WHERE(" status=#{record.status}");
	    			}
	    			if(StringUtils.isNotBlank(rp.getProblemTitle())){
	    				WHERE(" problem_title=#{record.problemTitle}");
	    			}
	    			if(StringUtils.isNotBlank(rp.getDealPerson())){
	    				WHERE(" deal_person=#{record.dealPerson}");
	    			}
	    		}}.toString();
	    	}
	    	
	    	public String selectAuditAllInfo(Map<String,Object> param){
	    		final RiskProblem rp=(RiskProblem)param.get("record");
	    		return new SQL(){{
	    			SELECT("*");
	    			FROM("risk_problem");
	    			WHERE("status=2");
	    			if(rp.getProblemId()!=null){
	    				WHERE("problem_id=#{record.problemId}");
	    			}
	    			if(rp.getProblemType()!=null && rp.getProblemType()!=-1){
	    				WHERE("problem_type=#{record.problemType}");
	    			}
	    			if(StringUtils.isNotBlank(rp.getProblemTitle())){
	    				WHERE("problem_title=#{record.problemTitle}");
	    			}
	    			if(StringUtils.isNotBlank(rp.getDealPerson())){
	    				WHERE(" deal_person=#{record.dealPerson}");
	    			}
	    		}}.toString();
	    	}
	    	
	    }
	
}
