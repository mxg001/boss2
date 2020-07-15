package cn.eeepay.framework.daoCreditMgr;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmVipConfig;
import cn.eeepay.framework.model.CmVipConfigAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface CmSettingDao {

	/**
	 * 根据type查询会员配置信息
	 * @author	mays
	 * @date	2018年5月19日
	 */
	@Select("SELECT * FROM cm_vip_config WHERE type = ${type}")
	@ResultType(CmVipConfig.class)
	CmVipConfig selectVipConfigByType(@Param("type") int type);

	/**
	 * 修改会员配置信息
	 * @author	mays
	 * @date	2018年5月19日
	 */
	@Update("update cm_vip_config set vip_fee=${info.vipFee},valid_period=${info.validPeriod},agent_share=${info.agentShare},update_time=now()" +
			",vip_charge=${info.vipCharge},agent_comfig=${info.agentComfig},card_limit=${info.cardLimit},card_limit_num=${info.cardLimitNum} where type=${info.type}")
	int updateVipConfig(@Param("info") CmVipConfig info);

	@SelectProvider(type = SqlProvider.class, method = "selectVipConfigAgentInfo")
	@ResultType(CmVipConfigAgent.class)
	List<CmVipConfigAgent> selectVipConfigAgent(@Param("page") Page<CmVipConfigAgent> page,@Param("info") CmVipConfigAgent info);

	@Select("SELECT * FROM cm_vip_config_agent WHERE agent_no = #{agentNo}")
	@ResultType(CmVipConfigAgent.class)
	CmVipConfigAgent selectVipConfigAgentByAgentNo(@Param("agentNo") String agentNo);

	@Select("SELECT count(agent_no) FROM cm_vip_config_agent WHERE agent_no = #{agentNo}")
	int selectVipConfigAgentByAgentNoCount(String agentNo);

	@SelectProvider(type = SqlProvider.class, method = "selectVipConfigAgentCount")
	int selectVipConfigAgentCount(@Param("info") CmVipConfigAgent info);

	@Insert("INSERT INTO cm_vip_config_agent ( agent_no, agent_name, agent_level, src_org_prduct, src_org_id, " +
			"vip_fee, valid_period, agent_share, remark, create_time, operator) VALUES " +
			"( #{info.agentNo}, #{info.agentName}, #{info.agentLevel}, #{info.srcOrgPrduct}, #{info.srcOrgId}, " +
			"#{info.vipFee}, #{info.validPeriod},#{info.agentShare}, #{info.remark}, NOW(), #{info.operator})")
	int saveCmSettingAgent(@Param("info") CmVipConfigAgent info);

	@Delete("delete from cm_vip_config_agent where agent_no = #{agentNo}")
	int deleteCmSettingAgentByAgentNo(@Param("agentNo") String agentNo);

	@Update("update cm_vip_config_agent set vip_fee=#{info.vipFee},valid_period=#{info.validPeriod},agent_share=#{info.agentShare}" +
			",remark=#{info.remark},update_time=now(),operator=#{info.operator} where agent_no=${info.agentNo}")
	int updateCmSettingAgent(@Param("info") CmVipConfigAgent info);

	@InsertProvider(type = SqlProvider.class, method = "addButchCmSettingAgent")
	int addButchCmSettingAgent(@Param("list") List<CmVipConfigAgent> list);

	public class SqlProvider {

		public String addButchCmSettingAgent(Map<String, Object> param) {
			@SuppressWarnings("unchecked")
			final List<CmVipConfigAgent> list = (List<CmVipConfigAgent>) param.get("list");
			StringBuilder sb = new StringBuilder();
			sb.append(
					"insert into cm_vip_config_agent(agent_no, agent_name, agent_level, src_org_prduct, src_org_id," +
							"vip_fee, valid_period, agent_share,operator) values ");
			MessageFormat message = new MessageFormat(
					"(#'{'list[{0}].agentNo},#'{'list[{0}].agentName},#'{'list[{0}].agentLevel},#'{'list[{0}].srcOrgPrduct},#'{'list[{0}].srcOrgId},"
							+ "#'{'list[{0}].vipFee},#'{'list[{0}].validPeriod},#'{'list[{0}].agentShare},#'{'list[{0}].operator}),");
			for (int i = 0; i < list.size(); i++) {
				sb.append(message.format(new Integer[] { i }));
			}
			sb.setLength(sb.length() - 1);
			System.out.println(sb.toString());
			return sb.toString();
		}

		public String selectVipConfigAgentInfo(Map<String, Object> param) {
			final CmVipConfigAgent info = (CmVipConfigAgent) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ca.*");
					FROM("cm_vip_config_agent ca");
				}
			};
			where(sql, info);
			sql.ORDER_BY("ca.id desc");
			return sql.toString();
		}

		public String selectVipConfigAgentCount(Map<String, Object> param) {
			final CmVipConfigAgent info = (CmVipConfigAgent) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("count(ca.id)");
					FROM("cm_vip_config_agent ca");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		public void where(SQL sql, CmVipConfigAgent info) {
			if (StringUtils.isNotBlank(info.getSrcOrgId())) {
				sql.WHERE("ca.src_org_id = #{info.srcOrgId}");
			}
			if (StringUtils.isNotBlank(info.getSrcOrgPrduct())) {
				sql.WHERE("ca.src_org_prduct = #{info.srcOrgPrduct}");
			}
			if (StringUtils.isNotBlank(info.getAgentNo())) {
				sql.WHERE("ca.agent_no = #{info.agentNo}");
			}
			if (StringUtils.isNotBlank(info.getAgentName())) {
				sql.WHERE("ca.agent_name = #{info.agentName}");
			}
			if (StringUtils.isNotBlank(info.getStartTime())) {
				sql.WHERE("ca.create_time >= #{info.startTime}");
			}
			if (StringUtils.isNotBlank(info.getEndTime())) {
				sql.WHERE("ca.create_time <= #{info.endTime}");
			}
		}
	}
}