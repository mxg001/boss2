package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.BlacklistAmount;
import cn.eeepay.framework.db.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 金额黑名单 数据层
 * 
 * @author tans
 * @date 2019-08-09
 */
public interface BlacklistAmountDao {

	/**
     * 查询金额黑名单列表
     * 
     * @param baseInfo 金额黑名单信息
     * @return 金额黑名单集合
     */
	@SelectProvider(type = SqlProvider.class, method = "selectPage")
	List<BlacklistAmount> selectPage(@Param("page") Page<BlacklistAmount> page,
                                            @Param("baseInfo") BlacklistAmount baseInfo);
	
	/**
     * 新增金额黑名单
     * 
     * @param baseInfo 金额黑名单信息
     * @return 结果
     */
	@Insert("insert into blacklist_amount(jump_rule_id,amount,operator) values " +
			" (#{jumpRuleId},#{amount},#{operator})")
	int insert(BlacklistAmount baseInfo);
	
	@Delete("delete from blacklist_amount where id = #{id}")
	int delete(@Param("id") Integer id);

	@Select("select count(*) from blacklist_amount where jump_rule_id = #{jumpRuleId} and amount = #{amount}")
	int selectExists(BlacklistAmount baseInfo);

	class SqlProvider{
		public String selectPage(Map<String, Object> param) {
			BlacklistAmount baseInfo = (BlacklistAmount) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("ba.*");
			sql.FROM("blacklist_amount ba");
			if(baseInfo.getJumpRuleId() != null) {
				sql.WHERE("ba.jump_rule_id = #{baseInfo.jumpRuleId}");
			}
			sql.ORDER_BY("ba.id desc");
			return sql.toString();
		}
	}
}