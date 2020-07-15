package cn.eeepay.framework.daoHistory;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransInfoHistoryDao {
	
	Logger log = LoggerFactory.getLogger(TransInfoHistoryDao.class);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> queryAllInfo(@Param("transInfo")CollectiveTransOrder transInfo,@Param("page")Page<CollectiveTransOrder> page);
	
	@SelectProvider(type=SqlProvider.class,method="importAllInfo")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> importAllInfo(@Param("transInfo")CollectiveTransOrder transInfo);
	
	@SelectProvider(type=SqlProvider.class,method="selectMoneyInfo")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryNumAndMoney(@Param("transInfo")CollectiveTransOrder transInfo);
	
	@Select("select * from trans_info where order_no=#{orderNo}")
	@ResultType(TransInfo.class)
	TransInfo queryInfo(@Param("orderNo")String orderNo);
	
	@Select("select * from trans_info where acq_reference_no=#{acqReferenceNo}")
	@ResultType(TransInfo.class)
	TransInfo findTransInfoByAcqReferenceNo(@Param("acqReferenceNo")String acqReferenceNo);
	
	@Select("select * from scan_code_trans where trade_no=#{orderNo}")
	@ResultType(ScanCodeTrans.class)
	ScanCodeTrans queryScanInfo(@Param("orderNo")String orderNo);
	
	@Select("select profits_${agentNode} as num from collective_trans_order where order_no=#{orderNo}")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectByOrderNo(@Param("agentNode")int agentNode,@Param("orderNo")String orderNo);
	
	@Select("select * from collective_trans_order where order_no=#{orderNo}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryCtoInfo(@Param("orderNo")String orderNo);
	
	@Select("select * from collective_trans_order where order_no=#{orderNo} ")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryCtoForSettle(@Param("orderNo")String orderNo);

	@Insert(" INSERT INTO share_settle (share_settle_no, order_no, merchant_no, type, acq_enname, zq_merchant_no, amount, status,opertor) "
			+ " values(#{ssInfo.shareSettleNo},#{ssInfo.orderNo},#{ssInfo.merchantNo},#{ssInfo.type},#{ssInfo.acqEnname},#{ssInfo.zqMerchantNo},#{ssInfo.amount},#{ssInfo.status},#{ssInfo.opertor})")
	int insertShareSettleInfo(@Param("ssInfo")ShareSettleInfo ssInfo);

	@Select("SELECT cto.*,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price,sis.service_name,bpd.bp_name,"
			+ "hp.type_name,mis.one_agent_no,mis.agent_no,mis.merchant_name,mis.address, case when tis.acq_enname='ZF_ZQ' THEN mis.merchant_name ELSE am.acq_merchant_name END acq_merchant_name,"
			+ "acs.service_name as acqServiceType,tis.acq_terminal_no,tis.acq_batch_no,tis.acq_serial_no,tis.is_iccard,tis.read_card,"
			+ "tis.terminal_no,tis.batch_no,tis.serial_no,tis.currency_type,tis.settle_err_code,"
			+ "tis.acq_auth_no,case when cto.pay_method = '1' then tis.acq_reference_no else sct.acq_reference_no end acq_reference_no,tis.id as tisId "
			+ "FROM collective_trans_order cto "
			+ "LEFT JOIN trans_info tis ON tis.order_no = cto.order_no "
			+ "LEFT JOIN nposp.scan_code_trans sct ON sct.trade_no = cto.order_no "
			+ "LEFT JOIN nposp.merchant_info mis ON mis.merchant_no = cto.merchant_no "
			+ "LEFT JOIN nposp.agent_info ais ON ais.agent_no = tis.agent_no "
			+ "LEFT JOIN nposp.terminal_info ter ON ter.SN = cto.device_sn "
			+ "LEFT JOIN nposp.hardware_product hp on hp.hp_id=ter.type "
			+ "LEFT JOIN nposp.service_info sis ON sis.service_id = cto.service_id "
			+ "LEFT JOIN nposp.acq_merchant am on am.acq_merchant_no=cto.acq_merchant_no "
			+ "LEFT JOIN nposp.acq_service acs on acs.id=cto.acq_service_id "
			+ "LEFT JOIN nposp.business_product_define bpd on bpd.bp_id=cto.business_product_id "
			+ "where cto.order_no=#{id}")
	@ResultType(TransInfo.class)
	CollectiveTransOrder queryInfoDetail(@Param("id")String id);

	/**
	 * 用收单商户号去acq_terminal查询acqTerminalNo
	 * @author	mays
	 * @date	2018年2月2日
	 */
	@Select("SELECT acq_terminal_no FROM nposp.acq_terminal where acq_merchant_no=#{acqMerchantNo}")
	String queryAcqTerminalNo(@Param("acqMerchantNo") String acqMerchantNo);

	@Select("SELECT cto.* "
			+ "FROM collective_trans_order cto "
			+ "where cto.id=#{id}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryCollectiveTransOrder(@Param("id")String id);

	@Update("update collective_trans_order set freeze_status=#{tt.freezeStatus} where order_no=#{tt.orderNo}")
	int updateInfoByOrderNo(@Param("tt")CollectiveTransOrder tt);
	
	@Select("select s.*,o.service_name,IFNULL(bsu.user_name,'自动结算') as userName from settle_transfer s "
			+ "left join nposp.out_account_service o on s.out_service_id=o.id "
			+ "left join nposp.boss_shiro_user bsu on bsu.id=s.settle_creator "
			+ "where s.trans_id=#{tranId} and s.order_no=#{orderNo} ORDER BY s.create_time desc")
	@ResultType(SettleTransfer.class)
	List<SettleTransfer> selectSettleInfo(@Param("tranId")String tranId,@Param("orderNo")String orderNo);
	
	@SelectProvider(type=SqlProvider.class,method="queryAllInfoSale")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> queryAllInfoSale(@Param("transInfo")CollectiveTransOrder transInfo,@Param("page")Page<CollectiveTransOrder> page);
	
	@SelectProvider(type=SqlProvider.class,method="queryMoneySale")
	@ResultType(CollectiveTransOrder.class)
	String queryMoneySale(@Param("transInfo")CollectiveTransOrder transInfo);
	
	//账户条用
	@Select("SELECT 'CNY',ti.insurance,c.acq_enname,ti.trans_source,"
			+ "NULL trans_id,c.account_no card_no,ti.pos_type,c.acq_org_id acq_id,"
			+ "c.holidays_mark,ti.is_iccard,c.holidays_mark holidays,null acq_code,"
			+ "c.order_no trans_orderno,null id,ti.acq_serial_no,c.trans_status,'transSystem' from_system,"
			+ "null ori_serialno,ti.terminal_no,ti.batch_no,c.order_no,c.trans_time,null acq_merchantname,"
			+ "c.merchant_fee,ai.agent_no direct_agentno,ti.acq_merchant_no,ti.issued_status,ti.acq_batch_no,"
			+ "ti.serial_no,c.profits_1 agentShareAmount,c.trans_amount,c.trans_type,c.merchant_rate,ti.acq_response_code,"
			+ "ti.review_status,c.agent_node,ti.ori_acq_batch_no,ti.last_update_time,c.merchant_no,c.settle_status,"
			+ "ti.msg_id,ti.merchant_settle_date,ti.ori_batch_no,ti.acq_auth_no,ti.acq_reference_no,c.settlement_method,"
			+ "c.acq_merchant_fee,c.device_sn,ti.acq_terminal_no,c.account_no,date(c.trans_time) trans_date,ti.ori_acq_serial_no,"
			+ "c.service_id,ai.agent_no,c.card_type,c.acq_service_id,ti.acc_status,c.acq_org_id,c.acq_merchant_rate,c.id fromSerialNo,"
			+ "ai.one_level_id one_agentno,c.create_time,ti.acq_settle_date,ti.bag_settle,'000001' trans_typecode "
			+ "FROM collective_trans_order c "
			+ "LEFT JOIN trans_info ti ON c.order_no = ti.order_no "
			+ "LEFT JOIN scan_code_trans sc ON c.order_no = sc.trade_no,agent_info ai "
			+ "where c.agent_node = ai.agent_node "
			+ "and c.order_no = #{orderNo}")
	@ResultType(AccountCollectiveTransOrder.class)
	AccountCollectiveTransOrder queryInfoAccount(@Param("orderNo")String orderNo);
	
	@Select("select * from collective_trans_order where account<>1 and trans_status='SUCCESS' "
			+ "and acq_org_id is not null")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectAllRecordAccountFail();

	@Select("select * from collective_trans_order where" +
			" create_time >= #{yesDate}" +
			" and account <> 1 and trans_status='SUCCESS' " +
			" and acq_org_id is not null")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectRecordAccountFail(@Param("yesDate") String yesDate);

	@Update("update collective_trans_order set account=#{account},trans_msg=#{transMsg} where order_no=#{orderNo}")
	int updateAccount(CollectiveTransOrder info);
	
	@Update("update collective_trans_order set freeze_status=#{freezeStatus} where order_no=#{orderNo}")
	int updateFreezeStatusByOrderNo(@Param("orderNo")String orderNo,@Param("freezeStatus")String freezeStatus);

	@Select("select * from scan_code_trans where trade_state=\"SUCCESS\" and trade_no=#{orderNo}")
	@ResultType(ScanCodeTrans.class)
	ScanCodeTrans getScanCodeTransByOrder(@Param("orderNo")String orderNo);

	@Update("update collective_trans_order c set c.settle_status = 4 where c.settlement_method = '0'"
			+" and c.settle_status in (0,3) "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is null"
			+" and c.acq_enname not in (${channels})"
			+" and c.create_time < #{currentDate} and c.create_time > #{maxdate}"
//			+" and c.create_time < curdate() and c.create_time > date_sub(curdate(), INTERVAL #{maxdate} DAY)"
			+" and c.order_no not in (select active_order from activity_detail where active_order >'')")
	int updateT0SettleStatus(@Param("channels")String channels, @Param("maxdate") String maxdate, @Param("currentDate")String currentDate);
	
	@Update("update collective_trans_order c set c.settle_status = 4 where c.settlement_method = '1'"
			+" and c.settle_status  in (3) "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is not null"
			+" and c.settle_type='2'"
			+" and c.acq_enname not in (${channels})"
			+" and c.create_time < #{currentDate} and c.create_time > #{maxdate}"
//			+" and c.create_time < curdate() and c.create_time > date_sub(curdate(), INTERVAL #{maxdate} DAY)"
			+" and c.order_no not in (select active_order from activity_detail where active_order >'')")
	int updateT1SettleStatus(@Param("channels")String channels, @Param("maxdate") String maxdate, @Param("currentDate")String currentDate);
	
	@Update("update collective_trans_order c set c.settle_status = 4 where c.settlement_method = '0'"
			+" and c.settle_status in (0,3) "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is null"
			+" and c.acq_enname not in (${channels})"
			+" and c.acq_enname in (${transchannels})"
			+" and c.create_time < #{currentDate} and c.create_time > #{maxdate}"
//			+" and c.create_time < curdate() and c.create_time > date_sub(curdate(), INTERVAL #{maxdate} DAY)"
			+" and c.order_no not in (select active_order from activity_detail where active_order >'')")
	int updateT0SettleStatus2(@Param("channels")String channels, @Param("maxdate")String maxdate, @Param("transchannels")String transchannels
			, @Param("currentDate")String currentDate);
	
	@Update("update collective_trans_order c set c.settle_status = 4 where c.settlement_method = '1'"
			+" and c.settle_status  in (3) "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is not null"
			+" and c.settle_type='2'"
			+" and c.acq_enname not in (${channels})"
			+" and c.acq_enname in (${transchannels})"
			+" and c.create_time < #{currentDate} and c.create_time > #{maxdate}"
//			+" and c.create_time < curdate() and c.create_time > date_sub(curdate(), INTERVAL #{maxdate} DAY)"
			+" and c.order_no not in (select active_order from activity_detail where active_order >'')")
	int updateT1SettleStatus2(@Param("channels")String channels, @Param("maxdate")String maxdate, @Param("transchannels")String transchannels
			, @Param("currentDate")String currentDate);
	
	@Select("select 1 count from sys_calendar WHERE sys_date=#{sys_date} and status=1")
	@ResultType(Map.class)
	Map<String, Object> getHolidayFlag(@Param("sys_date")String sys_date);
	
	@Select("select s.in_acc_no from settle_transfer s where s.trans_id=#{id}")
	@ResultType(String.class)
	String findCardById(@Param("id")int id);
	/**
	 * 订单出款状态置为已返代理商
	 * @author tans
	 * @date 2017年7月3日 下午10:20:49
	 * @return
	 */
	@Update("update collective_trans_order set settle_status=#{settleStatus} where order_no=#{orderNo}")
	int updateReturnAgent(@Param("orderNo")String orderNo, @Param("settleStatus")Integer settleStatus);

	@Select("SELECT SUM(trans_amount) AS amountSum,COUNT(1) AS countNum FROM collective_trans_order WHERE merchant_no=#{merchantNo} AND trans_status = 'SUCCESS' AND trans_time BETWEEN #{startTime} AND  #{endTime}")
	@ResultType(Map.class)
	Map<String, Object> getAmountAndNum(@Param("merchantNo")String merchant_no,@Param("startTime")String startTime,@Param("endTime")String endTime);

	/**
	 * 查询 当天交易状态为"已提交"的订单,时间往前推三分钟
	 * @author	mays
	 * @date	2017年12月15日
	 */
	@Select("select order_no from collective_trans_order where trans_status='SENDORDER' and trans_time >= CURDATE() and trans_time <= date_add(now(), interval -3 minute)")
	List<String> syncTransStatus();

	/**
	 * 查询符合范围的需要出款T0流水
	 * @param settletype
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@Select("select cto.id from collective_trans_order cto "
			+ "where cto.trans_status='SUCCESS' and cto.trans_type='PURCHASE' "
			+ "and cto.account='1' and cto.freeze_status='0' "
			+ "and cto.settlement_method='0' and (cto.settle_status='0' or cto.settle_status is null "
			+ "or (cto.settle_status='3' && cto.order_no not in (select st.order_no from settle_transfer st where st.order_no=cto.order_no))) "
			+ "and cto.acq_enname = #{settletype} "
			+ "and cto.create_time >= #{starttime} "
			+ "and cto.create_time <= #{endtime} ")
	@ResultType(Map.class)
	List<Map<String, Object>> querySFTSettle(@Param("settletype")String settletype,@Param("starttime")String starttime,@Param("endtime")String endtime);

	@Select("select * from share_settle where order_no = #{orderNo}")
	ShareSettleInfo shareSettleInfo(@Param("orderNo") String orderNo);

	@SelectProvider(type=TransInfoHistoryDao.SqlProvider.class,method="getCtoBySurvey")
	@ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder getCtoBySurvey(@Param("orderNo")String orderNo);

    class SqlProvider{
		public String getCtoBySurvey(Map<String,Object> param){
			StringBuffer sb=new StringBuffer();
			sb.append("select  ");
			sb.append("	 cto.*,tra.acq_reference_no,hp.type_name, ");
			sb.append("	 bpd.bp_name,sis.service_name, ");
			sb.append("  mis.parent_node merAgentNode,mis.agent_no,mis.one_agent_no,mis.recommended_source, ");
			sb.append("	 case when tra.acq_enname='ZF_ZQ' THEN mis.merchant_name ELSE am.acq_merchant_name END acq_merchant_name, ");
			sb.append("	 (select s.out_amount FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) outAmount ");
			sb.append("  from collective_trans_order cto ");
			sb.append("		LEFT JOIN trans_info tra ON tra.order_no=cto.order_no ");
			sb.append("		LEFT JOIN nposp.business_product_define bpd on bpd.bp_id=cto.business_product_id ");
			sb.append("		LEFT JOIN nposp.service_info sis ON sis.service_id = cto.service_id ");
			sb.append("	    LEFT JOIN nposp.merchant_info mis ON mis.merchant_no = cto.merchant_no ");
			sb.append("	    LEFT JOIN nposp.acq_merchant am on am.acq_merchant_no=cto.acq_merchant_no ");
			sb.append("	    LEFT JOIN nposp.terminal_info ter ON ter.SN = cto.device_sn ");
			sb.append("     LEFT JOIN nposp.hardware_product hp on hp.hp_id=ter.type ");
			sb.append("  where 1=1 ");
			sb.append(" 	and cto.order_no=#{orderNo}");
			return sb.toString();
		}

	    	public String selectAllInfo(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
	    			StringBuilder sb = new StringBuilder();
	    			sb.append(" select cto.id, cto.order_no, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price, ");
					sb.append(" cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account, ");
	    			sb.append(" cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,si.service_type, ");
	    			
					sb.append(" cto.n_prm,cto.zx_rate,");
					sb.append(" (select mi.merchant_name from nposp.merchant_info mi where mi.merchant_no=cto.merchant_no) merchant_name,");
					
					sb.append(" (select concat(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount");
	    			fromSql(transInfo,sb);
	    			whereSql(transInfo, sb);
	    			sb.append(" order by cto.create_time desc ");
					log.info("交易查询sql：" + sb.toString());
					return sb.toString();
	    	}

		  public String importAllInfo(Map<String,Object> param){
			  final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
			  StringBuilder sb = new StringBuilder();
			  sb.append(" select cto.id, cto.order_no, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price, ");
			  sb.append(" cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account, cto.quick_rate,cto.quick_fee, ");
			  sb.append(" cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.acq_enname,cto.business_product_id,cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,cto.settle_msg, ");
			  sb.append(" cto.n_prm,cto.zx_rate,");
			  sb.append(" (select mi.merchant_name from nposp.merchant_info mi where mi.merchant_no=cto.merchant_no) merchant_name,");
			  sb.append(" (select ti.acq_reference_no from trans_info ti where ti.order_no=cto.order_no) acq_reference_no,");
			  sb.append(" (select concat(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount,");
			  sb.append(" cto.res_msg,ti.read_card,ti.is_iccard,si.service_type ");
			  sb.append(" from collective_trans_order cto ");
			  sb.append(" JOIN nposp.service_info si on si.service_id = cto.service_id ");
			  sb.append(" LEFT JOIN trans_info ti ON ti.order_no=cto.order_no ");
			  //增加过滤，当条件没有的时候不联表查询
			  if(StringUtils.isNotBlank(transInfo.getTerType())||StringUtils.isNotBlank(transInfo.getActivityType())){
				  sb.append(" LEFT JOIN nposp.terminal_info ter on ter.SN = cto.device_sn ");
			  }
			  if(StringUtils.isNotBlank(transInfo.getGroupCode())){
				  sb.append(" LEFT JOIN nposp.trans_route_group_acq_merchant route ON route.acq_merchant_no=cto.acq_merchant_no ");
			  }
			  whereSql(transInfo, sb);
			  sb.append(" order by cto.create_time desc ");
			  log.info("交易查询导出的sql：" + sb);
			  return sb.toString();
		  }


			public void fromSql(final CollectiveTransOrder transInfo,StringBuilder sb) {
				sb.append(" from collective_trans_order cto ");
				sb.append(" JOIN nposp.service_info si on si.service_id = cto.service_id ");
				//增加过滤，当条件没有的时候不联表查询
				if(StringUtils.isNotBlank(transInfo.getTerType())||StringUtils.isNotBlank(transInfo.getActivityType())){
					sb.append(" LEFT JOIN nposp.terminal_info ter on ter.SN = cto.device_sn ");
				}
				if(StringUtils.isNotBlank(transInfo.getAcqMerchantNo()) || StringUtils.isNotBlank(transInfo.getAcqSerialNo())){
					sb.append(" LEFT JOIN trans_info ti ON ti.order_no=cto.order_no ");
				}
				if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" LEFT JOIN nposp.trans_route_group_acq_merchant route ON route.acq_merchant_no=cto.acq_merchant_no ");
				}
			}

			public void whereSql(final CollectiveTransOrder transInfo, StringBuilder sb) {
				sb.append(" where 1=1 ");
				if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
					sb.append(" and cto.merchant_no=#{transInfo.merchantNo}");
				}
				if(transInfo.getId()!=null){
					sb.append(" and cto.id=#{transInfo.id}");
				}
				if(StringUtils.isNotBlank(transInfo.getAccount())){
					sb.append(" and cto.account=#{transInfo.account}");
				}
				if(StringUtils.isNotBlank(transInfo.getAccountSerialNo())){
					sb.append(" and cto.order_no in (select sts.order_no from settle_transfer sts where sts.account_serial_no=#{transInfo.accountSerialNo})");
				}
				if(StringUtils.isNotBlank(transInfo.getAgentNo())){
					if(StringUtils.isNotBlank(transInfo.getBool())){
						if(transInfo.getBool().equals("1")){
							sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM nposp.agent_info a where a.agent_no=#{transInfo.agentNo})");
						}else{
							sb.append(" and cto.agent_node = (SELECT agent_node FROM nposp.agent_info a where a.agent_no=#{transInfo.agentNo})");
						}
					}else{
						sb.append(" and cto.agent_node = (SELECT agent_node FROM nposp.agent_info a where a.agent_no=#{transInfo.agentNo})");
					}
				}
				
				//2.2.5 增加“出款类型”、“出款订单ID”查询条件 开始
				if(StringUtils.isNotBlank(transInfo.getSettleType())){
					sb.append(" and cto.settle_type=#{transInfo.settleType}");
				}
				if(StringUtils.isNotBlank(transInfo.getSettleOrder())){
					sb.append(" and cto.settle_order=#{transInfo.settleOrder}");
				}
				//2.2.5 增加“出款类型”、“出款订单ID”查询条件 结束
				
				if(StringUtils.isNotBlank(transInfo.getOrderNo())){
					sb.append(" and cto.order_no=#{transInfo.orderNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getTransStatus())){
					sb.append(" and cto.trans_status=#{transInfo.transStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getSettleStatus())){
					sb.append(" and cto.settle_status=#{transInfo.settleStatus}");
				}
				if(StringUtils.isNotBlank(transInfo.getFreezeStatus())){
					sb.append(" and cto.freeze_status=#{transInfo.freezeStatus}");
				}
				if(transInfo.getAcqOrgId()!=null && transInfo.getAcqOrgId()!=-1){
					sb.append(" and cto.acq_org_id=#{transInfo.acqOrgId}");
				}
				if(StringUtils.isNotBlank(transInfo.getMobilephone())){
					sb.append(" and cto.mobile_no=#{transInfo.mobilephone}");
				}
				if(StringUtils.isNotBlank(transInfo.getTerType())){
					sb.append(" and ter.type=#{transInfo.terType}");
				}
				if(StringUtils.isNotBlank(transInfo.getPayMethod())){
					sb.append(" and cto.pay_method=#{transInfo.payMethod}");
				}

				if(StringUtils.isNotBlank(transInfo.getAcqMerchantNo())){
					sb.append(" and cto.acq_merchant_no=#{transInfo.acqMerchantNo} ");
				}

				if(StringUtils.isNotBlank(transInfo.getAcqSerialNo())){
					sb.append(" and ti.acq_serial_no=#{transInfo.acqSerialNo} ");
				}

				if(StringUtils.isNotBlank(transInfo.getAcqReferenceNo())){
					sb.append(" and cto.order_no in (select ti.order_no from trans_info ti where ti.acq_reference_no = #{transInfo.acqReferenceNo})");
				}
				if(transInfo.getBusinessProductId()!=null && transInfo.getBusinessProductId()!=-1){
					sb.append(" and cto.business_product_id=#{transInfo.businessProductId}");
				}

				if(StringUtils.isNotBlank(transInfo.getAccountNo())){
					sb.append(" and cto.account_no=#{transInfo.accountNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getCardType())){
					sb.append(" and cto.card_type=#{transInfo.cardType}");
				}
				if(StringUtils.isNotBlank(transInfo.getSettlementMethod())){
					sb.append(" and cto.settlement_method=#{transInfo.settlementMethod}");
				}
				if(StringUtils.isNotBlank(transInfo.getSmoney())){
					sb.append(" and cto.trans_amount>=#{transInfo.smoney}");
				}
				if(StringUtils.isNotBlank(transInfo.getEmoney())){
					sb.append(" and cto.trans_amount<=#{transInfo.emoney}");
				}
				if(transInfo.getSdate()!=null){
					sb.append(" and cto.create_time>=#{transInfo.sdate}");
				}
				if(transInfo.getEdate()!=null){
					sb.append(" and cto.create_time<=#{transInfo.edate}");
				}
				if(transInfo.getTransTimeStart()!=null){
					sb.append(" and cto.trans_time>=#{transInfo.transTimeStart}");
				}
				if(transInfo.getTransTimeEnd()!=null){
					sb.append(" and cto.trans_time<=#{transInfo.transTimeEnd}");
				}
				//添加活动类型查询20170216tgh
				if(StringUtils.isNotBlank(transInfo.getActivityType())){
					sb.append(" and FIND_IN_SET(#{transInfo.activityType},ter.activity_type)");
				}
				if(transInfo.getOrderType()!=null){
					sb.append(" and cto.order_type=#{transInfo.orderType}");
				}
				if(StringUtils.isNotBlank(transInfo.getServiceType())){
					sb.append(" and si.service_type=#{transInfo.serviceType}");
				}
				if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" and route.group_code=#{transInfo.groupCode}");
				}
				
				if(transInfo.getZxStatus() != null){
					if(transInfo.getZxStatus() == 1){
						sb.append(" and cto.zx_rate > 0");
					} else{
						sb.append(" and cto.zx_rate = 0");
					}

				}
			}

	    	public String selectMoneyInfo(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
				StringBuilder sb = new StringBuilder();
				sb.append("select sum(cto.trans_amount) as totalMoney, sum(cto.merchant_fee) totalMerchantFee, sum(cto.deduction_fee) totalDeductionFee ");
				sb.append(", sum(st.amount) as totalAmount, sum(st.fee_amount) as totalFeeAmount, sum(st.out_amount) as totalOutAmount");
				sb.append(", sum(cto.merchant_price) as totalMerchantPrice, sum(cto.deduction_mer_fee) as totalDeductionMerFee");
				sb.append(", sum(cto.n_prm) as totalNPrm");
				fromSql(transInfo,sb);
				sb.append(" left join settle_transfer st on st.order_no = cto.order_no and st.STATUS = '4' AND st.correction = '0'");
				whereSql(transInfo, sb);
				log.info("交易查询统计总金额sql：" + sb.toString());
				return sb.toString();
	    	}
	 
	    	public String queryAllInfoSale(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
	    		return new SQL(){{
	    			SELECT("cto.business_product_id,cto.account,cto.acq_org_id,cto.id,cto.settlement_method,cto.merchant_no,cto.pay_method,cto.card_type,cto.account_no,"
	    					+ "cto.trans_amount,cto.trans_status,cto.freeze_status,cto.settle_status,cto.trans_time,cto.create_time,"
	    					+ "ais.agent_node,cto.order_no,ter.type,mis.merchant_name,ais.agent_no,cto.mobile_no,tis.acq_merchant_no,"
	    					+ "tis.acq_reference_no,sis.service_type");
					FROM (" collective_trans_order cto ");
					LEFT_OUTER_JOIN(" trans_info tis on tis.order_no=cto.order_no ");
					LEFT_OUTER_JOIN(" merchant_info mis on mis.merchant_no=cto.merchant_no ");
					LEFT_OUTER_JOIN(" agent_info ais on ais.agent_no=tis.agent_no ");
					LEFT_OUTER_JOIN(" terminal_info ter on ter.SN = cto.device_sn ");
					LEFT_OUTER_JOIN(" service_info sis on sis.service_id=cto.service_id ");
					LEFT_OUTER_JOIN(" agent_info info1 on ais.one_level_id = info1.agent_no ");
					
	    			if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
	    				WHERE(" (mis.merchant_no=#{transInfo.merchantNo} or mis.merchant_name=#{transInfo.merchantNo})");
	    			}
	    			if(transInfo.getId()!=null){
	    				WHERE(" cto.id=#{transInfo.id}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccount())){
	    				WHERE(" cto.account=#{transInfo.account}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccountSerialNo())){
	    				WHERE(" exists(select 1 from settle_transfer sts where sts.order_no=cto.order_no and sts.account_serial_no=#{transInfo.accountSerialNo} )");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAgentNo())){
	    				if(StringUtils.isNotBlank(transInfo.getBool())){
		    				if(transInfo.getBool().equals("1")){
		    					WHERE(" ais.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
		    				}else{
		    					WHERE(" ais.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
		    				}
		    			}else{
		    				WHERE(" ais.agent_no=#{transInfo.agentNo}");
		    			}
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getOrderNo())){
	    				WHERE(" cto.order_no=#{transInfo.orderNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getTransStatus())){
	    				WHERE(" cto.trans_status=#{transInfo.transStatus}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSettleStatus())){
	    				WHERE(" cto.settle_status=#{transInfo.settleStatus}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getFreezeStatus())){
	    				WHERE(" cto.freeze_status=#{transInfo.freezeStatus}");
	    			}
	    			if(transInfo.getAcqOrgId()!=null && transInfo.getAcqOrgId()!=-1){
	    				WHERE(" cto.acq_org_id=#{transInfo.acqOrgId}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getMobilephone())){
	    				WHERE(" cto.mobile_no=#{transInfo.mobilephone}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getTerType())){
	    				WHERE(" ter.type=#{transInfo.terType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getPayMethod())){
	    				WHERE(" cto.pay_method=#{transInfo.payMethod}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAcqMerchantNo())){
	    				WHERE(" cto.acq_merchant_no=#{transInfo.acqMerchantNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAcqReferenceNo())){
	    				WHERE(" tis.acq_reference_no=#{transInfo.acqReferenceNo}");
	    			}
	    			if(transInfo.getBusinessProductId()!=null && transInfo.getBusinessProductId()!=-1){
	    				WHERE(" cto.business_product_id=#{transInfo.businessProductId}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getServiceType())){
	    				WHERE(" sis.service_type=#{transInfo.serviceType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccountNo())){
	    				WHERE(" cto.account_no=#{transInfo.accountNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getCardType())){
	    				WHERE(" cto.card_type=#{transInfo.cardType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSettlementMethod())){
	    				WHERE(" cto.settlement_method=#{transInfo.settlementMethod}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSmoney())){
						WHERE(" cto.trans_amount>=#{transInfo.smoney}");
					}
					if(StringUtils.isNotBlank(transInfo.getEmoney())){
						WHERE(" cto.trans_amount<=#{transInfo.emoney}");
					}
					if(transInfo.getSdate()!=null){
						WHERE(" cto.create_time>=#{transInfo.sdate}");
					}
					if(transInfo.getEdate()!=null){
						WHERE(" cto.create_time<=#{transInfo.edate}");
					}
					if(transInfo.getTransTimeStart()!=null){
						WHERE(" cto.trans_time>=#{transInfo.transTimeStart}");
					}
					if(transInfo.getTransTimeEnd()!=null){
						WHERE(" cto.trans_time<=#{transInfo.transTimeEnd}");
					}
					if(transInfo.getSaleName()!=null){
						WHERE("info1.sale_name=#{transInfo.saleName} and info1.agent_level = '1' ");
					}
					ORDER_BY(" cto.create_time desc");
	    		}}.toString();
	    	}
	    	
	    	public String queryMoneySale(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
	    	    return new SQL(){{
	    	    	SELECT("SUM(cto.trans_amount) AS totalMone");
					FROM (" collective_trans_order cto ");
					LEFT_OUTER_JOIN(" trans_info tis on tis.order_no=cto.order_no ");
					LEFT_OUTER_JOIN(" merchant_info mis on mis.merchant_no=cto.merchant_no ");
					LEFT_OUTER_JOIN(" agent_info ais on ais.agent_no=tis.agent_no ");
					LEFT_OUTER_JOIN(" terminal_info ter on ter.SN = cto.device_sn ");
					LEFT_OUTER_JOIN(" service_info sis on sis.service_id=cto.service_id ");
					LEFT_OUTER_JOIN(" agent_info info1 on ais.one_level_id = info1.agent_no ");
					
	    			if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
	    				WHERE(" (mis.merchant_no=#{transInfo.merchantNo} or mis.merchant_name=#{transInfo.merchantNo})");
	    			}
	    			if(transInfo.getId()!=null){
	    				WHERE(" cto.id=#{transInfo.id}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccount())){
	    				WHERE(" cto.account=#{transInfo.account}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccountSerialNo())){
	    				WHERE(" exists(select 1 from settle_transfer sts where sts.order_no=cto.order_no and sts.account_serial_no=#{transInfo.accountSerialNo} )");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAgentNo())){
	    				if(StringUtils.isNotBlank(transInfo.getBool())){
		    				if(transInfo.getBool().equals("1")){
		    					WHERE(" ais.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
		    				}else{
		    					WHERE(" ais.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
		    				}
		    			}else{
		    				WHERE(" ais.agent_no=#{transInfo.agentNo}");
		    			}
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getOrderNo())){
	    				WHERE(" cto.order_no=#{transInfo.orderNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getTransStatus())){
	    				WHERE(" cto.trans_status=#{transInfo.transStatus}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSettleStatus())){
	    				WHERE(" cto.settle_status=#{transInfo.settleStatus}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getFreezeStatus())){
	    				WHERE(" cto.freeze_status=#{transInfo.freezeStatus}");
	    			}
	    			if(transInfo.getAcqOrgId()!=null && transInfo.getAcqOrgId()!=-1){
	    				WHERE(" cto.acq_org_id=#{transInfo.acqOrgId}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getMobilephone())){
	    				WHERE(" cto.mobile_no=#{transInfo.mobilephone}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getTerType())){
	    				WHERE(" ter.type=#{transInfo.terType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getPayMethod())){
	    				WHERE(" cto.pay_method=#{transInfo.payMethod}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAcqMerchantNo())){
	    				WHERE(" cto.acq_merchant_no=#{transInfo.acqMerchantNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAcqReferenceNo())){
	    				WHERE(" tis.acq_reference_no=#{transInfo.acqReferenceNo}");
	    			}
	    			if(transInfo.getBusinessProductId()!=null && transInfo.getBusinessProductId()!=-1){
	    				WHERE(" cto.business_product_id=#{transInfo.businessProductId}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getServiceType())){
	    				WHERE(" sis.service_type=#{transInfo.serviceType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getAccountNo())){
	    				WHERE(" cto.account_no=#{transInfo.accountNo}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getCardType())){
	    				WHERE(" cto.card_type=#{transInfo.cardType}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSettlementMethod())){
	    				WHERE(" cto.settlement_method=#{transInfo.settlementMethod}");
	    			}
	    			if(StringUtils.isNotBlank(transInfo.getSmoney())){
						WHERE(" cto.trans_amount>=#{transInfo.smoney}");
					}
					if(StringUtils.isNotBlank(transInfo.getEmoney())){
						WHERE(" cto.trans_amount<=#{transInfo.emoney}");
					}
	    			if(transInfo.getSdate()!=null){
						WHERE(" cto.create_time>=#{transInfo.sdate}");
					}
					if(transInfo.getEdate()!=null){
						WHERE(" cto.create_time<=#{transInfo.edate}");
					}
					if(transInfo.getSaleName()!=null){
						WHERE("info1.sale_name=#{transInfo.saleName} and info1.agent_level = '1' ");
					}
	    	    }}.toString();
	    	}
	    	
	    	
	    	public String selectShareSettleInfo(Map<String,Object> param){
	    		final ShareSettleInfo ssInfo =(ShareSettleInfo)param.get("ssInfo");
	    		return new SQL(){{
	    			SELECT(" ss.id,ss.share_settle_no,ss.order_no,ss.merchant_no,ss.type,ss.acq_enname,ss.zq_merchant_no,ss.amount,ss.status,ss.start_time,ss.end_time,ss.err_msg,cto.order_type,ss.opertor , " +
							" cto.n_prm, "+
							" CASE  when ss.type = '2' then st.create_time when ss.type = '1' then cto.create_time else '' end origin_time  ");
					FROM (" share_settle ss ");
					LEFT_OUTER_JOIN(" collective_trans_order cto on ss.order_no=cto.order_no ");
					LEFT_OUTER_JOIN("  settle_transfer st on ss.order_no = st.account_serial_no ");
					if(!StringUtils.isBlank(ssInfo.getMerchantNo())){
						WHERE(" ss.merchant_no = #{ssInfo.merchantNo}");
					}
					if(!StringUtils.isBlank(ssInfo.getShareSettleNo())){
						WHERE(" ss.share_settle_no = #{ssInfo.shareSettleNo}");
					}
					if(!StringUtils.isBlank(ssInfo.getOrderNo())){
						WHERE(" ss.order_no = #{ssInfo.orderNo}");
					}
					if(!StringUtils.isBlank(ssInfo.getType())){
						WHERE(" ss.type = #{ssInfo.type}");
					}
					if(ssInfo.getAmountMin()!=null){
						WHERE(" ss.amount >= #{ssInfo.amountMin}");
					}
					if(ssInfo.getAmountMax()!=null){
						WHERE(" ss.amount <= #{ssInfo.amountMax}");
					}
					if(!StringUtils.isBlank(ssInfo.getZqMerchantNo())){
						WHERE(" ss.zq_merchant_no = #{ssInfo.zqMerchantNo}");
					}
					if(!StringUtils.isBlank(ssInfo.getAcqEnname())){
						WHERE(" ss.acq_enname = #{ssInfo.acqEnname}");
					}
					
					if(!StringUtils.isBlank(ssInfo.getOrderType())){
						WHERE(" cto.order_type = #{ssInfo.orderType}");
					}
					
					if(!StringUtils.isBlank(ssInfo.getStatus())){
						WHERE(" ss.status = #{ssInfo.status}");
					}

					if(ssInfo.getStartTimeMin()!=null){
						WHERE(" ss.start_time >= #{ssInfo.startTimeMin}");
					}
					if(ssInfo.getStartTimeMax()!=null){
						WHERE(" ss.start_time <= #{ssInfo.startTimeMax}");
					}

					if(ssInfo.getOriginTimeMin()!=null){
						WHERE(" (cto.create_time >= #{ssInfo.originTimeMin} or st.create_time >=  #{ssInfo.originTimeMin}) ");
					}
					if(ssInfo.getOriginTimeMax()!=null){
						WHERE(" (cto.create_time <= #{ssInfo.originTimeMax} or st.create_time <=  #{ssInfo.originTimeMax})");
					}

					if(ssInfo.getEndTimeMin()!=null){
						WHERE(" ss.end_time >= #{ssInfo.endTimeMin}");
					}
					if(ssInfo.getEndTimeMax()!=null){
						WHERE(" ss.end_time <= #{ssInfo.endTimeMax}");
					}
					
					ORDER_BY(" ss.start_time desc");
	    		}}.toString();
	    	}
	  }
	@SelectProvider(type=SqlProvider.class,method="selectShareSettleInfo")
	@ResultType(ShareSettleInfo.class)
	List<ShareSettleInfo> queryShareSettleInfo(@Param("ssInfo")ShareSettleInfo ssInfo, @Param("page")Page<ShareSettleInfo> page);
	
	@SelectProvider(type=SqlProvider.class,method="selectShareSettleInfo")
	@ResultType(ShareSettleInfo.class)
	List<ShareSettleInfo> exportShareSettleInfo(@Param("ssInfo")ShareSettleInfo ssInfo);

	@Select("SELECT * FROM collective_trans_order WHERE trans_status='SUCCESS' AND account=1 AND freeze_status=0 AND " +
			"(settle_status = 0 OR settle_status=3) AND acq_enname in (${channelName}) AND create_time BETWEEN #{startTime} AND #{endTime}")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> getUnSettele(@Param("channelName")String channelNames, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	
	
	@Update("UPDATE collective_trans_order c,"+
	" trans_info t"+
	" SET c.trans_status = t.trans_status,"+
	" c.trans_type = t.trans_type,"+
	" c.trans_time = t.trans_time,"+
	" c.service_id = t.service_id,"+
	" c.holidays_mark = t.holidays_mark,"+
	" c.card_type = t.card_type,"+
	" c.account_no = t.account_no,"+
	" c.merchant_fee = t.merchant_fee,"+
	" c.merchant_Rate = t.merchant_Rate,"+
	" c.acq_service_id = t.acq_service_id,"+
	" c.acq_merchant_no = t.acq_merchant_no,"+
	" c.acq_merchant_fee = t.acq_merchant_fee,"+
	" c.acq_merchant_rate = t.acq_merchant_rate,"+
	" c.settlement_method = t.settlement_method,"+
	" c.acq_enname = t.acq_enname,"+
	" c.device_sn = t.device_sn,"+
	" c.settle_status = t.settle_status,"+
	" c.trans_msg = t.trans_msg,"+
	" c.unionpay_mer_no = t.acq_merchant_no,c.pay_method = '1', "+
	" c.acq_org_id = case when t.acq_enname ='ZF_ZQ' THEN 7 when t.acq_enname ='YS_ZQ' THEN 9 ELSE 0 END ,"+
	" c.res_msg = 0"+
	" WHERE"+
	" c.order_no = t.order_no"+
	" AND t.order_no = #{cto.orderNo}")
	int syncOrder(@Param("cto") CollectiveTransOrder cto);
}
