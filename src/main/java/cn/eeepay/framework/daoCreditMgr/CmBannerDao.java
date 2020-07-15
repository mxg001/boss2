package cn.eeepay.framework.daoCreditMgr;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBannerInfo;

public interface CmBannerDao {

	/**
	 * Banner查询
	 * @author	mays
	 * @date	2018年3月29日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectInfo")
	@ResultType(CmBannerInfo.class)
	List<CmBannerInfo> selectInfo(@Param("page") Page<CmBannerInfo> page, @Param("info") CmBannerInfo info);

	/**
	 * 新增Banner
	 * @author	mays
	 * @return 
	 * @date	2018年3月30日
	 */
	@Insert("INSERT INTO cm_banner_info (org_id,pic_url,banner_title,show_no,begin_time,end_time,status,banner_url,remark,position_type,pic_name) "
			+ "VALUES (#{info.orgId},#{info.picUrl},#{info.bannerTitle},#{info.showNo},#{info.beginTime},"
			+ "#{info.endTime},#{info.status},#{info.bannerUrl},#{info.remark},#{info.positionType},#{info.picName})")
	int addBanner(@Param("info") CmBannerInfo info);

	/**
	 * 根据id查询Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	@Select("select * from cm_banner_info where id = #{id}")
	@ResultType(CmBannerInfo.class)
	CmBannerInfo queryBannerById(@Param("id") String id);

	/**
	 * 修改Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateBanner")
	int updateBanner(@Param("info") CmBannerInfo info);

	/**
	 * 根据id删除Banner
	 * @author	mays
	 * @date	2018年4月2日
	 */
	@Delete("delete from cm_banner_info where id = #{id}")
	int delBannerById(@Param("id") String id);

	/**
	 * 修改Banner状态
	 * @author	mays
	 * @date	2018年4月2日
	 */
	@Update("update cm_banner_info set status = #{status} where id = #{id}")
	int updateBannerStatus(@Param("id") String id, @Param("status") String status);

	public class SqlProvider {

		public String selectInfo(Map<String, Object> param) {
			final CmBannerInfo info = (CmBannerInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cbi.*,case when cbi.org_id='-1' then '所有组织' else coi.org_name end org_name");
					FROM("cm_banner_info cbi");
					LEFT_OUTER_JOIN("cm_org_info coi ON coi.org_id = cbi.org_id");
					if (StringUtils.isNotBlank(info.getOrgId())) {
						WHERE("cbi.org_id = #{info.orgId} ");
					}
					if (info.getPositionType() != null) {
						WHERE("cbi.position_type = #{info.positionType} ");
					}
					if (StringUtils.isNotBlank(info.getBannerTitle())) {
						WHERE("cbi.banner_title = #{info.bannerTitle} ");
					}
					if (info.getId() != null) {
						WHERE("cbi.id = #{info.id} ");
					}
					if (info.getStatus() != null) {
						WHERE("cbi.status = #{info.status} ");
					}
					ORDER_BY("cbi.id desc");
				}
			};
			return sql.toString();
		}

		public String updateBanner(Map<String, Object> param){
	    	 StringBuffer sql = new StringBuffer("");
	    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	 final CmBannerInfo info = (CmBannerInfo) param.get("info");
	    	 sql.append("update cm_banner_info set org_id='" + info.getOrgId());
	    	 sql.append("',position_type='" + info.getPositionType());
	    	 sql.append("',banner_title='" + info.getBannerTitle());
	    	 sql.append("',show_no='" + info.getShowNo());
	    	 sql.append("',begin_time='" + sdf.format(info.getBeginTime()));
	    	 sql.append("',end_time='" + sdf.format(info.getEndTime()));
	    	 sql.append("',banner_url='" + info.getBannerUrl());
	    	 sql.append("',remark='" + info.getRemark());
	         if(StringUtils.isNotBlank(info.getPicName())){
	        	 sql.append("',pic_name='" + info.getPicName());
	        	 sql.append("',pic_url='" + info.getPicUrl());
	         }
	         sql.append("' where id=" + info.getId());
	         return sql.toString();
	     }

	}

}