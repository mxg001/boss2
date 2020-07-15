package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.BannerAllAgent;
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
public interface BannerAllAgentDao {

    @SelectProvider(type=BannerAllAgentDao.SqlProvider.class,method="selectAllList")
    @ResultType(BannerAllAgent.class)
    List<BannerAllAgent> selectAllList(@Param("banner") BannerAllAgent banner, @Param("page") Page<BannerAllAgent> page);

    @Insert(
            "INSERT INTO pa_banner " +
                    " (apply_type,oem_no,banner_name,up_time,down_time," +
                    "  show_no,status,img_url,link,remark,postion_type," +
                    "  create_time,last_update) " +
                    " VALUES " +
                    " (#{banner.applyType},#{banner.oemNo},#{banner.bannerName},#{banner.upTime},#{banner.downTime}," +
                    "  #{banner.showNo},'0',#{banner.imgUrl},#{banner.link},#{banner.remark},#{banner.postionType}," +
                    "  NOW(),NOW() ) "
    )
    int addBanner(@Param("banner") BannerAllAgent banner);

    @Select(
            "select * from pa_banner where id=#{id}"
    )
    BannerAllAgent getBanner(@Param("id") long id);

    @Update(
            "update pa_banner set " +
                    " apply_type=#{banner.applyType},oem_no=#{banner.oemNo},banner_name=#{banner.bannerName}, " +
                    " up_time=#{banner.upTime},down_time=#{banner.downTime},show_no=#{banner.showNo}, " +
                    " img_url=#{banner.imgUrl},link=#{banner.link},remark=#{banner.remark},postion_type=#{banner.postionType} " +
                    " where id=#{banner.id}"
    )
    int updateBanner(@Param("banner") BannerAllAgent banner);

    @Update(
            "update pa_banner set " +
                    " apply_type=#{banner.applyType},oem_no=#{banner.oemNo},banner_name=#{banner.bannerName}, " +
                    " up_time=#{banner.upTime},down_time=#{banner.downTime},show_no=#{banner.showNo}, " +
                    " link=#{banner.link},remark=#{banner.remark},postion_type=#{banner.postionType} " +
                    " where id=#{banner.id}"
    )
    int updateBannerNoImg(@Param("banner") BannerAllAgent banner);

    @Update(
            "update pa_banner set status='2' where id=#{id}"
    )
    int deleteBanner(@Param("id") long id);

    @Update(
            "update pa_banner set status=#{state} where id=#{id}"
    )
    int closeBanner(@Param("id") long id, @Param("state") String state);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final BannerAllAgent banner = (BannerAllAgent) param.get("banner");
            return new SQL(){{
                SELECT("banner.*,oem.brand_name oemName");
                FROM("pa_banner banner");
                LEFT_OUTER_JOIN("pa_brand oem ON oem.brand_code=banner.oem_no");
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
                if(StringUtils.isNotBlank(banner.getPostionType())){
                    WHERE("banner.postion_type = #{banner.postionType} ");
                }
                ORDER_BY("banner.show_no ASC");
                ORDER_BY("banner.last_update DESC");
            }}.toString();
        }
    }
}
