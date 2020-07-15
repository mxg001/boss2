package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface LoanInstitutionManagementDao {

    @SelectProvider(type = SqlProvider.class, method = "selectList")
    @ResultType(LoanSource.class)
    List<LoanSource> selectList(@Param("baseInfo") LoanSource baseInfo, @Param("page") Page<LoanSource> page);

    @Select("select ls.* from loan_source ls  where id = #{id}")
    @ResultType(LoanSource.class)
    LoanSource selectDetail(@Param("id") Long id);


    @Insert("insert into loan_source(company_name ,loan_product ,show_order ,status ,register_profit_flag ,loan_profit_flag ,"
    		+ "show_logo ,h5_link ,register_link ,loan_link ,register_bonus ,loan_bonus ,profit_type ,propaganda ,loan_alias ,reward_requirements,loan_status"
    		+ ",source , special_position ,special_image ,settlement_cycle "
    		+ ",settlement_rules ,update_time ,create_time ,rule_code,access_way,api_url)" +
            " values " +
            " (#{companyName},#{loanProduct},#{showOrder},#{status},#{registerProfitFlag},#{loanProfitFlag},#{showLogo},#{h5Link},"
            + "#{registerLink},#{loanLink},#{registerBonus},#{loanBonus},#{profitType},#{propaganda},#{loanAlias},#{rewardRequirements},#{loanStatus}"
            + ",#{source},#{specialPosition},#{specialImage},#{settlementCycle}"
            + ",#{settlementRules},#{updateTime},#{createTime},#{ruleCode},#{accessWay},#{apiUrl})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(LoanSource info);

    @Update("update loan_source set  company_name=#{companyName},loan_product=#{loanProduct},show_order=#{showOrder},status=#{status},"
    		+ "register_profit_flag=#{registerProfitFlag},loan_profit_flag=#{loanProfitFlag},show_logo=#{showLogo},h5_link=#{h5Link},"
    		+ "register_link=#{registerLink},loan_link=#{loanLink},register_bonus=#{registerBonus},loan_bonus=#{loanBonus},profit_type=#{profitType},"
    		+ "propaganda=#{propaganda},loan_alias=#{loanAlias},reward_requirements=#{rewardRequirements},loan_status=#{loanStatus},source=#{source},special_image=#{specialImage},"
    		+ "settlement_cycle=#{settlementCycle},settlement_rules=#{settlementRules},"
    		+ "special_position=#{specialPosition},update_time=#{updateTime},create_time=#{createTime},rule_code=#{ruleCode}, "
            + "access_way=#{accessWay},api_url=#{apiUrl} "+
            " where id = #{id}")
    int updateLoan(LoanSource info);

    @Update("update loan_source set status = #{status} where id = #{id}")
    int updateLoanStatus(LoanSource info);

    @Select("select * from loan_source where loan_alias = #{loanAlias} ")
    @ResultType(LoanSource.class)
    LoanSource selectLoanAliasExists(@Param("loanAlias") String loanAlias);
    
    @Select("select id,rule_code from loan_source where rule_code = #{ruleCode} ")
    @ResultType(LoanSource.class)
    LoanSource selectRuleCodeIdExists(@Param("ruleCode") String ruleCode);

    @Select("select id,show_order from loan_source where show_order = #{showOrder}")
    @ResultType(LoanSource.class)
    LoanSource selectOrderExists(@Param("showOrder")String showOrder);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
        	LoanSource baseInfo = (LoanSource) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("ls.*");
            sql.FROM("loan_source ls");
            if(baseInfo != null){
            	 if(baseInfo.getId()!=null){
                     sql.WHERE("ls.id = #{baseInfo.id}");
                 }
            	 if(StringUtils.isNotBlank(baseInfo.getLoanProduct())){
            		 baseInfo.setLoanProduct(baseInfo.getLoanProduct()+ "%");
                     sql.WHERE("ls.loan_product like #{baseInfo.loanProduct}");
                 }
            	 if(StringUtils.isNotBlank(baseInfo.getLoanAlias())){
            		 baseInfo.setLoanAlias(baseInfo.getLoanAlias()+ "%");
                     sql.WHERE("ls.loan_alias like #{baseInfo.loanAlias}");
                 }
            	 if(StringUtils.isNotBlank(baseInfo.getStatus())){
                     sql.WHERE("ls.status = #{baseInfo.status}");
                 }
                if(StringUtils.isNotBlank(baseInfo.getRewardRequirements())){
                    sql.WHERE("ls.reward_requirements = #{baseInfo.rewardRequirements}");
                }
                if(StringUtils.isNotBlank(baseInfo.getProfitType())){
                    sql.WHERE("ls.profit_type = #{baseInfo.profitType}");
                }
                sql.ORDER_BY("ls.create_time desc");
            }
            return sql.toString();
        }
    }
    
}
