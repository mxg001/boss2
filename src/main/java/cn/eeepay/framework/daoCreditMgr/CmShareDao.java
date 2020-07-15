package cn.eeepay.framework.daoCreditMgr;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmShare;

public interface CmShareDao {

	@SelectProvider(type = SqlProvider.class, method = "selectShareInfo")
	@ResultType(CmShare.class)
	List<CmShare> selectShareInfo(@Param("page") Page<CmShare> page, @Param("info") CmShare info);

	@SelectProvider(type = SqlProvider.class, method = "selectShareInfo")
	@ResultType(CmShare.class)
	List<CmShare> exportShareInfo(@Param("info") CmShare info);
	
	@SelectProvider(type = SqlProvider.class, method = "sumShareInfo")
	Map<String, String> sumShareInfo(@Param("info") CmShare info);

	public class SqlProvider {

		public String selectShareInfo(Map<String, Object> param) {
			final CmShare info = (CmShare) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cs.*");
					FROM("cm_share cs");
				}
			};
			where(sql, info);
			sql.ORDER_BY("cs.id desc");
			return sql.toString();
		}

		public String sumShareInfo(Map<String, Object> param) {
			final CmShare info = (CmShare) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("sum(cs.share_cash) sumShareCash, sum(cs.order_cash) sumOrderCash");
					FROM("cm_share cs");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		public void where(SQL sql, CmShare info) {
			if (StringUtils.isNotBlank(info.getUserId())) {
				sql.WHERE("cs.user_id = #{info.userId}");
			}
			if (StringUtils.isNotBlank(info.getAgentNode())) {
				if ("1".equals(info.getContain())) {
					sql.WHERE("cs.agent_node like CONCAT(#{info.agentNode}, '%')");
				} else {
					sql.WHERE("cs.agent_node = #{info.agentNode}");
				}
			}
			if (StringUtils.isNotBlank(info.getRelatedOrderNo())) {
				sql.WHERE("cs.related_order_no = #{info.relatedOrderNo}");
			}
			if (info.getOrderType() != null) {
				sql.WHERE("cs.order_type = #{info.orderType}");
			}
			if (info.getEnterStatus() != null) {
				sql.WHERE("cs.enter_status = #{info.enterStatus}");
			}
			if (StringUtils.isNotBlank(info.getShareAgentNo())) {
				sql.WHERE("cs.share_agent_no = #{info.shareAgentNo}");
			}
			if (StringUtils.isNotBlank(info.getsCreateDate())) {
				sql.WHERE("cs.create_date >= #{info.sCreateDate}");
			}
			if (StringUtils.isNotBlank(info.geteCreateDate())) {
				sql.WHERE("cs.create_date <= #{info.eCreateDate}");
			}
		}

	}

}