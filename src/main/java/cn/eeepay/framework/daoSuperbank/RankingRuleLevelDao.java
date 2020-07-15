package cn.eeepay.framework.daoSuperbank;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.RankingRuleLevel;

public interface RankingRuleLevelDao {
	/**查询排行榜规则等级配置列表*/
	
	@Select("select * from ranking_rule_level where rule_id = #{id} order by level_num ")
    @ResultType(RankingRuleLevel.class)
	List<RankingRuleLevel> selectRankingRuleLevelByRuleId(@Param("id") Long id);
	
	
	
	@InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(@Param("list") List<RankingRuleLevel> list, @Param("ruleId") Long ruleId);
	
	class SqlProvider{
        public String insertBatch(Map<String, Object> param){
            List<RankingRuleLevel> list = (List<RankingRuleLevel>) param.get("list");
            if(list == null || list.size() < 1){
                return "";
            }
            StringBuilder values = new StringBuilder();
            MessageFormat message = new MessageFormat("(#'{'ruleId},#'{'list[{0}].levelNum}" +
                    ", #'{'list[{0}].level}, #'{'list[{0}].prizePeopleCount},#'{'list[{0}].singlePrize}),");
            for(int i = 0; i < list.size(); i++){
                values.append(message.format(new Integer[]{i}));
            }
            final String valuesSql  = values.substring(1, values.length() - 2);//去掉最前面那个括号,和最后面的,)
            SQL sql = new SQL();
            sql.INSERT_INTO("ranking_rule_level");
            sql.VALUES("rule_id,level_num,level,prize_people_count,single_prize" ,valuesSql);
            System.out.println(sql.toString());
            return sql.toString();
        }
    }
	
	@Delete(" delete from ranking_rule_level where rule_id=#{id}")
    int deleteRankingRuleLevelByRuleId(@Param("id") Long id);
}
