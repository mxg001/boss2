package cn.eeepay.framework.dao;

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

public interface TransInfoDao {
	
	Logger log = LoggerFactory.getLogger(TransInfoDao.class);
	
	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> queryAllInfo(@Param("transInfo")CollectiveTransOrder transInfo);
	
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

	@SelectProvider(type=SqlProvider.class,method="selectMoneyInfoByOrderNos")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryNumAndMoneyByOrderNos(@Param("orderNos") String[] orderNos);

	@SelectProvider(type=SqlProvider.class,method="selectByOrderNos")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectByOrderNos(@Param("orderNos") String[] orderNos, Page<CollectiveTransOrder> page);

	@Insert(" INSERT INTO share_settle (share_settle_no, order_no, merchant_no, type, acq_enname, zq_merchant_no, amount, status,opertor,terminal_no) "
			+ " values(#{ssInfo.shareSettleNo},#{ssInfo.orderNo},#{ssInfo.merchantNo},#{ssInfo.type},#{ssInfo.acqEnname},#{ssInfo.zqMerchantNo},#{ssInfo.amount},#{ssInfo.status},#{ssInfo.opertor},#{ssInfo.terminalNo})")
	int insertShareSettleInfo(@Param("ssInfo")ShareSettleInfo ssInfo);

	@Select("SELECT cto.*,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price,sis.service_name,bpd.bp_name,"
			+ "hp.type_name,mis.one_agent_no,mis.agent_no,mis.merchant_name,mis.address, case when tis.acq_enname='ZF_ZQ' THEN mis.merchant_name ELSE am.acq_merchant_name END acq_merchant_name,"
			+ "mis.merchant_type,"
			+ "toe.group_code,toe.trans_type transTypeExt,"
			+ "acs.service_name as acqServiceType,tis.acq_terminal_no,tis.acq_batch_no,tis.acq_serial_no,tis.is_iccard,tis.read_card,"
			+ "tis.terminal_no,tis.batch_no,tis.serial_no,tis.currency_type,tis.settle_err_code,mis.team_id,"
			+ "ai.sale_name,mis.mobilephone,mis.address,"
			+ "tis.acq_auth_no,case when cto.pay_method = '1' then tis.acq_reference_no else sct.acq_reference_no end acq_reference_no,tis.id as tisId,"
			+ "vo.vas_rate "
			+ "FROM collective_trans_order cto "
			+ "LEFT JOIN trans_info tis ON tis.order_no = cto.order_no "
			+ "LEFT JOIN scan_code_trans sct ON sct.trade_no = cto.order_no "
			+ "LEFT JOIN merchant_info mis ON mis.merchant_no = cto.merchant_no "
			+ "LEFT JOIN agent_info ai ON (ai.agent_no = mis.one_agent_no AND mis.one_agent_no IS NOT NULL) "
			+ "LEFT JOIN agent_info ais ON ais.agent_no = tis.agent_no "
			+ "LEFT JOIN terminal_info ter ON ter.SN = cto.device_sn "
			+ "LEFT JOIN hardware_product hp on hp.hp_id=ter.type "
			+ "LEFT JOIN service_info sis ON sis.service_id = cto.service_id "
			+ "LEFT JOIN acq_merchant am on am.acq_merchant_no=cto.acq_merchant_no "
			+ "LEFT JOIN acq_service acs on acs.id=cto.acq_service_id "
			+ "LEFT JOIN business_product_define bpd on bpd.bp_id=cto.business_product_id "
			+ "LEFT JOIN trans_order_ext toe on toe.order_no=cto.order_no "
			+ "LEFT JOIN vas_order vo on vo.order_no=cto.order_no "
			+ "where cto.order_no=#{id}")
	@ResultType(TransInfo.class)
	CollectiveTransOrder queryInfoDetail(@Param("id")String id);

	/**
	 * 用收单商户号去acq_terminal查询acqTerminalNo
	 * @author	mays
	 * @date	2018年2月2日
	 */
	@Select("SELECT acq_terminal_no FROM acq_terminal where acq_merchant_no=#{acqMerchantNo}")
	String queryAcqTerminalNo(@Param("acqMerchantNo") String acqMerchantNo);

	@Select("SELECT cto.* "
			+ "FROM collective_trans_order cto "
			+ "where cto.id=#{id}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryCollectiveTransOrder(@Param("id")String id);

	@Update("update collective_trans_order set freeze_status=#{tt.freezeStatus} where order_no=#{tt.orderNo}")
	int updateInfoByOrderNo(@Param("tt")CollectiveTransOrder tt);
	
	@Select("select s.*,o.service_name,IFNULL(bsu.user_name,'自动结算') as userName from settle_transfer s "
			+ "left join out_account_service o on s.out_service_id=o.id "
			+ "left join boss_shiro_user bsu on bsu.id=s.settle_creator "
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
			" and acq_org_id is not null order by create_time,id limit 2000")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectRecordAccountFail(@Param("yesDate") String yesDate);

	@Update("update collective_trans_order set account=#{account},trans_msg=#{transMsg} where order_no=#{orderNo}")
	int updateAccount(CollectiveTransOrder info);
	
	@Update("update collective_trans_order set freeze_status=#{freezeStatus} where order_no=#{orderNo}")
	int updateFreezeStatusByOrderNo(@Param("orderNo")String orderNo,@Param("freezeStatus")String freezeStatus);

	@Select("select * from scan_code_trans where trade_state=\"SUCCESS\" and trade_no=#{orderNo}")
	@ResultType(ScanCodeTrans.class)
	ScanCodeTrans getScanCodeTransByOrder(@Param("orderNo")String orderNo);

	@Select("select order_no from collective_trans_order c where " +
			" c.create_time < #{currentDate} and c.create_time > #{maxdate}" +
			" and c.settlement_method = '0'" +
			" and c.settle_status in (0,3) " +
			" and c.trans_status = 'SUCCESS'" +
			" and c.settle_order is null" +
			" and c.acq_enname not in (${channels})" +
			" and not EXISTS" +
			" (select 1 from activity_detail a where a.active_order = c.order_no);")
	@ResultType(String.class)
	List<String> selectT0SettleOrderList(@Param("channels")String channels, @Param("maxdate") String maxdate, @Param("currentDate")String currentDate);

	@Select("select order_no from collective_trans_order c where " +
			" c.create_time < #{currentDate} and c.create_time > #{maxdate}" +
			" and c.settle_status  in (3) " +
			" and c.trans_status = 'SUCCESS'" +
			" and c.settle_order is not null" +
			" and c.settle_type='2'" +
			" and c.acq_enname not in (${channels})" +
			" and not EXISTS" +
			" (select 1 from activity_detail a where a.active_order = c.order_no);")
	@ResultType(String.class)
	List<String> selectT1SettleOrderList(@Param("channels")String channels, @Param("maxdate") String maxdate, @Param("currentDate")String currentDate);

	@Select("select order_no from collective_trans_order c where " +
			" c.create_time < #{currentDate} and c.create_time > #{maxdate}"
			+" and c.settlement_method = '0'"
			+" and c.settle_status in (0,3) "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is null"
			+" and c.acq_enname not in (${channels})"
			+" and c.acq_enname in (${transchannels})"
			+" and not EXISTS"
			+" (select 1 from activity_detail a where a.active_order = c.order_no);")
	@ResultType(String.class)
	List<String> selectT0HolidaySettleOrderList(@Param("channels")String channels, @Param("maxdate")String maxdate, @Param("transchannels")String transchannels
			, @Param("currentDate")String currentDate);

	@Select("select order_no from collective_trans_order c where " +
			" c.create_time < #{currentDate} and c.create_time > #{maxdate}"
			+" and c.settlement_method = '1'"
			+" and c.settle_status = 3 "
			+" and c.trans_status = 'SUCCESS'"
			+" and c.settle_order is not null"
			+" and c.settle_type='2'"
			+" and c.acq_enname not in (${channels})"
			+" and c.acq_enname in (${transchannels})"
			+" and not EXISTS"
			+" (select 1 from activity_detail a where a.active_order = c.order_no);")
	@ResultType(String.class)
	List<String> selectT1HolidaySettleOrderList(@Param("channels")String channels, @Param("maxdate")String maxdate, @Param("transchannels")String transchannels
			, @Param("currentDate")String currentDate);

	@Update("<script>" +
			"update collective_trans_order c set c.settle_status = 4 where c.order_no in " +
			" <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" "+
			"         close=\")\" separator=\",\"> "+
			"#{item}"+
			"   </foreach> "+
			"</script>"
	)
	int updateSettle(@Param("list") List<String> orderNoList);

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


	@SelectProvider(type=SqlProvider.class,method="getCtoBySurvey")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder getCtoBySurvey(@Param("orderNo")String orderNo);

	@Select("select * from zq_merchant_info where unionpay_mer_no = #{uniMerNo}")
    ZqMerchantInfo getAcqMerchant(@Param("uniMerNo")String uniMerNo);

	@Select("select * from acq_terminal_store where union_mer_no = #{uniMerNo} order by id desc limit 1")
	AcqTerminalStore getAcqTer(String uniMerNo);

	@SelectProvider(type=SqlProvider.class,method="exportByOrderNos")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> exportByOrderNos(@Param("orderNos") String[] orderNos);

	@SelectProvider(type=SqlProvider.class,method="selectShareSettleInfoByOrderNos")
	@ResultType(ShareSettleInfo.class)
	List<ShareSettleInfo> selectShareSettleInfoByOrderNosWithPage(@Param("orderNos") String[] orderNos, @Param("page") Page<ShareSettleInfo> page);

	@SelectProvider(type=SqlProvider.class,method="selectShareSettleInfoByOrderNos")
	@ResultType(ShareSettleInfo.class)
	List<ShareSettleInfo> selectShareSettleInfoByOrderNos(@Param("orderNos") String[] orderNos);

	/**
	 * 解除黑名单T0订单
	 * @param merchantNo
	 * @param reSettleTaskCount
	 * @return
	 */
	@Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
			"FROM collective_trans_order " +
			"WHERE settlement_method='0' " +
			"AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE'" +
			"AND account=1 " +
			"AND (settle_status=0 OR settle_status IS NULL OR settle_status='3') " +
			"AND merchant_no=#{merchantNo} " +
			"AND trans_time < NOW()-INTERVAL #{reSettleTaskCount} SECOND")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectT0ByMerchantNoAndTransTime(@Param("merchantNo") String merchantNo, @Param("reSettleTaskCount")int reSettleTaskCount);

	/**
	 * 解除黑名单T1订单
	 * @param merchantNo
	 * @return
	 */
	@Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
			"FROM collective_trans_order " +
			"WHERE settlement_method='1' " +
			"AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
			"AND account=1 " +
			"AND (settle_status=0 OR settle_status IS NULL OR settle_status='3' OR settle_status='4') " +
			"AND merchant_no=#{merchantNo} " +
			"AND trans_time < DATE_SUB(CURDATE(), INTERVAL 8 DAY)")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectT1ByMerchantNo(@Param("merchantNo")String merchantNo);

	/**
	 * 解除黑名单T0转T1
	 * @param merchantNo
	 * @return
	 */
	@Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
			"FROM collective_trans_order " +
			"WHERE settlement_method='0' " +
			"AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
			"AND account=1 " +
			"AND settle_status='4' " +
			"AND merchant_no=#{merchantNo} " +
			"AND trans_time < DATE_SUB(CURDATE(), INTERVAL 8 DAY)")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> selectToT1ByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select(" SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
			"FROM collective_trans_order " +
			"WHERE order_no=#{orderNo}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder selectByOrderNo2(@Param("orderNo") String orderNo);


	class SqlProvider{

		public String selectShareSettleInfoByOrderNos(Map<String,Object> param){
			final String[] orderNos = (String[])param.get("orderNos");
			SQL sql = new SQL(){
				{
					SELECT("ss.id,ss.share_settle_no,ss.order_no,ss.merchant_no,ss.type,ss.acq_enname,ss.zq_merchant_no,ss.amount,ss.status,ss.start_time,ss.end_time,ss.err_msg,cto.order_type,ss.opertor ,  cto.n_prm,  CASE  WHEN ss.type = '2' THEN st.create_time WHEN ss.type = '1' THEN cto.create_time ELSE '' END origin_time ");
					FROM("share_settle ss");
					LEFT_OUTER_JOIN("collective_trans_order cto ON ss.order_no=cto.order_no");
					LEFT_OUTER_JOIN("settle_transfer st ON ss.order_no = st.account_serial_no");
					WHERE("ss.order_no IN " + orderNosStr(orderNos));
					ORDER_BY("ss.start_time DESC");
				}
			};
			return sql.toString();
		}


		public String exportByOrderNos(Map<String,Object> param){
			final String[] orderNos = (String[])param.get("orderNos");
			SQL sql = new SQL(){
				{
					SELECT("cto.id, cto.order_no, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, " +
							"cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price,  " +
							"cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account, " +
							"cto.quick_rate,cto.quick_fee,  cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.acq_enname," +
							"cto.business_product_id,cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,cto.settle_msg,  cto.n_prm,cto.zx_rate," +
							"mi.merchant_name,mi.recommended_source,mi.source_sys, mi.merchant_type, " +
							"CASE WHEN cto.pay_method = '1' THEN (SELECT ti.acq_reference_no FROM trans_info ti WHERE ti.order_no=cto.order_no) ELSE (SELECT ti2.acq_reference_no FROM scan_code_trans ti2 WHERE ti2.trade_no = cto.order_no ) END acq_reference_no, " +
							"(SELECT CONCAT(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s WHERE s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount, " +
							"cto.res_msg,ti.read_card,ti.is_iccard,si.service_type,mi.mobilephone,mi.address," +
							"ai.sale_name,toe.group_code," +
							"vo.vas_rate");
							//+",toe.merchant_rate AS merRate ");
					FROM("collective_trans_order cto");
					LEFT_OUTER_JOIN("service_info si ON si.service_id = cto.service_id");
					LEFT_OUTER_JOIN("trans_info ti ON ti.order_no=cto.order_no");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no=cto.merchant_no");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.one_agent_no");
					LEFT_OUTER_JOIN("trans_order_ext toe ON cto.order_no=toe.order_no");
					LEFT_OUTER_JOIN("vas_order vo ON vo.order_no = cto.order_no");
					WHERE("cto.order_no in " + orderNosStr(orderNos));
					ORDER_BY("cto.create_time DESC");
				}
			};
			return sql.toString();
		}

		public String selectMoneyInfoByOrderNos(Map<String,Object> param){
			final String[] orderNos = (String[])param.get("orderNos");
			SQL sql = new SQL(){
				{
					SELECT("SUM(cto.trans_amount) AS totalMoney, SUM(cto.merchant_fee) totalMerchantFee, SUM(cto.deduction_fee) totalDeductionFee , SUM(st.amount) AS totalAmount, SUM(st.fee_amount) AS totalFeeAmount, SUM(st.out_amount) AS totalOutAmount, SUM(cto.merchant_price) AS totalMerchantPrice, SUM(cto.deduction_mer_fee) AS totalDeductionMerFee, SUM(cto.n_prm) AS totalNPrm, " +
							"ifnull(count(*),0) as tradeCountNum ");
					FROM("collective_trans_order cto");
					LEFT_OUTER_JOIN("service_info si ON si.service_id = cto.service_id");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no=cto.merchant_no");
					LEFT_OUTER_JOIN("settle_transfer st ON st.order_no = cto.order_no AND st.STATUS = '4' AND st.correction = '0'");
					LEFT_OUTER_JOIN("vas_order vo ON vo.order_no = cto.order_no");
					WHERE("cto.order_no in " + orderNosStr(orderNos));
					ORDER_BY("cto.create_time DESC");
				}
			};
			return sql.toString();
		}

		public String selectByOrderNos(Map<String,Object> param){
			final String[] orderNos = (String[])param.get("orderNos");
			SQL sql = new SQL(){
				{
					SELECT("cto.id, cto.order_no,cto.acq_enname, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, " +
							"cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price,  " +
							"cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account,  " +
							"cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,si.service_type,  " +
							"cto.n_prm,cto.zx_rate,mi.merchant_name,mi.merchant_type,mi.recommended_source,mi.source_sys, " +
							"(SELECT CONCAT(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s WHERE s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount," +
							"cto.acq_merchant_fee,mi.mobilephone,mi.address,ai.sale_name,TO_DAYS(NOW()) - TO_DAYS(cto.trans_time) as days,"+
							"vo.vas_rate"
					);
							//",toe.merchant_rate AS merRate ");
					FROM("collective_trans_order cto");
					LEFT_OUTER_JOIN("service_info si ON si.service_id = cto.service_id");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no=cto.merchant_no");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.one_agent_no");
					LEFT_OUTER_JOIN("vas_order vo ON vo.order_no = cto.order_no");
					//LEFT_OUTER_JOIN("trans_order_ext toe ON cto.order_no=toe.order_no");
					WHERE("cto.order_no in " + orderNosStr(orderNos));
					ORDER_BY("cto.create_time DESC");
				}
			};
			return sql.toString();
		}

		private String orderNosStr(String[] orderNos){
			StringBuilder orderNoSb = new StringBuilder("(");
			for (int i = 0; i < orderNos.length; i++) {
				orderNoSb.append("'" + orderNos[i] + "'");
				if(i == orderNos.length - 1){
					orderNoSb.append(")");
				}else {
					orderNoSb.append(",");
				}
			}
			return orderNoSb.toString();
		}

		public String getCtoBySurvey(Map<String,Object> param){
			StringBuffer sb=new StringBuffer();
			sb.append("select  ");
			sb.append("	 cto.*,tis.acq_reference_no,hp.type_name, ");
			sb.append("	 bpd.bp_name,sis.service_name, ");
			sb.append("  mis.parent_node merAgentNode,mis.agent_no,mis.one_agent_no,mis.recommended_source, ");
			sb.append("	 case when tis.acq_enname='ZF_ZQ' THEN mis.merchant_name ELSE am.acq_merchant_name END acq_merchant_name, ");
			sb.append("	 (select s.out_amount FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) outAmount ");
			sb.append("  from collective_trans_order cto ");
			sb.append("		LEFT JOIN trans_info tis ON tis.order_no=cto.order_no ");
			sb.append("		LEFT JOIN business_product_define bpd on bpd.bp_id=cto.business_product_id ");
			sb.append("		LEFT JOIN service_info sis ON sis.service_id = cto.service_id ");
			sb.append("	    LEFT JOIN merchant_info mis ON mis.merchant_no = cto.merchant_no ");
			sb.append("	    LEFT JOIN acq_merchant am on am.acq_merchant_no=cto.acq_merchant_no ");
			sb.append("	    LEFT JOIN terminal_info ter ON ter.SN = cto.device_sn ");
			sb.append("     LEFT JOIN hardware_product hp on hp.hp_id=ter.type ");
			sb.append("  where 1=1 ");
			sb.append(" and cto.order_no=#{orderNo}");
			return sb.toString();
		}

			public String selectAllInfo(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
	    			StringBuilder sb = new StringBuilder();
	    			sb.append(" select cto.id, cto.order_no,cto.acq_enname, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price, ");
					sb.append(" cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account, ");
	    			sb.append(" cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,si.service_type, ");
				
					sb.append(" cto.n_prm,cto.zx_rate,mi.merchant_name,mi.merchant_type,mi.recommended_source,mi.source_sys,cte.profit_type, ");
					sb.append(" (select concat(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount");
					//查询营销费用
					sb.append(",cto.acq_merchant_fee,IFNULL(toe.delivery,0) AS delivery,mi.mobilephone,mi.address,ai.sale_name,TO_DAYS(NOW()) - TO_DAYS(cto.trans_time) as days ");
					sb.append(",vo.vas_rate");
					sb.append(" from collective_trans_order cto ");
					sb.append(" LEFT JOIN service_info si on si.service_id = cto.service_id ");
					sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no=cto.merchant_no ");
					sb.append(" LEFT JOIN agent_info ai ON ai.agent_no = mi.one_agent_no  ");
					sb.append(" LEFT JOIN trans_order_ext toe ON toe.order_no=cto.order_no ");
					sb.append(" LEFT JOIN vas_order vo ON vo.order_no=cto.order_no ");
					sb.append(" LEFT JOIN collective_trans_extended cte ON cte.source_id=cto.order_no ");
	    			fromSql(transInfo,sb);
	    			whereSql(transInfo, sb);
	    			sb.append(" order by cto.create_time desc ");
					sb.append(" LIMIT "+transInfo.getPageFirst()+","+transInfo.getPageSize());
					log.info("交易查询sql：" + sb.toString());
					return sb.toString();
	    	}

		  public String importAllInfo(Map<String,Object> param){
			  final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
			  StringBuilder sb = new StringBuilder();
			  sb.append(" select cto.id, cto.order_no, cto.settlement_method, cto.merchant_no, cto.pay_method, cto.card_type, cto.account_no, cto.trans_amount,cto.merchant_price,cto.deduction_mer_fee,(cto.merchant_price-cto.deduction_mer_fee) actual_merchant_price, ");
			  sb.append(" cto.merchant_fee, cto.deduction_fee, cto.actual_fee, cto.trans_status, cto.freeze_status, cto.settle_status, cto.account, cto.quick_rate,cto.quick_fee, ");
			  sb.append(" cto.create_time, cto.trans_time, cto.settle_order, cto.settle_type, cto.acq_enname,cto.business_product_id,cto.order_type,cto.merchant_rate,cto.unionpay_mer_no,cto.settle_msg, ");
			  sb.append(" cto.n_prm,cto.zx_rate,mi.merchant_name,mi.recommended_source,mi.source_sys,cte.profit_type, ");
			  sb.append(" mi.merchant_type,mi.mobilephone,mi.address,");
			  sb.append(" vo.vas_rate,");
			  sb.append(" toe.group_code,IFNULL(toe.delivery,0) AS delivery,");
			  sb.append(" (select ti.acq_reference_no from trans_info ti where ti.order_no=cto.order_no) acq_reference_no,");
			  sb.append(" (select concat(s.amount,',',s.fee_amount, ',', s.out_amount, ',', s.actual_fee) FROM settle_transfer s where s.order_no =cto.order_no AND s.STATUS = '4' AND s.correction = '0' LIMIT 1) amount,");
			  sb.append(" cto.res_msg,ti.read_card,ti.is_iccard,si.service_type,ai.sale_name ");
			  sb.append(" from collective_trans_order cto ");
			  sb.append(" LEFT JOIN service_info si on si.service_id = cto.service_id ");
			  sb.append(" LEFT JOIN trans_info ti ON ti.order_no=cto.order_no ");
			  sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no=cto.merchant_no ");
			  sb.append(" LEFT JOIN agent_info ai ON ai.agent_no = mi.one_agent_no  ");
			  sb.append(" LEFT JOIN collective_trans_extended cte ON cte.source_id=cto.order_no ");
			  sb.append(" LEFT JOIN vas_order vo ON vo.order_no = cto.order_no ");
			  //增加过滤，当条件没有的时候不联表查询
			  if(StringUtils.isNotBlank(transInfo.getTerType())||StringUtils.isNotBlank(transInfo.getActivityType())){
				  sb.append(" LEFT JOIN terminal_info ter on ter.SN = cto.device_sn ");
			  }
			  if(transInfo.getTeamId() != null){
				  sb.append(" LEFT JOIN business_product_define bpd ON bpd.bp_id=cto.business_product_id ");
			  }
//			  if(StringUtils.isNotBlank(transInfo.getGroupCode())){
//				  sb.append(" LEFT JOIN trans_route_group_acq_merchant route ON route.acq_merchant_no=cto.acq_merchant_no ");
//			  }
			  //if(StringUtils.isNotBlank(transInfo.getGroupCode())){
				  sb.append(" LEFT JOIN trans_order_ext toe ON toe.order_no=cto.order_no ");
			 // }
			  if("1".equals(transInfo.getAuthCodeStatus())){
				  sb.append(" LEFT JOIN scan_code_trans sct ON sct.trade_no=cto.order_no ");
			  }
			  whereSql(transInfo, sb);
			  sb.append(" order by cto.create_time desc ");
			  sb.append(" LIMIT "+transInfo.getPageFirst()+","+transInfo.getPageSize());
			  log.info("交易查询导出的sql：" + sb);
			  return sb.toString();
		  }


			public void fromSql(final CollectiveTransOrder transInfo,StringBuilder sb) {
//				sb.append(" from collective_trans_order cto ");
//				sb.append(" JOIN service_info si on si.service_id = cto.service_id ");
//				sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no=cto.merchant_no ");
				//增加过滤，当条件没有的时候不联表查询
				if(StringUtils.isNotBlank(transInfo.getTerType())||StringUtils.isNotBlank(transInfo.getActivityType())){
					sb.append(" LEFT JOIN terminal_info ter on ter.SN = cto.device_sn ");
				}
				if(StringUtils.isNotBlank(transInfo.getAcqMerchantNo()) || StringUtils.isNotBlank(transInfo.getAcqSerialNo())){
					sb.append(" LEFT JOIN trans_info ti ON ti.order_no=cto.order_no ");
				}
				/*if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" LEFT JOIN trans_route_group_acq_merchant route ON route.acq_merchant_no=cto.acq_merchant_no ");
				}*/
				/*if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" LEFT JOIN trans_order_ext toe ON toe.order_no=cto.order_no ");
				}*/
				if(transInfo.getTeamId() != null || transInfo.getTeamEntryId() != null){
					sb.append(" LEFT JOIN business_product_define bpd ON bpd.bp_id=cto.business_product_id ");
				}

				if("1".equals(transInfo.getAuthCodeStatus())){
					sb.append(" LEFT JOIN scan_code_trans sct ON sct.trade_no=cto.order_no ");
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
							sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
						}else{
							sb.append(" and cto.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
						}
					}else{
						sb.append(" and cto.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{transInfo.agentNo})");
					}
				}

				if(StringUtils.isNotBlank(transInfo.getProfitType())){
					sb.append(" and cte.profit_type=#{transInfo.profitType}");
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
					if("1".equals(transInfo.getAuthCodeStatus())){
						sb.append(" and sct.auth_code=#{transInfo.orderNo}");
					}else{
						sb.append(" and cto.order_no=#{transInfo.orderNo}");
					}
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
				/*if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" and route.group_code=#{transInfo.groupCode}");
				}*/
				if(StringUtils.isNotBlank(transInfo.getGroupCode())){
					sb.append(" and toe.group_code=#{transInfo.groupCode}");
				}
				if(transInfo.getZxStatus() != null){
					if(transInfo.getZxStatus() == 1){
						sb.append(" and cto.zx_rate > 0");
					} else{
						sb.append(" and cto.zx_rate = 0");
					}

				}
				if(StringUtils.isNotBlank(transInfo.getRecommendedSource())){
					sb.append(" and mi.recommended_source=#{transInfo.recommendedSource}");
				}
				if(StringUtils.isNotBlank(transInfo.getMerchantType())){
					sb.append(" and mi.merchant_type=#{transInfo.merchantType}");
				}
				// 如果有子组织，则按子组织查询
				if(StringUtils.isNotBlank(transInfo.getTeamEntryId())){
					sb.append(" and mi.team_entry_id=#{transInfo.teamEntryId} and bp_id IS NOT NULL ");
				}else if(transInfo.getTeamId() != null){
					sb.append(" and bpd.team_id=#{transInfo.teamId}");
				}
				if(StringUtils.isNotBlank(transInfo.getDeviceSn())){
					sb.append(" and cto.device_sn=#{transInfo.deviceSn}");
				}
				if(StringUtils.isNotBlank(transInfo.getDeviceSn())){
					sb.append(" and cto.device_sn=#{transInfo.deviceSn}");
				}
				if(transInfo.getSourceSysSta()!=null){
					if(transInfo.getSourceSysSta().intValue()==1){
						sb.append(" and mi.source_sys ='V2-agent'");
					}else if(transInfo.getSourceSysSta().intValue()==2){
						sb.append(" and IFNULL(mi.source_sys,'')!='V2-agent' ");
					}
				}
				if(StringUtils.isNotBlank(transInfo.getDelivery())){
					if("1".equals(transInfo.getDelivery())){
						sb.append(" and toe.delivery=1");
					}else{
						sb.append(" AND (toe.delivery=0 OR toe.delivery IS NULL)");
					}
				}
				//是否收取服务费
				if(StringUtils.isNotBlank(transInfo.getVasRate())){
					if("1".equals(transInfo.getVasRate())){
						sb.append(" and vo.vas_rate is not null");
					}else{
						sb.append(" and vo.vas_rate is null");
					}

				}
			}

	    	public String selectMoneyInfo(Map<String,Object> param){
	    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
				StringBuilder sb = new StringBuilder();
				sb.append("select ");
				if(transInfo.getQueryTotalStatus() == 1) {
					sb.append(" ifnull(sum(cto.trans_amount),0) as totalMoney, ifnull(sum(cto.merchant_fee),0) totalMerchantFee, ifnull(sum(cto.deduction_fee),0) totalDeductionFee ");
					sb.append(", ifnull(sum(st.amount),0) as totalAmount, ifnull(sum(st.fee_amount),0) as totalFeeAmount, ifnull(sum(st.out_amount),0) as totalOutAmount");
					sb.append(", ifnull(sum(cto.merchant_price),0) as totalMerchantPrice, ifnull(sum(cto.deduction_mer_fee),0) as totalDeductionMerFee");
					sb.append(", ifnull(sum(cto.n_prm),0) as totalNPrm,");
				}
				sb.append(" ifnull(count(*),0) as tradeCountNum");
				sb.append(" from collective_trans_order cto ");
				sb.append(" LEFT JOIN trans_order_ext toe ON toe.order_no=cto.order_no ");
				sb.append(" LEFT JOIN collective_trans_extended cte ON cte.source_id=cto.order_no ");
				//统计金额时，部分表如果只涉及筛选条件，则当筛选条件存在时，才关联表
				if(StringUtils.isNotBlank(transInfo.getServiceType())){
					sb.append("LEFT JOIN service_info si on si.service_id = cto.service_id ");
				}
				if(StringUtils.isNotBlank(transInfo.getRecommendedSource()) || StringUtils.isNotBlank(transInfo.getMerchantType())
						|| StringUtils.isNotBlank(transInfo.getTeamEntryId())|| transInfo.getSourceSysSta()!=null){
					sb.append(" LEFT JOIN merchant_info mi on mi.merchant_no=cto.merchant_no ");
				}
				fromSql(transInfo,sb);

				if(transInfo.getQueryTotalStatus() == 1) {
					sb.append(" left join settle_transfer st on st.order_no = cto.order_no and st.STATUS = '4' AND st.correction = '0'");
				}
				if(StringUtils.isNotBlank(transInfo.getVasRate())){
					sb.append(" LEFT JOIN vas_order vo on vo.order_no=cto.order_no ");
				}
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

	@Select("<script>" +
			"SELECT * FROM collective_trans_order WHERE trans_status='SUCCESS' AND account=1 AND freeze_status=0 AND " +
			"(settle_status = 0 OR settle_status=3)" +
			" <if test=\"channelNameList!=null and channelNameList.size > 0\">" +
			"		AND acq_enname in " +
			" 		<foreach collection=\"channelNameList\" item=\"item\" index=\"index\" open=\"(\" " +
			"         close=\")\" separator=\",\"> " +
			" 			#{item}" +
			"     </foreach> " +
			" </if> " +
			" AND create_time BETWEEN #{startTime} AND #{endTime}" +
			" order by create_time,id limit #{limitNumbers}" +
			"</script>")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> getUnSettele(@Param("channelNameList") List<String> channelNameList, @Param("startTime") Date startTime,
											@Param("endTime") Date endTime, @Param("limitNumbers")Integer limitNumbers);
	
	
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

	/**
	 * 查询商户的需要出款T0流水
	 * @return
	 */
	@Select("select cto.id from collective_trans_order cto "
			+ "where cto.trans_status='SUCCESS' and cto.trans_type='PURCHASE' "
			+ "and cto.account='1' and cto.freeze_status='0' "
			+ "and cto.settlement_method='0' and (cto.settle_status='0' or cto.settle_status is null or cto.settle_status='3') "
			+ "and cto.merchant_no = #{merchantNo} and cto.create_time<#{startTime}")
	@ResultType(Map.class)
	List<Map<String, Object>> queryResetTransfer(@Param("merchantNo")String merchantNo,@Param("startTime") Date startTime);
}
