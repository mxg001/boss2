package cn.eeepay.framework.daoSuperbank;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.RankingRuleHistory;

public interface RankingRuleHistoryDao {
	/**查询排行榜规则修改记录列表*/
	/**根据id查询排行配置*/
	@Select("select * from ranking_rule_history where rule_id=#{ruleId} order by update_time desc ")
    @ResultType(RankingRuleHistory.class)
	List<RankingRuleHistory> selectRankingRuleHistory(@Param("ruleId") Long ruleId);
	
	/**新增排行榜修改记录*/
	@Insert(
         	" INSERT INTO ranking_rule_history " +
                    "(rule_id,org_before,org_after,update_time,update_by) " +
                    " VALUES (#{hisRecord.ruleId},#{hisRecord.orgBefore},#{hisRecord.orgAfter},SYSDATE(),#{hisRecord.updateBy}) "
			)
	int insertRankingRuleHistory(@Param("hisRecord") RankingRuleHistory hisRecord);
}
