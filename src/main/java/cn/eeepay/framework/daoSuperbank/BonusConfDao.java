package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BonusConf;
import cn.eeepay.framework.util.StringUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface BonusConfDao {

    @Update("update bonus_conf set total_bonus=#{totalBonus},company_bonus=#{companyBonus},org_bonus=#{orgBonus}," +
            " update_by=#{updateBy}, update_date=sysdate()" +
            " where id=#{id}")
    int updBonusConf(BonusConf BonusConf);

    @Insert(" insert into bonus_conf (id, org_id, " +
            "  total_bonus, company_bonus," +
            " reward_claim, org_bonus, profit_type, type," +
            "  create_by,update_by, create_date, " +
            " update_date,share_rate)" +
            " values(#{id}, #{orgId}, " +
            " #{totalBonus}, #{companyBonus}, " +
            " #{rewardClaim}, #{orgBonus}, #{profitType}, #{type}, " +
            " #{createBy}, #{updateBy}, #{createDate}, " +
            " #{updateDate},#{shareRate})")
    int addBonusConf(BonusConf BonusConf);


    @SelectProvider(type = BonusConfDao.SqlProvider.class, method = "selectList")
    @ResultType(BonusConf.class)
    List<BonusConf> selectList(@Param("baseInfo") BonusConf baseInfo, @Param("page") Page<BonusConf> page);

    @Select("SELECT count(1) from bonus_conf where  org_id=#{orgId} ")
    int checkOrgExist(long orgId);

    @Select("SELECT * from bonus_conf where type=#{type}")
    List<BonusConf> getByType(String type);

    class SqlProvider{
        public String selectList(Map<String, Object> param){
            BonusConf baseInfo = (BonusConf) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("bc.id,bc.org_id,bc.agency_alias,bc.total_bonus,bc.company_bonus,bc.reward_claim," +
                    "bc.org_bonus,bc.profit_type,bc.type,bc.create_by,bc.update_by,bc.create_date,bc.update_date,oi.org_name");
            sql.FROM("bonus_conf bc");
            sql.LEFT_OUTER_JOIN("org_info oi on oi.org_id = bc.org_id");
            if(baseInfo != null){
                if(baseInfo.getOrgId()!=null&&baseInfo.getOrgId()!=-1){
                    sql.WHERE("bc.org_id = #{baseInfo.orgId}");
                }
                if(!StringUtil.isBlank(baseInfo.getAgencyAlias())&&!"全部".equals(baseInfo.getAgencyAlias())){
                    sql.WHERE("bc.agency_alias = #{baseInfo.agencyAlias}");
                }
                if(!StringUtil.isBlank(baseInfo.getType())){
                    sql.WHERE("bc.type = #{baseInfo.type}");
                }
            }
            sql.ORDER_BY("bc.create_date desc");
            return sql.toString();
        }
    }

}
