package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceBonusConf;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface InsuranceBonusDao {

    @Update("update insurance_bonus_conf set total_bonus=#{totalBonus},company_bonus=#{companyBonus},org_bonus=#{orgBonus}," +
            " update_by=#{updateBy}, update_date=sysdate()" +
            " where id=#{id}")
    int updInsuranceBonusConf(InsuranceBonusConf insuranceBonusConf);

    @Insert(" insert into insurance_bonus_conf (id, org_id, company_no, " +
            "      product_id, total_bonus, company_bonus," +
            "      status, org_bonus, profit_type, " +
            "      create_by, update_by, create_date, " +
            "      update_date)" +
            "    values (#{id,jdbcType=INTEGER}, #{orgId,jdbcType=BIGINT}, #{companyNo,jdbcType=INTEGER}, " +
            "      #{productId,jdbcType=INTEGER}, #{totalBonus,jdbcType=VARCHAR}, #{companyBonus,jdbcType=VARCHAR}, " +
            "      #{status,jdbcType=INTEGER}, #{orgBonus,jdbcType=VARCHAR}, #{profitType,jdbcType=VARCHAR}, " +
            "      #{createBy,jdbcType=VARCHAR}, #{updateBy,jdbcType=VARCHAR}, #{createDate,jdbcType=TIMESTAMP}, " +
            "      #{updateDate,jdbcType=TIMESTAMP})")
    int addInsuranceBonusConf(InsuranceBonusConf insuranceBonusConf);


    @SelectProvider(type = InsuranceBonusDao.SqlProvider.class, method = "selectList")
    @ResultType(InsuranceBonusConf.class)
    List<InsuranceBonusConf> selectList(@Param("baseInfo") InsuranceBonusConf baseInfo, @Param("page") Page<InsuranceBonusConf> page);

    @Select("SELECT count(1) from insurance_bonus_conf where  product_id=#{productId} AND org_id !=0")
    int selectByProductId(int productId);

    @Select("SELECT * from insurance_bonus_conf where  company_no=#{companyNo}")
    List<InsuranceBonusConf> getByCompanyNo(int companyNo);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            InsuranceBonusConf baseInfo = (InsuranceBonusConf) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("ibc.*,ip.product_name,ic.company_nick_name,oi.org_name");
            sql.FROM("insurance_bonus_conf ibc");
            sql.LEFT_OUTER_JOIN("insurance_company ic on ic.company_no = ibc.company_no");
            sql.LEFT_OUTER_JOIN("insurance_product ip on ip.product_id = ibc.product_id");
            sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id = ibc.org_id");
            if(baseInfo != null){
                if(baseInfo.getOrgId()!=null&&baseInfo.getOrgId()!=-1){
                    sql.WHERE("ibc.org_id = #{baseInfo.orgId}");
                }
                if(baseInfo.getCompanyNo()!=null&&baseInfo.getCompanyNo()!=-1){
                    sql.WHERE("ibc.company_no = #{baseInfo.companyNo}");
                }
                if(baseInfo.getProductId()!=null&&baseInfo.getProductId()!=-1){
                    sql.WHERE("ibc.product_id = #{baseInfo.productId}");
                }
            }
            sql.ORDER_BY("ibc.create_date desc");
            return sql.toString();
        }
    }

}
