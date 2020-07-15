package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateBanner;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * bannerDao
 */
public interface ExchangeActivateBannerDao {

    @SelectProvider(type=ExchangeActivateBannerDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateBanner.class)
    List<ExchangeActivateBanner> selectAllList(@Param("banner") ExchangeActivateBanner banner, @Param("page") Page<ExchangeActivateBanner> page);

    @Insert(
            "INSERT INTO yfb_banner " +
                    " (apply_type,oem_no,banner_name,up_time,down_time," +
                    "  show_no,status,img_url,link,remark) " +
                    " VALUES " +
                    " (#{banner.applyType},#{banner.oemNo},#{banner.bannerName},#{banner.upTime},#{banner.downTime}," +
                    "  #{banner.showNo},'0',#{banner.imgUrl},#{banner.link},#{banner.remark} ) "
    )
    int addBanner(@Param("banner") ExchangeActivateBanner banner);

    @Select(
            "select * from yfb_banner where id=#{id}"
    )
    ExchangeActivateBanner getBanner(@Param("id") long id);

    @Update(
            "update yfb_banner set " +
                    " apply_type=#{banner.applyType},oem_no=#{banner.oemNo},banner_name=#{banner.bannerName}, " +
                    " up_time=#{banner.upTime},down_time=#{banner.downTime},show_no=#{banner.showNo}, " +
                    " img_url=#{banner.imgUrl},link=#{banner.link},remark=#{banner.remark} " +
                    " where id=#{banner.id}"
    )
    int updateBanner(@Param("banner") ExchangeActivateBanner banner);

    @Update(
            "update yfb_banner set " +
                    " apply_type=#{banner.applyType},oem_no=#{banner.oemNo},banner_name=#{banner.bannerName}, " +
                    " up_time=#{banner.upTime},down_time=#{banner.downTime},show_no=#{banner.showNo}, " +
                    " link=#{banner.link},remark=#{banner.remark} " +
                    " where id=#{banner.id}"
    )
    int updateBannerNoImg(@Param("banner") ExchangeActivateBanner banner);

    @Update(
            "update yfb_banner set status='2' where id=#{id}"
    )
    int deleteBanner(@Param("id") long id);

    @Update(
            "update yfb_banner set status=#{state} where id=#{id}"
    )
    int closeBanner(@Param("id") long id, @Param("state") String state);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateBanner banner = (ExchangeActivateBanner) param.get("banner");
            return new SQL(){{
                SELECT("banner.*,oem.oem_name");
                FROM("yfb_banner banner");
                LEFT_OUTER_JOIN("yfb_oem_service oem ON oem.oem_no=banner.oem_no");
                WHERE("banner.status!='2'");
                if(StringUtils.isNotBlank(banner.getOemNo())){
                    WHERE("banner.oem_no = #{banner.oemNo} ");
                }
                if(StringUtils.isNotBlank(banner.getApplyType())){
                    WHERE("banner.apply_type = #{banner.applyType} ");
                }
                if(StringUtils.isNotBlank(banner.getBannerName())){
                    WHERE("banner.banner_name like concat(#{banner.bannerName},'%') ");
                }
                if(banner.getId()!=null){
                    WHERE("banner.id = #{banner.id} ");
                }
                if(StringUtils.isNotBlank(banner.getStatus())){
                    WHERE("banner.status = #{banner.status} ");
                }
            }}.toString();
        }
    }
}
