package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LotteryBonusConf;
import cn.eeepay.framework.model.RankingRule;

public interface RankingRuleDao {
	/**查询排行榜规则管理列表*/
	@SelectProvider(type = SqlProvider.class, method = "selectRankingRuleByPage")
	@ResultType(LotteryBonusConf.class)
	List<RankingRule> getRankingRuleList(@Param("baseInfo") RankingRule baseInfo,
			@Param("pager") Page<RankingRule> pager);
	
	 public class SqlProvider{
	     public String selectRankingRuleByPage(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final RankingRule ruleParam = (RankingRule) param.get("baseInfo");
	           sql.append(("select rr.id,rr.rule_code,rr.rule_name,rr.rule_type,rr.data_type,rr.status,rr.show_order_no,rr.advert_url,rr.open_org,rr.org_type,rr.start_time,rr.end_time,rr.update_time,rr.level_total_money,rr.open_time "));
	           sql.append((" from ranking_rule rr where 1=1 "));
	          
	           if(ruleParam != null){
	        	   if(StringUtils.isNotBlank(ruleParam.getRuleCode())){
	        		   sql.append(" and rr.rule_code like concat('%',#{baseInfo.ruleCode},'%')");
	        	   }
	        	   if(StringUtils.isNotBlank(ruleParam.getRuleType())){
	        		   sql.append(" and rr.rule_type =#{baseInfo.ruleType}");
	        	   }
	        	   if(StringUtils.isNotBlank(ruleParam.getStatus())){
	        		   sql.append(" and rr.status =#{baseInfo.status}");
	        	   }
	        	   if(StringUtils.isNotBlank(ruleParam.getOpenOrg())){
	        		   sql.append(" and rr.open_org like concat('%',#{baseInfo.openOrg},'%')");
	        	   }
	           }
	            
	           sql.append(" order by rr.id desc ");
	           
	           System.out.println(sql.toString());
	           
	           return sql.toString();
	     }
    }
	@Update("update ranking_rule set status = #{status},open_time = #{openTime},update_time = SYSDATE() where id = #{id}")
	int updateRankingRuleStatus(RankingRule info);
	
	/**根据id查询排行配置*/
	@Select("select * from ranking_rule where id = #{id} ")
    @ResultType(RankingRule.class)
	RankingRule selectRankingRuleByRuleId(@Param("id") Long id);
	
	/**新增排行榜规则*/
	@Insert(
         	" INSERT INTO ranking_rule " +
                    "(rule_code,rule_name,rule_type,data_type,status,show_order_no,advert_url,open_org,org_type,start_time,end_time,update_time,introduction,bonus_note,level_total_money,open_time,open_org_info,busi_type) " +
                    " VALUES (#{rankingRule.ruleCode},#{rankingRule.ruleName},#{rankingRule.ruleType},#{rankingRule.dataType},#{rankingRule.status}," +
                    "  #{rankingRule.showOrderNo},#{rankingRule.advertUrl},#{rankingRule.openOrg},#{rankingRule.orgType},#{rankingRule.startTime},#{rankingRule.endTime},SYSDATE(),#{rankingRule.introduction},#{rankingRule.bonusNote},#{rankingRule.levelTotalMoney},#{rankingRule.openTime},#{rankingRule.openOrgInfo},#{rankingRule.busiType}) "
			)
	int insertRankingRule(@Param("rankingRule")RankingRule rankingRule);

	/**修改排行榜规则*/
    @Update(
            " update ranking_rule set rule_name=#{rankingRule.ruleName},rule_type=#{rankingRule.ruleType},data_type=#{rankingRule.dataType}, " +
                    " status=#{rankingRule.status},show_order_no=#{rankingRule.showOrderNo}, advert_url=#{rankingRule.advertUrl}, " +
                    " open_org=#{rankingRule.openOrg},org_type=#{rankingRule.orgType}, start_time=#{rankingRule.startTime}," +
                    " end_time=#{rankingRule.endTime}, update_time=SYSDATE(),introduction=#{rankingRule.introduction}," +
                    " bonus_note= #{rankingRule.bonusNote},level_total_money=#{rankingRule.levelTotalMoney},open_time=#{rankingRule.openTime},open_org_info=#{rankingRule.openOrgInfo},busi_type=#{rankingRule.busiType} " +
                    "where id=#{rankingRule.id}"
    )
    int updateRankingRule(@Param("rankingRule")RankingRule rankingRule);
	
    /**根据Rule Code查询排行配置*/
	@Select("select * from ranking_rule where rule_code = #{code} ")
    @ResultType(RankingRule.class)
	RankingRule selectRankingRuleByRuleCode(@Param("code") String code);
	
	 /**根据open_org模糊查询排行配置*/
	@Select("select * from ranking_rule where open_org like concat('%',#{orgId},'%') order by id desc ")
    @ResultType(RankingRule.class)
	List<RankingRule> selectRankingRuleByOrgId(@Param("orgId") String orgId);
	
	 /**根据showOrderNo查询是否已经有存在的显示顺序*/
	@Select("select * from ranking_rule where show_order_no =#{showOrderNo} ")
    @ResultType(RankingRule.class)
	List<RankingRule> selectRankingRuleByShowOrderNo(@Param("showOrderNo") String showOrderNo);
	
}
