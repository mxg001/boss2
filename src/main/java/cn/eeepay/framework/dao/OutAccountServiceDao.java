package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 出款服务dao
 * 
 * @author junhu
 *
 */
public interface OutAccountServiceDao {

	@Insert("insert into out_account_service_function (close_advance_mobile, out_account_mobile, skip_channel_mobile, second_out, self_audit, out_account)"
			+ " values (#{closeAdvanceMobile}, #{outAccountMobile},#{skipChannelMobile}, #{secondOut}, #{selfAudit}, #{outAccount})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertOutAccountServiceFunction(OutAccountServiceFunction function);

	@Update("update out_account_service_function set close_advance_mobile = #{closeAdvanceMobile}, out_account_mobile = #{outAccountMobile},"
			+ " skip_channel_mobile = #{skipChannelMobile}, second_out = #{secondOut}, self_audit = #{selfAudit}, out_account = #{outAccount},"
			+ " out_account_failure=#{outAccountFailure}"
			+ " where id = #{id}")
	int updateOutAccountServiceFunction(OutAccountServiceFunction function);

	@Select("select * from out_account_service_function")
	@ResultType(OutAccountServiceFunction.class)
	OutAccountServiceFunction queryOutAccountServiceFunction();

	@Insert("insert into out_account_service (acq_org_id, service_type, service_name, out_account_min_amount, out_account_max_amount, "
			+ "day_out_account_amount, out_amount_warning, transformation_amount, level, anto_close_msg, out_account_status,acq_enname,day_reset_limit,remark,ic_month_max_amount)"
			+ " values (#{acqOrgId}, #{serviceType}, #{serviceName}, #{outAccountMinAmount}, #{outAccountMaxAmount}, #{dayOutAccountAmount}, "
			+ "#{outAmountWarning}, #{transformationAmount}, #{level}, #{antoCloseMsg}, #{outAccountStatus},#{acqEnname},#{dayResetLimit},#{remark},#{icMonthMaxAmount})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertOutAccountService(OutAccountService service);

	@Insert("insert into out_account_service_rate (out_account_service_id, agent_rate_type, cost_rate_type, single_amount, rate, "
			+ "capping, safe_line, ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, "
			+ "ladder4_max, effective_status, effective_date, create_time, create_person,ladder1_safe_line,ladder2_safe_line,"
			+ "ladder3_safe_line,ladder4_safe_line)"
			+ " values (#{outAccountServiceId}, #{agentRateType}, #{costRateType}, #{singleAmount}, #{rate}, #{capping}, #{safeLine}, "
			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max}, "
			+ "#{effectiveStatus}, #{effectiveDate}, #{createTime}, #{createPerson},#{ladder1SafeLine},#{ladder2SafeLine}"
			+ ",#{ladder3SafeLine},#{ladder4SafeLine})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertOutAccountServiceRate(OutAccountServiceRate serviceRate);

	@SelectProvider(type = SqlProvider.class, method = "queryOutAccountService")
	@ResultType(OutAccountService.class)
	List<OutAccountService> queryOutAccountService(Map<String, Object> param, Page<OutAccountService> page);

	@SelectProvider(type = SqlProvider.class, method = "queryOutAccountService")
	@ResultType(OutAccountService.class)
	OutAccountService queryOutAccountServiceById(Map<String, Object> param);

	@Update("update out_account_service set out_account_status = #{outAccountStatus} where id = #{id}")
	int updateOutAccountServiceStatus(@Param("id") Integer id, @Param("outAccountStatus") Integer outAccountStatus);

	@Select("select * from out_account_service_rate where out_account_service_id = #{serviceId}")
	@ResultType(OutAccountServiceRate.class)
	List<OutAccountServiceRate> queryOutAccountServiceRate(Integer serviceId);

	@Select("select * from out_account_service_rate_task where out_account_service_rate_id = #{serviceRateId}")
	@ResultType(OutAccountServiceRateTask.class)
	List<OutAccountServiceRateTask> queryOutAccountServiceRateLog(Integer serviceRateId);

	@Update("update out_account_service set acq_org_id = #{acqOrgId}, service_type = #{serviceType},"
			+ " service_name = #{serviceName}, out_account_min_amount = #{outAccountMinAmount},"
			+ " out_account_max_amount = #{outAccountMaxAmount}, day_out_account_amount = #{dayOutAccountAmount},"
			+ " out_amount_warning = #{outAmountWarning}, transformation_amount = #{transformationAmount},"
			+ " level = #{level}, anto_close_msg = #{antoCloseMsg}, out_account_status = #{outAccountStatus},remark=#{remark},ic_month_max_amount=#{icMonthMaxAmount}"
			+ " where id = #{id}")
	int updateOutAccountService(OutAccountService service);

	@Insert("insert into out_account_service_rate_task (out_account_service_rate_id, agent_rate_type, cost_rate_type, single_amount, rate, "
			+ "capping, safe_line, ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, "
			+ "ladder4_max, effective_status, effective_date, create_time, create_person,ladder1_safe_line,ladder2_safe_line,"
			+ "ladder3_safe_line,ladder4_safe_line)"
			+ " values (#{outAccountServiceRateId}, #{agentRateType}, #{costRateType}, #{singleAmount}, #{rate}, #{capping}, #{safeLine}, "
			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max}, "
			+ "#{effectiveStatus}, #{effectiveDate}, #{createTime}, #{createPerson},#{ladder1SafeLine},#{ladder2SafeLine}"
			+ ",#{ladder3SafeLine},#{ladder4SafeLine})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertOutAccountServiceRateTask(OutAccountServiceRateTask serviceRateTask);

	@Delete("delete from out_account_service_rate_task where id = #{id}")
	int deleteOutAccountServiceRateTask(Integer id);
	
	//查询分润task：根据生效日期和生效状态
    @Select("SELECT c.* FROM out_account_service_rate b,out_account_service_rate_task c,"
    		+" (SELECT t.out_account_service_rate_id, MAX(t.effective_date) AS effective_date_max"
    		+" FROM out_account_service_rate_task t WHERE (NOW() >= t.effective_date) GROUP BY"
    		+" (t.out_account_service_rate_id)) a WHERE b.id = a.out_account_service_rate_id"
    		+" AND b.effective_date < a.effective_date_max"
    		+" AND c.effective_date = a.effective_date_max"
    		+" AND c.out_account_service_rate_id = a.out_account_service_rate_id")
    @ResultType(OutAccountServiceRateTask.class)
    List<OutAccountServiceRateTask> findByEffective();
	
	@Select("select * from out_account_service_rate where id=#{id}")
	@ResultType(OutAccountServiceRate.class)
	OutAccountServiceRate getRateById(@Param("id")Integer id);
	
	@UpdateProvider(type = SqlProvider.class, method = "updateRateByTaskBatch")
    int updateRateByTaskBatch(@Param("list")List<OutAccountServiceRateTask> taskList);
    
    @UpdateProvider(type = SqlProvider.class, method = "updateTaskByRateBatch")
    int updateTaskByRateBatch(@Param("list")List<OutAccountServiceRate> ruleList, @Param("taskIdList")List<Integer> taskIdList);
    
    @Update("update out_account_service set day_total_amount=0 where acq_org_id=#{acqOrgId} and day_reset_limit=1 ")
	int updateResetDayTotalAmount(@Param("acqOrgId")Integer acq_org_id);

    @Select("select * from out_account_service")
    @ResultType(OutAccountService.class)
    List<OutAccountService> queryBoxAllInfo();

	@Select("select id,user_code,acq_enname from out_account_service where user_code is not null")
	@ResultType(OutAccountService.class)
	List<OutAccountService> selectNeedUpdateBalance();

	@Update("update out_account_service set user_balance = #{userBalance} where id = #{id}")
	int updateBalance(OutAccountService service);

	@Select("select * from out_account_service where id = #{serviceId}")
	@ResultType(OutAccountService.class)
    OutAccountService findServiceId(@Param("serviceId") Integer serviceId);

    public class SqlProvider {
		public String queryOutAccountService(final Map<String, Object> param) {
			return new SQL() {
				{
					SELECT("s.*,(s.day_out_account_amount-s.day_total_amount) last_amount, o.acq_name as acq_org_name");
					FROM("out_account_service s LEFT JOIN acq_org o on s.acq_org_id = o.id");
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqOrgId") == null ? "" : param.get("acqOrgId")))) {
						WHERE("s.acq_org_id = #{acqOrgId}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("serviceType") == null ? "" : param.get("serviceType")))) {
						WHERE("s.service_type = #{serviceType}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("serviceName") == null ? "" : param.get("serviceName")))) {
						WHERE("s.service_name like CONCAT('%',#{serviceName},'%')");
					}

					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("serviceId") == null ? "" : param.get("serviceId")))) {
						WHERE("s.id = #{serviceId}");
					}
				}
			}.toString();
		}
		
		public String updateRateByTaskBatch(Map<String, Object> map) {
    		List<OutAccountServiceRateTask> taskList = (List<OutAccountServiceRateTask>) map.get("list");
    		StringBuilder sb = new StringBuilder();  
            sb.append("INSERT INTO out_account_service_rate ");  
            sb.append("(id,effective_date,create_time,create_person,agent_rate_type,cost_rate_type,single_amount,rate,capping,safe_line,");
            sb.append("ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max, ");
            sb.append("ladder1_safe_line,ladder2_safe_line,ladder3_safe_line,ladder4_safe_line) ");
            sb.append("VALUES ");  
            MessageFormat mf = new MessageFormat("(#'{'list[{0}].outAccountServiceRateId},#'{'list[{0}].effectiveDate},#'{'list[{0}].createTime},"
            		+ "#'{'list[{0}].createPerson},#'{'list[{0}].agentRateType},#'{'list[{0}].costRateType},"
            		+ "#'{'list[{0}].singleAmount},#'{'list[{0}].rate}, #'{'list[{0}].capping}, #'{'list[{0}].safeLine},"
            		+ "#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},"
            		+ "#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max},"
            		+ "#'{'list[{0}].ladder1SafeLine},#'{'list[{0}].ladder2SafeLine},#'{'list[{0}].ladder3SafeLine},#'{'list[{0}].ladder4SafeLine})");  
            for (int i = 0; i < taskList.size(); i++) {  
                sb.append(mf.format(new Object[]{i}));  
                if (i < taskList.size() - 1) {  
                    sb.append(",");  
                }  
            }  
            sb.append("on duplicate key update effective_date=values(effective_date),create_time=values(create_time),create_person=values(create_person), agent_rate_type=values(agent_rate_type),cost_rate_type=values(cost_rate_type),single_amount=values(single_amount),rate=values(rate),capping=values(capping),safe_line=values(safe_line),");
            sb.append("ladder1_rate=values(ladder1_rate),");
            sb.append("ladder1_max=values(ladder1_max),ladder2_rate=values(ladder2_rate),ladder2_max=values(ladder2_max),ladder3_rate=values(ladder3_rate),ladder3_max=values(ladder3_max),ladder4_rate=values(ladder4_rate),ladder4_max=values(ladder4_max),");
            sb.append("ladder1_safe_line=values(ladder1_safe_line),ladder2_safe_line=values(ladder2_safe_line),ladder2_max=values(ladder2_max),ladder3_safe_line=values(ladder3_safe_line),ladder4_safe_line=values(ladder4_safe_line)");
            System.out.println(sb.toString());
            return sb.toString(); 
    	}
    	
    	public String updateTaskByRateBatch(Map<String, Object> map) {
    		List<AgentShareRule> taskList = (List<AgentShareRule>) map.get("list");
    		List<Integer> taskIdList = (List<Integer>) map.get("taskIdList");
    		StringBuilder sb = new StringBuilder();  
            sb.append("INSERT INTO out_account_service_rate_task ");  
            sb.append("(id, effective_date, create_time,create_person,agent_rate_type,cost_rate_type,single_amount,rate,capping,safe_line,");
            sb.append("ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,ladder3_max,ladder4_rate,ladder4_max,ladder1_safe_line,ladder2_safe_line,ladder3_safe_line,ladder4_safe_line) ");
            sb.append("VALUES ");  
            MessageFormat mf = new MessageFormat("(#'{'taskIdList[{0}]},#'{'list[{0}].effectiveDate},#'{'list[{0}].createTime},#'{'list[{0}].createPerson}, #'{'list[{0}].agentRateType},#'{'list[{0}].costRateType},#'{'list[{0}].singleAmount},#'{'list[{0}].rate}, #'{'list[{0}].capping}, #'{'list[{0}].safeLine},#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max},"
            		+ "#'{'list[{0}].ladder1SafeLine},#'{'list[{0}].ladder2SafeLine},#'{'list[{0}].ladder3SafeLine},#'{'list[{0}].ladder4SafeLine})");  
            
            for (int i = 0; i < taskList.size(); i++) {  
                sb.append(mf.format(new Object[]{i}));  
                if (i < taskList.size() - 1) {  
                    sb.append(",");  
                }  
            }  
            sb.append("on duplicate key update effective_date=values(effective_date),create_time=values(create_time),create_person=values(create_person), agent_rate_type=values(agent_rate_type),cost_rate_type=values(cost_rate_type),single_amount=values(single_amount),rate=values(rate),capping=values(capping),safe_line=values(safe_line),");
            sb.append("ladder1_rate=values(ladder1_rate),");
            sb.append("ladder1_max=values(ladder1_max),ladder2_rate=values(ladder2_rate),ladder2_max=values(ladder2_max),ladder3_rate=values(ladder3_rate),ladder3_max=values(ladder3_max),ladder4_rate=values(ladder4_rate),ladder4_max=values(ladder4_max),");
            sb.append("ladder1_safe_line=values(ladder1_safe_line),ladder2_safe_line=values(ladder2_safe_line),ladder3_safe_line=values(ladder3_safe_line),ladder4_safe_line=values(ladder4_safe_line)");
            System.out.println(sb.toString());
            return sb.toString(); 
    	}
	}
	
	@Select("select * from out_account_service where id=#{id}")
	@ResultType(OutAccountService.class)
	OutAccountService getOutAccountServiceById(@Param("id")Integer serviceId);

	@Select("select s.*,o.acq_name as acq_org_name from out_account_service s LEFT JOIN acq_org o on s.acq_org_id = o.id")
	@ResultType(OutAccountService.class)
	List<OutAccountService> queryOutAccountServiceNoPage();
	
}
