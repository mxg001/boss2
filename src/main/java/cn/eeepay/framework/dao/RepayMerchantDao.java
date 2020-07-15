package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.enums.RepayEnum;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
public interface RepayMerchantDao {

	@SelectProvider(type = SqlProvider.class, method = "selectRepayMerchantByParam")
	@ResultType(RepayMerchantInfo.class)
	List<RepayMerchantInfo> selectRepayMerchantByParam(@Param("page") Page<RepayMerchantInfo> page,
			@Param("info") RepayMerchantInfo info);

	@Select("SELECT yrmi.*,ywi.nickname,ywi.create_time AS enter_time,ywi.headimgurl,"
			+ "ai.agent_name,yua.sfzzm_url,yua.sfzfm_url,yua.scsfz_url "
			+ "FROM yfb_repay_merchant_info yrmi "
			+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
			+ "LEFT JOIN agent_info ai ON ai.agent_no = yrmi.agent_no "
			+ "LEFT JOIN yfb_unified_account yua ON yua.un_account_mer_no = yrmi.merchant_no "
			+ "WHERE yrmi.merchant_no=#{merchantNo} ")
	@ResultType(RepayMerchantInfo.class)
	RepayMerchantInfo queryRepayMerchantByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT ycm.* FROM yfb_unified_account_product yuap "
			+ "INNER JOIN yfb_unified_account_card yuac ON yuac.un_account_mer_no = yuap.un_account_mer_no "
			+ "INNER JOIN yfb_card_manage ycm ON ycm.card_no = yuac.card_no "
			+ "WHERE yuap.pro_mer_no = #{merchantNo} AND yuap.pro_code = 'repay'")
	@ResultType(YfbCardManage.class)
	List<YfbCardManage> queryCardByMerchantNo(@Param("merchantNo")String merchantNo);

	@Update("UPDATE yfb_repay_merchant_info SET mer_account=1 WHERE merchant_no=#{merchantNo}")
	int updateRepayMerchantAccountStatus(String merchantNo);

	@Select("SELECT balance_no,balance,freeze_amount FROM yfb_balance "
			+ "WHERE mer_no = #{merchantNo}")
	@ResultType(YfbBalance.class)
	List<YfbBalance> queryBalanceByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT yzmi.merchant_no,yzmi.zq_merchant_no,yzmi.report_status,yzmi.effective_status,yzmi.channel_code,ci.channel_name FROM yfb_zq_merchant_info yzmi LEFT JOIN yfb_pay_channel ci on yzmi.channel_code=ci.channel_code "
			+ "WHERE yzmi.merchant_no = #{merchantNo}")
	@ResultType(YfbChannelSyn.class)
	List<YfbChannelSyn> queryYfbChannelSynByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT yzsl.operator,yzsl.operate_type,yzsl.operate_result,yzsl.create_time,ci.channel_name FROM yfb_zq_sync_log yzsl LEFT JOIN yfb_pay_channel ci on yzsl.channel_code=ci.channel_code "
			+ "WHERE yzsl.merchant_no = #{merchantNo}")
	@ResultType(YfbChannelSynLog.class)
	List<YfbChannelSynLog> queryYfbChannelSynLogByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("select * from yfb_repay_merchant_info where merchant_no = #{merchantNo}")
	@ResultType(RepayMerchantInfo.class)
	RepayMerchantInfo selectByMerchantNo(String merchantNo);

	@Update("update yfb_repay_merchant_info set status = #{status} where merchant_no = #{merchantNo}")
	int updateRepayMerchantStatus(@Param("status")String status, @Param("merchantNo")String merchantNo);

	@Select("SELECT * FROM " +
			"(SELECT ypc.channel_name,ybcr.acq_code,ybcr.card_no,ybcr.resp_msg,DATE_FORMAT(ybcr.create_time,'%Y-%m-%d %H:%i:%s') AS create_time " +
			"FROM yfb_bind_card_record ybcr " +
			"LEFT JOIN yfb_pay_channel ypc ON ybcr.acq_code=ypc.channel_code " +
			"WHERE card_no=#{cardNo} ORDER BY ybcr.create_time DESC ) nybcr " +
			"GROUP BY acq_code")
	@ResultType(YfbBindCardRecord.class)
	List<YfbBindCardRecord> queryBindCardRecord(@Param("cardNo") String cardNo);

	@Select(
			"select * FROM yfb_repay_merchant_info WHERE merchant_no=#{merchantNo}"
	)
	RepayMerchantInfo getDataProcessing(@Param("merchantNo")String merchantNo);

	public class SqlProvider{

		public String selectRepayMerchantByParam(Map<String, Object> param) {
			final RepayMerchantInfo info = (RepayMerchantInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("yrmi.merchant_no,yrmi.mobile_no,yrmi.user_name,yrmi.agent_no,yrmi.mer_account,yrmi.status,yrmi.status as statusValue,yrmi.create_time,"
							+ "ywi.nickname,yua.create_time AS enter_time,ai.agent_name,yii.pro_mer_no, yrmi.one_agent_no,aii.agent_name one_agent_name");
					FROM("yfb_repay_merchant_info yrmi "
							+ "INNER JOIN agent_info ai ON ai.agent_no = yrmi.agent_no "
							+ "INNER JOIN agent_info aii ON aii.agent_no = yrmi.one_agent_no "
							+ "INNER JOIN yfb_unified_account_product yi ON yi.pro_mer_no = yrmi.merchant_no AND yi.pro_code = 'repay' "
							+ "LEFT JOIN yfb_unified_account_product yii ON yii.un_account_mer_no = yi.un_account_mer_no AND yii.pro_code = 'gatherService' "
							+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
							+ "INNER JOIN yfb_unified_account yua on yua.un_account_mer_no = yi.un_account_mer_no ");
					info.setNfcStartDate(RepayMerchantInfo.NFC_START_DATE);
					if(StringUtils.isNotBlank(info.getSelectType()) && RepayEnum.NFC.getType().equals(info.getSelectType())) {
						WHERE("yrmi.create_time >= #{info.nfcStartDate}");
						WHERE("yua.create_time >= #{info.nfcStartDate}");
					} else {
						WHERE("yrmi.create_time < #{info.nfcStartDate}");
						WHERE("yua.create_time < #{info.nfcStartDate}");
					}
					if (StringUtils.isNotBlank(info.getMerchantNo())) {
						WHERE("yrmi.merchant_no=#{info.merchantNo} ");
					}
					if (StringUtils.isNotBlank(info.getMobileNo())) {
						WHERE("yrmi.mobile_no=#{info.mobileNo} ");
					}
					if (StringUtils.isNotBlank(info.getProMerNo())) {
						WHERE("yii.pro_mer_no=#{info.proMerNo} ");
					}
					if (StringUtils.isNotBlank(info.getAgentNo())) {
						WHERE("yrmi.agent_no=#{info.agentNo} ");
					}
					if (StringUtils.isNotBlank(info.getMerAccount())) {
						WHERE("yrmi.mer_account=#{info.merAccount} ");
					}
					if (StringUtils.isNotBlank(info.getStatus())) {
						WHERE("yrmi.status = #{info.status}");
					}
					if (StringUtils.isNotBlank(info.getsCreateTime())) {
						WHERE("yrmi.create_time>=#{info.sCreateTime} ");
					}
					if (StringUtils.isNotBlank(info.geteCreateTime())) {
						WHERE("yrmi.create_time<=#{info.eCreateTime} ");
					}
					if (StringUtils.isNotBlank(info.getsEnterTime())) {
						WHERE("yua.create_time>=#{info.sEnterTime} ");
					}
					if (StringUtils.isNotBlank(info.geteEnterTime())) {
						WHERE("yua.create_time<=#{info.eEnterTime} ");
					}
					ORDER_BY("yrmi.create_time DESC");
				}
			};
			return sql.toString();
		}

	}

}
