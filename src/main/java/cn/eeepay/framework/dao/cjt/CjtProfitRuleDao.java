package cn.eeepay.framework.dao.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtProfitRule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 超级推分润奖励规则 数据层
 * 
 * @author tans
 * @date 2019-05-24
 */
public interface CjtProfitRuleDao {

	@Select("select * from cjt_profit_rule where profit_type = #{profitType} and user_type=#{userType} and status = '1'")
	@ResultType(CjtProfitRule.class)
	List<CjtProfitRule> selectProfitRuleByType(@Param("profitType") String profitType,@Param("userType")String userType);

	/**
     * 修改超级推分润奖励规则
     * 
     * @param cjtProfitRule 超级推分润奖励规则信息
     * @return
     */
	@Update("update cjt_profit_rule set profit_1 = #{profit1}, profit_2 = #{profit2}" +
			",profit_orgs=#{profitOrgs},profit_0=#{profit0},profit_1=#{profit1},profit_2=#{profit2}" +
			",remark=#{remark},status=#{status} where profit_rule_no = #{profitRuleNo}")
	int updateProfit(CjtProfitRule cjtProfitRule);

	@Select("select * from cjt_profit_rule where profit_type = #{profitType} and user_type=#{userType} limit 1")
	@ResultType(CjtProfitRule.class)
	CjtProfitRule selectFirstProfitRuleByType(@Param("profitType")String profitType,@Param("userType")String userType);

	@Select("select * from cjt_profit_rule where profit_rule_no = #{profitRuleNo} limit 1")
	@ResultType(CjtProfitRule.class)
    CjtProfitRule selectProfitRuleDetail(@Param("profitRuleNo")String profitRuleNo);
}