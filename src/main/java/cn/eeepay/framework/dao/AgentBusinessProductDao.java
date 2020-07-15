package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface AgentBusinessProductDao {

	@Select("select bp_id from agent_business_product where agent_no = #{agentNo}")
	@ResultType(java.lang.String.class)
	List<String> selectProductByAgent(@Param("agentNo")String agentNo);

	@Select("select id from agent_business_product where bp_id= #{bpId} limit 1")
	@ResultType(java.lang.Integer.class)
	Integer findIdByBp(@Param("bpId")String bpId);

	@SelectProvider(type=SqlProvider.class, method="findIdByBpIds")
	int findIdByBpIds(@Param("bpIds")List<Long> bpIds);

	@UpdateProvider(type=SqlProvider.class, method="updateDefaultBp")
	int updateDefaultBp(@Param("bpIds")List<Integer> bps, @Param("agentNo")String agentNo);
	
	public class SqlProvider{
		public String findIdByBpIds(Map<String, Object> param){
			final List<Long> bpIds = (List<Long>) param.get("bpIds");
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from agent_business_product where bp_id in(");
			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]},");
            for (int i = 0; i < bpIds.size(); i++) {
                sb.append(messageFormat.format(new Integer[]{i}));
            }
            sb.setLength(sb.length() - 1);
            sb.append(")");
            System.out.println(sb.toString());
			return sb.toString();
		}
		
		public String updateDefaultBp(Map<String, Object> param){
			StringBuilder sql = new StringBuilder();
			final List<Integer> bpIds = (List<Integer>) param.get("bpIds");
			sql.append("UPDATE agent_business_product");
			sql.append(" 	SET default_bp_flag = 1");
			sql.append(" 	WHERE (");
			sql.append(" 	 agent_no = #{agentNo}");
			sql.append("  ) AND bp_id IN (");
			MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]},");
            for (int i = 0; i < bpIds.size(); i++) {
                sql.append(messageFormat.format(new Integer[]{i}));
            }
            sql.setLength(sql.length() - 1);
            sql.append(")");
			sql.append(" 	and	(bp_id NOT IN(");
			sql.append(" 		SELECT bp_id FROM business_product_group");
			sql.append("    )");
			sql.append("    OR");
			sql.append("    bp_id IN (");
			sql.append("       SELECT bpg.bp_id FROM business_product_group bpg");
            sql.append("       JOIN business_product_define bpd ON bpg.bp_id = bpd.bp_id");
            sql.append("       WHERE bpd.allow_individual_apply = 1");
            sql.append("      ))");
			System.out.println(sql.toString());
			return sql.toString();
		}
		
	}

	
}
