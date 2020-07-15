package cn.eeepay.framework.dao.capitalInsurance;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.util.StringUtil;

public interface BillEneryDao {
	/**
	 * 
	 * @param tis
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(BillEntry.class)
	List<BillEntry> queryAllInfo(@Param("tis") BillEntry tis, @Param("page") Page<BillEntry> page);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Select("select billEntry.*,agent1.agent_name oneAgentName " +
			" from zjx_check_bill_entry billEntry " +
			"	LEFT JOIN agent_info  agent1 ON agent1.agent_no=billEntry.one_agent_no " +
			"where billEntry.id=#{id}")
	@ResultType(BillEntry.class)
	BillEntry getEneryDetail(@Param("id") String id);
	
	/**
	 * 
	 * @param tis
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(BillEntry.class)
	List<BillEntry> importAllInfo(@Param("tis") BillEntry tis);
	
	/**
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="initReport")
	@ResultType(ShareReport.class)
	List<ShareReport> getInitReport(@Param("sDate") String sDate,@Param("eDate") String eDate);
	
	/**
	 * 
	 * @param batchNo
	 * @param sDate
	 * 
	 * @param eDate
	 * @return
	 */
	@Update("update zjx_check_bill_entry set report_batch_no=#{batchNo},report_status=1 where bill_time >=#{sDate} and bill_time <=#{eDate} "
			+ "and trans_status='SUCCESS' and insure_status='SUCCESS' and check_status=1 and  report_status=2 ")
	@ResultType(Integer.class)
	Integer updateReportStatus(@Param("batchNo") String batchNo, @Param("sDate") String sDate, @Param("eDate") String eDate);
	
	/**
	 * 
	 * @param batchNo
	 * @return
	 */
	@Delete("delete from zjx_check_bill_entry where batch_no=#{batchNo}")
	@ResultType(Integer.class)
	Integer delByBatchNo(@Param("batchNo") String batchNo);
	
	/**
	 * 
	 * @param batchNo
	 * @return
	 */
	@Select("select count(0) from zjx_check_bill_entry where batch_no =#{batchNo} and report_status =1")
	@ResultType(Integer.class)
	Integer reportNumByBatchNo(@Param("batchNo") String batchNo);
	
	/**
	 * 
	 */
	public class SqlProvider{
		public String selectAllInfo(Map<String,Object> param){
    		final BillEntry tis = (BillEntry)param.get("tis");
    		return new SQL(){{
    			SELECT(" billEntry.*,agent1.agent_name oneAgentName ");
    			FROM("zjx_check_bill_entry billEntry ");
				LEFT_OUTER_JOIN("agent_info  agent1 ON agent1.agent_no=billEntry.one_agent_no");
    			if(StringUtils.isNotBlank(tis.getBatchNo())){
    				WHERE(" billEntry.batch_no=#{tis.batchNo}");
    			}
    			if(tis.getCreateTime()!=null){
    				WHERE(" billEntry.create_time>=#{tis.createTime}");
    			}
    			if(tis.getCreateTimeEnd()!=null){
    				WHERE(" billEntry.create_time<=#{tis.createTimeEnd}");
    			}
    			if(StringUtils.isNotBlank(tis.getInsurer()) && !"0".equals(tis.getInsurer())){
    				WHERE(" billEntry.insurer=#{tis.insurer}");
    			}
    			if(tis.getCheckStatus() != null && tis.getCheckStatus()!=0){
    				WHERE(" billEntry.check_status=#{tis.checkStatus}");
    			}
    			if(tis.getOrderType()!=null && tis.getOrderType()!=0){
    				WHERE(" billEntry.order_type=#{tis.orderType}");
    			}
    			if(StringUtils.isNotBlank(tis.getAcqOrderNo())){
    				WHERE(" billEntry.acq_order_no=#{tis.acqOrderNo}");
    			}
    			if(StringUtils.isNotBlank(tis.getSysOrderNo())){
    				WHERE(" billEntry.sys_order_no=#{tis.sysOrderNo}");
    			}
    			if(tis.getReportStatus()!=null && tis.getReportStatus()!=0){
    				WHERE(" billEntry.report_status=#{tis.reportStatus}");
    			}
    			if(tis.getInsureTime()!=null){
    				WHERE(" billEntry.insure_time>=#{tis.insureTime}");
    			}
    			if(tis.getInsureTimeEnd()!=null){
    				WHERE(" billEntry.insure_time<=#{tis.insureTimeEnd}");
    			}
    			if(StringUtils.isNotBlank(tis.getOneAgentNo())){
					WHERE(" billEntry.one_agent_no=#{tis.oneAgentNo}");
				}
				if(tis.getBillTimeBegin()!=null){
					WHERE(" billEntry.bill_time>=#{tis.billTimeBegin}");
				}
				if(tis.getBillTimeEed()!=null){
					WHERE(" billEntry.bill_time<=#{tis.billTimeEed}");
				}
    		}}.toString();
    	}
		
		public String initReport(Map<String,Object> param){
			final  String sDate = StringUtil.filterNull(param.get("sDate"));
			final  String eDate = StringUtil.filterNull(param.get("eDate"));
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT one_agent_no as one_agent_no,count(0) as total_count,sum(bill_amount) as total_amount");
			sb.append(" FROM zjx_check_bill_entry where 1=1 ");
			if(!StringUtil.isBlank(sDate)){
				sb.append(" and bill_time >=#{sDate}");
			}
			if(!StringUtil.isBlank(eDate)){
				sb.append(" and bill_time <=#{eDate}");
			}
			sb.append(" and trans_status='SUCCESS' and insure_status='SUCCESS' and check_status=1 and  report_status=2 ");
			sb.append(" GROUP BY one_agent_no");
			return sb.toString();
    	}

	}
	@Select("select count(0) from zjx_check_bill_entry where insurer =#{insurer} and order_type =#{orderType} and bill_time >=#{startDate} and bill_time <=#{endtDate} limit 1 ")
	@ResultType(Integer.class)
	Integer isUploadFile(@Param("orderType")String orderType, @Param("insurer")String insurer, @Param("startDate")String startDate, @Param("endtDate")String endtDate);
}
