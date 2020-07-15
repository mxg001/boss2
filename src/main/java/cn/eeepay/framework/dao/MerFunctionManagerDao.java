package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.FunctionMerchant;
import cn.eeepay.framework.model.MerchantInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import java.util.List;
import java.util.Map;

/**
 * 商户黑名单功能管理
 * 
 * @author Administrator
 *
 */
public interface MerFunctionManagerDao {

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(FunctionMerchant.class)
	List<FunctionMerchant> selectByParam(@Param("functionMerchant") FunctionMerchant functionMerchant,
                                             @Param("page") Page<FunctionMerchant> page);

	@SelectProvider(type = SqlProvider.class, method = "selectByParam")
	@ResultType(FunctionMerchant.class)
	List<FunctionMerchant> exportConfig(@Param("functionMerchant") FunctionMerchant functionMerchant);

	@Insert("Insert into function_merchant(merchant_no,type,create_time,operator,function_number) "
			+ "values(#{functionMerchant.merchantNo},#{functionMerchant.type},"
			+ "now(),#{functionMerchant.operator},"
			+ "#{functionMerchant.functionNumber})")
	int addFunctionMerchant(@Param("functionMerchant") FunctionMerchant functionMerchant);

	@Select("select * from merchant_info where merchant_no=#{merNo}")
	@ResultType(MerchantInfo.class)
	MerchantInfo findMerInfoByMerNo(String merNo);

	@Select("select count(1) from function_merchant where merchant_no = #{merchantNo} and function_number=#{functionNumber}")
	@ResultType(Integer.class)
    int selectExists(FunctionMerchant functionMerchant);

    public class SqlProvider {

		public String selectByParam(final Map<String, Object> param) {
			final FunctionMerchant functionMerchant = (FunctionMerchant) param.get("functionMerchant");
			SQL sql= new SQL() {
				{
					SELECT("fm.*,mi.merchant_name,ti.team_name,tie.team_entry_name");
					FROM(" function_merchant fm");
					LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no=fm.merchant_no");
					LEFT_OUTER_JOIN("team_info ti on mi.team_id=ti.team_id");
					LEFT_OUTER_JOIN("team_info_entry tie on mi.team_entry_id=tie.team_entry_id");
					//添加查询条件
					WHERE("fm.function_number=#{functionMerchant.functionNumber} ");
					if (StringUtils.isNotBlank(functionMerchant.getMerchantNo())) {
						WHERE("fm.merchant_no=#{functionMerchant.merchantNo} ");
					}
					if (StringUtils.isNotBlank(functionMerchant.getMerchantName())) {
						functionMerchant.setMerchantName("%" + functionMerchant.getMerchantName() + "%");
						WHERE("mi.merchant_name like #{functionMerchant.merchantName} ");
					}
					if (StringUtils.isNotBlank(functionMerchant.getTeamId())) {
						WHERE("mi.team_id=#{functionMerchant.teamId} ");
					}

				}
			};
			return sql.toString();
		}

	}

    @Select("select * from function_merchant where id = #{id}")
	FunctionMerchant selectById(Integer id);

    @Delete(" delete from function_merchant where id = #{id} ")
	int delete(Integer id);

	@Delete("delete from function_merchant where merchant_no=#{merchantNo} and function_number=#{functionNumber} ")
	int deleteInfo(@Param("merchantNo") String merchantNo, @Param("functionNumber") String functionNumber);



}