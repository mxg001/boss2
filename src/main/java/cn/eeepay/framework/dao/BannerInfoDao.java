package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.BannerInfo;

public interface BannerInfoDao {

	@Update("update banner_info set banner_status=1,offline_time=null where banner_id=#{bannerId}")
	int openStatus(@Param("bannerId")Long bannerId);
	
	@Update("update banner_info set banner_status=0 where banner_id=#{bannerId}")
	int closeStatus(@Param("bannerId")Long bannerId);
	
	/**
	 * 根据ID查询banner详情
	 * @param id
	 * @return
	 */
	@Select("select b.*,a.team_name,a.app_name from banner_info b left join app_info a on b.app_no=a.app_no "
			+ "  where b.banner_id=#{id}")
	@ResultType(BannerInfo.class)
	BannerInfo selectDetailById(@Param("id")String id);
	
	@Update("update banner_info set banner_name=#{banner.bannerName},weight=#{banner.weight},online_time=#{banner.onlineTime},"
			+ "offline_time=#{banner.offlineTime},team_id=#{banner.teamId},agent_no=#{banner.agentNo},banner_content=#{banner.bannerContent},"
			+ "banner_status=#{banner.bannerStatus},banner_attachment=#{banner.bannerAttachment},banner_link=#{banner.bannerLink},app_no=#{banner.appNo},"
			+ "banner_position=#{banner.bannerPosition} "
			+ "where banner_id=#{banner.bannerId}")
	int update(@Param("banner")BannerInfo banner);

	@Insert("insert into banner_info(banner_id,banner_name,weight,online_time,offline_time,team_id,agent_no,banner_content,"
			+ "banner_status,banner_attachment,banner_link,app_no,banner_position) values(#{banner.bannerId},#{banner.bannerName},#{banner.weight},"
			+ "#{banner.onlineTime},#{banner.offlineTime},#{banner.teamId},#{banner.agentNo},#{banner.bannerContent},"
			+ "#{banner.bannerStatus},#{banner.bannerAttachment},#{banner.bannerLink},#{banner.appNo},#{banner.bannerPosition})")
	int insert(@Param("banner")BannerInfo banner);

	@Select("select * from app_info")
	List<AppInfo> getAppInfo();

	@Update("update banner_info set banner_status=3 where banner_id=#{id}")
	int deleteBanner(Integer id);
	
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(BannerInfo.class)
	List<BannerInfo> selectByCondition(@Param("banner")BannerInfo bannerInfo,Page<BannerInfo> page);
	
	public class SqlProvider{
		public String selectByCondition(Map<String, Object> param){
			final BannerInfo banner = (BannerInfo)param.get("banner");
			return new SQL(){{
				SELECT("b.*,a.app_name");
				FROM("banner_info b left join app_info a on b.app_no=a.app_no");
				if(banner.getBannerName()!=null && StringUtils.isNotBlank(banner.getBannerName())){
					banner.setBannerName(banner.getBannerName()+"%");
					WHERE("b.banner_name like #{banner.bannerName}");
				}
				if(banner.getBannerId() != null){
					WHERE("b.banner_id=#{banner.bannerId}");
				}
				if(banner.getBannerStatus() != null && !(banner.getBannerStatus()==2)){
					WHERE("b.banner_status=#{banner.bannerStatus}");
				}
				if(StringUtils.isNotBlank(banner.getAppNo())){
					WHERE("b.app_no=#{banner.appNo}");
				}
				if(banner.getBannerPosition()!=null && banner.getBannerPosition()!=-1){
					WHERE("b.banner_position=#{banner.bannerPosition}");
				}
				WHERE("b.banner_status != 3");		//状态为3的，表示已删除
				ORDER_BY(" IF ((isnull(b.online_time) AND isnull(b.offline_time)), 0, 1) ASC ");
				ORDER_BY(" IF ((isnull(b.online_time) AND !isnull(b.offline_time)), 0, 1) ASC ");
				ORDER_BY(" IF (isnull(b.offline_time), 0, 1) ASC ");
				ORDER_BY(" b.offline_time DESC  ");
				ORDER_BY(" b.online_time DESC ");
				ORDER_BY(" b.banner_id DESC ");
			}}.toString();
		}
		
	}
	
}
