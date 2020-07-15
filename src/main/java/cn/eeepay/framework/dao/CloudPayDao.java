package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CloudPayInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 云闪付
 * @author mays
 * @date 2017年10月23日
 */
public interface CloudPayDao {

	@SelectProvider(type = SqlProvider.class, method = "selectCloudPayByParam")
	@ResultType(CloudPayInfo.class)
	List<CloudPayInfo> selectCloudPayByParam(@Param("page") Page<CloudPayInfo> page, @Param("info") CloudPayInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectMerchantProfitCount")
	@ResultType(String.class)
	String selectMerchantProfitCount(@Param("info") CloudPayInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectCloudPayByParam")
	@ResultType(CloudPayInfo.class)
	List<CloudPayInfo> importCloudPayByParam(@Param("info") CloudPayInfo info);

	public class SqlProvider{

		public String selectCloudPayByParam(Map<String, Object> param) {
			final CloudPayInfo info = (CloudPayInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cpp.*,mi.merchant_name,mi.mobilephone,ai.agent_name,aii.agent_name AS one_agent_name");
					FROM("cloud_pay_profit cpp "
							+ "LEFT JOIN merchant_info mi ON mi.merchant_no = cpp.merchant_no "
							+ "LEFT JOIN agent_info ai ON ai.agent_no = mi.agent_no "
							+ "LEFT JOIN agent_info aii ON aii.agent_no = mi.one_agent_no ");
				}
			};
			whereSql(info,sql);
			sql.ORDER_BY(" cpp.create_time DESC");
			return sql.toString();
		}

		public String selectMerchantProfitCount(Map<String, Object> param) {
			final CloudPayInfo info = (CloudPayInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("sum(cpp.merchant_profit) merchantProfitCount");
					FROM("cloud_pay_profit cpp "
							+ "LEFT JOIN merchant_info mi ON mi.merchant_no = cpp.merchant_no "
							+ "LEFT JOIN agent_info ai ON ai.agent_no = mi.agent_no "
							+ "LEFT JOIN agent_info aii ON aii.agent_no = mi.one_agent_no ");
				}
			};
			whereSql(info,sql);
			return sql.toString();
		}

		private  void whereSql(CloudPayInfo info, SQL sql){
			if (StringUtils.isNotBlank(info.getMerchantNo())) {
				sql.WHERE("cpp.merchant_no=#{info.merchantNo} ");
			}
			if (StringUtils.isNotBlank(info.getMerchantName())) {
				sql.WHERE("mi.merchant_name=#{info.merchantName} ");
			}
			if (StringUtils.isNotBlank(info.getMobilephone())) {
				sql.WHERE("mi.mobilephone=#{info.mobilephone} ");
			}
			if (StringUtils.isNotBlank(info.getAgentNo())) {
				sql.WHERE("mi.agent_no=#{info.agentNo} ");
			}
			if (info.getsTransAmount() != null) {
				sql.WHERE("cpp.trans_amount >= #{info.sTransAmount} ");
			}
			if (info.geteTransAmount() != null) {
				sql.WHERE("cpp.trans_amount <= #{info.eTransAmount} ");
			}
			if (info.getsMerchantProfit() != null) {
				sql.WHERE("cpp.merchant_profit >= #{info.sMerchantProfit} ");
			}
			if (info.geteMerchantProfit() != null) {
				sql.WHERE("cpp.merchant_profit <= #{info.eMerchantProfit} ");
			}
			if (StringUtils.isNotBlank(info.getOrderNo())) {
				sql.WHERE("cpp.order_no=#{info.orderNo} ");
			}
			if (StringUtils.isNotBlank(info.getActivityCode())) {
				sql.WHERE("cpp.activity_code=#{info.activityCode} ");
			}
			if (StringUtils.isNotBlank(info.getsTime())) {
				sql.WHERE("cpp.create_time >= #{info.sTime} ");
			}
			if (StringUtils.isNotBlank(info.geteTime())) {
				sql.WHERE("cpp.create_time <= #{info.eTime} ");
			}
		}

	}

}
