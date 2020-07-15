package cn.eeepay.framework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AcqServiceRate;
import cn.eeepay.framework.model.AcqServiceRateTask;
import cn.eeepay.framework.model.AcqServiceTransRules;

/**
 * 收单服务dao
 * 
 * @author junhu
 *
 */
public interface GroupServiceDao {

	@Select("select * from acq_org")
	@ResultType(AcqOrg.class)
	List<AcqOrg> acqOrgSelectBox();
	
	@SelectProvider(type = SqlProvider.class, method = "listAcqServiceByCon")
	@ResultType(AcqService.class)
	List<AcqService> listAcqServiceByCon(Map<String, Object> param, Page<AcqService> page);

	@Insert("insert into acq_service (acq_id,service_type,service_name,fee_is_card,quota_is_card,service_remark,service_status,allow_trans_start_time,allow_trans_end_time,create_person,acq_enname)"
			+ " VALUES(#{acqId},#{serviceType},#{serviceName},#{feeIsCard},#{quotaIsCard},#{serviceRemark},#{serviceStatus},#{allowTransStartTime},#{allowTransEndTime},#{createPerson},#{acqEnname})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertAcqService(AcqService acqService);

	@Insert("insert into acq_service_rate (acq_service_id, rate_type, card_rate_type, single_amount, rate, capping, safe_line, effective_date, effective_status, create_person,"
			+ "ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max)"
			+ " VALUES(#{acqServiceId},#{rateType},#{cardRateType},#{singleAmount},#{rate},#{capping},#{safeLine},#{effectiveDate},#{effectiveStatus},#{createPerson},"
			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertAcqServiceRate(AcqServiceRate acqServiceRate);
	
	@Insert("insert into acq_service_trans_rules (acq_service_id, savings_single_min_amount, savings_single_max_amount, savings_day_total_amount, credit_single_min_amount, credit_single_max_amount, credit_day_total_amount, day_total_amount, warning_phone, trans_limit_min_amount, trans_limit_max_amount, clint_msg, create_person)"
			+ " VALUES(#{acqServiceId},#{savingsSingleMinAmount},#{savingsSingleMaxAmount},#{savingsDayTotalAmount},#{creditSingleMinAmount},#{creditSingleMaxAmount},#{creditDayTotalAmount},#{dayTotalAmount},#{warningPhone},#{transLimitMinAmount},#{transLimitMaxAmount},#{clintMsg},#{createPerson})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertAcqServiceTransRules(AcqServiceTransRules acqServiceTransRules);

	@Update("update acq_service " +
			" set service_status=#{serviceStatus}," +
			"     time_switch=0, " +
			"     time_start_time=null, " +
			"     time_end_time=null, " +
			"     periodicity_start_time=null, " +
			"     periodicity_end_time=null, " +
			"     close_prompt=#{closePrompt} " +
			"  where id=#{id}")
	int updateAcqServiceStatus(AcqService acq);


	@Select("select s.*, o.acq_name from acq_service s LEFT JOIN acq_org o on s.acq_id = o.id where s.id=#{id}")
	@ResultType(AcqService.class)
	AcqService queryServiceById(Long id);

	@Select("select * from acq_service_rate where acq_service_id=#{acqServiceId}")
	@ResultType(AcqServiceRate.class)
	List<AcqServiceRate> queryServiceRateByServiceId(Long acqServiceId);

	@Select("select * from acq_service_trans_rules where  acq_service_id=#{acqServiceId}")
	@ResultType(AcqServiceTransRules.class)
	AcqServiceTransRules queryServiceTransRulesByServiceId(Long acqServiceId);

	@Select("SELECT * FROM acq_service_rate WHERE id = #{rateId} UNION SELECT * FROM acq_service_rate_task WHERE acq_service_rate_id = #{rateId} AND card_rate_type = #{cardType}")
	@ResultType(AcqServiceRate.class)
	List<AcqServiceRate> queryServiceRateByRateId(@Param("rateId") Long rateId, @Param("cardType") Integer cardType);

	@Delete("delete from acq_service_rate_task where id=#{id}")
	int deleteServiceRateTask(Long id);

	@Insert("insert into acq_service_rate_task (acq_service_rate_id, rate_type, card_rate_type, single_amount, rate, capping, safe_line, effective_date, effective_status, create_person,"
			+ "ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max)"
			+ " VALUES(#{acqServiceRateId},#{rateType},#{cardRateType},#{singleAmount},#{rate},#{capping},#{safeLine},#{effectiveDate},#{effectiveStatus},#{createPerson},"
			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertAcqServiceRateTask(AcqServiceRateTask acqServiceRateTask);
	
	@Select("select * from acq_service_trans_rules where acq_service_id = #{acqServiceId}")
	@ResultType(AcqServiceTransRules.class)
	AcqServiceTransRules queryServiceTransRuleByServiceId(Long acqServiceId);

	@Update("update acq_service_trans_rules set savings_single_min_amount = #{savingsSingleMinAmount},"
			+ "savings_single_max_amount = #{savingsSingleMaxAmount},savings_day_total_amount = #{savingsDayTotalAmount},"
			+ "credit_single_min_amount = #{creditSingleMinAmount},credit_single_max_amount = #{creditSingleMaxAmount},"
			+ "credit_day_total_amount = #{creditDayTotalAmount},day_total_amount = #{dayTotalAmount},"
			+ "warning_phone = #{warningPhone},trans_limit_min_amount = #{transLimitMinAmount},"
			+ "trans_limit_max_amount = #{transLimitMaxAmount},clint_msg = #{clintMsg} where acq_service_id = #{acqServiceId}")
	int updateAcqServiceTransRulesByServiceId(AcqServiceTransRules acqServiceTransRules);

	@Update("update acq_service " +
			" set time_switch = #{timeSwitch},time_start_time = #{timeStartTime},"+
			" time_end_time = #{timeEndTime},close_prompt = #{closePrompt} ,"+
			" periodicity_start_time = #{periodicityStartTime},periodicity_end_time = #{periodicityEndTime}"+
			" where id = #{id}")
	int updateTimeSwitch(AcqService acq);

	public class SqlProvider {
		public String listAcqServiceByCon(final Map<String, Object> param) {
			return new SQL() {
				{
					SELECT("s.*, o.acq_name");
					FROM("acq_service s LEFT JOIN acq_org o on s.acq_id = o.id");
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqId") == null ? "" : param.get("acqId")))) {
						WHERE("s.acq_id=#{acqId}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("serviceType") == null ? "" : param.get("serviceType")))) {
						WHERE("s.service_type=#{serviceType}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("serviceName") == null ? "" : param.get("serviceName")))) {
						WHERE("s.service_name like CONCAT('%',#{serviceName},'%')");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("feeIsCard") == null ? "" : param.get("feeIsCard")))) {
						WHERE("s.fee_is_card=#{feeIsCard}");
					}
					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("quotaIsCard") == null ? "" : param.get("quotaIsCard")))) {
						WHERE("s.quota_is_card=#{quotaIsCard}");
					}
				}

			}.toString();
		}

	}
	
	@Update("update acq_service " +
			" set service_status=#{serviceStatus}" +
			"  where id=#{id}")
	void updateServiceStatus(AcqService acqService);
	
	@Select("SELECT a_s.id AS id, a_s.service_name AS serviceName, a_o.acq_name AS acqOrgName, a_s.service_type AS serviceType FROM acq_service a_s LEFT JOIN acq_org a_o ON a_s.acq_id = a_o.id")
	@ResultType(HashMap.class)
	List<Map<String, Object>> findAllServiceInfo();

}
