package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayOemServiceCostBean;

/**
 *
 * Created by 666666 on 2017/11/4.
 */
public interface RepayBusinessDao {

	/**
	 * 分页查询oem服务商交易成本配置
	 * 
	 * @param page
	 * @return
	 */
//	@Select("SELECT yos.agent_no,ai.agent_name,yos.trade_fee_rate rate,yos.trade_single_fee singleAmount,"
//			+ "full_repay_fee_rate fullRepayRate,full_repay_single_fee fullRepaySingleAmount "
//			+ "FROM yfb_oem_service yos " + "LEFT JOIN agent_info ai ON ai.agent_no = yos.agent_no "
//			+ "WHERE yos.oem_type = 'repay'")
	@SelectProvider(type = SqlProvider.class, method = "listRepayBusiness")
	@ResultType(RepayOemServiceCostBean.class)
	List<RepayOemServiceCostBean> listRepayBusiness(@Param("page") Page<RepayOemServiceCostBean> page,
			@Param("info") RepayOemServiceCostBean info);

	@Update("update yfb_oem_service set trade_fee_rate = #{bean.rate},trade_single_fee=#{bean.singleAmount},"
			+ "full_repay_fee_rate = #{bean.fullRepayRate},full_repay_single_fee=#{bean.fullRepaySingleAmount},"
			+ "perfect_repay_fee_rate = #{bean.perfectRate},perfect_repay_single_fee=#{bean.perfectSingleAmount} "
			+ "where agent_no = #{bean.agentNo} and oem_type = 'repay'")
	int updateRepayBusiness(@Param("bean") RepayOemServiceCostBean bean);

	@Select("SELECT rate,single_amount,full_repay_rate,full_repay_single_amount,perfect_repay_rate perfectRate,"
			+ "perfect_repay_single_amount perfectSingleAmount FROM yfb_service_cost WHERE agent_no = #{agentNo}")
	RepayOemServiceCostBean queryRepayServiceCost(@Param("agentNo") String agentNo);

	public class SqlProvider {

		public String listRepayBusiness(final Map<String, Object> param) {
			final RepayOemServiceCostBean info = (RepayOemServiceCostBean) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("yos.agent_no,ai.agent_name,yos.trade_fee_rate rate,yos.trade_single_fee singleAmount,"
							+ "full_repay_fee_rate fullRepayRate,full_repay_single_fee fullRepaySingleAmount,"
							+ "perfect_repay_fee_rate perfectRate,perfect_repay_single_fee perfectSingleAmount");
					FROM("yfb_oem_service yos");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = yos.agent_no");
//					WHERE("yos.oem_type = 'repay'"); 20180604注掉
					if (StringUtils.isNotBlank(info.getAgentNo())) {
						WHERE("yos.agent_no = #{info.agentNo}");
					}
					if (StringUtils.isNotBlank(info.getAgentName())) {
						WHERE("ai.agent_name = #{info.agentName}");
					}
				}
			};
			return sql.toString();
		}
	}

}
