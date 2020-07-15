package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SuperPushConfig;
import cn.eeepay.framework.model.SuperPushShareRule;

/** 
 * @author tans 
 * @version ：2017年5月5日 上午10:29:10 
 * 
 */
public interface SuperPushConfigDao {

	@Select("select spc.* from super_push_config spc where spc.bp_id = #{bpId}")
	@ResultType(SuperPushConfig.class)
	SuperPushConfig getByBpId(@Param("bpId")String bpId);

	@SelectProvider(type=SqlProvider.class, method="getAgentList")
	@ResultType(AgentInfo.class)
	List<AgentInfo> getAgentList(@Param("appAgentNoList")String[] appAgentNoList);
	
//	@Select("select csr.*,si.service_name from super_push_share_rule csr "
//			+ " inner join service_info si on csr.service_Id=si.service_id"
//			+ " inner join business_product_info bpi on bpi.service_id=csr.service_id and bpi.bp_id = #{bpId}"
//			+ " order by CASE WHEN si.link_service IS NULL THEN si.service_id ELSE si.link_service END,"
//    		+ " CASE WHEN si.service_type in ('10000', '10001') THEN '1' ELSE '0' END;")
	@Select("select csr.*,bpd.bp_name from super_push_share_rule csr "
	+ " inner join business_product_define bpd on bpd.bp_id=csr.bp_id and csr.bp_id in (${bpId})"
			)
	@ResultType(SuperPushShareRule.class)
	List<SuperPushShareRule> getShareRuleList(@Param("bpId")String bpId);
	
	@Insert("insert into super_push_share_rule(bp_id,service_id,service_type,agent_profit_type,one_agent_profit_type,profit_type,profit_type1,profit_type2,profit_type3,"
			+ " agent_per_fix_income,one_agent_per_fix_income,per_fix_income,per_fix_income1,per_fix_income2,per_fix_income3,agent_per_fix_inrate,one_agent_per_fix_inrate,per_fix_inrate,per_fix_inrate1,"
			+ " per_fix_inrate2,per_fix_inrate3,create_date,operator) "
			+ " values(#{bpId},#{serviceId},#{serviceType},"
			+ "#{agentProfitType},#{oneAgentProfitType},#{profitType},#{profitType1},#{profitType2},#{profitType3},"
			+ "#{agentPerFixIncome},#{oneAgentPerFixIncome},#{perFixIncome},#{perFixIncome1},#{perFixIncome2},#{perFixIncome3},"
			+ "#{agentPerFixInrate},#{oneAgentPerFixInrate},#{perFixInrate},#{perFixInrate1},#{perFixInrate2},#{perFixInrate3},"
			+ "now(),#{operator})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
	int insertShareRule(SuperPushShareRule rule);
	
	@Update("update super_push_share_rule set "
			+ " agent_profit_type=#{agentProfitType},one_agent_profit_type=#{oneAgentProfitType},"
			+ " profit_type=#{profitType},profit_type1=#{profitType1},profit_type2=#{profitType2},profit_type3=#{profitType3},"
			+ " agent_per_fix_income=#{agentPerFixIncome},one_agent_per_fix_income=#{oneAgentPerFixIncome},"
			+ " per_fix_income=#{perFixIncome},per_fix_income1=#{perFixIncome1},per_fix_income2=#{perFixIncome2},per_fix_income3=#{perFixIncome3},"
			+ " agent_per_fix_inrate=#{agentPerFixInrate},one_agent_per_fix_inrate=#{oneAgentPerFixInrate},"
			+ " per_fix_inrate=#{perFixInrate},per_fix_inrate1=#{perFixInrate1},per_fix_inrate2=#{perFixInrate2},per_fix_inrate3=#{perFixInrate3},"
			+ " operator=#{operator}"
			+ " where id=#{id}")
	int updateShareRule(SuperPushShareRule rule);
	
	@Insert("insert into super_push_config(bp_id,tx_service_id,app_agent_no_list,message_module,create_date,operator,incentive_fund_switch,reward_point_switch,prizes_amount,bonus_message_module) "
			+ " values (#{bpId},#{txServiceId},#{appAgentNoList},#{messageModule},now(),#{operator}, #{incentiveFundSwitch},#{rewardPointSwitch},#{prizesAmount},#{bonusMessageModule})")
	int insertConfig(SuperPushConfig baseInfo);

	@Update("update super_push_config set bp_id=#{bpId},tx_service_id=#{txServiceId},app_agent_no_list=#{appAgentNoList},"
			+ " message_module=#{messageModule},update_date=now(),operator=#{operator},incentive_fund_switch = #{incentiveFundSwitch}, "
			+ " reward_point_switch=#{rewardPointSwitch},prizes_amount=#{prizesAmount},bonus_message_module=#{bonusMessageModule}  where id=#{id}")
	int updateConfig(SuperPushConfig baseInfo);
	
	public class SqlProvider{
		public String getAgentList(Map<String, Object> param){
			StringBuilder sql = new StringBuilder("select ai.agent_no,ai.agent_name from agent_info ai");
			final String[] appAgentNoList = (String[]) param.get("appAgentNoList");
			if(appAgentNoList!=null && appAgentNoList.length>0){
				sql.append(" where ai.agent_no in(-1,");
				MessageFormat messageFormat = new MessageFormat("#'{'appAgentNoList[{0}]},");
				for(int i=0;i<appAgentNoList.length;i++){
					sql.append(messageFormat.format(new Integer[] { i }));
				}
				sql.replace(sql.length()-1, sql.length(), ")");//替换最后一个“,”
			}
			return sql.toString();
		}
		
	}


}
