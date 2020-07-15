package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.AddRequireItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface BusinessRequireItemDao {

	@Select("SELECT br_id FROM business_require_item WHERE bp_id=#{bpId} ORDER BY id")
	@ResultType(java.lang.String.class)
	List<String> findByProduct(@Param("bpId") String bpId);
	
	@Insert("INSERT INTO business_require_item(bp_id,br_id) values (#{map.bpId},#{map.itemId})")
	int insert(@Param("map")Map<String, String> map);

	@Delete("DELETE FROM business_require_item WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId") String id);
	
	@Delete("DELETE FROM business_require_item WHERE bp_id=#{bpId} and br_id=#{itemId}")
	int deleteProductByItemAndbpId(@Param("bpId") String bpId,@Param("itemId") String itemId);
	
	@Insert("INSERT INTO business_require_item(bp_id,br_id) values (#{bpId},#{itemId})")
	int insertProductByItemAndbpId(@Param("bpId") String bpId,@Param("itemId") String itemId);

	@Select("SELECT item_id,item_name FROM add_require_item WHERE item_id in (SELECT br_id FROM business_require_item WHERE bp_id=#{bpId})")
	@ResultType(List.class)
    List<AddRequireItem> findBusinessRequireItem(@Param("bpId") Long bpId);
}