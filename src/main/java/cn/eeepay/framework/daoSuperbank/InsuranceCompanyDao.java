package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceCompany;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface InsuranceCompanyDao {

    @SelectProvider(type = SqlProvider.class, method = "selectList")
    @ResultType(InsuranceCompany.class)
    List<InsuranceCompany> selectList(@Param("baseInfo") InsuranceCompany baseInfo, @Param("page") Page<InsuranceCompany> page);

    @Select("select ic.* from insurance_company ic where ic.company_no = #{companyNo}")
    @ResultType(InsuranceCompany.class)
    InsuranceCompany selectDetail(@Param("companyNo") Long companyNo);


    @Insert("insert into insurance_company (company_no, company_name, company_nick_name," +
            "source, show_logo ,show_order , create_order_type, " +
            " rule_code, share_rule_remark, status, " +
            " create_by, update_by, create_date," +
            " update_date)" +
            "  values (" +
            "#{companyNo}, #{companyName}, #{companyNickName}, " +
            "#{source}, #{showLogo}, #{showOrder}, #{createOrderType}" +
            ", #{ruleCode}, #{shareRuleRemark}, #{status}, " +
            "#{createBy}, #{updateBy}, #{createDate}," +
            " #{updateDate})")
    int insert(InsuranceCompany info);

    @Update(" update insurance_company" +
            "    set company_name = #{companyName}," +
            "      company_nick_name = #{companyNickName}," +
            "      source = #{source}," +
            "      show_logo = #{showLogo}," +
            "      show_order = #{showOrder}," +
            "      create_order_type = #{createOrderType}," +
            "      rule_code = #{ruleCode}," +
            "      share_rule_remark = #{shareRuleRemark}," +
            "      status = #{status}," +
            "      create_by = #{createBy}," +
            "      update_by = #{updateBy}," +
            "      create_date = #{createDate}," +
            "      update_date = #{updateDate}" +
            "    where company_no = #{companyNo}")
    int updateCompany(InsuranceCompany info);

    @Update("update insurance_company set status = #{status},update_by=#{updateBy},update_date=#{updateDate} where company_no = #{companyNo}")
    int updateCompanyStatus(InsuranceCompany info);

    @Select("select count(1) from insurance_company where company_nick_name = #{companyNickName}")
    int selectNickExists(@Param("companyNickName") String companyNickName);


    @Select("select count(1) from insurance_company where company_nick_name = #{companyNickName} and company_no <> #{companyNo}")
    int selectNickIdExists(@Param("companyNickName") String companyNickName, @Param("companyNo") Long companyNo);

    @Select("select count(1) from insurance_company where show_order = #{showOrder}")
    int selectOrderExists(@Param("showOrder") Integer showOrder);

    @Select("select count(1) from insurance_company where show_order = #{showOrder} and company_no <> #{companyNo}")
    int selectOrderIdExists(@Param("showOrder") Integer showOrder, @Param("companyNo") Long companyNo);

    @Select("select * from insurance_company where company_no = #{companyNo}")
    InsuranceCompany selectByCompanyNo(@Param("companyNo") long companyNo);

    @Select("select ic.* from insurance_company ic ")
    @ResultType(InsuranceCompany.class)
    List<InsuranceCompany> getCompanyList();

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            InsuranceCompany baseInfo = (InsuranceCompany) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("ic.*");
            sql.FROM("insurance_company ic");
            if(baseInfo != null){
                if(StringUtils.isNotBlank(baseInfo.getCompanyName())){
                    sql.WHERE("ic.company_name = #{baseInfo.companyName}");
                }
                if(StringUtils.isNotBlank(baseInfo.getCompanyNickName())){
                    baseInfo.setCompanyNickName(baseInfo.getCompanyNickName() + "%");
                    sql.WHERE("ic.company_nick_name like #{baseInfo.companyNickName}");
                }
                if(baseInfo.getStatus()!=null){
                    sql.WHERE("ic.status = #{baseInfo.status}");
                }
                if(baseInfo.getCompanyNo() != null){
                    sql.WHERE("ic.company_no = #{baseInfo.companyNo}");
                }
                if(StringUtils.isNotBlank(baseInfo.getSource())){
                    sql.WHERE("ic.source = #{baseInfo.source}");
                }
            }
            sql.ORDER_BY("ic.create_date desc");
            return sql.toString();
        }
    }
    
}
