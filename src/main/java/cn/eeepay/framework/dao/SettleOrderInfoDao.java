package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SettleOrderInfoDao {

	Logger log = LoggerFactory.getLogger(SettleOrderInfoDao.class);

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> selectAllInfo(@Param("page")Page<SettleOrderInfo> page,@Param("soi")SettleOrderInfo soi);

	// 20171011,mys,优化导出
	@SelectProvider(type=SqlProvider.class,method="importAllInfo")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> importAllInfo(@Param("soi")SettleOrderInfo soi);

	@SelectProvider(type=SqlProvider.class,method="getTotalNumAndTotalMoney")
	@ResultType(String.class)
	String getTotalNumAndTotalMoney(@Param("soi")SettleOrderInfo soi);

	@Select("SELECT soi.*,mis.merchant_name as settle_user_name,ais.agent_name as settle_user_name,uis.user_name from settle_order_info soi  "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=soi.settle_user_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=soi.settle_user_no "
			+ "LEFT JOIN user_info uis on uis.id=soi.create_user "
			+ "where soi.settle_order=#{settleOrder}")
	@ResultType(SettleOrderInfo.class)
	SettleOrderInfo selectInfo(@Param("settleOrder")String settleOrder);

	@Select("select s.*,o.service_name from settle_transfer s "
			+ "left join out_account_service o on s.out_service_id=o.id "
			+ "where s.settle_type>1 and s.trans_id=#{tranId} ORDER BY s.create_time desc")
	@ResultType(SettleTransfer.class)
	List<SettleTransfer> selectSettleInfo(@Param("tranId")String tranId);

	@SelectProvider(type=SqlProvider.class,method="selectOutDetailAllInfo")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> selectOutDetailAllInfo(@Param("soi")SettleOrderInfo soi);

	@SelectProvider(type=SqlProvider.class,method="getOutDetailTotalMoney")
	@ResultType(String.class)
	Map<String, String> getOutDetailTotalMoney(@Param("soi")SettleOrderInfo soi);

	// 20171011,mys,优化导出
    @SelectProvider(type=SqlProvider.class,method="exportOutDetailAllInfo")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> exportOutDetailAllInfo(@Param("soi")SettleOrderInfo soi);

	@Select("select s.*,o.service_name,mis.merchant_name as settle_user_name,ais.agent_name as settle_user_name from settle_transfer s "
			+ "left join out_account_service o on s.out_service_id=o.id "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=s.settle_user_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=s.settle_user_no "
			+ "where s.id=#{id}")
	@ResultType(SettleTransfer.class)
	SettleTransfer selectOutSettleInfo(@Param("id")String id);

	@Select("select settle_msg,syn_status,order_no,settle_status,'system' AS createUser,account,create_time "
			+ "from collective_trans_order "
			+ "where order_no=#{orderNo}")
	@ResultType(SettleOrderInfo.class)
	SettleOrderInfo selectOrderNoInfo(@Param("orderNo")String orderNo);

	@Select("select settle_order,settle_type,settle_status "
			+ " from settle_order_info "
			+ " where settle_order=#{settleOrder}")
	@ResultType(SettleOrderInfo.class)
	SettleOrderInfo getBySettleOrder(@Param("settleOrder")String settleOrder);

	@Update("update settle_order_info set settle_order_status=#{i} where settle_order=#{settleOrder}")
	int updateSettleOrderStatus(@Param("settleOrder")String settleOrder, @Param("i")int i);

	@Select("select soi.settle_order,soi.create_time,soi.settle_amount,soi.settle_status from settle_order_info soi "
			+ " where soi.settle_type=#{soi.settleType} and soi.sub_type=#{soi.subType}"
			+ " and soi.settle_user_type=#{soi.settleUserType}"
			+ " and soi.settle_user_no=#{soi.settleUserNo} order by soi.create_time desc")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> getCashPage(@Param("soi")SettleOrderInfo settleOrderInfo, @Param("page")Page<SettleOrderInfo> page);



	//出款变更状态
	@Update("update settle_transfer set status=#{currSettleStatus} ,err_msg=#{changeSettleStatusReason} where account_serial_no=#{accountSerialNo}")
	public int changeSettleStatus(@Param("currSettleStatus")String currSettleStatus, @Param("changeSettleStatusReason") String changeSettleStatusReason ,@Param("accountSerialNo") String accountSerialNo);

	@Insert("insert into settle_status_change_log (order_id,old_settle_status,curr_settle_status,operate_person,order_origin,change_settle_status_reason)values(#{orderNo},#{oldSettleStatus},#{currSettleStatus}" +
			",#{operatorPerson},#{orderOrigin},#{changeSettleStatusReason})")
	public void saveChangeSettleStatusLog(@Param("currSettleStatus")String currSettleStatus, @Param("oldSettleStatus")String oldSettleStatus, @Param("changeSettleStatusReason")String changeSettleStatusReason, @Param("orderNo")String orderNo, @Param("operatorPerson")String operatorPerson,@Param("orderOrigin")Integer orderOrigin);

	@Select("select count(1) total from settle_status_change_log where order_id in (${id}) and order_origin = #{orderOrigin}")
	@ResultType(Integer.class)
    public int checkCanChangeSettleStatus(@Param("id") String id,@Param("orderOrigin") Integer orderOrigin);

	@Select(" SELECT" +
			" IFNULL(t.settle_status,soi.settle_status) AS settle_status" +
			" from settle_transfer s \n" +
			" LEFT JOIN collective_trans_order t ON t.id = s.trans_id AND s.settle_type = 1 " +
			" LEFT JOIN settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type <> 1 " +
			" where s.account_serial_no = #{account_serial_no}")
	@ResultType(Integer.class)
	Integer getSettleStatusById(@Param("account_serial_no")String account_serial_no);

	@Select("select settle_status from collective_trans_order where order_no = #{order_no}")
	@ResultType(Integer.class)
	Integer getCTOStatusById(@Param("order_no") String order_no);

	@Update("update collective_trans_order set settle_status = #{currSettleStatus},settle_msg=#{changeSettleStatusReason} where order_no = #{orderNo}")
	int changeCTOStatus(@Param("currSettleStatus")String currSettleStatus, @Param("changeSettleStatusReason")String changeSettleStatusReason, @Param("orderNo")String orderNo);

	@SelectProvider(type=SqlProvider.class,method="selectByAccountNos")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> selectByAccountNos(@Param("page")Page<SettleOrderInfo> page, @Param("accountNos")String[] accountNos);

	@SelectProvider(type=SqlProvider.class,method="selectTotalMoneyByAccountNos")
	@ResultType(String.class)
	Map<String, String> selectTotalMoneyByAccountNos(@Param("accountNos") String[] accountNos);

	@SelectProvider(type=SqlProvider.class,method="selectByAccountNos")
	@ResultType(SettleOrderInfo.class)
	List<SettleOrderInfo> exportByAccountNos(@Param("accountNos") String[] accountNos);

	public class SqlProvider{



		public String selectTotalMoneyByAccountNos(Map<String,Object> param){
			final String[] accountNos = (String[])param.get("accountNos");
			SQL sql = new SQL(){
				{
					SELECT("IFNULL(SUM(s.amount), 0) AS ckje,IFNULL(SUM(s.fee_amount), 0) AS cksxf,IFNULL(SUM(s.deduction_fee), 0) AS dksxf,IFNULL(SUM(s.out_amount), 0) AS sjckje,IFNULL(SUM(s.actual_fee), 0) AS sjcksxf," +
							"IFNULL(COUNT(s.id),0) AS countNum");
					FROM("settle_transfer s");
					LEFT_OUTER_JOIN("collective_trans_order t ON t.id = s.trans_id AND s.settle_type = 1");
					LEFT_OUTER_JOIN("settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type <> 1");
					LEFT_OUTER_JOIN("scan_code_trans sct ON sct.trade_no = s.order_no");
					LEFT_OUTER_JOIN("trans_info ti ON ti.order_no=t.order_no");
					WHERE("s.account_serial_no IN " + accountNosStr(accountNos));
				}
			};
			return sql.toString();
		}

		public String selectByAccountNos(Map<String,Object> param){
			final String[] accountNos = (String[])param.get("accountNos");
			SQL sql = new SQL(){
				{
					SELECT("s.settle_user_type,s.id,s.trans_id,s.order_no,s.account_serial_no,soi.settle_order,oas.service_name AS out_service_name," +
							"s.amount,s.out_amount,s.fee_amount,s.create_time,s.settle_bank AS acq_enname,s.settle_type," +
							"IFNULL(t.settle_status,soi.settle_status) AS settle_status,IFNULL(mis.merchant_name,ais.agent_name) AS settle_user_name," +
							"IFNULL(mis.mobilephone,ais.mobilephone) AS mobile,s.`status`,s.settle_user_no,soi.source_batch_no,soi.source_system," +
							"s.out_service_id,s.deduction_fee,s.actual_fee,t.unionpay_mer_no,soi.per_agent_fee,soi.sub_type,soi.agent_node," +
							"s.in_acc_name,s.in_acc_no,s.in_bank_name," +
							"aio.agent_name agent_name_one,aio.agent_no agent_no_one,CASE WHEN t.pay_method = '1' THEN ti.acq_reference_no ELSE sct.acq_reference_no END acq_reference_no ");
					FROM("settle_transfer s");
					LEFT_OUTER_JOIN("collective_trans_order t ON t.id = s.trans_id AND s.settle_type in (1,3)");
					LEFT_OUTER_JOIN("settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type not in (1,3)");
					LEFT_OUTER_JOIN("merchant_info mis ON mis.merchant_no=s.settle_user_no");
					LEFT_OUTER_JOIN("agent_info ais ON ais.agent_no=s.settle_user_no");
					LEFT_OUTER_JOIN("out_account_service oas ON oas.id=s.out_service_id");
					LEFT_OUTER_JOIN("agent_info aio ON aio.agent_no = mis.one_agent_no");
					LEFT_OUTER_JOIN("trans_info ti ON ti.order_no=t.order_no");
					LEFT_OUTER_JOIN("scan_code_trans sct ON sct.trade_no = t.order_no");
					WHERE("s.account_serial_no IN " + accountNosStr(accountNos));
					ORDER_BY("s.create_time DESC");
				}
			};
			return sql.toString();
		}

		private String accountNosStr(String[] accountNos){
			StringBuilder orderNoSb = new StringBuilder("(");
			for (int i = 0; i < accountNos.length; i++) {
				orderNoSb.append("'" + accountNos[i] + "'");
				if(i == accountNos.length - 1){
					orderNoSb.append(")");
				}else {
					orderNoSb.append(",");
				}
			}
			return orderNoSb.toString();
		}

	    public String selectAllInfo(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	String sql = new SQL(){{
	    		SELECT("soi.*,IFNULL(mis.merchant_name,ais.agent_name) as settle_user_name,aio.agent_name agent_name_one,aio.agent_no agent_no_one");
	    		FROM("settle_order_info soi "
	    				+ " LEFT JOIN merchant_info mis on mis.merchant_no=soi.settle_user_no"
	    				+ " LEFT JOIN agent_info ais on ais.agent_no=soi.settle_user_no"
						+ " LEFT JOIN agent_info aio ON aio.agent_no = mis.one_agent_no"
	    				+ " LEFT JOIN settle_transfer st on st.trans_id=soi.settle_order and st.settle_type = soi.settle_type ");
	   			if(soi.getSettleOrder()!=null){
	   				if(StringUtils.isNotBlank(soi.getSettleOrder().toString())){
	   					WHERE(" soi.settle_order=#{soi.settleOrder}");
	   				}
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserType())){
	    			WHERE(" soi.settle_user_type=#{soi.settleUserType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getAcqEnname())){
	    			WHERE(" soi.acq_enname=#{soi.acqEnname}");
	    		}
	    		if(StringUtils.isNotBlank(soi.getMobilephone())){
	    			WHERE(" mis.mobilephone=#{soi.mobilephone}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleType())){
	    			WHERE(" soi.settle_type=#{soi.settleType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserNo())){
	    			WHERE(" soi.settle_user_no=#{soi.settleUserNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleStatus())){
	    			WHERE(" soi.settle_status=#{soi.settleStatus}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceOrderNo())){
	    			WHERE(" soi.source_order_no=#{soi.sourceOrderNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceBatchNo())){
	    			WHERE("soi.source_batch_no=#{soi.sourceBatchNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceSystem())){
	    			WHERE("soi.source_system=#{soi.sourceSystem}");
	    		}
	   			if(soi.getSdate()!=null){
					WHERE("soi.create_time>=#{soi.sdate}");
				}
				if(soi.getEdate()!=null){
					WHERE("soi.create_time<=#{soi.edate}");
				}
				if(soi.getSubType()!=null && soi.getSubType()== -2){
					WHERE("soi.sub_type in ('10','11')");
				} else if(soi.getSubType()!=null && soi.getSubType()!=-1){
	    			WHERE("soi.sub_type=#{soi.subType}");
	    		}
				if(StringUtils.isNotBlank(soi.getSettleOrderStatus())){
	    			WHERE("soi.settle_order_status=#{soi.settleOrderStatus}");
	    		}
				if(StringUtils.isNotBlank(soi.getInAccName())){
					WHERE("st.in_acc_name=#{soi.inAccName}");
				}
	    		ORDER_BY("soi.create_time desc");
	    	}}.toString();
	    	System.out.println("出款订单查询:");
	    	System.out.println(sql);
	    	return sql;
	    }
	    
	    public String importAllInfo(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	String sql = new SQL(){{
	    		SELECT("soi.*,IFNULL(mis.merchant_name,ais.agent_name) as settle_user_name,"
//	    				+ "sd.sys_name as settle_name,sd1.sys_name as settle_status_name,"
	    				+ "s.out_amount,s.fee_amount,s.err_msg" +
						",aio.agent_name agent_name_one,aio.agent_no agent_no_one");
	    		FROM("settle_order_info soi "
	    				+ "LEFT JOIN merchant_info mis on mis.merchant_no=soi.settle_user_no "
	    				+ "LEFT JOIN agent_info ais on ais.agent_no=soi.settle_user_no "
//	    				+ "left join sys_dict sd on sd.sys_key='SETTLE_TYPE' and sd.`status` =1 and sd.sys_value=soi.settle_type "
//	    				+ "left join sys_dict sd1 on sd1.sys_key='SETTLE_STATUS' and sd1.`status` =1 and sd1.sys_value=soi.settle_status "
	    				+ "LEFT JOIN settle_transfer s on soi.settle_order = s.trans_id and s.settle_type = soi.settle_type and s.status='4' and s.correction='0' and s.settle_type<>'1'" +
						" LEFT JOIN agent_info aio ON aio.agent_no = mis.one_agent_no");
	   			if(soi.getSettleOrder()!=null){
	   				if(StringUtils.isNotBlank(soi.getSettleOrder().toString())){
	   					WHERE(" soi.settle_order=#{soi.settleOrder}");
	   				}
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserType())){
	    			WHERE(" soi.settle_user_type=#{soi.settleUserType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getAcqEnname())){
	    			WHERE(" soi.acq_enname=#{soi.acqEnname}");
	    		}
	    		if(StringUtils.isNotBlank(soi.getMobilephone())){
	    			WHERE(" mis.mobilephone=#{soi.mobilephone}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleType())){
	    			WHERE(" soi.settle_type=#{soi.settleType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserNo())){
	    			WHERE(" soi.settle_user_no=#{soi.settleUserNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleStatus())){
	    			WHERE(" soi.settle_status=#{soi.settleStatus}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceOrderNo())){
	    			WHERE(" soi.source_order_no=#{soi.sourceOrderNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceBatchNo())){
	    			WHERE(" soi.source_batch_no=#{soi.sourceBatchNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceSystem())){
	    			WHERE(" soi.source_system=#{soi.sourceSystem}");
	    		}
	   			if(soi.getSdate()!=null){
					WHERE("soi.create_time>=#{soi.sdate}");
				}
				if(soi.getEdate()!=null){
					WHERE("soi.create_time<=#{soi.edate}");
				}
				if(soi.getSubType()!=null && soi.getSubType() == -2){
					WHERE(" soi.sub_type in ('10','11')");
				} else if(soi.getSubType()!=null && soi.getSubType()!=-1){
	    			WHERE(" soi.sub_type=#{soi.subType}");
	    		}
				if(StringUtils.isNotBlank(soi.getSettleOrderStatus())){
	    			WHERE(" soi.settle_order_status=#{soi.settleOrderStatus}");
	    		}
	    		ORDER_BY("soi.create_time desc");
	    	}}.toString();
	    	System.out.println("出款订单导出:");
	    	System.out.println(sql);
	    	return sql;
	    }
	    
	    public String getTotalNumAndTotalMoney(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	return new SQL(){{
	    		SELECT("sum(soi.settle_amount) as money");
	    		FROM("settle_order_info soi "
	    				+ "LEFT JOIN merchant_info mis on mis.merchant_no=soi.settle_user_no "
	    				+ "LEFT JOIN settle_transfer st on st.trans_id=soi.settle_order and st.settle_type = soi.settle_type");
	   			if(soi.getSettleOrder()!=null){
	   				if(StringUtils.isNotBlank(soi.getSettleOrder().toString())){
	   					WHERE(" soi.settle_order=#{soi.settleOrder}");
	   				}
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserType())){
	    			WHERE(" soi.settle_user_type=#{soi.settleUserType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getAcqEnname())){
	    			WHERE(" soi.acq_enname=#{soi.acqEnname}");
	    		}
	    		if(StringUtils.isNotBlank(soi.getMobilephone())){
	    			WHERE(" mis.mobilephone=#{soi.mobilephone}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleType())){
	    			WHERE(" soi.settle_type=#{soi.settleType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserNo())){
	    			WHERE(" soi.settle_user_no=#{soi.settleUserNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleStatus())){
	    			WHERE(" soi.settle_status=#{soi.settleStatus}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceOrderNo())){
	    			WHERE(" soi.source_order_no=#{soi.sourceOrderNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceBatchNo())){
	    			WHERE(" soi.source_batch_no=#{soi.sourceBatchNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceSystem())){
	    			WHERE(" soi.source_system=#{soi.sourceSystem}");
	    		}
	   			if(soi.getSdate()!=null){
					WHERE("soi.create_time>=#{soi.sdate}");
				}
				if(soi.getEdate()!=null){
					WHERE("soi.create_time<=#{soi.edate}");
				}
				if(soi.getSubType()!=null && soi.getSubType() == -2){
					WHERE(" soi.sub_type in ('10','11')");
				} else if(soi.getSubType()!=null && soi.getSubType()!=-1){
	    			WHERE(" soi.sub_type=#{soi.subType}");
	    		}
				if(StringUtils.isNotBlank(soi.getSettleOrderStatus())){
	    			WHERE(" soi.settle_order_status=#{soi.settleOrderStatus}");
	    		}
				if(StringUtils.isNotBlank(soi.getInAccName())){
					WHERE("st.in_acc_name=#{soi.inAccName}");
				}
	    	}}.toString();
	    }
	    
	    public String selectOutDetailAllInfo(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	SQL sql = new SQL(){{
	    		SELECT("s.settle_user_type,s.id,s.trans_id,s.order_no,"
	    				+ "s.account_serial_no,soi.settle_order,oas.service_name as out_service_name,"
	    				+ "s.amount,s.out_amount,s.fee_amount,s.create_time,s.settle_bank as acq_enname,s.settle_type,"
	    				+ "IFNULL(t.settle_status,soi.settle_status) AS settle_status,"
	    				+ "IFNULL(mis.merchant_name,ais.agent_name) AS settle_user_name,"
	    				+ "IFNULL(mis.mobilephone,ais.mobilephone) as mobile,s.`status`,"
	    				+ "s.settle_user_no,soi.source_batch_no,soi.source_system,s.out_service_id,s.deduction_fee,s.actual_fee,t.unionpay_mer_no"
						+ ",soi.sub_type,soi.agent_node,aio.agent_name agent_name_one,aio.agent_no agent_no_one,IFNULL(toe.delivery,0) AS delivery"
	    				+ " from settle_transfer s "
	    				+ "LEFT JOIN collective_trans_order t ON t.id = s.trans_id AND s.settle_type in (1,3) "
	    				+ "LEFT JOIN trans_order_ext toe ON toe.order_no=t.order_no "
	    				+ "LEFT JOIN settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type not in  (1,3) "
	    				+ "LEFT JOIN merchant_info mis on mis.merchant_no=s.settle_user_no "
	    				+ "LEFT JOIN agent_info ais on ais.agent_no=s.settle_user_no "
	    				+ "LEFT JOIN out_account_service oas on oas.id=s.out_service_id"
						+ " LEFT JOIN agent_info aio on aio.agent_no = mis.one_agent_no");
	    	}};
	    	whereSql(soi, sql);
	    	if(StringUtils.isNotBlank(soi.getMobilephone())){
	    		sql.WHERE(" (mis.mobilephone=#{soi.mobilephone} or ais.mobilephone=#{soi.mobilephone} )");
	    	}
	    	if(StringUtils.isNotBlank(soi.getInAccName())){
	    		sql.WHERE("s.in_acc_name=#{soi.inAccName}");
	    	}
	    	sql.ORDER_BY("s.create_time desc");
			//手动分页
			String resql= sql.toString() + " LIMIT "+soi.getPageFirst()+","+soi.getPageSize();
			System.out.println("出款明细查询：" + resql);
	    	return resql;
	    }

	    public String exportOutDetailAllInfo(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	SQL sql = new SQL(){{
				SELECT("s.settle_user_type,s.id,s.trans_id,s.order_no,"
						+ "s.account_serial_no,soi.settle_order,oas.service_name as out_service_name,"
						+ "s.amount,s.out_amount,s.fee_amount,s.create_time,s.settle_bank as acq_enname,s.settle_type,"
						+ "IFNULL(t.settle_status,soi.settle_status) AS settle_status,"
						+ "IFNULL(mis.merchant_name,ais.agent_name) AS settle_user_name,"
						+ "IFNULL(mis.mobilephone,ais.mobilephone) as mobile,s.`status`,"
						+ "s.settle_user_no,soi.source_batch_no,soi.source_system,s.out_service_id,s.deduction_fee,s.actual_fee,"
						+ "t.unionpay_mer_no,s.in_acc_name,s.in_acc_no,s.in_bank_name" +
						" ,soi.sub_type,soi.agent_node,aio.agent_name agent_name_one,aio.agent_no agent_no_one,IFNULL(toe.delivery,0) AS delivery "
						+ " from settle_transfer s "
						+ "LEFT JOIN collective_trans_order t ON t.id = s.trans_id AND s.settle_type in (1,3) "
						+ "LEFT JOIN trans_order_ext toe ON toe.order_no=t.order_no "
						+ "LEFT JOIN settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type not in (1,3) "
						+ "LEFT JOIN merchant_info mis on mis.merchant_no=s.settle_user_no "
						+ "LEFT JOIN agent_info ais on ais.agent_no=s.settle_user_no "
						+ "LEFT JOIN out_account_service oas on oas.id=s.out_service_id" +
						" LEFT JOIN agent_info aio ON aio.agent_no = mis.one_agent_no");
	    	}};
	    	whereSql(soi, sql);
	    	if(StringUtils.isNotBlank(soi.getMobilephone())){
	    		sql.WHERE(" (mis.mobilephone=#{soi.mobilephone} or ais.mobilephone=#{soi.mobilephone} )");
	    	}
	    	if(StringUtils.isNotBlank(soi.getInAccName())){
	    		sql.WHERE("s.in_acc_name=#{soi.inAccName}");
	    	}
	    	sql.ORDER_BY("s.create_time desc");
			String resql=sql.toString();
	    	if(soi.getPageSize()>0){
	    		resql= sql.toString() + " LIMIT "+soi.getPageFirst()+","+soi.getPageSize();
			}
			System.out.println("出款明细导出：" + resql);
	    	return resql;
	    }
	    
	    public String getOutDetailTotalMoney(Map<String,Object> param){
	    	// 下面这条sql会导致sql执行很慢，重新改了个写法。20171128,mays
	    	// sql.WHERE(" (mis.mobilephone=#{soi.mobilephone} or ais.mobilephone=#{soi.mobilephone} )");
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	String sql = "";
	    	if(StringUtils.isNotBlank(soi.getMobilephone())){
	    		SQL sql1 = new SQL();
	    		countSql(soi, sql1);
	    		sql1.LEFT_OUTER_JOIN("merchant_info mis on mis.merchant_no=s.settle_user_no");
	    		sql1.WHERE("mis.mobilephone=#{soi.mobilephone}");
		    	whereSql(soi, sql1);

		    	SQL sql2 = new SQL();
		    	countSql(soi, sql2);
	    		sql2.LEFT_OUTER_JOIN("agent_info ais on ais.agent_no=s.settle_user_no");
	    		sql2.WHERE("ais.mobilephone=#{soi.mobilephone}");
		    	whereSql(soi, sql2);
				if(soi.getQueryTotalStatus() == 1) {
					sql = "SELECT SUM(ckje) ckje,SUM(cksxf) cksxf,SUM(dksxf) dksxf,"
							+ "SUM(sjckje) sjckje,SUM(sjcksxf) sjcksxf,SUM(countNum) countNum ";
				} else {
					sql = "SELECT SUM(countNum) countNum ";
				}
		    	sql += " FROM (( "
		    			+ sql1.toString() + " ) union all ( " + sql2.toString() + " )) temp";
    		} else {
		    	SQL s = new SQL(){};
		    	countSql(soi, s);
		    	whereSql(soi, s);
		    	sql = s.toString();
    		}
	    	log.info("出款明细查询汇总sql: " + sql);
	    	return sql;
	    }

	    public void countSql(SettleOrderInfo soi, SQL sql) {
	    	if(soi.getQueryTotalStatus() == 1) {
				sql.SELECT("ifnull(sum(s.amount), 0) as ckje,ifnull(sum(s.fee_amount), 0) as cksxf,ifnull(sum(s.deduction_fee), 0) as dksxf,"
						+ "ifnull(sum(s.out_amount), 0) as sjckje,ifnull(sum(s.actual_fee), 0) as sjcksxf");
			}
			sql.SELECT("ifnull(count(*),0) as countNum");
	    	sql.FROM("settle_transfer s ");
			/*if(StringUtils.isNotBlank(soi.getSettleStatus())){
				sql.LEFT_OUTER_JOIN("collective_trans_order t ON t.id = s.trans_id AND s.settle_type = 1");
			}*/
			sql.LEFT_OUTER_JOIN("collective_trans_order t ON t.id = s.trans_id AND s.settle_type = 1");
			sql.LEFT_OUTER_JOIN("trans_order_ext toe ON toe.order_no=t.order_no");
			if(StringUtils.isNotBlank(soi.getSettleStatus()) || StringUtils.isNotBlank(soi.getSourceBatchNo())
					|| StringUtils.isNotBlank(soi.getSourceSystem())){
				sql.LEFT_OUTER_JOIN("settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type <> 1");
			}

	    }

	    public void whereSql(SettleOrderInfo soi, SQL sql) {
			if (soi == null) {
				return;
			}
			if(StringUtils.isNotBlank(soi.getId())){
	   			sql.WHERE(" s.id=#{soi.id}");
	   		}
    		if(StringUtils.isNotBlank(soi.getTransId())){
    			sql.WHERE(" s.trans_id=#{soi.transId}");
	   		}
    		if(StringUtils.isNotBlank(soi.getOutServiceId())){
    			sql.WHERE(" s.out_service_id=#{soi.outServiceId}");
	   		}
    		if(StringUtils.isNotBlank(soi.getOrderNo())){
    			sql.WHERE(" s.order_no=#{soi.orderNo}");
	   		}
    		if(StringUtils.isNotBlank(soi.getAcqEnname())){
    			sql.WHERE(" s.settle_bank=#{soi.acqEnname}");
	   		}
    		if(StringUtils.isNotBlank(soi.getAccountSerialNo())){
    			sql.WHERE(" s.account_serial_no=#{soi.accountSerialNo}");
	   		}
   			if(StringUtils.isNotBlank(soi.getSettleUserType())){
   				sql.WHERE(" s.settle_user_type=#{soi.settleUserType}");
    		}
   			if(StringUtils.isNotBlank(soi.getSettleType())){
   				sql.WHERE(" s.settle_type=#{soi.settleType}");
    		}
   			if(StringUtils.isNotBlank(soi.getSettleUserNo())){
   				sql.WHERE(" s.settle_user_no=#{soi.settleUserNo}");
    		}
   			if(StringUtils.isNotBlank(soi.getSettleStatus())){
   				sql.WHERE(" (t.settle_status=#{soi.settleStatus} or soi.settle_status=#{soi.settleStatus})");
    		}
   			if(StringUtils.isNotBlank(soi.getStatus())){
   				sql.WHERE(" s.status=#{soi.status}");
    		}
   			if(StringUtils.isNotBlank(soi.getSourceBatchNo())){
   				sql.WHERE(" soi.source_batch_no=#{soi.sourceBatchNo}");
    		}
   			if(StringUtils.isNotBlank(soi.getSourceSystem())){
   				sql.WHERE(" soi.source_system=#{soi.sourceSystem}");
    		}
   			if(soi.getSdate()!=null){
   				sql.WHERE("s.create_time>=#{soi.sdate}");
			}
			if(soi.getEdate()!=null){
				sql.WHERE("s.create_time<=#{soi.edate}");
			}
			if( soi.getSubType() != null && soi.getSubType() == -2){
				sql.WHERE(" s.sub_type in ('10','11')");
			} else if(soi.getSubType() != null && -1 != soi.getSubType()){
				sql.WHERE(" s.sub_type=#{soi.subType}");
			}
			if(StringUtils.isNotBlank(soi.getDelivery())){
				if("1".equals(soi.getDelivery())){
					sql.WHERE(" toe.delivery=1");
				}else{
					sql.WHERE(" (toe.delivery=0 OR toe.delivery IS NULL)");
				}
			}
		}
	    
	    public String importOutDetailAllInfo(Map<String,Object> param){
	    	final SettleOrderInfo soi=(SettleOrderInfo)param.get("soi");
	    	String sql = new SQL(){{
	    		SELECT("bb.*,sd.sys_name as settleStatusName");
	    		FROM(" (select s.settle_user_type,s.id,s.trans_id,s.order_no,s.account_serial_no,soi.settle_order,oas.service_name as outServiceName,"
	    				+ "s.amount,s.out_amount,s.fee_amount,s.create_time,s.settle_bank as acqEnname,"
	    				+ "(SELECT sys_name from sys_dict where sys_key='SETTLE_TYPE' and `status` =1 and sys_value=s.settle_type) as settle_name,"
	    				+ "IFNULL(t.settle_status,soi.settle_status) AS settleStatus,"
	    				+ "IFNULL(mis.merchant_name,ais.agent_name) AS settle_user_name,"
	    				+ "s.settle_type,s.`status`,"
	    				+ "IFNULL(mis.mobilephone,ais.mobilephone) as mobile,"
	    				+ "	(SELECT sys_name from sys_dict where sys_key='OUT_STATUS' and `status` =1 and sys_value=s.`status`) as statusName,"
	    				+ "s.settle_user_no,soi.source_batch_no,soi.source_system,s.out_service_id,s.deduction_fee,s.actual_fee "
	    				+ "from settle_transfer s "
	    				+ "LEFT JOIN collective_trans_order t ON t.id = s.trans_id AND s.settle_type = 1 "
	    				+ "LEFT JOIN settle_order_info soi ON s.trans_id = soi.settle_order AND s.settle_type <> 1 "
	    				+ "LEFT JOIN merchant_info mis on mis.merchant_no=s.settle_user_no "
	    				+ "LEFT JOIN agent_info ais on ais.agent_no=s.settle_user_no "
	    				+ "LEFT JOIN out_account_service oas on oas.id=s.out_service_id ) as bb "
	    				+ "LEFT JOIN sys_dict sd on sd.sys_value=bb.settleStatus and sd.`status`=1 and sys_key='SETTLE_STATUS'");
	    		if(StringUtils.isNotBlank(soi.getId())){
		   			WHERE(" bb.id=#{soi.id}");
		   		}
	    		if(StringUtils.isNotBlank(soi.getTransId())){
		   			WHERE(" bb.trans_id=#{soi.transId}");
		   		}
	    		if(StringUtils.isNotBlank(soi.getOutServiceId())){
		   			WHERE(" bb.out_service_id=#{soi.outServiceId}");
		   		}
	    		if(StringUtils.isNotBlank(soi.getOrderNo())){
		   			WHERE(" bb.order_no=#{soi.orderNo}");
		   		}
	    		if(StringUtils.isNotBlank(soi.getAcqEnname())){
		   			WHERE(" bb.acqEnname=#{soi.acqEnname}");
		   		}
	    		if(StringUtils.isNotBlank(soi.getAccountSerialNo())){
		   			WHERE(" bb.account_serial_no=#{soi.accountSerialNo}");
		   		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserType())){
	    			WHERE(" bb.settle_user_type=#{soi.settleUserType}");
	    		}
	    		if(StringUtils.isNotBlank(soi.getMobilephone())){
	    			WHERE(" bb.mobile=#{soi.mobilephone}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleType())){
	    			WHERE(" bb.settle_type=#{soi.settleType}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleUserNo())){
	    			WHERE(" bb.settle_user_no=#{soi.settleUserNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSettleStatus())){
	    			WHERE(" bb.settleStatus=#{soi.settleStatus}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getStatus())){
	    			WHERE(" bb.status=#{soi.status}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceBatchNo())){
	    			WHERE(" bb.source_batch_no=#{soi.sourceBatchNo}");
	    		}
	   			if(StringUtils.isNotBlank(soi.getSourceSystem())){
	    			WHERE(" bb.source_system=#{soi.sourceSystem}");
	    		}
	   			if(soi.getSdate()!=null){
					WHERE("bb.create_time>=#{soi.sdate}");
				}
				if(soi.getEdate()!=null){
					WHERE("bb.create_time<=#{soi.edate}");
				}
	    		ORDER_BY("bb.create_time desc");
	    	}}.toString();
	    	System.out.println("出款明细导出：");
	    	System.out.println(sql);
	    	return sql;
	    }
	    
    }

}
