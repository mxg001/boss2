package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.AreaInfo;

/**
 * 地区信息dao
 * 
 * @author junhu
 *
 */
/**
 * @author 沙
 *
 */
public interface AreaInfoDao {
	/**
	 * 获取所有省份
	 * 
	 * @return
	 */
	@Select("select * from area_info where parent_id = 0")
	List<Map<String,Object>> provinceSelectBox();

	/**
	 * 获取所有省份
	 *
	 * @return
	 */
	@Select("select * from area_info where id = #{id} ")
	Map<String,Object> getProvincebyId(int id);


	/**
	 * 根据省份查询该省份下的所有地级市
	 * 
	 * @param province
	 * @return
	 */
	@Select("select city.* from area_info city LEFT JOIN area_info province on city.parent_id=province.id where province.name = #{province}")
	List<Map<String,Object>> citySelectBox(String province);
	
	/**
	 * 三级行政区域
	 */
	@Select("select *  from area_info where parent_id =#{pid}")
	List<Map<String,Object>> getItemsByParentId(Integer pid);

	/**
	 *
	 * @param pids 1,2,3
 	 * @return
	 */
	@Select("select *  from area_info where parent_id in (${pids}) order by sft_code")
	List<Map<String,Object>> getItemsByParentIds(@Param("pids")String pids);
	/**
	 * 通过名称查询
	 * @param province
	 * @param city
	 * @param area
	 * @return
	 */
	@Select("SELECT p.name pname,p.id pid,c.name cname,c.id cid,a.name aname,a.id aid FROM area_info p LEFT JOIN area_info c ON c.parent_id = p.id"
			+ " LEFT JOIN area_info a ON a.parent_id = c.id WHERE p.level=1 and p.name=#{province} and"
			+ " c.name=#{city} and a.name=#{area}")
	Map<String, Object> getAreaByNames(@Param("province")String province,@Param("city")String city,@Param("area")String area);
	
	@Select("SELECT a.* FROM area_info a LEFT JOIN area_info b ON a.parent_id=b.id WHERE b.name=#{name}")
	List<Map<String,Object>> getItemsByName(String name);
}
