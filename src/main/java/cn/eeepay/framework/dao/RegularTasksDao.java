package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.eeepay.framework.model.AcqServiceRate;
import cn.eeepay.framework.model.AcqServiceRateTask;

public interface RegularTasksDao {
//	"select * from acq_service_rate where id in (#{ids})"
	@SelectProvider(type=SqlProvider.class,method="selectAsrInfoByIds")
    @ResultType(AcqServiceRate.class)
    List<AcqServiceRate> selectAsrInfoByIds(@Param("list")List<Integer> list);
    
    @Select("SELECT c. * FROM acq_service_rate b, acq_service_rate_task c,(SELECT acq_service_rate_id,MAX(t.effective_date)AS effective_date_max "
    		+ "FROM acq_service_rate_task t WHERE (NOW() >= t.effective_date) GROUP BY(t.acq_service_rate_id))a "
    		+ "WHERE b.id = c.acq_service_rate_id AND b.effective_date < a.effective_date_max "
    		+ "and a.effective_date_max = c.effective_date and c.acq_service_rate_id=a.acq_service_rate_id;")
    @ResultType(AcqServiceRateTask.class)
    List<AcqServiceRateTask> selectByEffectiveDate();
	
//	@Insert("insert into acq_service_rate (acq_service_id, rate_type, card_rate_type, single_amount, rate, capping, safe_line, effective_date, effective_status, create_person,"
//			+ "ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max)"
//			+ " VALUES(#{acqServiceId},#{rateType},#{cardRateType},#{singleAmount},#{rate},#{capping},#{safeLine},#{effectiveDate},#{effectiveStatus},#{createPerson},"
//			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max})")
//	int insertAcqServiceRateByTask(AcqServiceRateTask acqServiceRateTask);
	
	@Update("update acq_service_rate set rate_type=#{rateType},single_amount=#{singleAmount},"
			+ "rate=#{rate},capping=#{capping},safe_line=#{safeLine},effective_date=#{effectiveDate},"
			+ "create_person=#{createPerson},create_time=#{createTime},ladder1_rate=#{ladder1Rate},ladder1_max=#{ladder1Max},ladder2_rate=#{ladder2Rate},"
			+ "ladder2_max=#{ladder2Max},ladder3_rate=#{ladder3Rate},ladder3_max=#{ladder3Max},ladder4_rate=#{ladder4Rate},"
			+ "ladder4_max=#{ladder4Max} where id=#{acqServiceRateId}")
	int updateAcqServiceRateByTask(AcqServiceRateTask acqServiceRateTask);
	
	@Insert("insert into acq_service_rate_task (acq_service_rate_id, rate_type, card_rate_type, single_amount, rate, capping, safe_line, effective_date, effective_status, create_person,"
			+ "ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max)"
			+ " VALUES(#{id},#{rateType},#{cardRateType},#{singleAmount},#{rate},#{capping},#{safeLine},#{effectiveDate},#{effectiveStatus},#{createPerson},"
			+ "#{ladder1Rate}, #{ladder1Max}, #{ladder2Rate}, #{ladder2Max}, #{ladder3Rate}, #{ladder3Max}, #{ladder4Rate}, #{ladder4Max})")
	int insertAcqServiceRateTask(AcqServiceRateTask acqServiceRateTask);
	
	@Update("update acq_service_rate_task set rate_type=#{rateType},single_amount=#{singleAmount},"
			+ "rate=#{rate},capping=#{capping},safe_line=#{safeLine},effective_date=#{effectiveDate}"
			+ "create_person=#{createPerson},create_time=#{createTime},ladder1_rate=#{ladder1Rate},ladder1_max=#{ladder1Max},ladder2_rate=#{ladder2Rate},"
			+ "ladder2_max=#{ladder2Max},ladder3_rate=#{ladder3Rate},ladder3_max=#{ladder3Max},ladder4_rate=#{ladder4Rate},"
			+ "ladder4_max=#{ladder4Max} where acq_service_id=#{acqServiceId}")
	int updateAcqServiceRateTaskByRate(AcqServiceRate acqServiceRate);
//	" id in (#{ids})"
	@DeleteProvider(type=SqlProvider.class,method="deleteBatchAcqServiceRateTaskByIds")
	int deleteBatchAcqServiceRateTaskByIds(@Param("list")List<Integer> list);

	@UpdateProvider(type=SqlProvider.class,method="updateBatchAcqServiceRateByTask")
	int updateBatchAcqServiceRateByTask(@Param("list")List<AcqServiceRateTask> list);
	
	@InsertProvider(type=SqlProvider.class,method="insertBatchAcqServiceRateTaskByRate")
	int insertBatchAcqServiceRateTaskByRate(@Param("list")List<AcqServiceRate> list);

	@Update("UPDATE terminal_apply SET status='2',remark='直属代理商超过三天没处理,转为一级代理商处理' WHERE status='0' AND DATEDIFF(CURRENT_TIME, create_time)>=3")
	int updateTerminalApplyStatus();

	public class SqlProvider{
		public String updateBatchAcqServiceRateByTask(Map<String, List<AcqServiceRateTask>> param){
			List<AcqServiceRateTask> list = param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append("insert into acq_service_rate(id,acq_service_id, rate_type, card_rate_type, single_amount, rate, capping,"
					+ " safe_line, effective_date, effective_status, create_person,"
					+ "ladder1_rate, ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max) values");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].acqServiceRateId},0,#'{'list[{0}].rateType},"
					+ "#'{'list[{0}].cardRateType},#'{'list[{0}].singleAmount},#'{'list[{0}].rate},#'{'list[{0}].capping},"
            		+ "#'{'list[{0}].safeLine},#'{'list[{0}].effectiveDate},#'{'list[{0}].effectiveStatus},#'{'list[{0}].createPerson},"
            		+ "#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},"
            		+ "#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},"
            		+ "#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");
			for(int i=0;i<list.size();i++){
				sb.append(messageFormat.format(new Integer[]{i}));
                sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(" on duplicate key update rate_type=values(rate_type),single_amount=values(single_amount),");
			sb.append("rate=values(rate),capping=values(capping),safe_line=values(safe_line),effective_date=values(effective_date),");
			sb.append("create_person=values(create_person),create_time=values(create_time),ladder1_rate=values(ladder1_rate),ladder1_max=values(ladder1_max),ladder2_rate=values(ladder2_rate),");
			sb.append("ladder2_max=values(ladder2_max),ladder3_rate=values(ladder3_rate),ladder3_max=values(ladder3_max),ladder4_rate=values(ladder4_rate),");
			sb.append("ladder4_max=values(ladder4_max)");
            System.out.println(sb.toString());
			return sb.toString();
		}
		
		public String insertBatchAcqServiceRateTaskByRate(Map<String, List<AcqServiceRate>> param){
			List<AcqServiceRate> list = param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append("insert into acq_service_rate_task (acq_service_rate_id, rate_type, card_rate_type, single_amount,"
					+ " rate, capping, safe_line, effective_date, effective_status, create_person,ladder1_rate, "
					+ "ladder1_max, ladder2_rate, ladder2_max, ladder3_rate, ladder3_max, ladder4_rate, ladder4_max) values");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].id},#'{'list[{0}].rateType},"
					+ "#'{'list[{0}].cardRateType},#'{'list[{0}].singleAmount},#'{'list[{0}].rate},#'{'list[{0}].capping},"
            		+ "#'{'list[{0}].safeLine},#'{'list[{0}].effectiveDate},2,#'{'list[{0}].createPerson},"
            		+ "#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},#'{'list[{0}].ladder2Rate},"
            		+ "#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},"
            		+ "#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");
            for (int i = 0; i < list.size(); i++) {
                sb.append(messageFormat.format(new Integer[]{i}));
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
			return sb.toString();
		}
		
		public String selectAsrInfoByIds(Map<String, List<Integer>> param){
			List<Integer> list = param.get("list");
			StringBuilder sb = new StringBuilder();
			if(list!=null && list.size()>0){
				sb.append("select * from acq_service_rate where id in(");
				MessageFormat messageFormat = new MessageFormat("#'{'list[{0}]},");
				for (int i = 0; i < list.size(); i++) {
					sb.append(messageFormat.format(new Integer[]{i}));
				}
				sb.setLength(sb.length()-1);
				sb.append(")");
			}
			return sb.toString();
		}
		
		public String deleteBatchAcqServiceRateTaskByIds(Map<String, List<Integer>> param){
			List<Integer> list = param.get("list");
			StringBuilder sb = new StringBuilder();
			if(list!=null && list.size()>0){
				sb.append("delete from acq_service_rate_task where id in(");
				MessageFormat messageFormat = new MessageFormat("#'{'list[{0}]},");
				for (int i = 0; i < list.size(); i++) {
					sb.append(messageFormat.format(new Integer[]{i}));
				}
				sb.setLength(sb.length()-1);
				sb.append(")");
			}
			return sb.toString();
		}
	}

}
