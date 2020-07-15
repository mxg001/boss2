package cn.eeepay.framework.dao.capitalInsurance;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.CheckBill;
import cn.eeepay.framework.model.capitalInsurance.TransOrder;
import cn.eeepay.framework.util.StringUtil;

public interface CheckBillDao {
	/**
	 * 
	 * @param tis
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(CheckBill.class)
	List<CheckBill> queryAllInfo(@Param("tis") CheckBill tis,  @Param("page") Page<CheckBill> page);
	
	/**
	 * 
	 * SqlProvider
	 *
	 */
	public class SqlProvider{
		public String selectAllInfo(Map<String,Object> param){
    		final CheckBill tis = (CheckBill)param.get("tis");
    		return new SQL(){{
    			SELECT("id,batch_no,insurer,order_type,acq_total_amount,acq_total_count,acq_success_count,acq_fail_count,sys_total_amount,"
    					+ "sys_total_count,sys_success_count,sys_fail_count,check_status,file_date,check_time,file_name,create_person,create_time,remark ");
    			FROM("zjx_check_bill");
    			if(StringUtils.isNotBlank(tis.getBatchNo())){
    				WHERE(" batch_no=#{tis.batchNo}");
    			}
    			if(StringUtils.isNotBlank(tis.getInsurer()) && !"0".equals(tis.getInsurer())){
    				WHERE(" insurer=#{tis.insurer}");
    			}
    			if(tis.getOrderType()!=null && tis.getOrderType() != 0){
    				WHERE(" order_type=#{tis.orderType}");
    				
    			}
    			if(tis.getCreateTime() != null){
    				WHERE(" create_time>=#{tis.createTime}");
    			}
    			if(tis.getCreateTimeEnd()!= null){
    				WHERE(" create_time<=#{tis.createTimeEnd}");
    			}
    			if(tis.getCheckStatus()!=null && tis.getCheckStatus() != 0){
    				WHERE(" check_status=#{tis.checkStatus}");
    			}
    			
    		}}.toString();
    	}

		public String batchTransOrder(Map<String,Object> param){
    		final Set<String> keys = (Set<String>)param.get("keys");
    		final String startDate = (String)param.get("startDate");
    		final String endDate = (String)param.get("endDate");
    		final Integer orderType = (Integer)param.get("orderType");

    		StringBuffer SQL= new StringBuffer();
    		SQL.append("SELECT ")
    		.append("trans.bx_order_no as orderNo, ")
    		.append("people.c_nme as holder, ")
    		.append("trans.n_amt as insure_amount, ")
    		.append("trans.n_fee as acq_amount, ")
    		.append("trans.n_fee as sys_amount, ")
    		.append("trans.n_prm as bill_amount, ")
    		.append("trans.bx_type as sys_bill_status, ")
    		.append("trans.t_time as bill_time, ")
    		.append("trans.t_begin_time as  effective_stime, ")
    		.append("trans.t_end_time as effective_etime, ")
    		.append("trans.one_agent_no as one_agent_no, ")
    		.append("trans.bx_unit as insurer, ")
    		.append("trans.prod_no as product_no, ")
    		.append("trans.third_order_no  as acq_bill_no ")
    		.append("FROM ")
    		.append("zjx_trans_order trans ")
    		.append("LEFT JOIN zjx_trans_order_people people ON trans.bx_order_no = people.bx_order_no ");
    		StringBuffer inSQL = new StringBuffer("");
    		for (String key : keys) {
    			inSQL.append("'").append(key).append("',");
			}
    		SQL.append("where trans.bx_order_no in("+inSQL.substring(0, inSQL.length()-1)+") ");
    		if(!StringUtil.isBlank(startDate)){
    			SQL.append(" and t_time>='"+startDate+"'");
    		}
    		if(!StringUtil.isBlank(endDate)){
    			SQL.append(" and t_time<='"+endDate+"'");
    		}
    		if(orderType == 1){
    			SQL.append(" and bx_type in('SUCCESS','OVERLIMIT','RECEDEFAILED')");
    		}else{
    			SQL.append(" and bx_type ='OVERLIMIT' ");
    		}
    		return SQL.toString();
    	}
		
		
		public String insertBatchEnery(Map<String,Object> param){
    		final  List<BillEntry> newEntry = (List<BillEntry>)param.get("newEntry");
    		StringBuffer sql= new StringBuffer();
    		sql.append("INSERT INTO zjx_check_bill_entry (batch_no,insurer,order_type,product_no,holder,acq_order_no,sys_order_no,"
    				+ "acq_amount,sys_amount,acq_bill_no,sys_bill_no,insure_amount,trans_status,insure_status,acq_bill_status,"
    				+ "sys_bill_status,insure_time,bill_time,effective_stime,effective_etime,check_status,"
    				+ "create_person,create_time,one_agent_no,bill_amount) VALUES ");
    		int size = newEntry.size();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		for (int index = 0; index < size; index++) {
    			BillEntry be =  newEntry.get(index);
    					sql.append("('").append(be.getBatchNo()).append("',");
    					sql.append("'").append(be.getInsurer()).append("',");
    					sql.append("'").append(be.getOrderType()).append("',");
    					sql.append("'").append(be.getProductNo()).append("',");
    					sql.append("'").append(be.getHolder()).append("',");
    					sql.append("'").append(be.getAcqOrderNo()).append("',");
    					sql.append("'").append(be.getSysOrderNo()).append("',");
    					sql.append("'").append(be.getAcqAmount()).append("',");
    					sql.append("'").append(be.getSysAmount()).append("',");
    					sql.append("'").append(be.getAcqBillNo()).append("',");
    					sql.append("'").append(be.getSysBillNo()).append("',");
    					sql.append("'").append(be.getInsureAmount()).append("',");
    					sql.append("'").append(be.getTransStatus()).append("',");
    					sql.append("'").append(be.getInsureStatus()).append("',");
    					sql.append("'").append(be.getAcqBillStatus()).append("',");
    					sql.append("'").append(be.getSysBillStatus()).append("',");
    					sql.append("'").append(sdf.format(be.getInsureTime())).append("',");
    					sql.append("'").append(sdf.format(be.getBillTime())).append("',");
    					sql.append("'").append(sdf.format(be.getEffectiveStime())).append("',");
    					sql.append("'").append(sdf.format(be.getEffectiveEtime())).append("',");
    					sql.append("'").append(be.getCheckStatus()).append("',");
    					sql.append("'").append(be.getCreatePerson()).append("',");
    					sql.append("now(),");
    					sql.append("'").append(be.getOneAgentNo()).append("',");
    					if(index == size-1){
    						sql.append("'").append(be.getBillAmount()).append("');");
    					}else{
    						sql.append("'").append(be.getBillAmount()).append("'),");
    					}
    					
			}
    		return sql.toString();
    	}
	}
	/**
	 * 
	 * @param batchNo
	 * @return
	 */
	@Delete("delete from zjx_check_bill where batch_no=#{batchNo}")
	@ResultType(Integer.class)
	Integer delByBatchNo(@Param("batchNo") String batchNo);
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Select("select bx_order_no from zjx_trans_order where bx_unit=#{bxUnit} and bx_type in('SUCCESS','OVERLIMIT','RECEDEFAILED') and t_time>=#{startDate} and t_time<=#{endDate}")
	@ResultType(String.class)
	LinkedList<String> getTransOrderSuccess(@Param("bxUnit")String bxUnit,@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Select("select bx_order_no from zjx_trans_order where bx_unit=#{bxUnit} and bx_type ='OVERLIMIT' and t_time>=#{startDate} and t_time<=#{endDate}")
	@ResultType(String.class)
	LinkedList<String> getTransOrderOver(@Param("bxUnit")String bxUnit,@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 
	 * @param keys
	 * @param orderType 
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="batchTransOrder")
	@ResultType(TransOrder.class)
	List<TransOrder> getBatchTransOrder(@Param("keys") Set<String> keys,@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orderType")Integer orderType);
	/**
	 * 
	 * @param bill
	 * @return
	 */
	@Insert("INSERT INTO zjx_check_bill ( batch_no,insurer,order_type,acq_total_amount,acq_total_count,acq_success_count,"
			+ "acq_fail_count,sys_total_amount,sys_total_count,sys_success_count,sys_fail_count,check_status,file_date,"
			+ "check_time,file_name,create_person,create_time,remark ) "
			+ "VALUES (#{bill.batchNo},#{bill.insurer},#{bill.orderType},#{bill.acqTotalAmount},#{bill.acqTotalCount},"
			+ "#{bill.acqSuccessCount},#{bill.acqFailCount},#{bill.sysTotalAmount},#{bill.sysTotalCount},"
			+ "#{bill.sysSuccessCount},#{bill.sysFailCount},#{bill.checkStatus},#{bill.fileDate},#{bill.checkTime},"
			+ "#{bill.fileName},#{bill.createPerson},now(),#{bill.remark});")
	@ResultType(Integer.class)
	Integer insertBill(@Param("bill") CheckBill bill);
	
	/**
	 * 
	 * @param newEntry
	 * @return
	 */
	@InsertProvider(type=SqlProvider.class,method="insertBatchEnery")
	@ResultType(Integer.class)
	Integer insertBatchEnery(@Param("newEntry")List<BillEntry> newEntry);
	
}
