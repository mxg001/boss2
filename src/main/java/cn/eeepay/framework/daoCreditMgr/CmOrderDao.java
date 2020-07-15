package cn.eeepay.framework.daoCreditMgr;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmPayment;

public interface CmOrderDao {

	@SelectProvider(type = SqlProvider.class, method = "selectOrderInfo")
	@ResultType(CmPayment.class)
	List<CmPayment> selectOrderInfo(@Param("page") Page<CmPayment> page, @Param("info") CmPayment info);

	@SelectProvider(type = SqlProvider.class, method = "selectOrderInfo")
	@ResultType(CmPayment.class)
	List<CmPayment> exportOrderInfo(@Param("info") CmPayment info);
	
	@SelectProvider(type = SqlProvider.class, method = "sumOrderInfo")
	String sumOrderInfo(@Param("info") CmPayment info);

	@Select("SELECT cp.*,coi.org_name,coi.org_id FROM cm_payment cp LEFT JOIN cm_user cu ON cu.user_no = cp.user_no "
			+ "LEFT JOIN cm_org_info coi ON coi.org_id = cu.src_org_id WHERE cp.trade_no = #{tradeNo}")
	@ResultType(CmPayment.class)
	CmPayment queryOrderInfoById(@Param("tradeNo") String tradeNo);

	public class SqlProvider {

		public String selectOrderInfo(Map<String, Object> param) {
			final CmPayment info = (CmPayment) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cp.*,coi.org_name,coi.org_id");
					FROM("cm_payment cp");
					LEFT_OUTER_JOIN("cm_user cu ON cu.user_no = cp.user_no");
					LEFT_OUTER_JOIN("cm_org_info coi ON coi.org_id = cu.src_org_id");
				}
			};
			where(sql, info);
			sql.ORDER_BY("cp.id desc");
			return sql.toString();
		}

		public String sumOrderInfo(Map<String, Object> param) {
			final CmPayment info = (CmPayment) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("sum(cp.trans_amount) / 100");
					FROM("cm_payment cp");
					LEFT_OUTER_JOIN("cm_user cu ON cu.user_no = cp.user_no");
					LEFT_OUTER_JOIN("cm_org_info coi ON coi.org_id = cu.src_org_id");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		public void where(SQL sql, CmPayment info) {
			if (StringUtils.isNotBlank(info.getTradeNo())) {
				sql.WHERE("cp.trade_no = #{info.tradeNo}");
			}
			if (StringUtils.isNotBlank(info.getOrgId())) {
				sql.WHERE("coi.org_id = #{info.orgId}");
			}
			if (StringUtils.isNotBlank(info.getAgentNo())) {
				sql.WHERE("cu.agent_no = #{info.agentNo}");
			}
			if (StringUtils.isNotBlank(info.getTransStatus())) {
				sql.WHERE("cp.trans_status = #{info.transStatus}");
			}
			if (StringUtils.isNotBlank(info.getTransType())) {
				sql.WHERE("cp.trans_type = #{info.transType}");
			}
			if (StringUtils.isNotBlank(info.getsExpireTime())) {
				sql.WHERE("cp.expire_time >= #{info.sExpireTime}");
			}
			if (StringUtils.isNotBlank(info.geteExpireTime())) {
				sql.WHERE("cp.expire_time <= #{info.eExpireTime}");
			}
		}

	}

}