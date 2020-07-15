package cn.eeepay.framework.dao.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.LuckDrawEntry;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/7/007.
 * @author  抽奖记录dao
 */
public interface LuckDrawOrderDao {

    @SelectProvider(type=LuckDrawOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(LuckDrawOrder.class)
    List<LuckDrawOrder> selectAllList(@Param("order") LuckDrawOrder order,@Param("page") Page<LuckDrawOrder> page);

    @SelectProvider(type=LuckDrawOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(LuckDrawOrder.class)
    List<LuckDrawOrder> importDetailSelect(@Param("order")LuckDrawOrder order);

    @SelectProvider(type=LuckDrawOrderDao.SqlProvider.class,method="sumAwardsConfigId")
    @ResultType(Integer.class)
    Integer sumAwardsConfigId(@Param("order")LuckDrawOrder order);

    @Select(
            "select awa.*,mer.merchant_name userName from awards_recode awa " +
                    " LEFT JOIN merchant_info mer ON mer.merchant_no=awa.user_code" +
                    " where awa.id=#{id}"
    )
    LuckDrawOrder getLuckDrawOrder(@Param("id")int id);

    @Select(
            "select awa.*,cou.coupon_name from awards_item_log awa " +
                    " LEFT JOIN coupon_activity_entity cou ON cou.id=awa.coupon_id " +
                    " where awa.awards_recode_id=#{id}"
    )
    List<LuckDrawEntry> getLuckDrawEntry(@Param("id")int id);

    class SqlProvider{
        public String sumAwardsConfigId(final Map<String, Object> param) {
            LuckDrawOrder order = (LuckDrawOrder) param.get("order");
            StringBuffer sb=new StringBuffer();
            sb.append(" select count(*)");
            sb.append(" from awards_recode");
            sb.append(" where status in (2,3) ");
            if(order.getAwardsConfigId()!=null){
                sb.append(" and awards_config_id = #{order.awardsConfigId} ");
            }
            if(order.getPlayTimeBegin() != null){
                sb.append(" and play_time >= #{order.playTimeBegin}");
            }
            if(order.getPlayTimeEnd() != null){
                sb.append(" and play_time <= #{order.playTimeEnd}");
            }
            return sb.toString();
        }

        public String selectAllList(final Map<String, Object> param) {
            final LuckDrawOrder order = (LuckDrawOrder) param.get("order");
            return new SQL(){{
                SELECT("awa.*,mer.merchant_name userName ");
                FROM("awards_recode awa");
                LEFT_OUTER_JOIN(" merchant_info mer ON mer.merchant_no=awa.user_code ");
                if(StringUtils.isNotBlank(order.getUserCode())){
                    WHERE("awa.user_code = #{order.userCode} ");
                }
                if(StringUtils.isNotBlank(order.getAwardName())){
                    WHERE("awa.award_name like CONCAT('%',#{order.awardName},'%') ");
                }
                if(order.getStatus()!=null&&order.getStatus().intValue()>=0){
                    WHERE("awa.status = #{order.status} ");
                }
                if(order.getPlayTimeBegin() != null){
                    WHERE("awa.play_time >= #{order.playTimeBegin}");
                }
                if(order.getPlayTimeEnd() != null){
                    WHERE("awa.play_time <= #{order.playTimeEnd}");
                }
                if(StringUtils.isNotBlank(order.getMobilephone())){
                    WHERE("awa.mobilephone = #{order.mobilephone} ");
                }
                if(order.getAwardsConfigId()!=null){
                    WHERE("awa.awards_config_id = #{order.awardsConfigId} ");
                }
                ORDER_BY("awa.play_time desc");
            }}.toString();
        }
    }
}
