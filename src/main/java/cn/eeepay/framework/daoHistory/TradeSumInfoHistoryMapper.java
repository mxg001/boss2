package cn.eeepay.framework.daoHistory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.three.SumDo;
import cn.eeepay.framework.model.three.TransServiceIdDo;
import cn.eeepay.framework.util.StringUtil;

public interface TradeSumInfoHistoryMapper {

	@SelectProvider(type = TradeSumInfoHistoryMapper.SqlProvider.class, method = "tradeSumByList")
	@ResultType(SumDo.class)
	List<SumDo> tradeSumByList(@Param("agentList") List<String> agentList, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("teamList") List<String> teamList);

	@SelectProvider(type = TradeSumInfoHistoryMapper.SqlProvider.class, method = "findAllTransServiceIdByTeamId")
	@ResultType(TransServiceIdDo.class)
	List<TransServiceIdDo> findAllTransServiceIdByTeamId(@Param("teamId") String teamId,
			@Param("childAgentNo") String childAgentNo, @Param("agentNode") String agentNode,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	@SelectProvider(type = TradeSumInfoHistoryMapper.SqlProvider.class, method = "findAllSettleServiceIdByTeamId")
	@ResultType(TransServiceIdDo.class)
	List<TransServiceIdDo> findAllSettleServiceIdByTeamId(@Param("teamId") String teamId,
			@Param("childAgentNo") String childAgentNo, @Param("agentNode") String agentNode,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	@SuppressWarnings("unchecked")
	public class SqlProvider {
		public String tradeSumByList(Map<String, Object> params) {
			final List<String> agentList = (List<String>) params.get("agentList");
			final List<String> teamList = (List<String>) params.get("teamList");
			String sql = new SQL() {
				{
					SELECT(" sum( c_t_order.trans_amount ) AS trans_amount,a_i.agent_no AS agent_no,m_info.team_id AS team_id ");
					FROM(" agent_info a_i ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.agent_node LIKE CONCAT( a_i.agent_node, '%' ) ");
					JOIN(" merchant_info m_info ON m_info.merchant_no = c_t_order.merchant_no ");
					if (agentList != null && agentList.size() > 0) {
						String inJoint = StringUtil.inJoint("a_i.agent_no", "agentList", agentList.size());
						WHERE(inJoint);
					}
					if (teamList != null && teamList.size() > 0) {
						String inJoint = StringUtil.inJoint("m_info.team_id", "teamList", teamList.size());
						WHERE(inJoint);
					}
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					GROUP_BY(" a_i.agent_no,m_info.team_id ");
				}
			}.toString();
			return sql;
		}

		public String findAllTransServiceIdByTeamId(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" sum( c_t_order.trans_amount ) AS trans_amount_sum,c_t_order.service_id as service_id , c_t_order.card_type AS card_type,a_a_link.agent_node as agent_node  ");
					FROM(" agent_business_product a_b_pro ");
					JOIN(" business_product_define b_p_de ON a_b_pro.bp_id = b_p_de.bp_id ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.business_product_id = b_p_de.bp_id ");
					JOIN(" agent_info a_info ON c_t_order.agent_node LIKE CONCAT( a_info.agent_node, '%' ) ");
					JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link =a_info.agent_no ");
					WHERE(" a_a_link.agent_node LIKE CONCAT( #{agentNode}, '%' ) ");
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					WHERE(" c_t_order.order_type != 3 ");
					WHERE(" a_b_pro.agent_no = #{childAgentNo} ");
					WHERE(" b_p_de.team_id=#{teamId} ");
					GROUP_BY("service_id,card_type,agent_node");
				}
			}.toString();
			return sql;
		}

		public String findAllSettleServiceIdByTeamId(Map<String, Object> params) {
			String sql = new SQL() {
				{
					SELECT(" sum( s_trans.amount ) AS trans_amount_sum,c_t_order.service_id AS service_id,c_t_order.card_type AS card_type,count( * ) AS num,a_a_link.agent_node as agent_node  ");
					FROM(" agent_business_product a_b_pro ");
					JOIN(" business_product_define b_p_de ON a_b_pro.bp_id = b_p_de.bp_id ");
					JOIN(" collective_trans_order c_t_order ON c_t_order.business_product_id = b_p_de.bp_id ");
					JOIN(" agent_info a_info ON c_t_order.agent_node LIKE CONCAT( a_info.agent_node, '%' ) ");
					JOIN(" agent_authorized_link a_a_link ON a_a_link.agent_link =a_info.agent_no ");
					JOIN(" settle_transfer s_trans ON c_t_order.order_no = s_trans.order_no ");
					WHERE(" a_a_link.agent_node LIKE CONCAT( #{agentNode}, '%' ) ");
					WHERE(" c_t_order.trans_status = 'SUCCESS' ");
					WHERE(" c_t_order.trans_time >= #{startTime} ");
					WHERE(" c_t_order.trans_time <= #{endTime} ");
					WHERE(" a_b_pro.agent_no = #{childAgentNo} ");
					WHERE(" b_p_de.team_id=#{teamId} ");
					GROUP_BY("service_id,card_type,agent_node");
				}
			}.toString();
			return sql;
		}
	}

}
