package cn.eeepay.framework.daoExchange.exchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExcRouteGood;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 渠道商品dao
 */
public interface ExcRouteGoodDao {

    @SelectProvider(type=ExcRouteGoodDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExcRouteGood.class)
    List<ExcRouteGood> selectAllList(@Param("good") ExcRouteGood good, @Param("page") Page<ExcRouteGood> page);

    @SelectProvider(type=ExcRouteGoodDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExcRouteGood.class)
    List<ExcRouteGood> importDetailSelect(@Param("good") ExcRouteGood good);

    @Insert(
            "INSERT INTO rdmp_pass_good " +
                    " (channel_no,good_type_no,channel_good_no,channel_good_name," +
                    "  good_content_id,good_mode,p_id,channel_price,create_time) " +
                    " VALUES " +
                    " (#{good.channelNo},#{good.goodTypeNo},#{good.channelGoodNo},#{good.channelGoodName}," +
                    "  #{good.goodContentId},#{good.goodMode},#{good.pId},#{good.channelPrice},NOW() ) "
    )
    int addExcRouteGood(@Param("good") ExcRouteGood good);

    @Update(
            "update rdmp_pass_good set " +
                    " good_type_no=#{good.goodTypeNo},channel_good_no=#{good.channelGoodNo}," +
                    " channel_good_name=#{good.channelGoodName},good_content_id=#{good.goodContentId}," +
                    " good_mode=#{good.goodMode},p_id=#{good.pId},channel_price=#{good.channelPrice}" +
                    " where id=#{good.id}"
    )
    int updateExcRouteGood(@Param("good") ExcRouteGood good);

    @Select(
            "select * from rdmp_pass_good where channel_no=#{good.channelNo} and p_id=#{good.pId}"
    )
    List<ExcRouteGood> getGoodList(@Param("good") ExcRouteGood good);

    @Select(
            "select * from rdmp_pass_good where channel_no=#{good.channelNo} and p_id=#{good.pId} and id!=#{good.id}"
    )
    List<ExcRouteGood> getGoodListById(@Param("good") ExcRouteGood good);

    @Select(
            "select * from rdmp_pass_good where id=#{id}"
    )
    ExcRouteGood getRouteGood(@Param("id") int id);

    @Update(
            "update rdmp_pass_good set status=#{state} where id=#{id}"
    )
    int closeGood(@Param("id") int id, @Param("state") String state);

    @Delete(
            "delete from rdmp_pass_good where id=#{id}"
    )
    int deleteGood(@Param("id") int id);

    @Select(
            "select * from rdmp_pass_good where " +
                    " channel_no=#{good.channelNo} " +
                    " and p_id=#{good.pId} and channel_price!=#{good.channelPrice}"
    )
    ExcRouteGood getCheckRouteGood(@Param("good") ExcRouteGood good);

    @Update(
            "update rdmp_pass_good set channel_price=#{good.channelPrice} " +
                    " where channel_no=#{good.channelNo} and p_id=#{good.pId} "
    )
    int updateCheckRouteGood(@Param("good") ExcRouteGood good);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExcRouteGood good = (ExcRouteGood) param.get("good");
            return new SQL(){{
                SELECT(" good.*,route.channel_name,product.product_name pName ");
                FROM("rdmp_pass_good good ");
                INNER_JOIN("rdmp_pass_route route ON route.channel_no=good.channel_no");
                LEFT_OUTER_JOIN("rdmp_product_info product ON product.id=good.p_id");
                if(StringUtils.isNotBlank(good.getChannelNo())){
                    WHERE("good.channel_no = #{good.channelNo} ");
                }
                if(StringUtils.isNotBlank(good.getChannelName())){
                    WHERE("route.channel_name like concat(#{good.channelName},'%') ");
                }
                if(StringUtils.isNotBlank(good.getChannelGoodNo())){
                    WHERE("good.channel_good_no = #{good.channelGoodNo} ");
                }
                if(good.getpId()!=null){
                    WHERE("good.p_id=#{good.pId}");
                }
                ORDER_BY("good.create_time desc");
            }}.toString();
        }
    }
}
