package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActProductMedia;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/11/011.
 * @author  liuks
 * 商品媒体资源dao
 */
public interface ExcActProductMediaDao {

    @SelectProvider(type=ExcActProductMediaDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExcActProductMedia.class)
    List<ExcActProductMedia> selectAllList(@Param("media")ExcActProductMedia media,@Param("page") Page<ExcActProductMedia> page);

    @Delete(
            "delete from act_product_media where id=#{id}"
    )
    int deleteMedia(@Param("id")int id);

    @Select(
            "select * from act_product_media where id=#{id}"
    )
    ExcActProductMedia getProductMedia(@Param("id")int id);

    @Insert(
            "INSERT INTO act_product_media" +
                    "(channel_no,good_type_no,channel_good_no,channel_good_name," +
                    " message,good_file,create_time)" +
                    "VALUES" +
                    "(#{media.channelNo},#{media.goodTypeNo},#{media.channelGoodNo},#{media.channelGoodName}," +
                    " #{media.message},#{media.goodFile},NOW())"
    )
    int addProductMedia(@Param("media")ExcActProductMedia media);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExcActProductMedia media = (ExcActProductMedia) param.get("media");
            return new SQL(){{
                SELECT("media.*,route.channel_name ");
                FROM("act_product_media media");
                LEFT_OUTER_JOIN("act_pass_route route ON route.channel_no=media.channel_no");
                if(StringUtils.isNotBlank(media.getChannelNo())){
                    WHERE("media.channel_no = #{media.channelNo} ");
                }
                if(StringUtils.isNotBlank(media.getChannelGoodNo())){
                    WHERE("media.channel_good_no = #{media.channelGoodNo} ");
                }
                ORDER_BY("media.create_time desc");
            }}.toString();
        }
    }
}
