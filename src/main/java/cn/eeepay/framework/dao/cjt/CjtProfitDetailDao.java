package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtProfitDetail;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 超级推收益明细 数据层
 * 
 * @author tans
 * @date 2019-06-14
 */
public interface CjtProfitDetailDao {

	/**
     * 查询超级推收益明细列表
     *
     * @param baseInfo 超级推收益明细信息
     * @return 超级推收益明细集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	@ResultType(CjtProfitDetail.class)
	List<CjtProfitDetail> selectPage(@Param("page") Page<CjtProfitDetail> page,
									 @Param("baseInfo") CjtProfitDetail baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectTotal")
	@ResultType(CjtProfitDetail.class)
	Map<String,Object> selectTotal(@Param("baseInfo") CjtProfitDetail baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectTotalTrans")
	@ResultType(CjtProfitDetail.class)
	Map<String,Object> selectTotalTrans(@Param("baseInfo")CjtProfitDetail baseInfo);

	@Select("select * from cjt_profit_detail where order_no = #{orderNo}")
	@ResultType(CjtProfitDetail.class)
	CjtProfitDetail select(@Param("orderNo") String orderNo);

	@Update("update cjt_profit_detail set recharge_status = #{rechargeStatus}, recharge_time = #{rechargeTime}" +
			" where order_no = #{orderNo}")
	int updateRechargeStatus(@Param("orderNo") String orderNo, @Param("rechargeTime") Date rechargeTime,
							 @Param("rechargeStatus") String rechargeStatus);

	class SqlProvider{
		public String selectPage(Map<String, Object> param){
			CjtProfitDetail baseInfo = (CjtProfitDetail) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cpd.*");
			if("M".equals(baseInfo.getUserType())){
				sql.SELECT("mi.merchant_name");
			}else if("A".equals(baseInfo.getUserType())){
				sql.SELECT("mi.agent_name merchant_name");
			}
			fromSql(sql,baseInfo);
			whereSql(sql, baseInfo);
			sql.ORDER_BY("cpd.create_time desc");
			return sql.toString();
		}

		public String selectTotal(Map<String, Object> param){
			CjtProfitDetail baseInfo = (CjtProfitDetail) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("sum(cpd.profit_amount) as totalProfitAmount");
			sql.SELECT("sum(if(cpd.recharge_status='1',cpd.profit_amount,0)) as totalSuccessProfitSAmount");
			sql.SELECT("sum(if(cpd.recharge_status='0',cpd.profit_amount,0)) as totalFailProfitSAmount");
			fromSql(sql,baseInfo);
			whereSql(sql, baseInfo);
			return sql.toString();
		}

		public String selectTotalTrans(Map<String, Object> param){
			CjtProfitDetail baseInfo = (CjtProfitDetail) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("cpd.profit_from_order_no,cpd.profit_from_amount");
			fromSql(sql,baseInfo);
			whereSql(sql, baseInfo);
			sql.WHERE("cpd.profit_type in ('posTrade','noCardTrade')");
			sql.GROUP_BY("cpd.profit_from_order_no");

			SQL sumSql = new SQL();
			sumSql.SELECT("sum(t.profit_from_amount) as totalTransAmount, count(1) as totalTransNum");
			sumSql.FROM("(" + sql.toString() + ") t");
			return sumSql.toString();
		}

		private void whereSql(SQL sql, CjtProfitDetail baseInfo) {
			if(StringUtils.isNotEmpty(baseInfo.getUserType())){
				sql.WHERE("cpd.user_type = #{baseInfo.userType}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getMerchantNo())) {
				sql.WHERE("cpd.merchant_no = #{baseInfo.merchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getFromLevel())) {
				sql.WHERE("cpd.from_level = #{baseInfo.fromLevel}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getProfitType())) {
				if(CjtProfitRule.trade.equals(baseInfo.getProfitType())) {
					sql.WHERE("(cpd.profit_type = 'posTrade' or cpd.profit_type = 'noCardTrade')");
				} else {
					sql.WHERE("cpd.profit_type = #{baseInfo.profitType}");
				}
			}
			if(StringUtils.isNotEmpty(baseInfo.getTransType())) {
				sql.WHERE("cpd.profit_type = #{baseInfo.transType}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeStart())){
				sql.WHERE("cpd.create_time >= #{baseInfo.createTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getCreateTimeEnd())){
				sql.WHERE("cpd.create_time <= #{baseInfo.createTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getRechargeTimeStart())){
				sql.WHERE("cpd.recharge_time >= #{baseInfo.rechargeTimeStart}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getRechargeTimeEnd())){
				sql.WHERE("cpd.recharge_time <= #{baseInfo.rechargeTimeEnd}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getProfitFromOrderNo())) {
				sql.WHERE("cpd.profit_from_order_no = #{baseInfo.profitFromOrderNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getFromMerchantNo())) {
				sql.WHERE("cpd.from_merchant_no = #{baseInfo.fromMerchantNo}");
			}
			if(StringUtils.isNotEmpty(baseInfo.getRechargeStatus())) {
				sql.WHERE("cpd.recharge_status = #{baseInfo.rechargeStatus}");
			}
		}

		private void fromSql(SQL sql,CjtProfitDetail baseInfo) {
			sql.FROM("cjt_profit_detail cpd");
			if("M".equals(baseInfo.getUserType())){
				sql.LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no = cpd.merchant_no");
			}
			if("A".equals(baseInfo.getUserType())){
				sql.LEFT_OUTER_JOIN("agent_info mi on mi.agent_no = cpd.merchant_no");
			}

		}
	}

}