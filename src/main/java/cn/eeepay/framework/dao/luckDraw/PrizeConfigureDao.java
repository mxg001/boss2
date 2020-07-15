package cn.eeepay.framework.dao.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CouponActivityEntity;
import cn.eeepay.framework.model.luckDraw.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6/006.
 * @author liuks
 * 奖项配置dao
 */
public interface PrizeConfigureDao {

    @Select(
            "select * from luck_config where func_code=#{funcCode}"
    )
    PrizeConfigure getPrizeConfigure(@Param("funcCode") String funcCode);


    @Update(
            "update luck_config set " +
                    " func_name=#{pc.funcName},func_desc=#{pc.funcDesc}," +
                    " start_time=#{pc.startTime},end_time=#{pc.endTime}," +
                    " warn=#{pc.warn},amount=#{pc.amount},operator=#{pc.operator}" +
                    " where func_code=#{pc.funcCode}"
    )
    int updatePrizeConfigure(@Param("pc") PrizeConfigure pc);

    @Update(
            "update coupon_activity_info set activity_explain=#{pc.funcName}" +
                    " where activetiy_code=#{pc.funcCode}"
    )
    int updateCouponActivityInfo(@Param("pc")PrizeConfigure pc);

    @Select(
            "select * from awards_config where func_code=#{funcCode} and status!=2 order by sort asc,create_time asc"
    )
    List<Prize> getPrizeList(@Param("funcCode") String funcCode);

    @Select(
            "select * from awards_config where id=#{id}"
    )
    Prize getPrize(@Param("id")int id);

    @Select(
            "select item.*,cou.coupon_name from awards_item item " +
                    " LEFT JOIN coupon_activity_entity cou ON cou.id=item.coupon_id " +
                    " where item.award_config_id=#{awId}"
    )
    List<PrizeDetail> getPrizeDetailList(@Param("awId") Integer awId);

    @SelectProvider(type=PrizeConfigureDao.SqlProvider.class,method="sumPrize")
    @ResultType(BigDecimal.class)
    BigDecimal checkPrize(@Param("prize")Prize prize);

    /**
     *新增奖项
     */
    @Insert(
            "INSERT INTO awards_config " +
                    " (func_code,award_desc,award_name,award_count," +
                    "  prob,sort,day_count,award_hit,award_pic," +
                    "  award_type,create_time,operator,mer_day_count)" +
                    "VALUES " +
                    "(#{prize.funcCode},#{prize.awardDesc},#{prize.awardName},#{prize.awardCount}," +
                    " #{prize.prob},#{prize.sort},#{prize.dayCount},#{prize.awardHit},#{prize.awardPic}," +
                    " #{prize.awardType},NOW(),#{prize.operator},#{prize.merDayCount})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "prize.id")
    int addPrize(@Param("prize")Prize prize);


    /**
     *修改奖项
     */

    @UpdateProvider(type=PrizeConfigureDao.SqlProvider.class,method="updatePrize")
    int updatePrize(@Param("prize")Prize prize);

    /**
     *开启/关闭奖项
     */
    @Update(
            "update awards_config set status=#{sta} where id=#{id}"
    )
    int updatePrizeStatus(@Param("id")int id,@Param("sta")int sta);

    /**
     *查询券
     */
    @Select(
            "select * from coupon_activity_entity where activetiy_code =#{funcCode}"
    )
    List<CouponActivityEntity> getCouponList(@Param("funcCode")String funcCode);

    /**
     * 新增前先删除
     */
    @Delete(
            "delete from awards_item where award_config_id=#{prize.id}"
    )
    int deletePrizeDetail(@Param("prize")Prize prize);


    @Insert(
         "INSERT INTO awards_item(award_config_id,coupon_id,item_count,money)" +
                 " VALUES " +
                 "(#{prize.id},#{item.couponId},#{item.itemCount},#{item.money})"
    )
    int insertPrizeDetail(@Param("prize")Prize prize,@Param("item") PrizeDetail item);


    /**
     * 开启/关闭奖项状态
     * @param id
     * @param status
     * @return
     */
    @Update(
            "update awards_config set status=#{status} where id=#{id}"
    )
    int closeOpenPrize(@Param("id")int id,@Param("status") int status);

    @SelectProvider(type=PrizeConfigureDao.SqlProvider.class,method="getPrizeBlacklist")
    @ResultType(LuckDrawOrder.class)
    List<PrizeBlacklist> getPrizeBlacklist(@Param("info")PrizeBlacklist blacklist,@Param("page") Page<PrizeBlacklist> page);

    @Insert(
            "INSERT INTO awards_config_blacklist " +
                    " (award_config_id,merchant_no,creater,create_time) " +
                    " VALUES " +
                    " (#{info.awardConfigId},#{info.merchantNo},#{info.creater},NOW())"
    )
    int addPrizeBlacklist(@Param("info")PrizeBlacklist blacklist);

    @Delete(
            "delete from awards_config_blacklist where id=#{id}"
    )
    int deleteBlacklist(@Param("id")int id);

    @Select(
            "select * from awards_config_blacklist " +
                    "where award_config_id=#{info.awardConfigId} and merchant_no=#{info.merchantNo}"
    )
    PrizeBlacklist checkBlacklist(@Param("info")PrizeBlacklist blacklist);

    /**
     * 初始化兑奖次数
     * @return
     */
    @Update(
            " UPDATE awards_config SET day_lottery_count = 0 "
    )
    int initializationAwardsConfigCount();

    class SqlProvider{

        public String getPrizeBlacklist(final Map<String, Object> param) {
            final PrizeBlacklist info = (PrizeBlacklist) param.get("info");
            return new SQL(){{
                SELECT("blacklist.*,mer.merchant_name merchantName ");
                FROM("awards_config_blacklist blacklist");
                LEFT_OUTER_JOIN(" merchant_info mer ON mer.merchant_no=blacklist.merchant_no ");
                if(StringUtils.isNotBlank(info.getMerchantNo())){
                    WHERE("blacklist.merchant_no = #{info.merchantNo} ");
                }
                if(info.getAwardConfigId()!=null){
                    WHERE("blacklist.award_config_id = #{info.awardConfigId} ");
                }
                ORDER_BY("blacklist.create_time desc");
            }}.toString();
        }

        public String updatePrize(final Map<String, Object> param) {
            Prize prize = (Prize) param.get("prize");
            StringBuffer sb=new StringBuffer();
            sb.append("update awards_config set ");
            sb.append(" award_desc=#{prize.awardDesc},award_name=#{prize.awardName},award_count=#{prize.awardCount}, ");
            sb.append(" prob=#{prize.prob},sort=#{prize.sort},day_count=#{prize.dayCount},award_hit=#{prize.awardHit}, ");
            if(StringUtils.isNotBlank(prize.getAwardPic())){
                sb.append(" award_pic=#{prize.awardPic}, ");
            }
            sb.append(" award_type=#{prize.awardType},operator=#{prize.operator},mer_day_count=#{prize.merDayCount} ");
            sb.append(" where id=#{prize.id}");
            return  sb.toString();
        }
        public String sumPrize(final Map<String, Object> param) {
            final Prize prize = (Prize) param.get("prize");
            return new SQL(){{
                SELECT("sum(prob) probTotal");
                FROM("awards_config");
                WHERE("status!=2 ");
                if (prize.getId()!=null){
                    WHERE("id!=#{prize.id}");
                }
            }}.toString();
        }
    }
}
