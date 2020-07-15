package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.TerminalInfo;

public interface AddRequireItemDao {

    @Select("SELECT * from add_require_item where item_id=#{meriId}")
    @ResultType(AddRequireItem.class)
    AddRequireItem selectByMeriId(@Param("meriId")String meriId);
    
    @Select("select item_id,item_name from add_require_item where item_name like #{itemName}")
    @ResultType(AddRequireItem.class)
	List<AddRequireItem> selectByName(Page<AddRequireItem> page, @Param("itemName")String params);

	@Select("SELECT * FROM add_require_item WHERE item_id=#{id}")
	@ResultType(AddRequireItem.class)
	AddRequireItem selectById(@Param("id")String id);
	
	@Update("UPDATE add_require_item SET item_name=#{item.itemName},example_type=#{item.exampleType},"
			+ "example=#{item.example},photo=#{item.photo},photo_address=#{item.photoAddress},"
			+ "remark=#{item.remark},check_status=#{item.checkStatus},check_msg=#{item.checkMsg} WHERE item_id=#{item.itemId}")
	int update(@Param("item")AddRequireItem item);

	@Insert("INSERT INTO add_require_item(item_id,item_name,example_type,example,photo,photo_address,remark,check_status,check_msg) "
			+ "values (#{item.itemId},#{item.itemName},#{item.exampleType},#{item.example},#{item.photo},#{item.photoAddress},"
			+ "#{item.remark},#{item.checkStatus},#{item.checkMsg})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="item.itemId", before=false, resultType=Long.class)  
	int insert(@Param("item")AddRequireItem item);

	@Select("SELECT item_id,item_name FROM add_require_item WHERE item_id=#{id}")
	@ResultType(AddRequireItem.class)
	AddRequireItem selectRequireName(@Param("id")String id);

	@Select("SELECT item_id,item_name FROM add_require_item")
	@ResultType(AddRequireItem.class)
	List<AddRequireItem> selectAllRequireName();
	
	
	@Select("SELECT ari.* FROM business_require_item bri,add_require_item ari WHERE ari.item_id = bri.br_id and bri.bp_id=#{bpId}")
	@ResultType(AddRequireItem.class)
	List<AddRequireItem> selectItemByBpId(@Param("bpId")String bpId);


	@Select("SELECT item_id,item_name FROM add_require_item")
	@ResultType(AddRequireItem.class)
	List<AddRequireItem> queryItemNameList(Page<TerminalInfo> page);
	
//	public class SqlProvider{
//		public String selectByName(String params){
//			
////			final String itemName = (String)map.get("params");
//			final String itemName = params;
//			return new SQL(){{
//				SELECT("item_id,item_name");
//				FROM("add_require_item");
//				if(StringUtils.isNotBlank(itemName)){
//					WHERE("item_name like #{itemName}");
//				}
//			}}.toString();
//		}
//	}
}