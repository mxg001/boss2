package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExchangeAwardsConfig;
import cn.eeepay.framework.model.ExchangeAwardsRecode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface RedemptionDao {

    @SelectProvider(type=SqlProvider.class,method="queryRedemptionList")
    @ResultType(ExchangeAwardsRecode.class)
    List<ExchangeAwardsRecode> queryRedemptionList(@Param("info") ExchangeAwardsRecode info, @Param("page") Page<ExchangeAwardsRecode> page);

    @Select("SELECT eac.*,cae.cancel_verification_code,cae.activity_first " +
            "from exchange_awards_config eac " +
            "LEFT JOIN coupon_activity_entity cae on eac.coupon_id=cae.id")
    @ResultType(ExchangeAwardsConfig.class)
    List<ExchangeAwardsConfig> queryRedemptionManageList(@Param("info") ExchangeAwardsConfig info);

    @Insert("INSERT INTO exchange_awards_config " +
            " (func_code,award_desc,award_name,coupon_id,effect_days," +
            "  award_hit,money,award_pic,award_type,status,create_time,operator)  VALUES " +
            " (#{info.funcCode},#{info.awardName},#{info.awardName},#{info.couponId},#{info.effectDays}," +
            "  #{info.awardHit},#{info.money},#{info.awardPic},#{info.awardType},1,NOW(),#{info.operator}) ")
    int addRedemptionManage(@Param("info") ExchangeAwardsConfig info);

    @Update("update exchange_awards_config set award_desc=#{info.awardName},award_name=#{info.awardName}," +
            "effect_days=#{info.effectDays},money=#{info.money},status=1,operator=#{info.operator} " +
            "where id=#{info.id}")
    int updateRedemptionManage(@Param("info") ExchangeAwardsConfig info);

    @Delete("DELETE FROM exchange_awards_config WHERE id=#{id}")
    int deleteRedemptionManage(@Param("id") Integer id);

    @Insert("INSERT INTO exchange_awards_recode " +
                    " (awards_config_id,exc_code,award_desc,award_name,status," +
                    "  off_begin_time,off_end_time,award_type)  VALUES " +
                    " (#{info.awardsConfigId},#{info.excCode},#{ info.awardName},#{info.awardName},0," +
                    "  #{ info.offBeginTime},#{ info.offEndTime},#{ info.awardType}) ")
    int addRedemption(@Param("info") ExchangeAwardsRecode info);

    @Select("SELECT * from exchange_awards_config where id=#{id}")
    @ResultType(ExchangeAwardsConfig.class)
    ExchangeAwardsConfig queryRedemptionManageById(@Param("id") Integer id);

    @Update("update exchange_awards_recode set status=3 where id=#{id}")
    int updateRedemptionStatus(@Param("id") Integer id);

    @Update("update exchange_awards_recode set status=2 where status=0 and off_end_time<NOW()")
    int updateRedemptionExpired();

    class SqlProvider {

        public String queryRedemptionList(final Map<String, Object> param) {
            final ExchangeAwardsRecode info = (ExchangeAwardsRecode) param.get("info");
            SQL sql = new SQL() {{
                SELECT("ear.*");
                FROM("exchange_awards_recode ear");
            }};
            where(sql, info);
            sql.ORDER_BY("ear.create_time DESC");
            return sql.toString();
        }

        public void where(SQL sql, ExchangeAwardsRecode info) {
            if(StringUtils.isNotBlank(info.getExcCode())){
                sql.WHERE("ear.exc_code = #{info.excCode} ");
            }
            if(info.getStatus()!=null){
                sql.WHERE("ear.status = #{info.status} ");
            }
            if(info.getAwardType()!=null){
                sql.WHERE("ear.award_type = #{info.awardType} ");
            }
            if(StringUtils.isNotBlank(info.getStartTime())){
                sql.WHERE("ear.off_begin_time  >= #{info.startTime} ");
            }
            if(StringUtils.isNotBlank(info.getEndTime())){
                sql.WHERE("ear.off_end_time  <= #{info.endTime} ");
            }
            if(StringUtils.isNotBlank(info.getAwardStartTime())){
                sql.WHERE("ear.award_time  >= #{info.awardStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getAwardEndTime())){
                sql.WHERE("ear.award_time  <= #{info.awardEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getCreateStartTime())){
                sql.WHERE("ear.create_time  >= #{info.createStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getCreateEndTime())){
                sql.WHERE("ear.create_time  <= #{info.createEndTime} ");
            }
        }
    }
}
