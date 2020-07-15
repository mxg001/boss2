package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtMerchantInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 新版超级推商户 数据层
 * 
 * @author tans
 * @date 2019-06-14
 */
public interface CjtMerchantInfoDao {

	/**
     * 查询新版超级推商户列表
     * 
     * @param baseInfo 新版超级推商户信息
     * @return 新版超级推商户集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(CjtMerchantInfo.class)
	public List<CjtMerchantInfo> selectPage(@Param("page") Page<CjtMerchantInfo> page,
											@Param("baseInfo") CjtMerchantInfo baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectTotal")
	@ResultType(Map.class)
	Map<String, Object> selectTotal(@Param("baseInfo") CjtMerchantInfo baseInfo);

	@Select("select cmi.merchant_no, " +
			" mi.merchant_name,mi.mobilephone,mi.lawyer,mi.id_card_no,mi.status as merchantStatus,mi.pre_frozen_amount," +
			" mi.merchant_type,mi.address, " +
			" mi1.merchant_name as oneMerchantName," +
			" mi2.merchant_name as twoMerchantName," +
			" ai.agent_name, " +
			" pai.agent_name as oneAgentName,pai.sale_name " +
			" from cjt_merchant_info cmi" +
			" left join merchant_info mi on mi.merchant_no = cmi.merchant_no " +
			" left join merchant_info mi1 on mi1.merchant_no = cmi.one_merchant_no" +
			" left join merchant_info mi2 on mi2.merchant_no = cmi.two_merchant_no" +
			" left join agent_info ai on ai.agent_node = cmi.agent_node" +
			" left join agent_info  pai on pai.agent_no = ai.one_level_id" +
			" where cmi.merchant_no = #{merchantNo}")
	@ResultType(CjtMerchantInfo.class)
    CjtMerchantInfo selectDetail(@Param("merchantNo") String merchantNo);

	@Select("select mi.merchant_no,mi.merchant_name," +
			" ai.agent_no,ai.agent_name" +
			" from merchant_info mi " +
			" left join agent_info ai on ai.agent_no = mi.agent_no " +
			" where mi.merchant_no = #{merchantNo}")
	@ResultType(CjtMerchantInfo.class)
	CjtMerchantInfo selectSmallDetail(@Param("merchantNo") String merchantNo);

	@Select("select sum(profit_amount) as profitAmount from cjt_profit_count where merchant_no = #{merchantNo} group by merchant_no")
	@ResultType(BigDecimal.class)
	BigDecimal selectTotalAmount(@Param("merchantNo") String merchantNo);

	class SqlProvider{
		public String selectPage(Map<String, Object> param){
			CjtMerchantInfo baseInfo = (CjtMerchantInfo) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cmi.*");
			sql.SELECT("ai.agent_no,ai.agent_name,ai.one_level_id as oneAgentNo");
			sql.SELECT("ui.mobilephone");
			sql.SELECT("cap.status as activityStatus,cap.have_amount,cap.target_amount,cap.create_time as startTime,cap.end_time");
			sql.SELECT("cap.target_time");
			sql.SELECT("if(cap.id is not null,1,0) as terBindStatus");
			sql.SELECT("if((select count(1) from cjt_order co where co.merchant_no = cmi.merchant_no and co.order_status IN ('1', '2') limit 1) = 0 ,0,1) as terApplyStatus");
			fromSql(sql);
			whereSql(sql, baseInfo);
			sql.ORDER_BY("cmi.id desc");
			return sql.toString();
		}

		public String selectTotal(Map<String, Object> param){
			CjtMerchantInfo baseInfo = (CjtMerchantInfo) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("sum(cpc.profit_amount) as profit_amount");//累计收益
			fromSql(sql);
			sql.LEFT_OUTER_JOIN("cjt_profit_count cpc on cpc.merchant_no = cmi.merchant_no");
			whereSql(sql, baseInfo);
			sql.GROUP_BY("cmi.merchant_no");
			SQL sumSqL = new SQL();
			sumSqL.SELECT("sum(profit_amount) as profitTotal");
			sumSqL.FROM("(" + sql.toString() + ")t");
			return sumSqL.toString();
		}

		private void fromSql(SQL sql) {
			sql.FROM("cjt_merchant_info cmi");
			sql.LEFT_OUTER_JOIN("agent_info ai on ai.agent_node = cmi.agent_node");
			sql.LEFT_OUTER_JOIN("user_info ui on ui.user_id = cmi.user_id");
			sql.LEFT_OUTER_JOIN("cjt_activity_profit cap on cap.from_merchant_no = cmi.merchant_no and cap.level = 'one'");
		}

		private void whereSql(SQL sql, CjtMerchantInfo baseInfo) {

			if(StringUtils.isNotEmpty(baseInfo.getMerchantNo())) {
				sql.WHERE("cmi.merchant_no = #{baseInfo.merchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getMobilephone())) {
				sql.WHERE("ui.mobilephone = #{baseInfo.mobilephone}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())){
				sql.WHERE("cmi.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())){
				sql.WHERE("cmi.create_time <= #{baseInfo.createTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getAgentNode())) {
				sql.WHERE("cmi.agent_node = #{baseInfo.agentNode}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getOneAgentNo())) {
				sql.WHERE("ai.one_level_id = #{baseInfo.oneAgentNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getOneMerchantNo())) {
				sql.WHERE("cmi.one_merchant_no = #{baseInfo.oneMerchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTwoMerchantNo())) {
				sql.WHERE("cmi.two_merchant_no = #{baseInfo.twoMerchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getActivityStatus())) {
				sql.WHERE("cap.status = #{baseInfo.activityStatus}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTerApplyStatus())) {
				String existsSql = " exists (select 1 from cjt_order co2 where co2.merchant_no = cmi.merchant_no)";
				if("0".equals(baseInfo.getTerApplyStatus())){
					sql.WHERE("not " + existsSql);
				} else {
					sql.WHERE(existsSql);
				}
			}
			if(StringUtils.isNotEmpty(baseInfo.getTerBindStatus())) {
				if("0".equals(baseInfo.getTerBindStatus())){
					sql.WHERE("cap.id is null");
				} else {
					sql.WHERE("cap.id is not null");
				}
			}
			if(StringUtils.isNotEmpty(baseInfo.getTargetTimeStart())){
				sql.WHERE("cap.target_time >= #{baseInfo.targetTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getTargetTimeEnd())){
				sql.WHERE("cap.target_time <= #{baseInfo.targetTimeEnd}");
			}
		}
	}
	
}