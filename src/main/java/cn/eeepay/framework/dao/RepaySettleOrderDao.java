package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WithdrawHisInfo;
import cn.eeepay.framework.model.YfbPayOrder;

/**
 * 超级还款结算管理
 * @author mays
 * @date 2017年10月31日
 */
public interface RepaySettleOrderDao {

	//结算订单查询
	@SelectProvider(type = SqlProvider.class, method = "selectSettleOrderByParam")
	@ResultType(WithdrawHisInfo.class)
	List<WithdrawHisInfo> selectSettleOrderByParam(@Param("page") Page<WithdrawHisInfo> page,
			@Param("info") WithdrawHisInfo info);

	//导出结算订单查询
	@SelectProvider(type = SqlProvider.class, method = "selectSettleOrderByParam")
	@ResultType(WithdrawHisInfo.class)
	List<WithdrawHisInfo> exportSettleOrderByParam(@Param("info") WithdrawHisInfo info);

	//结算订单查询汇总
	@SelectProvider(type = SqlProvider.class, method = "countSettleOrderByParam")
	@ResultType(String.class)
	String countSettleOrderByParam(@Param("info") WithdrawHisInfo info);

	//根据orderNo查询yfb_withdraw_his
	@Select("SELECT * FROM yfb_withdraw_his WHERE order_no=#{orderNo}")
	@ResultType(WithdrawHisInfo.class)
	WithdrawHisInfo selectYfbWithdrawHis(@Param("orderNo") String orderNo);

//	@Update("UPDATE yfb_extraction_his SET status='2' WHERE order_no=#{orderNo}")
//	int updateYfbExtractionHisStatus(String orderNo);

	//交易查询
	@SelectProvider(type = SqlProvider.class, method = "selectRepayTradeOrder")
	@ResultType(YfbPayOrder.class)
	List<YfbPayOrder> selectRepayTradeOrder(@Param("page") Page<YfbPayOrder> page,
			@Param("info") YfbPayOrder info);

	//交易查询-导出
	@SelectProvider(type = SqlProvider.class, method = "exportRepayTradeOrder")
	@ResultType(YfbPayOrder.class)
	List<YfbPayOrder> exportRepayTradeOrder(@Param("info") YfbPayOrder info);

	//交易查询汇总
	@SelectProvider(type = SqlProvider.class, method = "countRepayTradeOrder")
	Map<String, String> countRepayTradeOrder(@Param("info") YfbPayOrder info);

	//交易详情
	@Select("SELECT ypo.trans_type,ypo.trans_status,ypo.trans_amount,ypo.account_no,ypo.trans_time,ypo.card_type,ypo.res_msg,ycm.bank_name,"
			+ "ypo.merchant_no,yrmi.one_agent_no,ypo.trans_fee_rate,ypo.trans_fee,ai.agent_name one_agent_name,"
			+ "ypo.acq_code,ypo.acq_merchant_no,ypc.pay_merchant_name "
			+ "FROM yfb_pay_order ypo "
			+ "LEFT JOIN yfb_card_manage ycm ON ycm.account_no = ypo.account_no "
			+ "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ypo.merchant_no "
			+ "LEFT JOIN agent_info ai ON ai.agent_no = yrmi.one_agent_no "
			//+ "LEFT JOIN yfb_pay_channel ypc ON ypc.pay_merchant_no = ypo.acq_merchant_no "
			+"LEFT JOIN yfb_pay_channel ypc ON ypc.channel_code = ypo.acq_code AND ypc.pay_merchant_no = ypo.acq_merchant_no "
			+ "WHERE ypo.order_no = #{orderNo}")
	@ResultType(YfbPayOrder.class)
	YfbPayOrder selectTradeOrderDetail(@Param("orderNo") String orderNo);
	
	//获取交易通道，yfb_pay_channel
	@Select("SELECT channel_name text,channel_code value FROM yfb_pay_channel")
	List<Map<String, String>> listAcqCode();


	@Select(
			"select ypo.*," +
					" mis.agent_node merAgentNode,mis.agent_no,mis.one_agent_no, " +
					" zqmer.zq_merchant_no zqMerchantNo " +
					" from yfb_pay_order ypo " +
					" LEFT JOIN yfb_repay_merchant_info mis ON mis.merchant_no = ypo.merchant_no " +
					" LEFT JOIN yfb_zq_merchant_info zqmer ON (zqmer.merchant_no = ypo.merchant_no and zqmer.channel_code = ypo.acq_code) " +
					" where ypo.order_no = #{orderNo} "
	)
	YfbPayOrder getYfbPayOrder(@Param("orderNo")String orderNo);

	public class SqlProvider{

		public String selectSettleOrderByParam(Map<String, Object> param) {
			final WithdrawHisInfo info = (WithdrawHisInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ywh.order_no,ywh.service_order_no,ywh.mer_no,ywh.acc_name,ywh.mobile_no,ywh.status,ywh.fee,"
							+ "ywh.service,ywh.amount,ywh.create_time,ywi.nickname,yt.bank_order_no,ypc.channel_name,ywh.acc_no," +
							"yt.out_acc_no");
					FROM("yfb_withdraw_his ywh "
							+ "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ywh.mer_no "
							+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
							+ "LEFT JOIN yfb_transfer yt ON yt.order_no = ywh.transfer_order_no "
							+ "LEFT JOIN yfb_pay_channel ypc ON ypc.channel_code = ywh.withdraw_channel ");
				}
			};
			whereSql(info, sql);
			if (StringUtils.isNotBlank(info.getBankOrderNo())) {
				sql.WHERE("yt.bank_order_no=#{info.bankOrderNo} ");
			}
			sql.ORDER_BY("ywh.create_time DESC");
			return sql.toString();
		}

		public String countSettleOrderByParam(Map<String, Object> param) {
			final WithdrawHisInfo info = (WithdrawHisInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("sum(ywh.amount)");
					FROM("yfb_withdraw_his ywh LEFT JOIN yfb_transfer yt ON yt.order_no = ywh.transfer_order_no ");
				}
			};
			whereSql(info, sql);
			return sql.toString();
		}

		public void whereSql(WithdrawHisInfo info, SQL sql){
			if(info == null){
				return;
			}
			if (StringUtils.isNotBlank(info.getMerNo())) {
				sql.WHERE("ywh.mer_no=#{info.merNo} ");
			}
			if (StringUtils.isNotBlank(info.getOrderNo())) {
				sql.WHERE("ywh.order_no=#{info.orderNo} ");
			}
			if (StringUtils.isNotBlank(info.getMobileNo())) {
				sql.WHERE("ywh.mobile_no=#{info.mobileNo} ");
			}
			if (StringUtils.isNotBlank(info.getStatus())) {
				sql.WHERE("ywh.status=#{info.status} ");
			}
			if (StringUtils.isNotBlank(info.getService())) {
				sql.WHERE("ywh.service=#{info.service} ");
			}
			if (StringUtils.isNotBlank(info.getsAmount())) {
				sql.WHERE("ywh.amount>=#{info.sAmount} ");
			}
			if (StringUtils.isNotBlank(info.geteAmount())) {
				sql.WHERE("ywh.amount<=#{info.eAmount} ");
			}
			if (StringUtils.isNotBlank(info.getWithdrawChannel())) {
				sql.WHERE("ywh.withdraw_channel=#{info.withdrawChannel} ");
			}
			if (StringUtils.isNotBlank(info.getsCreateTime())) {
				sql.WHERE("ywh.create_time>=#{info.sCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.geteCreateTime())) {
				sql.WHERE("ywh.create_time<=#{info.eCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.getAccNo())) {
				sql.WHERE("ywh.acc_no=#{info.accNo}");
			}
			if (StringUtils.isNotBlank(info.getServiceOrderNo())) {
				sql.WHERE("ywh.service_order_no=#{info.serviceOrderNo}");
			}
			if (StringUtils.isNotBlank(info.getOutAccNo())) {
				sql.WHERE("yt.out_acc_no=#{info.outAccNo} ");
			}
		}

		public String selectRepayTradeOrder(Map<String, Object> param) {
			final YfbPayOrder info = (YfbPayOrder) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ypo.id,ypo.merchant_no,ypo.order_no,ypo.service,ypo.trans_type,ypo.trans_status,ypo.trans_amount,ypo.trans_fee,ypo.res_msg,"
							+ "ypo.trans_fee_rate,ypo.service_order_no,ypo.acq_code,ypo.account_no,ypo.create_time,ypo.trans_time,ypo.acq_merchant_no,"
							+ "yrmi.mobile_no,ywi.nickname,yth.status record_status");
					FROM("yfb_pay_order ypo "
							+ "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ypo.merchant_no "
							+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
							+ "LEFT JOIN yfb_tally_his yth ON yth.service_order_no = ypo.order_no AND yth.service = 'trade' ");
				}
			};
			whereTradeSql(info, sql);
			sql.ORDER_BY("ypo.create_time DESC");
			return sql.toString();
		}

		public String exportRepayTradeOrder(Map<String, Object> param) {
			final YfbPayOrder info = (YfbPayOrder) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ypo.id,ypo.merchant_no,ypo.order_no,ypo.service,ypo.trans_type,ypo.trans_status,ypo.trans_amount,ypo.trans_fee,ypo.res_msg,"
							+ "ypo.trans_fee_rate,ypo.service_order_no,ypo.acq_code,ypo.account_no,ypo.create_time,ypo.trans_time,ypo.acq_merchant_no,"
							+ "yrmi.mobile_no,yth.status record_status");
					FROM("yfb_pay_order ypo "
							+ "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ypo.merchant_no "
							+ "LEFT JOIN yfb_tally_his yth ON yth.service_order_no = ypo.order_no AND yth.service = 'trade' ");
				}
			};
			whereTradeSql(info, sql);
			sql.ORDER_BY("ypo.create_time DESC");
			return sql.toString();
		}

		public String countRepayTradeOrder(Map<String, Object> param) {
			final YfbPayOrder info = (YfbPayOrder) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("SUM(ypo.trans_amount) sumAmount,SUM(ypo.trans_fee) sumFeeAmount");
					FROM("yfb_pay_order ypo "
							+ "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ypo.merchant_no "
							+ "LEFT JOIN yfb_tally_his yth ON yth.service_order_no = ypo.order_no AND yth.service = 'trade' ");
				}
			};
			whereTradeSql(info, sql);
			System.out.println("超级还交易查询汇总sql: " + sql.toString());
			return sql.toString();
		}

		public void whereTradeSql(YfbPayOrder info, SQL sql){
			if(info == null){
				return;
			}
			if (StringUtils.isNotBlank(info.getOrderNo())) {
				sql.WHERE("ypo.order_no=#{info.orderNo} ");
			}
			if (StringUtils.isNotBlank(info.getMerchantNo())) {
				sql.WHERE("ypo.merchant_no=#{info.merchantNo} ");
			}
			if (StringUtils.isNotBlank(info.getMobileNo())) {
				sql.WHERE("yrmi.mobile_no=#{info.mobileNo} ");
			}
			if (StringUtils.isNotBlank(info.getService())) {
				sql.WHERE("ypo.service=#{info.service} ");
			}
			if (StringUtils.isNotBlank(info.getTransType())) {
				sql.WHERE("ypo.trans_type=#{info.transType} ");
			}
			if (StringUtils.isNotBlank(info.getTransStatus())) {
				sql.WHERE("ypo.trans_status=#{info.transStatus} ");
			}
			if (StringUtils.isNotBlank(info.getServiceOrderNo())) {
				sql.WHERE("ypo.service_order_no=#{info.serviceOrderNo} ");
			}
			if (StringUtils.isNotBlank(info.getRecordStatus())) {
				sql.WHERE("yth.status=#{info.recordStatus} ");
			}
			if (StringUtils.isNotBlank(info.getAcqCode())) {
				sql.WHERE("ypo.acq_code=#{info.acqCode} ");
			}
			if (StringUtils.isNotBlank(info.getAcqMerchantNo())) {
				sql.WHERE("ypo.acq_merchant_no=#{info.acqMerchantNo} ");
			}
			if (StringUtils.isNotBlank(info.getsTransAmount())) {
				sql.WHERE("ypo.trans_amount>=#{info.sTransAmount} ");
			}
			if (StringUtils.isNotBlank(info.geteTransAmount())) {
				sql.WHERE("ypo.trans_amount<=#{info.eTransAmount} ");
			}
			if (StringUtils.isNotBlank(info.getsTransFee())) {
				sql.WHERE("ypo.trans_fee>=#{info.sTransFee} ");
			}
			if (StringUtils.isNotBlank(info.geteTransFee())) {
				sql.WHERE("ypo.trans_fee<=#{info.eTransFee} ");
			}
			if (StringUtils.isNotBlank(info.getsCreateTime())) {
				sql.WHERE("ypo.create_time>=#{info.sCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.geteCreateTime())) {
				sql.WHERE("ypo.create_time<=#{info.eCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.getsTransTime())) {
				sql.WHERE("ypo.trans_time>=#{info.sTransTime} ");
			}
			if (StringUtils.isNotBlank(info.geteTransTime())) {
				sql.WHERE("ypo.trans_time<=#{info.eTransTime} ");
			}

		}

	}

}
