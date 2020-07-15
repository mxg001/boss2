package cn.eeepay.framework.daoSuperbank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedOrg;
import cn.eeepay.framework.model.RedOrgSort;

public interface RedOrgSortDao {

	/**
	 * 查询红包业务组织分类排序
	 * @param redOrgSort
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class, method="selectRedOrgSort")
    @ResultType(RedOrgSort.class)
	List<RedOrgSort> selectRedOrgSort(@Param("redOrgSort")RedOrgSort redOrgSort, @Param("page")Page<RedOrgSort> page);

	/**
	 * 删除红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
	@DeleteProvider(type=SqlProvider.class, method="deleteRedOrgSort")
	int deleteRedOrgSort(@Param("redOrgSort")RedOrgSort redOrgSort);

	/**
	 * 新增红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
    @Insert("insert into red_org_sort(bus_code,org_id,sort_num,category,create_time,update_time,operator)" +
            " values(#{busCode},#{orgId},#{sortNum},#{category},now(),now(),#{operator})")
	int insertRedOrgSort(RedOrgSort redOrgSort);

	/**
	 * 更新红包业务组织分类排序
	 * @param redOrgSort
	 * @return
	 */
    @Update("update red_org_sort set bus_code = #{busCode},org_id = #{orgId},sort_num = #{sortNum},"
    		+ "category = #{category},update_time = now() where id=#{id}")
	int updateRedOrgSort(RedOrgSort redOrgSort);

    class SqlProvider{
        public String selectRedOrgSort(Map<String, Object> param){
        	RedOrgSort redOrgSort = (RedOrgSort) param.get("redOrgSort");
            SQL sql = new SQL();
            sql.SELECT("id,bus_code,org_id,sort_num,category,create_time,update_time,operator");
            sql.FROM("red_org_sort");
            if(redOrgSort != null){
                if(StringUtils.isNotBlank(redOrgSort.getBusCode())){
                    sql.WHERE("bus_code=#{redOrgSort.busCode}");
                }
                if(null != redOrgSort.getOrgId()){
                    sql.WHERE("org_id=#{redOrgSort.orgId}");
                }
                if(StringUtils.isNotBlank(redOrgSort.getCategory())){
                    sql.WHERE("category=#{redOrgSort.category}");
                }
                if(null != redOrgSort.getId()){
                    sql.WHERE("id=#{redOrgSort.id}");
                }
            }
            sql.ORDER_BY("update_time desc");
            return sql.toString();
        }
        
        public String deleteRedOrgSort(Map<String, Object> param){
        	RedOrgSort redOrgSort = (RedOrgSort) param.get("redOrgSort");
            SQL sql = new SQL();
            sql.DELETE_FROM("red_org_sort");
            if(redOrgSort != null){
                if(StringUtils.isNotBlank(redOrgSort.getBusCode())){
                    sql.WHERE("bus_code=#{redOrgSort.busCode}");
                }
                if(StringUtils.isNotBlank(redOrgSort.getOrgIds())){
                    sql.WHERE("org_id in (" + redOrgSort.getOrgIds() + ")");
                }
                if(StringUtils.isNotBlank(redOrgSort.getIds())) {
                	sql.WHERE("id in ("+redOrgSort.getIds()+")");
                }
            }
            return sql.toString();
        }
    }
}
